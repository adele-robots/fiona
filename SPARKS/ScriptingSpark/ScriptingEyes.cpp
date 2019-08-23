#include "ScriptingEyes.h"

//Class static members must be defined in source code
	Persistent<v8::Context> ScriptingEyes::context_;
	Persistent<v8::Function> ScriptingEyes::rotateEye_;

void ScriptingEyes::chargeFunctions(Persistent<v8::Context> contextPasado_, v8::Handle<ObjectTemplate>global){
	//Update the context
		setContext(contextPasado_);

	// Enter the new context so all the following operations take place within it.
		v8::Context::Scope context_scope(context_);
		v8::HandleScope handle_scope;

		v8::Handle<String> rotateEye_name = v8::String::New("rotateEye");
		v8::Handle<Value> rotateEye_val;
		rotateEye_val= context_->Global()->Get(rotateEye_name);

	// If there is no rotateEye function, or if it is not a function, bail out
		if (!rotateEye_val->IsFunction()) return ;

	//Obtain the rotateEye
		// It is a function; cast it to a Function
			v8::Handle<Function> rotateEye_fun = v8::Handle<Function>::Cast(rotateEye_val);

		// Store the function in a Persistent handle, since we also want that to remain after this call returns
			rotateEye_ = v8::Persistent<Function>::New(rotateEye_fun);

		//Update setFaceExpression
			setRotateEye(rotateEye_);
}

void ScriptingEyes::quit(){
	rotateEye_.Dispose();
}

/********************************************** IFaceExpression implementation*****************************************/

void ScriptingEyes::rotateEye(float pan,float tilt)
{
	printf("C++ side : rotateEye called. pan:%f , tilt:%f\n",pan,tilt);

	// Create a handle scope to keep the temporary object references.
		v8::HandleScope handle_scope;

   // Enter the new context so all the following operations take place within it.
		v8::Context::Scope context_scope(context_);

		v8::Handle<String> rotateEye_name = v8::String::New("rotateEye");
		v8::Handle<Value> rotateEye_val;
		rotateEye_val= context_->Global()->Get(rotateEye_name);

	// If there is no rotateEye function, or if it is not a function, bail out
		if (!rotateEye_val->IsFunction())return ;

	//Obtain the rotateEye
		rotateEye_= getRotateEye();

	// Wrap the C++ request object in a JavaScript wrapper
		v8::Handle<Value> par_float1 = v8::Number::New(pan);
		v8::Handle<Value> par_float2 = v8::Number::New(tilt);

	// Set up an exception handler before calling the Process function
		 TryCatch try_catch;

	// Invoke the process function, giving the global object as 'this'
	// and one argument, the request.
		const int argc =2;
		v8::Handle<Value> argv[argc] = { par_float1, par_float2};

		v8::Handle<Value> result = rotateEye_->Call(context_->Global(), argc, argv);
		if (result.IsEmpty()) {
			String::Utf8Value error(try_catch.Exception());
			ScriptingGeneral *sg= new ScriptingGeneral();
			sg->Log(*error);
		}
		return;
}

/******************************** Implementations for required interfaces methods **********************************/
// Callbacks implementations. Just clean the objects and invoke actual code
// Callbacks are called from JavaScript code, when the function, that is mapped
// to this (FunctionTemplate) callback, is called

// IEyes
v8::Handle<Value> ScriptingEyes::rotateEyeRequiredCallback(const Arguments& args) {
	printf("C++ side : rotateEyeCallback called\n");
	if (args.Length() < 3) return v8::Undefined();
	v8::HandleScope scope;
	v8::Handle<Value> arg0 = args[0];
	ScriptingGeneral *sg = new ScriptingGeneral();
	ScriptingEyes *sc=sg->UnwrapClass<ScriptingEyes>(v8::Handle<Object>::Cast(arg0));
	v8::Handle<Value> arg1 = args[1];
	float val1= static_cast<float>(arg1->NumberValue());
	v8::Handle<Value> arg2 = args[2];
	float val2= static_cast<float>(arg2->NumberValue());
	sc->rotateEyeRequired(val1,val2);
	return v8::Undefined();
}

void ScriptingEyes::rotateEyeRequired(float pan, float tilt) {
	printf("C++ side : rotateEyeRequired called\n");
	printf("C++ side : pan:%f , tilt:%f\n",pan,tilt);
	ScriptingSpark::myEyes->rotateEye(pan, tilt);
}

void ScriptingEyes::setProcedures(v8::Handle<ObjectTemplate> global){
	global->Set(String::New("rotateEyeRequired"), FunctionTemplate::New(rotateEyeRequiredCallback));
}

