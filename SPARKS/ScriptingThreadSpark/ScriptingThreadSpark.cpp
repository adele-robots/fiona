/*
 * ScriptingThreadSpark.cpp
 *
 *  Created on: 08/11/2012
 *      Author: guille
 */

/// @file ScriptingThreadSpark.cpp
/// @brief ScriptingThreadSpark class implementation.


//#include "stdAfx.h"
#include "ScriptingThreadSpark.h"


#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "ScriptingThreadSpark")) {
			return new ScriptingThreadSpark(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif

/// Initializes ScriptingThreadSpark component.
void ScriptingThreadSpark::init(void){
	//2-configurations
	myScriptingComponent->componentConfiguration = componentConfiguration;
	myThreadComponent->componentConfiguration = componentConfiguration;

	//3-connections
	myScriptingComponent->myCamera = myCamera;

	//required interfaces of ThreadComponent
	myThreadComponent->myThreadProc = myScriptingComponent;
	myThreadComponent->myAsyncFatalErrorHandler = myAsyncFatalErrorHandler;

	//4-initializations
	myScriptingComponent->init();
	myThreadComponent->init();
}


/// Unitializes the ScriptingThreadSpark component.
void ScriptingThreadSpark::quit(void){
	myScriptingComponent->quit();
	myThreadComponent->quit();
}

//IConcurrent implementation
void ScriptingThreadSpark::start(void){
	myThreadComponent->start();
}

void ScriptingThreadSpark::stop(void){
	myThreadComponent->stop();
}

void ScriptingThreadSpark::setFaceExpression(char *expressionName,float intensity){
	myScriptingComponent->setFaceExpression(expressionName,intensity);
}



