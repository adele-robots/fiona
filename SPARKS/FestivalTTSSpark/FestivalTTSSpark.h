/*
 * FestivalTTSSpark.h
 *
 *  Created on: 23/12/2013
 *      Author: jandro
 */


/// @file FestivalTTSSpark.h
/// @brief Component FestivalTTSSpark main class.


#ifndef __FestivalTTSSpark_H
#define __FestivalTTSSpark_H

#include "Component.h"
#include "IFlow.h"
#include "AudioQueue.h"
#include "IAudioQueue.h"
#include "IThreadProc.h"

/* Festival TTS headers */
#include <festival.h>
#include "siod.h"

#include "queue"
#include "iconv.h"

// Messages to be shared within the app
// - Message type (PID_proceso, 1)
// - Content
typedef struct{
	long int tipo;
	char contenido[1024];
} mensaje;

/// @brief This is the main class for component FestivalTTSSpark.
///
/// 

class FestivalTTSSpark :
	public Component,

	public IFlow<char*>,
	public IAudioQueue,
	public IThreadProc

{
public:
		FestivalTTSSpark(
				char *instanceName,
				ComponentSystem *cs
		) : Component(instanceName, cs)
		{}
		virtual ~FestivalTTSSpark() {};

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
	int valid_sample_rate;
	EST_Wave wave;
	bool festival_initialized;
	iconv_t ic;
	std::queue<std::string> colaMensajes;

private:
	//Put class private methods here
	void getError(int err);
	void synthesize(const char *text);
};

#endif
