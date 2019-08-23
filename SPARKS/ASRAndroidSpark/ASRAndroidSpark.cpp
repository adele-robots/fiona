/// @file ASRAndroidSpark.cpp
/// @brief ASRAndroidSpark class implementation.

// Necesita el audio en pcm lineal 16 bit signed 8khz 64Kbps

#include "stdAfx.h"
#include "ASRAndroidSpark.h"
#define SAVE_TO_FILE
#define limit 5
//#define PROB 50
#include <sstream>
#include <algorithm>
extern "C" {
	#include <errno.h>
	#include <sys/stat.h>
	#include <libgen.h>
	#include <sys/types.h>
	#include <unistd.h>
}

#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
)
{
	if (!strcmp(componentType, "ASRAndroidSpark")) {
		return new ASRAndroidSpark(componentInstanceName, componentSystem);
	}
	else {
		return NULL;
	}
}
#endif

/* Function with behaviour like `mkdir -p'  */
int
mkpath(const char *s, mode_t mode){
        char *q, *r = NULL, *path = NULL, *up = NULL;
        int rv;

        rv = -1;
        if (strcmp(s, ".") == 0 || strcmp(s, "/") == 0)
                return (0);

        if ((path = strdup(s)) == NULL)
                exit(1);

        if ((q = strdup(s)) == NULL)
                exit(1);

        if ((r = dirname(q)) == NULL)
                goto out;

        if ((up = strdup(r)) == NULL)
                exit(1);

        if ((mkpath(up, mode) == -1) && (errno != EEXIST))
                goto out;

        if ((mkdir(path, mode) == -1) && (errno != EEXIST))
                rv = -1;
        else
                rv = 0;

out:
        if (up != NULL)
                free(up);
        free(q);
        free(path);
        return (rv);
}

/// Initializes ASRAndroidSpark component.
void ASRAndroidSpark::init(void){
	int audioQueueSize;
	try {
		audioQueueSize = getComponentConfiguration()->getInt(const_cast<char*>("AudioQueueSizeBytes"));
	}
	catch(Exception & e) {
		audioQueueSize = 44100;
	}
	audioQueue.init(audioQueueSize);
	noiseVolumeThreshold = getComponentConfiguration()->getFloat(const_cast<char*>("Noise_Threshold"));
	silence = getComponentConfiguration()->getFloat(const_cast<char*>("Silence_Time"));
	sendData = false;
	url = getComponentConfiguration()->getString(const_cast<char*>("Server_Url"));
	userDirectory = getComponentConfiguration()->getString(const_cast<char*>("User_Spark_Data"));
	int status = mkpath(userDirectory.c_str(), S_IRWXU | S_IRGRP | S_IXGRP | S_IROTH | S_IXOTH);
	if(status == -1 && errno != EEXIST)
		LoggerError("[ASRAndroidSpark] Error creating directory %s: %d %s", userDirectory.c_str(), errno, strerror(errno));

	firstBufferQueued = true;

	FRAME_SIZE = 160;
	SAMPLING_RATE = 8000;
	PROB = getComponentConfiguration()->getInt(const_cast<char*>("Probability_Threshold"));
	NOISE_SUPPRESS = getComponentConfiguration()->getInt(const_cast<char*>("Noise_Suppress"));

	preprocess = speex_preprocess_state_init(FRAME_SIZE, SAMPLING_RATE);
	int on = 1;
	speex_preprocess_ctl(preprocess, SPEEX_PREPROCESS_SET_VAD, &on);
	if(NOISE_SUPPRESS != 0) {
		on = 1;
		speex_preprocess_ctl(preprocess, SPEEX_PREPROCESS_SET_DENOISE, &on);
	}

	speex_preprocess_ctl(preprocess, SPEEX_PREPROCESS_SET_PROB_START, &PROB);
	//LoggerInfo("[ASRAndroidSpark] PROB_START: %d", PROB);
	speex_preprocess_ctl(preprocess, SPEEX_PREPROCESS_SET_PROB_CONTINUE, &PROB);
	//LoggerInfo("[ASRAndroidSpark] PROB_CONTINUE: %d", PROB);
	if(NOISE_SUPPRESS != 0) {
		speex_preprocess_ctl(preprocess, SPEEX_PREPROCESS_SET_NOISE_SUPPRESS, &NOISE_SUPPRESS);
		//LoggerInfo("[ASRAndroidSpark] NOISE_SUPPRESS: %d", NOISE_SUPPRESS);
	}

	/*preprocess = NULL;*/

	watcher.restart();
}

/// Unitializes the ASRSpark component.
void ASRAndroidSpark::quit(void){
	audioQueue.quit();
	while(!cola_inicio.empty()) {
		free(cola_inicio.front().first);
		cola_inicio.pop();
	}
	/*if(preprocess)*/
		speex_preprocess_state_destroy(preprocess);
}


// Provided interface implementation
// IFlow<AudioWrap *> implementation

