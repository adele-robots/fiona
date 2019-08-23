/*
 * RemotePhotoRendererSpark.h
 *
 *  Created on: 12/11/2013
 *      Author: guille
 */

/// @file RemotePhotoRendererSpark.h
/// @brief Component RemotePhotoRendererSpark main class.


#ifndef __RemotePhotoRendererSpark_H
#define __RemotePhotoRendererSpark_H

#ifndef REMOTERENDERER_INDEPENDENT
#define REMOTERENDERER_INDEPENDENT
#endif

#include "Component.h"
#include "IFrameEventPublisher.h"

#include <signal.h>
#include "IAudioQueue.h"
#include "IApplication.h"
#include "IFlow.h"
#include "IRenderizable.h"
#include "StopWatch.h"


/// @brief This is the main class for component RemotePhotoRendererSpark.
///
/// 

class RemotePhotoRendererSpark :
	public Component,

	public IApplication,
	public IFrameEventPublisher

{
public:
		RemotePhotoRendererSpark(
				char *instanceName,
				ComponentSystem *cs
		) : Component(instanceName, cs)
		{}
		virtual ~RemotePhotoRendererSpark(){};

	IRenderizable *myRenderizable;
	IFlow<OutgoingImage *> *myFlow;
	IFlow<AudioWrap *> *myAudioFlow;
	IAudioQueue *myAudioQueue;

	void initializeRequiredInterfaces() {	
		requestRequiredInterface<IRenderizable>(&myRenderizable);
		requestRequiredInterface<IFlow<AudioWrap *> >(&myAudioFlow);
		requestRequiredInterface<IFlow<OutgoingImage *> >(&myFlow);
		requestRequiredInterface<IAudioQueue>(&myAudioQueue);
	}

public:
	//Mandatory
	void init(void);
	void quit(void);
	
	// IApplication implementation
	void run();
	
	//IFrameEventPublisher implementation
	void addFrameEventSubscriber(FrameEventSubscriber *frameEventSubscriber);

private:
	StopWatch avsychStopWatch;
	float audioPacketDuration;
	int audioPacketSize; // size in 16 bit samples
	std::vector< FrameEventSubscriber *> frameEventSubscriberArray;
	int width, height;
	int bytesPerPixel;
private:
	void sendVideoFrame(void);
	void sendAudioFrame(void);
	void sendPacketUnit(void);
	void dispatchPackets(void);
	void updater(void);
	static void signalHandler(int sig);
};

#endif
