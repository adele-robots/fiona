/// @file RemoteAudioInput.h
/// @brief RemoteAudioInput class declaration.


#ifndef __REMOTE_AUDIO_INPUT_H
#define __REMOTE_AUDIO_INPUT_H

#include "AudioInput.h"
#include "IAudioConsumer.h"


class RemoteAudioInput : public AudioInput {
public:
	RemoteAudioInput(IAudioConsumer *ac, IAsyncFatalErrorHandler *afeh) : 
	  AudioInput(ac, afeh) 
	{}
	void init();
	void quit(void);
};


#endif
