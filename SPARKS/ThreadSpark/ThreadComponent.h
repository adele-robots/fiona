/// @file ThreadComponent.cpp
/// @brief Component ThreadComponent main class.

#ifndef __ThreadComponent_H
#define __ThreadComponent_H

#include <pthread.h>
#include "Component.h"

// Provided
#include "IConcurrent.h"

// Required
#include "IAsyncFatalErrorHandler.h"
#include "IThreadProc.h"

/// @brief This is the main class for component ThreadComponent.

class ThreadComponent :
	public Component,
	public IConcurrent
{
public:
	ThreadComponent(
		char *instanceName, 
		ComponentSystem *cs
	) : Component(instanceName, cs)
	{}

public:
	IThreadProc *myThreadProc;	// accesed form the static member
	IAsyncFatalErrorHandler *myAsyncFatalErrorHandler;

private:
	void initializeRequiredInterfaces() {	
		requestRequiredInterface<IAsyncFatalErrorHandler>(&myAsyncFatalErrorHandler);
		requestRequiredInterface<IThreadProc>(&myThreadProc);
	}

public:
	
	// Mandatory
	void init();
	void quit();

	//IConcurrent implementation
	void start();
	void stop();

public:
	bool shutDown;

private:
	static void *threadProcedure(void *);

private:
	pthread_t thread_id;
};

#endif
