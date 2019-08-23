/// @file ChatThreadSpark.cpp
/// @brief ChatThreadSpark class implementation.


#include "stdAfx.h"
#include "ChatThreadSpark.h"


#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "ChatThreadSpark")) {
			return new ChatThreadSpark(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif

/// Initializes ChatThreadSpark component.
void ChatThreadSpark::init(void){
	//2-configurations
	myChatComponent->componentConfiguration = componentConfiguration;
	myThreadComponent->componentConfiguration = componentConfiguration;

	//3-connections
	//required interfaces of ChatSpark
	myChatComponent->myFlow = myFlow;

	//required interfaces of ThreadComponent
	myThreadComponent->myThreadProc = myChatComponent;
	myThreadComponent->myAsyncFatalErrorHandler = myAsyncFatalErrorHandler;

	//4-initializations
	myChatComponent->init();
	myThreadComponent->init();
}

/// Unitializes the ChatThreadSpark component.
void ChatThreadSpark::quit(void){
	myChatComponent->quit();
	myThreadComponent->quit();
}

//IFlow implementation
void ChatThreadSpark::processData(char *prompt){
	myChatComponent->processData(prompt);
}

//IConcurrent implementation
void ChatThreadSpark::start(void){
	myThreadComponent->start();
}

void ChatThreadSpark::stop(void){
	myThreadComponent->stop();
}




