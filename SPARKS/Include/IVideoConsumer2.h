/// @file IVideoConsumer.h
/// @brief IVideoConsumer interface definition


#ifndef __I_VIDEO_CONSUMER_2_H
#define __I_VIDEO_CONSUMER_2_H

#include "OutgoingImage.h"


//OJO
//Image problem in FFMpegAVOutputComponent
//FFMpegAVOutputComponent uses its own class Image
//IVideoConsumer interface uses a class Image.h different from FFMpegAVOutputComponent::Image
class IVideoConsumer2 {
public:
	virtual void consumeVideoFrame(OutgoingImage *) = 0;
};


#endif
