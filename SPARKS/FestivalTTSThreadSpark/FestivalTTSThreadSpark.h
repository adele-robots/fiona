/*
 * FestivalTTSThreadSpark.h
 *
 *  Created on: 26/12/2013
 *      Author: jandro
 */


/// @file FestivalTTSThreadSpark.h
/// @brief Component FestivalTTSThreadSpark main class.


#ifndef __FestivalTTSThreadSpark_H
#define __FestivalTTSThreadSpark_H


#include "Component.h"

#include "IAsyncFatalErrorHandler.h"
#include "IFlow.h"
#include "IAudioQueue.h"
#include "IThreadProc.h"
#include "IConcurrent.h"

#include "../FestivalTTSSpark/FestivalTTSSpark.h"
#include "../ThreadSpark/ThreadComponent.h"

/// @brief This is the main class for component FestivalTTSThreadSpark.
///
///

class FestivalTTSThreadSpark :
	public Component,

	public IFlow<char*>,
	public IAudioQueue,
	public IConcurrent
{
public:
		FestivalTTSThreadSpark(
				char *instanceName,
				ComponentSystem *cs
		) : Component(instanceName, cs)
		{
			//1-Instances
			myFestivalTTSComponent=new FestivalTTSSpark(instanceName,cs);
			myThreadComponent=new ThreadComponent(instanceName,cs);
		}
		virtual ~FestivalTTSThreadSpark() {};

private:
	IAsyncFatalErrorHandler *myAsyncFatalErrorHandler;

	void initializeRequiredInterfaces() {
		requestRequiredInterface<IAsyncFatalErrorHandler>(&myAsyncFatalErrorHandler);
	}

public:
	//Instance sub-components
	FestivalTTSSpark *myFestivalTTSComponent;
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
