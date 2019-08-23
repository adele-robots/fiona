/*
 * ScriptV8ThreadSpark.cpp
 *
 *  Created on: 15/11/2012
 *      Author: guille
 */

/// @file ScriptV8ThreadSpark.cpp
/// @brief ScriptV8ThreadSpark class implementation.


//#include "stdAfx.h"
#include "ScriptV8ThreadSpark.h"


#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "ScriptV8ThreadSpark")) {
			return new ScriptV8ThreadSpark(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif

/// Initializes ScriptV8ThreadSpark component.
void ScriptV8ThreadSpark::init(void){
	//2-configurations
	myScriptV8Component->componentConfiguration = componentConfiguration;
	myThreadComponent->componentConfiguration = componentConfiguration;

	//3-connections
	myScriptV8Component->myFaceExpression = myFaceExpression;
	myScriptV8Component->myCamera = myCamera;
	myScriptV8Component->myNeck = myNeck;
	myScriptV8Component->myEyes = myEyes;
	myScriptV8Component->myFlow = myFlow;
	myScriptV8Component->myControlVoice = myControlVoice;
	myScriptV8Component->myAnimation = myAnimation;
	myScriptV8Component->myJoint = myJoint;
	//myScriptV8Component->myFrameEventPublisher = myFrameEventPublisher;

	//required interfaces of ThreadComponent
	myThreadComponent->myThreadProc = myScriptV8Component;
	myThreadComponent->myAsyncFatalErrorHandler = myAsyncFatalErrorHandler;

	//4-initializations
	myScriptV8Component->init();
	myThreadComponent->init();

	myFrameEventPublisher->addFrameEventSubscriber(this);
}

/// Unitializes the ScriptV8ThreadSpark component.
void ScriptV8ThreadSpark::quit(void){
	myScriptV8Component->quit();
	myThreadComponent->quit();
}


//IConcurrent implementation
void ScriptV8ThreadSpark::start(void){
	myThreadComponent->start();
}

void ScriptV8ThreadSpark::stop(void){
	myThreadComponent->stop();
}

//FrameEventSubscriber implementation
void ScriptV8ThreadSpark::notifyFrameEvent(void){
	myScriptV8Component->notifyFrameEvent();
}

//IFaceExpression implementation
void ScriptV8ThreadSpark::setFaceExpression(char *expressionName,float intensity){
	myScriptV8Component->setFaceExpression(expressionName,intensity);
}

//ICamera implementation
void ScriptV8ThreadSpark::setCameraPosition(float X,float Y,float Z){
	myScriptV8Component->setCameraPosition(X,Y,Z);
}

void ScriptV8ThreadSpark::setCameraRotation(float X,float Y,float Z){
	myScriptV8Component->setCameraRotation(X,Y,Z);
}

void ScriptV8ThreadSpark::setCameraParameters(bool isOrtho, float visionAngle,float nearClippingPlane,float farClippingPlane){
	myScriptV8Component->setCameraParameters( isOrtho,  visionAngle, nearClippingPlane, farClippingPlane);
}

//IFlow<char*> implementation
void ScriptV8ThreadSpark::processData(char* text){
	myScriptV8Component->processData(text);
}

//IControlVoice implementation
void ScriptV8ThreadSpark::startSpeaking(){
	myScriptV8Component->startSpeaking();
}

void ScriptV8ThreadSpark::stopSpeaking(){
	myScriptV8Component->stopSpeaking();
}

void ScriptV8ThreadSpark::startVoice(){
	myScriptV8Component->startVoice();
}
