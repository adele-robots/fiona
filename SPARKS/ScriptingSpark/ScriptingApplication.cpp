#include "ScriptingApplication.h"

//Class static members must be defined in source code
	Persistent<v8::Context> ScriptingApplication::context_;
	Persistent<Function> ScriptingApplication::run_;

void ScriptingApplication::chargeFunctions(Persistent<v8::Context> contextPasado_, v8::Handle<ObjectTemplate>global){
	//Update the context
		setContext(contextPasado_);

	// Enter the new context so all the following operations take place within it.
		v8::HandleScope handle_scope;
		v8::Handle<String> run_name = v8::String::New("run");
		v8::Handle<Value> run_val;
		run_val= context_->Global()->Get(run_name);

	// If there is no run function, or if it is not a function, bail out
		if (!run_val->IsFunction()) return ;

	//Obtain the run
		// It is a function; cast it to a Function
			v8::Handle<Function> run_fun = v8::Handle<Function>::Cast(run_val);

		// Store the function in a Persistent handle, since we also want that to remain after this call returns
			run_ = v8::Persistent<Function>::New(run_fun);

		// Update the run
			setRun(run_);
}

void ScriptingApplication::quit(){
	run_.Dispose();
}
/**************************************** IFaceExpression implementation ************************************************/

void ScriptingApplication::run()
{
		printf("C++ side : run called\n");

	// Create a handle scope to keep the temporary object references.
		v8::HandleScope handle_scope;

	// Enter the new context so all the following operations take place within it.
		v8::Context::Scope context_scope(context_);
		v8::Handle<String> run_name = v8::String::New("run");
		v8::Handle<Value> run_val;
		run_val= context_->Global()->Get(run_name);

	// If there is no run function, or if it is not a function, bail out
		if (!run_val->IsFunction())return ;

	//Obtain the run
		run_= getRun();

	// Wrap the C++ request object in a JavaScript wrapper (in this case isn't necessary)

	// Set up an exception handler before calling the Process function
		 TryCatch try_catch;

	// Invoke the process function, giving the global object as 'this'
	// and one argument, the request.
		const int argc =0;
		v8::Handle<Value> argv[argc]={};
		v8::Handle<Value> result = run_->Call(context_->Global(), argc, argv);
		if (result.IsEmpty()) {
			String::Utf8Value error(try_catch.Exception());
			ScriptingGeneral* sg= new ScriptingGeneral();
			sg->Log(*error);
		}
		return;
}

/************************** Implementations for required interfaces methods *********************************/
// Callbacks implementations. Just clean the objects and invoke actual code
// Callbacks are called from JavaScript code, when the function, that is mapped
// to this (FunctionTemplate) callback, is called

// IApplication
v8::Handle<Value> ScriptingApplication::runRequiredCallback(const Arguments& args) {
	printf("C++ side : runCallback called\n");
	if (args.Length() < 1) return v8::Undefined();
	v8::HandleScope scope;
	v8::Handle<Value> arg0 = args[0];
	ScriptingGeneral *sg = new ScriptingGeneral();
	ScriptingApplication *sc=sg->UnwrapClass<ScriptingApplication>(v8::Handle<Object>::Cast(arg0));
	sc->runRequired();
	return v8::Undefined();
}

void ScriptingApplication::runRequired() {
	printf("C++ side : runRequired called\n");
	ScriptingSpark::myApplication->run();
}

void ScriptingApplication::setProcedures(v8::Handle<ObjectTemplate> global){
	global->Set(String::New("runRequired"), FunctionTemplate::New(runRequiredCallback));
}
