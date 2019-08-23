/// @file IvonaThreadSpark.h
/// @brief Component IvonaThreadSpark main class.


#ifndef __IvonaThreadSpark_H
#define __IvonaThreadSpark_H


#include "Component.h"
// Required
#include "IAsyncFatalErrorHandler.h"

// Provided
#include "IFlow.h"
#include "IAudioQueue.h"
#include "IThreadProc.h"
#include "IConcurrent.h"


#include "../IvonaSpark/IvonaSpark.h"
#include "../ThreadSpark/ThreadComponent.h"

/// @brief This is the main class for component IvonaThreadSpark.
///
/// 

class IvonaThreadSpark :
	public Component,
	//**Component class must extend provided interfaces**
	public IFlow<char*>,
	public IAudioQueue,
	public IConcurrent
{
public:
		IvonaThreadSpark(
				char *instanceName,
				ComponentSystem *cs
		) : Component(instanceName, cs)
		{
			//1-Instances
			myIvonaComponent=new IvonaSpark(instanceName,cs);
			myThreadComponent=new ThreadComponent(instanceName,cs);
		}

		virtual ~IvonaThreadSpark() {};

private:
	IAsyncFatalErrorHandler *myAsyncFatalErrorHandler;

	void initializeRequiredInterfaces() {
		requestRequiredInterface<IAsyncFatalErrorHandler>(&myAsyncFatalErrorHandler);
	}

public:
	//Instance sub-components
	IvonaSpark *myIvonaComponent;
	ThreadComponent *myThreadComponent;

	//Mandatory
	void init(void);
	void quit(void);
	
	//IFlow<char*> implementation
	void processData(char * prompt);

	// IAudioQueue implementation
	int getStoredAudioSize();
	void queueAudioBuffer(char *buffer, int size);
	void dequeueAudioBuffer(char *buffer, int size);
	void reset();

	//IConcurrent implementation
	void start();
	void stop();

	

private:
	//Put class attributes here

private:
	//Put class private methods here
	
};

#endif
