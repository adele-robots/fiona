#ifndef __REMOTE_RENDERER_3D_H
#define __REMOTE_RENDERER_3D_H


#include "Component.h"
#include "IThreadProc.h"
#include "IFrameEventPublisher.h"

#include "IAudioQueue.h"
#include "IFlow.h"
#include "IWindow.h"
#include "IRenderizable.h"
#include "IControlVoice.h"

#include "StopWatch.h"

#ifdef _WIN32
#else
#include "Horde3D.h"
#include "Horde3DUtils.h"
#endif

using namespace std;

#define NUM_HORDE_IMAGE_CHANNELS 4

class RemoteRendererComponent : 
	public Component,
	public IThreadProc
{
public:
	IRenderizable *myRenderizable;
	IFlow<OutgoingImage *> *myFlow;
	IFlow<AudioWrap *> *myAudioFlow;
	IWindow *myWindow;
	IAudioQueue *myAudioQueue;
	IControlVoice *myControlVoice;
private:
	void initializeRequiredInterfaces() {	
		requestRequiredInterface<IRenderizable>(&myRenderizable);
		requestRequiredInterface<IFlow<AudioWrap *> >(&myAudioFlow);
		requestRequiredInterface<IFlow<OutgoingImage *> >(&myFlow);
		requestRequiredInterface<IWindow>(&myWindow);
		requestRequiredInterface<IAudioQueue>(&myAudioQueue);
		requestRequiredInterface<IControlVoice>(&myControlVoice);
	}

public:
	RemoteRendererComponent(
		char *instanceName, 
		ComponentSystem *cs
	) : Component(instanceName, cs)
	{}

	void init();
	void process();
	void quit();

	//IFrameEventPublisher implementation
	void addFrameEventSubscriber(FrameEventSubscriber *frameEventSubscriber);


private:
	void sendVideoFrame(void);
	void sendAudioFrame(void);
	void sendPacketUnit(void);
	void dispatchPackets(void);
	void updater(void);
	
private:
	StopWatch fpsSynch;
	StopWatch avsychStopWatch;
	H3DRes renderTargetTexture;
	float lastAudioPacketIssueTime;
	int width;
	int height;
	float audioPacketDuration;
	int audioPacketSize; // size in 16 bit samples
	std::vector< FrameEventSubscriber *> frameEventSubscriberArray;

	// Advertising
	bool hasAdvertising;
	int pixelsPerImage;
	int dontRemove;
	IplImage* backgroundImage;
	int cnB, cnG, cnR, cnA;
};

#endif
