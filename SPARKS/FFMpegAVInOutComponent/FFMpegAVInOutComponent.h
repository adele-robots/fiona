/// @file FFMpegAVInOutComponent.cpp
/// @brief Component FFMpegAVInOutComponent main class.


#ifndef __FFMpegAVInOutComponent_H
#define __FFMpegAVInOutComponent_H

#include "Component.h"

#include "IFlow.h"

#include "OutgoingImage.h"
#include "AudioWrap.h"
#include "EnumParser.h"

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <arpa/inet.h>
#include <netinet/in.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <unistd.h>

#define BUF_SIZE 1024

char muLawCompressTable [256] = {
    0, 0, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3,
    4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4,
    5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5,
    5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5,
    6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6,
    6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6,
    6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6,
    6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6,
    7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7,
    7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7,
    7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7,
    7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7,
    7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7,
    7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7,
    7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7,
    7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7
};

short seg_end[8] = {0xFF, 0x1FF, 0x3FF, 0x7FF,
        0xFFF, 0x1FFF, 0x3FFF, 0x7FFF
    };

const int cBias = 0x84;
const int cClip = 32635;

#ifdef _WIN32
#pragma warning(disable:4244) // FFMpeg silly warnings
#else
#endif

extern "C" {
#include <libavcodec/avcodec.h>
#include <libavformat/avformat.h>
#include <libswscale/swscale.h>
#include <libavutil/opt.h>
}

#include "PixelFormatMap.h"

/// @brief This is the main class for component FFMpegAVInOutComponent.
///
/// Remote Audio/Video Output

class FFMpegAVInOutComponent :
	public Component,	
	
	public IFlow<OutgoingImage *>,
	public IFlow<AudioWrap *>

{
public:
	FFMpegAVInOutComponent(
		char *instanceName, 
		ComponentSystem *cs
	) : Component(instanceName, cs)
	{}

private:

	void initializeRequiredInterfaces() {	
		requestRequiredInterface<IFlow<AudioWrap *> >(&myAudioFlow);
	}
public:
	// Mandatory
	void init(void);
	void quit(void);

#ifdef _WIN32
	//IAudioConsumer implementation
	void consumeAudioBuffer(__int16 *audioBuffer, int bufferSizeInBytes);
#else
	
#endif
	//IFlow<AudioWrap *> implementation
	void processData(AudioWrap *);

	//IFlow<OutgoingImage *> implementation
	void processData(OutgoingImage *);

	void process();


public:
	void open(char *url);
	void close(void);

	int16_t pcmu_to_l16(unsigned int ulawbyte);

private:
	int16_t *samples;
	AVFormatContext *oc;
	AVStream *audio_st;
	int audioSampleRate;
	int audioPacketSize;
	float audioPacketDuration;
	int audio_input_frame_size;
	AVStream *video_st;
	PixelFormat FFMPEG_CODE_FOR_HORDE3D_COLORSPACE2;
private:

	AVFrame *alloc_picture(
		enum PixelFormat pix_fmt, 
		int width, 
		int height
	);
	void write_video_frame(
		AVFormatContext *oc, 
		AVStream *st, 
		OutgoingImage *im
	);
private:
	AVStream *add_audio_stream(AVFormatContext *oc, enum CodecID codec_id);
	AVStream *add_video_stream(AVFormatContext *oc, enum CodecID codec_id);

	void open_audio(AVFormatContext *oc, AVStream *st);
	void open_video(AVFormatContext *oc, AVStream *st);

	void close_audio(AVFormatContext *oc, AVStream *st);
	void close_video(AVFormatContext *oc, AVStream *st);

	void write_audio_frame(AVFormatContext *oc, AVStream *st);
	void fill_yuv_image(OutgoingImage *im);

	static int ff_lockmgr(void **mutex, enum AVLockOp op);

	static void my_log_callback(void *ptr, int level, const char *fmt, va_list vargs);

	void processAudio(void);

	unsigned char linearToULawSample(short sample);
	unsigned char linear2ulaw(short sample);
	int search(int val, short* table, int size);

	/*int Component_avio_open(AVIOContext **s, const char *filename, int flags);
	int Component_ffurl_open(URLContext **puc, const char *filename, int flags);
	int Component_ffurl_connect(URLContext* uc);
	int Component_ffio_fdopen(AVIOContext **s, URLContext *h);
	int Component_ffio_init_context(AVIOContext *s,
			unsigned char *buffer,
			int buffer_size,
			int write_flag,
			void *opaque,
			int(*read_packet)(void *opaque, uint8_t *buf, int buf_size),
			int(*write_packet)(void *opaque, uint8_t *buf, int buf_size),
			int64_t(*seek)(void *opaque, int64_t offset, int whence));
	int Component_url_resetbuf(AVIOContext *s, int flags);*/


private:
	AVFrame *avFrameFromScene;	/* scene generated colorspace & dimensions */
	AVFrame *avFrameFFMpegCodec;	/* ffmpeg codec ready colorspace & dimensions */
	AVOutputFormat *fmt;
	uint8_t *video_outbuf;
	uint8_t *audio_outbuf;
	int audio_outbuf_size;
	int streamVideoHeight;
	int streamVideoWidth;
	struct SwsContext *img_convert_ctx;
	int video_outbuf_size;

	pthread_t thread_id;
	AVFormatContext *pFormatCtx;
	int audioStream;
	int i, frameFinished;

	AVCodecContext  *pAudioCodecCtx;
	AVCodec         *pAudioCodec;

	AVCodec         *pCodec;
	AVPacket        packet;
	uint8_t         *buffer, *audio_buffer, *resampled_audio_buffer;
	ReSampleContext* resamplingContext;

	struct SwsContext *INimg_convert_ctx;


public:

	IFlow<AudioWrap *> *myAudioFlow;
	int s;
	int n;
	int len;
	struct sockaddr_in other;
    char buf[BUF_SIZE];

#ifdef SAVE_FILE
	FILE * file;
#endif

};


#endif
