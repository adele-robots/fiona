/// @file AVInput.h
/// @brief AVInput class declaration.

#ifndef __AV_INPUT_H
#define __AV_INPUT_H

#include "Configuration.h"
#include "AudioInput.h"
#include "VideoInput.h"
#include "IAudioConsumer.h"
#include "IVideoConsumer.h"
#include "FFMpegAudioVideoSource.h"
#include "IAsyncFatalErrorHandler.h"


class AVInput
{
private:
	bool isLocal;
	FFMpegAudioVideoSource *fFMpegAudioVideoSource;

public:
	AVInput(
		bool isLocal,
		IAudioConsumer *,
		IVideoConsumer *,
		IAsyncFatalErrorHandler *
	);
	void init();
	void quit(void);
	void start(void);
	void stop(void);

public:
	AudioInput *audioInput;
	VideoInput *videoInput;
	IAudioConsumer *audioConsumer;
	IVideoConsumer *videoConsumer;
	IAsyncFatalErrorHandler *asyncFatalErrorHandler;
	bool isVideoCaptureEnabled;

private:
	void openFFMpegInputStream(void);
};


#endif
