/*
 * FliteVoiceSpark.h
 *
 *  Created on: 17/10/2012
 *      Author: guille
 */


/// @file FliteVoiceSpark.h
/// @brief Component FliteVoiceSpark main class.


#ifndef __FliteVoiceSpark_H
#define __FliteVoiceSpark_H

#include <Component.h>
#include "IFlow.h"
#include "AudioQueue.h"
#include "IAudioQueue.h"
#include "IThreadProc.h"
#include "flite/flite.h"
#include <queue>

extern "C" {
#include <libavcodec/avcodec.h>
}

#define VERBOSEMSG(...) if(_global_verbose) { fprintf(stderr, __VA_ARGS__); }

/// @brief This is the main class for component FliteVoiceSpark.
///
/// Flite TTS engine

class FliteVoiceSpark :
	public Component,

	public IFlow<char*>,
	public IAudioQueue,
	public IThreadProc
{
public:
		FliteVoiceSpark(
				char *instanceName,
				ComponentSystem *cs
		) : Component(instanceName, cs)
		{}

private:
	void initializeRequiredInterfaces() {
	}

public:
	//Mandatory
	void init(void);
	void quit(void);
	
	//IFlow implementation
	void processData(char *);

	// IThreadproc
	void process();

	// IAudioQueue
	int getStoredAudioSize();
	void queueAudioBuffer(char *buffer, int size);
	void dequeueAudioBuffer(char *buffer, int size);
	void reset();
	
	static void signalHandler(int sig);

private:
	static int callback_audio_stream_chunk(const cst_wave *, int, int, int, void *);
	/* Error handler */
	void getError(int error);
private:
	char voice[256];
	int audioPacketSize;
	char * text;
	int sampleRate;
	bool hiloTerminado;

	AudioQueue audioQueue;
	int audioQueueSize;
	cst_voice *v;
	cst_audio_streaming_info *asi;

	int _global_verbose;
	std::queue<std::string> colaMensajes;

	ReSampleContext* ctx;
	int voiceRate;

	pthread_cond_t condition_full;
	pthread_mutex_t mutex;
};

#endif
