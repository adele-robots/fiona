/*
 * CharacterAnimatorSpark.h
 *
 *  Created on: 29/07/2013
 *      Author: guille
 */

/// @file CharacterAnimatorSpark.h
/// @brief Component CharacterAnimatorSpark main class.


#ifndef __CharacterAnimatorSpark_H
#define __CharacterAnimatorSpark_H


#include "Component.h"

#include "IJoint.h"
#include "IFlow.h"
#include "FrameEventSubscriber.h"
#include "IFrameEventPublisher.h"
#include "IAnimation.h"

#include <assimp/Importer.hpp>      // C++ importer interface
#include <assimp/scene.h>           // Output data structure
#include <assimp/postprocess.h>     // Post processing flags

#include <stdexcept>      // std::out_of_range

#include "utMath.h"
#include "StopWatch.h"
#include "AnimationFile.h"
#include "PerlinNoise.h"

#define ANIMATION_FILE_EXTENSION ".bvh"
#define ANIMATION_REMOVE_PREFIX "[REMOVE]"
#define DIRECT 'D'
#define REVERSE 'I'
#define YES 'Y'
#define NO 'N'

struct Animation{
	char* 				animName;
	int 				numFrames;
	float 				timeForFrame;
	float	 			intervalTime;
	int					frameToPlay;
	aiAnimation**		animationPtr;
	Assimp::Importer*	importer;
};

/// @brief This is the main class for component CharacterAnimatorSpark.
///
/// 

class CharacterAnimatorSpark :
	public Component,

	public FrameEventSubscriber,
	public IFlow<char*>
{
public:
		CharacterAnimatorSpark(
				char *instanceName,
				ComponentSystem *cs
		) : Component(instanceName, cs)
		{}

		~CharacterAnimatorSpark(){};

private:
	IFrameEventPublisher *myFrameEventPublisher;
	IJoint *myJoint;

	void initializeRequiredInterfaces() {	
		requestRequiredInterface<IJoint>(&myJoint);
		requestRequiredInterface<IFrameEventPublisher>(&myFrameEventPublisher);
	}

public:
	//Mandatory
	void init(void);
	void quit(void);
	
	//FrameEventSubscriber
	void notifyFrameEvent();
	
	//IFlow<char*>
	void processData(char* prompt);

private:
	unsigned int numChannels;

	StopWatch timeCounter;
	StopWatch pauseTimeCounter;
	float elapsedTime;

	string userWorkingDir;

	float rootNodeOffsetX;
	float rootNodeOffsetY;
	float rootNodeOffsetZ;

	vector<Animation> animationsArray;
	bool animationCompleted;
	vector<int> animationsToRemove;

	string state;
	string nextState;
	string pose;
	char direction;
	float speed;
	float pauseTime;
	char randomHeadMovement;
	PerlinNoise pn;
	string neck;

	vector<string> validStates;
private:
	void playAnimation(char *animationName);
	int searchForAnimation(const char* animName);
	float randomFloat(float a, float b);
	int pickPose(vector<AnimationFile> & vec);
};

#endif
