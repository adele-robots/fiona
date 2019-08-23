/*
 * FliteVoiceSpark.cpp
 *
 *  Created on: 17/10/2012
 *      Author: guille
 */


/// @file FliteVoiceSpark.cpp
/// @brief FliteVoiceSpark class implementation.


#include "stdAfx.h"
#include "FliteVoiceSpark.h"

#include <sstream>
#include <signal.h>

#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "FliteVoiceSpark")) {
			return new FliteVoiceSpark(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}

//Need this to set flite's voices
extern "C" cst_voice *register_cmu_us_slt(); //girl
extern "C" cst_voice *register_cmu_us_kal(); //boy
extern "C" cst_voice *register_cmu_us_awb(); //boy
extern "C" cst_voice *register_cmu_us_rms(); //boy

#endif

using namespace std;

/// Initializes FliteVoiceSpark component.
void FliteVoiceSpark::init(void){
	//signal((int) SIGTERM, signalHandler);
	//signal((int) SIGINT, signalHandler);

	//FFMPEG resample need this
	avcodec_register_all();
	//FLITE initialization
	flite_init();

	//Set the queue size (in samples, 1 byte/sample)
	try {
		audioQueueSize = getComponentConfiguration()->getInt(const_cast<char *>("AudioQueueSizeBytes"));
	} catch(::Exception &e){
		LoggerWarn("[FIONA-logger]Default size: 44100");
		audioQueueSize = 44100;
	}
	audioQueue.init(audioQueueSize);

	audioPacketSize = getGlobalConfiguration()->getInt(const_cast<char *>("AudioVideoConfig_Remote_AvatarStream_AudioPacketSize"));
	voiceRate = getComponentConfiguration()->getInt(const_cast<char *>("Flite_SampleRate"));// 16k | 8k
	sampleRate = getComponentConfiguration()->getInt(const_cast<char *>("Out_SampleRate"));
	char voz[256];
	const char * Voice = "Voice";
	getComponentConfiguration()->getString((char*)Voice, voz, 256);

	//set the voice
	if (!strcmp(voz, "cmu_us_slt")) {
		v = register_cmu_us_slt();
	}else if(!strcmp(voz, "cmu_us_awb")) {
		v = register_cmu_us_awb();
	}else if(!strcmp(voz, "cmu_us_rms")) {
		v = register_cmu_us_rms();
	}else if(!strcmp(voz, "cmu_us_kal")) {
		v = register_cmu_us_kal();
	}

	//streaming
	asi = new_audio_streaming_info();
	//set the callback
	asi->asc = callback_audio_stream_chunk;
	//min_buffsize takes 1264 as value with 1185 input (¿?)
	asi->min_buffsize = 2000;
	//we need to pass this to handle attributes and methods within the callback
	asi->userdata = this;
	//set streaming feature, needed to use the callback
	feat_set(v->features,"streaming_info",audio_streaming_info_val(asi));

	ctx = audio_resample_init(1, 1, sampleRate, voiceRate);

	pthread_cond_init(&condition_full, NULL);
	pthread_mutex_init(&mutex, NULL);
}

/// Unitializes the FliteVoiceSpark component.
void FliteVoiceSpark::quit(void){
	delete_audio_streaming_info(asi);
	audio_resample_close(ctx);
}

//IFlow<char*> implementation
void FliteVoiceSpark::processData(char *prompt){
	colaMensajes.push(string(prompt));
	pthread_cond_signal(&condition_full);
}


//IThreadProc implementation
void FliteVoiceSpark::process() {
	pthread_mutex_lock(&mutex);
	// Waiting for messages
	if (colaMensajes.empty()) {
		pthread_cond_wait(&condition_full, &mutex);
	}
	string text = colaMensajes.front();
	flite_text_to_wave(text.c_str(),v);
	colaMensajes.pop();
	pthread_mutex_unlock(&mutex);
}

void FliteVoiceSpark::signalHandler(int sig){
	// Asign the signal to default handler
	signal(sig,SIG_DFL);
	// Launch again the term signal
	raise(sig);

}

int FliteVoiceSpark::callback_audio_stream_chunk(const cst_wave *w, int start,
								int size, int last, void *user){
	/* Called with new samples from start for size samples */
	/* last is true if this is the last segment. */

	//Casting to access class attributes and methods through FliteVoiceSpark
	FliteVoiceSpark * fliteVoiceSpark;
	fliteVoiceSpark = (FliteVoiceSpark *)user;

	short * buffer = new short[size*2];
	int new_size = audio_resample(fliteVoiceSpark->ctx, buffer, w->samples+start, size);
	fliteVoiceSpark->queueAudioBuffer((char *)buffer, new_size*2);

	delete buffer;
	/* if you want to stop return CST_AUDIO_STREAM_STOP */
	if (last == 1)
	{
		return CST_AUDIO_STREAM_STOP;
	}
	return CST_AUDIO_STREAM_CONT;
}

//IAudioQueue implementation
int FliteVoiceSpark::getStoredAudioSize(){
	return audioQueue.getStoredAudioSize();
}

void FliteVoiceSpark::queueAudioBuffer(char *buffer, int size){
	audioQueue.queueAudioBuffer(buffer, size);
}

void FliteVoiceSpark::dequeueAudioBuffer(char *buffer, int size){
	audioQueue.dequeueAudioBuffer(buffer,size);
}

void FliteVoiceSpark::reset(){
	audioQueue.reset();
}

