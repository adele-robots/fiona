/// @file TestFaceTrackerSpark.cpp
/// @brief Component TestFaceTrackerSpark main class.


#ifndef __TestFaceTrackerSpark_H
#define __TestFaceTrackerSpark_H

#include "Component.h"

#include "IApplication.h"
#include "IDetectedFacePositionConsumer.h"
#include "IAsyncFatalErrorHandler.h"

/// @brief This is the main class for component TestFaceTrackerSpark.
///
/// Test for FaceTracker spark

class TestFaceTrackerSpark :
	public Component,
	public IDetectedFacePositionConsumer,
	public IApplication,
	public IAsyncFatalErrorHandler
{
public:
	TestFaceTrackerSpark(
		char *instanceName, 
		ComponentSystem *cs
	) : Component(instanceName, cs)
	{}

private:

	void initializeRequiredInterfaces() {	
	}

public:

	void init(void);
	void quit(void);

	//IDetectedFacePositionConsumer implementation
	void consumeDetectedFacePosition(bool isFaceDetected, double x, double y);
	//Application implementation
	void run(void);
	//IThreadProc implementation
	void handleError(char*);

private:
	bool isStreamBroken;


};


#endif
