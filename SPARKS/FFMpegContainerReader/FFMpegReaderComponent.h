#ifndef __FFMPEG_READER_H
#define __FFMPEG_READER_H


#include "Component.h"

#include "IThreadProc.h"
#include "IFlow.h"

extern "C" {
#include <libavcodec/avcodec.h>
#include <libavformat/avformat.h>
#include <libswscale/swscale.h>
#include <libavcodec/opt.h>
}



class FFMpegReaderComponent :
	public Component,
	public IThreadProc
{
public:
	/*	We use IFlow interface for data exchange
	 * 	IFlow<Image *> <--- IVideoConsumer
	 * 	IFlow<AudioWrap *> <--- IAudioConsumer
	 * 	*/
	IFlow<Image *> *myFlow;
	IFlow<AudioWrap *> *myAudioFlow;
private:
	void initializeRequiredInterfaces() {
		requestRequiredInterface<IFlow<AudioWrap *> >(&myAudioFlow);
		requestRequiredInterface<IFlow<Image *> >(&myFlow);
	}

public:
	FFMpegReaderComponent(
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
