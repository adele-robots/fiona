/*
 * MouseInfoThreadSpark.h
 *
 *  Created on: 12/11/2013
 *      Author: guille
 */

/// @file MouseInfoThreadSpark.h
/// @brief Component MouseInfoThreadSpark main class.


#ifndef __MouseInfoThreadSpark_H
#define __MouseInfoThreadSpark_H


#include "Component.h"

#include "IAsyncFatalErrorHandler.h"
#include "IFlow.h"
#include "IThreadProc.h"
#include "IConcurrent.h"
#include "IFrameEventPublisher.h"
#include "FrameEventSubscriber.h"

#include "../MouseInfoSpark/MouseInfoSpark.h"
#include "../ThreadSpark/ThreadComponent.h"

/// @brief This is the main class for component MouseInfoThreadSpark.
///
/// 

class MouseInfoThreadSpark :
	public Component,

	public IFlow<char*>,
	public FrameEventSubscriber,
	public IConcurrent
{
public:
		MouseInfoThreadSpark(
				char *instanceName,
				ComponentSystem *cs
		) : Component(instanceName, cs)
		{
			//1-Instances
			myMouseInfoComponent=new MouseInfoSpark(instanceName,cs);
			myThreadComponent=new ThreadComponent(instanceName,cs);
		}
		virtual ~MouseInfoThreadSpark(){};

protected:
	IFlow<char*> *myFlow;
	IAsyncFatalErrorHandler *myAsyncFatalErrorHandler;
	IFrameEventPublisher *myFrameEventPublisher;

	void initializeRequiredInterfaces() {	
		requestRequiredInterface<IFlow<char *> >(&myFlow);
		requestRequiredInterface<IAsyncFatalErrorHandler>(&myAsyncFatalErrorHandler);
		requestRequiredInterface<IFrameEventPublisher>(&myFrameEventPublisher);
	}

public:
	//Instance sub-components
	MouseInfoSpark *myMouseInfoComponent;
	ThreadComponent *myThreadComponent;

	//Mandatory
	void init(void);
	void quit(void);

	//IFlow implementation
	void processData(char *);

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
