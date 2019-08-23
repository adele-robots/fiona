/// @file AVInputSpark.cpp
/// @brief AVInputSpark class implementation.

// Third party libraries are linked explicitly once in the project.
// #pragma comment(lib, "thirdPartyLib.lib")

#include "stdAfx.h"
#include "AVInputSpark.h"


#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "AVInputSpark")) {
			return new AVInputSpark(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif
/// Initializes AVInputSpark component.

void AVInputSpark::init(void) {
	//2-configurations
	myThreadComponent->componentConfiguration = componentConfiguration;
	myFFMpegReaderComponent->componentConfiguration = componentConfiguration;

	//3-connections
	//required interfaces of AVInputComponent
	myThreadComponent->myAsyncFatalErrorHandler = myAsyncFatalErrorHandler;
	myFFMpegReaderComponent->myAudioFlow = myAudioFlow;
	myFFMpegReaderComponent->myFlow = myFlow;

	//required interfaces of ThreadComponent
	myThreadComponent->myThreadProc = myFFMpegReaderComponent;

	//4-initializations
	myFFMpegReaderComponent->init();
	myThreadComponent->init();
}

//IConcurrent implementation
void AVInputSpark::start(void){
	myThreadComponent->start();
}

void AVInputSpark::stop(void){
	myThreadComponent->stop();
}

/// Unitializes the AVInputSpark component.
void AVInputSpark::quit(void) {
	myFFMpegReaderComponent->quit();
	myThreadComponent->quit();
}
