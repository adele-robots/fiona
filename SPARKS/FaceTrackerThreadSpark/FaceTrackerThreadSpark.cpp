/// @file FaceTrackerThreadSpark.cpp
/// @brief FaceTrackerThreadSpark class implementation.

// Third party libraries are linked explicitly once in the project.
// #pragma comment(lib, "thirdPartyLib.lib")

#include "stdAfx.h"
#include "FaceTrackerThreadSpark.h"

#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "FaceTrackerThreadSpark")) {
			return new FaceTrackerThreadSpark(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif

/// Initializes FaceTrackerThreadSpark component.
void FaceTrackerThreadSpark::init(void){
	//2-configurations
	myFaceTrackerComponent->componentConfiguration = componentConfiguration;
	myThreadComponent->componentConfiguration = componentConfiguration;

	//3-connections
	//required interfaces of FaceTrackerSpark
	myFaceTrackerComponent->myDetectedFacePositionConsumer = myDetectedFacePositionConsumer;

	//required interfaces of ThreadComponent
	myThreadComponent->myThreadProc = myFaceTrackerComponent;
	myThreadComponent->myAsyncFatalErrorHandler = myAsyncFatalErrorHandler;

	//4-initializations
	myFaceTrackerComponent->init();
	myThreadComponent->init();
}

/// Unitializes the FaceTrackerThreadSpark component.
void FaceTrackerThreadSpark::quit(void){
	myFaceTrackerComponent->init();
	myThreadComponent->quit();
}

//IConcurrent implementation
void FaceTrackerThreadSpark::start(void){
	myThreadComponent->start();
}

void FaceTrackerThreadSpark::stop(void){
	myThreadComponent->stop();
}

//IFlow implementation
void FaceTrackerThreadSpark::processData(Image * im){
	myFaceTrackerComponent->processData(im);
}




