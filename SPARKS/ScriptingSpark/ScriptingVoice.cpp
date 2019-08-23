#include "ScriptingVoice.h"

//Class static members must be defined in source code
	Persistent<v8::Context> ScriptingVoice::context_;
	Persistent<v8::Function> ScriptingVoice::sayThis_;
	Persistent<v8::Function> ScriptingVoice::stopSpeech_;
	Persistent<v8::Function> ScriptingVoice::waitEndOfSpeech_;

void ScriptingVoice::chargeFunctions(Persistent<v8::Context> contextPasado_, v8::Handle<ObjectTemplate>global){
	//Update the context
		setContext(contextPasado_);

	// Enter the new context so all the following operations take place within it.
		v8::HandleScope handle_scope;

		v8::Handle<String> sayThis_name = v8::String::New("sayThis");
		v8::Handle<Value> sayThis_val;
		sayThis_val= context_->Global()->Get(sayThis_name);

		v8::Handle<String> stopSpeech_name = v8::String::New("stopSpeech");
		v8::Handle<Value> stopSpeech_val;
		stopSpeech_val= context_->Global()->Get(stopSpeech_name);

		v8::Handle<String> waitEndOfSpeech_name = v8::String::New("waitEndOfSpeech");
		v8::Handle<Value> waitEndOfSpeech_val;
		waitEndOfSpeech_val= context_->Global()->Get(waitEndOfSpeech_name);

	// If there is no sayThis function, or if it is not a function, bail out
		if (!sayThis_val->IsFunction()) return ;

	// If there is no stopSpeech function, or if it is not a function, bail out
		if (!stopSpeech_val->IsFunction()) return ;

	// If there is no waitEndOfSpeech function, or if it is not a function, bail out
		if (!waitEndOfSpeech_val->IsFunction()) return ;

	//Obtain the sayThis
		// It is a function; cast it to a Function
			v8::Handle<Function> sayThis_fun = v8::Handle<Function>::Cast(sayThis_val);

		// Store the function in a Persistent handle, since we also want that to remain after this call returns
			sayThis_ = v8::Persistent<Function>::New(sayThis_fun);

		//Update sayThis
			setSayThis(sayThis_);

	//Obtain the stopSpeech
		// It is a function; cast it to a Function
			v8::Handle<Function> stopSpeech_fun = v8::Handle<Function>::Cast(stopSpeech_val);

		// Store the function in a Persistent handle, since we also want that to remain after this call returns
			stopSpeech_ = v8::Persistent<Function>::New(stopSpeech_fun);

		//Update stopSpeech
			setStopSpeech(stopSpeech_);

	//Obtain the waitEndOfSpeech
		// It is a function; cast it to a Function
			v8::Handle<Function> waitEndOfSpeech_fun = v8::Handle<Function>::Cast(waitEndOfSpeech_val);

		// Store the function in a Persistent handle, since we also want that to remain after this call returns
			waitEndOfSpeech_ = v8::Persistent<Function>::New(waitEndOfSpeech_fun);

		//Update waitEndOfSpeech
			setWaitEndOfSpeech(waitEndOfSpeech_);
}

void ScriptingVoice::quit(){
	sayThis_.Dispose();
	stopSpeech_.Dispose();
	waitEndOfSpeech_.Dispose();
}

/*******************************************IVoice implementation *******************************************/

void  ScriptingVoice::sayThis(char *prompt)
{
	//Print
		printf("C++ side : sayThis called. propmt:%s\n",prompt);

	// Create a handle scope to keep the temporary object references.
		v8::HandleScope handle_scope;

   // Enter the new context so all the following operations take place within it.
		v8::Context::Scope context_scope(context_);

		v8::Handle<String> sayThis_name = v8::String::New("sayThis");
		v8::Handle<Value> sayThis_val;
		sayThis_val= context_->Global()->Get(sayThis_name);

	// If there is no sayThis function, or if it is not a function, bail out
		if (!sayThis_val->IsFunction())return ;

	//Obtain the sayThis
		sayThis_= getSayThis();

	// Wrap the C++ request object in a JavaScript wrapper
		ScriptingGeneral *sg= new ScriptingGeneral();
		v8::Handle<Object> par_char = sg->WrapClass(prompt,&(ScriptingGeneral::MakeCharTemplate));

	// Set up an exception handler before calling the Process function
		 TryCatch try_catch;

	// Invoke the process function, giving the global object as 'this' and one argument, the request.
		const int argc =1;
		v8::Handle<Value> argv[argc] = { par_char};

		v8::Handle<Value> result = sayThis_->Call(context_->Global(), argc, argv);
		if (result.IsEmpty()) {
			String::Utf8Value error(try_catch.Exception());
			ScriptingGeneral *sg= new ScriptingGeneral();
			sg->Log(*error);
		}
		return;
}

