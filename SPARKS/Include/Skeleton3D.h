#pragma once
//PABLO: Scene
#include "IScene.h"
#include "Iskeleton.h"

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
/// \brief This class encapsulates the 3D scene rendering implementation.
///
/// Scenes are 3D, time variying geometries. To render them we use a 3D enging.
/// This class exploits the engine facilities. The main structure is the
/// Scene Tree. It has a root node. The rest of the nodes have transformations,
/// (traslations, rotations and scale). They are compounded as one traverses
/// the Tree. Other scene elements are meshes, textures, camara and lights.
class Skeleton3D : public ISkeleton
{
public:
	Skeleton3D(psisban::Config *c) : ISkeleton(c) {}
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


