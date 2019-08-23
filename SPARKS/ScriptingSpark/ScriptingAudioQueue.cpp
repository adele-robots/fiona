#include "ScriptingAudioQueue.h"

//Class static members must be defined in source code
	Persistent<v8::Context> ScriptingAudioQueue::context_;
	Persistent<Function> ScriptingAudioQueue::dequeueAudioBuffer_;
	Persistent<Function> ScriptingAudioQueue::queueAudioBuffer_;
	Persistent<Function> ScriptingAudioQueue::getStoredAudioSize_;

void ScriptingAudioQueue::chargeFunctions(Persistent<v8::Context> contextPasado_, v8::Handle<ObjectTemplate>global){
	//Update the context
		setContext(contextPasado_);

	// Enter the new context so all the following operations take place within it.
		v8::HandleScope handle_scope;
		v8::Handle<String> dequeueAudioBuffer_name = v8::String::New("dequeueAudioBuffer");
		v8::Handle<Value> dequeueAudioBuffer_val;
		dequeueAudioBuffer_val= context_->Global()->Get(dequeueAudioBuffer_name);

		v8::Handle<String> queueAudioBuffer_name = v8::String::New("queueAudioBuffer");
		v8::Handle<Value> queueAudioBuffer_val;
		queueAudioBuffer_val= context_->Global()->Get(queueAudioBuffer_name);

		v8::Handle<String> getStoredAudioSize_name = v8::String::New("getStoredAudioSize");
		v8::Handle<Value> getStoredAudioSize_val;
		getStoredAudioSize_val= context_->Global()->Get(getStoredAudioSize_name);

	// If there is no dequeueAudioBuffer function, or if it is not a function, bail out
		if (!dequeueAudioBuffer_val->IsFunction()) return ;

	// If there is no getStoredAudioSize function, or if it is not a function, bail out
		if (!getStoredAudioSize_val->IsFunction()) return ;

	//Obtain the dequeueAudioBuffer
		// It is a function; cast it to a Function
			v8::Handle<Function> dequeueAudioBuffer_fun = v8::Handle<Function>::Cast(dequeueAudioBuffer_val);

		// Store the function in a Persistent handle, since we also want that to remain after this call returns
			dequeueAudioBuffer_ = v8::Persistent<Function>::New(dequeueAudioBuffer_fun);

		//Update dequeueAudioBuffer
			setDequeueAudioBuffer(dequeueAudioBuffer_);

	//Obtain the getStoredAudioSize
		// It is a function; cast it to a Function
			v8::Handle<Function> getStoredAudioSize_fun = v8::Handle<Function>::Cast(getStoredAudioSize_val);

		// Store the function in a Persistent handle, since we also want that to remain after this call returns
			getStoredAudioSize_ = v8::Persistent<Function>::New(getStoredAudioSize_fun);

		//Update getStoredAudioSize
			setGetStoredAudioSize(getStoredAudioSize_);
}

void ScriptingAudioQueue::quit(){
	dequeueAudioBuffer_.Dispose();
	getStoredAudioSize_.Dispose();
}

/********************************************IAudioQueue implementation *********************************************/

int  ScriptingAudioQueue::getStoredAudioSize()
{
	//Print
		printf("C++ side : getStoredAudioSize called. \n");

	//Create a handle scope to keep the temporary object references.
		v8::HandleScope handle_scope;

    // Enter the new context so all the following operations take place within it.
		v8::Context::Scope context_scope(context_);
		v8::Handle<String> getStoredAudioSize_name = v8::String::New("getStoredAudioSize");
		v8::Handle<Value> getStoredAudioSize_val;
		getStoredAudioSize_val= context_->Global()->Get(getStoredAudioSize_name);

	// TODO: Comprobar que vale con retornar 0
	// If there is no GetStoredAudioSize function, or if it is not a function, bail out
		if (!getStoredAudioSize_val->IsFunction())return 0;

	//Obtain the getStoredAudioSize
		getStoredAudioSize_= getGetStoredAudioSize();

	// Wrap the C++ request object in a JavaScript wrapper (in this case isn't necessary)

	// Set up an exception handler before calling the Process function
		 TryCatch try_catch;

	// Invoke the process function, giving the global object as 'this' and one argument, the request.
		const int argc =0;
		v8::Handle<Value> argv[argc]={};
		v8::Handle<Value> result = getStoredAudioSize_->Call(context_->Global(), argc, argv);
		if (result.IsEmpty()) {
			String::Utf8Value error(try_catch.Exception());
			ScriptingGeneral *sg= new ScriptingGeneral();
			sg->Log(*error);
		}
		// TODO: Comprobar que el retorno es correcto
		return static_cast<int>(result->Int32Value());
}

