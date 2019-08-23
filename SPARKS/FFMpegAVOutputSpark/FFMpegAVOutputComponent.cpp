/// @file FFMpegAVOutputComponent.cpp
/// @brief FFMpegAVOutputComponent class implementation.

// Third party libraries are linked explicitly once in the project.
// #pragma comment(lib, "thirdPartyLib.lib")

#include "stdAfx.h"
#include "FFMpegAVOutputComponent.h"

#include "Configuration.h"
#include "ErrorHandling.h"
#include "StopWatch.h"
#include "Logger.h"
#include "dumpFormat.h"



#ifdef _WIN32
#pragma comment(lib, "avformat.lib")
#pragma comment(lib, "avcodec.lib")
#pragma comment(lib, "avutil.lib")
#pragma comment(lib, "swscale.lib")
#else
#endif

PixelFormat RENDERED_FRAME_PIXEL_FORMAT;

/// Initializes FFMpegAVOutputComponent component.
void FFMpegAVOutputComponent::init(void) 
{
	// Initialize PixelFormat enum map
	EnumParser<PixelFormat> parser;

	img_convert_ctx = NULL;

	av_lockmgr_register(&ff_lockmgr);

	av_log_set_level(AV_LOG_QUIET);
	av_log_set_callback(&my_log_callback);
	av_register_all();
	
	// Load video stream parameters
	streamVideoWidth = getGlobalConfiguration()->getInt(const_cast<char *>("AudioVideoConfig_Width"));
	streamVideoHeight = getGlobalConfiguration()->getInt(const_cast<char *>("AudioVideoConfig_Height"));
	// Load audio stream parameters
	audioSampleRate = getComponentConfiguration()->getInt(const_cast<char *>("AudioVideoConfig_Remote_AvatarStream_AudioSampleRate"));
	audioPacketSize = getGlobalConfiguration()->getInt(const_cast<char *>("AudioVideoConfig_Remote_AvatarStream_AudioPacketSize"));

    /* allocate the output media context */
    oc = avformat_alloc_context();
	if (!oc) ERR("avformat_alloc_context: not enough memory");

	char format[80];	
	getComponentConfiguration()->getString(const_cast<char *>("AudioVideoConfig_Remote_AvatarStream_Format"), format, 80);

	fmt = av_guess_format(format, NULL, NULL);
	if (!fmt) ERR("Invalid ffmpeg output format: %s", format);

	oc->oformat = fmt;
	try{
		/* Load pixel format from config */
		string pixelFormat = getComponentConfiguration()->getString(const_cast<char *>("Pixel_Format"));
		RENDERED_FRAME_PIXEL_FORMAT = parser.ParseSomeEnum(pixelFormat);
	} catch (...) {
		LoggerInfo("Parameter pixelFormat not found Remote, using default PIX_FMT_BGRA.");
		RENDERED_FRAME_PIXEL_FORMAT = PIX_FMT_BGRA;
	}

	/* AVFrame from engine (3D/2D/photo etc) frame */
	avFrameFromScene = alloc_picture(
		RENDERED_FRAME_PIXEL_FORMAT,
		//parser.ParseSomeEnum(pixelFormat),
		streamVideoWidth,
		streamVideoHeight
	);

	/* AVFrame for ffmpeg, changed colorspace & dimensions*/
	avFrameFFMpegCodec = alloc_picture(
		PIX_FMT_YUV420P, 
		streamVideoWidth, 
		streamVideoHeight
	);

	// Load URL stream destination and open it
	char outputFfmpegStreamURL[256];
	getComponentConfiguration()->getString(const_cast<char *>("AudioVideoConfig_Remote_AvatarStream_URL"), outputFfmpegStreamURL, 256);
	std::string url(outputFfmpegStreamURL);
	url.replace(url.find("room"), 4, getGlobalConfiguration()->getString(const_cast<char *>("room")));

	LoggerInfo("Opening avatar stream: %s", url.c_str());
	open(const_cast<char *>(url.c_str()));
}

/// Unitializes the FFMpegAVOutputComponent component.
void FFMpegAVOutputComponent::quit(void) 
{
	av_lockmgr_register(NULL);
	close();
}

