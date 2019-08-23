#ifndef __I_AUDIO_QUEUE_H
#define __I_AUDIO_QUEUE_H


class IAudioQueue {
public:
	// All sizes in bytes, not samples of 16 bit words.
	virtual int getStoredAudioSize() = 0;	
	virtual void queueAudioBuffer(char *buffer, int size) = 0;
	virtual void dequeueAudioBuffer(char *buffer, int size) = 0;
	virtual void reset() = 0; // vaciar el buffer
};


#endif
