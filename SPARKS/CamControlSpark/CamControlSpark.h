/*
 * CamControlSpark.h
 *
 *  Created on: 02/12/2013
 *      Author: guille
 */

/// @file CamControlSpark.h
/// @brief Component CamControlSpark main class.


#ifndef __CamControlSpark_H
#define __CamControlSpark_H


#include "Component.h"
#include "ICamera.h"
#include "IFlow.h"

#include <algorithm>
#include <iterator>
#include <stdexcept>

/// @brief This is the main class for component CamControlSpark.
///
/// 

class CamControlSpark :
	public Component,

	public IFlow<char*>
{
public:
		CamControlSpark(
				char *instanceName,
				ComponentSystem *cs
		) : Component(instanceName, cs)
		{}
		virtual ~CamControlSpark(){};

private:
	ICamera *myCamera;

	void initializeRequiredInterfaces() {	
		requestRequiredInterface<ICamera>(&myCamera);
	}

public:
	//Mandatory
	void init(void);
	void quit(void);
	
	//IFlow implementation
	void processData(char *);
	
private:
	string mouseCurrentPosX;
	string mouseCurrentPosY;
	int intMouseCurrentPosX;
	int intMouseStartMovePosX;
	int intMouseCurrentPosY;

	//Position
	float xPosCam;
	float yPosCam;
	float zPosCam;
	//Rotation
	float xRotCam;
	float yRotCam;
	float zRotCam;

	bool moveCam;
	bool rotateCam;
private:
	//Put class private methods here
	
};

#endif
