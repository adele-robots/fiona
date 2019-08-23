/*
 * ScriptV8Spark.h
 *
 *  Created on: 15/11/2012
 *      Author: guille
 */

/// @file ScriptV8Spark.h
/// @brief Component ScriptV8Spark main class.


#ifndef __ScriptV8Spark_H
#define __ScriptV8Spark_H


#include "Component.h"
#include <string>
#include "IThreadProc.h"
#include "IFaceExpression.h"
#include "ICamera.h"
#include "INeck.h"
#include "IEyes.h"
#include "IFlow.h"
#include "IControlVoice.h"
#include "IAnimation.h"
#include "IJoint.h"

#include "FrameEventSubscriber.h"

#include <stdlib.h>
#include <v8.h>

//includes for alert function
/*
#include <gtkmm.h>
#include <libnotify/notify.h>
*/

using namespace v8;

/// @brief This is the main class for component ScriptV8Spark.
///
/// 

class ScriptV8Spark :
	public Component,

	public	IThreadProc,
	public IFaceExpression,
	public ICamera,
	public FrameEventSubscriber,
	public IFlow<char*>,
	public IControlVoice
{
public:
		ScriptV8Spark(
				char *instanceName,
				ComponentSystem *cs
		) : Component(instanceName, cs)
		{}
		virtual ~ScriptV8Spark(){};

		IFaceExpression *myFaceExpression;
		ICamera *myCamera;
		INeck *myNeck;
		IEyes *myEyes;
		IFlow<char*> *myFlow;
		IControlVoice *myControlVoice;
		IAnimation *myAnimation;
		IJoint *myJoint;

private:
	void initializeRequiredInterfaces() {
		requestRequiredInterface<IFaceExpression>(&myFaceExpression);
		requestRequiredInterface<ICamera>(&myCamera);
		requestRequiredInterface<INeck>(&myNeck);
		requestRequiredInterface<IEyes>(&myEyes);
		requestRequiredInterface<IFlow<char *> >(&myFlow);
		requestRequiredInterface<IControlVoice>(&myControlVoice);
		requestRequiredInterface<IAnimation>(&myAnimation);
		requestRequiredInterface<IJoint>(&myJoint);
	}
public:
	//Mandatory
	void init(void);
	void quit(void);
	
	//IThreadProc implementation
	void process();

	//IFaceExpression implementation
	void setFaceExpression(char *expressionName,float intensity);
	
	//ICamera
	void setCameraPosition(float X,float Y,float Z);
	void setCameraRotation(float X,float Y,float Z);
	void setCameraParameters(bool isOrtho, float visionAngle,float nearClippingPlane,float farClippingPlane);

	//FrameEventSubscriber
	void notifyFrameEvent();

	//IFlow<char*> implementation
	void processData(char *);

	//IControlVoice
	void startSpeaking();
	void stopSpeaking();
	void startVoice();

private:
	//Put class attributes here
	Persistent<Context> context_;
	Handle<String> script_;

	Persistent<Function> setFaceExpression_;
	Persistent<Function> setCameraPosition_;
	Persistent<Function> setCameraRotation_;
	Persistent<Function> setCameraParameters_;
	Persistent<Function> notifyFrameEvent_;
	Persistent<Function> processData_;
	Persistent<Function> startSpeaking_;
	Persistent<Function> stopSpeaking_;
	Persistent<Function> startVoice_;

	static Persistent<ObjectTemplate> sc_template_;
	static Persistent<ObjectTemplate> char_template_;
private:

	static Handle<Value> LogCallback(const Arguments& args);
	static Handle<Value> PrintCallback(const Arguments& args);
	static Handle<Value> AlertCallback(const Arguments& args);
	static Handle<Value> SleepCallback(const Arguments& args);

	//Callbacks to call interfaces
	static Handle<Value> setFaceExpressionRequiredCallback(const Arguments& args);
	static Handle<Value> setCameraPositionRequiredCallback(const Arguments& args);
	static Handle<Value> setCameraRotationRequiredCallback(const Arguments& args);
	static Handle<Value> setCameraParametersRequiredCallback(const Arguments& args);
	static Handle<Value> rotateHeadRequiredCallback(const Arguments& args);
	static Handle<Value> rotateEyeRequiredCallback(const Arguments& args);
	static Handle<Value> processDataRequiredCallback(const Arguments& args);
	static Handle<Value> startSpeakingRequiredCallback(const Arguments& args);
	static Handle<Value> stopSpeakingRequiredCallback(const Arguments& args);
	static Handle<Value> startVoiceRequiredCallback(const Arguments& args);
	static Handle<Value> playAnimationRequiredCallback(const Arguments& args);
	static Handle<Value> setJointTransMatRequiredCallback(const Arguments& args);
	static Handle<Value> rotateJointPartRequiredCallback(const Arguments& args);
	static Handle<Value> moveJointPartRequiredCallback(const Arguments& args);
	static Handle<Value> moveDiamondJointRequiredCallback(const Arguments& args);
	static Handle<Value> GetJointRotationRequiredCallback(const Arguments& args);
	static Handle<Value> GetJointPositionRequiredCallback(const Arguments& args);
	static Handle<Value> findNodeRequiredCallback(const Arguments& args);

	static void Log(const char* event, int logLevel);
	Handle<String> ReadFile(const string& name);
	bool ExecuteScript(Handle<String> script);
	Persistent<Function> getFunction(char *functionName);
};

#endif
