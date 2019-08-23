/// @file RemoteFliteVoiceSpark.h
/// @brief Component RemoteFliteVoiceSpark main class.


#ifndef __RemoteFliteVoiceSpark_H
#define __RemoteFliteVoiceSpark_H

#include "flite.h"

#include "Component.h"
//#include "IVoice.h"
#include "IFlow.h"

//#include "IAsyncFatalErrorHandler.h"
#include "AudioQueue.h"
#include "IAudioQueue.h"

extern "C" {
#include <libavcodec/avcodec.h>
#include <libavformat/avformat.h>
#include <libswscale/swscale.h>
#include <libavcodec/opt.h>
}

/// @brief This is the main class for component RemoteFliteVoiceSpark.
///
/// Flite TTS engine

class RemoteFliteVoiceSpark :
	public Component,

	public IFlow<char*>,
	public IAudioQueue
{
public:
		RemoteFliteVoiceSpark(
				char *instanceName,
				ComponentSystem *cs
		) : Component(instanceName, cs)
		{}

private:

	void initializeRequiredInterfaces(){
	}
public:
	void init(void);
	void quit(void);

	//IFlow implementation
	void processData(char *);

	// IAudioQueue
	int getStoredAudioSize();
	void queueAudioBuffer(char *buffer, int size);
	void dequeueAudioBuffer(char *buffer, int size);

private:
	static int callback_audio_stream_chunk(const cst_wave *, int, int, int, void *);
	static void *threadProcedure(void *);
private:
	char voice[256];
	int audioPacketSize;
	char * text;
	static const int sampleRate = 11025;
	bool hiloTerminado;

	AudioQueue audioQueue;
	cst_voice *v;
	cst_audio_streaming_info *asi;

	pthread_t thread_id;
};

#endif
