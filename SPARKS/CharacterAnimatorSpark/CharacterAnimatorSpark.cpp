/*
 * CharacterAnimatorSpark.cpp
 *
 *  Created on: 29/07/2013
 *      Author: guille
 */

/// @file CharacterAnimatorSpark.cpp
/// @brief CharacterAnimatorSpark class implementation.


//#include "stdAfx.h"
#include "CharacterAnimatorSpark.h"
#include <fstream>
#include "simplexnoise.h"
#include "time.h"


#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "CharacterAnimatorSpark")) {
			return new CharacterAnimatorSpark(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif

float xRot = 0.0, yRot = 0.0, zRot = 0.0;
float xoff = 0.0;
float xincrement = 0.1;
float yoff = 0.0;
float yincrement = 0.1;
float zoff = 0.0;
float zincrement = 0.1;

/// Initializes CharacterAnimatorSpark component.
void CharacterAnimatorSpark::init(void){
	// When a frame is about to be updated by the 3D engine, 'notifyFrameEvent()' will be called
	myFrameEventPublisher->addFrameEventSubscriber(this);

	// Load a sample file to extract some model useful information
	userWorkingDir = getComponentConfiguration()->getString(const_cast<char*>("User_Spark_Data"));
	string pFile = userWorkingDir + getComponentConfiguration()->getString(const_cast<char*>("Sample_Filename")) + ANIMATION_FILE_EXTENSION;

	Assimp::Importer importer;
	// Read the given file
	const aiScene* scene = importer.ReadFile( pFile, 0);

	// If the import failed, report it
	if( !scene){
		LoggerError("[FIONA-logger] Something went wrong importing asset: %s", importer.GetErrorString());
		ERR("[FIONA-logger] Something went wrong importing asset: %s", importer.GetErrorString());
	}else{
		aiAnimation ** animations;
		// Hold animations double-pointer to ease the access
		animations = scene->mAnimations;

		LoggerInfo("Animation file loaded! 3D model used has %d nodes/bones",animations[0]->mNumChannels);

		aiVector3t<float> position;
		aiQuaterniont<float> rotation;
		// Get the rootnode offset
		scene->mRootNode->mTransformation.DecomposeNoScaling(rotation,position);
		rootNodeOffsetX = position.x;
		rootNodeOffsetY = position.y;
		rootNodeOffsetZ = position.z;

		// Keep the animation frame number, frame time-width and number of channels (one per bone)
		numChannels = animations[0]->mNumChannels;

		animationCompleted = false;
	}

	state = getComponentConfiguration()->getString(const_cast<char*>("Initial_State"));
	nextState = state;
	pose = getComponentConfiguration()->getString(const_cast<char*>("Initial_Pose"));
	direction = DIRECT;
	speed = 1.0;
	pauseTime = 0.0;
	randomHeadMovement = NO;
	neck = getComponentConfiguration()->getString(const_cast<char*>("Neck_Joint_Name"));

	validStates.clear();
	int statesNumber = getComponentConfiguration()->getLength(const_cast<char *>("States"));
	for (int statesIndex = 0; statesIndex < statesNumber; statesIndex++) {
		char setting[80];
		snprintf(setting, 80, "States.[%d]", statesIndex);
		validStates.push_back(getComponentConfiguration()->getString(setting));
	}
}

/// Unitializes the CharacterAnimatorSpark component.
void CharacterAnimatorSpark::quit(void){
}

// IFlow<char*> implementation
void CharacterAnimatorSpark::processData(char * prompt) {

	char removedChars[] = "<>";

	std::string promptString(prompt);

	string::size_type firstToken = promptString.find("<");
	string::size_type lastToken = promptString.find(">", firstToken);
	if(firstToken == string::npos || lastToken == string::npos)
		return;
	std::string tagString = promptString.substr(firstToken, lastToken-firstToken);

	for (unsigned int i = 0; i < strlen(removedChars); ++i)
	{
		tagString.erase(std::remove(tagString.begin(), tagString.end(), removedChars[i]), tagString.end());
	}

	for(vector<string>::iterator it = validStates.begin(); it != validStates.end(); it++) {
		if(*it == tagString) {
			nextState = tagString;
			break;
		}
	}
}

void CharacterAnimatorSpark::playAnimation(char *animationName){
	// Check if animation is going to be added or removed
	if(strncmp(animationName, ANIMATION_REMOVE_PREFIX, strlen(ANIMATION_REMOVE_PREFIX)) == 0){
		// Extract the animation name to be removed
		string animationNameStr(animationName);
		string animationToRemove = animationNameStr.substr(strlen(ANIMATION_REMOVE_PREFIX),animationNameStr.size());

		// Search for its position within the animations array
		int animationPos = searchForAnimation(animationToRemove.c_str());
		if(animationPos != -1){
			animationCompleted = true;
			animationsToRemove.push_back(animationPos);
		}else{
			LoggerError("Animation '%s' does not exist or it is not being played at the moment",animationToRemove.c_str());
		}
	}else{
		// Set the animation filename
		string animationFileName = userWorkingDir + animationName + ANIMATION_FILE_EXTENSION;

		// It is very important to allocate dynamic memory for the importer
		Assimp::Importer* importer = new Assimp::Importer();
		const aiScene* scene = importer->ReadFile( animationFileName, 0);
		// If the import failed, report it
		if( !scene){
			LoggerError("[FIONA-logger] Something went wrong importing asset: %s", importer->GetErrorString());
			ERR("[FIONA-logger] Something went wrong importing asset: %s", importer->GetErrorString());
		}else{
			// Build the animation structure and add to the animations array to be played
			Animation a1;
			a1.animName = animationName;
			a1.numFrames = scene->mAnimations[0]->mDuration;
			a1.timeForFrame = speed*(1/scene->mAnimations[0]->mTicksPerSecond);
			a1.frameToPlay = direction == DIRECT ? 0 : a1.numFrames;
			a1.intervalTime = 0.0;
			a1.animationPtr = scene->mAnimations;
			a1.importer = importer;

			animationsArray.push_back(a1);
		}
	}
}

void CharacterAnimatorSpark::notifyFrameEvent()
{
	static bool firsTime = true;
	if (firsTime){
		firsTime = false;
		timeCounter.restart();
	}
	elapsedTime = timeCounter.elapsedTime();
	timeCounter.restart();

	if(animationsArray.size() >= 1){
		for(unsigned int i = 0; i<numChannels; i++ ){
			// Declare identity matrix to hold animation matrix product
			Horde3D::Matrix4f qMatFinal;
			// Declare variables to hold root node translation sum
			float rootPosX = 0.0, rootPosY = 0.0, rootPosZ = 0.0;
			for(unsigned int j = 0; j<animationsArray.size(); j++){
				// Only calculate frameToSet in the first 'node/bone' of the animation
				if(i==0){
					animationsArray[j].intervalTime += elapsedTime;
					int frameToSet = animationsArray[j].intervalTime/animationsArray[j].timeForFrame;
					if(frameToSet > 0){
						animationsArray[j].intervalTime = 0.0;
						if(direction == DIRECT)
							animationsArray[j].frameToPlay += frameToSet;
						else
							animationsArray[j].frameToPlay -= frameToSet;
					}
				}
				// Check if the animation is not completed
				if(		(direction == DIRECT  && animationsArray[j].frameToPlay <= animationsArray[j].numFrames) ||
						(direction == REVERSE && animationsArray[j].frameToPlay >= 0)){
					Horde3D::Quaternion quat(animationsArray[j].animationPtr[0]->mChannels[i]->mRotationKeys[animationsArray[j].frameToPlay].mValue.x,
							animationsArray[j].animationPtr[0]->mChannels[i]->mRotationKeys[animationsArray[j].frameToPlay].mValue.y,
							animationsArray[j].animationPtr[0]->mChannels[i]->mRotationKeys[animationsArray[j].frameToPlay].mValue.z,
							animationsArray[j].animationPtr[0]->mChannels[i]->mRotationKeys[animationsArray[j].frameToPlay].mValue.w);
					Horde3D::Matrix4f qMat(quat);
					// For each node, sum all of the animations rotation matrix
					qMatFinal = qMatFinal * qMat;
					if(i==0){
						rootPosX += animationsArray[j].animationPtr[0]->mChannels[i]->mPositionKeys[animationsArray[j].frameToPlay].mValue.x;
						rootPosY += animationsArray[j].animationPtr[0]->mChannels[i]->mPositionKeys[animationsArray[j].frameToPlay].mValue.y;
						rootPosZ += animationsArray[j].animationPtr[0]->mChannels[i]->mPositionKeys[animationsArray[j].frameToPlay].mValue.z;
					}
				}else{
					animationCompleted = true;
					animationsToRemove.push_back(j);
				}
			}
			if(animationCompleted){
				//LoggerInfo("animation completed");
				pauseTimeCounter.restart();
				animationCompleted = false;
				while(!animationsToRemove.empty()){
					// Free previously reserved dynamic memory and remove the animation from the array
					delete(animationsArray[animationsToRemove.back()].importer);
					animationsArray.erase(animationsArray.begin()+animationsToRemove.back());
					animationsToRemove.pop_back();
				}
			}
			// Double-check array size, in case an animation was deleted within the inside loop and emptied the array
			if(animationsArray.size() >= 1){
				aiNodeAnim* a = animationsArray[0].animationPtr[0]->mChannels[i];
				// Translation applied to the transformation matrix is different for the root node (usually called 'hip')
				if(i==0){
					qMatFinal.translate(rootPosX + rootNodeOffsetX, rootPosY + rootNodeOffsetY, rootPosZ + rootNodeOffsetZ);
				}else{
					// For the neck node
					if(!strcmp(neck.c_str(), a->mNodeName.C_Str())){

						// ROTACION
						float alphaRot = 0.1;
						float noiseNeckRotX = (a->mRotationKeys[0].mValue.x * alphaRot + xRot) / (1 + alphaRot);
						float noiseNeckRotY = (a->mRotationKeys[0].mValue.y * alphaRot + yRot) / (1 + alphaRot);
						float noiseNeckRotZ = (a->mRotationKeys[0].mValue.z * alphaRot + zRot) / (1 + alphaRot);
						xRot = noiseNeckRotX;
						yRot = noiseNeckRotY;
						zRot = noiseNeckRotZ;
						myJoint->rotateJointPart(const_cast<char *>(neck.c_str()),noiseNeckRotX,noiseNeckRotY,noiseNeckRotZ);

						// TRANSLACION
						float alphaTrans = 0.1;
						float posX, posY, posZ;
						myJoint->GetJointPosition(const_cast<char *>(neck.c_str()),&posX,&posY,&posZ);
						float noiseNeckTraX = (a->mPositionKeys[0].mValue.x * alphaTrans + posX) / (1 + alphaTrans);
						float noiseNeckTraY = (a->mPositionKeys[0].mValue.y * alphaTrans + posY) / (1 + alphaTrans);
						float noiseNeckTraZ = (a->mPositionKeys[0].mValue.z * alphaTrans + posZ) / (1 + alphaTrans);
						myJoint->moveJointPart(const_cast<char *>(neck.c_str()),noiseNeckTraX,noiseNeckTraY,noiseNeckTraZ);

						// Avoid setJointTransMat
						continue;
					}
					//else
					// For the rest of the nodes, only apply a single translation
					qMatFinal.translate(
							a->mPositionKeys[0].mValue.x,
							a->mPositionKeys[0].mValue.y,
							a->mPositionKeys[0].mValue.z);
				}
				myJoint->setJointTransMat(a->mNodeName.C_Str(),qMatFinal.x);
			}else{
				// If it isn't an animation to play, don't loop the rest of the 3D model bones
				break;
			}
		}
	}
	// If there is no animation to play
	else {
		// If the pause time has passed we load a new animation
		if(pauseTimeCounter.elapsedTime() >= pauseTime) {
			/**
			 * waiting.cfg
			 * output_state   | animation    | direction       | speed     | stopMin | stopMax | poseIn | poseOut | probability | randomHeadMovement
			 * -------------------------------------------------------------------------------------------------------------------------------------
			 * waiting        | w1_to_w6     | D               | 1.0       | 0.0     | 1.0     | w1     | w6      | 1.2         | N
			 * waiting        | poses_idle_3 | I               | 1.0       | 0.0     | 1.0     | w3     | w3      | 2.4         | N
			 * talking        | w6_to_talk1  | D               | 1.0       | 1.0     | 2.0     | w6     | t1      | 0.98        | Y
			 */
			string name = getComponentConfiguration()->getString(const_cast<char*>("User_Spark_Data")) + state + string(".cfg");
			fstream f(name.c_str(), ios::in);
			AnimationFile a;
			vector<AnimationFile> vec;
			vec.clear();
			while(f >> a) {
				if(a.getPoseIn() == pose && a.getOutputState() == nextState)
					vec.push_back(a);
			}
			if(vec.size() == 0) {
				LoggerWarn("No animation suites the pose %s in file %s", pose.c_str(), name.c_str());
				return;
			}
			int nextPose = pickPose(vec);
			a = vec[nextPose];
			LoggerInfo("playAnimation %s from %s to %s", a.getAnimation().c_str(), pose.c_str(), a.getPoseOut().c_str());
			pose = a.getPoseOut();
			state = a.getOutputState();
			direction = a.getDirection();
			pauseTime = randomFloat(a.getStopMax(), a.getStopMin());
			speed = a.getSpeed();
			randomHeadMovement = a.getRandomHeadMovement();
			playAnimation(const_cast<char*>(a.getAnimation().c_str()));

			// New random seed
			time_t t = time(0);
			struct tm * now = localtime( & t );
			pn.setSeed(((unsigned int)now->tm_gmtoff)%std::numeric_limits<int>::max());
			xoff = 0.0;
			yoff = 0.0;
			zoff = 0.0;
		}
		// Random head movement

		if(randomHeadMovement == YES) {
			float ms = pauseTime;
			xoff += xincrement;
			yoff += yincrement;
			zoff += zincrement;

			float x = xoff/ms;
			float y = yoff/ms;
			float z = zoff/ms;

			float noiseNeckRotX = pn.noise(x*1, y*1, z*1)*10;
			float noiseNeckRotY = pn.noise(x*2, y*2, z*2)*10;
			float noiseNeckRotZ = pn.noise(x*3, y*3, z*3)*10;
			float actualX, actualY, actualZ;
			myJoint->GetJointRotation(const_cast<char *>(neck.c_str()),&actualX,&actualY,&actualZ);
			float alpha = 0.1;
			noiseNeckRotX = (noiseNeckRotX * alpha + actualX) / (1 + alpha);
			noiseNeckRotY = (noiseNeckRotY * alpha + actualY) / (1 + alpha);
			noiseNeckRotZ = (noiseNeckRotZ * alpha + actualZ) / (1 + alpha);
			xRot = noiseNeckRotX;
			yRot = noiseNeckRotY;
			zRot = noiseNeckRotZ;
			myJoint->rotateJointPart(const_cast<char *>(neck.c_str()),noiseNeckRotX,noiseNeckRotY,noiseNeckRotZ);
		}
	}
}

int CharacterAnimatorSpark::searchForAnimation(const char* animName){
	int animationPos = -1;
	for(unsigned int i=0; i<animationsArray.size(); i++){
		if(strcmp(animationsArray[i].animName, animName) == 0){
			animationPos = i;
			// Can't be two animations with the same name
			break;
		}
	}
	return animationPos;
}

float CharacterAnimatorSpark::randomFloat(float a, float b) {
    float random = ((float) rand()) / (float) RAND_MAX;
    float diff = b - a;
    float r = random * diff;
    return a + r;
}

int CharacterAnimatorSpark::pickPose(vector<AnimationFile> & vec) {
	float sum = 0.0;
	for(uint i = 0; i < vec.size(); ++i) {
		sum += vec[i].getProbability();
	}
	float res = randomFloat(0, sum);
	for(uint i = 0; i < vec.size(); ++i) {
		res -= vec[i].getProbability();
		if(res <= 0)
			return i;
	}
	return 0;
}
