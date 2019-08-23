/// @file Scene3DComponent.cpp
/// @brief Component Scene3DComponent main class.


#ifndef __Scene3DComponent_H
#define __Scene3DComponent_H


#include "Component.h"
#include "IFaceExpression.h"
#include "IEyes.h"
#include "INeck.h"
#include "IJoint.h"
#include "IAnimation.h"
#include "IRenderizable.h"
#include "ICamera.h"

#include "MorphTargetBlender.h"
//#include "Skeleton.h"


#include "Configuration.h"

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



#define HORDE_LOG_FILE_NAME "Horde3D_Log.html"


#define HORDE_ERR(...) {															\
	LoggerError("Horde3D Error. See %S for further details.", HORDE_LOG_FILE_NAME);	\
	ERR(__VA_ARGS__);																\
	h3dutDumpMessages();															\
}


/// @brief This is the main class for component Scene3DComponent.
///
/// Loads 3D scene data from disk and renders it to screen.

class Scene3DComponent :
	public Component,

	public IFaceExpression,		
	public IEyes,
	public INeck,
	public IJoint,
	public IAnimation,
	public IRenderizable,
	public ICamera
{
public:
	Scene3DComponent(
		char *instanceName, 
	ComponentSystem *componentSystem
	) : Component(instanceName,componentSystem) 
	{}

private:
	void initializeRequiredInterfaces() {	
		// none
	}


public:
	

	void init(void);
	void quit(void);
	
	//IFaceExpression implementation
	void setFaceExpression(char *expressionName,float intensity);
	
	//IJoint implementation
	void setJointTransMat(const char *jointName, const float *mat4x4);
	void rotateJointPart(char *jointName, float rotationX,	float rotationY, float rotationZ);//For general joint rotation
	void moveJointPart(char *jointName, float translationX,	float translationY, float translationZ);
	void moveDiamondJoint(float translationX, float translationY, float translationZ);//For Diamont joint movement
	void GetJointRotation(char *jointName, float *x, float *y, float *z);
	void GetJointPosition(char *jointName, float *x, float *y, float *z);
	bool findNode(const char *jointName);

	//IEyes implementation
	void rotateEye(float pan,float tilt);
	void setBlinkLevel(float blinkLevel);
	
	//INeck implementation
	void rotateHead(float pan, float tilt);
	
	//IAnimation implementation
	void update();
	void playAnimation(char *animationFileName);
	
	//IRenderizable implementation
	void render(void);
	H3DRes getCamaraNode();
	
	//ICamera
	void setCameraPosition(float X,float Y,float Z);
	void setCameraRotation(float X,float Y,float Z);
	void setCameraParameters(bool isOrtho, float visionAngle,float nearClippingPlane,float farClippingPlane);

private:
	void addResources(void);
	void loadResourcesFormDisk(void);
	void addAnimationResources(void);
	void addNodesToScene(void);
	void setInitialMorphs(void);
	void createLights(void);
	//Skeleton
	void initSkeleton(void);
	void setNodeTransMat(const char *jointName, const float *mat4x4);
	void rotateJoint(
			char *jointName,
			float x,
			float y,
			float z
		);
	void moveJoint(char *jointName,float x,float y,float z);
	void rotateHeadSkeleton(float pan, float tilt);
	void getHeadPos(float *pPan, float *pTilt);
	void armsDown(void);
	void rotateEyeSkeleton(Eye *eye, float pan, float tilt);
	//end of Skeleton
	
private:
	MorphTargetBlender *myOldMorphTargetBlender;
	//psisban::Config bodyConfiguration;
	psisban::Config characterConfiguration;
	H3DRes pipelineResource;
	H3DRes sceneResourcePlane;
	H3DRes lightMatRes;
	H3DRes sceneResource;
	H3DRes _fontMatRes;
	H3DRes _panelMatRes;
	H3DRes camaraNode;
	int width;
	int height;
	bool background;

public:
	H3DNode morphNode;
	float _animTime;
	bool hasEyeBones;
	Eye *leftEye;
	Eye *rightEye;
	float headPanOffset;
	float headTiltOffset;
	float pan;
	float tilt;

};


#endif
