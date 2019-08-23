/// @file FFMpegAVInOutComponent.cpp
/// @brief FFMpegAVInOutComponent class implementation.

// Third party libraries are linked explicitly once in the project.
// #pragma comment(lib, "thirdPartyLib.lib")

#include "stdAfx.h"
//#define SAVE_FILE
#include "FFMpegAVInOutComponent.h"

#include "Configuration.h"
#include "ErrorHandling.h"
#include "StopWatch.h"
#include "Logger.h"
#include "dumpFormat.h"
#include "FFImage.h"
#include "libavformat/avio.h"
#include "libavformat/url.h"
#include "libavformat/avio_internal.h"

#include "ccrtp/rtppkt.h"
#include "g711.h"

#define IO_BUFFER_SIZE 32768

#ifdef _WIN32
#pragma comment(lib, "avformat.lib")
#pragma comment(lib, "avcodec.lib")
#pragma comment(lib, "avutil.lib")
#pragma comment(lib, "swscale.lib")
#else
#endif

PixelFormat RENDERED_FRAME_PIXEL_FORMAT;

pthread_t threadid;

/*void * bucle(void * args) {
	LoggerInfo("bucle");
	av_register_all();
	FFMpegAVInOutComponent * ffmpeg = (FFMpegAVInOutComponent*)args;
        while(1) {
        	ffmpeg->process();
        }
        return NULL;
}*/
void * bucle(void * args) {
	LoggerInfo("bucle");
	FFMpegAVInOutComponent * ffmpeg = (FFMpegAVInOutComponent*)args;

    while ((ffmpeg->n = recvfrom(ffmpeg->s, ffmpeg->buf, BUF_SIZE, 0, (struct sockaddr *) &(ffmpeg->other), &(ffmpeg->len))) != -1) {
    	//LoggerInfo("Tamaño del paquete: %d", ffmpeg->n);
		/*printf("Received from %s:%d: ",
			inet_ntoa(ffmpeg->other.sin_addr),
			ntohs(ffmpeg->other.sin_port));*/
		/*fflush(stdout);
		write(1, ffmpeg->buf, ffmpeg->n);
		write(1, "\n", 1);*/
    	/*int headersSizeInBytes = 12;
    	int audioSize = 160;
    	int offset = ffmpeg->n - audioSize;*/
    	//LoggerInfo("Tamaño del paquete: %d Offset: %d", ffmpeg->n, offset);
		//TODO: quitar cabeceras
    	char * b = malloc(ffmpeg->n);
    	memcpy(b, ffmpeg->buf, ffmpeg->n);
    	ost::IncomingRTPPkt rtpPacket(b, ffmpeg->n);
    	if(!rtpPacket.isHeaderValid()) {
    		LoggerWarn("Incoming RTP Packet is invalid");
    		continue;
    	}
    	int audioSize = rtpPacket.getPayloadSize();
    	int offset = rtpPacket.getHeaderSize();
    	//LoggerInfo("Tamaño del paquete: %d Offset: %d Payload: %d", rtpPacket.getRawPacketSize(), rtpPacket.getHeaderSize(), rtpPacket.getPayloadSize());
    	// Los paquetes que llegan cada 5 segundos que generan ruido tienen este tamaño. El resto son mayores.
    	/*if(audioSize <= 48) {
    		continue;
    	}*/
    	//TODO: convertir ulaw -> pcm16s
    	short buff[audioSize];
    	for(int i=0; i<audioSize; i++) {
    		buff[i] = ulaw2linear((ffmpeg->buf+offset)[i]);
    	}
    	AudioWrap audioWrap(buff, audioSize*2);
		//TODO: ffmpeg->myAudioFlow->processData(AudioWrapper*);
    	ffmpeg->myAudioFlow->processData(&audioWrap);
		// Change volume
		/*float multiplier = 1.5;
		if(multiplier!=1.0) {
			short * buf = (short*) ffmpeg->buf;
			for(int i=27; i<(ffmpeg->n/2); i++){
				buf[i] = buf[i] * multiplier;
			}
		}*/
#ifdef SAVE_FILE
    	/*FILE * f = fopen("/home/adele/hexdump", "a");
    	for(int i=0;i<(ffmpeg->n)-headersSizeInBytes;i++)
    		fprintf(f, "%02x", ffmpeg->buf+headersSizeInBytes+i);
    	fclose(f);*/
			fwrite(buff, audioSize*2, 1, ffmpeg->file);
			FILE * f = fopen("/home/adele/pcmulaw", "a");
			fwrite(ffmpeg->buf+offset, audioSize, 1, f);
			fclose(f);
#endif

		/* echo back to client */
		//sendto(ffmpeg->s, ffmpeg->buf, ffmpeg->n, 0, (struct sockaddr *) &(ffmpeg->other), ffmpeg->len);
		/*if(rtpPacket.getPayloadSize() > 48) {
			char pid[256];
			sprintf(pid, "%ld", getpid());
			FILE * f = fopen(string(string("/tmp/pcmulaw") + string(pid)).c_str(), "a");
			fwrite(rtpPacket.getPayload(), rtpPacket.getPayloadSize(), 1, f);
			fclose(f);
		}*/
    }
        return NULL;
}