void FFMpegAVOutputComponent::close(void) {

	/* write the trailer, if any.  the trailer must be written
     * before you close the CodecContexts open when you wrote the
     * header; otherwise write_trailer may try to use memory that
     * was freed on av_codec_close() */
    av_write_trailer(oc);

	if (video_st) close_video(oc, video_st);
    if (audio_st) close_audio(oc, audio_st);

    /* free the streams */
    for(unsigned int i = 0; i < oc->nb_streams; i++) {
        av_freep(&oc->streams[i]->codec);
        av_freep(&oc->streams[i]);
    }

    if (!(fmt->flags & AVFMT_NOFILE)) {
        /* close the output file */
        avio_close(oc->pb);
    }

    /* free the stream */
    av_free(oc);
}

void FFMpegAVOutputComponent::close_audio(AVFormatContext *oc, AVStream *st) {
    avcodec_close(st->codec);
    av_free(samples);
    av_free(audio_outbuf);
}


void FFMpegAVOutputComponent::close_video(AVFormatContext *oc, AVStream *st) {
    avcodec_close(st->codec);

	av_free(avFrameFromScene->data[0]);
    av_free(avFrameFromScene);

	av_free(avFrameFFMpegCodec->data[0]);
    av_free(avFrameFFMpegCodec);

}

void FFMpegAVOutputComponent::open(char *url) {

	strncpy(oc->filename, url, sizeof(oc->filename));

    video_st = NULL;
    audio_st = NULL;

	if (fmt->video_codec != CODEC_ID_NONE) {
        video_st = add_video_stream(oc, fmt->video_codec);
    }
    if (fmt->audio_codec != CODEC_ID_NONE) {
        audio_st = add_audio_stream(oc, fmt->audio_codec);
    }

    /* set the output parameters (must be done even if no parameters). */
    if (av_set_parameters(oc, NULL) < 0) ERR("Invalid output format parameters");

	char ffmpegFormatString[4096];
	getFFMpegFormatString(ffmpegFormatString, 4096, oc, 0, url, 1);
	LoggerInfo(ffmpegFormatString);

    if (video_st)
        open_video(oc, video_st);
    if (audio_st)
        open_audio(oc, audio_st);

	if (avio_open(&oc->pb, url, AVIO_WRONLY) < 0) {
		LoggerError("[FIONA-logger]Error opening output stream");
		ERR("Error opening output ffmpeg stream");
    }

	av_write_header(oc);

	LoggerInfo("Output stream opened");
}

AVStream *FFMpegAVOutputComponent::add_audio_stream(
	AVFormatContext *oc, 
	enum CodecID codec_id
)
{
    AVCodecContext *c;
    AVStream *st;

    st = av_new_stream(oc, 1);
    if (!st) ERR("Could not alloc stream");

    c = st->codec;
    c->codec_id = codec_id;
    c->codec_type = AVMEDIA_TYPE_AUDIO;
    c->bit_rate = getGlobalConfiguration()->getInt(const_cast<char *>("AudioVideoConfig_Remote_AvatarStream_CodecAudioBitRate"));
    c->sample_rate = audioSampleRate;
    c->channels = 1;

	// some formats want stream headers to be separate
    if(oc->oformat->flags & AVFMT_GLOBALHEADER)
        c->flags |= CODEC_FLAG_GLOBAL_HEADER;

    return st;
}

AVStream *FFMpegAVOutputComponent::add_video_stream(
	AVFormatContext *oc, 
	enum CodecID codec_id
)
{
    AVCodecContext *c;
    AVStream *st;

    st = av_new_stream(oc, 0);
    if (!st) ERR("Could not alloc stream");

    c = st->codec;
    c->codec_id = codec_id;
    c->codec_type = AVMEDIA_TYPE_VIDEO;
    c->bit_rate = getGlobalConfiguration()->getInt(const_cast<char *>("AudioVideoConfig_Remote_AvatarStream_CodecVideoBitRate"));
	/* resolution must be a multiple of two */
    c->width = streamVideoWidth;
    c->height = streamVideoHeight;

    /* time base: this is the fundamental unit of time (in seconds) in terms
       of which frame timestamps are represented. for fixed-fps content,
       timebase should be 1/framerate and timestamp increments should be
       identically 1. */

    // FLV codec imposes 11025/22050/44100Hz audio sample rate
    c->time_base.den = audioSampleRate;
    // Audio packet size is multiplied by two because a packet contains two audio frames
    c->time_base.num = audioPacketSize*2;

    c->gop_size = 24; /* emit one intra frame every twelve frames at most (12) */
    // FLV codec imposes 'PIX_FMT_YUV420P' pixel format
    c->pix_fmt = PIX_FMT_YUV420P;

	// some formats want stream headers to be separate
    if(oc->oformat->flags & AVFMT_GLOBALHEADER)
        c->flags |= CODEC_FLAG_GLOBAL_HEADER;

    return st;
}

