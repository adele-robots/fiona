/// @file EmailReaderThreadSpark.h
/// @brief Component EmailReaderThreadSpark main class.


#ifndef __EmailReaderThreadSpark_H
#define __EmailReaderThreadSpark_H


#include "Component.h"

// Provided
#include "IFlow.h"
#include "IThreadProc.h"
#include "IConcurrent.h"


#include "../EmailReaderSpark/EmailReaderSpark.h"
#include "../ThreadSpark/ThreadComponent.h"

/// @brief This is the main class for component EmailReaderThreadSpark.
///
/// 

class EmailReaderThreadSpark :
	public Component,
	//**Component class must extend provided interfaces**
	public IFlow<char*>,
	public IConcurrent
{
public:
		EmailReaderThreadSpark(
				char *instanceName,
				ComponentSystem *cs
		) : Component(instanceName, cs)
		{
			//1-Instances
			myEmailReaderComponent=new EmailReaderSpark(instanceName,cs);
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
	EmailReaderSpark *myEmailReaderComponent;
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
