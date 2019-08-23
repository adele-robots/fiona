/// @file VideoQueue.cpp
/// @brief VideoQueue class definition.


#include "stdAfx.h"
#include <pthread.h>
#include "Image.h"
#include "Logger.h"
#include "VideoQueue.h"
//ERR needs this header
#include <cstdio>


#pragma comment(lib, "pthreadVC2.lib")


/// Initializes the queue.
		
void VideoQueue::init(int num_buffers) {
	this->num_buffers = num_buffers;
	videoBuffers = new VideoBuffer[num_buffers];
	storedBuffers = 0;
	indice_insercion = 0;
	indice_extraccion = 0;

	pthread_cond_init(&condition_not_full, NULL);
	pthread_cond_init(&condition_not_empty, NULL);
	pthread_mutex_init(&mutex, NULL);
}


/// Queues a frame.

void VideoQueue::queueVideoFrame(Image *image) {

	pthread_mutex_lock(&mutex);

	// Frame drop if the queue is full.
	// As queueVideoFrame is called asynchronouly on frame arrival,
	// this does not imply a busy wait.

	if (isFull()) {
		// LoggerWarn("Frame drop");
		delete image;
		pthread_mutex_unlock(&mutex);
		return;
	}

	/*
	 * wait until not full queue 

	while (isFull()) {
		LoggerWarn("Video input buffer full");
		pthread_cond_wait(&condition_not_full, &mutex);
    }
	*/


	videoBuffers[indice_insercion].image = image;
	indice_insercion++;
	indice_insercion %= num_buffers;
	storedBuffers++;

	pthread_cond_signal(&condition_not_empty);
	pthread_mutex_unlock(&mutex);
	
}


Image *VideoQueue::dequeueVideoFrame(void) {

	Image *image;
	pthread_mutex_lock(&mutex);

	while (isEmpty()) {
		int res;
		res = pthread_cond_wait(&condition_not_empty, &mutex);
		if (res != 0) ERR("pthread_cond_wait");
	}

	image = videoBuffers[indice_extraccion].image;
	indice_extraccion++;
	indice_extraccion %= num_buffers;
	storedBuffers--;

	pthread_mutex_unlock(&mutex);
	pthread_cond_signal(&condition_not_full);

	return image;
}

void VideoQueue::quit(void) {
	delete [] videoBuffers;
}


bool VideoQueue::isFull(void) {
	return storedBuffers == num_buffers;
}

bool VideoQueue::isEmpty(void) {
	return storedBuffers == 0;
}

