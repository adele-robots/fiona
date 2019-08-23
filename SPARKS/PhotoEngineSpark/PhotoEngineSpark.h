/*
 * PhotoEngineSpark.h
 *
 *  Created on: 12/11/2013
 *      Author: guille
 */

/// @file PhotoEngineSpark.h
/// @brief Component PhotoEngineSpark main class.


#ifndef __PhotoEngineSpark_H
#define __PhotoEngineSpark_H

#ifndef REMOTERENDERER_INDEPENDENT
#define REMOTERENDERER_INDEPENDENT
#endif


#include "Component.h"

#include "IFlow.h"
#include "IRenderizable.h"

#include <algorithm>
#include <iterator>
#include <stdexcept>

#include <opencv2/opencv.hpp>

/// @brief This is the main class for component PhotoEngineSpark.
///
/// 

class PhotoEngineSpark :
	public Component,

	public IRenderizable,
	public IFlow<char*>
{
public:
		PhotoEngineSpark(
				char *instanceName,
				ComponentSystem *cs
		) : Component(instanceName, cs)
		{}
		virtual ~PhotoEngineSpark(){};

private:

	void initializeRequiredInterfaces() {	

	}

public:
	//Mandatory
	void init(void);
	void quit(void);
	
	//IFlow implementation
	void processData(char *);

	//IRenderizable implementation
	void* render(void);
	void unMapResourceStream(void);

	
private:
	string mousePosX;
	string mousePosY;

	int numFrames;
	float frameTrackStep;
private:
	//Put class private methods here
	
};

#endif
