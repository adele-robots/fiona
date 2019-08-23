/*
 * RemotePhotoCharacterSpark.h
 *
 *  Created on: 13/11/2013
 *      Author: guille
 */

/// @file RemotePhotoCharacterSpark.h
/// @brief Component RemotePhotoCharacterSpark main class.


#ifndef __RemotePhotoCharacterSpark_H
#define __RemotePhotoCharacterSpark_H


#include "Component.h"

#include "FrameEventSubscriber.h"
#include "IFrameEventPublisher.h"
#include "IFlow.h"
#include "IApplication.h"
#include "IAudioQueue.h"

#include "../PhotoEngineSpark/PhotoEngineSpark.h"
#include "../RemotePhotoRendererSpark/RemotePhotoRendererSpark.h"
#include "../FFMpegAVOutputSpark/FFMpegAVOutputComponent.h"
//#include "../ThreadSpark/ThreadComponent.h"
//#include "../WindowSpark/WindowComponent.h"

/// @brief This is the main class for component RemotePhotoCharacterSpark.
///
/// 

class RemotePhotoCharacterSpark :
	public Component,

	public IFlow<char*>,
	public IFrameEventPublisher,
	public IApplication
{
public:
		RemotePhotoCharacterSpark(
				char *instanceName,
				ComponentSystem *cs
		) : Component(instanceName, cs)
		{
			LoggerInfo("RemotePhotoCharacterSpark constructor");
			//1-Instances
			myPhotoEngineComponent=new PhotoEngineSpark(instanceName,cs);
			myRemotePhotoRendererComponent=new RemotePhotoRendererSpark(instanceName,cs);
			myFFMpegAVOutputComponent=new FFMpegAVOutputComponent(instanceName,cs);
		}
		virtual ~RemotePhotoCharacterSpark(){};

private:
	IAudioQueue *myAudioQueue;

	void initializeRequiredInterfaces() {	
		requestRequiredInterface<IAudioQueue>(&myAudioQueue);
	}

public:
	//Instance sub-components
	PhotoEngineSpark *myPhotoEngineComponent;
	RemotePhotoRendererSpark *myRemotePhotoRendererComponent;
	FFMpegAVOutputComponent *myFFMpegAVOutputComponent;

	//Mandatory
	void init(void);
	void quit(void);
	
	//IApplication implementation
	void run();

	//IFlow implementation
	void processData(char *);

	//IFrameEventPublisher implementation
	void addFrameEventSubscriber(FrameEventSubscriber *frameEventSubscriber);
	
private:
	//Put class attributes here
private:
	//Put class private methods here
	
};

#endif
