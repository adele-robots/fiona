#ifndef __VIDEO_QUEUE
#define __VIDEO_QUEUE

#include <pthread.h>
#include "IImage.h"

typedef struct {
	IImage *image;
} VideoBuffer;


/// Video frame queue.

class VideoQueue {
public:
	void init(int num_buffers);
	void queueVideoFrame(IImage *);	
	IImage *dequeueVideoFrame();
	void quit(void);
	bool isFull(void);
	bool isEmpty(void);
	void cancel(void);

private:
	pthread_cond_t condition_not_empty;
	pthread_cond_t condition_not_full;
	pthread_mutex_t mutex;
	int storedBuffers;

	VideoBuffer *videoBuffers;
	int num_buffers;	// max number
	int indice_insercion;
	int indice_extraccion;
};


#endif
