#include "ScriptingFrameEventPublisher.h"

//Class static members must be defined in source code
	Persistent<v8::Context> ScriptingFrameEventPublisher::context_;
	Persistent<v8::Function> ScriptingFrameEventPublisher::addFrameEventSubscriber_;


void ScriptingFrameEventPublisher::chargeFunctions(Persistent<v8::Context> contextPasado_, v8::Handle<ObjectTemplate>global){
	//Update the context
		setContext(contextPasado_);

	// Enter the new context so all the following operations take place within it.
		v8::Context::Scope context_scope(context_);
		v8::HandleScope handle_scope;

		v8::Handle<String> addFrameEventSubscriber_name = v8::String::New("addFrameEventSubscriber");
		v8::Handle<Value> addFrameEventSubscriber_val;
		addFrameEventSubscriber_val= context_->Global()->Get(addFrameEventSubscriber_name);

	// If there is no addFrameEventSubscriber function, or if it is not a function, bail out
		if (!addFrameEventSubscriber_val->IsFunction()) return ;

	//Obtain the addFrameEventSubscriber
		// It is a function; cast it to a Function
			v8::Handle<Function> addFrameEventSubscriber_fun = v8::Handle<Function>::Cast(addFrameEventSubscriber_val);

		// Store the function in a Persistent handle, since we also want that to remain after this call returns
			addFrameEventSubscriber_ = v8::Persistent<Function>::New(addFrameEventSubscriber_fun);

		//Update addFrameEventSubscriber
			setAddFrameEventSubscriber(addFrameEventSubscriber_);

}

void ScriptingFrameEventPublisher::quit(){
	addFrameEventSubscriber_.Dispose();
}
/***************************** IFrameEventPublisher implementation ****************************************/

void ScriptingFrameEventPublisher::addFrameEventSubscriber(FrameEventSubscriber *frameEventSubscriber)
{
		printf("C++ side : addFrameEventSubscriber called. \n");

	// Create a handle scope to keep the temporary object references.
		v8::HandleScope handle_scope;

   // Enter the new context so all the following operations take place within it.
		v8::Context::Scope context_scope(context_);

		v8::Handle<String> addFrameEventSubscriber_name = v8::String::New("addFrameEventSubscriber");
		v8::Handle<Value> addFrameEventSubscriber_val;
		addFrameEventSubscriber_val= context_->Global()->Get(addFrameEventSubscriber_name);

	// If there is no addFrameEventSubscriber function, or if it is not a function, bail out
		if (!addFrameEventSubscriber_val->IsFunction())return ;

	//Obtain the addFrameEventSubscriber
		addFrameEventSubscriber_= getAddFrameEventSubscriber();

	// Wrap the C++ request object in a JavaScript wrapper
		ScriptingGeneral *sg= new ScriptingGeneral();
		v8::Handle<Object> par_frameEventSubscriber = sg->WrapClass(frameEventSubscriber, &(ScriptingGeneral::MakeFrameEventSubscriberTemplate));

	// Set up an exception handler before calling the Process function
		 TryCatch try_catch;

	// Invoke the process function, giving the global object as 'this' and one argument, the request.
		const int argc =1;
		v8::Handle<Value> argv[argc]={par_frameEventSubscriber};
		v8::Handle<Value> result = addFrameEventSubscriber_->Call(context_->Global(), argc, argv);
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

// IFrameEventPublisher
v8::Handle<Value> ScriptingFrameEventPublisher::addFrameEventSubscriberRequiredCallback(const Arguments& args) {
	printf("C++ side : addFrameEventSubscriberRequiredCallback called\n");
	if (args.Length() < 3) return v8::Undefined();
	v8::HandleScope scope;
	v8::Handle<Value> arg0 = args[0];
	ScriptingGeneral *sg = new ScriptingGeneral();
	ScriptingFrameEventPublisher *sc=sg->UnwrapClass<ScriptingFrameEventPublisher>(v8::Handle<Object>::Cast(arg0));
	v8::Handle<Value> arg1 = args[1];
	//TODO: SIN GARANTIZAR AUN QUE ESTO FUNCIONE, PERO PARECE QUE DE MOMENTO NO SERA NECESARIO
	FrameEventSubscriber* val1 = sg->UnwrapClass<FrameEventSubscriber>(v8::Handle<Object>::Cast(arg1));
	sc->addFrameEventSubscriberRequired(val1);
	return v8::Undefined();
}

void ScriptingFrameEventPublisher::addFrameEventSubscriberRequired(FrameEventSubscriber *frameEventSubscriber) {
	printf("C++ side : addFrameEventSubscriber called\n");
	ScriptingSpark::myFrameEventPublisher->addFrameEventSubscriber(frameEventSubscriber);
}

void ScriptingFrameEventPublisher::setProcedures(v8::Handle<ObjectTemplate> global){
	global->Set(String::New("addFrameEventSubscriberRequired"), FunctionTemplate::New(addFrameEventSubscriberRequiredCallback));
	}

