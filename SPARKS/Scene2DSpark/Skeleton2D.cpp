#include "stdAfx.h"
#include "Logger.h"
#include "Skeleton2D.h"

using namespace std;
// Implementation RotateJoint
// Rotates a joint acting on its three euler angles.
void Skeleton2D::rotateJoint( char *jointName,
								  float new_rx,
								  float new_ry,
								  float new_rz ){

}

/// Initialization
void Skeleton2D::init(void) {
	headPanOffset=0;
	headTiltOffset=0;
}



inline float absoluteValue(float v) {
	return v > 0 ? v : -v;
}

inline float sign(float x) {
	return x >= 0 ? 1.0f : -1.0f;
}

// Implementation rotateHead
// Rotates the head side-by-side and upside-down.
void Skeleton2D::rotateHead(float pan, float tilt) {
	float myPan = sign(pan) * min(absoluteValue(pan), bodyConfiguration->getFloat(const_cast<char *>("Body.Joints.MaxHeadPan")));
 	float myTilt= sign(tilt) * min(absoluteValue(tilt), bodyConfiguration->getFloat(const_cast<char *>("Body.Joints.MaxHeadTilt")));
	this->pan = myPan;
	this->tilt = myTilt;
}

// Implementation armsDown
/// Baja los brazos para un personaje situado inicialmente en posicion T.
void Skeleton2D::armsDown(void) {

}

/// Rotate an eye side-by-side and upside-down.
void Skeleton2D::rotateEye(Eye *eye, float pan, float tilt) {

}

// Implementation getHeadPos
// Gets the pan and tilt angles of the head.
void Skeleton2D::getHeadPos(float *pPan, float *pTilt) {
	*pPan = pan;
	*pTilt = tilt;
}

// Implementation setHeadPos
// Sets the pan and tilt angles of the head.
/*void Skeleton2D::setHeadPos(float pan, float tilt) {
	this->pan = pan;
	this->tilt = tilt;
}*/


