/*
 * BlankSpark.h
 *
 *  Created on: 15/10/2012
 *      Author: guille
 */

/// @file BlankSpark.h
/// @brief Component BlankSpark main class.


#ifndef __BlankSpark_H
#define __BlankSpark_H


#include "Component.h"

// This Spark implements all interfaces, but it does nothing within that implementation.
// It's a dummy spark to connect and provide empty code to not-connected required interfaces
#include "INeck.h"
#include "IEyes.h"
#include "IFlow.h"
#include "ICamera.h"
#include "IFaceExpression.h"
#include "IAnimation.h"
#include "IRenderizable.h"
#include "IApplication.h"
#include "IAsyncFatalErrorHandler.h"
#include "IAudioQueue.h"
#include "IConcurrent.h"
#include "IControlVoice.h"
#include "IDetectedFacePositionConsumer.h"
#include "IFrameEventPublisher.h"
#include "FrameEventSubscriber.h"
#include "IThreadProc.h"
#include "IVoice.h"
#include "IWindow.h"
#include "IJoint.h"

/// @brief This is the main class for component BlankSpark.
///
/// 

class BlankSpark :
	public Component,
	public INeck,
	public IEyes,
	public IFlow<char*>,
	public IFlow<OutgoingImage*>,
	public IFlow<Image*>,
	public IFlow<AudioWrap*>,
	public IFlow<Json::Value*>,
	public IFlow<int>,
	public ICamera,
	public IFaceExpression,
	public IAnimation,
	public IRenderizable,
	public IApplication,
	public IAsyncFatalErrorHandler,
	public IAudioQueue,
	public IConcurrent,
	public IControlVoice,
	public IDetectedFacePositionConsumer,
	public IFrameEventPublisher,
	public FrameEventSubscriber,
	public IThreadProc,
	public IVoice,
	public IWindow,
	public IJoint
{
public:
		BlankSpark(
				char *instanceName,
				ComponentSystem *cs
		) : Component(instanceName, cs)
		{}

private:
	void initializeRequiredInterfaces() {
	}

public:
	//Mandatory
	void init(void);
	void quit(void);
	
	//INeck implementation
	void rotateHead(float pan, float tilt);
	//IFlow<char*> implementation
	void processData(char *);
	//IFlow<OutgoingImage*> implementation
	void processData(OutgoingImage *);
	//IFlow<Image*> implementation
	void processData(Image *);
	//IFlow<AudioWrap*> implementation
	void processData(AudioWrap *);
	//IFlow<Json::Value*> implementation
	void processData(Json::Value *);
	//IFlow<int> implementation
	void processData(int);
	//ICamera implementation
	void setCameraPosition(float X,float Y,float Z);
	void setCameraRotation(float X,float Y,float Z);
	void setCameraParameters(bool isOrtho, float visionAngle,float nearClippingPlane,float farClippingPlane);
	//IEyes implementation
	void rotateEye(float pan,float tilt);
	void setBlinkLevel(float blinkLevel);
	//IFaceExpression implementation
	void setFaceExpression(char *expressionName,float intensity);
	//IAnimation implementation
	void update();
	void playAnimation(char *animationFileName);
	//IRenderizable implementation
	void render(void);
	H3DRes getCamaraNode();
	//IApplication implementation
	void run();
	//IAsyncFatalErrorHandler implementation
	void handleError(char *msg);
	//IAudioQueue implementation
	int getStoredAudioSize();
	void queueAudioBuffer(char *buffer, int size);
	void dequeueAudioBuffer(char *buffer, int size);
	void reset();
	//IConcurrent implementation
	void start();
	void stop();
	//IControlVoice implementation
	void startSpeaking(void);
	void stopSpeaking(void);
	void startVoice(void);
	//IDetectedFacePositionConsumer implementation
	void consumeDetectedFacePosition(bool isFaceDetected, double x, double y);
	//IFrameEventPublisher implementation
	void addFrameEventSubscriber(FrameEventSubscriber *frameEventSubscriber);
	//FrameEventSubscriber implementation
	void notifyFrameEvent();
	//IThreadProc implementation
	void process();
	//IVoice implementation
	void sayThis(char *prompt);
	void waitEndOfSpeech(void);
	void stopSpeech(void);
	//IWindow implementation
	Display* getWindowDisplay(void);
	void show(void);
	void hide(void);
	int getColorDepth(void);
	void makeCurrentopenGlThread(void);
	void openGlSwapBuffers(void);
	//IJoint implementation
	void setJointTransMat(const char *jointName, const float *mat4x4);
	void rotateJointPart(char *jointName, float rotationX,	float rotationY, float rotationZ);//For general joint rotation
	void moveJointPart(char *jointName, float translationX,	float translationY, float translationZ);
	void moveDiamondJoint(float translationX, float translationY, float translationZ);//For Diamont joint movement
	void GetJointRotation(char *jointName, float *x, float *y, float *z);
	void GetJointPosition(char *jointName, float *x, float *y, float *z);
	bool findNode(const char *jointName);
	
};

#endif
