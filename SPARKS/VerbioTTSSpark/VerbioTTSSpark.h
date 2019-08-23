/*
 * VerbioTTSSpark.h
 *
 *  Created on: 10/07/2013
 *      Author: guille
 */


/// @file VerbioTTSSpark.h
/// @brief Component VerbioTTSSpark main class.


#ifndef __VerbioTTSSpark_H
#define __VerbioTTSSpark_H

#include "Component.h"
#include "IFlow.h"
#include "AudioQueue.h"
#include "IAudioQueue.h"
#include "IThreadProc.h"
#include <queue>

#include "voxlib.h"

/* We use FFMPEG to resample audio waves */
extern "C" {
#include "libavcodec/avcodec.h"
}

#define BUFFER_SIZE 2048

/// @brief This is the main class for component VerbioTTSSpark.
///
/// 

class VerbioTTSSpark :
	public Component,

	public IFlow<char*>,
	public IAudioQueue,
	public IThreadProc
{
public:
		VerbioTTSSpark(
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
	short buffer_in[BUFFER_SIZE];
	std::queue<std::string> * colaMensajes;
	bool running;
	int dev;
	int bytes_recv;
	pthread_cond_t cond_full;
	pthread_mutex_t mutex;
private:
	void getError(int err);
	void synthesize(const char *text);
	void initializeVerbio();
	//static int ntfy_event(const char *event, unsigned long offset, unsigned long dev);
};

#endif
