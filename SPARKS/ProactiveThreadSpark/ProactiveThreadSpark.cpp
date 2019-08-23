/*
 * ProactiveThreadSpark.cpp
 *
 *  Created on: 03/10/2013
 *      Author: guille
 */

/// @file ProactiveThreadSpark.cpp
/// @brief ProactiveThreadSpark class implementation.


//#include "stdAfx.h"
#include "ProactiveThreadSpark.h"


#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "ProactiveThreadSpark")) {
			return new ProactiveThreadSpark(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif

/// Initializes ProactiveThreadSpark component.
void ProactiveThreadSpark::init(void){
	//2-configurations
	myProactiveComponent->componentConfiguration = componentConfiguration;
	myThreadComponent->componentConfiguration = componentConfiguration;

	//3-connections
	//required interfaces of ChatSpark
	myProactiveComponent->myFlow = myFlow;

	//required interfaces of ThreadComponent
	myThreadComponent->myThreadProc = myProactiveComponent;
	myThreadComponent->myAsyncFatalErrorHandler = myAsyncFatalErrorHandler;

	//4-initializations
	myProactiveComponent->init();
	myThreadComponent->init();

}

/// Unitializes the ProactiveThreadSpark component.
void ProactiveThreadSpark::quit(void){
	myProactiveComponent->quit();
	myThreadComponent->quit();
}

//IFlow implementation
void ProactiveThreadSpark::processData(char *prompt){
	myProactiveComponent->processData(prompt);
}

//IConcurrent implementation
void ProactiveThreadSpark::start(void){
	myThreadComponent->start();
}

void ProactiveThreadSpark::stop(void){
	myThreadComponent->stop();
}



