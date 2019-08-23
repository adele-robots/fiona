/// @file FFMpegAVOutputComponent.cpp
/// @brief Component FFMpegAVOutputComponent main class.


#ifndef __FFMpegAVOutputComponent_H
#define __FFMpegAVOutputComponent_H

#include "Component.h"

#include "IFlow.h"

#include "OutgoingImage.h"
#include "AudioWrap.h"
#include "EnumParser.h"

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

/// @brief This is the main class for component FFMpegAVOutputComponent.
///
/// Remote Audio/Video Output

class FFMpegAVOutputComponent :
	public Component,	
	
	public IFlow<OutgoingImage *>,
	public IFlow<AudioWrap *>

{
public:
	FFMpegAVOutputComponent(
		char *instanceName, 
		ComponentSystem *cs
	) : Component(instanceName, cs)
	{}

private:

	void initializeRequiredInterfaces() {	
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


private:
	void open(char *url);
	void close(void);

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


};


#endif
