/// @file Skeleton2D.h
/// @brief Skeleton2D class declaration.

#ifndef __SKELETON2D_H
#define __SKELETON2D_H

/*#include "IScene.h"
#include "ISkeleton.h"*/

//Siguiendo criterio 3D
#include "Scene2D.h"
// Siguiendo criterio 3D esto sobra
//:	public ISkeleton
class Eye {

};
class Skeleton2D
{
public:
	//Siguiendo criterio 3D esto se hace de la otra forma
	//Skeleton2D(psisban::Config *c): ISkeleton(c) {}
	Skeleton2D(psisban::Config *c) : bodyConfiguration(c) {}

	void rotateJoint(
		char *jointName, 
		float x, 
		float y, 
		float z
	);
	void init(void);
	void rotateHead(float pan, float tilt);
	void getHeadPos(float *pPan, float *pTilt);
	// Esta sobra siguiendo el criterio 3D
	//void setHeadPos(float pan, float tilt);
	void armsDown(void);
	void rotateEye(Eye *eye, float pan, float tilt);

	// A partir de aqui todo nuevo vero
public:
	bool hasEyeBones;
	Eye *leftEye;
	Eye *rightEye;
	float headPanOffset;
	float headTiltOffset;

private:
	float pan;
	float tilt;
	psisban::Config *bodyConfiguration;

};


#endif
