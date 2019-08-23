/*
 * ASRGoogleThreadSpark.h
 *
 *  Created on: 06/06/2014
 *      Author: jandro
 */


/// @file ASRGoogleThreadSpark.h
/// @brief Component ASRGoogleThreadSpark main class.


#ifndef __ASRGoogleThreadSpark_H
#define __ASRGoogleThreadSpark_H


#include "Component.h"

#include "IAsyncFatalErrorHandler.h"
#include "IFlow.h"
#include "IThreadProc.h"
#include "IConcurrent.h"

#include "../ASRGoogleSpark/ASRGoogleSpark.h"
#include "../ThreadSpark/ThreadComponent.h"

/// @brief This is the main class for component ASRGoogleThreadSpark.
///
///

class ASRGoogleThreadSpark :
	public Component,

	public IFlow<AudioWrap*>,
	public IConcurrent
{
public:
		ASRGoogleThreadSpark(
				char *instanceName,
				ComponentSystem *cs
		) : Component(instanceName, cs)
		{
			//1-Instances
			myASRGoogleComponent=new ASRGoogleSpark(instanceName,cs);
			myThreadComponent=new ThreadComponent(instanceName,cs);
		}
		virtual ~ASRGoogleThreadSpark() {};

private:
	IFlow<char*> *myFlow;
	IAsyncFatalErrorHandler *myAsyncFatalErrorHandler;

	void initializeRequiredInterfaces() {
		requestRequiredInterface<IFlow<char *> >(&myFlow);
		requestRequiredInterface<IAsyncFatalErrorHandler>(&myAsyncFatalErrorHandler);
	}

public:
	//Instance sub-components
	ASRGoogleSpark *myASRGoogleComponent;
	ThreadComponent *myThreadComponent;

	//Mandatory
	void init(void);
	void quit(void);

	// IFlow<AudioWrap*> implementation
	void processData(AudioWrap *audio);

	//IConcurrent implementation
	void start();
	void stop();

private:
	//Put class attributes here
private:
	//Put class private methods here

};

#endif
