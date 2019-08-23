#include "ScriptingNeck.h"

//Class static members must be defined in source code
	Persistent<v8::Context> ScriptingNeck::context_;
	Persistent<v8::Function> ScriptingNeck::rotateHead_;

void ScriptingNeck::chargeFunctions(Persistent<v8::Context> contextPasado_, v8::Handle<ObjectTemplate>global){
	//Update the context
		setContext(contextPasado_);

	// Enter the new context so all the following operations take place within it.
		v8::Context::Scope context_scope(context_);
		v8::HandleScope handle_scope;

		v8::Handle<String> rotateHead_name = v8::String::New("rotateHead");
		v8::Handle<Value> rotateHead_val;
		rotateHead_val= context_->Global()->Get(rotateHead_name);

	// If there is no rotateHead function, or if it is not a function, bail out
		if (!rotateHead_val->IsFunction()) return ;

	//Obtain the rotateHead
		// It is a function; cast it to a Function
			v8::Handle<Function> rotateHead_fun = v8::Handle<Function>::Cast(rotateHead_val);

		// Store the function in a Persistent handle, since we also want that to remain after this call returns
			rotateHead_ = v8::Persistent<Function>::New(rotateHead_fun);

		//Update rotateHead
			setRotateHead(rotateHead_);

}

void ScriptingNeck::quit(){
	rotateHead_.Dispose();
}

/************************************ IFaceExpression implementation ***************************************/

void ScriptingNeck::rotateHead(float pan,float tilt)
{
	//print
		printf("C++ side : rotateHead called. pan:%f , tilt:%f\n",pan,tilt);

		// Create a handle scope to keep the temporary object references.
			v8::HandleScope handle_scope;

    // Enter the new context so all the following operations take place within it.
		v8::Context::Scope context_scope(context_);
		v8::Handle<String> rotateHead_name = v8::String::New("rotateHead");
		v8::Handle<Value> rotateHead_val;
		rotateHead_val= context_->Global()->Get(rotateHead_name);

	// If there is no rotateHead function, or if it is not a function, bail out
		if (!rotateHead_val->IsFunction())return ;

	//Obtain the rotateHead
		rotateHead_= getRotateHead();

	// Wrap the C++ request object in a JavaScript wrapper
		v8::Handle<Value> par_float1 = v8::Number::New(pan);
		v8::Handle<Value> par_float2 = v8::Number::New(tilt);

	// Set up an exception handler before calling the Process function
		 TryCatch try_catch;

	// Invoke the process function, giving the global object as 'this' and one argument, the request.
		const int argc =2;
		v8::Handle<Value> argv[argc] = { par_float1, par_float2};
		v8::Handle<Value> result = rotateHead_->Call(context_->Global(), argc, argv);
		if (result.IsEmpty()) {
			String::Utf8Value error(try_catch.Exception());
			ScriptingGeneral *sg= new ScriptingGeneral();
			sg->Log(*error);
		}
		return;
}

// Implementations for required interfaces methods
/****************************************************/

// Callbacks implementations. Just clean the objects and invoke actual code
// Callbacks are called from JavaScript code, when the function, that is mapped
// to this (FunctionTemplate) callback, is called

// INeck
v8::Handle<Value> ScriptingNeck::rotateHeadRequiredCallback(const Arguments& args) {
	printf("C++ side : rotateHeadCallback called\n");
	if (args.Length() < 3) return v8::Undefined();
	v8::HandleScope scope;
	v8::Handle<Value> arg0 = args[0];
	ScriptingGeneral *sg = new ScriptingGeneral();
	ScriptingNeck *sc=sg->UnwrapClass<ScriptingNeck>(v8::Handle<Object>::Cast(arg0));
	v8::Handle<Value> arg1 = args[1];
	float val1= static_cast<float>(arg1->NumberValue());
	v8::Handle<Value> arg2 = args[2];
	float val2= static_cast<float>(arg2->NumberValue());
	sc->rotateHeadRequired(val1,val2);
	return v8::Undefined();
}

void ScriptingNeck::rotateHeadRequired(float pan, float tilt) {
	printf("C++ side : rotateHeadRequired called\n");
	printf("C++ side : pan:%f , tilt:%f\n",pan,tilt);
	ScriptingSpark::myNeck->rotateHead(pan, tilt);
}

void ScriptingNeck::setProcedures(v8::Handle<ObjectTemplate> global){
	global->Set(String::New("rotateHeadRequired"), FunctionTemplate::New(rotateHeadRequiredCallback));
}


