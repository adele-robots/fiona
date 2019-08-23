/*
 * VisemeQueue.h
 *
 *  Created on: 19/10/2012
 *      Author: Alejandro
 */

#ifndef VISEMEQUEUE_H_
#define VISEMEQUEUE_H_

#include <pthread.h>
#include <stdio.h>
#include <queue>
#include <string>

typedef std::pair<std::string, float> Viseme;

class VisemeQueue {

	pthread_mutex_t mutex;

	std::queue<Viseme> visemeBuffer;

public:
	void init();
	void queueViseme(Viseme);
	void dequeueViseme();
	Viseme front();
	void quit(void);
	bool isEmpty(void);
	void reset(void); // vaciar el buffer
	int getStoredVisemes(); // in bytes
};

#endif /* VISEMEQUEUE_H_ */
