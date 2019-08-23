#ifndef __FFMpegAudioInputComponent_H
#define __FFMpegAudioInputComponent_H


#include "Component.h"

#include "IThreadProc.h"
#include "IFlow.h"
#include "StopWatch.h"

extern "C" {
#include <libavcodec/avcodec.h>
#include <libavformat/avformat.h>
#include <libswscale/swscale.h>
#include <libavcodec/opt.h>
}



class FFMpegAudioInputComponent :
	public Component,
	public IThreadProc
{
public:

	IFlow<AudioWrap *> *myAudioFlow;
private:
	void initializeRequiredInterfaces() {
		requestRequiredInterface<IFlow<AudioWrap *> >(&myAudioFlow);
	}

public:
	FFMpegAudioInputComponent(
		char *instanceName, 
		ComponentSystem *cs
	) : Component(instanceName, cs)
	{}

	void init();
	void quit();

	//IThreadProc implementation
	void process();

private:
	pthread_t thread_id;
	AVFormatContext *pFormatCtx;
	int audioStream;
	int i;

	bool isStreamOpened;
	string stream;
	StopWatch * timeout;

	AVCodecContext  *pAudioCodecCtx;
	AVCodec         *pAudioCodec;

	AVCodec         *pCodec;
	AVPacket        packet;
	uint8_t         *audio_buffer, *resampled_audio_buffer;
	ReSampleContext* resamplingContext;
public:
	void open_file(char *);
private:
	void processAudio(void);
};


#endif
