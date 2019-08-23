/*
 * AnimationManagerSpark.h
 *
 *  Created on: 06/09/2013
 *      Author: guille
 */

/// @file AnimationManagerSpark.h
/// @brief Component AnimationManagerSpark main class.


#ifndef __AnimationManagerSpark_H
#define __AnimationManagerSpark_H

#include <ctime>
#include <cstdlib>
//#include <unistd.h>
#include <csignal>


#include "Component.h"
#include "IFlow.h"
#include "IThreadProc.h"
#include "FrameEventSubscriber.h"
#include "IJoint.h"
#include "IAnimation.h"
#include "IFaceExpression.h"

#include "StopWatch.h"

#define OPENMOUTH_ITERATIONS 20
#define HAPPY_ITERATIONS 20

/// @brief This is the main class for component AnimationManagerSpark.
///
/// 

class AnimationManagerSpark :
	public Component,
	public IFlow<char*>,
	public FrameEventSubscriber,
	public IThreadProc
{
public:
		AnimationManagerSpark(
				char *instanceName,
				ComponentSystem *cs
		) : Component(instanceName, cs), isHappy(false), isSad(false), openMouth(false), isSurprised(false), round(0)
		{}
		virtual ~AnimationManagerSpark(){}


	void initializeRequiredInterfaces() {	
		requestRequiredInterface<IFlow<char *> >(&myFlow);
		requestRequiredInterface<IJoint>(&myJoint);
		requestRequiredInterface<IAnimation>(&myAnimation);
		requestRequiredInterface<IFaceExpression>(&myFaceExpression);
	}
	IFlow<char *> *myFlow;
	IJoint *myJoint;
	IAnimation *myAnimation;
	IFaceExpression *myFaceExpression;

public:
	//Mandatory
	void init(void);
	void quit(void);

	//IFlow<char*> implementation
	void processData(char * prompt);

	// Implementation of IThreadproc
	void process();

	//FrameEventSubscriber
	void notifyFrameEvent();

private:
	//Ojo que si utilizo aquí un 'StopWatch' tengo que linkarlo en AnimationManagerThreadSpark
	// ya que en el constructor de este crea una instancia con 'new' de AnimationManagerSpark y
	// empieza a crear los miembros declarados, y al ejecutar el constructor del StopWatch peta
//	StopWatch timeCounter;
//	StopWatch timeCounterBlink;
	float elapsedTime;
	int randomBlinkTime;

	float noiseRotX;
	float noiseRotY;
	float noiseRotZ;

	float noiseNeckRotX;


	float noiseEyeRotX;
	float noiseEyeRotY;

	float noiseRShoulderRotX;
	float noiseRShoulderRotY;
	float noiseRShoulderRotZ;

	float noiseLShoulderRotX;
	float noiseLShoulderRotY;
	float noiseLShoulderRotZ;

	float noiseRForeArmRotX;
	float noiseRForeArmRotY;
	float noiseRForeArmRotZ;

	float noiseLForeArmRotX;
	float noiseLForeArmRotY;
	float noiseLForeArmRotZ;

	// Set up your range fade function variables here.
	float mini;
	float maxi;
	//float step = 0.1;
	float curr;
	float step;

	float maxNostrilsFlare;
	float currentNostrilsFlare;
	float stepNostrilsFlare;

	float maxOpenMouth;
	float currentOpenMouth;
	float stepOpenMouth;

	float maxSad;
	float currentSad;
	float stepSad;

	float currentSurprised;

	bool isHappy;
	bool expressionEnded;
	int happyFreezes;

	bool isSad;
	bool sadExpressionEnded;
	int sadFreezes;

	bool isSurprised;
	bool surprisedEnded;
	int surprisedFreezes;

	bool openMouth;
	bool openMouthExpressionEnded;
	int openMouthFreezes;

	int round;
	//timer_t firstTimerID;

private:
	void blink();
	float nextVal (float curr, float min, float max, float *pStep);
	static void timerHandler( int sig, siginfo_t *si, void *uc );
	static int makeTimer( char *name, timer_t *timerID, int expireMS, int intervalMS );
};

#endif
