/// @file TwitterThreadSpark.h
/// @brief Component TwitterThreadSpark main class.


#ifndef __TwitterThreadSpark_H
#define __TwitterThreadSpark_H


#include "Component.h"

// Provided
#include "IFlow.h"
#include "IThreadProc.h"
#include "IConcurrent.h"


#include "../TwitterSpark/TwitterSpark.h"
#include "../ThreadSpark/ThreadComponent.h"

/// @brief This is the main class for component TwitterThreadSpark.
///
/// 

class TwitterThreadSpark :
	public Component,
	//**Component class must extend provided interfaces**
	public IFlow<char*>,
	public IConcurrent
{
public:
		TwitterThreadSpark(
				char *instanceName,
				ComponentSystem *cs
		) : Component(instanceName, cs)
		{
			//1-Instances
			myTwitterComponent=new TwitterSpark(instanceName,cs);
			myThreadComponent=new ThreadComponent(instanceName,cs);
		}

private:
	IFlow<char*> *myFlow;
	IAsyncFatalErrorHandler *myAsyncFatalErrorHandler;

	void initializeRequiredInterfaces() {	
		requestRequiredInterface<IFlow<char *> >(&myFlow);
	}

public:
	//Instance sub-components
	TwitterSpark *myTwitterComponent;
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
