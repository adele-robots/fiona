/*
 * RemotePhotoCharacterSpark.cpp
 *
 *  Created on: 13/11/2013
 *      Author: guille
 */

/// @file RemotePhotoCharacterSpark.cpp
/// @brief RemotePhotoCharacterSpark class implementation.


#include "stdAfx.h"
#include "RemotePhotoCharacterSpark.h"


#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "RemotePhotoCharacterSpark")) {
			return new RemotePhotoCharacterSpark(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif

/// Initializes RemotePhotoCharacterSpark component.
void RemotePhotoCharacterSpark::init(void){
	//2-configurations
	myPhotoEngineComponent->componentConfiguration = componentConfiguration;
	myRemotePhotoRendererComponent->componentConfiguration = componentConfiguration;
	myFFMpegAVOutputComponent->componentConfiguration = componentConfiguration;

	//3-connections
	//required interfaces of RemoteCharacterEmbodiment3DComponent
	myRemotePhotoRendererComponent->myAudioQueue = myAudioQueue;

	//required interfaces of RemoteRendererComponent
	myRemotePhotoRendererComponent->myRenderizable = myPhotoEngineComponent;
	myRemotePhotoRendererComponent->myAudioFlow = myFFMpegAVOutputComponent;
	myRemotePhotoRendererComponent->myFlow = myFFMpegAVOutputComponent;

	//4-initializations
	LoggerInfo("RemotePhotoCharacterSpark::init | myPhotoEngineComponent->init()");
	myPhotoEngineComponent->init();
	LoggerInfo("RemotePhotoCharacterSpark::init | myRemotePhotoRendererComponent->init()");
	myRemotePhotoRendererComponent->init();
	LoggerInfo("RemotePhotoCharacterSpark::init | myFFMpegAVOutputComponent->init()");
	myFFMpegAVOutputComponent->init();
	LoggerInfo("RemotePhotoCharacterSpark::init | DESPUÉS myFFMpegAVOutputComponent->init()!");
}

/// Unitializes the RemotePhotoCharacterSpark component.
void RemotePhotoCharacterSpark::quit(void){
	myFFMpegAVOutputComponent->quit();
	myRemotePhotoRendererComponent->quit();
	myPhotoEngineComponent->quit();
}

//IApplication implementation
void RemotePhotoCharacterSpark::run(void){
	myRemotePhotoRendererComponent->run();
}

//IFlow implementation
void RemotePhotoCharacterSpark::processData(char *prompt){
	myPhotoEngineComponent->processData(prompt);
}

//IFrameEventPublisher implementation
void RemotePhotoCharacterSpark::addFrameEventSubscriber(FrameEventSubscriber *frameEventSubscriber)
{
	myRemotePhotoRendererComponent->addFrameEventSubscriber(frameEventSubscriber);
}

