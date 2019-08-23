/*
 * AudioQueueSpark.h
 *
 *  Created on: 28/02/2014
 *      Author: jandro
 */


/// @file AudioQueueSpark.h
/// @brief Component AudioQueueSpark main class.


#ifndef __AudioQueueSpark_H
#define __AudioQueueSpark_H

#include "Component.h"
#include "IFlow.h"
#include "AudioQueue.h"
#include "IAudioQueue.h"

/// @brief This is the main class for component AudioQueueSpark.
///
///

class AudioQueueSpark :
	public Component,

	public IFlow<AudioWrap*>,
	public IAudioQueue

{
public:
		AudioQueueSpark(
				char *instanceName,
				ComponentSystem *cs
		) : Component(instanceName, cs)
		{}
		virtual ~AudioQueueSpark() {};

private:


	void initializeRequiredInterfaces() {

	}

public:
	//Mandatory
	void init(void);
	void quit(void);

	//IFlow<AudioWrap*> implementation
	void processData(AudioWrap* prompt);

	// IAudioQueue
	int getStoredAudioSize();
	void queueAudioBuffer(char *buffer, int size);
	void dequeueAudioBuffer(char *buffer, int size);
	void reset(); // vaciar el buffer

private:
	AudioQueue audioQueue;
	int audioQueueSize;

};

#endif
