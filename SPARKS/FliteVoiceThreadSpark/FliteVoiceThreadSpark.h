/*
 * FliteVoiceThreadSpark.h
 *
 *  Created on: 17/10/2012
 *      Author: guille
 */


/// @file FliteVoiceThreadSpark.h
/// @brief Component FliteVoiceThreadSpark main class.


#ifndef __FliteVoiceThreadSpark_H
#define __FliteVoiceThreadSpark_H


#include "Component.h"
// Required
#include "IAsyncFatalErrorHandler.h"
// Provided
#include "IFlow.h"
#include "IAudioQueue.h"
#include "IThreadProc.h"
#include "IConcurrent.h"

#include "../FliteVoiceSpark/FliteVoiceSpark.h"
#include "../ThreadSpark/ThreadComponent.h"

/// @brief This is the main class for component FliteVoiceThreadSpark.
///
/// 

class FliteVoiceThreadSpark :
	public Component,

	public IFlow<char*>,
	public IAudioQueue,
	public IConcurrent
{
public:
		FliteVoiceThreadSpark(
				char *instanceName,
				ComponentSystem *cs
		) : Component(instanceName, cs)
		{
			//1-Instances
			myFliteComponent=new FliteVoiceSpark(instanceName,cs);
			myThreadComponent=new ThreadComponent(instanceName,cs);
		}
		virtual ~FliteVoiceThreadSpark() {};

private:
	IAsyncFatalErrorHandler *myAsyncFatalErrorHandler;

	void initializeRequiredInterfaces() {
		requestRequiredInterface<IAsyncFatalErrorHandler>(&myAsyncFatalErrorHandler);
	}

public:
	//Instance sub-components
	FliteVoiceSpark *myFliteComponent;
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
