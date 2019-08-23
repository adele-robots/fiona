#include "ScriptingControlVoice.h"

//Class static members must be defined in source code
	Persistent<v8::Context> ScriptingControlVoice::context_;
	Persistent<v8::Function> ScriptingControlVoice::startSpeaking_;
	Persistent<v8::Function> ScriptingControlVoice::stopSpeaking_;
	Persistent<v8::Function> ScriptingControlVoice::startVoice_;

void ScriptingControlVoice::chargeFunctions(Persistent<v8::Context> contextPasado_, v8::Handle<ObjectTemplate>global){
	//Update the context
		setContext(contextPasado_);

	// Enter the new context so all the following operations take place within it.
		v8::Context::Scope context_scope(context_);
		v8::HandleScope handle_scope;

		v8::Handle<String> startSpeaking_name = v8::String::New("startSpeaking");
		v8::Handle<Value> startSpeaking_val;
		startSpeaking_val= context_->Global()->Get(startSpeaking_name);

		v8::Handle<String> stopSpeaking_name = v8::String::New("stopSpeaking");
		v8::Handle<Value> stopSpeaking_val;
		stopSpeaking_val= context_->Global()->Get(stopSpeaking_name);

		v8::Handle<String> startVoice_name = v8::String::New("startVoice");
		v8::Handle<Value> startVoice_val;
		startVoice_val= context_->Global()->Get(startVoice_name);

	// If there is no startSpeaking function, or if it is not a function, bail out
		if (!startSpeaking_val->IsFunction()) return ;

	// If there is no stopSpeaking function, or if it is not a function, bail out
		if (!stopSpeaking_val->IsFunction()) return ;

	// If there is no startVoice function, or if it is not a function, bail out
		if (!startVoice_val->IsFunction()) return ;

	//Obtain the startSpeaking
		// It is a function; cast it to a Function
			v8::Handle<Function> startSpeaking_fun = v8::Handle<Function>::Cast(startSpeaking_val);

		// Store the function in a Persistent handle, since we also want that to remain after this call returns
			startSpeaking_ = v8::Persistent<Function>::New(startSpeaking_fun);

		//Update startSpeaking
			setStartSpeaking(startSpeaking_);

	//Obtain the stopSpeaking
		// It is a function; cast it to a Function
			v8::Handle<Function> stopSpeaking_fun = v8::Handle<Function>::Cast(stopSpeaking_val);

		// Store the function in a Persistent handle, since we also want that to remain after this call returns
			stopSpeaking_ = v8::Persistent<Function>::New(stopSpeaking_fun);

		//Update stopSpeaking
			setStopSpeaking(stopSpeaking_);

	//Obtain the startVoice
		// It is a function; cast it to a Function
			v8::Handle<Function> startVoice_fun = v8::Handle<Function>::Cast(startVoice_val);

		// Store the function in a Persistent handle, since we also want that to remain after this call returns
			startVoice_ = v8::Persistent<Function>::New(startVoice_fun);

		//Update startVoice
			setStartVoice(startVoice_);
}

void ScriptingControlVoice::quit(){
	startSpeaking_.Dispose();
	stopSpeaking_.Dispose();
	startVoice_.Dispose();
}
/************************************************* IControlVoice implementation *************************************/

void  ScriptingControlVoice::startSpeaking()
{
	//Print
		printf("C++ side : startSpeaking called.\n");

	// Create a handle scope to keep the temporary object references.
		v8::HandleScope handle_scope;

	// Enter the new context so all the following operations take place within it.
		v8::Context::Scope context_scope(context_);
		v8::Handle<String> startSpeaking_name = v8::String::New("startSpeaking");
		v8::Handle<Value> startSpeaking_val;
		startSpeaking_val= context_->Global()->Get(startSpeaking_name);

	// If there is no startSpeaking function, or if it is not a function, bail out
		if (!startSpeaking_val->IsFunction())return ;

	//Obtain the startSpeaking
		startSpeaking_= getStartSpeaking();

	// Wrap the C++ request object in a JavaScript wrapper (in this case isn't necessary)

	// Set up an exception handler before calling the Process function
		 TryCatch try_catch;

	// Invoke the process function, giving the global object as 'this' and one argument, the request.
		const int argc =0;
		v8::Handle<Value> argv[argc]={};

		v8::Handle<Value> result = startSpeaking_->Call(context_->Global(), argc, argv);
		if (result.IsEmpty()) {
			String::Utf8Value error(try_catch.Exception());
			ScriptingGeneral *sg= new ScriptingGeneral();
			sg->Log(*error);
		}
		return;
}

