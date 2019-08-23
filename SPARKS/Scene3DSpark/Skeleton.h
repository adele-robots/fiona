/// @file Skeleton.h
/// @brief Skeleton class declaration.

#ifndef __SKELETON_H
#define __SKELETON_H


#include "Scene.h"

typedef struct {
	float vx;
	float vy;
	float vz;
} Vector;


static const int MAX_JOINT_NAME_LENGTH = 80;


class Eye {
public:
	Eye(char *jointName, bool zSignShift);
	Vector originalRotation;
	char jointName[MAX_JOINT_NAME_LENGTH];
	bool zSignShift;
};

/// \brief This class for joint manipulation.
///
/// Skeletons have joints which are scene nodes of which other joints are 
/// daughters in the scene graph. Rotating a joint causes the corresponding
/// deformation of the associated body mesh. Thus joint rotation angles
/// are the elemental degrees of freedom of the character kinematics.
///
/// Currently joint translations are not used with the possible exception
/// of the root joint to translate the whole character.

class Skeleton
{
public:
	Skeleton(psisban::Config *c) : bodyConfiguration(c) {}
	void rotateJoint(
		char *jointName, 
		float x, 
		float y, 
		float z
	);
	void init(void);
	void rotateHead(float pan, float tilt);
	void getHeadPos(float *pPan, float *pTilt);
	void armsDown(void);
	void rotateEye(Eye *eye, float pan, float tilt);

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
