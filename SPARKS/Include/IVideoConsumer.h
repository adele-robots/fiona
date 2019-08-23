/// @file IVideoConsumer.h
/// @brief IVideoConsumer interface definition


#ifndef __I_VIDEO_CONSUMER_H
#define __I_VIDEO_CONSUMER_H

#include "Image.h"



class IVideoConsumer {
public:
	virtual void consumeVideoFrame(Image *) = 0;
};


#endif
