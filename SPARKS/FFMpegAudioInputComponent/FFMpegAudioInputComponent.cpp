/// @file FFMpegAudioInputComponent.cpp
/// @brief FFMpegAudioInputComponent class implementation.

#include "stdAfx.h"
#include "FFMpegAudioInputComponent.h"

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
	if (!strcmp(componentType, "FFMpegAudioInputComponent")) {
			return new FFMpegAudioInputComponent(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif

void FFMpegAudioInputComponent::init() {
	isStreamOpened = false;
	// Get input rtmp url from config
	stream = getComponentConfiguration()->getString(const_cast<char *>("AudioVideoConfig_Remote_WebcamStream_URL"));
	string room("room");
	stream.replace(stream.find(room), room.length(), getGlobalConfiguration()->getString(const_cast<char *>("room")));

	// av_malloc implements memory aligment necessary for ffmpeg
	audio_buffer = (uint8_t *)av_malloc(AVCODEC_MAX_AUDIO_FRAME_SIZE);
	// Don't know exactly why resampling... it's a matter with ASR AudioConsumer (Hz allowed)..
	resampled_audio_buffer = (uint8_t *)av_malloc(AVCODEC_MAX_AUDIO_FRAME_SIZE);

	// Register all formats and codecs
	av_register_all();
	open_file(const_cast<char *>(stream.c_str()));
}

void FFMpegAudioInputComponent::open_file(char *ffmpegURL){
	if (av_open_input_file(&pFormatCtx, ffmpegURL, NULL, 0, NULL) !=0) {
		LoggerError("ffmpeg: could not open %s", ffmpegURL);
		//ERR("ffmpeg: could not open %s", ffmpegURL);
		return;
	}

	pFormatCtx->probesize = 1000;

	if (av_find_stream_info(pFormatCtx) < 0) {
		ERR("Couldn't find stream information");
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

	// Get a pointer to the codec context for the audio stream
	pAudioCodecCtx=pFormatCtx->streams[audioStream]->codec;

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

	char formatString[4096];
	//like built-in ffmpeg function av_dump_format (dumps info about file onto standard error)
	//but retrieves the information and writes into log
	getFFMpegFormatString(formatString, 4096, pFormatCtx, 0, ffmpegURL, 0);
	LoggerInfo(formatString);

	isStreamOpened = true;

}

void FFMpegAudioInputComponent::processAudio() {
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

void FFMpegAudioInputComponent::process(void) {

	// TODO: Poner StopWatch y esperar tiempo para espera pasiva

	if(isStreamOpened) {

		int err = av_read_frame(pFormatCtx, &packet);
		if (err < 0)
			ERR("Error en av_read_frame()");

		if (url_feof(pFormatCtx->pb))
			ERR("End of stream");

		// != on error
		if (url_ferror(pFormatCtx	->pb) != 0)
			ERR("Error reading audio/video input");

		// Is this a packet from the audio stream?
		if (packet.stream_index == audioStream)	processAudio();

		// Free the packet that was allocated by av_read_frame
		av_free_packet(&packet);

	}
	else
		open_file(const_cast<char *>(stream.c_str()));
}

void FFMpegAudioInputComponent::quit() {
	//Close the file
	av_close_input_file(pFormatCtx);
}
