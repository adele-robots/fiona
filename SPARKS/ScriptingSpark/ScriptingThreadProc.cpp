#include "ScriptingThreadProc.h"

//Class static members must be defined in source code
	Persistent<v8::Context> ScriptingThreadProc::context_;
	Persistent<v8::Function> ScriptingThreadProc::process_;

void ScriptingThreadProc::chargeFunctions(Persistent<v8::Context> contextPasado_, v8::Handle<ObjectTemplate>global){
	//Update the context
		setContext(contextPasado_);

	// Enter the new context so all the following operations take place within it.
		v8::HandleScope handle_scope;

		v8::Handle<String> process_name = v8::String::New("process");
		v8::Handle<Value> process_val;
		process_val= context_->Global()->Get(process_name);

	// If there is no process function, or if it is not a function, bail out
		if (!process_val->IsFunction()) return ;

	//Obtain the process
		// It is a function; cast it to a Function
			v8::Handle<Function> process_fun = v8::Handle<Function>::Cast(process_val);

		// Store the function in a Persistent handle, since we also want that to remain after this call returns
			process_ = v8::Persistent<Function>::New(process_fun);

		//Update process
			setProcess(process_);
}


void ScriptingThreadProc::quit(){
	process_.Dispose();
}

/***************************** IFaceExpression implementation *****************************************/

void ScriptingThreadProc::process()
{
	// print
		printf("C++ side : process called\n");

	// Create a handle scope to keep the temporary object references.
		v8::HandleScope handle_scope;

   // Enter the new context so all the following operations take place within it.
		v8::Context::Scope context_scope(context_);

		v8::Handle<String> process_name = v8::String::New("process");
		v8::Handle<Value> process_val;
		process_val= context_->Global()->Get(process_name);

	// If there is no process function, or if it is not a function, bail out
		if (!process_val->IsFunction())return ;

	//Obtain the process
		process_= getProcess();

	// Wrap the C++ request object in a JavaScript wrapper (in this case isn't necessary)

	// Set up an exception handler before calling the Process function
		 TryCatch try_catch;

	// Invoke the process function, giving the global object as 'this' and one argument, the request.
		const int argc =0;
		v8::Handle<Value> argv[argc]={};
		v8::Handle<Value> result = process_->Call(context_->Global(), argc, argv);
		if (result.IsEmpty()) {
			String::Utf8Value error(try_catch.Exception());
			ScriptingGeneral *sg= new ScriptingGeneral();
			sg->Log(*error);
		}
		return;
}

/*********************** Implementations for required interfaces methods ***********************/
// Callbacks implementations. Just clean the objects and invoke actual code
// Callbacks are called from JavaScript code, when the function, that is mapped
// to this (FunctionTemplate) callback, is called

// IThreadProc
v8::Handle<Value> ScriptingThreadProc::processRequiredCallback(const Arguments& args) {
	printf("C++ side : processRequiredCallback called\n");
	if (args.Length() < 1) return v8::Undefined();
	v8::HandleScope scope;
	Handle<Value> arg0 = args[0];
	ScriptingGeneral *sg = new ScriptingGeneral();
	ScriptingThreadProc *sc=sg->UnwrapClass<ScriptingThreadProc>(v8::Handle<Object>::Cast(arg0));
	sc->processRequired();
	return v8::Undefined();
}

void ScriptingThreadProc::processRequired() {
	printf("C++ side : processRequired called\n");
	ScriptingSpark::myThreadProc->process();
}

void ScriptingThreadProc::setProcedures(v8::Handle<ObjectTemplate> global){
	global->Set(String::New("processRequired"), FunctionTemplate::New(processRequiredCallback));
}
