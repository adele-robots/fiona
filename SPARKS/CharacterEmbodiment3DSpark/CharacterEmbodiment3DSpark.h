/// @file CharacterEmbodiment3DSpark.cpp
/// @brief Component CharacterEmbodiment3DSpark main class.


#ifndef __CharacterEmbodiment3DSpark_H
#define __CharacterEmbodiment3DSpark_H

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
#include "IWindow.h"
#include "IAsyncFatalErrorHandler.h"

#include "../Scene3DSpark/Scene3DComponent.h"
#include "../LocalRendererSpark/LocalRendererComponent.h"
#include "../ThreadSpark/ThreadComponent.h"
#include "../WindowSpark/WindowComponent.h"


/// @brief This is the main class for component CharacterEmbodiment3DSpark.
/// 3DCharacterEmbodiment

class CharacterEmbodiment3DSpark :
	public Component,
	public IAnimation,
	public IFaceExpression,
	public IEyes,
	public INeck,
	public IConcurrent,
	public IApplication,
	public IAsyncFatalErrorHandler,
	public IEventQueue,
	public ICamera,
	public IFrameEventPublisher
{
public:
	CharacterEmbodiment3DSpark(
		char *instanceName, 
		ComponentSystem *cs
	) : Component(instanceName, cs)
	{
		//1-Instances
		myScene3DComponent=new Scene3DComponent(instanceName,cs);
		myLocalRendererComponent=new LocalRendererComponent(instanceName,cs);
		myThreadComponent=new ThreadComponent(instanceName,cs);	
		myWindowComponent=new WindowComponent(instanceName,cs);
	}

private:

	IAsyncFatalErrorHandler *myAsyncFatalErrorHandler;

	void initializeRequiredInterfaces() {	
		requestRequiredInterface<IAsyncFatalErrorHandler>(&myAsyncFatalErrorHandler);
	}

public:
	//Instance sub-components
	Scene3DComponent *myScene3DComponent;
	LocalRendererComponent *myLocalRendererComponent;
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
