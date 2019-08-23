/*
 * TimerThreadSpark.cpp
 *
 *  Created on: 05/11/2012
 *      Author: guille
 */

/// @file TimerThreadSpark.cpp
/// @brief TimerThreadSpark class implementation.


#include "stdAfx.h"
#include "TimerThreadSpark.h"


#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "TimerThreadSpark")) {
			return new TimerThreadSpark(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif

/// Initializes TimerThreadSpark component.
void TimerThreadSpark::init(void){
	//2-configurations
	myTimerComponent->componentConfiguration = componentConfiguration;
	myThreadComponent->componentConfiguration = componentConfiguration;

	//3-connections
	myTimerComponent->myFaceExpression = myFaceExpression;
	myTimerComponent->myFlow = myFlow;

	//required interfaces of ThreadComponent
	myThreadComponent->myThreadProc = myTimerComponent;
	myThreadComponent->myAsyncFatalErrorHandler = myAsyncFatalErrorHandler;

	//4-initializations
	myTimerComponent->init();
	myThreadComponent->init();
}

/// Unitializes the TimerThreadSpark component.
void TimerThreadSpark::quit(void){
	myTimerComponent->quit();
	myThreadComponent->quit();
}

//IConcurrent implementation
void TimerThreadSpark::start(void){
	myThreadComponent->start();
}

void TimerThreadSpark::stop(void){
	myThreadComponent->stop();
}

