/// @file EyeContactSpark.h
/// @brief Component EyeContactSpark main class.


#ifndef __EyeContactSpark_H
#define __EyeContactSpark_H

#include "Component.h"
#include "FrameEventSubscriber.h"

#include "IDetectedFacePositionConsumer.h"

#include "IEyes.h"
#include "INeck.h"
#include "IFrameEventPublisher.h"

#include "Perlin.h"
#include "StopWatch.h"

#define LOW_PASS_FILTER

/// @brief This is the main class for component EyeContactSpark.
///
/// Transforms (x,y) face position into (pan,tilt) to move eyes and neck

class I2DFilter;

class EyeContactSpark :
	public Component,

	public IDetectedFacePositionConsumer,		
	public FrameEventSubscriber

{
public:
		EyeContactSpark(
		char *instanceName, 
		ComponentSystem *cs
	) : Component(instanceName, cs)
	{}

private:
	IEyes *myEyes;
	INeck *myNeck;
	IFrameEventPublisher *myFrameEventPublisher;

	void initializeRequiredInterfaces() {	
		requestRequiredInterface<IEyes>(&myEyes);
		requestRequiredInterface<INeck>(&myNeck);
		requestRequiredInterface<IFrameEventPublisher>(&myFrameEventPublisher);
	}

public:

	void init(void);
	void quit(void);

	//IDetectedFacePositionConsumer implementation
	void consumeDetectedFacePosition(bool isFaceDetected, double x, double y);

	//IFrameEventPublisher implementation
	void notifyFrameEvent();


private:
	float faceTrackedHorizontalPos;
	float faceTrackedVerticalPos;
	bool isFaceDetected;

	Perlin *m_pPerlinX;
	Perlin *m_pPerlinY;
	Perlin *m_pPerlinZ;

	//we need to use the current pan and the current tilt
	//so every call to rotateNeck(pan,tilt) we save them in currentPan and currentTilt
	float currentPan;
	float currentTilt;

	float headPanOffset;
	float headTiltOffset;
	float hasEyeBones;

	float presenceLevel;

	psisban::Config bodyConfiguration;
private:
	void updateHeadRandomMovements(float *pPan, float *pTilt);
	void updateEyeContactHeadPos(float *pPan, float *pTilt);
	void updateNonVisemeHeadMorphTargets(void);
	void updateHeadIdleMovements(void);

#ifdef LOW_PASS_FILTER
	I2DFilter *neck2dFilter;
#endif
};

const double PI  =3.141592653589793238462;

class I2DFilter {
public:	
	I2DFilter() { state_x = 0; state_y = 0; }
	virtual void filter(float&, float&) = 0;

public:
	float state_x, state_y;
};

class LowPass : public I2DFilter {

public:

	// calculate alpha (wikipead:Low-pass_filter)
	LowPass(float sampleFrequency, float cutoffFrequency) : I2DFilter() {
		float samplingPeriod = 1 / sampleFrequency;
		
		float rc = 1 / (2 * PI * cutoffFrequency);

		alpha = samplingPeriod / (rc + samplingPeriod);
	}
	
	void filter(float &x, float &y) {
		x = state_x + alpha * (x - state_x); state_x = x;
		y = state_y + alpha * (y - state_y); state_y = y;
	}

private:
	float alpha;
};

#endif


