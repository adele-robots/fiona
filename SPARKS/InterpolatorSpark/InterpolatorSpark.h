/*
 * InterpolatorSpark.h
 *
 *  Created on: 28/11/2013
 *      Author: guille
 */

/// @file InterpolatorSpark.h
/// @brief Component InterpolatorSpark main class.


#ifndef __InterpolatorSpark_H
#define __InterpolatorSpark_H

#include "simplexnoise.h"
#include "Component.h"

#include "IFlow.h"

#include <algorithm>
#include <iterator>
#include <stdexcept>

/// @brief This is the main class for component InterpolatorSpark.
///
/// 

class InterpolatorSpark :
	public Component,

	public IFlow<char*>
{
public:
		InterpolatorSpark(
				char *instanceName,
				ComponentSystem *cs
		) : Component(instanceName, cs)
		{}
		virtual ~InterpolatorSpark() {};

private:
	IFlow<char *> *myFlow;

	void initializeRequiredInterfaces() {	
		requestRequiredInterface<IFlow<char *> >(&myFlow);
	}

public:
	//Mandatory
	void init(void);
	void quit(void);
	
	//IFlow implementation
	void processData(char *);
	
private:
	//Array to hold mouse info
	vector<string> mouseInfo;

	int mouseTargetPosX, mouseTargetPosY;
	int mouseCurrentPosX, mouseCurrentPosY;
	int mouseIncrement;

	//Noise variables
	int noiseTarget;
	int currentNoise;
	int noiseFactor;
	int offsetIncrement;
	int numScales;
	float persistence;
	int loBound, hiBound;

	long double xoff;

	int playerWidth;
	int playerHeight;

private:
	void updateNoise(void);
	void updateMouse(void);
	
};

#endif
