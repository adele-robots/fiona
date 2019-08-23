/// @file AudioInputSpark.cpp
/// @brief AudioInputSpark class implementation.

// Third party libraries are linked explicitly once in the project.
// #pragma comment(lib, "thirdPartyLib.lib")

#include "stdAfx.h"
#include "AudioInputSpark.h"


#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "AudioInputSpark")) {
			return new AudioInputSpark(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif
/// Initializes AudioInputSpark component.

void AudioInputSpark::init(void) {
	//2-configurations
	myThreadComponent->componentConfiguration = componentConfiguration;
	myFFMpegAudioInputComponent->componentConfiguration = componentConfiguration;

	//3-connections
	//required interfaces of AVInputComponent
	myThreadComponent->myAsyncFatalErrorHandler = myAsyncFatalErrorHandler;
	myFFMpegAudioInputComponent->myAudioFlow = myAudioFlow;

	//required interfaces of ThreadComponent
	myThreadComponent->myThreadProc = myFFMpegAudioInputComponent;

	//4-initializations
	myFFMpegAudioInputComponent->init();
	myThreadComponent->init();
}

//IConcurrent implementation
void AudioInputSpark::start(void){
	myThreadComponent->start();
}

void AudioInputSpark::stop(void){
	myThreadComponent->stop();
}

/// Unitializes the AudioInputSpark component.
void AudioInputSpark::quit(void) {
	myFFMpegAudioInputComponent->quit();
	myThreadComponent->quit();
}
