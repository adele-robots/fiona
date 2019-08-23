//#include "stdAfx.h"
#include "AudioWrap.h"

AudioWrap::AudioWrap(int16_t *audioBuffer, int bufferSizeInBytes)
{
	this->audioBuffer = audioBuffer;
	this->bufferSizeInBytes = bufferSizeInBytes;
}
