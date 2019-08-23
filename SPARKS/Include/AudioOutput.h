/// @file AudioOutput.h
/// @brief AudioOutput class declaration.


#ifndef __AUDIO_OUTPUT_H
#define __AUDIO_OUTPUT_H


#include "LipSynch.h"

class AudioOutput
{
public:
	virtual void init() = 0;
	virtual void quit() = 0;

public:
	LipSynch *lipSynch;

protected:
	AudioOutput(LipSynch *l) : lipSynch(l) {}
};


#endif
