/// @file AudioInput.h
/// @brief AudioInput class declaration.

#ifndef __AUDIO_INPUT_H
#define __AUDIO_INPUT_H

#include "IAsyncFatalErrorHandler.h"
#include "IAudioConsumer.h"


class AudioInput
{
public:
	AudioInput(IAudioConsumer *ac, IAsyncFatalErrorHandler* afeh) : 
		audioConsumer(ac),
		asyncFatalErrorHandler(afeh)
	{}
	virtual void init() = 0;
	virtual void quit(void) = 0;
	virtual void start() {}
	virtual void stop() {}

public:
	IAudioConsumer *audioConsumer;
	IAsyncFatalErrorHandler *asyncFatalErrorHandler;
};


#endif
