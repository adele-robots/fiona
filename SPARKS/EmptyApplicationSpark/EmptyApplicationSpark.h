/// @file EmptyApplicationSpark.cpp
/// @brief Component EmptyApplicationSpark main class.

#ifndef __EmptyApplicationSpark_H
#define __EmptyApplicationSpark_H

#include "Component.h"

// Provided
#include "IApplication.h"
#include "IAsyncFatalErrorHandler.h"


/// @brief This is the main class for component EmptyApplicationSpark.

class EmptyApplicationSpark :
	public Component,
	public IApplication,
	public IAsyncFatalErrorHandler
{
public:
	EmptyApplicationSpark(
		char *instanceName, 
		ComponentSystem *cs
	) : Component(instanceName, cs)
	{}

private:
	void initializeRequiredInterfaces() {	
		// no required interfaces
	}

public:
	static void signalHandler(int sig);
	void init();
	void quit();
	
	// IApplication implementation
	void run();

	// IAsyncFatalErrorHandler implementation
	void handleError(char *msg);


private:
	void eventLoop();
};


#endif