void FFMpegAVOutputComponent::open_audio(AVFormatContext *oc, AVStream *st)
{
    AVCodecContext *c;
    AVCodec *codec;

    c = st->codec;

    /* find the audio encoder */
    codec = avcodec_find_encoder(c->codec_id);
    if (!codec) ERR("Audio codec not found");

    /* open it */
    if (avcodec_open(c, codec) < 0) ERR("could not open audio codec");

    audio_outbuf_size = 160;
    audio_outbuf = (uint8_t *)av_malloc(audio_outbuf_size);

    /* ugly hack for PCM codecs (will be removed ASAP with new PCM
       support to compute the input frame size in samples */
	if (c->frame_size <= 1) {
        audio_input_frame_size = audio_outbuf_size / c->channels;
        switch(st->codec->codec_id) {
        case CODEC_ID_PCM_S16LE:
        case CODEC_ID_PCM_S16BE:
        case CODEC_ID_PCM_U16LE:
        case CODEC_ID_PCM_U16BE:
            audio_input_frame_size >>= 1;
            break;
        default:
            break;
        }
    } else {
        audio_input_frame_size = c->frame_size;
    }

	samples = (int16_t *)av_malloc(audio_input_frame_size * 2);

	if (audio_input_frame_size != audioPacketSize) {
		ERR(
			"Output audio packet size mistmatch. "
			"AudioVideoConfig_Remote_AvatarStream_AudioPacketSize=%d, "
			"Codec provided: %d",
			audioPacketSize,
			audio_input_frame_size
		);
	}
}

void FFMpegAVOutputComponent::open_video(AVFormatContext *oc, AVStream *st)
{
	AVCodec *codec;
	AVCodecContext *c = NULL;

	c = st->codec;

	/* find the video encoder */
	codec = avcodec_find_encoder(c->codec_id);
	if (!codec) ERR("Video codec not found");

	c = avcodec_alloc_context3(codec);
	if (!c) {
		ERR("Could not allocate audio codec context\n");
	}

	c = st->codec;

	/* open the codec */
	if (avcodec_open2(c, codec,NULL) < 0) ERR("could not open video codec");

	video_outbuf_size = 2000000;
	video_outbuf = (uint8_t *)av_malloc(video_outbuf_size);
}

//IFlow<AudioWrap *> implementation
void FFMpegAVOutputComponent::processData(AudioWrap * audioWrap)
{
	// A packet will contain two audio frames and a video frame
	if (audioWrap->bufferSizeInBytes != audio_input_frame_size * 2) ERR("OJO wrong audio packet size");

	memcpy(samples, audioWrap->audioBuffer, audioWrap->bufferSizeInBytes);
	write_audio_frame(oc, audio_st);
}

void FFMpegAVOutputComponent::write_audio_frame(AVFormatContext *oc, AVStream *st)
{
    AVCodecContext *c;
    AVPacket pkt;
    av_init_packet(&pkt);

    c = st->codec;

	int res = avcodec_encode_audio(c, audio_outbuf, audio_outbuf_size, samples);
	if (res < 0) ERR("avcodec_encode_audio");

	//res, is the number of bytes used to encode the data read
	//from the input buffer (samples)
	pkt.size = res;

	if ((unsigned)(c->coded_frame->pts) != AV_NOPTS_VALUE) {
        pkt.pts= av_rescale_q(c->coded_frame->pts, c->time_base, st->time_base);
	}
    pkt.flags |= AV_PKT_FLAG_KEY;
    pkt.stream_index= st->index;
    pkt.data= audio_outbuf;
 
    /* write the compressed frame in the media file */
    if (av_interleaved_write_frame(oc, &pkt) != 0) {
        ERR("Error while writing audio frame");
    }
}

//IFlow implementation
void FFMpegAVOutputComponent::processData(OutgoingImage *image){
	write_video_frame(oc, video_st, image);
}

