/*
 * FestivalTTSThreadSpark.cpp
 *
 *  Created on: 26/12/2013
 *      Author: jandro
 */


/// @file FestivalTTSThreadSpark.cpp
/// @brief FestivalTTSThreadSpark class implementation.


#include "stdAfx.h"
#include "FestivalTTSThreadSpark.h"


#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "FestivalTTSThreadSpark")) {
			return new FestivalTTSThreadSpark(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif

/// Initializes FestivalTTSThreadSpark component.
void FestivalTTSThreadSpark::init(void){
	//2-configurations
	myFestivalTTSComponent->componentConfiguration = componentConfiguration;
	myThreadComponent->componentConfiguration = componentConfiguration;

	//3-connections
	//required interfaces of FestivalTTSSpark

	//required interfaces of ThreadComponent
	myThreadComponent->myThreadProc = myFestivalTTSComponent;
	myThreadComponent->myAsyncFatalErrorHandler = myAsyncFatalErrorHandler;

	//4-initializations
	myFestivalTTSComponent->init();
	myThreadComponent->init();
}

/// Unitializes the FestivalTTSThreadSpark component.
void FestivalTTSThreadSpark::quit(void){
	myFestivalTTSComponent->quit();
	myThreadComponent->quit();
}

//IFlow<char*> implementation
void FestivalTTSThreadSpark::processData(char *prompt){
	myFestivalTTSComponent->processData(prompt);
}

//IAudioQueue implementation
int FestivalTTSThreadSpark::getStoredAudioSize() {
	return myFestivalTTSComponent->getStoredAudioSize();
}

void FestivalTTSThreadSpark::queueAudioBuffer(char *buffer, int size) {
	myFestivalTTSComponent->queueAudioBuffer(buffer, size);
}

void FestivalTTSThreadSpark::dequeueAudioBuffer(char *buffer, int size) {
	myFestivalTTSComponent->dequeueAudioBuffer(buffer, size);
}

void FestivalTTSThreadSpark::reset(){
	myFestivalTTSComponent->reset();
}

//IConcurrent implementation
void FestivalTTSThreadSpark::start(void){
	myThreadComponent->start();
}

void FestivalTTSThreadSpark::stop(void){
	myThreadComponent->stop();
}



