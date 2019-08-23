/// @file IAudioConsumer.h
/// @brief IAudioConsumer interface definition.


#ifndef __I_AUDIO_CONSUMER_H
#define __I_AUDIO_CONSUMER_H

#include <stdint.h>


class IAudioConsumer {
public:
#ifdef _WIN32
	virtual void consumeAudioBuffer(__int16 *audioBuffer, int bufferSizeInBytes) = 0;
#else
	virtual void consumeAudioBuffer(int16_t *audioBuffer, int bufferSizeInBytes) = 0;
#endif
};


#endif

