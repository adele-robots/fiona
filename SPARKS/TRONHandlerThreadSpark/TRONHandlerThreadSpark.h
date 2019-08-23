/*
 * TRONHandlerThreadSpark.h
 *
 *  Created on: 01/02/2013
 *      Author: guille
 */

/// @file TRONHandlerThreadSpark.h
/// @brief Component TRONHandlerThreadSpark main class.


#ifndef __TRONHandlerThreadSpark_H
#define __TRONHandlerThreadSpark_H


#include "Component.h"
#include "IFlow.h"
#include "IConcurrent.h"
#include "IAsyncFatalErrorHandler.h"

#include "../TRONHandlerSpark/include/TRONHandlerSpark.h"
#include "../TRONAVInputSpark/TRONAVInputSpark.h"
#include "../ThreadSpark/ThreadComponent.h"



/// @brief This is the main class for component TRONHandlerThreadSpark.
///
/// 

class TRONHandlerThreadSpark :
	public Component,
	public IFlow<char*>,
	public IConcurrent
{
public:
		TRONHandlerThreadSpark(
				char *instanceName,
				ComponentSystem *cs
		) : Component(instanceName, cs)
		{
			//1-Instances
			myTRONHandlerComponent=new TRONHandlerSpark(instanceName,cs);
			myTRONAVInputComponent=new TRONAVInputSpark(instanceName,cs);
			myThreadComponent=new ThreadComponent(instanceName,cs);
		}
		virtual ~TRONHandlerThreadSpark() {};

private:
		IFlow<char*> *myCharFlow;
		IFlow<AudioWrap *> *myAudioFlow;
		IFlow<Image *> *myImageFlow;
	IAsyncFatalErrorHandler *myAsyncFatalErrorHandler;
	void initializeRequiredInterfaces() {	
		requestRequiredInterface<IFlow<char *> >(&myCharFlow);
		requestRequiredInterface<IFlow<AudioWrap *> >(&myAudioFlow);
		requestRequiredInterface<IFlow<Image *> >(&myImageFlow);
		requestRequiredInterface<IAsyncFatalErrorHandler>(&myAsyncFatalErrorHandler);
	}

public:
	//Instance sub-components
	TRONHandlerSpark *myTRONHandlerComponent;
	TRONAVInputSpark *myTRONAVInputComponent;
	ThreadComponent *myThreadComponent;

	//Mandatory
	void init(void);
	void quit(void);

	//IFlow<char*> implementation
	void processData(char * prompt);
	
	//IConcurrent implementation
	void start();
	void stop();

private:
	//Put class attributes here
private:
	//Put class private methods here
	
};

#endif
