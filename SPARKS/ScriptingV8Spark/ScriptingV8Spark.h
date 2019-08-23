/*
 * ScriptingV8Spark.h
 *
 *  Created on: 12/11/2012
 *      Author: guille
 */
/// @file ScriptingV8Spark.h
/// @brief Component ScriptingV8Spark main class.


#ifndef __ScriptingV8Spark_H
#define __ScriptingV8Spark_H


#include "Component.h"
#include "IThreadProc.h"
#include "IFaceExpression.h"
#include "FrameEventSubscriber.h"
#include "IFlow.h"
#include "INeck.h"
#include "IEyes.h"
#include "ICamera.h"
#include "IControlVoice.h"
#include "IAnimation.h"

#include <stdlib.h>
#include <math.h>
/* v8 */
#include <v8.h>
/* v8-convert */
#include "cvv8/v8-convert.hpp"
#include "cvv8/V8Shell.hpp"

/* v8-convert addons*/
#include "time.hpp"
#include "socket.hpp"
#include "bytearray.hpp"
#include "ConvertDemo.hpp"
#include "jspdo.hpp"

#ifndef CERR
#define CERR std::cerr << __FILE__ << ":" << std::dec << __LINE__ << ":" <<__FUNCTION__ << "(): "
#endif

#ifndef COUT
#define COUT std::cout << __FILE__ << ":" << std::dec << __LINE__ << " : "
#endif

using namespace v8;
//namespace cv = cvv8;

/// @brief This is the main class for component ScriptingV8Spark.
///
/// 

class ScriptingV8Spark :
	public Component,

	public IFaceExpression,
	public FrameEventSubscriber,
	public	IThreadProc,
	public IFlow<char*>,
	public IControlVoice
{
public:
		ScriptingV8Spark(
				char *instanceName,
				ComponentSystem *cs
		) : Component(instanceName, cs)
		{
		}
		virtual ~ScriptingV8Spark(){};


		IFaceExpression *myFaceExpression;
		INeck *myNeck;
		IEyes *myEyes;
		ICamera *myCamera;
		IFlow<char*> *myFlow;
		IControlVoice *myControlVoice;
		IAnimation *myAnimation;


private:
	void initializeRequiredInterfaces() {
		requestRequiredInterface<IFaceExpression>(&myFaceExpression);
		requestRequiredInterface<INeck>(&myNeck);
		requestRequiredInterface<IEyes>(&myEyes);
		requestRequiredInterface<ICamera>(&myCamera);
		requestRequiredInterface<IFlow<char *> >(&myFlow);
		requestRequiredInterface<IControlVoice>(&myControlVoice);
		requestRequiredInterface<IAnimation>(&myAnimation);
	}

public:
	//Mandatory
	void init(void);
	void quit(void);
	
	//IThreadProc implementation
	void process();

	//FrameEventSubscriber
	void notifyFrameEvent();

	//IFaceExpression implementation
	void setFaceExpression(char *expressionName,float intensity);

	//IFlow<char*> implementation
	void processData(char *);

	//IControlVoice
	void startSpeaking();
	void stopSpeaking();
	void startVoice();

private:
	Handle<Context> context_;
	Handle<Object> global;

private:
	//Callbacks
	static Handle<Value> LogCallback(const Arguments& args);
	static Handle<Value> setFaceExpressionRequired(const Arguments& args);
	static Handle<Value> rotateEyeRequired(const Arguments& args);
	static Handle<Value> rotateHeadRequired(const Arguments& args);
	static Handle<Value> setCameraPositionRequired(const Arguments& args);
	static Handle<Value> setCameraRotationRequired(const Arguments& args);
	static Handle<Value> setCameraParametersRequired(const Arguments& args);
	static Handle<Value> processDataRequired(const Arguments& args);
	static Handle<Value> startSpeakingRequired(const Arguments& args);
	static Handle<Value> stopSpeakingRequired(const Arguments& args);
	static Handle<Value> startVoiceRequired(const Arguments& args);
	static Handle<Value> playAnimationRequired(const Arguments& args);

	static void Log(const char* event, int logLevel);
};


#endif
