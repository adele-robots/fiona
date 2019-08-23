/// @file VideoConsumerSpark.h
/// @brief Component VideoConsumerSpark main class.


#ifndef __VideoConsumerSpark_H
#define __VideoConsumerSpark_H

#include "Component.h"
#include "IVideoConsumer.h"
//#include "IVideoConsumer2.h"


class VideoConsumerSpark :
	public Component,
	public IVideoConsumer
	//public IVideoConsumer2
{
public:
		VideoConsumerSpark(
		char *instanceName,
		ComponentSystem *cs
	) : Component(instanceName, cs)
	{}

private:
	//IVideoConsumer *myVideoConsumer;
	//IVideoConsumer2 *myVideoConsumer2;
	void initializeRequiredInterfaces() {
		//requestRequiredInterface<IVideoConsumer>(&myVideoConsumer);
		//requestRequiredInterface<IVideoConsumer2>(&myVideoConsumer2);
	}

public:
	void init(void);
	void quit(void);

	//IVideoConsumer implementation
	void consumeVideoFrame(Image *);

	//IVideoConsumer2 implementation
	//void consumeVideoFrame(OutgoingImage *);

};

#endif
