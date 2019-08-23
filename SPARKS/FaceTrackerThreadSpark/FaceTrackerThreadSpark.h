/// @file FaceTrackerThreadSpark.h
/// @brief Component FaceTrackerThreadSpark main class.


#ifndef __FaceTrackerThreadSpark_H
#define __FaceTrackerThreadSpark_H

#include "Component.h"

#include "IDetectedFacePositionConsumer.h"
#include "IAsyncFatalErrorHandler.h"

#include "IFlow.h"
#include "IConcurrent.h"

#include "IThreadProc.h"


#include "../FaceTrackerSpark/FaceTrackerSpark.h"
#include "../ThreadSpark/ThreadComponent.h"


/// @brief This is the main class for component FaceTrackerThreadSpark.
///
///

class FaceTrackerThreadSpark :
	public Component,

	public IFlow<Image *>,
	public IConcurrent
{
public:
		FaceTrackerThreadSpark(
		char *instanceName, 
		ComponentSystem *cs
	) : Component(instanceName, cs)
	{
		//1-Instances
		myFaceTrackerComponent=new FaceTrackerSpark(instanceName,cs);
		myThreadComponent=new ThreadComponent(instanceName,cs);	
	}

private:

	IDetectedFacePositionConsumer *myDetectedFacePositionConsumer;
	IAsyncFatalErrorHandler *myAsyncFatalErrorHandler;

	void initializeRequiredInterfaces() {	
		requestRequiredInterface<IDetectedFacePositionConsumer>(&myDetectedFacePositionConsumer);
		requestRequiredInterface<IAsyncFatalErrorHandler>(&myAsyncFatalErrorHandler);
	}

public:
	//Instance sub-components
	FaceTrackerSpark *myFaceTrackerComponent;
	ThreadComponent *myThreadComponent;

	void init(void);
	void quit(void);

	//IConcurrent implementation
	void start();
	void stop();

	//IFlow implementation
	void processData(Image *);
};

#endif
