/// @file FFMpegReaderComponent.cpp
/// @brief FFMpegContainerReaderComponent class implementation.

#include "stdAfx.h"
#include "FFMpegReaderComponent.h"

#include "Configuration.h"
#include "ErrorHandling.h"
#include "Logger.h"
#include "dumpFormat.h"
#include "FFImage.h"
#include "AudioWrap.h"

#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "FFMpegReaderComponent")) {
			return new FFMpegReaderComponent(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif

void FFMpegReaderComponent::init() {
	// Get input rtmp url from config
	char inputFfmpegStreamURL[256];
	getComponentConfiguration()->getString(const_cast<char *>("AudioVideoConfig_Remote_WebcamStream_URL"), inputFfmpegStreamURL, 256);
	std::string url(inputFfmpegStreamURL);
	url.replace(url.find("room"), 4, getGlobalConfiguration()->getString(const_cast<char *>("room")));

	img_convert_ctx = NULL;

	// av_malloc implements memory aligment necessary for ffmpeg
	audio_buffer = (uint8_t *)av_malloc(AVCODEC_MAX_AUDIO_FRAME_SIZE);
	// Don't know exactly why resampling... it's a matter with ASR AudioConsumer (Hz allowed)..
	resampled_audio_buffer = (uint8_t *)av_malloc(AVCODEC_MAX_AUDIO_FRAME_SIZE);

	// Register all formats and codecs
	av_register_all();
	open_file(const_cast<char *>(url.c_str()));
}

void FFMpegReaderComponent::open_file(char *ffmpegURL){
	if (av_open_input_file(&pFormatCtx, ffmpegURL, NULL, 0, NULL) !=0) {
		ERR("ffmpeg: could not open %s", ffmpegURL);
	}

	if (av_find_stream_info(pFormatCtx) < 0) {
		ERR("Couldn't find stream information");
	}

	// Find the first video stream
	videoStream = -1;
	for(unsigned int i = 0; i < pFormatCtx->nb_streams; i++) {
		if (pFormatCtx->streams[i]->codec->codec_type==AVMEDIA_TYPE_VIDEO) {
			videoStream=i;
			break;
		}
	}
	if (videoStream == -1) {
		ERR("ffmpeg: Didn't find a video stream");
	}

	// Find the first audio stream
	audioStream = -1;
	for(unsigned int i = 0; i < pFormatCtx->nb_streams; i++) {
		if (pFormatCtx->streams[i]->codec->codec_type==AVMEDIA_TYPE_AUDIO) {
			audioStream=i;
			break;
		}
	}
	if (audioStream == -1) {
		ERR("ffmpeg: Didn't find an audio stream");
	}

	// Get a pointer to the codec context for the video stream
	pVideoCodecCtx=pFormatCtx->streams[videoStream]->codec;
	// Get a pointer to the codec context for the audio stream
	pAudioCodecCtx=pFormatCtx->streams[audioStream]->codec;

	// Find the decoder for the video stream
	pVideoCodec=avcodec_find_decoder(pVideoCodecCtx->codec_id);
	if (pVideoCodec==NULL)
		ERR("ffmpeg: unsupported video codec");
	// Open video codec
	if (avcodec_open(pVideoCodecCtx, pVideoCodec)<0)
		ERR("Could not open video codec");

	// Find the decoder for the audio stream
	pAudioCodec=avcodec_find_decoder(pAudioCodecCtx->codec_id);
	if (pAudioCodec==NULL)
		ERR("ffmpeg: unsupported audio codec");

	if (pAudioCodecCtx->channels != 1) {
		ERR("Assuming one audio channel, input has %d", pAudioCodecCtx->channels);
	}

	// RESAMPLING STUFF BEGINS
	int sampleRate = 44100;
	try {
		sampleRate = getComponentConfiguration()->getInt(const_cast<char *>("Out_Sample_Rate"));
	} catch(Exception &e) {
		LoggerWarn("Out_Sample_Rate not found. Setting to default: %d", sampleRate);
	}

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

	if (!resamplingContext) ERR("Failed to initialize resampling");

	// RESAMPLING STUFF ENDS

	// Open audio codec
	if (avcodec_open(pAudioCodecCtx, pAudioCodec)<0) ERR("Could not open audio codec");

	// Allocate video frame
	pFrame=avcodec_alloc_frame();

	char formatString[4096];
	//like built-in ffmpeg function av_dump_format (dumps info about file onto standard error)
	//but retrieves the information and writes into log
	getFFMpegFormatString(formatString, 4096, pFormatCtx, 0, ffmpegURL, 0);
	LoggerInfo(formatString);

}

void FFMpegReaderComponent::processVideo() {
	// Decode video packet
	avcodec_decode_video2(pVideoCodecCtx,pFrame,&frameFinished,&packet);

	// Did we get a video frame? if not, continue processing packets
	// until the frame is fully filled
	if (!frameFinished) return;

	// convert frame colorspace to OpenCV's BGR24, generate IplImage
	img_convert_ctx = sws_getCachedContext(
		img_convert_ctx,
		pVideoCodecCtx->width,
		pVideoCodecCtx->height,
		pVideoCodecCtx->pix_fmt,
		pVideoCodecCtx->width,
		pVideoCodecCtx->height,
		PIX_FMT_BGR24,
		SWS_BICUBIC,
		NULL, NULL, NULL
	);

	if (img_convert_ctx == NULL) {
		ERR("ffmpeg: Cannot initialize the conversion context");
	}

	FFImage *ffImage = new FFImage(
			pVideoCodecCtx->width,
			pVideoCodecCtx->height
	);

	int res;
	res = sws_scale(
		img_convert_ctx,
		pFrame->data,
		pFrame->linesize,
		0,
		pVideoCodecCtx->height,
		ffImage->avFrame.data,
		ffImage->avFrame.linesize
	);

	if (res == -1)
		ERR("Error in sws_scale");
	//** Added Guille
	//Free the swscaler context img_convert_ctx
	//sws_freeContext(img_convert_ctx);
	//img_convert_ctx = NULL;

	myFlow->processData(ffImage);

	delete ffImage;

}

void FFMpegReaderComponent::processAudio() {
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
			ERR("avcodec_decode_audio3");
		}
		if (out_size > 0) {
			int input_samples;
			int resampled_samples;
			input_samples = out_size >> 1;

			resampled_samples = audio_resample(
					resamplingContext,
					//(__int16 *)resampled_audio_buffer,
					//(__int16 *)audio_buffer,
					(int16_t *)resampled_audio_buffer,
					(int16_t *)audio_buffer,
					input_samples
			);

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
			AudioWrap audioWrap((int16_t *)resampled_audio_buffer, 2*resampled_samples);
			myAudioFlow->processData(&audioWrap);
		}
		packet.size -= res;
		packet.data += res;
	}
	packet.data = initial_packet_data;
	packet.size = initial_packet_size;
}

void FFMpegReaderComponent::process(void) {

	// TODO: Poner StopWatch y esperar tiempo para espera pasiva

	int err = av_read_frame(pFormatCtx, &packet);
	if (err < 0)
		ERR("Error en av_read_frame()");

	if (url_feof(pFormatCtx->pb))
		ERR("End of stream");

	// != on error
	if (url_ferror(pFormatCtx	->pb) != 0)
		ERR("Error reading audio/video input");

	// Is this a packet from the video stream?
	if (packet.stream_index == videoStream)	processVideo();

	// Is this a packet from the audio stream?
	if (packet.stream_index == audioStream)	processAudio();

	// Free the packet that was allocated by av_read_frame
	av_free_packet(&packet);
}

void FFMpegReaderComponent::quit() {
	//Free the YUV frame
	av_free(pFrame);
	//Close the codec ||||| Why only video??
	avcodec_close(pVideoCodecCtx);
	//Close the file
	av_close_input_file(pFormatCtx);
}
