/// @file ChatThreadSpark.h
/// @brief Component ChatThreadSpark main class.


#ifndef __ChatThreadSpark_H
#define __ChatThreadSpark_H


#include "Component.h"
// Required
#include "IAsyncFatalErrorHandler.h"

// Provided
#include "IFlow.h"
#include "IThreadProc.h"
#include "IConcurrent.h"


#include "../ChatSpark/ChatSpark.h"
#include "../ThreadSpark/ThreadComponent.h"

/// @brief This is the main class for component ChatThreadSpark.
///
/// 

class ChatThreadSpark :
	public Component,
	//**Component class must extend provided interfaces**
	public IFlow<char*>,
	public IConcurrent
{
public:
		ChatThreadSpark(
				char *instanceName,
				ComponentSystem *cs
		) : Component(instanceName, cs)
		{
			//1-Instances
			myChatComponent=new ChatSpark(instanceName,cs);
			myThreadComponent=new ThreadComponent(instanceName,cs);
		}

private:
	IFlow<char*> *myFlow;
	IAsyncFatalErrorHandler *myAsyncFatalErrorHandler;

	void initializeRequiredInterfaces() {	
		requestRequiredInterface<IFlow<char *> >(&myFlow);
		requestRequiredInterface<IAsyncFatalErrorHandler>(&myAsyncFatalErrorHandler);
	}

public:
	//Instance sub-components
	ChatSpark *myChatComponent;
	ThreadComponent *myThreadComponent;

	//Mandatory
	void init(void);
	void quit(void);
	
	//IFlow implementation
	void processData(char *);

	//IConcurrent implementation
	void start();
	void stop();
	
private:
	//Put class attributes here
private:
	//Put class private methods here
	
};

#endif
