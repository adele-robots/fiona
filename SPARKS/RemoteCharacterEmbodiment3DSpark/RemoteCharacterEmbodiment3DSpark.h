/// @file RemoteCharacterEmbodiment3DSpark.h
/// @brief Component RemoteCharacterEmbodiment3DSpark main class.


#ifndef __RemoteCharacterEmbodiment3DSpark_H
#define __RemoteCharacterEmbodiment3DSpark_H

#include "Component.h"
#include "FrameEventSubscriber.h"

#include "IAnimation.h"
#include "IFaceExpression.h"
#include "IEyes.h"
#include "INeck.h"
#include "IJoint.h"
#include "IConcurrent.h"
#include "IEventQueue.h"
#include "ICamera.h"
#include "IFrameEventPublisher.h"

#include "IControlVoice.h"
#include "IAsyncFatalErrorHandler.h"
#include "IAudioQueue.h"


#include "../Scene3DSpark/Scene3DComponent.h"
#include "../RemoteRendererSpark/RemoteRendererComponent.h"
#include "../FFMpegAVOutputSpark/FFMpegAVOutputComponent.h"
#include "../ThreadSpark/ThreadComponent.h"
#include "../WindowSpark/WindowComponent.h"



/// @brief This is the main class for component RemoteCharacterEmbodiment3DSpark.
///
/// RemoteCharacterEmbodiment3D component.
/// Includes RemoteRendererComponent, FFMpegAVOutputComponent, Scene3DSpark
/// MainWindow and Thread

class RemoteCharacterEmbodiment3DSpark :
	public Component,

	public IAnimation,
	public IFaceExpression,
	public IEyes,
	public INeck,
	public IJoint,
	public IConcurrent,
	public IApplication,
	public IAsyncFatalErrorHandler,
	public IEventQueue,
	public ICamera,
	public IFrameEventPublisher
{
public:
		RemoteCharacterEmbodiment3DSpark(
		char *instanceName, 
		ComponentSystem *cs
	) : Component(instanceName, cs)
	{
		//1-Instances
		myWindowComponent=new WindowComponent(instanceName,cs);
		myScene3DComponent=new Scene3DComponent(instanceName,cs);
		myThreadComponent=new ThreadComponent(instanceName,cs);	
		myRemoteRendererComponent=new RemoteRendererComponent(instanceName,cs);
		myFFMpegAVOutputComponent=new FFMpegAVOutputComponent(instanceName,cs);
	}

private:
	IControlVoice *myControlVoice;
	IAsyncFatalErrorHandler *myAsyncFatalErrorHandler;
	IAudioQueue *myAudioQueue;


	void initializeRequiredInterfaces() {	
		requestRequiredInterface<IControlVoice>(&myControlVoice);
		requestRequiredInterface<IAsyncFatalErrorHandler>(&myAsyncFatalErrorHandler);
		requestRequiredInterface<IAudioQueue>(&myAudioQueue);
	}

public:
	//Instance sub-components
	Scene3DComponent *myScene3DComponent;
	RemoteRendererComponent *myRemoteRendererComponent;
	FFMpegAVOutputComponent *myFFMpegAVOutputComponent;
	ThreadComponent *myThreadComponent;
	WindowComponent *myWindowComponent;

	void init(void);
	void quit(void);

	//IConcurrent implementation
	void start();
	void stop();

	//IApplication implementation
	void run();

	//IAsyncFatalErrorHandler implementation
	void handleError(char*);
	
	// IEventQueue implementation
	void postEvent(psisban::Messages, void *data1, void *data2);
	void sendEvent(psisban::Messages, void *data1, void *data2);
	void registerListener(psisban::Messages, EventCallback);
	void eventLoop(void);

	//IAnimation implementation
	void update();
	void playAnimation(char *animationFileName);

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
	void setBlinkLevel(float);
	
	//INeck implementation
	void rotateHead(float pan, float tilt);

	//ICamera
	void setCameraPosition(float X,float Y,float Z);
	void setCameraRotation(float X,float Y,float Z);
	void setCameraParameters(bool IsOrtho,float VisionAngle,float nearClippingPlane,float FarClippingPlane);

	//IFrameEventPublisher implementation
	void addFrameEventSubscriber(FrameEventSubscriber *frameEventSubscriber);
};

#endif
