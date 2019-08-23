/// @file IPCThreadSpark.h
/// @brief Component IPCThreadSpark main class.


#ifndef __IPCThreadSpark_H
#define __IPCThreadSpark_H


#include "Component.h"
// Required
#include "IAsyncFatalErrorHandler.h"

// Provided
#include "IFlow.h"
#include "IThreadProc.h"
#include "IConcurrent.h"


#include "../IPCSpark/IPCSpark.h"
#include "../ThreadSpark/ThreadComponent.h"

/// @brief This is the main class for component IPCThreadSpark.
///
///

class IPCThreadSpark :
	public Component,
	//**Component class must extend provided interfaces**
	public IFlow<char*>,
	public IConcurrent
{
public:
		IPCThreadSpark(
				char *instanceName,
				ComponentSystem *cs
		) : Component(instanceName, cs)
		{
			//1-Instances
			myIPCComponent=new IPCSpark(instanceName,cs);
			myThreadComponent=new ThreadComponent(instanceName,cs);
		}

private:
	IAsyncFatalErrorHandler *myAsyncFatalErrorHandler;

	void initializeRequiredInterfaces() {
		requestRequiredInterface<IAsyncFatalErrorHandler>(&myAsyncFatalErrorHandler);
	}

public:
	//Instance sub-components
	IPCSpark *myIPCComponent;
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
