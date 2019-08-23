/// @file IPCThreadSpark.cpp
/// @brief IPCThreadSpark class implementation.

#include "IPCThreadSpark.h"

extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "IPCThreadSpark")) {
			return new IPCThreadSpark(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}

/// Initializes IPCThreadSpark component.
void IPCThreadSpark::init(void){
	//2-configurations
	myIPCComponent->componentConfiguration = componentConfiguration;
	myThreadComponent->componentConfiguration = componentConfiguration;

	//3-connections

	//required interfaces of ThreadComponent
	myThreadComponent->myThreadProc = myIPCComponent;
	myThreadComponent->myAsyncFatalErrorHandler = myAsyncFatalErrorHandler;

	//4-initializations
	myIPCComponent->init();
	myThreadComponent->init();
}

/// Unitializes the IPCThreadSpark component.
void IPCThreadSpark::quit(void){
	myIPCComponent->quit();
	myThreadComponent->quit();
}

//IFlow implementation
void IPCThreadSpark::processData(char *prompt){
	myIPCComponent->processData(prompt);
}

//IConcurrent implementation
void IPCThreadSpark::start(void){
	myThreadComponent->start();
}

void IPCThreadSpark::stop(void){
	myThreadComponent->stop();
}
