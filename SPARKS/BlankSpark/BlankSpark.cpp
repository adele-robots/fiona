/*
 * BlankSpark.cpp
 *
 *  Created on: 15/10/2012
 *      Author: guille
 */

/// @file BlankSpark.cpp
/// @brief BlankSpark class implementation.


//#include "stdAfx.h"
#include "BlankSpark.h"


#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "BlankSpark")) {
			return new BlankSpark(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif

/// Initializes BlankSpark component.
void BlankSpark::init(void){
}

/// Unitializes the BlankSpark component.
void BlankSpark::quit(void){
}

//INeck implementation
void BlankSpark::rotateHead(float pan, float tilt){
	//puts("BlankSpark::rotateHead called!");
}
//IFlow<char*> implementation
void BlankSpark::processData(char *){
	//puts("BlankSpark::processData with char* called!");
}
//IFlow<OutgoingImage*> implementation
void BlankSpark::processData(OutgoingImage *){
	//puts("BlankSpark::processData with OutgoingImage* called!");
}
//IFlow<Image*> implementation
void BlankSpark::processData(Image *){
	//puts("BlankSpark::processData with Image* called!");
}
//IFlow<AudioWrap*> implementation
void BlankSpark::processData(AudioWrap *){
	//puts("BlankSpark::processData with AudioWrap* called!");
}
//IFlow<Json::Value*> implementation
void BlankSpark::processData(Json::Value *){
	//puts("BlankSpark::processData with Json::Value* called!");
}
//IFlow<int> implementation
void BlankSpark::processData(int){
	//puts("BlankSpark::processData with int called!");
}
//IEyes implementation
void BlankSpark::rotateEye(float pan,float tilt){
	//puts("BlankSpark::rotateEye called!");
}

void BlankSpark::setBlinkLevel(float blinkLevel){
	//puts("BlankSpark::setBlinkLevel called!");
}
//ICamera implementation
void BlankSpark::setCameraPosition(float X,float Y,float Z){
	//puts("BlankSpark::setCameraPosition called!");
}

void BlankSpark::setCameraRotation(float X,float Y,float Z){
	//puts("BlankSpark::setCameraRotation called!");
}

void BlankSpark::setCameraParameters(bool isOrtho, float visionAngle,float nearClippingPlane,float farClippingPlane){
	//puts("BlankSpark::setCameraParameters called!");
}
//IFaceExpression implementation
void BlankSpark::setFaceExpression(char *expressionName,float intensity){
	//puts("BlankSpark::setFaceExpression called!");
}
//IAnimation implementation
void BlankSpark::update(void){
	//puts("BlankSpark::update called!");
}

void BlankSpark::playAnimation(char *animationFileName){
	//puts("BlankSpark::playAnimation called!");
}
//IRenderizable implementation
void BlankSpark::render(void){
	//puts("BlankSpark::render called!");
}

H3DRes BlankSpark::getCamaraNode(){
	//puts("BlankSpark::getCamaraNode called!");
}
//IApplication implementation
void BlankSpark::run(void){
	//puts("BlankSpark::run called!");
}
//IAsyncFatalErrorHandler implementation
void BlankSpark::handleError(char *msg){
	//puts("BlankSpark::handleError called!");
}
//IAudioQueue implementation
int BlankSpark::getStoredAudioSize(){
	//puts("BlankSpark::getStoredAudioSize called!");
	return 0;
}

void BlankSpark::queueAudioBuffer(char *buffer, int size){
	//puts("BlankSpark::queueAudioBuffer called!");
}

void BlankSpark::dequeueAudioBuffer(char *buffer, int size){
	//puts("BlankSpark::dequeueAudioBuffer called!");
}

void BlankSpark::reset(){
	//puts("BlankSpark::reset called!");
}

//IConcurrent implementation
void BlankSpark::start(){
	//puts("BlankSpark::start called!");
}

void BlankSpark::stop(){
	//puts("BlankSpark::stop called!");
}
//IControlVoice implementation
void BlankSpark::startSpeaking(void){
	//puts("BlankSpark::startSpeaking called!");
}

void BlankSpark::stopSpeaking(void){
	//puts("BlankSpark::stopSpeaking called!");
}

void BlankSpark::startVoice(void){
	//puts("BlankSpark::startVoice called!");
}
//IDetectedFacePositionConsumer implementation
void BlankSpark::consumeDetectedFacePosition(bool isFaceDetected, double x, double y){
	//puts("BlankSpark::consumeDetectedFacePosition called!");
}
//IFrameEventPublisher implementation
void BlankSpark::addFrameEventSubscriber(FrameEventSubscriber *frameEventSubscriber){
	//puts("BlankSpark::addFrameEventSubscriber called!");
}
//FrameEventSubscriber implementation
void BlankSpark::notifyFrameEvent(){
	//puts("BlankSpark::notifyFrameEvent called!");
}
//IThreadProc implementation
void BlankSpark::process(){
	//puts("BlankSpark::process called!");
}
//IVoice implementation
void BlankSpark::sayThis(char *prompt){
	//puts("BlankSpark::sayThis called!");
}
void BlankSpark::waitEndOfSpeech(void){
	//puts("BlankSpark::waitEndOfSpeech called!");
}
void BlankSpark::stopSpeech(void){
	//puts("BlankSpark::stopSpeech called!");
}
//IWindow implementation
Display* BlankSpark::getWindowDisplay(void){
	//puts("BlankSpark::getWindowDisplay called!");
}

void BlankSpark::show(void){
	//puts("BlankSpark::show called!");
}

void BlankSpark::hide(void){
	//puts("BlankSpark::hide called!");
}

int BlankSpark::getColorDepth(void){
	//puts("BlankSpark::getColorDepth called!");
}

void BlankSpark::makeCurrentopenGlThread(void){
	//puts("BlankSpark::makeCurrentopenGlThread called!");
}

void BlankSpark::openGlSwapBuffers(void){
	//puts("BlankSpark::openGlSwapBuffers called!");
}

//IJoint implementation
void BlankSpark::setJointTransMat(const char *jointName, const float *mat4x4){
}

void BlankSpark::rotateJointPart(char *jointName, float rotationX,	float rotationY, float rotationZ){
}

void BlankSpark::moveJointPart(char *jointName, float translationX,	float translationY, float translationZ){
}

void BlankSpark::moveDiamondJoint(float translationX, float translationY, float translationZ){
}

void BlankSpark::GetJointRotation(char *jointName, float *x, float *y, float *z){
}

void BlankSpark::GetJointPosition(char *jointName, float *x, float *y, float *z){
}

bool BlankSpark::findNode(const char *jointName){
}
