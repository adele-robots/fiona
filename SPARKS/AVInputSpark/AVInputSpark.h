/// @file AVInputSpark.h
/// @brief Component AVInputSpark main class.


#ifndef __AVInputSpark_H
#define __AVInputSpark_H

#include "Component.h"
#include "IConcurrent.h"
#include "IFlow.h"
#include "IAsyncFatalErrorHandler.h"

#include "../ThreadSpark/ThreadComponent.h"
#include "../FFMpegContainerReader/FFMpegReaderComponent.h"

/// @brief This is the main class for component AVInputSpark.
///
///

class AVInputSpark :
	public Component,
	public IConcurrent

{
public:
		AVInputSpark(
		char *instanceName, 
		ComponentSystem *cs
	) : Component(instanceName, cs)
	{
		//1-Instances of the components needed within the spark
		myThreadComponent=new ThreadComponent(instanceName,cs);
		myFFMpegReaderComponent=new FFMpegReaderComponent(instanceName,cs);
	}

private:
	IAsyncFatalErrorHandler *myAsyncFatalErrorHandler;

	/*	We use IFlow interface for data exchange
	 *  IFlow<Image *> <--- IVideoConsumer
	 * 	IFlow<AudioWrap *> <--- IAudioConsumer
	 * 	*/
	IFlow<Image *> *myFlow;
	IFlow<AudioWrap *> *myAudioFlow;

	void initializeRequiredInterfaces() {	
		requestRequiredInterface<IAsyncFatalErrorHandler>(&myAsyncFatalErrorHandler);
		requestRequiredInterface<IFlow<Image *> >(&myFlow);
		requestRequiredInterface<IFlow<AudioWrap *> >(&myAudioFlow);
	}

public:
	//Instance sub-components
	ThreadComponent *myThreadComponent;
	FFMpegReaderComponent *myFFMpegReaderComponent;

	//Mandatory
	void init(void);
	void quit(void);

	//IConcurrent implementation
	void start(void);
	void stop(void);
};

#endif
