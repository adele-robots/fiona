/// @file CameraControlSpark.cpp
/// @brief CameraControlSpark class implementation.

// Third party libraries are linked explicitly once in the project.
// #pragma comment(lib, "thirdPartyLib.lib")

#include "stdAfx.h"
#include "CameraControlSpark.h"
#include "math.h"

#include "stdio.h"

#define PI 3.1416

#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "CameraControlSpark")) {
			return new CameraControlSpark(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif

/// Initializes EyeContactComponent component.

void CameraControlSpark::init(void)
{

	//Set Camera Parameters
	myCamera->setCameraParameters(
				getComponentConfiguration()->getBool(const_cast<char *>("Scene_Camera_IsOrtho")),
				getComponentConfiguration()->getFloat(const_cast<char *>("Scene_Camera_CameraParameters_VisionAngle")),
				getComponentConfiguration()->getFloat(const_cast<char *>("Scene_Camera_CameraParameters_NearClippingPlane")),
				getComponentConfiguration()->getFloat(const_cast<char *>("Scene_Camera_CameraParameters_FarClippingPlane"))
				);

	//Load Camera Position

	xPosCam=getComponentConfiguration()->getFloat(const_cast<char *>("Scene_Camera_Position_X"));
	yPosCam=getComponentConfiguration()->getFloat(const_cast<char *>("Scene_Camera_Position_Y"));
	zPosCam=getComponentConfiguration()->getFloat(const_cast<char *>("Scene_Camera_Position_Z"));

	//Load Camera Rotation

	xRotCam=getComponentConfiguration()->getFloat(const_cast<char *>("Scene_Camera_Rotation_X"));
	yRotCam=getComponentConfiguration()->getFloat(const_cast<char *>("Scene_Camera_Rotation_Y"));
	zRotCam=getComponentConfiguration()->getFloat(const_cast<char *>("Scene_Camera_Rotation_Z"));

	//Choose Camera movements
	dynamicCamera=getComponentConfiguration()->getBool(const_cast<char *>("Scene_Camera_Dynamic"));

	//Speed of movements
	speed=getComponentConfiguration()->getFloat(const_cast<char *>("Scene_Camera_Speed"));

	//Choose Circle or Diamond Algorithm
	circle=getComponentConfiguration()->getBool(const_cast<char *>("Scene_Camera_Circle"));

	//Radio of the circle
	r=0;

	myFrameEventPublisher->addFrameEventSubscriber(this);

}
/// Unitializes the EyeContactComponent component.

void CameraControlSpark::quit(void) {
}


void CameraControlSpark::notifyFrameEvent(void)
{
	calculateTrayectory();
}


void CameraControlSpark::calculateTrayectory(void)
{
	if (dynamicCamera)
		{
			if (!circle)
			{
				//Diamond Algorithm
				if (yRotCam>=0 && yRotCam <=90)
				{
					xPosCam = xPosCam + speed ;
					zPosCam = zPosCam - speed;
					yRotCam = yRotCam + speed;
				}

				else if (yRotCam>90 && yRotCam<=180)
				{
					xPosCam = xPosCam - speed;
					zPosCam = zPosCam - speed;
					yRotCam = yRotCam + speed;
				}

				else if (yRotCam>180 &&  yRotCam<=270)
				{
					xPosCam = xPosCam - speed;
					zPosCam = zPosCam + speed;
					yRotCam = yRotCam + speed;
				}

				else if (yRotCam > 270 && yRotCam <=360)
				{
					xPosCam = xPosCam + speed;
					zPosCam = zPosCam + speed;
					yRotCam = yRotCam + speed;
				}

				//full circle
				if (yRotCam > 360)
				{
					xPosCam=getComponentConfiguration()->getFloat(const_cast<char *>("Scene_Camera_Position_X"));
					yPosCam=getComponentConfiguration()->getFloat(const_cast<char *>("Scene_Camera_Position_Y"));
					yRotCam=getComponentConfiguration()->getFloat(const_cast<char *>("Scene_Camera_Rotation_Y"));
				}

				myCamera->setCameraPosition(xPosCam,yPosCam,zPosCam);
				myCamera->setCameraRotation(xRotCam,yRotCam,zRotCam);
			}

			else
			{
				//Circle Algorithm

				r=sqrt(xPosCam*xPosCam+zPosCam*zPosCam);

				xPosCam=r*sin(yRotCam*PI/180);
				zPosCam=r*cos(yRotCam*PI/180);
				yRotCam=yRotCam+speed;

				//Parameteres to change
				myCamera->setCameraPosition(xPosCam,yPosCam,zPosCam);
				myCamera->setCameraRotation(xRotCam,yRotCam,zRotCam);
			}

	    }
	    else
	    {
	    	//Position chosen by the user
	    	myCamera->setCameraPosition(xPosCam,yPosCam,zPosCam);
	    	myCamera->setCameraRotation(xRotCam,yRotCam,zRotCam);
	    }
}
