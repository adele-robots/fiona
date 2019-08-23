/*
 * ScriptingV8Spark.cpp
 *
 *  Created on: 12/11/2012
 *      Author: guille
 */

/// @file ScriptingV8Spark.cpp
/// @brief ScriptingV8Spark class implementation.


//#include "stdAfx.h"
#include "ScriptingV8Spark.h"


#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "ScriptingV8Spark")) {
			return new ScriptingV8Spark(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif

bool firstTime = true;

/// Initializes ScriptingV8Spark component.
void ScriptingV8Spark::init(void){
	LoggerInfo("[FIONA-logger]ScriptingV8Spark::init | Initializing...\n");
}

void ScriptingV8Spark::process() {
	//static bool firstTime = true;
	/*
	 * This method (process) is being continuosly called.
	 * Code in following scope is only executed once.
	 */
	if (firstTime) {
		firstTime = false;

		/* Load script filename */
		string scriptFilename(
				getComponentConfiguration()->getString(const_cast<char*>("User_Spark_Data"))+
				getComponentConfiguration()->getString(const_cast<char*>("Script_Filename"))
				);

		int argc = 2;
		char const * argv[] = {"ScriptingV8Spark",scriptFilename.c_str()};
		try{
		/* Instantiate a V8Shell that makes v8 initializations (create a context, global object template, etc) */
		cvv8::Shell shell(NULL,argc,argv);

		/* Save context and global Handles. Do these need to be Persistent? */
		global = shell.Global();
		context_ = shell.Context();

		/* Setup basic functionality in the shell's Global() object ('print', 'getStacktrace' and 'load') */
		shell.SetupDefaultBindings();

		/**
		 * Behind the scenes, we are calling the V8Shell operator(), which adds the given function to the global object.
		 * It's the same as making the following call in the case of 'setFaceExpressionRequired' which needs to pass 'this' object:
		 * global->Set(String::New("setFaceExpressionRequired"), FunctionTemplate::New(setFaceExpressionRequired,External::New(this))->GetFunction());
		 */
		shell("log",LogCallback)
			 ("setFaceExpressionRequired",FunctionTemplate::New(setFaceExpressionRequired,External::New(this))->GetFunction())
			 ("rotateEyeRequired",FunctionTemplate::New(rotateEyeRequired,External::New(this))->GetFunction())
			 ("rotateHeadRequired",FunctionTemplate::New(rotateHeadRequired,External::New(this))->GetFunction())
			 ("setCameraPositionRequired",FunctionTemplate::New(setCameraPositionRequired,External::New(this))->GetFunction())
			 ("setCameraRotationRequired",FunctionTemplate::New(setCameraRotationRequired,External::New(this))->GetFunction())
			 ("setCameraParametersRequired",FunctionTemplate::New(setCameraParametersRequired,External::New(this))->GetFunction())
			 ("processDataRequired",FunctionTemplate::New(processDataRequired,External::New(this))->GetFunction())
			 ("startSpeakingRequired",FunctionTemplate::New(startSpeakingRequired,External::New(this))->GetFunction())
			 ("stopSpeakingRequired",FunctionTemplate::New(stopSpeakingRequired,External::New(this))->GetFunction())
			 ("startVoiceRequired",FunctionTemplate::New(startVoiceRequired,External::New(this))->GetFunction())
			 ("playAnimationRequired",FunctionTemplate::New(playAnimationRequired,External::New(this))->GetFunction());

		try {
			{
				cvv8::time::SetupBindings(global);
				cvv8::JSSocket::SetupBindings(global);
				cvv8::JSByteArray::SetupBindings(global);
				cvv8::JSPDO::SetupBindings(global);
			}
			char const * fname = argv[1];
			v8::Handle<v8::Value> rc( shell.ExecuteFile( fname ));
			if( rc.IsEmpty() ) { // exception was reported by shell already
				LoggerInfo("[FIONA-logger]-Problems during script execution");
				return;
			}
		}
		catch(std::exception const & ex) {
			LoggerError("[FIONA-logger]Caught a std::exception: %s",ex.what());
			std::cerr << "Caught a std::exception: " << ex.what() << '\n';
			return;
		}
		catch(...) {
			LoggerInfo("[FIONA-logger]A non-std::exception was thrown! Srsly.");
			std::cerr << "A non-std::exception was thrown! Srsly.\n";
			return;
		}
		//***Revisar para quitar dejar solo un try-catch
		}catch(std::exception const & ex) {
			LoggerError("[FIONA-logger]Caught a std::exception: %s",ex.what());
			std::cerr << "Caught a std::exception: " << ex.what() << '\n';
			return;
		}
		catch(...) {
			LoggerInfo("[FIONA-logger]A non-std::exception was thrown! Srsly.");
			std::cerr << "A non-std::exception was thrown! Srsly.\n";
			return;
		}
	}
}

/// Unitializes the ScriptingV8Spark component.
void ScriptingV8Spark::quit(void){
	// Dispose the persistent handles.  When none else has any
	// references to the objects stored in the handles they will be
	// automatically reclaimed.

	/** !!!!!!!!!!!!!!!!!!! HAY QUE LIMPIAR MÁS COSAS !!!!!!!!!!!!!!!!!!!!! **/
	//context_.Dispose();
}


/**
 * Callbacks called from JS
 */

Handle<Value> ScriptingV8Spark::LogCallback(const Arguments& args) {
	Locker locker;
	if (args.Length() < 2) {
		Log("LogCallback called with insufficient arguments", 3);
		return Undefined();
	}
	HandleScope scope;
	Handle<Value> arg0 = args[0];
	String::Utf8Value value(arg0);
	Handle<Value> arg1 = args[1];
	int logLevel = static_cast<int>(arg1->NumberValue());

	ScriptingV8Spark::Log(*value, logLevel);
	return Undefined();
}

void ScriptingV8Spark::Log(const char* event, int logLevel) {
	std::string message("[FIONA-logger]");
	message = message + event;
	switch (logLevel) {
	case 1:
		LoggerInfo(message.c_str());
		break;
	case 2:
		LoggerWarn(message.c_str());
		break;
	case 3:
		LoggerError(message.c_str());
		break;
	}
}

// IFaceExpression
Handle<Value> ScriptingV8Spark::setFaceExpressionRequired(const Arguments& args) {
	//Locker locker;
	if (args.Length() < 2)
		return Undefined();
	HandleScope scope;

	Local<Value> const jvself(args.Data());
	if( jvself.IsEmpty() || !jvself->IsExternal() )
	{
		return v8::ThrowException(v8::Exception::Error(v8::String::New("Callback is missing its native V8Shell object.")));
	}
	ScriptingV8Spark * self = static_cast<ScriptingV8Spark *>( v8::External::Cast(*jvself)->Value() );


	Handle<Value> arg0 = args[0];
	String::Utf8Value val0(arg0);
	Handle<Value> arg1 = args[1];
	float val1 = static_cast<float>(arg1->NumberValue());

	/* Check if passed arguments are numbers */
	if (!isnanf(val1)){
		/* Make the native call */
		try{
			self->myFaceExpression->setFaceExpression(*val0, val1);
		}catch(std::exception const & ex) {
			LoggerError("[FIONA-logger]setFaceExpression | Caught a std::exception: %s",ex.what());
		}catch(::Exception const & ex){
			LoggerError("[FIONA-logger]Caught an Exception: %s", ex.msg);
		}catch(...){
			LoggerError("[FIONA-logger]setFaceExpression | Unknown exception caught");
		}
	}else{
		LoggerError("[FIONA-logger]setFaceExpression | Intensity must be a float number");
	}

	return Undefined();
}

// IEyes
Handle<Value> ScriptingV8Spark::rotateEyeRequired(const Arguments& args) {
	//Locker locker;
	if (args.Length() < 2){
		Log("rotateEye called with insufficient arguments", 3);
		return Undefined();
	}
	HandleScope scope;

	/* Retrieve 'this' pointer */
	Local<Value> const jvself(args.Data());
	if( jvself.IsEmpty() || !jvself->IsExternal() )
	{
		return v8::ThrowException(v8::Exception::Error(v8::String::New("Callback is missing its native V8Shell object.")));
	}
	ScriptingV8Spark * self = static_cast<ScriptingV8Spark *>( v8::External::Cast(*jvself)->Value() );

	/* Read arguments from JavaScript call */
	Handle<Value> arg0 = args[0];
	float pan = static_cast<float>(arg0->NumberValue());
	Handle<Value> arg1 = args[1];
	float tilt = static_cast<float>(arg1->NumberValue());

	/* Check if passed arguments are numbers */
	if (!isnanf(pan) && !isnanf(tilt)){
		/* Make the native call */
		try{
			self->myEyes->rotateEye(pan,tilt);
		}catch(std::exception const & ex) {
			LoggerError("[FIONA-logger]rotateEye | Caught a std::exception: %s",ex.what());
		}catch(::Exception const & ex){
			LoggerError("[FIONA-logger]Caught an Exception: %s", ex.msg);
		}catch(...){
			LoggerError("[FIONA-logger]rotateEye | Unknown exception caught");
		}
	}else{
		LoggerError("[FIONA-logger]rotateEye | One of the arguments is NaN");
	}

	return Undefined();
}

// INeck
Handle<Value> ScriptingV8Spark::rotateHeadRequired(const Arguments& args) {
	//Locker locker;
	if (args.Length() < 2){
		Log("rotateHead called with insufficient arguments", 3);
		return Undefined();
	}
	HandleScope scope;

	/* Retrieve 'this' pointer */
	Local<Value> const jvself(args.Data());
	if( jvself.IsEmpty() || !jvself->IsExternal() )
	{
		return v8::ThrowException(v8::Exception::Error(v8::String::New("Callback is missing its native V8Shell object.")));
	}
	ScriptingV8Spark * self = static_cast<ScriptingV8Spark *>( v8::External::Cast(*jvself)->Value() );

	/* Read arguments from JavaScript call */
	Handle<Value> arg0 = args[0];
	float pan = static_cast<float>(arg0->NumberValue());
	Handle<Value> arg1 = args[1];
	float tilt = static_cast<float>(arg1->NumberValue());

	/* Check if passed arguments are numbers */
	if (!isnanf(pan) && !isnanf(tilt)){
		/* Make the native call */
		try{
			self->myNeck->rotateHead(pan,tilt);
		}catch(std::exception const & ex) {
			LoggerError("[FIONA-logger]rotateHead | Caught a std::exception: %s",ex.what());
		}catch(::Exception const & ex){
			LoggerError("[FIONA-logger]Caught an Exception: %s", ex.msg);
		}catch(...){
			LoggerError("[FIONA-logger]rotateHead | Unknown exception caught");
		}
	}else{
		LoggerError("[FIONA-logger]rotateHead | One of the arguments is NaN");
	}

	return Undefined();
}

// ICamera
Handle<Value> ScriptingV8Spark::setCameraPositionRequired(const Arguments& args) {
	//Locker locker;
	if (args.Length() < 3){
		Log("setCameraPosition called with insufficient arguments", 3);
		return Undefined();
	}
	HandleScope scope;

	/* Retrieve 'this' pointer */
	Local<Value> const jvself(args.Data());
	if( jvself.IsEmpty() || !jvself->IsExternal() )
	{
		return v8::ThrowException(v8::Exception::Error(v8::String::New("Callback is missing its native V8Shell object.")));
	}
	ScriptingV8Spark * self = static_cast<ScriptingV8Spark *>( v8::External::Cast(*jvself)->Value() );

	/* Read arguments from JavaScript call */
	Handle<Value> arg0 = args[0];
	float x = static_cast<float>(arg0->NumberValue());
	Handle<Value> arg1 = args[1];
	float y = static_cast<float>(arg1->NumberValue());
	Handle<Value> arg2 = args[2];
	float z = static_cast<float>(arg2->NumberValue());

	/* Check if passed arguments are numbers */
	if (!isnanf(x) && !isnanf(y) && !isnanf(z)){
		/* Make the native call */
		try{
			self->myCamera->setCameraPosition(x,y,z);
		}catch(std::exception const & ex) {
			LoggerError("[FIONA-logger]setCameraPosition | Caught a std::exception: %s",ex.what());
		}catch(::Exception const & ex){
			LoggerError("[FIONA-logger]Caught an Exception: %s", ex.msg);
		}catch(...){
			LoggerError("[FIONA-logger]setCameraPosition | Unknown exception caught");
		}
	}else{
		LoggerError("[FIONA-logger]setCameraPosition | One of the arguments is NaN");
	}

	return Undefined();
}

Handle<Value> ScriptingV8Spark::setCameraRotationRequired(const Arguments& args) {
	//Locker locker;
	if (args.Length() < 3){
		Log("setCameraRotation called with insufficient arguments", 3);
		return Undefined();
	}
	HandleScope scope;

	/* Retrieve 'this' pointer */
	Local<Value> const jvself(args.Data());
	if( jvself.IsEmpty() || !jvself->IsExternal() )
	{
		return v8::ThrowException(v8::Exception::Error(v8::String::New("Callback is missing its native V8Shell object.")));
	}
	ScriptingV8Spark * self = static_cast<ScriptingV8Spark *>( v8::External::Cast(*jvself)->Value() );

	/* Read arguments from JavaScript call */
	Handle<Value> arg0 = args[0];
	float x = static_cast<float>(arg0->NumberValue());
	Handle<Value> arg1 = args[1];
	float y = static_cast<float>(arg1->NumberValue());
	Handle<Value> arg2 = args[2];
	float z = static_cast<float>(arg2->NumberValue());

	/* Check if passed arguments are numbers */
	if (!isnanf(x) && !isnanf(y) && !isnanf(z)){
		/* Make the native call */
		try{
			self->myCamera->setCameraRotation(x,y,z);
		}catch(std::exception const & ex) {
			LoggerError("[FIONA-logger]setCameraRotation | Caught a std::exception: %s",ex.what());
		}catch(::Exception const & ex){
			LoggerError("[FIONA-logger]Caught an Exception: %s", ex.msg);
		}catch(...){
			LoggerError("[FIONA-logger]setCameraRotation | Unknown exception caught");
		}
	}else{
		LoggerError("[FIONA-logger]setCameraRotation | One of the arguments is NaN");
	}

	return Undefined();
}

Handle<Value> ScriptingV8Spark::setCameraParametersRequired(const Arguments& args) {
	//Locker locker;
	if (args.Length() < 4){
		Log("setCameraParameters called with insufficient arguments", 3);
		return Undefined();
	}
	HandleScope scope;

	/* Retrieve 'this' pointer */
	Local<Value> const jvself(args.Data());
	if( jvself.IsEmpty() || !jvself->IsExternal() )
	{
		return v8::ThrowException(v8::Exception::Error(v8::String::New("Callback is missing its native V8Shell object.")));
	}
	ScriptingV8Spark * self = static_cast<ScriptingV8Spark *>( v8::External::Cast(*jvself)->Value() );

	/* Read arguments from JavaScript call */
	Handle<Value> arg0 = args[0];
	bool IsOrtho = (arg0->BooleanValue());
	Handle<Value> arg1 = args[1];
	float VisionAngle = static_cast<float>(arg1->NumberValue());
	Handle<Value> arg2 = args[2];
	float nearClippingPlane = static_cast<float>(arg2->NumberValue());
	Handle<Value> arg3 = args[3];
	float FarClippingPlane = static_cast<float>(arg3->NumberValue());

	/* Make the native call */
	try{
		self->myCamera->setCameraParameters(IsOrtho,VisionAngle,nearClippingPlane,FarClippingPlane);
	}catch(std::exception const & ex) {
		LoggerError("[FIONA-logger]setCameraParameters | Caught a std::exception: %s",ex.what());
	}catch(::Exception const & ex){
		LoggerError("[FIONA-logger]Caught an Exception: %s", ex.msg);
	}catch(...){
		LoggerError("[FIONA-logger]setCameraParameters | Unknown exception caught");
	}

	return Undefined();
}

// IFlow<char*>
Handle<Value> ScriptingV8Spark::processDataRequired(const Arguments& args) {
	//Locker locker;
	if (args.Length() < 1){
		Log("processData called with insufficient arguments", 3);
		return Undefined();
	}
	HandleScope scope;

	Local<Value> const jvself(args.Data());
	if( jvself.IsEmpty() || !jvself->IsExternal() )
	{
		return v8::ThrowException(v8::Exception::Error(v8::String::New("Callback is missing its native V8Shell object.")));
	}
	ScriptingV8Spark * self = static_cast<ScriptingV8Spark *>( v8::External::Cast(*jvself)->Value() );


	Handle<Value> arg0 = args[0];
	String::Utf8Value msg(arg0);

	/* Make the native call */
	try{
		self->myFlow->processData(*msg);
	}catch(std::exception const & ex) {
		LoggerError("[FIONA-logger]processData | Caught a std::exception: %s",ex.what());
	}catch(::Exception const & ex){
		LoggerError("[FIONA-logger]Caught an Exception: %s", ex.msg);
	}catch(...){
		LoggerError("[FIONA-logger]processData | Unknown exception caught");
	}

	return Undefined();
}

// IControlvoice
Handle<Value> ScriptingV8Spark::startSpeakingRequired(const Arguments& args) {
	//Locker locker;
	HandleScope scope;

	Local<Value> const jvself(args.Data());
	if( jvself.IsEmpty() || !jvself->IsExternal() )
	{
		return v8::ThrowException(v8::Exception::Error(v8::String::New("Callback is missing its native V8Shell object.")));
	}
	ScriptingV8Spark * self = static_cast<ScriptingV8Spark *>( v8::External::Cast(*jvself)->Value() );

	/* Make the native call */
	try{
		self->myControlVoice->startSpeaking();
	}catch(std::exception const & ex) {
		LoggerError("[FIONA-logger]startSpeaking | Caught a std::exception: %s",ex.what());
	}catch(::Exception const & ex){
		LoggerError("[FIONA-logger]Caught an Exception: %s", ex.msg);
	}catch(...){
		LoggerError("[FIONA-logger]startSpeaking | Unknown exception caught");
	}

	return Undefined();
}

Handle<Value> ScriptingV8Spark::stopSpeakingRequired(const Arguments& args) {
	//Locker locker;
	HandleScope scope;

	Local<Value> const jvself(args.Data());
	if( jvself.IsEmpty() || !jvself->IsExternal() )
	{
		return v8::ThrowException(v8::Exception::Error(v8::String::New("Callback is missing its native V8Shell object.")));
	}
	ScriptingV8Spark * self = static_cast<ScriptingV8Spark *>( v8::External::Cast(*jvself)->Value() );

	/* Make the native call */
	try{
		self->myControlVoice->stopSpeaking();
	}catch(std::exception const & ex) {
		LoggerError("[FIONA-logger]stopSpeaking | Caught a std::exception: %s",ex.what());
	}catch(::Exception const & ex){
		LoggerError("[FIONA-logger]Caught an Exception: %s", ex.msg);
	}catch(...){
		LoggerError("[FIONA-logger]stopSpeaking | Unknown exception caught");
	}

	return Undefined();
}

Handle<Value> ScriptingV8Spark::startVoiceRequired(const Arguments& args) {
	//Locker locker;
	HandleScope scope;

	Local<Value> const jvself(args.Data());
	if( jvself.IsEmpty() || !jvself->IsExternal() )
	{
		return v8::ThrowException(v8::Exception::Error(v8::String::New("Callback is missing its native V8Shell object.")));
	}
	ScriptingV8Spark * self = static_cast<ScriptingV8Spark *>( v8::External::Cast(*jvself)->Value() );

	/* Make the native call */
	try{
		self->myControlVoice->startVoice();
	}catch(std::exception const & ex) {
		LoggerError("[FIONA-logger]startVoice | Caught a std::exception: %s",ex.what());
	}catch(::Exception const & ex){
		LoggerError("[FIONA-logger]Caught an Exception: %s", ex.msg);
	}catch(...){
		LoggerError("[FIONA-logger]startVoice | Unknown exception caught");
	}

	return Undefined();
}

// IAnimation
Handle<Value> ScriptingV8Spark::playAnimationRequired(const Arguments& args) {
	//Locker locker;
	if (args.Length() < 1){
		Log("playAnimation called with insufficient arguments", 3);
		return Undefined();
	}
	HandleScope scope;

	Local<Value> const jvself(args.Data());
	if( jvself.IsEmpty() || !jvself->IsExternal() )
	{
		return v8::ThrowException(v8::Exception::Error(v8::String::New("Callback is missing its native V8Shell object.")));
	}
	ScriptingV8Spark * self = static_cast<ScriptingV8Spark *>( v8::External::Cast(*jvself)->Value() );


	Handle<Value> arg0 = args[0];
	String::Utf8Value animation(arg0);

	/* Make the native call */
	try{
		self->myAnimation->playAnimation(*animation);
	}catch(std::exception const & ex) {
		LoggerError("[FIONA-logger]playAnimation | Caught a std::exception: %s",ex.what());
	}catch(::Exception const & ex){
		LoggerError("[FIONA-logger]Caught an Exception: %s", ex.msg);
	}catch(...){
		LoggerError("[FIONA-logger]playAnimation | Unknown exception caught");
	}

	return Undefined();
}

/**
 * Interfaces methods implemented in JavaScript.
 */

void ScriptingV8Spark::notifyFrameEvent() {
	//LoggerInfo("[FIONA-logger]ScriptingV8Spark::notifyFrameEvent");
	Locker locker;
	// Create a handle scope to keep the temporary object references.
	HandleScope handle_scope;
	// Enter this context so all the remaining operations take place there
	Context::Scope context_scope(context_);

	// Set up an exception handler before calling the function
	TryCatch try_catch;
	Local<Function> jf;
	const int argc = 0;
	Handle<Value> argv[argc] = { };
	jf = Function::Cast( *(global->Get(String::New("notifyFrameEvent"))) );
	jf->Call( global, argc, argv );
	if (jf.IsEmpty()) {
		String::Utf8Value error(try_catch.Exception());
		Log(*error, 3);
	}

	return;
}


//IFlow<char*> implementation
void ScriptingV8Spark::processData(char *text) {
	Locker locker;
	LoggerInfo("[FIONA-logger]ScriptingV8Spark::processData | Me llega el texto: %s",text);
	// Create a handle scope to keep the temporary object references.
	HandleScope handle_scope;
	// Enter this context so all the remaining operations take place there
	Context::Scope context_scope(context_);
	// Wrap the C++ request objects in a JavaScript wrapper
	Handle<Value> text_value = String::New(text);

	Local<Function> jf;
	const int argc = 1;
	Handle<Value> argv[argc] = { text_value };
	jf = Function::Cast( *(global->Get(String::New("processData"))) );
	jf->Call( global, argc, argv );

	return;
}

//IControlVoice implementation
void ScriptingV8Spark::startSpeaking() {
	Locker locker;
	// Create a handle scope to keep the temporary object references.
	HandleScope handle_scope;
	// Enter this context so all the remaining operations take place there
	Context::Scope context_scope(context_);

	// Set up an exception handler before calling the function
	TryCatch try_catch;
	Local<Function> jf;
	const int argc = 0;
	Handle<Value> argv[argc] = { };
	jf = Function::Cast( *(global->Get(String::New("startSpeaking"))) );
	jf->Call( global, argc, argv );
	if (jf.IsEmpty()) {
		String::Utf8Value error(try_catch.Exception());
		Log(*error, 3);
	}
	return;
}

void ScriptingV8Spark::stopSpeaking() {
	Locker locker;
	// Create a handle scope to keep the temporary object references.
	HandleScope handle_scope;
	// Enter this context so all the remaining operations take place there
	Context::Scope context_scope(context_);

	// Set up an exception handler before calling the function
	TryCatch try_catch;
	Local<Function> jf;
	const int argc = 0;
	Handle<Value> argv[argc] = { };
	jf = Function::Cast( *(global->Get(String::New("stopSpeaking"))) );
	jf->Call( global, argc, argv );
	if (jf.IsEmpty()) {
		String::Utf8Value error(try_catch.Exception());
		Log(*error, 3);
	}
	return;
}

void ScriptingV8Spark::startVoice() {
	Locker locker;
	// Create a handle scope to keep the temporary object references.
	HandleScope handle_scope;
	// Enter this context so all the remaining operations take place there
	Context::Scope context_scope(context_);

	// Set up an exception handler before calling the function
	TryCatch try_catch;
	Local<Function> jf;
	const int argc = 0;
	Handle<Value> argv[argc] = { };
	jf = Function::Cast( *(global->Get(String::New("startVoice"))) );
	jf->Call( global, argc, argv );
	if (jf.IsEmpty()) {
		String::Utf8Value error(try_catch.Exception());
		Log(*error, 3);
	}
	return;
}

//IFaceExpression implementation
void ScriptingV8Spark::setFaceExpression(char *expressionName,float intensity){
	Locker locker;

	// Create a handle scope to keep the temporary object references.
	HandleScope handle_scope;
	// Enter this context so all the remaining operations take place there
	Context::Scope context_scope(context_);

	// Wrap the C++ request objects in a JavaScript wrapper
	Handle<Value> par_char = String::New(expressionName);
	Handle<Value> par_float = Number::New(intensity);

	Local<Function> jf;
	const int argc = 2;
	Handle<Value> argv[argc] = { par_char, par_float };
	jf = Function::Cast( *(global->Get(String::New("setFaceExpression"))) );
	jf->Call( global, argc, argv );

	return;
}

