#ifndef NUANCETTSSPARK_H_
#define NUANCETTSSPARK_H_

#include <Component.h>
#include "IFlow.h"
#include "AudioQueue.h"
#include "IAudioQueue.h"
#include "IThreadProc.h"
#include <queue>
#include "NuanceTTSClient.h"
#include <mutex>

extern "C" {
	#include "libavcodec/avcodec.h"
}

class NuanceTTSSpark: public Component,
	//public IVoice,
	public IFlow<char*>,
	public IAudioQueue {
public:
	NuanceTTSSpark(
					char *instanceName,
					ComponentSystem *cs
			) : Component(instanceName, cs)
			{}
	virtual ~NuanceTTSSpark() {}

public:
	//Mandatory
	void init(void);
	void quit(void);

	//IFlow<char*> implementation
	void processData(char* prompt);

	// IAudioQueue
	int getStoredAudioSize();
	void queueAudioBuffer(char *buffer, int size);
	void dequeueAudioBuffer(char *buffer, int size);
	void reset();

private:
	AudioQueue audioQueue;
	int audioQueueSize;
	NuanceTTSClient ttsClient;
	int valid_sample_rate;
	string voice;
	string voiceModel;

public:
	ReSampleContext* ctx;
	mutex m;

private:
	void initializeRequiredInterfaces(){
		}

};

#endif
