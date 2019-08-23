/*
 * PicoTTSThreadSpark.h
 *
 *  Created on: 14/01/2013
 *      Author: guille
 */


/// @file PicoTTSThreadSpark.h
/// @brief Component PicoTTSThreadSpark main class.


#ifndef __PicoTTSThreadSpark_H
#define __PicoTTSThreadSpark_H


#include "Component.h"

#include "IAsyncFatalErrorHandler.h"
#include "IFlow.h"
#include "IAudioQueue.h"
#include "IThreadProc.h"
#include "IConcurrent.h"

#include "../PicoTTSSpark/PicoTTSSpark.h"
#include "../ThreadSpark/ThreadComponent.h"

/// @brief This is the main class for component PicoTTSThreadSpark.
///
/// 

class PicoTTSThreadSpark :
	public Component,

	public IFlow<char*>,
	public IAudioQueue,
	public IConcurrent
{
public:
		PicoTTSThreadSpark(
				char *instanceName,
				ComponentSystem *cs
		) : Component(instanceName, cs)
		{
			//1-Instances
			myPicoTTSComponent=new PicoTTSSpark(instanceName,cs);
			myThreadComponent=new ThreadComponent(instanceName,cs);
		}
		virtual ~PicoTTSThreadSpark() {};

private:
	IAsyncFatalErrorHandler *myAsyncFatalErrorHandler;

	void initializeRequiredInterfaces() {	
		requestRequiredInterface<IAsyncFatalErrorHandler>(&myAsyncFatalErrorHandler);
	}

public:
	//Instance sub-components
	PicoTTSSpark *myPicoTTSComponent;
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
