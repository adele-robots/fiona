/*
 * AudioWrap.h
 *
 *  Created on: 16/10/2012
 *      Author: guille
 */

#ifndef AUDIOWRAP_H_
#define AUDIOWRAP_H_

#include <stdint.h>

class AudioWrap {
public:
	AudioWrap(int16_t *audioBuffer, int bufferSizeInBytes);
	int16_t *audioBuffer;
	int bufferSizeInBytes;
};

#endif /* AUDIOWRAP_H_ */
