#ifndef __AUDIO_RENDERER_H
#define __AUDIO_RENDERER_H


#include "Component.h"
#include "IThreadProc.h"
#include "IAudioQueue.h"
#include "IFlow.h"
#include "IControlVoice.h"
#include "StopWatch.h"

using namespace std;

class AudioRendererSpark :
	public Component,
	public IThreadProc
{
public:
	IFlow<AudioWrap *> *myAudioFlow;
	IAudioQueue *myAudioQueue;
	IControlVoice *myControlVoice;

private:
	void initializeRequiredInterfaces() {
		requestRequiredInterface<IFlow<AudioWrap *> >(&myAudioFlow);
		requestRequiredInterface<IAudioQueue>(&myAudioQueue);
		requestRequiredInterface<IControlVoice>(&myControlVoice);
	}

public:
	AudioRendererSpark(
		char *instanceName,
		ComponentSystem *cs
	) : Component(instanceName, cs)
	{}

	void init();
	void process();
	void quit();

private:
	void sendAudioFrame(void);
	void sendPacketUnit(void);
	void dispatchPackets(void);

private:
	StopWatch avsychStopWatch;
	float lastAudioPacketIssueTime;
	float audioPacketDuration;
	int audioPacketSize; // size in 16 bit samples
};

#endif
