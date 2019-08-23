/// @file RemoteAudioOutput.h
/// @brief RemoteAudioOutput class declaration.


#ifndef __REMOTE_AUDIO_OUTPUT_H
#define __REMOTE_AUDIO_OUTPUT_H

#include "AudioOutput.h"
#include "AudioQueue.h"


class RemoteAudioOutput : public AudioOutput {
public:
	RemoteAudioOutput(LipSynch *l) : AudioOutput(l) {}
	void init(void);
	void quit(void);

public:
	bool speaking;
	AudioQueue audioQueue;
};


#endif
