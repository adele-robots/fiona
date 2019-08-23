/// @file IvonaThreadSpark.cpp
/// @brief IvonaThreadSpark class implementation.


#include "stdAfx.h"
#include "IvonaThreadSpark.h"


#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "IvonaThreadSpark")) {
			return new IvonaThreadSpark(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif

/// Initializes IvonaThreadSpark component.
void IvonaThreadSpark::init(void){
	//2-configurations
	myIvonaComponent->componentConfiguration = componentConfiguration;
	myThreadComponent->componentConfiguration = componentConfiguration;

	//3-connections
	//required interfaces of IvonaSpark

	//required interfaces of ThreadComponent
	myThreadComponent->myThreadProc = myIvonaComponent;
	myThreadComponent->myAsyncFatalErrorHandler = myAsyncFatalErrorHandler;

	//4-initializations
	myIvonaComponent->init();
	myThreadComponent->init();
}

/// Unitializes the IvonaThreadSpark component.
void IvonaThreadSpark::quit(void){
	myIvonaComponent->quit();
	myThreadComponent->quit();
}

//IFlow<char*> implementation
void IvonaThreadSpark::processData(char * prompt) {
	myIvonaComponent->processData(prompt);
}

//IAudioQueue implementation
int IvonaThreadSpark::getStoredAudioSize() {
	return myIvonaComponent->getStoredAudioSize();
}

void IvonaThreadSpark::queueAudioBuffer(char *buffer, int size) {
	myIvonaComponent->queueAudioBuffer(buffer, size);
}

void IvonaThreadSpark::dequeueAudioBuffer(char *buffer, int size) {
	myIvonaComponent->dequeueAudioBuffer(buffer, size);
}

void IvonaThreadSpark::reset() {
	myIvonaComponent->reset();
}

//IConcurrent implementation
void IvonaThreadSpark::start(void){
	myThreadComponent->start();
}

void IvonaThreadSpark::stop(void){
	myThreadComponent->stop();
}




