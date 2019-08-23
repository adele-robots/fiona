#ifndef __TRONREADERCOMPONENT_H
#define __TRONREADERCOMPONENT_H


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



class TRONReaderComponent :
	public Component,
	public IThreadProc
{
public:

	IFlow<Image *> *myImageFlow;
	IFlow<AudioWrap *> *myAudioFlow;
private:
	void initializeRequiredInterfaces() {
		requestRequiredInterface<IFlow<AudioWrap *> >(&myAudioFlow);
		requestRequiredInterface<IFlow<Image *> >(&myImageFlow);
	}

public:
	TRONReaderComponent(
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
	int videoStream, audioStream;
	int i, frameFinished;

	bool isStreamOpened;
	string stream;
	StopWatch * timeout;

	AVCodecContext  *pVideoCodecCtx, *pAudioCodecCtx;
	AVCodec         *pVideoCodec, *pAudioCodec;

	AVCodec         *pCodec;
	AVFrame         *pFrame;
	AVFrame         *pFrameRGB;
	AVPacket        packet;
	uint8_t         *buffer, *audio_buffer, *resampled_audio_buffer;
	ReSampleContext* resamplingContext;

	struct SwsContext *img_convert_ctx;
public:
	void open_file(char *);
private:
	void processVideo(void);
	void processAudio(void);
};


#endif
