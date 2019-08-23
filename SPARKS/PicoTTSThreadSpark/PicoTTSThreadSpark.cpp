/*
 * PicoTTSThreadSpark.cpp
 *
 *  Created on: 14/01/2013
 *      Author: guille
 */


/// @file PicoTTSThreadSpark.cpp
/// @brief PicoTTSThreadSpark class implementation.


#include "stdAfx.h"
#include "PicoTTSThreadSpark.h"


#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "PicoTTSThreadSpark")) {
			return new PicoTTSThreadSpark(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif

/// Initializes PicoTTSThreadSpark component.
void PicoTTSThreadSpark::init(void){
	//2-configurations
	myPicoTTSComponent->componentConfiguration = componentConfiguration;
	myThreadComponent->componentConfiguration = componentConfiguration;

	//3-connections
	//required interfaces of IvonaSpark

	//required interfaces of ThreadComponent
	myThreadComponent->myThreadProc = myPicoTTSComponent;
	myThreadComponent->myAsyncFatalErrorHandler = myAsyncFatalErrorHandler;

	//4-initializations
	myPicoTTSComponent->init();
	myThreadComponent->init();
}

/// Unitializes the PicoTTSThreadSpark component.
void PicoTTSThreadSpark::quit(void){
	myPicoTTSComponent->quit();
	myThreadComponent->quit();
}

//IFlow<char*> implementation
void PicoTTSThreadSpark::processData(char *prompt){
	myPicoTTSComponent->processData(prompt);
}

//IAudioQueue implementation
int PicoTTSThreadSpark::getStoredAudioSize() {
	return myPicoTTSComponent->getStoredAudioSize();
}

void PicoTTSThreadSpark::queueAudioBuffer(char *buffer, int size) {
	myPicoTTSComponent->queueAudioBuffer(buffer, size);
}

void PicoTTSThreadSpark::dequeueAudioBuffer(char *buffer, int size) {
	myPicoTTSComponent->dequeueAudioBuffer(buffer, size);
}

void PicoTTSThreadSpark::reset(){
	myPicoTTSComponent->reset();
}

//IConcurrent implementation
void PicoTTSThreadSpark::start(void){
	myThreadComponent->start();
}

void PicoTTSThreadSpark::stop(void){
myThreadComponent->stop();
}



