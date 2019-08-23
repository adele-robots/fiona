/**
 * @file VoiceFilterSpark.h
 *
 * @author Abel Castro Suárez
 * @version 0.1, 07/04/2014
 */

#ifndef __VoiceFilterSpark_H
#define __VoiceFilterSpark_H


#include "Component.h"
#include "IFlow.h"


class VoiceFilterSpark :
	public Component,
	public IFlow<AudioWrap*>
{
public:
		VoiceFilterSpark(
				char *instanceName,
				ComponentSystem *cs
		) : Component(instanceName, cs)
		{}

private:
	IFlow<AudioWrap*>* myFlowAudio;

	void initializeRequiredInterfaces()
	{
		requestRequiredInterface<IFlow<AudioWrap*> >(&myFlowAudio);
	}

public:
	void init(void);
	void quit(void);
	void processData(AudioWrap*);
	
private:

	/**
	 * @brief Applies a pitch shift effect algorithm to the received input.
	 *
	 * Examines the input chunk and checks if contained audio volume is
	 * over noise threshold value, in this case the input is considered valid
	 * and its processed applying a pitch shift algorithm.
	 *
	 * @param inputAudioChunk The input audio fragment
	 *
	 * @return The processed audio or nullptr if the input was considered invalid (noise).
	 */
	AudioWrap* applyEffect(AudioWrap* inputAudioChunk);

private:
	soundtouch::SoundTouch* st;
	AudioWrap* outputAudioChunk;
	float noiseVolumeThreshold;
};

#endif
