/// @file AudioInputSpark.h
/// @brief Component AudioInputSpark main class.


#ifndef __AudioInputSpark_H
#define __AudioInputSpark_H

#include "Component.h"
#include "IConcurrent.h"
#include "IFlow.h"
#include "IAsyncFatalErrorHandler.h"

#include "../ThreadSpark/ThreadComponent.h"
#include "../FFMpegAudioInputComponent/FFMpegAudioInputComponent.h"

/// @brief This is the main class for component AudioInputSpark.
///
///

class AudioInputSpark :
	public Component,
	public IConcurrent

{
public:
		AudioInputSpark(
		char *instanceName, 
		ComponentSystem *cs
	) : Component(instanceName, cs)
	{
		//1-Instances of the components needed within the spark
		myThreadComponent=new ThreadComponent(instanceName,cs);
		myFFMpegAudioInputComponent=new FFMpegAudioInputComponent(instanceName,cs);
	}

private:
	IAsyncFatalErrorHandler *myAsyncFatalErrorHandler;

	IFlow<AudioWrap *> *myAudioFlow;

	void initializeRequiredInterfaces() {	
		requestRequiredInterface<IAsyncFatalErrorHandler>(&myAsyncFatalErrorHandler);
		requestRequiredInterface<IFlow<AudioWrap *> >(&myAudioFlow);
	}

public:
	//Instance sub-components
	ThreadComponent *myThreadComponent;
	FFMpegAudioInputComponent *myFFMpegAudioInputComponent;

	//Mandatory
	void init(void);
	void quit(void);

	//IConcurrent implementation
	void start(void);
	void stop(void);
};

#endif
