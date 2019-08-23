#include "ScriptingFrameEventSubscriber.h"

//Class static members must be defined in source code
	Persistent<v8::Context> ScriptingFrameEventSubscriber::context_;
	Persistent<v8::Function> ScriptingFrameEventSubscriber::notifyFrameEvent_;

void ScriptingFrameEventSubscriber::chargeFunctions(Persistent<v8::Context> contextPasado_, v8::Handle<ObjectTemplate>global){
	//Update the context
		setContext(contextPasado_);

	// Enter the new context so all the following operations take place within it.
		v8::Context::Scope context_scope(context_);
		v8::HandleScope handle_scope;

		v8::Handle<String> notifyFrameEvent_name = v8::String::New("notifyFrameEvent");
		v8::Handle<Value> notifyFrameEvent_val;
		notifyFrameEvent_val= context_->Global()->Get(notifyFrameEvent_name);

	// If there is no notifyFrameEvent function, or if it is not a function, bail out
		if (!notifyFrameEvent_val->IsFunction()) return ;

	//Obtain the notifyFrameEvent
		// It is a function; cast it to a Function
			v8::Handle<Function> notifyFrameEvent_fun = v8::Handle<Function>::Cast(notifyFrameEvent_val);

		// Store the function in a Persistent handle, since we also want that to remain after this call returns
			notifyFrameEvent_ = v8::Persistent<Function>::New(notifyFrameEvent_fun);

		//Update notifyFrameEvent
			setNotifyFrameEvent(notifyFrameEvent_);
}

void ScriptingFrameEventSubscriber::quit(){
	notifyFrameEvent_.Dispose();
}
/***************************** FrameEventPublisher implementation ****************************************/

void ScriptingFrameEventSubscriber::notifyFrameEvent()
{
		printf("C++ side : notifyFrameEvent called. \n");

	// Create a handle scope to keep the temporary object references.
		v8::HandleScope handle_scope;

   // Enter the new context so all the following operations take place within it.
		v8::Context::Scope context_scope(context_);

		v8::Handle<String> notifyFrameEvent_name = v8::String::New("notifyFrameEvent");

		v8::Handle<Value> notifyFrameEvent_val;

		notifyFrameEvent_val= context_->Global()->Get(notifyFrameEvent_name);

	// If there is no notifyFrameEvent function, or if it is not a function, bail out
		if (!notifyFrameEvent_val->IsFunction())return ;

	//Obtain the notifyFrameEvent
		notifyFrameEvent_= getNotifyFrameEvent();

	// Wrap the C++ request object in a JavaScript wrapper

	// Set up an exception handler before calling the Process function
		 TryCatch try_catch;

	// Invoke the process function, giving the global object as 'this' and one argument, the request.
		const int argc =0;
		v8::Handle<Value> argv[argc]={};
		v8::Handle<Value> result = notifyFrameEvent_->Call(context_->Global(), argc, argv);
		if (result.IsEmpty()) {
			String::Utf8Value error(try_catch.Exception());
			ScriptingGeneral *sg= new ScriptingGeneral();
			sg->Log(*error);
		}
		return;
}
/********************************** Implementations for required interfaces methods *************************/
// Callbacks implementations. Just clean the objects and invoke actual code
// Callbacks are called from JavaScript code, when the function, that is mapped
// to this (FunctionTemplate) callback, is called

// FrameEventSubscriber


v8::Handle<Value> ScriptingFrameEventSubscriber::notifyFrameEventRequiredCallback(const Arguments& args) {
	printf("C++ side : notifyFrameEventRequiredCallback called\n");
	if (args.Length() < 1) return v8::Undefined();
	v8::HandleScope scope;
	v8::Handle<Value> arg0 = args[0];
	ScriptingGeneral *sg = new ScriptingGeneral();
	ScriptingFrameEventSubscriber *sc=sg->UnwrapClass<ScriptingFrameEventSubscriber>(v8::Handle<Object>::Cast(arg0));
	sc->notifyFrameEventRequired();
	return v8::Undefined();
}

void ScriptingFrameEventSubscriber::notifyFrameEventRequired() {
	printf("C++ side : notifyFrameEventRequired called\n");
	ScriptingSpark::myFrameEventSubscriber->notifyFrameEvent();

}

void ScriptingFrameEventSubscriber::setProcedures(v8::Handle<ObjectTemplate> global){
	global->Set(String::New("notifyFrameEventRequired"), FunctionTemplate::New(notifyFrameEventRequiredCallback));
}

