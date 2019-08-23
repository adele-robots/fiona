/*
 * AudioQueueSpark.cpp
 *
 *  Created on: 28/02/2014
 *      Author: jandro
 */

/// @file AudioQueueSpark.cpp
/// @brief AudioQueueSpark class implementation.

#include "stdAfx.h"
#include "AudioQueueSpark.h"

#ifdef _WIN32
#else
extern "C" Component *createComponent(char *componentInstanceName,
		char *componentType, ComponentSystem *componentSystem) {
	if (!strcmp(componentType, "AudioQueueSpark")) {
		return new AudioQueueSpark(componentInstanceName, componentSystem);
	} else {
		return NULL;
	}
}
#endif

/// Initializes AudioQueueSpark component.
void AudioQueueSpark::init(void) {
	//Set the queue size (in samples, 1 byte/sample)
	//try {
	//	audioQueueSize = getComponentConfiguration()->getInt(const_cast<char *>("AudioQueueSizeBytes"));
	//} catch(::Exception &e){
	//	LoggerWarn("[FIONA-logger]Default size: 44100");
		audioQueueSize = 2646000;
	//}
	audioQueue.init(audioQueueSize);

}

/// Unitializes the AudioQueueSpark component.
void AudioQueueSpark::quit(void) {
	audioQueue.reset();
}


void AudioQueueSpark::processData(AudioWrap *prompt) {
	//if(getStoredAudioSize()<4096)
	//LoggerInfo("sizeof(AudioWrap->audioBuffer)=%d, AudioWrap->bufferSizeInBytes=%d", sizeof(prompt->audioBuffer), prompt->bufferSizeInBytes);
	queueAudioBuffer((char *) prompt->audioBuffer, prompt->bufferSizeInBytes );//* 2);
}

int AudioQueueSpark::getStoredAudioSize() {
	return audioQueue.storedAudioSize;
}

void AudioQueueSpark::queueAudioBuffer(char *buffer, int size) {
	audioQueue.queueAudioBuffer(buffer, size);
}

void AudioQueueSpark::dequeueAudioBuffer(char *buffer, int size) {
	audioQueue.dequeueAudioBuffer(buffer, size);
}

void AudioQueueSpark::reset() {
	audioQueue.reset();
}
