/// @file MotionController.h
/// @brief MotionController class definition.

#ifndef __MOTION_CONTROLLER_H
#define __MOTION_CONTROLLER_H


#include "Perlin.h"
//PABLO: Scene
#include "IScene.h"
#include "LipSynch.h"
//PABLO: MTB MODIFIED
#include "IMorphTargetBlender.h"
#include "MotionController.h"
#include "Thread.h"
//DAVID
#include "MecaTronicaSerial.h"

/// \brief Regulated control of character joints and morph target values.
///
/// Controller of the kinematic actions of the virtual character. Launches primitives
/// at the level of joint rotations and morph target weight setting in function of
/// regulators following face detection results.

class MotionController {
public:
	float faceTrackedHorizontalPos;
	float faceTrackedVerticalPos;
	bool isFaceDetected;


public:
	#if defined(PABLO_2D)
	void initController(
		//PABLO: Scene
		IScene *scene, 
		//PABLO: MTB MODIFIED
		IMorphTargetBlender *blender, 
		LipSynch *lipSynch,
		psisban::Config *bodyConfig,
		//DAVID
		MecaTronicaSerial *mecaTronicaSerial
	);
#else
	void initController(
		//PABLO: Scene
		IScene *scene, 
		//PABLO: MTB MODIFIED
		IMorphTargetBlender *blender, 
		LipSynch *lipSynch,
		psisban::Config *bodyConfig
	);
#endif
	void updateAvatarSpeaking(void);
	void updateAvatarListening(void);
	void updateAvatar(void);
	//PABLO: Scene
	IScene *scene;
	//DAVID
	MecaTronicaSerial *mecaTronicaSerial;

private:
	//PABLO: MTB MODIFIED
	IMorphTargetBlender *blender;
	LipSynch *lipSynch;
	StopWatch stopWatch;

	Perlin *m_pPerlinX;
	Perlin *m_pPerlinY;
	Perlin *m_pPerlinZ;
	unsigned int nextBlinkTime; 


private:
	void updatePresenceLevel(float *pPresenceLevel);
	void updateEyeContactHeadPos(float *pPan, float *pTilt);
	void updateHeadRandomMovements(float *pPan, float *pTilt);
	void updateHeadIdleMovements(void);
	void updateNonVisemeHeadMorphTargets(void);
};

#endif
