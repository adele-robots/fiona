/// @file ASRSpark.h
/// @brief Component ASRSpark main class.


#ifndef __ASRSpark_H
#define __ASRSpark_H


#include "Component.h"

// Provided interface
#include "IAudioConsumer.h"


// Required interface
#include "IVoice.h"


// Include para pocketsphinx
#include <pocketsphinx.h>

// Includes sphinx
#include <sphinxbase/err.h>
#include <sphinxbase/ad.h>
#include <sphinxbase/cont_ad.h>

/// @brief This is the main class for component ASRSpark.
///
/// 

class ASRSpark :
	public Component,
	//**Component class must extend provided interfaces**
	public IAudioConsumer
{
public:
		ASRSpark(
				char *instanceName,
				ComponentSystem *cs
		) : Component(instanceName, cs)
		{}

private:
	// Required interface initialization
	IVoice *myVoice;

	void initializeRequiredInterfaces() {	
		requestRequiredInterface<IVoice>(&myVoice);
	}

public:
	//Mandatory
	void init(void);
	void quit(void);
	
	//**To change for convenience**
	// IAudioConsumer implementation
	#ifdef _WIN32
		void consumeAudioBuffer(__int16 *audioBuffer, int bufferSizeInBytes);
	#else
		void consumeAudioBuffer(int16_t *audioBuffer, int bufferSizeInBytes);
	#endif
private:
	//Put class attributes here
		ps_decoder_t *ps;
		cmd_ln_t *config;
private:
	//Put class private methods here
	
};

#endif
