#ifndef __ScriptingSpark_H
#define __ScriptingSpark_H

#include "ScriptingGeneral.h"

#include "Component.h"
#include "IAnimation.h"
#include "IApplication.h"
#include "IAsyncFatalErrorHandler.h"
#include "IAudioQueue.h"
#include "ICamera.h"
#include "IConcurrent.h"
#include "IControlVoice.h"
#include "IDetectedFacePositionConsumer.h"
#include "IEyes.h"
#include "IFaceExpression.h"
#include "IFrameEventPublisher.h"
#include "FrameEventSubscriber.h"

#include "INeck.h"
#include "IRenderizable.h"
#include "IThreadProc.h"
#include "IVoice.h"
#include "IWindow.h"


#include <v8.h>
#include <string>
#include <map>
#include <stdlib.h>
using namespace std;
using namespace v8;

class ScriptingSpark:
	public Component,
	public IAnimation,
	public	IApplication,
	public	IAsyncFatalErrorHandler,
	public	IAudioQueue,
	public	ICamera,
	public	IConcurrent,
	public	IControlVoice,
	public	IDetectedFacePositionConsumer,
	public	IEyes,
	public	IFaceExpression,
	public IFrameEventPublisher,
	public FrameEventSubscriber,
	public	INeck,
	public	IRenderizable,
	public	IThreadProc,
	public	IVoice,
	public	IWindow
{
public:
	ScriptingSpark(
			char *instanceName,
			ComponentSystem *cs
			) : Component(instanceName, cs)
	{}

	void init(void);

	void quit(void);

	//IAnimation implementation
	void  playAnimation(char *animationFileName);

	//IApplication implementation
	void run();

	//IAsyncFatalErrorHandler implementation
	void handleError(char *msg);

	//IAudioQueue implementation
	int getStoredAudioSize();
	void dequeueAudioBuffer(char *buffer, int size);
	void queueAudioBuffer(char *buffer, int size);

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

	//IFrameEventSubscriber implementation
	void notifyFrameEvent();

	//INeck implementation
	void rotateHead(float pan, float tilt);

	//IRenderizable implementation
	void render(void);
	H3DRes getCamaraNode();

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

	Persistent<v8::Context> getContext();

	void setContext(Persistent<v8::Context> contextPasado_);

private:
	v8::Handle<String> script_;

	static Persistent<v8::Context> context_;

	static Persistent<ObjectTemplate> sc_template_;

	void initializeRequiredInterfaces(){
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
	};

	bool ExecuteScript(v8::Handle<String> script);


public:
	static IAnimation *myAnimation;
	static IApplication *myApplication;
	static IAsyncFatalErrorHandler *myAsyncFatalErrorHandler;
	static IAudioQueue *myAudioQueue;
	static ICamera *myCamera;
	static IConcurrent *myConcurrent;
	static IControlVoice *myControlVoice;
	static IDetectedFacePositionConsumer *myDetectedFacePositionConsumer;
	static IEyes *myEyes;
	static IFaceExpression *myFaceExpression;
	static IFrameEventPublisher *myFrameEventPublisher;
	static FrameEventSubscriber *myFrameEventSubscriber;
	static INeck *myNeck;
	static IRenderizable *myRenderizable;
	static IThreadProc *myThreadProc;
	static IVoice *myVoice;
	static IWindow *myWindow;

};

#endif
