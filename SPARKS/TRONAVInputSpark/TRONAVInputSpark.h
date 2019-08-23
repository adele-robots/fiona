/// @file TRONAVInputSpark.h
/// @brief Component TRONAVInputSpark main class.


#ifndef __TRONAVInputSpark_H
#define __TRONAVInputSpark_H

#include "Component.h"
#include "IConcurrent.h"
#include "IFlow.h"
#include "IAsyncFatalErrorHandler.h"

#include "../ThreadSpark/ThreadComponent.h"
#include "../TRONReaderComponent/TRONReaderComponent.h"

/// @brief This is the main class for component TRONAVInputSpark.
///
///

class TRONAVInputSpark :
	public Component,
	public IConcurrent

{
public:
		TRONAVInputSpark(
		char *instanceName, 
		ComponentSystem *cs
	) : Component(instanceName, cs)
	{
		//1-Instances of the components needed within the spark
		myThreadComponent=new ThreadComponent(instanceName,cs);
		myTRONReaderComponent=new TRONReaderComponent(instanceName,cs);
	}


private:
	IAsyncFatalErrorHandler *myAsyncFatalErrorHandler;

	void initializeRequiredInterfaces() {	
		requestRequiredInterface<IAsyncFatalErrorHandler>(&myAsyncFatalErrorHandler);
		requestRequiredInterface<IFlow<Image *> >(&myImageFlow);
		requestRequiredInterface<IFlow<AudioWrap *> >(&myAudioFlow);
	}

public:
	//Instance sub-components
	ThreadComponent *myThreadComponent;
	TRONReaderComponent *myTRONReaderComponent;

	IFlow<Image *> *myImageFlow;
	IFlow<AudioWrap *> *myAudioFlow;

	//Mandatory
	void init(void);
	void quit(void);

	//IConcurrent implementation
	void start(void);
	void stop(void);
};

#endif
