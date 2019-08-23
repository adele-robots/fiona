/*
 * ScriptingV8ThreadSpark.h
 *
 *  Created on: 03/01/2013
 *      Author: guille
 */

/// @file ScriptingV8ThreadSpark.h
/// @brief Component ScriptingV8ThreadSpark main class.


#ifndef __ScriptingV8ThreadSpark_H
#define __ScriptingV8ThreadSpark_H


#include "Component.h"
#include "IThreadProc.h"
#include "IConcurrent.h"
#include "IAsyncFatalErrorHandler.h"
#include "IFrameEventPublisher.h"
#include "FrameEventSubscriber.h"
#include "IFlow.h"
#include "INeck.h"
#include "IEyes.h"
#include "ICamera.h"
#include "IControlVoice.h"
#include "IAnimation.h"

#include "IFaceExpression.h"

#include "../ScriptingV8Spark/ScriptingV8Spark.h"
#include "../ThreadSpark/ThreadComponent.h"


/// @brief This is the main class for component ScriptingV8ThreadSpark.
///
/// 

class ScriptingV8ThreadSpark :
	public Component,

	public FrameEventSubscriber,
	public IConcurrent,
	public IFaceExpression,
	public IFlow<char*>,
	public IControlVoice
{
public:
		ScriptingV8ThreadSpark(
				char *instanceName,
				ComponentSystem *cs
		) : Component(instanceName, cs)
		{
			//1-Instances
			myScriptingV8Component=new ScriptingV8Spark(instanceName,cs);
			myThreadComponent=new ThreadComponent(instanceName,cs);
		}
		virtual ~ScriptingV8ThreadSpark(){};

		IFaceExpression *myFaceExpression;
		IFrameEventPublisher *myFrameEventPublisher;
		INeck *myNeck;
		IEyes *myEyes;
		IFlow<char*> *myFlow;
		ICamera *myCamera;
		IControlVoice *myControlVoice;
		IAnimation *myAnimation;

private:
	IAsyncFatalErrorHandler *myAsyncFatalErrorHandler;

	void initializeRequiredInterfaces() {
		requestRequiredInterface<IAsyncFatalErrorHandler>(&myAsyncFatalErrorHandler);
		requestRequiredInterface<IFaceExpression>(&myFaceExpression);
		requestRequiredInterface<IFrameEventPublisher>(&myFrameEventPublisher);
		requestRequiredInterface<INeck>(&myNeck);
		requestRequiredInterface<IEyes>(&myEyes);
		requestRequiredInterface<ICamera>(&myCamera);
		requestRequiredInterface<IFlow<char *> >(&myFlow);
		requestRequiredInterface<IControlVoice>(&myControlVoice);
		requestRequiredInterface<IAnimation>(&myAnimation);
	}

public:
	//Mandatory
	void init(void);
	void quit(void);
	
	//Instance sub-components
	ScriptingV8Spark *myScriptingV8Component;
	ThreadComponent *myThreadComponent;

	//IConcurrent implementation
	void start();
	void stop();

	//FrameEventSubscriber
	void notifyFrameEvent();

	//IFaceExpression implementation
	void setFaceExpression(char *expressionName,float intensity);
	
	//IFlow<char*> implementation
	void processData(char *);

	//IControlVoice
	void startSpeaking();
	void stopSpeaking();
	void startVoice();

private:
	//Put class attributes here
private:
	//Put class private methods here
	
};

#endif