void ScriptingAudioQueue::dequeueAudioBuffer(char *buffer, int size)
{
	//Print
		printf("C++ side : dequeueAudioBuffer called. buffer:%s, size:%i \n",buffer,size);

	//Create a handle scope to keep the temporary object references.
		v8::HandleScope handle_scope;

    // Enter the new context so all the following operations take place within it.
		v8::Context::Scope context_scope(context_);
		v8::Handle<String> dequeueAudioBuffer_name = v8::String::New("dequeueAudioBuffer");
		v8::Handle<Value> dequeueAudioBuffer_val;
		dequeueAudioBuffer_val= context_->Global()->Get(dequeueAudioBuffer_name);

	// If there is no DequeueAudioBuffer function, or if it is not a function, bail out
		if (!dequeueAudioBuffer_val->IsFunction())return ;

	//Obtain the dequeueAudioBuffer
		dequeueAudioBuffer_= getDequeueAudioBuffer();

	// Wrap the C++ request object in a JavaScript wrapper
		ScriptingGeneral *sg= new ScriptingGeneral();
		v8::Handle<Object> par_char = sg->WrapClass(buffer,&(ScriptingGeneral::MakeCharTemplate));
		v8::Handle<Value> par_int = v8::Int32::New(size);

	// Set up an exception handler before calling the Process function
		 TryCatch try_catch;

	// Invoke the process function, giving the global object as 'this' and one argument, the request.
		const int argc =2;
		v8::Handle<Value> argv[argc] = { par_char, par_int};

		v8::Handle<Value> result = dequeueAudioBuffer_->Call(context_->Global(), argc, argv);
		if (result.IsEmpty()) {
			String::Utf8Value error(try_catch.Exception());
			ScriptingGeneral *sg= new ScriptingGeneral();
			sg->Log(*error);
		}
		return;
}


void ScriptingAudioQueue::queueAudioBuffer(char *buffer, int size)
{
	//Print
		printf("C++ side : queueAudioBuffer called. buffer:%s, size:%i \n",buffer,size);

	//Create a handle scope to keep the temporary object references.
		v8::HandleScope handle_scope;

    // Enter the new context so all the following operations take place within it.
		v8::Context::Scope context_scope(context_);
		v8::Handle<String> queueAudioBuffer_name = v8::String::New("queueAudioBuffer");
		v8::Handle<Value> queueAudioBuffer_val;
		queueAudioBuffer_val= context_->Global()->Get(queueAudioBuffer_name);

	// If there is no DequeueAudioBuffer function, or if it is not a function, bail out
		if (!queueAudioBuffer_val->IsFunction())return ;

	//Obtain the dequeueAudioBuffer
		queueAudioBuffer_= getQueueAudioBuffer();

	// Wrap the C++ request object in a JavaScript wrapper
		ScriptingGeneral *sg= new ScriptingGeneral();
		v8::Handle<Object> par_char = sg->WrapClass(buffer,&(ScriptingGeneral::MakeCharTemplate));
		v8::Handle<Value> par_int = v8::Int32::New(size);

	// Set up an exception handler before calling the Process function
		 TryCatch try_catch;

	// Invoke the process function, giving the global object as 'this' and one argument, the request.
		const int argc =2;
		v8::Handle<Value> argv[argc] = { par_char, par_int};

		v8::Handle<Value> result = queueAudioBuffer_->Call(context_->Global(), argc, argv);
		if (result.IsEmpty()) {
			String::Utf8Value error(try_catch.Exception());
			ScriptingGeneral *sg= new ScriptingGeneral();
			sg->Log(*error);
		}
		return;
}

