/*
 * VerbioTTSThreadSpark.h
 *
 *  Created on: 10/07/2013
 *      Author: guille
 */


/// @file VerbioTTSThreadSpark.h
/// @brief Component VerbioTTSThreadSpark main class.


#ifndef __VerbioTTSThreadSpark_H
#define __VerbioTTSThreadSpark_H


#include "Component.h"

#include "IAsyncFatalErrorHandler.h"
#include "IFlow.h"
#include "IAudioQueue.h"
#include "IThreadProc.h"
#include "IConcurrent.h"

#include "../VerbioTTSSpark/VerbioTTSSpark.h"
#include "../ThreadSpark/ThreadComponent.h"


/// @brief This is the main class for component VerbioTTSThreadSpark.
///
/// 

class VerbioTTSThreadSpark :
	public Component,

	public IFlow<char*>,
	public IAudioQueue,
	public IConcurrent
{
public:
		VerbioTTSThreadSpark(
				char *instanceName,
				ComponentSystem *cs
		) : Component(instanceName, cs)
		{
			//1-Instances
			myVerbioTTSComponent=new VerbioTTSSpark(instanceName,cs);
			myThreadComponent=new ThreadComponent(instanceName,cs);
		}
		virtual ~VerbioTTSThreadSpark() {};

private:
	IAsyncFatalErrorHandler *myAsyncFatalErrorHandler;

	void initializeRequiredInterfaces() {	
		requestRequiredInterface<IAsyncFatalErrorHandler>(&myAsyncFatalErrorHandler);
	}

public:
	//Instance sub-components
	VerbioTTSSpark *myVerbioTTSComponent;
	ThreadComponent *myThreadComponent;

	//Mandatory
	void init(void);
	void quit(void);
	
	// IFlow<char*> implementation
	void processData(char *prompt);

	// IAudioQueue implementation
	int getStoredAudioSize();
	void queueAudioBuffer(char *buffer, int size);
	void dequeueAudioBuffer(char *buffer, int size);
	void reset(); // vaciar el buffer

	//IConcurrent implementation
	void start();
	void stop();
	
private:
	//Put class attributes here
private:
	//Put class private methods here
	
};

#endif
