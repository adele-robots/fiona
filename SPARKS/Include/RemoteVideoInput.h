/// @file RemoteVideoInput.h
/// @brief RemoteVideoInput class declaration.


#ifndef __REMOTE_VIDEO_INPUT_H
#define __REMOTE_VIDEO_INPUT_H


#include "VideoInput.h"
#include "IVideoConsumer.h"


class RemoteVideoInput : public VideoInput
{
public:
	RemoteVideoInput(IVideoConsumer *vc, IAsyncFatalErrorHandler *afeh) : VideoInput(vc, afeh) {}
	void init();
	void quit();
};


#endif
