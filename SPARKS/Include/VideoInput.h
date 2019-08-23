/// @file VideoInput.h
/// @brief VideoInput class declaration.

#ifndef __VIDEO_INPUT_H
#define __VIDEO_INPUT_H


#include "VideoQueue.h"
#include "IAsyncFatalErrorHandler.h"
#include "IVideoConsumer.h"



class VideoInput {
public:
	VideoInput(IVideoConsumer *vc, IAsyncFatalErrorHandler* afeh) : 
		videoConsumer(vc),
		asyncFatalErrorHandler(afeh)
	{}
	virtual void init() = 0;
	virtual void quit() = 0;
	virtual void start() {}
	virtual void stop() {}

public:
	VideoQueue videoQueue;
	IVideoConsumer *videoConsumer;
	IAsyncFatalErrorHandler *asyncFatalErrorHandler;
};

#endif
