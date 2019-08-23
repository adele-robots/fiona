/// @file AudioQueue.cpp
/// @brief AudioQueue class implementation.


#include "stdAfx.h"
#include "pthread.h"
#include "AudioQueue.h"
#include "string.h"

#ifdef _WIN32
#pragma comment(lib, "pthreadVC2.lib")
#else
#endif


		
void AudioQueue::init(int bufferSize) {
	this->audioBufferSize = bufferSize;
	audioBuffer = new char[bufferSize];

	storedAudioSize = 0;
	indice_insercion = 0;
	indice_extraccion = 0;

	pthread_cond_init(&condition_full, NULL);
	pthread_cond_init(&condition_empty, NULL);
	pthread_mutex_init(&mutex, NULL);
}



void AudioQueue::queueAudioBuffer(char *buffer, int inputBufferSize) {

	pthread_mutex_lock(&mutex);

	while (!hasRoom(inputBufferSize)) {
		//puts("LA COLA ESTA PETADA !!");
		pthread_cond_wait(&condition_empty, &mutex);
    }

	int rightSpace = audioBufferSize - indice_insercion;

	if (inputBufferSize <= rightSpace) {
		memcpy(audioBuffer + indice_insercion, buffer, inputBufferSize);
	}
	else {
		memcpy(
			audioBuffer + indice_insercion, 
			buffer, 
			rightSpace
		);
		memcpy(
			audioBuffer, 
			buffer + rightSpace, 
			inputBufferSize - rightSpace
		);
	}
	indice_insercion += inputBufferSize;
	indice_insercion %= audioBufferSize;

	storedAudioSize += inputBufferSize;

	pthread_cond_signal(&condition_full);
	pthread_mutex_unlock(&mutex);
}


void AudioQueue::dequeueAudioBuffer(char *buffer, int outputBufferSize) {
	pthread_mutex_lock(&mutex);


	//printf("Tamanio del buffer output: %d\n",outputBufferSize);
	//printf("Tamanio del buffer stored: %d\n",storedAudioSize);

	while (outputBufferSize > storedAudioSize) {
		pthread_cond_wait(&condition_full, &mutex);
	}



	int rightSpace = audioBufferSize - indice_extraccion;

	//printf("Right space: %d\n", rightSpace);

	if (outputBufferSize < rightSpace) {
		memcpy(
			buffer,
			audioBuffer + indice_extraccion,
			outputBufferSize
		);

	}
	else {
		memcpy(
			buffer,
			audioBuffer + indice_extraccion,
			rightSpace
		);
		memcpy(
			buffer + rightSpace,
			audioBuffer,
			outputBufferSize - rightSpace
		);
	}

	indice_extraccion += outputBufferSize;
	indice_extraccion %= audioBufferSize;

	storedAudioSize -= outputBufferSize;

	pthread_mutex_unlock(&mutex);

	pthread_cond_signal(&condition_empty);
}

void AudioQueue::quit(void) {
	delete[] audioBuffer;
}


bool AudioQueue::hasRoom(int size) {
	return storedAudioSize + size <= audioBufferSize;
}

bool AudioQueue::isEmpty(void) {
	return storedAudioSize == 0;
}

void AudioQueue::reset(void) {
	storedAudioSize = 0;
	indice_insercion = 0;
	indice_extraccion = 0;
	pthread_cond_signal(&condition_empty);
}


int AudioQueue::getStoredAudioSize() {
	return storedAudioSize;
}