void ASRAndroidSpark::processData(AudioWrap * audio){

	int sampleCount = audio->bufferSizeInBytes / 2;

	/*initPreprocess(FRAME_SIZE);*/
	// 1 FRAME = 20 ms
	for(int i = 0; i < sampleCount; i = i + FRAME_SIZE) {
		int samplesToPreProcess = std::min(sampleCount - i, FRAME_SIZE);
		/*if(samplesToPreProcess < FRAME_SIZE)
			initPreprocess(samplesToPreProcess);*/
		// TODO: en teoria speex_preprocess_run sobreescribe la memoria que le pasamos, asi que si
		// FRAME_SIZE > sampleCount - i       estaria escribiendo donde no debe, aunque de momento es multiplo, asi que todo OK.
		if(FRAME_SIZE > samplesToPreProcess)
			LoggerWarn("[ASRAndroidSpark] FRAME_SIZE=%d, samplesToPreProcess=%d", FRAME_SIZE, samplesToPreProcess);
		// Si VAD no detecta speech, quitamos todo el audio del frame
		if(!speex_preprocess_run(preprocess, (short int *)(audio->audioBuffer + i)))
			memset(audio->audioBuffer + i, 0, samplesToPreProcess * sizeof(short));
		/*// Si VAD dice que la probabilidad de que sea speech es menor de PROB, quitamos el audio del frame, igual que lo de arriba
		int prob = 0;
		speex_preprocess_ctl(preprocess, SPEEX_PREPROCESS_GET_PROB, &prob);
		//LoggerInfo("PROB: %d", prob);
		if(prob < PROB)
			memset(audio->audioBuffer + i, 0, samplesToPreProcess * sizeof(short));*/
	}
	bool voiceDetected = false;

	for (int i = 0; i<sampleCount; i++)
	{
			if (abs(audio->audioBuffer[i]) > std::numeric_limits<int16_t>::max() * noiseVolumeThreshold)
			{
				voiceDetected = true;
				break;
			}
	}
	// Si detecto voz
	if(voiceDetected) {
		if(!firstBufferQueued) {
			while(!cola_inicio.empty()) {
				audioQueue.queueAudioBuffer((uint8_t *)cola_inicio.front().first, cola_inicio.front().second);
				free(cola_inicio.front().first);
				cola_inicio.pop();
			}
			firstBufferQueued = true;
		}
		audioQueue.queueAudioBuffer((uint8_t *)audio->audioBuffer, audio->bufferSizeInBytes);
		watcher.restart();
	}
	else
		// Si pasa mas de silence tiempo desde la ultima vez que se encontro voz
		if(audioQueue.getStoredAudioSize() > 0 && watcher.elapsedTime() >= silence) {
			doRequest();
			watcher.restart();
		}
		else
			// Si no detecto voz en este trozo pero esta siendo detectada
			if(audioQueue.getStoredAudioSize() > 0)
				audioQueue.queueAudioBuffer((uint8_t *)audio->audioBuffer, audio->bufferSizeInBytes);
			// Si no esta siendo detectada, almacenamos el ultimo trozo que nos llega para enviar el inicio
			else {
				int16_t * ptr = (int16_t *)malloc(audio->bufferSizeInBytes);
				if(ptr != NULL) {
					if(cola_inicio.size() >= limit) {
						free(cola_inicio.front().first);
						cola_inicio.pop();
					}
					memcpy(ptr, audio->audioBuffer, audio->bufferSizeInBytes);
					cola_inicio.push(pair<int16_t*, size_t>(ptr, audio->bufferSizeInBytes));
				}
				else {
				}
				firstBufferQueued = false;
			}
}

void ASRAndroidSpark::doRequest(){

	// Get audio size and allocate buffer
	size = audioQueue.getStoredAudioSize();
	LoggerInfo("[ASRAndroidSpark] Request sent with size: %d", size);
	uint8_t buffer[size];


	// Dequeue audio sample
	audioQueue.dequeueAudioBuffer(buffer, size);
//#ifdef SAVE_TO_FILE
	static int i = 0;
	stringstream ss;
	ss << userDirectory << "audio" << ++i << "_" << getpid() << ".esa";
	string filename;
	ss >> filename;
	FILE * file = fopen(filename.c_str(), "wb");
	fwrite (buffer , 1 , size, file );
	fclose(file);
//#endif

	// Send the request
	try
    {
		curlpp::Cleanup cleaner;
		curlpp::Easy request;
		/*request.setOpt(curlpp::options::Verbose(true));
		request.setOpt(curlpp::options::DebugFunction(curlpp::types::DebugFunctionFunctor(this, &writeDebug)));*/
		curlpp::types::WriteFunctionFunctor functor(this, &WriteCallback);
		curlpp::options::WriteFunction *test = new curlpp::options::WriteFunction(functor);
		request.setOpt(test);
		request.setOpt(new curlpp::options::Url(url));

		// Forms takes ownership of pointers!
		curlpp::Forms formParts;
		//formParts.push_back(new curlpp::FormParts::Content("fichero", buffer, "audio/wav"));
		formParts.push_back(new curlpp::FormParts::File("fichero", filename.c_str(), "audio/wav"));
		request.setOpt(new curlpp::options::HttpPost(formParts));

		request.perform();

#ifndef SAVE_TO_FILE
		remove(filename.c_str());
#endif
    }
	catch(curlpp::LogicError& e )
	{
    	LoggerError("Error making request : %s", e.what());
	}
    catch(curlpp::RuntimeError& e)
    {
    	LoggerError("Error making request : %s", e.what());
    }
}

int ASRAndroidSpark::writeDebug(curl_infotype, char * text, size_t)
{
	LoggerInfo("[DEBUG] %s", text);
	return 0;
}

size_t ASRAndroidSpark::WriteCallback(char* ptr, size_t size, size_t nmemb)
{
	// Calculate the real size of the incoming buffer
	size_t realsize = size * nmemb;

	LoggerInfo("[ASRAndroidSpark] %s", ptr);
	myCharFlow->processData(ptr);

	// return the real size of the buffer...
	return realsize;
}


/*void ASRAndroidSpark::initPreprocess(int frame_size) {
	if(preprocess)
		speex_preprocess_state_destroy(preprocess);
	preprocess = speex_preprocess_state_init(frame_size, SAMPLING_RATE);
	int on = 1;
	speex_preprocess_ctl(preprocess, SPEEX_PREPROCESS_SET_VAD, &on);
	on = 1;
	speex_preprocess_ctl(preprocess, SPEEX_PREPROCESS_SET_DENOISE, &on);
}*/
