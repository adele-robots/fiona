/*
 * VerbioTTSThreadSpark.cpp
 *
 *  Created on: 10/07/2013
 *      Author: guille
 */


/// @file VerbioTTSThreadSpark.cpp
/// @brief VerbioTTSThreadSpark class implementation.


#include "stdAfx.h"
#include "VerbioTTSThreadSpark.h"


#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "VerbioTTSThreadSpark")) {
			return new VerbioTTSThreadSpark(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif

/// Initializes VerbioTTSThreadSpark component.
void VerbioTTSThreadSpark::init(void){
	//2-configurations
	myVerbioTTSComponent->componentConfiguration = componentConfiguration;
	myThreadComponent->componentConfiguration = componentConfiguration;

	//3-connections
	//required interfaces of ThreadComponent
	myThreadComponent->myThreadProc = myVerbioTTSComponent;
	myThreadComponent->myAsyncFatalErrorHandler = myAsyncFatalErrorHandler;

	//4-initializations
	myVerbioTTSComponent->init();
	myThreadComponent->init();
}

/// Unitializes the VerbioTTSThreadSpark component.
void VerbioTTSThreadSpark::quit(void){
	myVerbioTTSComponent->quit();
	myThreadComponent->quit();
}

//IFlow<char*> implementation
void VerbioTTSThreadSpark::processData(char *prompt){
	myVerbioTTSComponent->processData(prompt);
}

//IAudioQueue implementation
int VerbioTTSThreadSpark::getStoredAudioSize() {
	return myVerbioTTSComponent->getStoredAudioSize();
}

void VerbioTTSThreadSpark::queueAudioBuffer(char *buffer, int size) {
	myVerbioTTSComponent->queueAudioBuffer(buffer, size);
}

void VerbioTTSThreadSpark::dequeueAudioBuffer(char *buffer, int size) {
	myVerbioTTSComponent->dequeueAudioBuffer(buffer, size);
}

void VerbioTTSThreadSpark::reset(){
	myVerbioTTSComponent->reset();
}

//IConcurrent implementation
void VerbioTTSThreadSpark::start(void){
	myThreadComponent->start();
}

void VerbioTTSThreadSpark::stop(void){
	myThreadComponent->stop();
}