/****************************************** Implementations for required interfaces methods ****************************/
// Callbacks implementations. Just clean the objects and invoke actual code
// Callbacks are called from JavaScript code, when the function, that is mapped
// to this (FunctionTemplate) callback, is called

// IAudioQueue
v8::Handle<Value> ScriptingAudioQueue::getStoredAudioSizeRequiredCallback(const Arguments& args) {
	printf("C++ side : getStroredAudioSizeCallback called\n");
	if (args.Length() < 1) return v8::Undefined();
	v8::HandleScope scope;
	v8::Handle<Value> arg0 = args[0];
	ScriptingGeneral *sg = new ScriptingGeneral();
	ScriptingAudioQueue *sc=sg->UnwrapClass<ScriptingAudioQueue>(v8::Handle<Object>::Cast(arg0));
	sc->getStoredAudioSizeRequired();
	return v8::Undefined();
}

v8::Handle<Value> ScriptingAudioQueue::dequeueAudioBufferRequiredCallback(const Arguments& args) {
	printf("C++ side : dequeueAudioBufferCallback called\n");
	if (args.Length() < 3) return v8::Undefined();
	v8::HandleScope scope;
	v8::Handle<Value> arg0 = args[0];
	ScriptingGeneral *sg = new ScriptingGeneral();
	ScriptingAudioQueue *sc=sg->UnwrapClass<ScriptingAudioQueue>(v8::Handle<Object>::Cast(arg0));
	v8::Handle<Value> arg1 = args[1];
	String::Utf8Value val1(arg1);
	v8::Handle<Value> arg2 = args[2];
	int val2= static_cast<int>(arg2->Int32Value());
	sc->dequeueAudioBufferRequired(*val1,val2);
	return v8::Undefined();
}

v8::Handle<Value> ScriptingAudioQueue::queueAudioBufferRequiredCallback(const Arguments& args) {
	printf("C++ side : queueAudioBufferCallback called\n");
	if (args.Length() < 3) return v8::Undefined();
	v8::HandleScope scope;
	v8::Handle<Value> arg0 = args[0];
	ScriptingGeneral *sg = new ScriptingGeneral();
	ScriptingAudioQueue *sc=sg->UnwrapClass<ScriptingAudioQueue>(v8::Handle<Object>::Cast(arg0));
	v8::Handle<Value> arg1 = args[1];
	String::Utf8Value val1(arg1);
	v8::Handle<Value> arg2 = args[2];
	int val2= static_cast<int>(arg2->Int32Value());
	sc->queueAudioBufferRequired(*val1,val2);
	return v8::Undefined();
}

int ScriptingAudioQueue::getStoredAudioSizeRequired() {
	printf("C++ side : getStoredAudioSizeRequired called\n");
	return ScriptingSpark::myAudioQueue->getStoredAudioSize();
}

void ScriptingAudioQueue::dequeueAudioBufferRequired(char *buffer, int size) {
	printf("C++ side : dequeueAudioBufferRequired called\n");
	printf("C++ side : params passed -> Buffer : %s, size: %i \n",buffer, size);
	ScriptingSpark::myAudioQueue->dequeueAudioBuffer(buffer,size);
}

void ScriptingAudioQueue::queueAudioBufferRequired(char *buffer, int size) {
	printf("C++ side : queueAudioBufferRequired called\n");
	printf("C++ side : params passed -> Buffer : %s, size: %i \n",buffer, size);
	ScriptingSpark::myAudioQueue->queueAudioBuffer(buffer,size);
}

void ScriptingAudioQueue::setProcedures(v8::Handle<ObjectTemplate> global){
	global->Set(String::New("getStoredAudioSizeRequired"), FunctionTemplate::New(getStoredAudioSizeRequiredCallback));
	global->Set(String::New("dequeueAudioBufferRequired"), FunctionTemplate::New(dequeueAudioBufferRequiredCallback));
	global->Set(String::New("queueAudioBufferRequired"), FunctionTemplate::New(queueAudioBufferRequiredCallback));
}
