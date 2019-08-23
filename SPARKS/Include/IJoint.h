#ifndef __I_JOINT_H
#define __I_JOINT_H


class IJoint {
public:
	virtual void setJointTransMat(const char *jointName, const float *mat4x4) = 0;
	virtual void rotateJointPart(char *jointName, float rotationX,	float rotationY, float rotationZ) = 0;//Rotates the joint specified the amount specified in each axis
	virtual void moveJointPart(char *jointName, float translationX,	float translationY, float translationZ) = 0;
	virtual void moveDiamondJoint(float translationX, float translationY, float translationZ) = 0;//Moves the hip diamond bone the amount specified for each axis

	virtual void GetJointRotation(char *jointName, float *x, float *y, float *z) = 0;//Gets the absolute values of the joint angles.
	virtual void GetJointPosition(char *jointName, float *x, float *y, float *z) = 0;//Gets the absolute values of the joint position.
	virtual bool findNode(const char *jointName) = 0;
};


#endif
