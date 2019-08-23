/*
 * TRONHandlerThreadSpark.cpp
 *
 *  Created on: 01/02/2013
 *      Author: guille
 */

/// @file TRONHandlerThreadSpark.cpp
/// @brief TRONHandlerThreadSpark class implementation.


//#include "stdAfx.h"
#include "TRONHandlerThreadSpark.h"


#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "TRONHandlerThreadSpark")) {
			return new TRONHandlerThreadSpark(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif

/// Initializes TRONHandlerThreadSpark component.
void TRONHandlerThreadSpark::init(void){
	//LoggerInfo("[FIONA-logger]TRONHandlerThreadSpark::init\n");
	//2-configurations
	myTRONHandlerComponent->componentConfiguration = componentConfiguration;
	myTRONAVInputComponent->componentConfiguration = componentConfiguration;
	myThreadComponent->componentConfiguration = componentConfiguration;

	//3-connections
	//required interfaces of TronHandlerSpark
	myTRONHandlerComponent->myCharFlow = myCharFlow;
	myTRONHandlerComponent->myAudioFlow = myAudioFlow;
	myTRONAVInputComponent->myAudioFlow = myAudioFlow;
	myTRONAVInputComponent->myImageFlow = myImageFlow;

	//required interfaces of ThreadComponent
	myThreadComponent->myThreadProc = myTRONHandlerComponent;
	myThreadComponent->myAsyncFatalErrorHandler = myAsyncFatalErrorHandler;

	//4-initializations
	myTRONHandlerComponent->init();
	myThreadComponent->init();
}

/// Unitializes the TRONHandlerThreadSpark component.
void TRONHandlerThreadSpark::quit(void){
	myTRONHandlerComponent->quit();
	myTRONAVInputComponent->quit();
	myThreadComponent->quit();
}

//IFlow<char*> implementation
void TRONHandlerThreadSpark::processData(char * prompt) {
	myTRONHandlerComponent->processData(prompt);
}

//IConcurrent implementation
void TRONHandlerThreadSpark::start(void){
	myThreadComponent->start();
}

void TRONHandlerThreadSpark::stop(void){
	myTRONHandlerComponent->stop();
	myTRONAVInputComponent->stop();
	myThreadComponent->stop();
}


