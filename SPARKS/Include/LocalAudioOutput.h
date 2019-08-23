/// @file LocalAudioOutput.h
/// @brief LocalAudioOutput class declaration.

#ifndef __LOCAL_AUDIO_OUTPUT_H
#define __LOCAL_AUDIO_OUTPUT_H


#include "AudioOutput.h"

class LocalAudioOutput :
	public AudioOutput
{
public:
	LocalAudioOutput(LipSynch *l) : AudioOutput(l) {}
	void init(void);
	void quit(void);
};


#endif
