/*
 * AnimationManagerThreadSpark.h
 *
 *  Created on: 06/09/2013
 *      Author: guille
 */

/// @file AnimationManagerThreadSpark.h
/// @brief Component AnimationManagerThreadSpark main class.


#ifndef __AnimationManagerThreadSpark_H
#define __AnimationManagerThreadSpark_H


#include "Component.h"

#include "IFlow.h"
#include "IConcurrent.h"
#include "IAsyncFatalErrorHandler.h"
#include "IFrameEventPublisher.h"
#include "FrameEventSubscriber.h"
#include "IJoint.h"
#include "IAnimation.h"
#include "IFaceExpression.h"

#include "../AnimationManagerSpark/AnimationManagerSpark.h"
#include "../ThreadSpark/ThreadComponent.h"


/// @brief This is the main class for component AnimationManagerThreadSpark.
///
/// 

class AnimationManagerThreadSpark :
	public Component,
	public IFlow<char*>,
	public FrameEventSubscriber,
	public IConcurrent
{
public:
		AnimationManagerThreadSpark(
				char *instanceName,
				ComponentSystem *cs
		) : Component(instanceName, cs)
		{
			//1-Instances
			myAnimationManagerComponent=new AnimationManagerSpark(instanceName,cs);
			myThreadComponent=new ThreadComponent(instanceName,cs);
		}
		virtual ~AnimationManagerThreadSpark(){}

private:
	IFlow<char*> *myFlow;
	IAsyncFatalErrorHandler *myAsyncFatalErrorHandler;
	IFrameEventPublisher *myFrameEventPublisher;
	IJoint *myJoint;
	IAnimation *myAnimation;
	IFaceExpression *myFaceExpression;

	void initializeRequiredInterfaces() {	
		requestRequiredInterface<IFlow<char *> >(&myFlow);
		requestRequiredInterface<IJoint>(&myJoint);
		requestRequiredInterface<IAnimation>(&myAnimation);
		requestRequiredInterface<IFaceExpression>(&myFaceExpression);
		requestRequiredInterface<IAsyncFatalErrorHandler>(&myAsyncFatalErrorHandler);
		requestRequiredInterface<IFrameEventPublisher>(&myFrameEventPublisher);
	}

public:
	//Instance sub-components
	AnimationManagerSpark *myAnimationManagerComponent;
	ThreadComponent *myThreadComponent;

	//Mandatory
	void init(void);
	void quit(void);

	//IFlow<char*> implementation
	void processData(char * prompt);

	//IConcurrent implementation
	void start();
	void stop();
	
	//FrameEventSubscriber
	void notifyFrameEvent();

private:
	//Put class attributes here
private:
	//Put class private methods here
	
};

#endif
