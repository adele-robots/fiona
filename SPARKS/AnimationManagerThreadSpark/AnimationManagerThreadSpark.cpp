/*
 * AnimationManagerThreadSpark.cpp
 *
 *  Created on: 06/09/2013
 *      Author: guille
 */

/// @file AnimationManagerThreadSpark.cpp
/// @brief AnimationManagerThreadSpark class implementation.


//#include "stdAfx.h"
#include "AnimationManagerThreadSpark.h"


#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "AnimationManagerThreadSpark")) {
			return new AnimationManagerThreadSpark(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif

/// Initializes AnimationManagerThreadSpark component.
void AnimationManagerThreadSpark::init(void){
	//2-configurations
	myAnimationManagerComponent->componentConfiguration = componentConfiguration;
	myThreadComponent->componentConfiguration = componentConfiguration;

	//3-connections
	//required interfaces of AnimationManagerSpark
	myAnimationManagerComponent->myFlow = myFlow;
	myAnimationManagerComponent->myJoint = myJoint;
	myAnimationManagerComponent->myAnimation = myAnimation;
	myAnimationManagerComponent->myFaceExpression = myFaceExpression;

	//required interfaces of ThreadComponent
	myThreadComponent->myThreadProc = myAnimationManagerComponent;
	myThreadComponent->myAsyncFatalErrorHandler = myAsyncFatalErrorHandler;

	//4-initializations
	myAnimationManagerComponent->init();
	myThreadComponent->init();

	myFrameEventPublisher->addFrameEventSubscriber(this);
}

/// Unitializes the AnimationManagerThreadSpark component.
void AnimationManagerThreadSpark::quit(void){
	myAnimationManagerComponent->quit();
	myThreadComponent->quit();
}

//IFlow<char*> implementation
void AnimationManagerThreadSpark::processData(char * prompt) {
	myAnimationManagerComponent->processData(prompt);
}

//IConcurrent implementation
void AnimationManagerThreadSpark::start(void){
	myThreadComponent->start();
}

void AnimationManagerThreadSpark::stop(void){
	myThreadComponent->stop();
}

//FrameEventSubscriber implementation
void AnimationManagerThreadSpark::notifyFrameEvent(void){
	myAnimationManagerComponent->notifyFrameEvent();
}