void sasigaction(int sig, siginfo_t * info, void * w){
        fprintf(stderr, "SIGTERM send by %d\n", info->si_pid);
        pthread_kill(threadid, SIGKILL);
}

/// Initializes FFMpegAVInOutComponent component.
void FFMpegAVInOutComponent::init(void) 
{
    struct sockaddr_in self;
    len = sizeof(struct sockaddr_in);
    int port;

    /* initialize socket */
    if ((s=socket(AF_INET, SOCK_DGRAM, IPPROTO_UDP)) == -1) {
    	perror("socket");
		return;
    }

    /* bind to server port */
    //port = 12350;//atoi(getGlobalConfiguration()->getString(const_cast<char *>("room")).c_str());
    port = getGlobalConfiguration()->getInt(const_cast<char *>("Avatar_Port"));
    memset((char *) &self, 0, sizeof(struct sockaddr_in));
    self.sin_family = AF_INET;
    self.sin_port = htons(port);
    self.sin_addr.s_addr = htonl(INADDR_ANY);
    if (bind(s, (struct sockaddr *) &self, sizeof(self)) == -1) {
    	perror("bind");
    	return;
    }

#ifdef SAVE_FILE
	file = fopen("/home/adele/audio.pcm", "w");
#endif

    pthread_create(&threadid, NULL, &bucle, this);
}
/// Initializes FFMpegAVInOutComponent component.
/*void FFMpegAVInOutComponent::init(void)
{
	// Initialize PixelFormat enum map
	EnumParser<PixelFormat> parser;

	img_convert_ctx = NULL;
	INimg_convert_ctx = NULL;

	// av_malloc implements memory aligment necessary for ffmpeg
	audio_buffer = (uint8_t *)av_malloc(AVCODEC_MAX_AUDIO_FRAME_SIZE);
	// Don't know exactly why resampling... it's a matter with ASR AudioConsumer (Hz allowed)..
	resampled_audio_buffer = (uint8_t *)av_malloc(AVCODEC_MAX_AUDIO_FRAME_SIZE);

	av_lockmgr_register(&ff_lockmgr);

	av_log_set_level(AV_LOG_DEBUG);
	av_log_set_callback(&my_log_callback);
	av_register_all();
	
	// Load video stream parameters
	streamVideoWidth = getGlobalConfiguration()->getInt(const_cast<char *>("AudioVideoConfig_Width"));
	streamVideoHeight = getGlobalConfiguration()->getInt(const_cast<char *>("AudioVideoConfig_Height"));
	// Load audio stream parameters
	audioSampleRate = getComponentConfiguration()->getInt(const_cast<char *>("AudioVideoConfig_Remote_AvatarStream_AudioSampleRate"));
	audioPacketSize = getGlobalConfiguration()->getInt(const_cast<char *>("AudioVideoConfig_Remote_AvatarStream_AudioPacketSize"));

    // allocate the output media context
    oc = avformat_alloc_context();
	if (!oc) ERR("avformat_alloc_context: not enough memory");

	char format[80];	
	getComponentConfiguration()->getString(const_cast<char *>("AudioVideoConfig_Remote_AvatarStream_Format"), format, 80);

	fmt = av_guess_format(format, NULL, NULL);
	if (!fmt) ERR("Invalid ffmpeg output format: %s", format);

	oc->oformat = fmt;
	try{
		// Load pixel format from config
		string pixelFormat = getComponentConfiguration()->getString(const_cast<char *>("Pixel_Format"));
		RENDERED_FRAME_PIXEL_FORMAT = parser.ParseSomeEnum(pixelFormat);
	} catch (...) {
		LoggerInfo("Parameter pixelFormat not found Remote, using default PIX_FMT_BGRA.");
		RENDERED_FRAME_PIXEL_FORMAT = PIX_FMT_BGRA;
	}

	// AVFrame from engine (3D/2D/photo etc) frame
	avFrameFromScene = alloc_picture(
		RENDERED_FRAME_PIXEL_FORMAT,
		//parser.ParseSomeEnum(pixelFormat),
		streamVideoWidth,
		streamVideoHeight
	);

	// AVFrame for ffmpeg, changed colorspace & dimensions
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
#ifdef SAVE_FILE
	file = fopen("/home/adele/audio.pcm", "w");
#endif
    pthread_t threadid;
    pthread_create(&threadid, NULL, &bucle, this);
}*/

