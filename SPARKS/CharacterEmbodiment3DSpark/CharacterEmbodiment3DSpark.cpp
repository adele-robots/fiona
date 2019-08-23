/// @file CharacterEmbodiment3DSpark.cpp
/// @brief CharacterEmbodiment3DSpark class implementation.

// Third party libraries are linked explicitly once in the project.
// #pragma comment(lib, "thirdPartyLib.lib")

#include "stdAfx.h"
#include "CharacterEmbodiment3DSpark.h"

#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "CharacterEmbodiment3DSpark")) {
			return new CharacterEmbodiment3DSpark(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif


/// Initializes CharacterEmbodiment3DSpark component.

void CharacterEmbodiment3DSpark::init(void)
{
	//2-configurations
	myWindowComponent->componentConfiguration = componentConfiguration;
	myScene3DComponent->componentConfiguration = componentConfiguration;
	myThreadComponent->componentConfiguration = componentConfiguration;
	myLocalRendererComponent->componentConfiguration = componentConfiguration;
	

	//3-connections
	//required interfaces of CharacterEmbodiment3DSpark
	myThreadComponent->myAsyncFatalErrorHandler = myAsyncFatalErrorHandler;

	//required interfaces of LocalRendererComponent
	myLocalRendererComponent->myRenderizable = myScene3DComponent;
	myLocalRendererComponent->myWindow = myWindowComponent;

	//required interfaces of ThreadComponent
	myThreadComponent->myThreadProc = myLocalRendererComponent;

	//4-initializations
	myWindowComponent->init();
	myScene3DComponent->init();
	myThreadComponent->init();
	myLocalRendererComponent->init();
}

/// Unitializes the CharacterEmbodiment3DSpark component.

void CharacterEmbodiment3DSpark::quit(void)
{
	myLocalRendererComponent->quit();
	myThreadComponent->quit();
	myScene3DComponent->quit();
	myWindowComponent->init();
}

//IConcurrent implementation
void CharacterEmbodiment3DSpark::start(void)
{
	myThreadComponent->start();
}

void CharacterEmbodiment3DSpark::stop(void)
{
	myThreadComponent->stop();
}

//IApplication implementation
void CharacterEmbodiment3DSpark::run(void)
{
	myWindowComponent->run();
}

//IAsyncFatalErrorHandler implementation
void CharacterEmbodiment3DSpark::handleError(char* messageError)
{
	myWindowComponent->handleError(messageError); 
}

// IEventQueue implementation
void CharacterEmbodiment3DSpark::postEvent(psisban::Messages msg, void *data1, void *data2)
{
	myWindowComponent->postEvent(msg,data1,data2);
}

void CharacterEmbodiment3DSpark::sendEvent(psisban::Messages msg, void *data1, void *data2)
{
	myWindowComponent->sendEvent(msg,data1,data2);
}

void CharacterEmbodiment3DSpark::registerListener(psisban::Messages msg, EventCallback eventCallback)
{
	myWindowComponent->registerListener(msg,eventCallback);
}

void CharacterEmbodiment3DSpark::eventLoop(void)
{
	myWindowComponent->eventLoop();
}

//IAnimation implementation
void CharacterEmbodiment3DSpark::update(void)
{
	myScene3DComponent->update();
}

void CharacterEmbodiment3DSpark::playAnimation(char *animationFileName)
{
	myScene3DComponent->playAnimation(animationFileName);
}

//IFaceExpression implementation
void CharacterEmbodiment3DSpark::setFaceExpression(char *expressionName,float intensity)
{
	myScene3DComponent->setFaceExpression(expressionName,intensity);	
}

//IEyes implementation
void CharacterEmbodiment3DSpark::rotateEye(float pan,float tilt)
{
	myScene3DComponent->rotateEye(pan,tilt);
}

void CharacterEmbodiment3DSpark::setBlinkLevel(float level)
{
	myScene3DComponent->setBlinkLevel(level);
}

//INeck implementation
void CharacterEmbodiment3DSpark::rotateHead(float pan, float tilt)
{
	myScene3DComponent->rotateHead(pan,tilt);
}

//ICamera
void CharacterEmbodiment3DSpark::setCameraPosition(float X,float Y,float Z)
{
	myScene3DComponent->setCameraPosition(X,Y,Z);
}

void CharacterEmbodiment3DSpark::setCameraRotation(float X,float Y,float Z)
{
	myScene3DComponent->setCameraRotation(X,Y,Z);
}

void CharacterEmbodiment3DSpark::setCameraParameters(bool isOrtho,
										 	 	 	 	 	float visionAngle,
										 	 	 	        float nearClippingPlane,
										 	 	 	        float farClippingPlane)
{
	myScene3DComponent->setCameraParameters(isOrtho,visionAngle,nearClippingPlane,farClippingPlane);
}

//IFrameEventPublisher implementation
void CharacterEmbodiment3DSpark::addFrameEventSubscriber(FrameEventSubscriber *frameEventSubscriber)
{
	myLocalRendererComponent->addFrameEventSubscriber(frameEventSubscriber);
}
