/// @file ASRAndroidSpark.h
/// @brief Component ASRAndroidSpark main class.


#ifndef __ASRAndroidSpark_H
#define __ASRAndroidSpark_H


#include "Component.h"

// Provided interface
#include "IFlow.h"

#include "AudioQueue.h"
#include "StopWatch.h"

#include "curlpp/cURLpp.hpp"
#include "curlpp/Easy.hpp"
#include "curlpp/Options.hpp"
#include <curlpp/Exception.hpp>

#include <queue>

/*#define OVERRIDE_SPEEX_FREE
static inline void speex_free (void *ptr)
{
   free(ptr);
   ptr = NULL;
}*/
#include <speex/speex.h>
#include <speex/speex_header.h>
#include <speex/speex_stereo.h>
#include <speex/speex_preprocess.h>

/*struct MemoryStruct {
  char *memory;
  size_t size;
};*/

/// @brief This is the main class for component ASRAndroidSpark.
///
/// 

class ASRAndroidSpark :
	public Component,
	//**Component class must extend provided interfaces**
	public IFlow<AudioWrap *>
{
public:
		ASRAndroidSpark(
				char *instanceName,
				ComponentSystem *cs
		) : Component(instanceName, cs)
		{}

private:
	// Required interface initialization

	void initializeRequiredInterfaces() {	
		requestRequiredInterface<IFlow<char *> >(&myCharFlow);
	}

public:
	//Mandatory
	void init(void);
	void quit(void);
	
	//**To change for convenience**
	// IFlow<AudioWrap *> implementation
	void processData(AudioWrap *);

private:
	//Put class attributes here
	// Variable para almacenar las muestras de audio recibidas antes de interpretarlas
	AudioQueue audioQueue;

	uint8_t * buffer;
	unsigned int size;
	float noiseVolumeThreshold;
	float silence;
	bool sendData;
	string url;
	string userDirectory;
	StopWatch watcher;

	bool		firstBufferQueued;
	queue<pair<int16_t*, size_t> >	cola_inicio;

	int FRAME_SIZE;
	int SAMPLING_RATE;
	int NOISE_SUPPRESS;
	int PROB;

	SpeexPreprocessState *preprocess;

public:
	IFlow<char *> *myCharFlow;

private:
	void doRequest();
	/*void initPreprocess(int frame_size);*/
	
	int writeDebug(curl_infotype, char *, size_t);
	size_t WriteCallback(char* ptr, size_t size, size_t nmemb);

};

#endif
