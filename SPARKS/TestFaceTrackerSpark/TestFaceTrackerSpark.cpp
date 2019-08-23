/// @file TestFaceTrackerSpark.cpp
/// @brief TestFaceTrackerSpark class implementation.

// Third party libraries are linked explicitly once in the project.
// #pragma comment(lib, "thirdPartyLib.lib")

#include "stdAfx.h"
#include <iostream>
#include <opencv2/opencv.hpp>
#include "TestFaceTrackerSpark.h"

#ifdef _WIN32
#include <windows.h>
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "TestFaceTrackerSpark")) {
			return new TestFaceTrackerSpark(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}

#endif

/// Initializes TestFaceTrackerSpark component.

void TestFaceTrackerSpark::init(void)
{
	isStreamBroken = false;
}


/// Unitializes the TestFaceTrackerSpark component.

void TestFaceTrackerSpark::quit(void) {
}

//IDetectedFacePositionConsumer implementation
void TestFaceTrackerSpark::consumeDetectedFacePosition(bool isFaceDetected,double x,double y)
{while(1);
	// LoggerInfo("Face detected | x | y %d, %f, %f", isFaceDetected, x, y);
	LoggerInfo("isFaceDetected: %s, x: %f, y: %f", isFaceDetected ? "yes" : "no" , x, y);
}

//Application implementation
void TestFaceTrackerSpark::run(void)
{
	LoggerInfo("Press any key in the window to exit...");

	do {
		sleep(100);
		if (isStreamBroken) break;
	} 
	while (-1 == cvWaitKey(1) && !isStreamBroken);

}

// IAsyncFatalErrorHandler implementation
void TestFaceTrackerSpark::handleError(char *msg)
{
		LoggerError(msg);
		isStreamBroken = true;
}
