/**
 * @file VoiceFilterSpark.cpp
 *
 * @author Abel Castro Suárez
 * @version 0.1, 07/04/2014
 */

//#include "stdAfx.h"

#include "include/soundtouch/SoundTouch.h"
#include "VoiceFilterSpark.h"

#include <AudioWrap.h>
#include <cstdlib>
#include <cstring>
#include <limits>
#include <cmath>


#ifdef _WIN32
#else
extern "C"
{
	Component *createComponent(char *componentInstanceName,
								char *componentType,
								ComponentSystem *componentSystem)
	{
		if (!strcmp(componentType, "VoiceFilterSpark"))
		{
			return new VoiceFilterSpark(componentInstanceName, componentSystem);
		}
		else
		{
			return NULL;
		}
	}
}
#endif

void VoiceFilterSpark::init(void)
{
	st = new soundtouch::SoundTouch();
	st->setPitchSemiTones(getComponentConfiguration()->getFloat("Effect_Pitch_Semitones"));
	st->setChannels(getComponentConfiguration()->getInt("Input_Channels"));
	st->setSampleRate(getComponentConfiguration()->getInt("Input_Sample_Rate"));
	st->setSetting(SETTING_USE_QUICKSEEK, 1);
	st->setSetting(SETTING_USE_AA_FILTER, 1);

	noiseVolumeThreshold = getComponentConfiguration()->getFloat("Noise_Threshold");
}

void VoiceFilterSpark::quit(void)
{
	delete st;
}

void VoiceFilterSpark::processData(AudioWrap* audioChunk)
{
	AudioWrap* outputAudioChunk = applyEffect(audioChunk);

	if (outputAudioChunk)
	{
		myFlowAudio->processData(outputAudioChunk);
	}
}

AudioWrap* VoiceFilterSpark::applyEffect(AudioWrap* inputAudioChunk)
{
	int sampleCount = inputAudioChunk->bufferSizeInBytes / 2;

	bool voiceDetected = false;

	for (int i = 0; i<sampleCount; i++)
	{
		if (abs(inputAudioChunk->audioBuffer[i]) > std::numeric_limits<int16_t>::max() * noiseVolumeThreshold)
		{
			voiceDetected = true;
			break;
		}
	}

	if(outputAudioChunk)
		delete outputAudioChunk;

	outputAudioChunk = nullptr;

	if (voiceDetected)
	{
		st->putSamples(inputAudioChunk->audioBuffer, sampleCount);

		int processedSamples = st->numSamples();

		int16_t* outputBuffer = new int16_t[processedSamples];

		int receivedSamples = st->receiveSamples(outputBuffer, processedSamples);

		outputAudioChunk = new AudioWrap(outputBuffer, receivedSamples * 2);
	}

	return outputAudioChunk;
}
