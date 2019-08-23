/*
 * ScriptingV8ThreadSpark.cpp
 *
 *  Created on: 03/01/2013
 *      Author: guille
 */

/// @file ScriptingV8ThreadSpark.cpp
/// @brief ScriptingV8ThreadSpark class implementation.


//#include "stdAfx.h"
#include "ScriptingV8ThreadSpark.h"


#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "ScriptingV8ThreadSpark")) {
			return new ScriptingV8ThreadSpark(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif

/// Initializes ScriptingV8ThreadSpark component.
void ScriptingV8ThreadSpark::init(void){
	LoggerInfo("ScriptingV8ThreadSpark::init\n");
	//2-configurations
	myScriptingV8Component->componentConfiguration = componentConfiguration;
	myThreadComponent->componentConfiguration = componentConfiguration;

	//3-connections
	myScriptingV8Component->myFaceExpression = myFaceExpression;
	myScriptingV8Component->myEyes = myEyes;
	myScriptingV8Component->myNeck = myNeck;
	myScriptingV8Component->myCamera = myCamera;
	myScriptingV8Component->myFlow = myFlow;
	myScriptingV8Component->myControlVoice = myControlVoice;
	myScriptingV8Component->myAnimation = myAnimation;

	//required interfaces of ThreadComponent
	myThreadComponent->myThreadProc = myScriptingV8Component;
	myThreadComponent->myAsyncFatalErrorHandler = myAsyncFatalErrorHandler;

	//4-initializations
	myScriptingV8Component->init();
	myThreadComponent->init();

	myFrameEventPublisher->addFrameEventSubscriber(this);
}

/// Unitializes the ScriptingV8ThreadSpark component.
void ScriptingV8ThreadSpark::quit(void){
	myScriptingV8Component->quit();
	myThreadComponent->quit();
}

//IConcurrent implementation
void ScriptingV8ThreadSpark::start(void){
	myThreadComponent->start();
}

void ScriptingV8ThreadSpark::stop(void){
	myThreadComponent->stop();
}

//FrameEventSubscriber implementation
void ScriptingV8ThreadSpark::notifyFrameEvent(void){
	myScriptingV8Component->notifyFrameEvent();
}

//IFaceExpression implementation
void ScriptingV8ThreadSpark::setFaceExpression(char *expressionName,float intensity){
	myScriptingV8Component->setFaceExpression(expressionName,intensity);
}

//IFlow<char*> implementation
void ScriptingV8ThreadSpark::processData(char* text){
	myScriptingV8Component->processData(text);
}

//IControlVoice implementation
void ScriptingV8ThreadSpark::startSpeaking(void){
	myScriptingV8Component->startSpeaking();
}

void ScriptingV8ThreadSpark::stopSpeaking(void){
	myScriptingV8Component->stopSpeaking();
}

void ScriptingV8ThreadSpark::startVoice(void){
	myScriptingV8Component->startVoice();
}
