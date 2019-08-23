/// @file FFMpegAudioVideoDestination.h
/// @brief FFMpegAudioVideoDestination class declaration.


#ifndef __FFMPEG_AUDIO_VIDEO_DESTINATION_H
#define __FFMPEG_AUDIO_VIDEO_DESTINATION_H

#pragma warning(disable:4244) // FFMpeg silly warnings

extern "C" {
#include <libavcodec/avcodec.h>
#include <libavformat/avformat.h>
#include <libswscale/swscale.h>
#include <libavcodec/opt.h>
}


class FFMpegAudioVideoDestination {
public:
	class Image {
	public:
		Image(int width, int height, int bytesPerPixel, unsigned char *buffer);
		int width;
		int height;
		int bytesPerPixel;
		unsigned char *buffer;
		int bufferSize;
	};

public:
	void init(void);

	void open(char *url);
	
	void renderAudio(__int16 *buffer, int size);
	void renderVideo(FFMpegAudioVideoDestination::Image *image);
	
	void close(void);


public:
	int16_t *samples;
	int audio_input_frame_size;
    AVFormatContext *oc;
    AVStream *audio_st;
	int audioSampleRate;
	int audioPacketSize;
	float audioPacketDuration;

private:
	AVStream *add_audio_stream(AVFormatContext *oc, enum CodecID codec_id);
	AVStream *add_video_stream(AVFormatContext *oc, enum CodecID codec_id);
	void open_audio(AVFormatContext *oc, AVStream *st);
	void open_video(AVFormatContext *oc, AVStream *st);
	void close_audio(AVFormatContext *oc, AVStream *st);
	void close_video(AVFormatContext *oc, AVStream *st);
	void write_audio_frame(AVFormatContext *oc, AVStream *st);
	void write_video_frame(
		AVFormatContext *oc, 
		AVStream *st, 
		FFMpegAudioVideoDestination::Image *im
	);
	void fill_yuv_image(FFMpegAudioVideoDestination::Image *im);
	AVFrame *alloc_picture(
		enum PixelFormat pix_fmt, 
		int width, 
		int height
	);

private:
    AVOutputFormat *fmt;
	float t, tincr, tincr2;
	AVFrame *avFrameFromScene;	/* scene generated colorspace & dimensions */
	AVFrame *avFrameFFMpegCodec;	/* ffmpeg codec ready colorspace & dimensions */
	AVStream *video_st;
	char URL[256];
	uint8_t *audio_outbuf;
	int audio_outbuf_size;
	int streamVideoHeight;
	int streamVideoWidth;	
	struct SwsContext *img_convert_ctx;
	uint8_t *video_outbuf;
	int video_outbuf_size;
};



#endif