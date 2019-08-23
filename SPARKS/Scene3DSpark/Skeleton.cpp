#include "stdAfx.h"
#include "Logger.h"
#include "Horde3D.h"
#include "Skeleton.h"


using namespace std;

#pragma comment (lib, "Horde3D.lib")
#pragma comment (lib, "Horde3DUtils.lib")
#pragma comment (lib, "opengl32.lib")


/*
Eye::Eye(char *jointName, bool zSignShift) {
	strncpy(this->jointName, jointName, MAX_JOINT_NAME_LENGTH);

	this->zSignShift = zSignShift;

	float tx, ty, tz;
	float sx, sy, sz;

	int res = h3dFindNodes(H3DRootNode, jointName, H3DNodeTypes::Undefined);
	if (res != 1) {
		ERR("Wrong number of joints found, expeted 1, found %d finding '%s'", 
			res, jointName
		);
	}

	H3DNode jointNode = h3dGetNodeFindResult(0);
	if (jointNode == 0) ERR("h3dGetNodeFindResult");

	
	h3dGetNodeTransform(
		jointNode, 
		&tx, &ty, &tz, 
		&originalRotation.vx,
		&originalRotation.vy,
		&originalRotation.vz,
		&sx, &sy, &sz
	);
}
*/


/// Rotate an eye side-by-side and upside-down.

void Skeleton::rotateEye(Eye *eye, float pan, float tilt) {
	float mult = eye->zSignShift ? -1.0f : 1.0f;

	rotateJoint(
		eye->jointName, 
		eye->originalRotation.vx ,
		eye->originalRotation.vy - tilt - headTiltOffset,
		mult * (eye->originalRotation.vz - pan - headPanOffset)
	);
}


/// Initialization

void Skeleton::init(void) {

	pan = tilt = 0;

	hasEyeBones = bodyConfiguration->getBool("Body.Joints.Eyes.HasEyeBones");
	if (hasEyeBones) {
		char leftEyeJoint[MAX_JOINT_NAME_LENGTH];
		char rightEyeJoint[MAX_JOINT_NAME_LENGTH];

		bodyConfiguration->getString("Body.Joints.Eyes.Left", leftEyeJoint, MAX_JOINT_NAME_LENGTH);
		bodyConfiguration->getString("Body.Joints.Eyes.Right", rightEyeJoint, MAX_JOINT_NAME_LENGTH);

		leftEye = new Eye(leftEyeJoint, true);
		rightEye = new Eye(rightEyeJoint, false);
	}
	else {
		leftEye = NULL;
		rightEye = NULL;
	}

	headPanOffset = bodyConfiguration->getFloat("Body.Joints.HeadPanOffset");
	headTiltOffset = bodyConfiguration->getFloat("Body.Joints.HeadTiltOffset");
}


/// Rotates a joint acting on its three euler angles.

void Skeleton::rotateJoint(
	char *jointName, 
	float new_rx, 
	float new_ry, 
	float new_rz
)
{
	int res = h3dFindNodes(H3DRootNode, jointName, H3DNodeTypes::Undefined);
	if (res != 1) {
		ERR("Wrong number of joints found, expeted 1, found %d finding '%s'", 
			res, jointName
		);
	}

	H3DNode jointNode = h3dGetNodeFindResult(0);
	if (jointNode == 0) ERR("h3dGetNodeFindResult");

	float tx, ty, tz;
	float rx, ry, rz;
	float sx, sy, sz;
	
	h3dGetNodeTransform(
		jointNode, 
		&tx, &ty, &tz, 
		&rx, &ry, &rz,
		&sx, &sy, &sz
	);

	h3dSetNodeTransform(
		jointNode, 
		tx, ty, tz, 
		new_rx, new_ry, new_rz,
		sx, sy, sz
	);
}


const float maxPan = 45;
const float maxTilt = 45;


inline float absoluteValue(float v) {
	return v > 0 ? v : -v;
}

inline float sign(float x) {
	return x >= 0 ? 1.0f : -1.0f;
}


/// Gets the pan and tilt angles of the head.

void Skeleton::getHeadPos(float *pPan, float *pTilt) {
	*pPan = pan;
	*pTilt = tilt;
}


/// Rotates the head side-by-side and upside-down.

void Skeleton::rotateHead(float pan, float tilt) {
	float myPan = sign(pan) * min(absoluteValue(pan), bodyConfiguration->getFloat("Body.Joints.MaxHeadPan"));
 	float myTilt= sign(tilt) * min(absoluteValue(tilt), bodyConfiguration->getFloat("Body.Joints.MaxHeadTilt"));

	this->pan = myPan;
	this->tilt = myTilt;

	int neckJointNumber = bodyConfiguration->getLength("Body.Joints.NeckJoints");

	char jointName[128];
	char settingName[128];

	for (
		int neckJointIndex = 0; 
		neckJointIndex < neckJointNumber;
		neckJointIndex++
	)
	{
		_snprintf(settingName, 128, "Body.Joints.NeckJoints.[%d].Joint", neckJointIndex);
		bodyConfiguration->getString(settingName, jointName, 128);


		if (bodyConfiguration->getBool("Body.Joints.NeckFix")) {
			rotateJoint(jointName, -myPan - headPanOffset, -myTilt - headTiltOffset, 0.0f);		
		}
		else {
			rotateJoint(jointName, -myTilt - headTiltOffset, -myPan - headPanOffset, 0.0f);		
		}
	}
}


/// Baja los brazos para un personaje situado inicialmente en posici�n T.

void Skeleton::armsDown(void) {

	char jointName[128];
	char settingName[128];

	int armIndex;
	int leftArmJointNumber;
	int rightArmJointNumber;

	leftArmJointNumber = bodyConfiguration->getLength("Body.Joints.LeftArmJoints");

	for (armIndex = 0; armIndex < leftArmJointNumber; armIndex++) {

		_snprintf(settingName, 128, "Body.Joints.LeftArmJoints.[%d].Joint", armIndex);
		bodyConfiguration->getString(settingName, jointName, 128);

		rotateJoint(
			jointName, 
			0.0f, 0.0f, -60.0f
		);
	}

	rightArmJointNumber = bodyConfiguration->getLength("Body.Joints.RightArmJoints");

	for (armIndex = 0; armIndex < rightArmJointNumber; armIndex++) {

		_snprintf(settingName, 128, "Body.Joints.RightArmJoints.[%d].Joint", armIndex);
		bodyConfiguration->getString(settingName, jointName, 128);

		rotateJoint(
			jointName, 
			0.0f, 0.0f, 60.0f
		);
	}
}


