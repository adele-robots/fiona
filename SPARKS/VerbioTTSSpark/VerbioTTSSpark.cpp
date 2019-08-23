/*
 * VerbioTTSSpark.cpp
 *
 *  Created on: 10/07/2013
 *      Author: guille
 */


/// @file VerbioTTSSpark.cpp
/// @brief VerbioTTSSpark class implementation.

#include "stdAfx.h"
#include "VerbioTTSSpark.h"


#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "VerbioTTSSpark")) {
			return new VerbioTTSSpark(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif

bool newMessage = false;

// Useful function to split a string by a delimiter
void split(string str, vector<string> & tokens, char delim){
	istringstream f(str);
	string s;
	while (getline(f, s, delim)) {
		tokens.push_back(s);
	}
}

/// Initializes VerbioTTSSpark component.
void VerbioTTSSpark::init(void){
	// Initialize parameters
	dev = 0;

	//FFMPEG initialization
	avcodec_register_all();

	//Set the queue size (in samples, 1 byte/sample)
	try {
		audioQueueSize = getComponentConfiguration()->getInt(const_cast<char *>("AudioQueueSizeBytes"));
	} catch(::Exception &e){
		LoggerWarn("[FIONA-logger]Default size: 44100");
		audioQueueSize = 44100;
	}
	audioQueue.init(audioQueueSize);

	colaMensajes = new std::queue<std::string>();

	initializeVerbio();

	pthread_cond_init(&cond_full, NULL);
	pthread_mutex_init(&mutex, NULL);
}

/// Unitializes the VerbioTTSSpark component.
void VerbioTTSSpark::quit(void){
	// Free resources and connection with Speech server
	vox_libclose();

	delete colaMensajes;
}

//IThreadProc implementation
void VerbioTTSSpark::process() {
	// Waiting for messages
	pthread_mutex_lock(&mutex);
	if(colaMensajes->empty())
		pthread_cond_wait(&cond_full, &mutex);
	string text = colaMensajes->front();
	synthesize(text.c_str());
	colaMensajes->pop();
	pthread_mutex_unlock(&mutex);
}

void VerbioTTSSpark::processData(char *prompt) {
	//LoggerInfo("[FIONA-logger]VerbioTTSSpark::processData, me llega el texto: %s", prompt);
	colaMensajes->push(string(prompt));
	newMessage = true;
	pthread_cond_signal(&cond_full);
}

int VerbioTTSSpark::getStoredAudioSize() {
	return audioQueue.storedAudioSize;
}

void VerbioTTSSpark::queueAudioBuffer(char *buffer, int size) {
	audioQueue.queueAudioBuffer(buffer, size);
}

void VerbioTTSSpark::dequeueAudioBuffer(char *buffer, int size) {
	audioQueue.dequeueAudioBuffer(buffer,size);
}

void VerbioTTSSpark::reset() {
	audioQueue.reset();
}

void VerbioTTSSpark::initializeVerbio() {
	LoggerInfo("[FIONA-logger]Initializing Verbio...");
	int server_rate;
	char * speakers_info;

	// Get config params
	int valid_sample_rate = getComponentConfiguration()->getInt(const_cast<char *>("AudioVideoConfig_Remote_AvatarStream_AudioSampleRate"));
	int verbio_rate = getComponentConfiguration()->getInt(const_cast<char *>("Frequency"));
	string server = getComponentConfiguration()->getString(const_cast<char *>("Synthesis_Speech_Server"));
	string voice = getComponentConfiguration()->getString(const_cast<char *>("Voice"));

	// Prepare the context for the resampling (ffmpeg utility)
	ctx = audio_resample_init(1, 1, valid_sample_rate, verbio_rate);

	// Set the recognition/synthesis speech server
	if (vox_setparm(-1, VXGB_DEFSERVER, server.c_str()) == -1) {
		LoggerError("[FIONA-logger]Failed to set server");
		ERR("Failed to set server");
	}
	// Initializes Verbio's speech synthesis library
	if (vox_tts_init(NULL, "es") == -1) {
		LoggerError("[FIONA-logger]Failed to initialize vox_tts");
		ERR("Failed to initialize vox_tts");
	}
	// Get speakers info
	if (vox_getparm(dev, VXGB_TTSSPKINFO, &speakers_info) == -1) {
		LoggerError("[FIONA-logger]Failed to get speakers info");
		ERR("Failed to get speakers info");
	}

	string speakers_info_str(speakers_info);
	vector<string> speaker_props;

	// and get the voice's language
	size_t found = speakers_info_str.find(voice);
	if (found!=std::string::npos){
		// Ugly hack to solve galician voice irregular structure
		if (voice != "Freire"){
			// get only current speaker info
			string str = speakers_info_str.substr(found);
			size_t pos = str.find(";");
			string speaker_info = str.substr(0,pos);
			// Split the string into its properties
			split(speaker_info,speaker_props,':');

			LoggerInfo("[FIONA-logger]Voice '%s' has '%s' idiom",voice.c_str(),speaker_props[3].c_str());
			// Set language
			if (vox_setparm(dev, VXCH_DEFTTSLANG, speaker_props[3].c_str()) == -1) {
				LoggerError("[FIONA-logger]Failed to set language");
				ERR("Failed to set language");
			}
		}else{
			// Set language
			if (vox_setparm(dev, VXCH_DEFTTSLANG, "ga") == -1) {
				LoggerError("[FIONA-logger]Failed to set language");
				ERR("Failed to set language");
			}
		}
	}
	else {
		LoggerInfo("[FIONA-logger]Voice supplied not found");
		ERR("[FIONA-logger]Voice supplied not found");
	}

	// Set voice
	if (vox_setparm(dev, VXCH_TTSSPKNAME, voice.c_str()) == -1) {
		LoggerError("[FIONA-logger]Failed to set voice");
		ERR("Failed to set voice");
	}
	// Get the synthesis engine's working frequency
	if (vox_getparm(dev, VXCH_TTSFREQUENCY, &server_rate) == -1) {
		LoggerError("[FIONA-logger]Failed to get synthesis engine's working frequency");
		ERR("Failed to get synthesis engine's working frequency");
	}
	// and compare with the frequency set in the config file
	if(server_rate != verbio_rate){
		LoggerError("[FIONA-logger]Synthesis engine and voice frequencies must match");
		ERR("Synthesis engine and voice frequencies must match");
	}
	// Get the synthesis engine's working frequency
	if (vox_getparm(dev, VXCH_TTSFREQUENCY, &server_rate) == -1) {
		LoggerError("[FIONA-logger]Failed to get synthesis engine's working frequency");
		ERR("Failed to get synthesis engine's working frequency");
	}
	LoggerInfo("[FIONA-logger]Registro la callback");
	// Establecer callback de síntesis para el canal 0
	//LoggerInfo("[FIONA-logger]Callback retorna: %d",vox_RegisterTTSCallback(dev, ntfy_event));
	//vox_RegisterTTSCallback(dev, ntfy_event);
}

//int VerbioTTSSpark::ntfy_event(const char *event, unsigned long offset, unsigned long dev){
//
//	LoggerInfo("[FIONA-logger][CALLBACK VISEMA] Vis: %s Offset: %.7f", event, (float)offset/8000);
//	return 0;
//}

void VerbioTTSSpark::synthesize(const char *text) {
	newMessage = false;
	long int playdev = -1;
	// This flag is needed to cut current synthesis if a new synthesis is launched
	bool primeraMuestra = true;

	if((playdev = vox_playstr_open(dev, text, MC_LIN16)) == -1) {
		LoggerError("[FIONA-logger]Failed to execute vox_playstr_open");
		ERR("Failed to execute vox_playstr_open");
	}
	do {
		bytes_recv = vox_playstr_read(playdev, buffer_in, BUFFER_SIZE);
		if (bytes_recv > 0) {
			// Clean the audio buffer queue in the first iteration receiving bytes
			if (primeraMuestra) {
				reset();
				primeraMuestra = false;
			}
			short * buffer = new short[bytes_recv];
			int new_size = audio_resample(ctx, buffer, buffer_in, bytes_recv/2);
			queueAudioBuffer((char *)buffer, new_size*2);
			delete buffer;
		}
	} while (bytes_recv == BUFFER_SIZE && newMessage == false);

	/* This chunk of synthesis is finished*/
	if (newMessage) {
		reset();
	}

	if(vox_playstr_close(dev, playdev) < 0) {
		LoggerError("[FIONA-logger]Failed to close the audio device");
		ERR("Failed to close the audio device");
	}
}
