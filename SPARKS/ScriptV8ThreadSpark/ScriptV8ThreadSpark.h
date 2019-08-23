/*
 * ScriptV8ThreadSpark.h
 *
 *  Created on: 15/11/2012
 *      Author: guille
 */

/// @file ScriptV8ThreadSpark.h
/// @brief Component ScriptV8ThreadSpark main class.


#ifndef __ScriptV8ThreadSpark_H
#define __ScriptV8ThreadSpark_H


#include "Component.h"
#include "IThreadProc.h"
#include "IConcurrent.h"
#include "IAsyncFatalErrorHandler.h"

#include "IFaceExpression.h"
#include "ICamera.h"
#include "INeck.h"
#include "IEyes.h"
#include "IFrameEventPublisher.h"
#include "FrameEventSubscriber.h"
#include "IFlow.h"
#include "IControlVoice.h"
#include "IAnimation.h"
#include "IJoint.h"

#include "../ScriptV8Spark/ScriptV8Spark.h"
#include "../ThreadSpark/ThreadComponent.h"

/// @brief This is the main class for component ScriptV8ThreadSpark.
///
/// 

class ScriptV8ThreadSpark :
	public Component,

	public IConcurrent,
	public IFaceExpression,
	public ICamera,
	public FrameEventSubscriber,
	public IFlow<char*>,
	public IControlVoice
{
public:
		ScriptV8ThreadSpark(
				char *instanceName,
				ComponentSystem *cs
		) : Component(instanceName, cs)
		{
			//1-Instances
			myScriptV8Component=new ScriptV8Spark(instanceName,cs);
			myThreadComponent=new ThreadComponent(instanceName,cs);
		}
		virtual ~ScriptV8ThreadSpark(){};

		IFaceExpression *myFaceExpression;
		ICamera *myCamera;
		IFrameEventPublisher *myFrameEventPublisher;
		INeck *myNeck;
		IEyes *myEyes;
		IFlow<char*> *myFlow;
		IControlVoice *myControlVoice;
		IAnimation *myAnimation;
		IJoint *myJoint;

private:
	IAsyncFatalErrorHandler *myAsyncFatalErrorHandler;


	void initializeRequiredInterfaces() {	
		requestRequiredInterface<IAsyncFatalErrorHandler>(&myAsyncFatalErrorHandler);
		requestRequiredInterface<IFaceExpression>(&myFaceExpression);
		requestRequiredInterface<ICamera>(&myCamera);
		requestRequiredInterface<IFrameEventPublisher>(&myFrameEventPublisher);
		requestRequiredInterface<INeck>(&myNeck);
		requestRequiredInterface<IEyes>(&myEyes);
		requestRequiredInterface<IFlow<char *> >(&myFlow);
		requestRequiredInterface<IControlVoice>(&myControlVoice);
		requestRequiredInterface<IAnimation>(&myAnimation);
		requestRequiredInterface<IJoint>(&myJoint);
	}

public:
	//Mandatory
	void init(void);
	void quit(void);
	
	//Instance sub-components
	ScriptV8Spark *myScriptV8Component;
	ThreadComponent *myThreadComponent;

	//IConcurrent implementation
	void start();
	void stop();

	//IFaceExpression implementation
	void setFaceExpression(char *expressionName,float intensity);
	
	//ICamera
	void setCameraPosition(float X,float Y,float Z);
	void setCameraRotation(float X,float Y,float Z);
	void setCameraParameters(bool isOrtho, float visionAngle,float nearClippingPlane,float farClippingPlane);

	//FrameEventSubscriber
	void notifyFrameEvent();

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
