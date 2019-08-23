#include "ScriptingConcurrent.h"

//Class static members must be defined in source code
	Persistent<v8::Context> ScriptingConcurrent::context_;
	Persistent<v8::Function> ScriptingConcurrent::start_;
	Persistent<v8::Function> ScriptingConcurrent::stop_;

void ScriptingConcurrent::chargeFunctions(Persistent<v8::Context> contextPasado_, v8::Handle<ObjectTemplate>global){
	//Update the context
		setContext(contextPasado_);

	// Enter the new context so all the following operations take place within it.
		v8::HandleScope handle_scope;

		v8::Handle<String> start_name = v8::String::New("start");
		v8::Handle<Value> start_val;
		start_val= context_->Global()->Get(start_name);

		v8::Handle<String> stop_name = v8::String::New("stop");
		v8::Handle<Value> stop_val;
		stop_val= context_->Global()->Get(stop_name);

	// If there is no start function, or if it is not a function, bail out
		if (!start_val->IsFunction()) return ;

	// If there is no stop function, or if it is not a function, bail out
		if (!stop_val->IsFunction()) return ;

	//Obtain the start
		// It is a function; cast it to a Function
			v8::Handle<Function> start_fun = v8::Handle<Function>::Cast(start_val);

		// Store the function in a Persistent handle, since we also want that to remain after this call returns
			start_ = v8::Persistent<Function>::New(start_fun);

		//Update start
			setStart(start_);

	//Obtain the stop
		// It is a function; cast it to a Function
			v8::Handle<Function> stop_fun = v8::Handle<Function>::Cast(stop_val);

		// Store the function in a Persistent handle, since we also want that to remain after this call returns
			stop_ = v8::Persistent<Function>::New(stop_fun);

		//Update stop
			setStop(stop_);

}

void ScriptingConcurrent::quit(){
	start_.Dispose();
	stop_.Dispose();
}
/****************************************** IConcurrent implementation ***********************************/

void  ScriptingConcurrent::start()
{
	//Print
		printf("C++ side : start called.\n");

	// Create a handle scope to keep the temporary object references.
		v8::HandleScope handle_scope;

   // Enter the new context so all the following operations take place within it.
		v8::Context::Scope context_scope(context_);
		v8::Handle<String> start_name = v8::String::New("start");
		v8::Handle<Value> start_val;
		start_val= context_->Global()->Get(start_name);

	// If there is no Start function, or if it is not a function, bail out
		if (!start_val->IsFunction())return ;

	//Obtain the Start
		start_= getStart();

	// Wrap the C++ request object in a JavaScript wrapper (in this case isn't necessary)

	// Set up an exception handler before calling the Process function
		 TryCatch try_catch;

	// Invoke the process function, giving the global object as 'this' and one argument, the request.
		const int argc =0;
		v8::Handle<Value> argv[argc]={};

		v8::Handle<Value> result = start_->Call(context_->Global(), argc, argv);
		if (result.IsEmpty()) {
			String::Utf8Value error(try_catch.Exception());
			ScriptingGeneral *sg= new ScriptingGeneral();
			sg->Log(*error);
		}
		return;
}

void  ScriptingConcurrent::stop()
{
	//Print
		printf("C++ side : stop called.\n");

	// Create a handle scope to keep the temporary object references.
		v8::HandleScope handle_scope;

    // Enter the new context so all the following operations take place within it.
		v8::Context::Scope context_scope(context_);
		v8::Handle<String> stop_name = v8::String::New("stop");
		v8::Handle<Value> stop_val;
		stop_val= context_->Global()->Get(stop_name);

	// If there is no Stop function, or if it is not a function, bail out
		if (!stop_val->IsFunction())return ;

	// Obtain the Stop
		stop_= getStop();

	// Wrap the C++ request object in a JavaScript wrapper (in this case isn't necessary)

	// Set up an exception handler before calling the Process function
		 TryCatch try_catch;

	// Invoke the process function, giving the global object as 'this' and one argument, the request.
		const int argc =0;
		v8::Handle<Value> argv[argc]={};

		v8::Handle<Value> result = stop_->Call(context_->Global(), argc, argv);
		if (result.IsEmpty()) {
			String::Utf8Value error(try_catch.Exception());
			ScriptingGeneral *sg= new ScriptingGeneral();
			sg->Log(*error);
		}
		return;
}

/******************************************** Implementations for required interfaces methods ************************/
// Callbacks implementations. Just clean the objects and invoke actual code
// Callbacks are called from JavaScript code, when the function, that is mapped
// to this (FunctionTemplate) callback, is called

// IConcurrent
v8::Handle<Value> ScriptingConcurrent::startRequiredCallback(const Arguments& args) {
	printf("C++ side : startCallback called\n");
	if (args.Length() < 1) return v8::Undefined();
	v8::HandleScope scope;
	v8::Handle<Value> arg0 = args[0];
	ScriptingGeneral *sg = new ScriptingGeneral();
	ScriptingConcurrent *sc=sg->UnwrapClass<ScriptingConcurrent>(v8::Handle<Object>::Cast(arg0));
	sc->startRequired();
	return v8::Undefined();
}

v8::Handle<Value> ScriptingConcurrent::stopRequiredCallback(const Arguments& args) {
	printf("C++ side : stopCallback called\n");
	if (args.Length() < 1) return v8::Undefined();
	v8::HandleScope scope;
	v8::Handle<Value> arg0 = args[0];
	ScriptingGeneral *sg = new ScriptingGeneral();
	ScriptingConcurrent *sc=sg->UnwrapClass<ScriptingConcurrent>(v8::Handle<Object>::Cast(arg0));
	sc->stopRequired();
	return v8::Undefined();
}

void ScriptingConcurrent::startRequired() {
	printf("C++ side : startRequired called\n");
	ScriptingSpark::myConcurrent->start();
}

void ScriptingConcurrent::stopRequired() {
	printf("C++ side : stopRequired called\n");
	ScriptingSpark::myConcurrent->stop();
}

void ScriptingConcurrent::setProcedures(v8::Handle<ObjectTemplate> global){
	global->Set(String::New("startRequired"), FunctionTemplate::New(startRequiredCallback));
	global->Set(String::New("stopRequired"), FunctionTemplate::New(stopRequiredCallback));
}

