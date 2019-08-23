/// @file ASRGoogleSpark.cpp
/// @brief ASRGoogleSpark class implementation.


#include "stdAfx.h"
#include "ASRGoogleSpark.h"
//#define SAVE_TO_FILE

#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
)
{
	if (!strcmp(componentType, "ASRGoogleSpark")) {
		return new ASRGoogleSpark(componentInstanceName, componentSystem);
	}
	else {
		return NULL;
	}
}
#endif

/// Initializes ASRGoogleSpark component.
void ASRGoogleSpark::init(void){
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
	APIKey = getComponentConfiguration()->getString(const_cast<char*>("APIKey"));
	lang = getComponentConfiguration()->getString(const_cast<char*>("Language"));
	userIp = getComponentConfiguration()->getString(const_cast<char*>("User_Ip"));
	sampleRate = getComponentConfiguration()->getInt(const_cast<char*>("Sample_Rate"));
	watcher.restart();
	//chunk.memory = (char*)malloc(1);  /* will be grown as needed by the realloc */
	//chunk.size = 0;    /* no data at this point */
	pthread_cond_init(&condition_send, NULL);
	pthread_mutex_init(&mutex, NULL);
}

/// Unitializes the ASRSpark component.
void ASRGoogleSpark::quit(void){
	audioQueue.quit();
	//free(chunk.memory);
}


// Provided interface implementation
// IFlow<AudioWrap *> implementation

void ASRGoogleSpark::processData(AudioWrap * audio){
	int sampleCount = audio->bufferSizeInBytes / 2;

	bool voiceDetected = false;

	for (int i = 0; i<sampleCount; i++)
	{
			if (abs(audio->audioBuffer[i]) > std::numeric_limits<int16_t>::max() * noiseVolumeThreshold)
			{
				voiceDetected = true;
				break;
			}
	}
	if(voiceDetected) {
		audioQueue.queueAudioBuffer((uint8_t *)audio->audioBuffer, audio->bufferSizeInBytes);
		watcher.restart();
	}
	else
		if(audioQueue.getStoredAudioSize() > 0 && watcher.elapsedTime() >= silence) {
			sendData = true;
			pthread_cond_signal(&condition_send);
			watcher.restart();
		}
}


// IConcurrent implementation
void ASRGoogleSpark::process() {
	pthread_mutex_lock(&mutex);
	if(!sendData){
		pthread_cond_wait(&condition_send, &mutex);
	}
	sendData = false;
	doRequest();
	pthread_mutex_unlock(&mutex);
}


size_t ASRGoogleSpark::WriteMemoryCallback(void *contents, size_t size, size_t nmemb, void *userp)
{
	ASRGoogleSpark * spark = (ASRGoogleSpark *) userp;
	size_t realsize = size * nmemb;
	/*struct MemoryStruct *mem = (struct MemoryStruct *)userp;

	mem->memory = (char*)realloc(mem->memory, mem->size + realsize + 1);
	if(mem->memory == NULL) {
		// out of memory!
		LoggerError("not enough memory (realloc returned NULL)\n");
		return 0;
	}
	memcpy(&(mem->memory[mem->size]), contents, realsize);
	mem->size += realsize;
	mem->memory[mem->size] = 0;*/

	//LoggerInfo("ASRGoogleSpark: %s size: %d", (char*)contents, realsize);
	if(realsize <= 14)
		return realsize;
	string value((char*)contents);
	string clave("{\"result\":[{\"alternative\":[{\"transcript\":\"");
	int tam = clave.size();
	if(value.find(clave) != string::npos)
		value=value.substr(tam, value.find("\"", tam) - tam);
	LoggerInfo("ASRGoogleSpark: %s", value.c_str());
	spark->myCharFlow->processData(const_cast<char*>(value.c_str()));

	return realsize;
}

void ASRGoogleSpark::doRequest(){

	// Get audio size and allocate buffer
	size = audioQueue.getStoredAudioSize();
	uint8_t buffer[size];

	// Dequeue audio sample
	audioQueue.dequeueAudioBuffer(buffer, size);
#ifdef SAVE_TO_FILE
	static int i = 0;
	stringstream ss;
	ss << "/tmp/audio" << ++i << ".esa";
	string filename;
	ss >> filename;
	FILE * file = fopen(filename.c_str(), "wb");
	fwrite (buffer , 1 , size, file );
	fclose(file);
#endif

	// Create de url
	std::string requestURI = "https://www.google.com/speech-api/v2/recognize?output=json&userIp=" + userIp + "&lang=" + lang + "&key=" + APIKey;

	// Send the request
	try
    {
		/* init */
		CURL * easyhandle = curl_easy_init();

		/* ulr */
		curl_easy_setopt(easyhandle, CURLOPT_URL, requestURI.c_str());

		/* post binary data */
		curl_easy_setopt(easyhandle, CURLOPT_POSTFIELDS, buffer);

		/* set the size of the postfields data */
		curl_easy_setopt(easyhandle, CURLOPT_POSTFIELDSIZE, size);

		/* pass our list of custom made headers */
		struct curl_slist *headers=NULL;
		string headersContent = string("Content-Type: audio/l16; rate=")+to_string(sampleRate)+string(";");
		headers = curl_slist_append(headers, headersContent.c_str());
		curl_easy_setopt(easyhandle, CURLOPT_HTTPHEADER, headers);

		/* callback function for receiving data */
		curl_easy_setopt(easyhandle, CURLOPT_WRITEFUNCTION, WriteMemoryCallback);
		curl_easy_setopt(easyhandle, CURLOPT_WRITEDATA, this);

		/* post away! */
		curl_easy_perform(easyhandle);

		/* free the header list */
		curl_slist_free_all(headers);
    }
    catch(curlpp::RuntimeError& e)
    {
    	LoggerError("Error making request : %s", e.what());
    }
}
