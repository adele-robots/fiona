#pragma once
//PABLO: Scene
#include "IScene.h"
#include "ISkeleton.h"
class Eye;
class Skeleton2D : public ISkeleton
{
public:
	Skeleton2D(psisban::Config *c) : ISkeleton(c) {}
	virtual void rotateJoint(
		char *jointName, 
		float x, 
		float y, 
		float z
		)const;

	virtual void init(void);
	virtual void rotateHead(float pan, float tilt);
	virtual void getHeadPos(float *pPan, float *pTilt);
	virtual void setHeadPos(float pan, float tilt);

	virtual void armsDown(void)const;
	virtual void rotateEye(Eye *eye, float pan, float tilt)const;

};


