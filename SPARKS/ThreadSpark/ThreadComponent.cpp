/// @file ThreadComponent.cpp
/// @brief ThreadComponent class implementation.

#include "stdAfx.h"
#include <stdio.h>
#include <sched.h>
#include "Logger.h"
#include "ThreadComponent.h"


#pragma comment(lib, "pthreadVC2.lib")

#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "ThreadComponent")) {
			return new ThreadComponent(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif

/// Generic thread procedure offering exception handling for inherited thread mechanisms:
/// baground, iterative and timed 

void *ThreadComponent::threadProcedure(void *args) {
	ThreadComponent *threadComponent;
	threadComponent = (ThreadComponent *)args;
	
	try {
		while (!threadComponent->shutDown) {
			threadComponent->myThreadProc->process();
		}
	}
	catch (Exception &ex) {
		threadComponent->shutDown = true;
		threadComponent->myAsyncFatalErrorHandler->handleError(ex.msg);
	}

	return NULL;
}

//IConcurrent implementation

/// Starts the thread
void ThreadComponent::start(void) {
	shutDown = false;

	LoggerInfo("Starting thread '%s'", getInstanceName());

	int res = pthread_create(
		&thread_id, 
		NULL, 
		ThreadComponent::threadProcedure, 
		this
	);

	if (res != 0) {
		ERR("pthread_create");
	}
}

/// Stop the thread, wait until thread termination.
void ThreadComponent::stop(void) {
	int res;

	shutDown = true;

	LoggerInfo("Stopping thread '%s'...", getInstanceName());

	res = pthread_cancel(thread_id);
	
	void *dummy;
	res = pthread_join(thread_id, &dummy);

	if (res != 0) {
		ERR("pthread_join");
	}

	LoggerInfo("Stopped.");
}

void ThreadComponent::init() {}

void ThreadComponent::quit() {}
