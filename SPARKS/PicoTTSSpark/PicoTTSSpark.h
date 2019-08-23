/*
 * PicoTTSSpark.h
 *
 *  Created on: 14/01/2013
 *      Author: guille
 */


/// @file PicoTTSSpark.h
/// @brief Component PicoTTSSpark main class.


#ifndef __PicoTTSSpark_H
#define __PicoTTSSpark_H

#include "Component.h"
#include "IFlow.h"
#include "AudioQueue.h"
#include "IAudioQueue.h"
#include "IThreadProc.h"
#include <queue>

/* Pico TTS headers */
#include <pico/picoapi.h>
#include <pico/picoapid.h>
#include <pico/picoos.h>

/* We use FFMPEG to resample audio waves */
extern "C" {
#include "libavcodec/avcodec.h"
}

/// @brief This is the main class for component PicoTTSSpark.
///
/// 

class PicoTTSSpark :
	public Component,

	public IFlow<char*>,
	public IAudioQueue,
	public IThreadProc

{
public:
		PicoTTSSpark(
				char *instanceName,
				ComponentSystem *cs
		) : Component(instanceName, cs)
		{}
		virtual ~PicoTTSSpark() {};

private:


	void initializeRequiredInterfaces() {	

	}

public:
	//Mandatory
	void init(void);
	void quit(void);

	//IFlow<char*> implementation
	void processData(char* prompt);

	// IAudioQueue
	int getStoredAudioSize();
	void queueAudioBuffer(char *buffer, int size);
	void dequeueAudioBuffer(char *buffer, int size);
	void reset(); // vaciar el buffer

	// Implementation of IThreadproc
	void process();
	
private:
	AudioQueue audioQueue;
	int audioQueueSize;
	ReSampleContext* ctx;
	int valid_sample_rate;
	std::queue<std::string> * colaMensajes;
	pthread_cond_t cond_full;
	pthread_mutex_t mutex;

private:
	//Put class private methods here
	void synthesize(const char *text);
	void initializePico();
};

#endif
