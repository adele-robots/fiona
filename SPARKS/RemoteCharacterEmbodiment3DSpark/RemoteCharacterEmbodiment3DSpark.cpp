/// @file RemoteCharacterEmbodiment3DSpark.cpp
/// @brief RemoteCharacterEmbodiment3DSpark class implementation.

// Third party libraries are linked explicitly once in the project.
// #pragma comment(lib, "thirdPartyLib.lib")

#include "stdAfx.h"
#include "RemoteCharacterEmbodiment3DSpark.h"

#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "RemoteCharacterEmbodiment3DSpark")) {
			return new RemoteCharacterEmbodiment3DSpark(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif

/// Initializes RemoteCharacterEmbodiment3DSpark component.
void RemoteCharacterEmbodiment3DSpark::init(void){
	//2-configurations
	myWindowComponent->componentConfiguration = componentConfiguration;
	myScene3DComponent->componentConfiguration = componentConfiguration;
	myThreadComponent->componentConfiguration = componentConfiguration;
	myRemoteRendererComponent->componentConfiguration = componentConfiguration;
	myFFMpegAVOutputComponent->componentConfiguration = componentConfiguration;

	//3-connections
	//required interfaces of RemoteCharacterEmbodiment3DComponent
	myRemoteRendererComponent->myAudioQueue = myAudioQueue;
	myRemoteRendererComponent->myControlVoice = myControlVoice;
	myThreadComponent->myAsyncFatalErrorHandler = myAsyncFatalErrorHandler;

	//required interfaces of RemoteRendererComponent
	myRemoteRendererComponent->myRenderizable = myScene3DComponent;
	myRemoteRendererComponent->myWindow = myWindowComponent;
	myRemoteRendererComponent->myAudioFlow =myFFMpegAVOutputComponent;
	myRemoteRendererComponent->myFlow =myFFMpegAVOutputComponent;

	//required interfaces of ThreadComponent
	myThreadComponent->myThreadProc = myRemoteRendererComponent;

	//4-initializations
	myWindowComponent->init();
	myScene3DComponent->init();
	myThreadComponent->init();
	myRemoteRendererComponent->init();
	myFFMpegAVOutputComponent->init();
}


/// Unitializes the RemoteCharacterEmbodiment3DComponent component.

void RemoteCharacterEmbodiment3DSpark::quit(void){
	myFFMpegAVOutputComponent->quit();
	myRemoteRendererComponent->quit();
	myThreadComponent->quit();
	myScene3DComponent->quit();
	myWindowComponent->quit();
}


//IConcurrent implementation
void RemoteCharacterEmbodiment3DSpark::start(void){
	myThreadComponent->start();
}

void RemoteCharacterEmbodiment3DSpark::stop(void){
	myThreadComponent->stop();
}

//IApplication implementation
void RemoteCharacterEmbodiment3DSpark::run(void){
	myWindowComponent->run();
}

//IAsyncFatalErrorHandler implementation
void RemoteCharacterEmbodiment3DSpark::handleError(char* messageError){
	myWindowComponent->handleError(messageError); 
}

// IEventQueue implementation
void RemoteCharacterEmbodiment3DSpark::postEvent(psisban::Messages msg, void *data1, void *data2){
	myWindowComponent->postEvent(msg,data1,data2);
}
void RemoteCharacterEmbodiment3DSpark::sendEvent(psisban::Messages msg, void *data1, void *data2){
	myWindowComponent->sendEvent(msg,data1,data2);
}
void RemoteCharacterEmbodiment3DSpark::registerListener(psisban::Messages msg, EventCallback eventCallback){
	myWindowComponent->registerListener(msg,eventCallback);
}
void RemoteCharacterEmbodiment3DSpark::eventLoop(void){
	myWindowComponent->eventLoop();
}
//IAnimation implementation
void RemoteCharacterEmbodiment3DSpark::update(void){
	myScene3DComponent->update();
}

void RemoteCharacterEmbodiment3DSpark::playAnimation(char *animationFileName){
	myScene3DComponent->playAnimation(animationFileName);
}

//IFaceExpression implementation
void RemoteCharacterEmbodiment3DSpark::setFaceExpression(char *expressionName,float intensity){
	myScene3DComponent->setFaceExpression(expressionName,intensity);	
}

//IJoint implementation
void RemoteCharacterEmbodiment3DSpark::setJointTransMat(const char *jointName, const float *mat4x4){
	myScene3DComponent->setJointTransMat(jointName, mat4x4);
}
void RemoteCharacterEmbodiment3DSpark::rotateJointPart(char *jointName, float rotationX,	float rotationY, float rotationZ)//--Provisional Iñigo
{
	myScene3DComponent->rotateJointPart(jointName, rotationX, rotationY, rotationZ);
}
void RemoteCharacterEmbodiment3DSpark::moveJointPart(char *jointName, float translationX,	float translationY, float translationZ)
{
	myScene3DComponent->moveJointPart(jointName, translationX, translationY, translationZ);
}
void RemoteCharacterEmbodiment3DSpark::moveDiamondJoint(float translationX, float translationY, float translationZ)
{
	myScene3DComponent->moveDiamondJoint(translationX, translationY, translationZ);
}
void RemoteCharacterEmbodiment3DSpark::GetJointRotation(char *jointName, float *x, float *y, float *z)
{
	myScene3DComponent->GetJointRotation(jointName, *&x, *&y, *&z);
}
void RemoteCharacterEmbodiment3DSpark::GetJointPosition(char *jointName, float *x, float *y, float *z)
{
	myScene3DComponent->GetJointPosition(jointName, *&x, *&y, *&z);
}
bool RemoteCharacterEmbodiment3DSpark::findNode(const char *jointName){
	return myScene3DComponent->findNode(jointName);
}

//IEyes implementation
void RemoteCharacterEmbodiment3DSpark::rotateEye(float pan,float tilt){
	myScene3DComponent->rotateEye(pan,tilt);
}

void RemoteCharacterEmbodiment3DSpark::setBlinkLevel(float level){
	myScene3DComponent->setBlinkLevel(level);
}

//INeck implementation
void RemoteCharacterEmbodiment3DSpark::rotateHead(float pan, float tilt){
	myScene3DComponent->rotateHead(pan,tilt);
}

//ICamera
void RemoteCharacterEmbodiment3DSpark::setCameraPosition(float X,float Y,float Z)
{
	myScene3DComponent->setCameraPosition(X,Y,Z);
}
void RemoteCharacterEmbodiment3DSpark::setCameraRotation(float X,float Y,float Z)
{
	myScene3DComponent->setCameraRotation(X,Y,Z);
}
void RemoteCharacterEmbodiment3DSpark::setCameraParameters(bool isOrtho,float visionAngle,float nearClippingPlane,float farClippingPlane)
{
	myScene3DComponent->setCameraParameters(isOrtho,visionAngle,nearClippingPlane,farClippingPlane);
}

//IFrameEventPublisher implementation
void RemoteCharacterEmbodiment3DSpark::addFrameEventSubscriber(FrameEventSubscriber *frameEventSubscriber)
{
	myRemoteRendererComponent->addFrameEventSubscriber(frameEventSubscriber);
}

