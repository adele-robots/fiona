#pragma once
//PABLO: Scene
#include "IScene.h"

class Eye;
class ISkeleton
{
public:
	ISkeleton(psisban::Config *c) : bodyConfiguration(c) {}
	virtual void rotateJoint(
		char *jointName, 
		float x, 
		float y, 
		float z
	)const=0;
	
	virtual void init(void);
	virtual void rotateHead(float pan, float tilt);
	virtual void getHeadPos(float *pPan, float *pTilt);
	virtual void setHeadPos(float pan, float tilt);

	virtual void armsDown(void)const=0;
	virtual void rotateEye(Eye *eye, float pan, float tilt)const=0;

public:
	bool hasEyeBones;
	Eye *leftEye;
	Eye *rightEye;
	float headPanOffset;
	float headTiltOffset;
//PABLO: Skeleton
//private:
protected:
	float pan;
	float tilt;
	psisban::Config *bodyConfiguration;
};


