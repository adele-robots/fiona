/*
 * HttpRequestThreadSpark.cpp
 *
 *  Created on: 17/06/2013
 *      Author: Alejandro
 */

/// @file HttpRequestThreadSpark.cpp
/// @brief HttpRequestThreadSpark class implementation.


//#include "stdAfx.h"
#include "HttpRequestThreadSpark.h"


#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "HttpRequestThreadSpark")) {
			return new HttpRequestThreadSpark(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif

/// Initializes HttpRequestThreadSpark component.
void HttpRequestThreadSpark::init(void){
	//2-configurations
	myHttpRequestComponent->componentConfiguration = componentConfiguration;
	myThreadComponent->componentConfiguration = componentConfiguration;

	//3-connections
	//required interfaces of ChatSpark
	myHttpRequestComponent->myFlow = myFlow;

	//required interfaces of ThreadComponent
	myThreadComponent->myThreadProc = myHttpRequestComponent;
	myThreadComponent->myAsyncFatalErrorHandler = myAsyncFatalErrorHandler;

	//4-initializations
	myHttpRequestComponent->init();
	myThreadComponent->init();
}

/// Unitializes the HttpRequestThreadSpark component.
void HttpRequestThreadSpark::quit(void){
	myHttpRequestComponent->quit();
	myThreadComponent->quit();
}

//IFlow<char*> implementation
void HttpRequestThreadSpark::processData(char * prompt) {
	myHttpRequestComponent->processData(prompt);
}

//IConcurrent implementation
void HttpRequestThreadSpark::start(void){
	myThreadComponent->start();
}

void HttpRequestThreadSpark::stop(void){
	myThreadComponent->stop();
}


