/// @file VideoConsumerSpark.cpp
/// @brief VideoConsumerSpark class implementation.

// Third party libraries are linked explicitly once in the project.


#include "stdAfx.h"

#include "VideoConsumerSpark.h"


#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "VideoConsumerSpark")) {
			return new VideoConsumerSpark(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif

/// Initializes VideoConsumerSpark component.
void VideoConsumerSpark::init(void){
}

/// Unitializes the VideoConsumerSpark component.
void VideoConsumerSpark::quit(void){
}

//IVideoConsumer implementation
void VideoConsumerSpark::consumeVideoFrame(Image *){

}

//IVideoConsumer2 implementation
/*void VideoConsumerSpark::consumeVideoFrame(OutgoingImage *){

}*/
