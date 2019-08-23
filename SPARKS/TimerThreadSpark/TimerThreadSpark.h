/*
 * TimerThreadSpark.h
 *
 *  Created on: 05/11/2012
 *      Author: guille
 */

/// @file TimerThreadSpark.h
/// @brief Component TimerThreadSpark main class.


#ifndef __TimerThreadSpark_H
#define __TimerThreadSpark_H


#include "Component.h"
#include "IThreadProc.h"
#include "IConcurrent.h"
#include "IAsyncFatalErrorHandler.h"

/*****!!!!!!!!*****/
#include "IFaceExpression.h"
#include "IFlow.h"

#include "../TimerSpark/TimerSpark.h"
#include "../ThreadSpark/ThreadComponent.h"


/// @brief This is the main class for component TimerThreadSpark.
///
/// 

class TimerThreadSpark :
	public Component,

	public IConcurrent
{
public:
		TimerThreadSpark(
				char *instanceName,
				ComponentSystem *cs
		) : Component(instanceName, cs)
		{
			//1-Instances
			myTimerComponent=new TimerSpark(instanceName,cs);
			myThreadComponent=new ThreadComponent(instanceName,cs);
		}
		virtual ~TimerThreadSpark() {};

private:
	IAsyncFatalErrorHandler *myAsyncFatalErrorHandler;

	/*****!!!!!!!!*****/
	IFaceExpression *myFaceExpression;
	IFlow<char *> *myFlow;

	void initializeRequiredInterfaces() {
		requestRequiredInterface<IAsyncFatalErrorHandler>(&myAsyncFatalErrorHandler);
		/*****!!!!!!!!*****/
		requestRequiredInterface<IFaceExpression>(&myFaceExpression);
		requestRequiredInterface<IFlow<char *> >(&myFlow);
	}

public:
	//Mandatory
	void init(void);
	void quit(void);
	
	//Instance sub-components
	TimerSpark *myTimerComponent;
	ThreadComponent *myThreadComponent;

	//IConcurrent implementation
	void start();
	void stop();
	
private:
	//Put class attributes here
private:
	//Put class private methods here
	
};

#endif
