/// @file EmptyEmbodimentSpark.cpp
/// @brief EmptyEmbodimentSpark class implementation.

// Third party libraries are linked explicitly once in the project.
// #pragma comment(lib, "thirdPartyLib.lib")

#include "stdAfx.h"
#include "EmptyEmbodimentSpark.h"

#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "EmptyEmbodimentSpark")) {
			return new EmptyEmbodimentSpark(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif

/// Initializes EmptyEmbodimentSpark component.
void EmptyEmbodimentSpark::init(void){
	//2-configurations
	myWindowComponent->componentConfiguration = componentConfiguration;
	myThreadComponent->componentConfiguration = componentConfiguration;
	myAudioRendererSpark->componentConfiguration = componentConfiguration;
	myFFMpegAVInOutComponent->componentConfiguration = componentConfiguration;

	//3-connections
	//required interfaces of RemoteCharacterEmbodiment3DComponent
	myAudioRendererSpark->myAudioQueue = myAudioQueue;
	myAudioRendererSpark->myControlVoice = myControlVoice;
	myThreadComponent->myAsyncFatalErrorHandler = myAsyncFatalErrorHandler;

	//required interfaces of AudioRendererSpark
	myAudioRendererSpark->myAudioFlow = myFFMpegAVInOutComponent;

	//required interfaces of ThreadComponent
	myThreadComponent->myThreadProc = myAudioRendererSpark;

	//required interfaces of FFMpegAVInOutComponent
	myFFMpegAVInOutComponent->myAudioFlow = myAudioFlow;

	//4-initializations
	myWindowComponent->init();
	myThreadComponent->init();
	myAudioRendererSpark->init();
	myFFMpegAVInOutComponent->init();
}


/// Unitializes the RemoteCharacterEmbodiment3DComponent component.

void EmptyEmbodimentSpark::quit(void){
	myFFMpegAVInOutComponent->quit();
	myAudioRendererSpark->quit();
	myThreadComponent->quit();
	myWindowComponent->quit();
}


//IConcurrent implementation
void EmptyEmbodimentSpark::start(void){
	myThreadComponent->start();
}

void EmptyEmbodimentSpark::stop(void){
	myThreadComponent->stop();
}

//IApplication implementation
void EmptyEmbodimentSpark::run(void){
	myWindowComponent->run();
}

//IAsyncFatalErrorHandler implementation
void EmptyEmbodimentSpark::handleError(char* messageError){
	myWindowComponent->handleError(messageError);
}
