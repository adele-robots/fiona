/// @file AudioConsumerSpark.h
/// @brief Component AudioConsumerSpark main class.


#ifndef __AudioConsumerSpark_H
#define __AudioConsumerSpark_H

#include "Component.h"

#include "IAudioConsumer.h"


class AudioConsumerSpark :
	public Component,
	public IAudioConsumer
{
public:
		AudioConsumerSpark(
		char *instanceName, 
		ComponentSystem *cs
	) : Component(instanceName, cs)
	{}

private:
	//IAudioConsumer *myAudioConsumer;
	void initializeRequiredInterfaces() {	
		//requestRequiredInterface<IAudioConsumer>(&myAudioConsumer);
	}

public:
	void init(void);
	void quit(void);

#ifdef _WIN32
	//IAudioConsumer implementation
	void consumeAudioBuffer(__int16 *audioBuffer, int bufferSizeInBytes);
#else
	//IAudioConsumer implementation
	void consumeAudioBuffer(int16_t *audioBuffer, int bufferSizeInBytes);
#endif

};

#endif
