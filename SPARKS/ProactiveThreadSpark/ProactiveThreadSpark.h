/*
 * ProactiveThreadSpark.h
 *
 *  Created on: 03/10/2013
 *      Author: guille
 */

/// @file ProactiveThreadSpark.h
/// @brief Component ProactiveThreadSpark main class.


#ifndef __ProactiveThreadSpark_H
#define __ProactiveThreadSpark_H


#include "Component.h"

#include "IFlow.h"
#include "IThreadProc.h"
#include "IConcurrent.h"
#include "IAsyncFatalErrorHandler.h"

#include "../ProactiveSpark/ProactiveSpark.h"
#include "../ThreadSpark/ThreadComponent.h"


/// @brief This is the main class for component ProactiveThreadSpark.
///
/// 

class ProactiveThreadSpark :
	public Component,

	public IFlow<char*>,
	public IConcurrent
{
public:
		ProactiveThreadSpark(
				char *instanceName,
				ComponentSystem *cs
		) : Component(instanceName, cs)
		{
			//1-Instances
			myProactiveComponent=new ProactiveSpark(instanceName,cs);
			myThreadComponent=new ThreadComponent(instanceName,cs);
		}
		virtual ~ProactiveThreadSpark(){}

protected:
	IFlow<char*> *myFlow;
	IAsyncFatalErrorHandler *myAsyncFatalErrorHandler;

	void initializeRequiredInterfaces() {	
		requestRequiredInterface<IFlow<char *> >(&myFlow);
		requestRequiredInterface<IAsyncFatalErrorHandler>(&myAsyncFatalErrorHandler);
	}

public:
	//Instance sub-components
	ProactiveSpark *myProactiveComponent;
	ThreadComponent *myThreadComponent;

	//Mandatory
	void init(void);
	void quit(void);
	
	//IFlow implementation
	void processData(char *);

	//IConcurrent implementation
	void start();
	void stop();

	
};

#endif
