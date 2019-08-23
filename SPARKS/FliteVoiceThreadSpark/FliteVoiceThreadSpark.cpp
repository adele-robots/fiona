/*
 * FliteVoiceThreadSpark.cpp
 *
 *  Created on: 17/10/2012
 *      Author: guille
 */


/// @file FliteVoiceThreadSpark.cpp
/// @brief FliteVoiceThreadSpark class implementation.

#include "stdAfx.h"
#include "FliteVoiceThreadSpark.h"


#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "FliteVoiceThreadSpark")) {
			return new FliteVoiceThreadSpark(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif

/// Initializes FliteVoiceThreadSpark component.
void FliteVoiceThreadSpark::init(void){
	//2-configurations
	myFliteComponent->componentConfiguration = componentConfiguration;
	myThreadComponent->componentConfiguration = componentConfiguration;

	//3-connections
	//required interfaces of IvonaSpark

	//required interfaces of ThreadComponent
	myThreadComponent->myThreadProc = myFliteComponent;
	myThreadComponent->myAsyncFatalErrorHandler = myAsyncFatalErrorHandler;

	//4-initializations
	myFliteComponent->init();
	myThreadComponent->init();
}

/// Unitializes the FliteVoiceThreadSpark component.
void FliteVoiceThreadSpark::quit(void){
	myFliteComponent->quit();
	myThreadComponent->quit();
}

//IFlow<char*> implementation
void FliteVoiceThreadSpark::processData(char *prompt){
	myFliteComponent->processData(prompt);
}

//IAudioQueue implementation
int FliteVoiceThreadSpark::getStoredAudioSize() {
	return myFliteComponent->getStoredAudioSize();
}

void FliteVoiceThreadSpark::queueAudioBuffer(char *buffer, int size) {
	myFliteComponent->queueAudioBuffer(buffer, size);
}

void FliteVoiceThreadSpark::dequeueAudioBuffer(char *buffer, int size) {
	myFliteComponent->dequeueAudioBuffer(buffer, size);
}

void FliteVoiceThreadSpark::reset() {
	myFliteComponent->reset();
}

//IConcurrent implementation
void FliteVoiceThreadSpark::start(void){
	myThreadComponent->start();
}

void FliteVoiceThreadSpark::stop(void){
	myThreadComponent->stop();
}


