/// @file TRONAVInputSpark.cpp
/// @brief TRONAVInputSpark class implementation.

// Third party libraries are linked explicitly once in the project.
// #pragma comment(lib, "thirdPartyLib.lib")

#include "stdAfx.h"
#include "TRONAVInputSpark.h"


#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "TRONAVInputSpark")) {
			return new TRONAVInputSpark(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif
/// Initializes TRONAVInputSpark component.

void TRONAVInputSpark::init(void) {
	//2-configurations
	myThreadComponent->componentConfiguration = componentConfiguration;
	myTRONReaderComponent->componentConfiguration = componentConfiguration;

	//3-connections
	//required interfaces of AVInputComponent
	myThreadComponent->myAsyncFatalErrorHandler = myAsyncFatalErrorHandler;
	myTRONReaderComponent->myAudioFlow = myAudioFlow;
	myTRONReaderComponent->myImageFlow = myImageFlow;

	//required interfaces of ThreadComponent
	myThreadComponent->myThreadProc = myTRONReaderComponent;

	//4-initializations
	myTRONReaderComponent->init();
	myThreadComponent->init();
}

//IConcurrent implementation
void TRONAVInputSpark::start(void){
	myThreadComponent->start();
}

void TRONAVInputSpark::stop(void){
	myThreadComponent->stop();
}

/// Unitializes the TRONAVInputSpark component.
void TRONAVInputSpark::quit(void) {
	myTRONReaderComponent->quit();
	myThreadComponent->quit();
}