void  FFMpegAVOutputComponent::write_video_frame(
		AVFormatContext *oc, 
		AVStream *st, 
		OutgoingImage *im
	)
{
    int out_size, ret;
    AVCodecContext *c;

    c = st->codec;

	fill_yuv_image(im);

    /* encode the image */
    out_size = avcodec_encode_video(c, video_outbuf, video_outbuf_size, avFrameFFMpegCodec);
	//out_size = avcodec_encode_video(c, video_outbuf, video_outbuf_size, avFrameFromScene);

	/* if zero size, it means the image was buffered */
    if (out_size > 0) {
        AVPacket pkt;
        av_init_packet(&pkt);

		if ((unsigned)(c->coded_frame->pts) != AV_NOPTS_VALUE) {
            pkt.pts= av_rescale_q(c->coded_frame->pts, c->time_base, st->time_base);
		}
		if(c->coded_frame->key_frame) {
            pkt.flags |= AV_PKT_FLAG_KEY;
		}

        pkt.stream_index= st->index;
        pkt.data= video_outbuf;
        pkt.size= out_size;

        /* write the compressed frame in the media file */
        ret = av_interleaved_write_frame(oc, &pkt);
    } else {
        ret = 0;
    }
	if (ret != 0) ERR("Error writing video frame");
}

// init allocates avFrameFromScene, avFrameFFMpegCodec
// here the FFMpegAudioVideoDestination::Image is memcpied into avFrameFromScene
// and avFrameFromScene is converted into avFrameFFMpegCodec
// via libswscale

void FFMpegAVOutputComponent::fill_yuv_image(OutgoingImage *im) {
	memcpy(avFrameFromScene->data[0], im->buffer, im->bufferSize);

	img_convert_ctx = sws_getCachedContext(
		img_convert_ctx,
		im->width,	
		im->height, 
		RENDERED_FRAME_PIXEL_FORMAT,
		streamVideoWidth,
		streamVideoHeight,
		PIX_FMT_YUV420P,
		SWS_FAST_BILINEAR,
		NULL, NULL, NULL
	);

	if (img_convert_ctx == NULL) {
		ERR("ffmpeg: Cannot initialize the conversion context");
	}

	unsigned char *buffer[4] = {avFrameFromScene->data[0], 0, 0, 0};
	buffer[0] += avFrameFromScene->linesize[0] * (im->height - 1);
	int stride[4] = { -avFrameFromScene->linesize[0], 0, 0, 0};

	int res = sws_scale(
		img_convert_ctx, 
		buffer,
		stride, 
		0,
		streamVideoHeight,
		avFrameFFMpegCodec->data, 
		avFrameFFMpegCodec->linesize
	);

	if (res == -1) ERR("Error in sws_scale");
}

AVFrame *FFMpegAVOutputComponent::alloc_picture(
	enum PixelFormat pix_fmt, 
	int width, 
	int height
)
{
    AVFrame *picture;
    uint8_t *picture_buf;
    int size;

    picture = avcodec_alloc_frame();
    if (!picture) ERR("avcodec_alloc_frame");

    size = avpicture_get_size(pix_fmt, width, height);

	picture_buf = (uint8_t *)av_malloc(size);
	if (!picture_buf) ERR("avcodec_alloc_frame");
    
	avpicture_fill(
		(AVPicture *)picture, 
		picture_buf,
        pix_fmt, 
		width, 
		height
	);
    
	return picture;
}

int FFMpegAVOutputComponent::ff_lockmgr(void **mutex, enum AVLockOp op){
   pthread_mutex_t** pmutex = (pthread_mutex_t**) mutex;
   switch (op) {
   case AV_LOCK_CREATE:
      *pmutex = (pthread_mutex_t*) malloc(sizeof(pthread_mutex_t));
       pthread_mutex_init(*pmutex, NULL);
       break;
   case AV_LOCK_OBTAIN:
       pthread_mutex_lock(*pmutex);
       break;
   case AV_LOCK_RELEASE:
       pthread_mutex_unlock(*pmutex);
       break;
   case AV_LOCK_DESTROY:
       pthread_mutex_destroy(*pmutex);
       free(*pmutex);
       break;
   }
   return 0;
}

void FFMpegAVOutputComponent::my_log_callback(void *ptr, int level, const char *fmt, va_list vargs)
{
	if (level > av_log_get_level())
		return;

	//vprintf(fmt, vargs);
	LoggerInfo("Log de av: %s",fmt);
}


