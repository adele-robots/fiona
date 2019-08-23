/// @file TestScripting.h
/// @brief Component TestScripting main class.

#ifndef TESTSCRIPTING_H_
#define TESTSCRIPTING_H_


#include <iostream>
using namespace std;

#include "Component.h"
#include "Component.h"
#include "IAnimation.h"
#include "IApplication.h"
#include "IAsyncFatalErrorHandler.h"
#include "IAudioConsumer.h"
#include "IAudioQueue.h"
#include "ICamera.h"
#include "IConcurrent.h"
#include "IControlVoice.h"
#include "IDetectedFacePositionConsumer.h"
#include "IEventQueue.h"
#include "IEyes.h"
#include "IFaceExpression.h"
#include "IFrameEventPublisher.h"
#include "FrameEventSubscriber.h"
#include "INeck.h"
#include "IRenderizable.h"
#include "IThreadProc.h"
#include "IVideoConsumer.h"
#include "IVideoConsumer2.h"
#include "IVoice.h"
#include "IWindow.h"

/// @brief This is the main class for component TestScripting.
///
///

class TestScripting :
	public Component,
	public IApplication,
	public IAnimation,
	public IAsyncFatalErrorHandler,
	public IAudioQueue,
	public ICamera,
	public IConcurrent,
	public IControlVoice,
	public IDetectedFacePositionConsumer,
	public IEyes,
	public IFaceExpression,
	public IFrameEventPublisher,
	public FrameEventSubscriber,
	public	INeck,
	public	IRenderizable,
	public	IThreadProc,
	public	IVoice,
	public	IWindow
{
public:
		TestScripting(
				char *instanceName,
				ComponentSystem *cs
		) : Component(instanceName, cs)
		{}

private:
	IApplication *myApplication;
	IAnimation *myAnimation;
	IAsyncFatalErrorHandler *myAsyncFatalErrorHandler;
	IAudioQueue *myAudioQueue;
	ICamera *myCamera;
	IConcurrent *myConcurrent;
	IControlVoice *myControlVoice;
	IDetectedFacePositionConsumer *myDetectedFacePositionConsumer;
	IEyes *myEyes;
	IFaceExpression *myFaceExpression;
	IFrameEventPublisher *myFrameEventPublisher;
	FrameEventSubscriber *myFrameEventSubscriber;
	INeck *myNeck;
	IRenderizable *myRenderizable;
	IThreadProc *myThreadProc;
	IVoice *myVoice;
	IWindow *myWindow;


	void initializeRequiredInterfaces() {
		requestRequiredInterface<IAnimation>(&myAnimation);
		requestRequiredInterface<IApplication>(&myApplication);
		requestRequiredInterface<IAsyncFatalErrorHandler>(&myAsyncFatalErrorHandler);
		requestRequiredInterface<IAudioQueue>(&myAudioQueue);
		requestRequiredInterface<ICamera>(&myCamera);
		requestRequiredInterface<IConcurrent>(&myConcurrent);
		requestRequiredInterface<IControlVoice>(&myControlVoice);
		requestRequiredInterface<IDetectedFacePositionConsumer>(&myDetectedFacePositionConsumer);
		requestRequiredInterface<IEyes>(&myEyes);
		requestRequiredInterface<IFaceExpression>(&myFaceExpression);
		requestRequiredInterface<IFrameEventPublisher>(&myFrameEventPublisher);
		requestRequiredInterface<FrameEventSubscriber>(&myFrameEventSubscriber);
		requestRequiredInterface<INeck>(&myNeck);
		requestRequiredInterface<IRenderizable>(&myRenderizable);
		requestRequiredInterface<IThreadProc>(&myThreadProc);
		requestRequiredInterface<IVoice>(&myVoice);
		requestRequiredInterface<IWindow>(&myWindow);
	}
public:
	//Mandatory
	void init(void);
	void quit(void);

	//Implementation of IAnimation
	void playAnimation(char *animationFileName);

	//Implementation of IApplication
	void run(void);

	//IAsyncFatalErrorHandler implementation
	void handleError(char *msg);

	//IAudioQueue implementation
	int getStoredAudioSize();
	void dequeueAudioBuffer(char *buffer, int size);

	//ICamera implementation
	void setCameraPosition(float X,float Y,float Z);
	void setCameraRotation(float X,float Y,float Z);
	void setCameraParameters(bool IsOrtho,float VisionAngle, float nearClippingPlane,float FarClippingPlane);

	//IConcurrent implementation
	void start();
	void stop();

	//IControlVoice implementation
	void startSpeaking(void);
	void stopSpeaking(void);
	void startVoice(void);

	//IDetectedFacePositionConsumer implementation
	void consumeDetectedFacePosition(bool isFaceDetected, double x, double y);

	//IEyes implementation
	void rotateEye(float pan,float tilt);

	//IFaceExpression implementation
	void setFaceExpression(char *expressionName,float intensity);

	//IFrameEventPublisher implementation
	void addFrameEventSubscriber(FrameEventSubscriber *frameEventSubscriber);

	//FrameEventSubscriber
	void notifyFrameEvent();

	//INeck implementation
	void rotateHead(float pan, float tilt);

	//IRenderizable implementation
	void render(void);
	H3DRes getCamaraNode();

	//IThreadProc
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



private:
	//Put class attributes here
	bool running;
private:
	//Put class private methods here

};

#endif /* TESTSCRIPTING_H_ */
