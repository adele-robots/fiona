/// @file RemoteCharacterEmbodiment2DSpark.h
/// @brief Component RemoteCharacterEmbodiment2DSpark main class.

#ifndef __RemoteCharacterEmbodiment2DSpark_H
#define __RemoteCharacterEmbodiment2DSpark_H

#include "Component.h"
#include "FrameEventSubscriber.h"

#include "IAnimation.h"
#include "IFaceExpression.h"
#include "IEyes.h"
#include "INeck.h"
#include "IConcurrent.h"
#include "IEventQueue.h"
#include "ICamera.h"
#include "IFrameEventPublisher.h"

#include "IControlVoice.h"
#include "IAsyncFatalErrorHandler.h"
#include "IAudioQueue.h"


#include "../FFMpegAVOutputSpark/FFMpegAVOutputComponent.h"
#include "../Scene2DSpark/Scene2DSpark.h"
#include "../RemoteRenderer2DSpark/RemoteRenderer2DComponent.h"
#include "../ThreadSpark/ThreadComponent.h"
#include "../EmptyApplicationSpark/EmptyApplicationSpark.h"



/// @brief This is the main class for component RemoteCharacterEmbodiment2DSpark.

/// RemoteCharacterEmbodiment2D component.
/// Includes RemoteRenderer2DComponent, FFMpegAVOutput2DComponent, Scene2DSpark
/// MainWindow and Thread

class RemoteCharacterEmbodiment2DSpark :
	public Component,

	public IAnimation,
	public IFaceExpression,
	public IEyes,
	public INeck,
	public IConcurrent,
	public IApplication,
	public IAsyncFatalErrorHandler,
	public ICamera,
	public IFrameEventPublisher
{
public:
		RemoteCharacterEmbodiment2DSpark(
		char *instanceName,
		ComponentSystem *cs
	) : Component(instanceName, cs)
	{
		//1-Instances
		myWindowComponent=new EmptyApplicationSpark(instanceName,cs);
		myScene2DComponent=new Scene2DSpark(instanceName,cs);
		myThreadComponent=new ThreadComponent(instanceName,cs);
		myRemoteRenderer2DComponent=new RemoteRenderer2DComponent(instanceName,cs);
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
	Scene2DSpark *myScene2DComponent;
	RemoteRenderer2DComponent *myRemoteRenderer2DComponent;
	FFMpegAVOutputComponent *myFFMpegAVOutputComponent;
	ThreadComponent *myThreadComponent;
	EmptyApplicationSpark *myWindowComponent;

	void init(void);
	void quit(void);

	//IConcurrent implementation
	void start();
	void stop();

	//IApplication implementation
	void run();

	//IAsyncFatalErrorHandler implementation
	void handleError(char*);

	//IAnimation implementation
	void update();
	void playAnimation(char *animationFileName);

	//IFaceExpression implementation
	void setFaceExpression(char *expressionName,float intensity);

	//IEyes implementation
	void rotateEye(float pan,float tilt);
	void setBlinkLevel(float);

	//INeck implementation
	void rotateHead(float pan, float tilt);

	//ICamera implementation
	void setCameraPosition(float X,float Y,float Z);
	void setCameraRotation(float X,float Y,float Z);
	void setCameraParameters(bool IsOrtho,float VisionAngle,float nearClippingPlane,float FarClippingPlane);

	//IFrameEventPublisher implementation
	void addFrameEventSubscriber(FrameEventSubscriber *frameEventSubscriber);
};

#endif
