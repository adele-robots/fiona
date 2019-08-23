/// @file LocalVideoInput.h
/// @brief LocalVideoInput class declaration.


#ifndef __LOCAL_VIDEO_INPUT_H
#define __LOCAL_VIDEO_INPUT_H


#include "VideoInput.h"
#include "IVideoConsumer.h"
#include "VidCapture.h"


class LocalVideoInput : public VideoInput {
public:
	LocalVideoInput(IVideoConsumer *vc, IAsyncFatalErrorHandler *afeh);
	LocalVideoInput::~LocalVideoInput(void);

	void init(void);
	void quit(void);
	
	void start(void);
	void stop(void);


private:
	VidCapture *vidCapture;
	bool enabled;
};


#endif 


