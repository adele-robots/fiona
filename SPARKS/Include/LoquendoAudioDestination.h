/// @file LoquendoAudioDestination.h
/// @brief LoquendoAudioDestination module implementation.


#ifndef __LOQUENDO_AUDIO_DESTIONATION_H
#define __LOQUENDO_AUDIO_DESTIONATION_H

#include "RemoteAudioOutput.h"

#define AUDIO_DESTINATION_AS_SYMBOL	LoquendoAudioDestination

#define DUMMY_MACRO(x) #x
#define MAKE_STR(x) DUMMY_MACRO(x)



typedef struct {
    unsigned int SampleRate;
    ttsAudioEncodingType coding;
    ttsAudioSampleType nChannels;
	RemoteAudioOutput *remoteAudioOutput;
	char *audioBuffer;

} ChannelType;


#endif
