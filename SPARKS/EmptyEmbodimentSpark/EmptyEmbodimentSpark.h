/// @file EmptyEmbodimentSpark.h
/// @brief Component EmptyEmbodimentSpark main class.


#ifndef __EmptyEmbodimentSpark_H
#define __EmptyEmbodimentSpark_H

#include "Component.h"
#include "IConcurrent.h"
#include "IEventQueue.h"
#include "IAsyncFatalErrorHandler.h"
#include "IAudioQueue.h"
#include "IControlVoice.h"
#include "IFlow.h"


#include "../AudioRendererSpark/AudioRendererSpark.h"
#include "../FFMpegAVInOutComponent/FFMpegAVInOutComponent.h"
#include "../ThreadSpark/ThreadComponent.h"
#include "../EmptyApplicationSpark/EmptyApplicationSpark.h"



/// @brief This is the main class for component EmptyEmbodimentSpark.
///
/// EmptyEmbodimentSpark component.
/// Includes AudioRendererSpark, FFMpegAVOutputComponent
/// MainWindow and Thread

class EmptyEmbodimentSpark :
	public Component,
	public IConcurrent,
	public IApplication,
	public IAsyncFatalErrorHandler
{
public:
		EmptyEmbodimentSpark(
		char *instanceName,
		ComponentSystem *cs
	) : Component(instanceName, cs)
	{
		//1-Instances
		myWindowComponent=new EmptyApplicationSpark(instanceName,cs);
		myThreadComponent=new ThreadComponent(instanceName,cs);
		myAudioRendererSpark=new AudioRendererSpark(instanceName,cs);
		myFFMpegAVInOutComponent=new FFMpegAVInOutComponent(instanceName,cs);
	}

private:
	IControlVoice *myControlVoice;
	IAsyncFatalErrorHandler *myAsyncFatalErrorHandler;
	IAudioQueue *myAudioQueue;
	IFlow<AudioWrap *> *myAudioFlow;


	void initializeRequiredInterfaces() {
		requestRequiredInterface<IControlVoice>(&myControlVoice);
		requestRequiredInterface<IAsyncFatalErrorHandler>(&myAsyncFatalErrorHandler);
		requestRequiredInterface<IAudioQueue>(&myAudioQueue);
		requestRequiredInterface<IFlow<AudioWrap *> >(&myAudioFlow);
	}

public:
	//Instance sub-components
	AudioRendererSpark *myAudioRendererSpark;
	FFMpegAVInOutComponent *myFFMpegAVInOutComponent;
	ThreadComponent *myThreadComponent;
	EmptyApplicationSpark *myWindowComponent;

	void init(void);
	void quit(void);

	//IConcurrent implementation
	void start();
	void stop();

	//IApplication implementation
	void run();

	//IAsyncFatalErrorHandler implementation
	void handleError(char*);
};

#endif
