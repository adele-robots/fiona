/// @file ASRGoogleSpark.h
/// @brief Component ASRGoogleSpark main class.


#ifndef __ASRGoogleSpark_H
#define __ASRGoogleSpark_H


#include "Component.h"

// Provided interface
#include "IFlow.h"
#include "IThreadProc.h"

#include "AudioQueue.h"
#include "StopWatch.h"

#include "curlpp/cURLpp.hpp"
#include "curlpp/Easy.hpp"
#include "curlpp/Options.hpp"

/*struct MemoryStruct {
  char *memory;
  size_t size;
};*/

/// @brief This is the main class for component ASRGoogleSpark.
///
/// 

class ASRGoogleSpark :
	public Component,
	//**Component class must extend provided interfaces**
	public IFlow<AudioWrap *>,
	public IThreadProc
{
public:
		ASRGoogleSpark(
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

	// IThreadProc implementation
	void process();

private:
	//Put class attributes here
	// Variable para almacenar las muestras de audio recibidas antes de interpretarlas
	AudioQueue audioQueue;

	uint8_t * buffer;
	unsigned int size;
	float noiseVolumeThreshold;
	float silence;
	bool sendData;
	string APIKey;
	string lang;
	string userIp;
	int sampleRate;
	StopWatch watcher;
	//struct MemoryStruct chunk;
	pthread_cond_t condition_send;
	pthread_mutex_t mutex;

public:
	IFlow<char *> *myCharFlow;

private:
	void doRequest();
	static size_t WriteMemoryCallback(void *contents, size_t size, size_t nmemb, void *userp);
	
};

#endif