void  ScriptingVoice::waitEndOfSpeech()
{
	//Print
		printf("C++ side : waitEndOfSpeech called.\n");

	// Create a handle scope to keep the temporary object references.
		v8::HandleScope handle_scope;

   // Enter the new context so all the following operations take place within it.
		v8::Context::Scope context_scope(context_);

		v8::Handle<String> waitEndOfSpeech_name = v8::String::New("waitEndOfSpeech");
		v8::Handle<Value> waitEndOfSpeech_val;
		waitEndOfSpeech_val= context_->Global()->Get(waitEndOfSpeech_name);

	// If there is no waitEndOfSpeech function, or if it is not a function, bail out
		if (!waitEndOfSpeech_val->IsFunction())return ;

	//Obtain the waitEndOfSpeech
		waitEndOfSpeech_= getWaitEndOfSpeech();

	// Wrap the C++ request object in a JavaScript wrapper (in this case isn't necessary)

	// Set up an exception handler before calling the Process function
		 TryCatch try_catch;

	// Invoke the process function, giving the global object as 'this' and one argument, the request.
		const int argc =0;
		v8::Handle<Value> argv[argc]={};

		v8::Handle<Value> result = waitEndOfSpeech_->Call(context_->Global(), argc, argv);
		if (result.IsEmpty()) {
			String::Utf8Value error(try_catch.Exception());
			ScriptingGeneral *sg= new ScriptingGeneral();
			sg->Log(*error);
		}
		return;
}

void  ScriptingVoice::stopSpeech()
{
	//Print
		printf("C++ side : stopSpeech called. \n");

	// Create a handle scope to keep the temporary object references.
		v8::HandleScope handle_scope;

   // Enter the new context so all the following operations take place within it.
		v8::Context::Scope context_scope(context_);

		v8::Handle<String> stopSpeech_name = v8::String::New("stopSpeech");
		v8::Handle<Value> stopSpeech_val;
		stopSpeech_val= context_->Global()->Get(stopSpeech_name);

	// If there is no StopSpeech function, or if it is not a function, bail out
		if (!stopSpeech_val->IsFunction())return ;

	//Obtain the StopSpeech
		stopSpeech_= getStopSpeech();

	// Wrap the C++ request object in a JavaScript wrapper (in this case isn't necessary)

	// Set up an exception handler before calling the Process function
		 TryCatch try_catch;

	// Invoke the process function, giving the global object as 'this' and one argument, the request.
		const int argc =0;
		v8::Handle<Value> argv[argc]={};

		v8::Handle<Value> result = stopSpeech_->Call(context_->Global(), argc, argv);
		if (result.IsEmpty()) {
			String::Utf8Value error(try_catch.Exception());
			ScriptingGeneral *sg= new ScriptingGeneral();
			sg->Log(*error);
		}
		return;
}

/************* Implementations for required interfaces methods **************************************************/

// Callbacks implementations. Just clean the objects and invoke actual code
// Callbacks are called from JavaScript code, when the function, that is mapped
// to this (FunctionTemplate) callback, is called

// IVoice
v8::Handle<Value> ScriptingVoice::sayThisRequiredCallback(const Arguments& args) {
	printf("C++ side : sayThisCallback called\n");
	if (args.Length() < 2) return v8::Undefined();
	v8::HandleScope scope;
	v8::Handle<Value> arg0 = args[0];
	ScriptingGeneral *sg = new ScriptingGeneral();
	ScriptingVoice *sc=sg->UnwrapClass<ScriptingVoice>(v8::Handle<Object>::Cast(arg0));
	v8::Handle<Value> arg1 = args[1];
	String::Utf8Value val1(arg1);
	sc->sayThisRequired(*val1);
	return v8::Undefined();
}

v8::Handle<Value> ScriptingVoice::waitEndOfSpeechRequiredCallback(const Arguments& args) {
	printf("C++ side : waitEndOfSpeechCallback called\n");
	if (args.Length() < 1) return v8::Undefined();
	v8::HandleScope scope;
	v8::Handle<Value> arg0 = args[0];
	ScriptingGeneral *sg = new ScriptingGeneral();
	ScriptingVoice *sc=sg->UnwrapClass<ScriptingVoice>(v8::Handle<Object>::Cast(arg0));
	sc->waitEndOfSpeechRequired();
	return v8::Undefined();
}

v8::Handle<Value> ScriptingVoice::stopSpeechRequiredCallback(const Arguments& args) {
	printf("C++ side : stopSpeechCallback called\n");
	if (args.Length() < 1) return v8::Undefined();
	v8::HandleScope scope;
	v8::Handle<Value> arg0 = args[0];
	ScriptingGeneral *sg = new ScriptingGeneral();
	ScriptingVoice *sc=sg->UnwrapClass<ScriptingVoice>(v8::Handle<Object>::Cast(arg0));
	sc->stopSpeechRequired();
	return v8::Undefined();
}

void ScriptingVoice::sayThisRequired(char *prompt) {
	printf("C++ side : sayThisRequired called\n");
	printf("C++ side : params passed -> prompt : %s\n",prompt);
	ScriptingSpark::myVoice->sayThis(prompt);
}

void ScriptingVoice::waitEndOfSpeechRequired() {
	printf("C++ side : waitEndOfSpeechRequired called\n");
	ScriptingSpark::myVoice->waitEndOfSpeech();
}

void ScriptingVoice::stopSpeechRequired() {
	printf("C++ side : stopSpeechRequired called\n");
	ScriptingSpark::myVoice->stopSpeech();
}

void ScriptingVoice::setProcedures(v8::Handle<ObjectTemplate> global){
	global->Set(String::New("sayThisRequired"), FunctionTemplate::New(sayThisRequiredCallback));
	global->Set(String::New("waitEndOfSpeechRequired"), FunctionTemplate::New(waitEndOfSpeechRequiredCallback));
	global->Set(String::New("stopSpeechRequired"), FunctionTemplate::New(stopSpeechRequiredCallback));
}
