/// @file FFMpegAudioVideoSource.h
/// @brief FFMpegAudioVideoSource class declaration.

#ifndef __FFMPEG_AUDIO_VIDEO_SOURCE_H
#define __FFMPEG_AUDIO_VIDEO_SOURCE_H

extern "C" {
#include <libavcodec/avcodec.h>
#include <libavformat/avformat.h>
#include <libswscale/swscale.h>
#include <libavcodec/opt.h>
}

#include <opencv2/opencv.hpp>

#include "RemoteAudioInput.h"
#include "RemoteVideoInput.h"

#include "Thread.h"


class FFMpegAudioVideoSource : public Thread {

private:
	RemoteAudioInput *remoteAudioInput;
	RemoteVideoInput *remoteVideoInput;

public:
	FFMpegAudioVideoSource(
		RemoteAudioInput *rai,
		RemoteVideoInput *rvi,
		IAsyncFatalErrorHandler *afeh
	) :
		Thread("FFMpeg-AV-Input-thread", afeh),
		remoteAudioInput(rai),
		remoteVideoInput(rvi)
	{}

	void init(void);

	// 'args' point to FFmpeg URL (char *)
	void open(char *ffmpegURL, int sampleRate);
	void close(void);
	void process(void);

private:
	void processVideo(void);
	void processAudio(void);

private:
	AVFormatContext *pFormatCtx;
	unsigned int	videoStream;
	unsigned int	audioStream;
	AVCodecContext  *pVideoCodecCtx;
	AVCodecContext  *pAudioCodecCtx;
	AVCodec         *pVideoCodec;
	AVCodec         *pAudioCodec;
	AVFrame         *pFrame; 
	AVPacket        packet;
	int             frameFinished;
	uint8_t			*audio_buffer;
	struct SwsContext *img_convert_ctx;
	uint8_t			*resampled_audio_buffer;
	ReSampleContext* resamplingContext;
};

#endif

