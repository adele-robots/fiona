/*
 * VisemeQueue.cpp
 *
 *  Created on: 19/10/2012
 *      Author: Alejandro
 */

#include "stdAfx.h"
#include "pthread.h"
#include "VisemeQueue.h"

#ifdef _WIN32
#pragma comment(lib, "pthreadVC2.lib")
#else
#endif



void VisemeQueue::init() {

	pthread_mutex_init(&mutex, NULL);
}



void VisemeQueue::queueViseme(Viseme v) {

	pthread_mutex_lock(&mutex);

	visemeBuffer.push(v);

	pthread_mutex_unlock(&mutex);
}


void VisemeQueue::dequeueViseme() {
	pthread_mutex_lock(&mutex);

	if(!visemeBuffer.empty()) {
		visemeBuffer.pop();
	}

	pthread_mutex_unlock(&mutex);
}

Viseme VisemeQueue::front() {
	if(!visemeBuffer.empty()) {
		return visemeBuffer.front();
	}
	return Viseme("NULL",0);
}
void VisemeQueue::quit(void) {
	reset();
}


bool VisemeQueue::isEmpty(void) {
	return visemeBuffer.empty();
}

void VisemeQueue::reset(void) {
	pthread_mutex_lock(&mutex);

	while(!visemeBuffer.empty()) {
		visemeBuffer.pop();
	}

	pthread_mutex_unlock(&mutex);
}


int VisemeQueue::getStoredVisemes() {
	return visemeBuffer.size();
}
