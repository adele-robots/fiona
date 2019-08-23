/// @file TRONReaderComponent.cpp
/// @brief TRONReaderComponent class implementation.

#include "stdAfx.h"
#include "TRONReaderComponent.h"

#include "Configuration.h"
#include "ErrorHandling.h"
#include "Logger.h"
#include "dumpFormat.h"
#include "FFImage.h"
#include "AudioWrap.h"

#ifdef _WIN32
#else
extern "C"
/*Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "TRONReaderComponent")) {
			return new TRONReaderComponent(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}*/
#endif
/* Aquí vamos a tener que poner algo indicando que se coge el stream del usuario y se modifica.
 * Otra cosa que hay que hacer el comprobar la frecuencia y, si es igual, no hacer el resample,
 * que lleva su tiempo y por tanto y retraso(unos 2s).
 * En caso de no poder acceder al stream de entrada(operatorcam) habrá que reintentarlo alguna
 * vez más hasta conseguirlo o hasta que finalice un timeout.
 * Que no se pueda acceder al stream se puede deber a que el avatar se inició antes de que el
 * operador acepte la conexión o porque directamente no hay operador.
 */
void TRONReaderComponent::init() {
	isStreamOpened = false;
	// Get input rtmp url from config
	stream = getComponentConfiguration()->getString(const_cast<char *>("AudioVideoConfig_Remote_WebcamStream_URL"));
	string room("room");
	string user("usercam");
	string op("operatorcam");
	stream.replace(stream.find(room), room.length(), getGlobalConfiguration()->getString(const_cast<char *>("room")));
	stream = stream.replace(stream.find(user),user.length(),op.c_str(),op.length());

	img_convert_ctx = NULL;
}

void TRONReaderComponent::open_file(char *ffmpegURL){

	// av_malloc implements memory aligment necessary for ffmpeg
	audio_buffer = (uint8_t *)av_malloc(AVCODEC_MAX_AUDIO_FRAME_SIZE);

	// Don't know exactly why resampling... it's a matter with ASR AudioConsumer (Hz allowed)..
	resampled_audio_buffer = (uint8_t *)av_malloc(AVCODEC_MAX_AUDIO_FRAME_SIZE);

	// Register all formats and codecs
	av_register_all();

	if (av_open_input_file(&pFormatCtx, ffmpegURL, NULL, 0, NULL) !=0) {
		ERR("ffmpeg: could not open %s", ffmpegURL);
	}

	LoggerInfo("[FIONA-logger]TRONReaderComponent::operatorConnected despues av_open_input_file");

	pFormatCtx->probesize = 1000;

	if (av_find_stream_info(pFormatCtx) < 0) {
		ERR("Couldn't find stream information");
	}

	LoggerInfo("[FIONA-logger]TRONReaderComponent::operatorConnected despues av_find_stream_info");

	// Find the first video stream
	/*videoStream = -1;
	for(unsigned int i = 0; i < pFormatCtx->nb_streams; i++) {
		if (pFormatCtx->streams[i]->codec->codec_type==AVMEDIA_TYPE_VIDEO) {
			videoStream=i;
			break;
		}
	}
	if (videoStream == -1) {
		ERR("ffmpeg: Didn't find a video stream");
	}*/

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
	//pVideoCodecCtx=pFormatCtx->streams[videoStream]->codec;
	// Get a pointer to the codec context for the audio stream
	pAudioCodecCtx=pFormatCtx->streams[audioStream]->codec;

	// Find the decoder for the video stream
	/*pVideoCodec=avcodec_find_decoder(pVideoCodecCtx->codec_id);
	if (pVideoCodec==NULL)
		ERR("ffmpeg: unsupported video codec");
	// Open video codec
	if (avcodec_open(pVideoCodecCtx, pVideoCodec)<0)
		ERR("Could not open video codec");*/

	// Find the decoder for the audio stream
	pAudioCodec=avcodec_find_decoder(pAudioCodecCtx->codec_id);
	if (pAudioCodec==NULL)
		ERR("ffmpeg: unsupported audio codec");

	if (pAudioCodecCtx->channels != 1) {
		ERR("Assuming one audio channel, input has %d", pAudioCodecCtx->channels);
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

	if (!resamplingContext) ERR("Failed to initialize resampling");

	// RESAMPLING STUFF ENDS

	// Open audio codec
	if (avcodec_open(pAudioCodecCtx, pAudioCodec)<0) ERR("Could not open audio codec");

	// Allocate video frame
	//pFrame=avcodec_alloc_frame();

	char formatString[4096];
	//like built-in ffmpeg function av_dump_format (dumps info about file onto standard error)
	//but retrieves the information and writes into log
	getFFMpegFormatString(formatString, 4096, pFormatCtx, 0, ffmpegURL, 0);
	LoggerInfo(formatString);

}

void TRONReaderComponent::processVideo() {
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

	myImageFlow->processData(ffImage);

}

void TRONReaderComponent::processAudio() {
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

			resampled_samples = audio_resample(resamplingContext, (int16_t *)resampled_audio_buffer, (int16_t *)audio_buffer, input_samples);

			AudioWrap audioWrap((int16_t *)resampled_audio_buffer, 2*resampled_samples);
			myAudioFlow->processData(&audioWrap);
		}
		packet.size -= res;
		packet.data += res;
	}
	packet.data = initial_packet_data;
	packet.size = initial_packet_size;
}

void TRONReaderComponent::operatorConnected(){

	LoggerInfo("[FIONA-logger]TRONReaderComponent::operatorConnected Operator connected notification received");

	if (!isStreamOpened) {

		LoggerInfo("[FIONA-logger]TRONReaderComponent::operatorConnected antes");

		open_file(const_cast<char *>(stream.c_str()));
		isStreamOpened = true;

		LoggerInfo("[FIONA-logger]TRONReaderComponent::operatorConnected despues");
	}
}

void TRONReaderComponent::process(void) {

	if(isStreamOpened) {

		int err = av_read_frame(pFormatCtx, &packet);
		if (err < 0)
			ERR("Error en av_read_frame()");

		if (url_feof(pFormatCtx->pb))
			ERR("End of stream");

		// != on error
		if (url_ferror(pFormatCtx	->pb) != 0)
			ERR("Error reading audio/video input");

		// Is this a packet from the video stream?
		//if (packet.stream_index == videoStream)	processVideo();

		// Is this a packet from the audio stream?
		if (packet.stream_index == audioStream)	processAudio();

		// Free the packet that was allocated by av_read_frame
		av_free_packet(&packet);

	}
}

void TRONReaderComponent::quit() {
	//Free the YUV frame
	//av_free(pFrame);
	//Close the codec ||||| Why only video??
	//avcodec_close(pVideoCodecCtx);
	//Close the file
	av_close_input_file(pFormatCtx);
}