void FFMpegAVInOutComponent::quit(void)
{
    ::close(s);
#ifdef SAVE_FILE
	fclose(file);
#endif

	struct sigaction act;
    act.sa_flags = SA_SIGINFO;
    act.sa_sigaction = &sasigaction;
    sigaction(SIGTERM, &act, NULL);
}
/// Unitializes the FFMpegAVInOutComponent component.
/*void FFMpegAVInOutComponent::quit(void)
{
	//Close the file
	av_close_input_file(pFormatCtx);

	av_lockmgr_register(NULL);
	close();
#ifdef SAVE_FILE
	fclose(file);
#endif
}*/

void FFMpegAVInOutComponent::close(void) {

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

void FFMpegAVInOutComponent::close_audio(AVFormatContext *oc, AVStream *st) {
    avcodec_close(st->codec);
    av_free(samples);
    av_free(audio_outbuf);
}


void FFMpegAVInOutComponent::close_video(AVFormatContext *oc, AVStream *st) {
    avcodec_close(st->codec);

	av_free(avFrameFromScene->data[0]);
    av_free(avFrameFromScene);

	av_free(avFrameFFMpegCodec->data[0]);
    av_free(avFrameFFMpegCodec);

}

void FFMpegAVInOutComponent::open(char *url) {

	/*strncpy(oc->filename, url, sizeof(oc->filename));

    video_st = NULL;
    audio_st = NULL;

	if (fmt->video_codec != CODEC_ID_NONE) {
        video_st = add_video_stream(oc, fmt->video_codec);
    }
    if (fmt->audio_codec != CODEC_ID_NONE) {
        audio_st = add_audio_stream(oc, fmt->audio_codec);
    }

    // set the output parameters (must be done even if no parameters).
    if (av_set_parameters(oc, NULL) < 0) ERR("Invalid output format parameters");

	char ffmpegFormatString[4096];
	getFFMpegFormatString(ffmpegFormatString, 4096, oc, 0, url, 1);
	LoggerInfo(ffmpegFormatString);

    if (video_st)
        open_video(oc, video_st);
    if (audio_st)
        open_audio(oc, audio_st);*/

	/*if (av_open_input_file(&pFormatCtx, "rtp://192.168.1.87:12350?reuse=1", NULL, 0, NULL) !=0) {
		ERR("ffmpeg: could not open %s", "rtp://192.168.1.87:12350?reuse=1");
	}
	LoggerInfo("Despues av_open_input_file");*/
	/*if (avio_open(&oc->pb, url, AVIO_RDWR) < 0) {
		LoggerError("[FIONA-logger]Error opening output stream");
		ERR("Error opening output ffmpeg stream");
    }
	LoggerInfo("Despues avio_open");*/
	/*oc->iformat = pFormatCtx->iformat;
	LoggerInfo("Despues iformat");*/

	/*pFormatCtx = oc;

	av_write_header(oc);

	LoggerInfo("Output stream opened");*/

	if (av_open_input_file(&pFormatCtx, url, NULL, 0, NULL) !=0) {
			LoggerError("ffmpeg: could not open %s", "rtp://192.168.1.87:12350");
		}
	LoggerInfo("Despues av_open_input_file");

	if (av_find_stream_info(pFormatCtx) < 0) {
		LoggerError("Couldn't find stream information");
	}

	LoggerInfo("pFormatCtx->nb_streams=%d", pFormatCtx->nb_streams);
	// Find the first audio stream
	audioStream = -1;
	for(unsigned int i = 0; i < pFormatCtx->nb_streams; i++) {
		LoggerInfo("pFormatCtx->streams[i]->codec->codec_type=%d", pFormatCtx->streams[i]->codec->codec_type);
		if (pFormatCtx->streams[i]->codec->codec_type==AVMEDIA_TYPE_AUDIO) {
			audioStream=i;
			break;
		}
	}
	if (audioStream == -1) {
		LoggerError("ffmpeg: Didn't find an audio stream");
	}

	// Get a pointer to the codec context for the audio stream
	pAudioCodecCtx=pFormatCtx->streams[audioStream]->codec;

	// Find the decoder for the audio stream
	pAudioCodec=avcodec_find_decoder(pAudioCodecCtx->codec_id);
	if (pAudioCodec==NULL)
		LoggerError("ffmpeg: unsupported audio codec");

	if (pAudioCodecCtx->channels != 1) {
		LoggerError("Assuming one audio channel, input has %d", pAudioCodecCtx->channels);
	}

	// RESAMPLING STUFF BEGINS

	int sampleRate = 11025;

	LoggerInfo(
			"Resampling input audio: %d Hz -> %d Hz",
			pAudioCodecCtx->sample_rate,
			sampleRate // PARAM FROM CONFIG ? RELATED WITH LOQUENDO?
	);

	resamplingContext = audio_resample_init(
			1,
			pAudioCodecCtx->channels,
			sampleRate,
			pAudioCodecCtx->sample_rate
	);

	if (!resamplingContext) LoggerError("Failed to initialize resampling");

	// RESAMPLING STUFF ENDS

	// Open audio codec
	if (avcodec_open(pAudioCodecCtx, pAudioCodec)<0) LoggerError("Could not open audio codec");

	char formatString[4096];
	//like built-in ffmpeg function av_dump_format (dumps info about file onto standard error)
	//but retrieves the information and writes into log
	getFFMpegFormatString(formatString, 4096, pFormatCtx, 0, url, 0);
	LoggerInfo(formatString);
}

AVStream *FFMpegAVInOutComponent::add_audio_stream(
	AVFormatContext *oc, 
	enum CodecID codec_id
)
{
    AVCodecContext *c;
    AVStream *st;

    st = av_new_stream(oc, 1);
    if (!st) LoggerError("Could not alloc stream");

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

AVStream *FFMpegAVInOutComponent::add_video_stream(
	AVFormatContext *oc, 
	enum CodecID codec_id
)
{
    AVCodecContext *c;
    AVStream *st;

    st = av_new_stream(oc, 0);
    if (!st) LoggerError("Could not alloc stream");

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

void FFMpegAVInOutComponent::open_audio(AVFormatContext *oc, AVStream *st)
{
    AVCodecContext *c;
    AVCodec *codec;

    c = st->codec;

    /* find the audio encoder */
    codec = avcodec_find_encoder(c->codec_id);
    if (!codec) LoggerError("Audio codec not found");

    /* open it */
    if (avcodec_open(c, codec) < 0) LoggerError("could not open audio codec");

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
		LoggerError(
			"Output audio packet size mistmatch. "
			"AudioVideoConfig_Remote_AvatarStream_AudioPacketSize=%d, "
			"Codec provided: %d",
			audioPacketSize,
			audio_input_frame_size
		);
	}
}

void FFMpegAVInOutComponent::open_video(AVFormatContext *oc, AVStream *st)
{
	AVCodec *codec;
	AVCodecContext *c = NULL;

	c = st->codec;

	/* find the video encoder */
	codec = avcodec_find_encoder(c->codec_id);
	if (!codec) LoggerError("Video codec not found");

	c = avcodec_alloc_context3(codec);
	if (!c) {
		LoggerError("Could not allocate audio codec context\n");
	}

	c = st->codec;

	/* open the codec */
	if (avcodec_open2(c, codec,NULL) < 0) LoggerError("could not open video codec");

	video_outbuf_size = 2000000;
	video_outbuf = (uint8_t *)av_malloc(video_outbuf_size);
}

//IFlow<AudioWrap *> implementation
void FFMpegAVInOutComponent::processData(AudioWrap * audioWrap)
{
	// PCM to U-law
	char buf[audioWrap->bufferSizeInBytes/2];
	for(int i=0; i<(audioWrap->bufferSizeInBytes/2); i++) {
		buf[i] = linear2ulaw(((short*)audioWrap->audioBuffer)[i]);
	}
	ost::OutgoingRTPPkt rtpPacket(buf, audioWrap->bufferSizeInBytes/2);
	//LoggerInfo("sizeof rtpPacket = %d", rtpPacket.getRawPacketSize());
	sendto(s, rtpPacket.getRawPacket(), rtpPacket.getRawPacketSize(), 0, (struct sockaddr *) &(other), len);
	return;


	// A packet will contain two audio frames and a video frame
	if (audioWrap->bufferSizeInBytes != audio_input_frame_size * 2) LoggerError("OJO wrong audio packet size: audioWrap->bufferSizeInBytes=%d\taudio_input_frame_size*2=%d", audioWrap->bufferSizeInBytes, audio_input_frame_size * 2);

	memcpy(samples, audioWrap->audioBuffer, audioWrap->bufferSizeInBytes);
	write_audio_frame(oc, audio_st);
}

void FFMpegAVInOutComponent::write_audio_frame(AVFormatContext *oc, AVStream *st)
{
    AVCodecContext *c;
    AVPacket pkt;
    av_init_packet(&pkt);

    c = st->codec;

	int res = avcodec_encode_audio(c, audio_outbuf, audio_outbuf_size, samples);
	if (res < 0) LoggerError("avcodec_encode_audio");

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
    	LoggerError("Error while writing audio frame");
    }
}

//IFlow implementation
void FFMpegAVInOutComponent::processData(OutgoingImage *image){
	write_video_frame(oc, video_st, image);
}

void  FFMpegAVInOutComponent::write_video_frame(
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
	if (ret != 0) LoggerError("Error writing video frame");
}

// init allocates avFrameFromScene, avFrameFFMpegCodec
// here the FFMpegAudioVideoDestination::Image is memcpied into avFrameFromScene
// and avFrameFromScene is converted into avFrameFFMpegCodec
// via libswscale

void FFMpegAVInOutComponent::fill_yuv_image(OutgoingImage *im) {
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
		LoggerError("ffmpeg: Cannot initialize the conversion context");
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

	if (res == -1) LoggerError("Error in sws_scale");
}

AVFrame *FFMpegAVInOutComponent::alloc_picture(
	enum PixelFormat pix_fmt, 
	int width, 
	int height
)
{
    AVFrame *picture;
    uint8_t *picture_buf;
    int size;

    picture = avcodec_alloc_frame();
    if (!picture) LoggerError("avcodec_alloc_frame");

    size = avpicture_get_size(pix_fmt, width, height);

	picture_buf = (uint8_t *)av_malloc(size);
	if (!picture_buf) LoggerError("avcodec_alloc_frame");
    
	avpicture_fill(
		(AVPicture *)picture, 
		picture_buf,
        pix_fmt, 
		width, 
		height
	);
    
	return picture;
}

int FFMpegAVInOutComponent::ff_lockmgr(void **mutex, enum AVLockOp op){
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

void FFMpegAVInOutComponent::my_log_callback(void *ptr, int level, const char *fmt, va_list vargs)
{
	if (level > av_log_get_level())
		return;

	//vprintf(fmt, vargs);
	//LoggerInfo(string(string("Log de av: ")+string(fmt)).c_str(), vargs);
	printf(fmt, vargs);
}

void FFMpegAVInOutComponent::process(void) {
	LoggerInfo("process");

	int err = av_read_frame(pFormatCtx, &packet);
	char buff [2000];
	av_strerror(err, buff, 2000);
	LoggerInfo("av_read_frame %s", buff);
	if (err < 0){
		//ERR("Error en av_read_frame()");
	}

	LoggerInfo("before url_feof");
	if (url_feof(pFormatCtx->pb))
		LoggerError("End of stream");
	LoggerInfo("url_feof");

	// != on error
	if (url_ferror(pFormatCtx->pb) != 0)
		LoggerError("Error reading audio/video input");
	LoggerInfo("url_ferror");

	// Is this a packet from the audio stream?
	if (packet.stream_index == audioStream)	processAudio();

	// Free the packet that was allocated by av_read_frame
	av_free_packet(&packet);
}

void FFMpegAVInOutComponent::processAudio() {
	LoggerInfo("processAudio");
	/*
	 A packet might have more than one frame, so you may have to
	 call avcodec_decode_audio3() several times to get all the data
	 out of the packet.
	 */
	int out_size;
	int res;

	// save packet attributes for av_free_packet
	uint8_t *initial_packet_data = packet.data;
	int initial_packet_size = packet.size;

	while (packet.size > 0) {
		out_size = AVCODEC_MAX_AUDIO_FRAME_SIZE;
		res = avcodec_decode_audio3(
				pAudioCodecCtx,
				(int16_t *)audio_buffer,
				&out_size,
				&packet
		);

		if (res < 0) {
			LoggerError("avcodec_decode_audio3");
		}
		if (out_size > 0) {
			int input_samples;
			int resampled_samples;
			input_samples = out_size >> 1;

		/*	resampled_samples = audio_resample(
					resamplingContext,
					//(__int16 *)resampled_audio_buffer,
					//(__int16 *)audio_buffer,
					(int16_t *)resampled_audio_buffer,
					(int16_t *)audio_buffer,
					input_samples
			);*/

			/*myAudioConsumer->consumeAudioBuffer(
					(unsigned short *)resampled_audio_buffer,
					2*resampled_samples
			);*/
			/*
			myAudioConsumer->consumeAudioBuffer(
								(int16_t *)audio_buffer,
								2*input_samples
								);
			*/
			AudioWrap audioWrap((int16_t *)audio_buffer, 2*input_samples);
			myAudioFlow->processData(&audioWrap);
#ifdef SAVE_FILE
			fwrite(audio_buffer, 2*input_samples, 1, file);
#endif
		}
		packet.size -= res;
		packet.data += res;
	}
	packet.data = initial_packet_data;
	packet.size = initial_packet_size;
}

unsigned char FFMpegAVInOutComponent::linearToULawSample(short sample) {
	int sign;
	int exponent;
	int mantissa;
	unsigned char compressedByte;

	sign = ((~sample) >> 8) & 0x80;
	if (!(sign == 0x80)) {
		sample = (short) -sample;
	}
	if (sample > cClip) {
		sample = cClip;
	}
	if (sample >= 256) {
		exponent = (int) muLawCompressTable[(sample >> 8) & 0x7F];
		mantissa = (sample >> (exponent + 3)) & 0x0F;
		compressedByte = (exponent << 4) | mantissa;
	} else {
		compressedByte = sample >> 4;
	}
	compressedByte ^= (sign ^ 0x55);
	return compressedByte;
}

unsigned char FFMpegAVInOutComponent::linear2ulaw(short pcm_val) {
	int mask;
	int seg;
	unsigned char  uval;

	/* Get the sign and the magnitude of the value. */
	if (pcm_val < 0) {
		pcm_val = (short) (cBias - pcm_val);
		mask = 0x7F;
	} else {
		pcm_val += cBias;
		mask = 0xFF;
	}

	/* Convert the scaled magnitude to segment number. */
	seg = search(pcm_val, seg_end, 8);

	/*
	 * Combine the sign, segment, quantization bits;
	 * and complement the code word.
	 */
	if (seg >= 8) /* out of range, return maximum value. */ {
		return (unsigned char)(0x7F ^ mask);
	} else {
		uval = (unsigned char)((seg << 4) | ((pcm_val >> (seg + 3)) & 0xF));
		return  (unsigned char)(uval ^ mask);
	}

}

int FFMpegAVInOutComponent::search(int val, short* table, int size) {
	int i;

	for (i = 0; i < size; i++) {
		if (val <= table[i]) {
			return (i);
		}
	}
	return (size);
}

/*
* Convert input of 8-bit ulaw to signed 16-bit linear sample.
*/
int16_t FFMpegAVInOutComponent::pcmu_to_l16(unsigned int ulawbyte)
{
  static int exp_lut[8] = { 0, 132, 396, 924, 1980, 4092, 8316, 16764 };
  int sign, exponent, mantissa, sample;

  ulawbyte = ~ ulawbyte;
  sign = ( ulawbyte & 0x80 );
  exponent = ( ulawbyte >> 4 ) & 0x07;
  mantissa = ulawbyte & 0x0F;
  sample = exp_lut[exponent] + ( mantissa << ( exponent + 3 ) );
  if (sign) sample = -sample;

  return sample;
} /* pcmu_to_l16 */

/*int FFMpegAVInOutComponent::Component_avio_open(AVIOContext **s, const char *filename, int flags) {
	URLContext *h;
	int err;

	err = Component_ffurl_open(&h, filename, flags);
	if (err < 0)
		return err;
	err = Component_ffio_fdopen(s, h);
	if (err < 0) {
		ffurl_close(h);
		return err;
	}
	return 0;
}

int FFMpegAVInOutComponent::Component_ffurl_open(URLContext **puc, const char *filename, int flags) {
	int ret = ffurl_alloc(puc, filename, flags);
	if (ret)
		return ret;
	ret = Component_ffurl_connect(*puc);
	if (!ret)
		return 0;
	ffurl_close(*puc);
	*puc = NULL;
	return ret;
}

int FFMpegAVInOutComponent::Component_ffurl_connect(URLContext* uc) {
	int err = uc->prot->url_open(uc, uc->filename, uc->flags);
	if (err)
		return err;
	uc->is_connected = 1;
	//We must be careful here as ffurl_seek() could be slow, for example for http
	if(   (uc->flags & (AVIO_WRONLY | AVIO_RDWR))
			|| !strcmp(uc->prot->name, "file"))
		if(!uc->is_streamed && ffurl_seek(uc, 0, SEEK_SET) < 0)
			uc->is_streamed= 1;
	return 0;
}

int FFMpegAVInOutComponent::Component_ffio_fdopen(AVIOContext **s, URLContext *h)
{
    uint8_t *buffer;
    int buffer_size, max_packet_size;

    max_packet_size = h->max_packet_size;
    if (max_packet_size) {
        buffer_size = max_packet_size; // no need to bufferize more than one packet
    } else {
        buffer_size = IO_BUFFER_SIZE;
    }
    buffer = (uint8_t *)av_malloc(buffer_size);
    if (!buffer)
        return AVERROR(ENOMEM);

    *s = (AVIOContext*)av_mallocz(sizeof(AVIOContext));
    if(!*s) {
        av_free(buffer);
        return AVERROR(ENOMEM);
    }

    if (Component_ffio_init_context(*s, buffer, buffer_size,
                      //(h->flags & AVIO_WRONLY || h->flags & AVIO_RDWR), h,
    					h->flags, h,
                      (void*)ffurl_read, (void*)ffurl_write, (void*)ffurl_seek) < 0) {
        av_free(buffer);
        av_freep(s);
        return AVERROR(EIO);
    }
#if FF_API_OLD_AVIO
    (*s)->is_streamed = h->is_streamed;
#endif
    (*s)->seekable = h->is_streamed ? 0 : AVIO_SEEKABLE_NORMAL;
    (*s)->max_packet_size = max_packet_size;
    if(h->prot) {
        (*s)->read_pause = (int (*)(void *, int))h->prot->url_read_pause;
        (*s)->read_seek  = (int64_t (*)(void *, int, int64_t, int))h->prot->url_read_seek;
    }
    return 0;
}

int FFMpegAVInOutComponent::Component_ffio_init_context(AVIOContext *s,
                  unsigned char *buffer,
                  int buffer_size,
                  int write_flag,
                  void *opaque,
                  int (*read_packet)(void *opaque, uint8_t *buf, int buf_size),
                  int (*write_packet)(void *opaque, uint8_t *buf, int buf_size),
                  int64_t (*seek)(void *opaque, int64_t offset, int whence))
{
    s->buffer = buffer;
    s->buffer_size = buffer_size;
    s->buf_ptr = buffer;
    s->opaque = opaque;
    Component_url_resetbuf(s, write_flag);// ? AVIO_WRONLY : AVIO_RDONLY);
    s->write_packet = write_packet;
    s->read_packet = read_packet;
    s->seek = seek;
    s->pos = 0;
    s->must_flush = 0;
    s->eof_reached = 0;
    s->error = 0;
#if FF_API_OLD_AVIO
    s->is_streamed = 0;
#endif
    s->seekable = AVIO_SEEKABLE_NORMAL;
    s->max_packet_size = 0;
    s->update_checksum= NULL;
    if(!read_packet && !write_flag){
        s->pos = buffer_size;
        s->buf_end = s->buffer + buffer_size;
    }
    s->read_pause = NULL;
    s->read_seek  = NULL;
    return 0;
}

#if FF_API_URL_RESETBUF
int FFMpegAVInOutComponent::Component_url_resetbuf(AVIOContext *s, int flags)
#else
static int FFMpegAVInOutComponent::Component_url_resetbuf(AVIOContext *s, int flags)
#endif
{
#if FF_API_URL_RESETBUF
    if (flags & AVIO_RDWR)
        return AVERROR(EINVAL);
#else
    assert(flags == AVIO_WRONLY || flags == AVIO_RDONLY);
#endif

    if (flags & AVIO_WRONLY) {
        s->buf_end = s->buffer + s->buffer_size;
        s->write_flag = 1;
    } else {
        s->buf_end = s->buffer;
        s->write_flag = 0;
    }
    return 0;
}*/
