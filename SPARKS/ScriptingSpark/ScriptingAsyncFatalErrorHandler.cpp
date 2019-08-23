#include "ScriptingAsyncFatalErrorHandler.h"

//Class static members must be defined in source code
	Persistent<v8::Context> ScriptingAsyncFatalErrorHandler::context_;
	Persistent<Function> ScriptingAsyncFatalErrorHandler::handleError_;

void ScriptingAsyncFatalErrorHandler::chargeFunctions(Persistent<v8::Context> contextPasado_, v8::Handle<ObjectTemplate>global){
	//Update the context
		setContext(contextPasado_);

	// Enter the new context so all the following operations take place within it.
		v8::HandleScope handle_scope;
		v8::Handle<String> handleError_name = v8::String::New("handleError");
		v8::Handle<Value> handleError_val;
		handleError_val= context_->Global()->Get(handleError_name);

	// If there is no handleError function, or if it is not a function, bail out
		if (!handleError_val->IsFunction()) return ;

	//Obtain the handleError
		// It is a function; cast it to a Function
			v8::Handle<Function> handleError_fun = v8::Handle<Function>::Cast(handleError_val);

		// Store the function in a Persistent handle, since we also want that to remain after this call returns
			handleError_ = v8::Persistent<Function>::New(handleError_fun);

		//Update handleError
			setHandleError(handleError_);
}

void ScriptingAsyncFatalErrorHandler::quit(){
	handleError_.Dispose();
}

/*************************************** IAsyncFatalErrorHandler implementation ************************************/

void  ScriptingAsyncFatalErrorHandler::handleError(char *msg)
{
	//Print
		printf("C++ side : handleError called. msg:%s \n",msg);

	//Create a handle scope to keep the temporary object references.
		v8::HandleScope handle_scope;

    // Enter the new context so all the following operations take place within it.
		v8::Context::Scope context_scope(context_);
		v8::Handle<String> handleError_name = v8::String::New("handleError");
		v8::Handle<Value> handleError_val;
		handleError_val= context_->Global()->Get(handleError_name);

	// If there is no handleError function, or if it is not a function, bail out
		if (!handleError_val->IsFunction())return ;

	//Obtain the handleError
		handleError_= getHandleError();

	// Wrap the C++ request object in a JavaScript wrapper
		ScriptingGeneral *sg = new ScriptingGeneral();
		v8::Handle<Object> par_char=sg->WrapClass(msg,&(ScriptingGeneral::MakeCharTemplate));

	// Set up an exception handler before calling the Process function
		 TryCatch try_catch;

	// Invoke the process function, giving the global object as 'this' and one argument, the request.
		const int argc =1;
		v8::Handle<Value> argv[argc] = { par_char};

		v8::Handle<Value> result = handleError_->Call(context_->Global(), argc, argv);
		if (result.IsEmpty()) {
			String::Utf8Value error(try_catch.Exception());
			ScriptingGeneral *sg = new ScriptingGeneral();
			sg->Log(*error);
		}
		return;
}

// Implementations for required interfaces methods
/****************************************************/

// Callbacks implementations. Just clean the objects and invoke actual code
// Callbacks are called from JavaScript code, when the function, that is mapped
// to this (FunctionTemplate) callback, is called

// IAsyncFatalErrorHandler
v8::Handle<Value> ScriptingAsyncFatalErrorHandler::handleErrorRequiredCallback(const Arguments& args) {
	printf("C++ side : handleErrorCallback called\n");
	if (args.Length() < 2) return v8::Undefined();
	v8::HandleScope scope;
	v8::Handle<Value> arg0 = args[0];
	ScriptingGeneral *sg = new ScriptingGeneral();
	ScriptingAsyncFatalErrorHandler *sc=sg->UnwrapClass<ScriptingAsyncFatalErrorHandler>(v8::Handle<Object>::Cast(arg0));
	v8::Handle<Value> arg1 = args[1];
	String::Utf8Value val1(arg1);
	sc->handleErrorRequired(*val1);
	return v8::Undefined();
}

void ScriptingAsyncFatalErrorHandler::handleErrorRequired(char *msg) {
	printf("C++ side : rotateHeadRequired called\n");
	printf("C++ side : params passed -> msg : %s\n",msg);
	ScriptingSpark::myAsyncFatalErrorHandler->handleError(msg);
}

void ScriptingAsyncFatalErrorHandler::setProcedures(v8::Handle<ObjectTemplate> global){
	global->Set(String::New("handleErrorRequired"), FunctionTemplate::New(handleErrorRequiredCallback));
}
