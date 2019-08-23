#ifndef __REMOTE_RENDERER_2D_H
#define __REMOTE_RENDERER_2D_H

#include "Component.h"
#include "IThreadProc.h"
#include "IFrameEventPublisher.h"
#include "IAudioQueue.h"
#include "IFlow.h"
#include "IRenderizable.h"
#include "IControlVoice.h"

#include "StopWatch.h"

#include "Horde3D.h"
#include "Horde3DUtils.h"

class RemoteRenderer2DComponent :
	public Component,
	public IThreadProc
{
public:
	IRenderizable *myRenderizable;
	IFlow<OutgoingImage *> *myFlow;
	IFlow<AudioWrap *> *myAudioFlow;
	IAudioQueue *myAudioQueue;
	IControlVoice *myControlVoice;

private:
	void initializeRequiredInterfaces() {	
		requestRequiredInterface<IRenderizable>(&myRenderizable);
		requestRequiredInterface<IFlow<AudioWrap *> >(&myAudioFlow);
		requestRequiredInterface<IFlow<OutgoingImage *> >(&myFlow);
		requestRequiredInterface<IAudioQueue>(&myAudioQueue);
		requestRequiredInterface<IControlVoice>(&myControlVoice);
	}

public:
	RemoteRenderer2DComponent(
		char *instanceName, 
		ComponentSystem *cs
	) : Component(instanceName, cs)
	{}

	void init();
	void quit();

	//IThreadProc implementation
	void process();

	//IFrameEventPublisher implementation
	void addFrameEventSubscriber(FrameEventSubscriber *frameEventSubscriber);

private:
	void sendVideoFrame(void);
	void sendAudioFrame(void);
	void sendPacketUnit(void);
	void dispatchPackets(void);
	void updater(void);

private:
	StopWatch avsychStopWatch;
	StopWatch fpsSynch;

	float lastAudioPacketIssueTime;
	int width;
	int height;
	float audioPacketDuration;
	int audioPacketSize; // size in 16 bit samples
	std::vector< FrameEventSubscriber *> frameEventSubscriberArray;
	int bytesPerPixel;
};

#endif
