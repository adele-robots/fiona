/*
 * ScriptingThreadSpark.h
 *
 *  Created on: 08/11/2012
 *      Author: guille
 */

/// @file ScriptingThreadSpark.h
/// @brief Component ScriptingThreadSpark main class.


#ifndef __ScriptingThreadSpark_H
#define __ScriptingThreadSpark_H


#include "Component.h"
#include "IThreadProc.h"
#include "IConcurrent.h"
#include "IAsyncFatalErrorHandler.h"

#include "IFaceExpression.h"
#include "ICamera.h"


#include "../ScriptingSpark/ScriptingSpark.h"
#include "../ThreadSpark/ThreadComponent.h"

/// @brief This is the main class for component ScriptingThreadSpark.
///
/// 

class ScriptingThreadSpark :
	public Component,

	public IConcurrent,
	public IFaceExpression
{
public:
		ScriptingThreadSpark(
				char *instanceName,
				ComponentSystem *cs
		) : Component(instanceName, cs)
		{
			//1-Instances
			myScriptingComponent=new ScriptingSpark(instanceName,cs);
			myThreadComponent=new ThreadComponent(instanceName,cs);
		}

private:
	IAsyncFatalErrorHandler *myAsyncFatalErrorHandler;
	ICamera *myCamera;

	void initializeRequiredInterfaces() {	
		requestRequiredInterface<IAsyncFatalErrorHandler>(&myAsyncFatalErrorHandler);
		requestRequiredInterface<ICamera>(&myCamera);
	}

public:
	//Mandatory
	void init(void);
	void quit(void);

	//Instance sub-components
	ScriptingSpark *myScriptingComponent;
	ThreadComponent *myThreadComponent;

	//IConcurrent implementation
	void start();
	void stop();

	//IFaceExpression implementation
	void setFaceExpression(char *expressionName,float intensity);
	
private:
	//Put class attributes here
private:
	//Put class private methods here
	
};

#endif
