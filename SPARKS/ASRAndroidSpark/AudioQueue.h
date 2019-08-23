/// @file AudioQueue.h
/// @brief AudioQueue class declaration.


#ifndef __BLOCKING_QUEUE
#define __BLOCKING_QUEUE

#include <pthread.h>
#include <stdint.h>


class AudioQueue {
	pthread_cond_t condition_empty;
	pthread_cond_t condition_full;
	pthread_mutex_t mutex;

	uint8_t *audioBuffer;
	int indice_insercion;
	int indice_extraccion;

public:
	int audioBufferSize;
	int storedAudioSize;

public:
	void init(int bufferSize);
	void queueAudioBuffer(uint8_t *buffer, int size);
	void dequeueAudioBuffer(uint8_t *buffer, int size);
	void quit(void);
	bool hasRoom(int size);
	bool isEmpty(void);
	void reset(void); // vaciar el buffer
	int  getStoredAudioSize();

};


#endif
