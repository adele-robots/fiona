/// @file AudioConsumerSpark.cpp
/// @brief AudioConsumerSpark class implementation.

// Third party libraries are linked explicitly once in the project.


#include "stdAfx.h"

#include "AudioConsumerSpark.h"


#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "AudioConsumerSpark")) {
			return new AudioConsumerSpark(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif

/// Initializes AudioConsumerSpark component.
void AudioConsumerSpark::init(void){
}

/// Unitializes the AudioConsumerSpark component.
void AudioConsumerSpark::quit(void){
}

//IAudioConsumer implementation
#ifdef _WIN32
void AudioConsumerSpark::consumeAudioBuffer(__int16 *audioBuffer, int bufferSizeInBytes)
#else
void AudioConsumerSpark::consumeAudioBuffer(int16_t *audioBuffer, int bufferSizeInBytes)
#endif
{
	//if (bufferSizeInBytes != audio_input_frame_size * 2) ERR("OJO wrong audio packet size");
	//printf("ESTOY EN AUDIO CONSUMERCOMPONENT\n");
	//printf("AudioBuffer %s",audioBuffer);
	//printf ("bufferSize %s", bufferSizeInBytes);

	//memcpy(samples, audioBuffer, bufferSizeInBytes);

	//write_audio_frame(oc, audio_st);
}
