/// @file Scene2DSpark.h
/// @brief Component Scene2DSpark main class.


#ifndef __Scene2DSpark_H
#define __Scene2DSpark_H

#include "Component.h"
#include "Configuration.h"

//Includes necesarios
#include "IFaceExpression.h"
#include "IEyes.h"
#include "INeck.h"
#include "IAnimation.h"
#include "IRenderizable.h"
#include "ICamera.h"

#include "MorphTargetBlender2D.h"


#include "Skeleton2D.h"
#include "SvgGeneral.h"

class Scene2DSpark :
	public Component,
	public IFaceExpression,
	public IEyes,
	public INeck,
	public IAnimation,
	public IRenderizable,
	public ICamera
{
public:
		Scene2DSpark(
				char *instanceName,
				ComponentSystem *cs
		) : Component(instanceName, cs)
		{

		}

private:
	// Metodos privados

	void initializeRequiredInterfaces() {
	}

	void initSvgConfiguration();

	//void die(const char *fmt, ...);

public:
	//Mandatory
	void init(void);
	void quit(void);
	
	//IFaceExpression implementation
	void setFaceExpression(char *expressionName,float intensity);

	//IEyes implementation
	void rotateEye(float pan,float tilt);
	void setBlinkLevel(float blinkLevel);

	//INeck implementation
	void rotateHead(float pan, float tilt);

	//IAnimation implementation
	void update();
	void playAnimation(char *animationFileName);

	//IRenderizable implementation
	void * render(void);
	//H3DRes getCamaraNode();
	void unMapResourceStream (void);

	//ICamera implementation
	void setCameraPosition(float X,float Y,float Z);
	void setCameraRotation(float X,float Y,float Z);
	void setCameraParameters(bool isOrtho, float visionAngle,float nearClippingPlane,float farClippingPlane);

private:
	// Atributos privados

	float _animTime;

	// Variables relativas al pan y al tilt
	float maxHeadPan;
	float maxHeadTilt;
	float maxEyePan;
	float maxEyeTilt;
	float myPan;
	float myTilt;
	float PanSvg;
	float TiltSvg;

	int height;
	int width;
	int bytesPerPixel;

	unsigned char* buffer;

	psisban::Config characterConfiguration;

	MorphTargetBlender2D *myOldMorphTargetBlender;

	SvgGeneral *svgGeneral;

	agg::rendering_buffer rbuf;

};

#endif