void  ScriptingControlVoice::stopSpeaking()
{
	//Print
		printf("C++ side : stopSpeaking called.\n");

	// Create a handle scope to keep the temporary object references.
		v8::HandleScope handle_scope;


   // Enter the new context so all the following operations take place within it.
		v8::Context::Scope context_scope(context_);

		v8::Handle<String> stopSpeaking_name = v8::String::New("stopSpeaking");

		v8::Handle<Value> stopSpeaking_val;

		stopSpeaking_val= context_->Global()->Get(stopSpeaking_name);


	// If there is no stopSpeaking function, or if it is not a function, bail out
		if (!stopSpeaking_val->IsFunction())return ;


	//Obtain the stopSpeaking
		stopSpeaking_= getStopSpeaking();


	// Wrap the C++ request object in a JavaScript wrapper (in this case isn't necessary)

	// Set up an exception handler before calling the Process function
		 TryCatch try_catch;


	// Invoke the process function, giving the global object as 'this' and one argument, the request.
		const int argc =0;

		v8::Handle<Value> argv[argc]={};


		v8::Handle<Value> result = stopSpeaking_->Call(context_->Global(), argc, argv);

		if (result.IsEmpty()) {

			String::Utf8Value error(try_catch.Exception());

			ScriptingGeneral *sg= new ScriptingGeneral();

			sg->Log(*error);
		}

		return;
}

void  ScriptingControlVoice::startVoice()
{
	//Print
		printf("C++ side : startVoice called.\n");

	// Create a handle scope to keep the temporary object references.
		v8::HandleScope handle_scope;

   // Enter the new context so all the following operations take place within it.
		v8::Context::Scope context_scope(context_);
		v8::Handle<String> startVoice_name = v8::String::New("startVoice");
		v8::Handle<Value> startVoice_val;
		startVoice_val= context_->Global()->Get(startVoice_name);

	// If there is no startVoice function, or if it is not a function, bail out
		if (!startVoice_val->IsFunction())return ;

	//Obtain the startVoice
		startVoice_= getStartVoice();

	// Wrap the C++ request object in a JavaScript wrapper (in this case isn't necessary)

	// Set up an exception handler before calling the Process function
		 TryCatch try_catch;

	// Invoke the process function, giving the global object as 'this' and one argument, the request.
		const int argc =0;
		v8::Handle<Value> argv[argc]={};

		v8::Handle<Value> result = startVoice_->Call(context_->Global(), argc, argv);
		if (result.IsEmpty()) {
			String::Utf8Value error(try_catch.Exception());
			ScriptingGeneral *sg= new ScriptingGeneral();
			sg->Log(*error);
		}
		return;
}

/******************************* Implementations for required interfaces methods **********************************/
// Callbacks implementations. Just clean the objects and invoke actual code
// Callbacks are called from JavaScript code, when the function, that is mapped
// to this (FunctionTemplate) callback, is called

// IControlVoice
v8::Handle<Value> ScriptingControlVoice::startSpeakingRequiredCallback(const Arguments& args) {
	printf("C++ side : startSpeakingCallback called\n");
	if (args.Length() < 1) return v8::Undefined();
	v8::HandleScope scope;
	v8::Handle<Value> arg0 = args[0];
	ScriptingGeneral *sg = new ScriptingGeneral();
	ScriptingControlVoice *sc=sg->UnwrapClass<ScriptingControlVoice>(v8::Handle<Object>::Cast(arg0));
	sc->startSpeakingRequired();
	return v8::Undefined();
}

v8::Handle<Value> ScriptingControlVoice::stopSpeakingRequiredCallback(const Arguments& args) {
	printf("C++ side : stopSpeakingCallback called\n");
	if (args.Length() < 1) return v8::Undefined();
	v8::HandleScope scope;
	v8::Handle<Value> arg0 = args[0];
	ScriptingGeneral *sg = new ScriptingGeneral();
	ScriptingControlVoice *sc=sg->UnwrapClass<ScriptingControlVoice>(v8::Handle<Object>::Cast(arg0));
	sc->stopSpeakingRequired();
	return v8::Undefined();
}

v8::Handle<Value> ScriptingControlVoice::startVoiceRequiredCallback(const Arguments& args) {
	printf("C++ side : startVoiceCallback called\n");
	if (args.Length() < 1) return v8::Undefined();
	v8::HandleScope scope;
	v8::Handle<Value> arg0 = args[0];
	ScriptingGeneral *sg = new ScriptingGeneral();
	ScriptingControlVoice *sc=sg->UnwrapClass<ScriptingControlVoice>(v8::Handle<Object>::Cast(arg0));
	sc->startVoiceRequired();
	return v8::Undefined();
}

void ScriptingControlVoice::startSpeakingRequired() {
	printf("C++ side : startSpeakingRequired called\n");
	ScriptingSpark::myControlVoice->startSpeaking();
}

void ScriptingControlVoice::stopSpeakingRequired() {
	printf("C++ side : stopSpeakingRequired called\n");
	ScriptingSpark::myControlVoice->stopSpeaking();
}

void ScriptingControlVoice::startVoiceRequired() {
	printf("C++ side : startVoiceRequired called\n");
	ScriptingSpark::myControlVoice->startVoice();
}

void ScriptingControlVoice::setProcedures(v8::Handle<ObjectTemplate> global){
	global->Set(String::New("startSpeakingRequired"), FunctionTemplate::New(startSpeakingRequiredCallback));
	global->Set(String::New("stopSpeakingRequired"), FunctionTemplate::New(stopSpeakingRequiredCallback));
	global->Set(String::New("startVoiceRequired"), FunctionTemplate::New(startVoiceRequiredCallback));
}

