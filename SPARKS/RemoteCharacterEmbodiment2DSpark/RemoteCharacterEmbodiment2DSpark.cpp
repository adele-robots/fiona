/// @file RemoteCharacterEmbodiment2DSpark.cpp
/// @brief RemoteCharacterEmbodiment2DSpark class implementation.

// Third party libraries are linked explicitly once in the project.
// #pragma comment(lib, "thirdPartyLib.lib")

#include "stdAfx.h"
#include "RemoteCharacterEmbodiment2DSpark.h"

#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "RemoteCharacterEmbodiment2DSpark")) {
			return new RemoteCharacterEmbodiment2DSpark(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif

/// Initializes RemoteCharacterEmbodiment2DSpark component.
void RemoteCharacterEmbodiment2DSpark::init(void){
	//2-configurations
	myWindowComponent->componentConfiguration = componentConfiguration;
	myScene2DComponent->componentConfiguration = componentConfiguration;
	myThreadComponent->componentConfiguration = componentConfiguration;
	myRemoteRenderer2DComponent->componentConfiguration = componentConfiguration;
	myFFMpegAVOutputComponent->componentConfiguration = componentConfiguration;

	//3-connections
	//required interfaces of RemoteCharacterEmbodimentComponent
	myRemoteRenderer2DComponent->myAudioQueue = myAudioQueue;
	myRemoteRenderer2DComponent->myControlVoice = myControlVoice;
	myThreadComponent->myAsyncFatalErrorHandler = myAsyncFatalErrorHandler;

	//required interfaces of RemoteRendererComponent
	myRemoteRenderer2DComponent->myRenderizable = myScene2DComponent;
	myRemoteRenderer2DComponent->myAudioFlow =myFFMpegAVOutputComponent;
	myRemoteRenderer2DComponent->myFlow =myFFMpegAVOutputComponent;

	//required interfaces of ThreadComponent
	myThreadComponent->myThreadProc = myRemoteRenderer2DComponent;

	//4-initializations
	myWindowComponent->init();
	myScene2DComponent->init();
	myThreadComponent->init();
	myRemoteRenderer2DComponent->init();
	myFFMpegAVOutputComponent->init();
}

/// Unitializes the RemoteCharacterEmbodiment2DComponent component.

void RemoteCharacterEmbodiment2DSpark::quit(void){
	myFFMpegAVOutputComponent->quit();
	myRemoteRenderer2DComponent->quit();
	myThreadComponent->quit();
	myScene2DComponent->quit();
	myWindowComponent->quit();
}

//IConcurrent implementation
void RemoteCharacterEmbodiment2DSpark::start(void){
	myThreadComponent->start();
}

void RemoteCharacterEmbodiment2DSpark::stop(void){
	myThreadComponent->stop();
}

//IApplication implementation
void RemoteCharacterEmbodiment2DSpark::run(void){
	myWindowComponent->run();
}

//IAsyncFatalErrorHandler implementation
void RemoteCharacterEmbodiment2DSpark::handleError(char* messageError){
	myWindowComponent->handleError(messageError);
}

//IAnimation implementation
void RemoteCharacterEmbodiment2DSpark::update(void){
	myScene2DComponent->update();
}

void RemoteCharacterEmbodiment2DSpark::playAnimation(char *animationFileName){
	myScene2DComponent->playAnimation(animationFileName);
}

//IFaceExpression implementation
void RemoteCharacterEmbodiment2DSpark::setFaceExpression(char *expressionName,float intensity){
	myScene2DComponent->setFaceExpression(expressionName,intensity);
}

//IEyes implementation
void RemoteCharacterEmbodiment2DSpark::rotateEye(float pan,float tilt){
	myScene2DComponent->rotateEye(pan,tilt);
}

void RemoteCharacterEmbodiment2DSpark::setBlinkLevel(float level){
	myScene2DComponent->setBlinkLevel(level);
}

//INeck implementation
void RemoteCharacterEmbodiment2DSpark::rotateHead(float pan, float tilt){
	myScene2DComponent->rotateHead(pan,tilt);
}

//ICamera
void RemoteCharacterEmbodiment2DSpark::setCameraPosition(float X,float Y,float Z)
{
	myScene2DComponent->setCameraPosition(X,Y,Z);
}

void RemoteCharacterEmbodiment2DSpark::setCameraRotation(float X,float Y,float Z)
{
	myScene2DComponent->setCameraRotation(X,Y,Z);
}

void RemoteCharacterEmbodiment2DSpark::setCameraParameters(bool isOrtho,float visionAngle,float nearClippingPlane,float farClippingPlane)
{
	myScene2DComponent->setCameraParameters(isOrtho,visionAngle,nearClippingPlane,farClippingPlane);
}

//IFrameEventPublisher implementation
void RemoteCharacterEmbodiment2DSpark::addFrameEventSubscriber(FrameEventSubscriber *frameEventSubscriber)
{
	myRemoteRenderer2DComponent->addFrameEventSubscriber(frameEventSubscriber);
}
