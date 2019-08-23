/*
 * ASRGoogleThreadSpark.cpp
 *
 *  Created on: 06/06/2014
 *      Author: jandro
 */


/// @file ASRGoogleThreadSpark.cpp
/// @brief ASRGoogleThreadSpark class implementation.


#include "stdAfx.h"
#include "ASRGoogleThreadSpark.h"


#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "ASRGoogleThreadSpark")) {
			return new ASRGoogleThreadSpark(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif

/// Initializes ASRGoogleThreadSpark component.
void ASRGoogleThreadSpark::init(void){
	//2-configurations
	myASRGoogleComponent->componentConfiguration = componentConfiguration;
	myThreadComponent->componentConfiguration = componentConfiguration;

	//3-connections
	//required interfaces of ASRGoogleSpark
	myASRGoogleComponent->myCharFlow = myFlow;

	//required interfaces of ThreadComponent
	myThreadComponent->myThreadProc = myASRGoogleComponent;
	myThreadComponent->myAsyncFatalErrorHandler = myAsyncFatalErrorHandler;

	//4-initializations
	myASRGoogleComponent->init();
	myThreadComponent->init();
}

/// Unitializes the ASRGoogleThreadSpark component.
void ASRGoogleThreadSpark::quit(void){
	myASRGoogleComponent->quit();
	myThreadComponent->quit();
}

//IFlow<AudioWrap*> implementation
void ASRGoogleThreadSpark::processData(AudioWrap *audio){
	myASRGoogleComponent->processData(audio);
}

//IConcurrent implementation
void ASRGoogleThreadSpark::start(void){
	myThreadComponent->start();
}

void ASRGoogleThreadSpark::stop(void){
	myThreadComponent->stop();
}



