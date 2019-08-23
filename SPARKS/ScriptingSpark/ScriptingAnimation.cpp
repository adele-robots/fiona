#include "ScriptingAnimation.h"

//Class static members must be defined in source code
	Persistent<v8::Context> ScriptingAnimation::context_;
	Persistent<Function> ScriptingAnimation::playAnimation_;

void ScriptingAnimation::chargeFunctions(Persistent<v8::Context> contextPasado_, v8::Handle<ObjectTemplate>global){
	//Update the context
		setContext(contextPasado_);

	// Enter the new context so all the following operations take place within it.
		v8::HandleScope handle_scope;
		v8::Handle<String> playAnimation_name = v8::String::New("playAnimation");
		v8::Handle<Value> playAnimation_val;
		playAnimation_val= context_->Global()->Get(playAnimation_name);

	// If there is no playAnimation function, or if it is not a function, bail out
		if (!playAnimation_val->IsFunction()) return ;

	//Obtain the playAnimation
		// It is a function; cast it to a Function
			v8::Handle<Function> playAnimation_fun = v8::Handle<Function>::Cast(playAnimation_val);

		// Store the function in a Persistent handle, since we also want that to remain after this call returns
			playAnimation_ = v8::Persistent<Function>::New(playAnimation_fun);

		// Update the playAnimation
			setPlayAnimation(playAnimation_);
}

void ScriptingAnimation::quit(){
	playAnimation_.Dispose();
}

/// Initializes the ScriptingAnimation component.

/***********************************************IAnimation implementation ************************************************/

void  ScriptingAnimation::playAnimation(char *animationFileName)
{
	//Print
		printf("C++ side : playAnimation called. animationFileName:%s\n",animationFileName);

	// Create a handle scope to keep the temporary object references.
		v8::HandleScope handle_scope;

	// Enter the new context so all the following operations take place within it.
		v8::Context::Scope context_scope(context_);
		v8::Handle<String> playAnimation_name = v8::String::New("playAnimation");
		v8::Handle<Value> playAnimation_val;
		playAnimation_val= context_->Global()->Get(playAnimation_name);

	// If there is no PlayAnimation function, or if it is not a function, bail out
		if (!playAnimation_val->IsFunction()) return ;

	//Obtain the playAnimation
		playAnimation_= getPlayAnimation();

	// Wrap the C++ request object in a JavaScript wrapper
		ScriptingGeneral *sg= new ScriptingGeneral();
		v8::Handle<Object> par_char = sg->WrapClass(animationFileName,&(ScriptingGeneral::MakeCharTemplate));

	// Set up an exception handler before calling the Process function
		TryCatch try_catch;

	// Invoke the process function, giving the global object as 'this' and one argument, the request.
		const int argc =1;
		v8::Handle<Value> argv[argc] = {par_char};
		v8::Handle<Value> result = playAnimation_->Call(context_->Global(), argc, argv);
		if (result.IsEmpty()) {
			v8::String::Utf8Value error(try_catch.Exception());
			ScriptingGeneral* sg= new ScriptingGeneral();
			sg->Log(*error);
		}
		return;
}

/***************************************** Implementations for required interfaces methods *******************************/

// Callbacks implementations. Just clean the objects and invoke actual code
// Callbacks are called from JavaScript code, when the function, that is mapped
// to this (FunctionTemplate) callback, is called

// IAnimation
v8::Handle<Value> ScriptingAnimation::playAnimationRequiredCallback(const Arguments& args) {
	printf("C++ side : playAnimationCallback called\n");
	if (args.Length() < 2) return v8::Undefined();
	v8::HandleScope scope;
	v8::Handle<Value> arg0 = args[0];
	ScriptingGeneral* sg = new ScriptingGeneral();
	ScriptingAnimation *sa=sg->UnwrapClass<ScriptingAnimation >(v8::Handle<Object>::Cast(arg0));
	v8::Handle<Value> arg1 = args[1];
	String::Utf8Value val1(arg1);
	sa->playAnimationRequired(*val1);
	return v8::Undefined();
}

void ScriptingAnimation::playAnimationRequired(char *animationFileName) {
	printf("C++ side : playAnimationRequired called\n");
	printf("C++ side : animationFileName : %s\n",animationFileName);
	ScriptingSpark::myAnimation->playAnimation(animationFileName);
}

void ScriptingAnimation::setProcedures(v8::Handle<ObjectTemplate> global){
	global->Set(String::New("playAnimationRequired"), FunctionTemplate::New(playAnimationRequiredCallback));
}
