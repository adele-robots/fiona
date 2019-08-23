/// @file CameraControlSpark.cpp
/// @brief Component CameraControlSpark main class.


#ifndef __CameraControlSpark_H
#define __CameraContorlSpark_H

#include "Component.h"
#include "FrameEventSubscriber.h"

#include "ICamera.h"
#include "IFrameEventPublisher.h"

class CameraControlSpark :
	public Component,
	public FrameEventSubscriber
{
public:
	CameraControlSpark(
		char *instanceName, 
		ComponentSystem *cs
	) : Component(instanceName, cs)
	{}

private:

	ICamera *myCamera;
	IFrameEventPublisher *myFrameEventPublisher;

	void initializeRequiredInterfaces() {	
		requestRequiredInterface<ICamera>(&myCamera);
		requestRequiredInterface<IFrameEventPublisher>(&myFrameEventPublisher);
	}

public:

	void init(void);
	void quit(void);

	void notifyFrameEvent();

private:

	//Position
	float xPosCam;
	float yPosCam;
	float zPosCam;
	//Rotation
	float xRotCam;
	float yRotCam;
	float zRotCam;
	//Static or dynamic camera
	bool dynamicCamera;
	//Radio
	float r;
	//Speed
	float speed;
	//Circle or diamond algorithm
	bool circle;

	void calculateTrayectory(void);

};

#endif
