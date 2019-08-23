#ifndef __I_CAMERA_H
#define __I_CAMERA_H


class ICamera {
public:
	virtual void setCameraPosition(float X,float Y,float Z) = 0;
	virtual void setCameraRotation(float X,float Y,float Z) = 0;
	virtual void setCameraParameters(bool IsOrtho,
										 float VisionAngle,
										 float nearClippingPlane,
										 float FarClippingPlane) = 0;


};


#endif
