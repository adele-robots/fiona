/*
 * MouseInfoThreadSpark.cpp
 *
 *  Created on: 12/11/2013
 *      Author: guille
 */

/// @file MouseInfoThreadSpark.cpp
/// @brief MouseInfoThreadSpark class implementation.


//#include "stdAfx.h"
#include "MouseInfoThreadSpark.h"


#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "MouseInfoThreadSpark")) {
			return new MouseInfoThreadSpark(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif

/// Initializes MouseInfoThreadSpark component.
void MouseInfoThreadSpark::init(void){
	//2-configurations
	myMouseInfoComponent->componentConfiguration = componentConfiguration;
	myThreadComponent->componentConfiguration = componentConfiguration;

	//3-connections
	//required interfaces of ChatSpark
	myMouseInfoComponent->myFlow = myFlow;

	//required interfaces of ThreadComponent
	myThreadComponent->myThreadProc = myMouseInfoComponent;
	myThreadComponent->myAsyncFatalErrorHandler = myAsyncFatalErrorHandler;

	//4-initializations
	myMouseInfoComponent->init();
	myThreadComponent->init();

	myFrameEventPublisher->addFrameEventSubscriber(this);
}

/// Unitializes the MouseInfoThreadSpark component.
void MouseInfoThreadSpark::quit(void){
	myMouseInfoComponent->quit();
	myThreadComponent->quit();
}

//IFlow implementation
void MouseInfoThreadSpark::processData(char *prompt){
	myMouseInfoComponent->processData(prompt);
}

//IConcurrent implementation
void MouseInfoThreadSpark::start(void){
	myThreadComponent->start();
}

void MouseInfoThreadSpark::stop(void){
	myThreadComponent->stop();
}

//FrameEventSubscriber implementation
void MouseInfoThreadSpark::notifyFrameEvent(void){
	myMouseInfoComponent->notifyFrameEvent();
}

