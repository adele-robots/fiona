#include "ScriptingDetectedFacePositionConsumer.h"

//Class static members must be defined in source code
	Persistent<v8::Context> ScriptingDetectedFacePositionConsumer::context_;
	Persistent<v8::Function> ScriptingDetectedFacePositionConsumer::consumeDetectedFacePosition_;

void ScriptingDetectedFacePositionConsumer::chargeFunctions(Persistent<v8::Context> contextPasado_, v8::Handle<ObjectTemplate>global){
	//Update the context
		setContext(contextPasado_);

	// Enter the new context so all the following operations take place within it.
		v8::HandleScope handle_scope;

		v8::Handle<String> consumeDetectedFacePosition_name = v8::String::New("consumeDetectedFacePosition");
		v8::Handle<Value> consumeDetectedFacePosition_val;
		consumeDetectedFacePosition_val= context_->Global()->Get(consumeDetectedFacePosition_name);

	// If there is no consumeDetectedFacePosition function, or if it is not a function, bail out
		if (!consumeDetectedFacePosition_val->IsFunction()) return ;

	//Obtain the consumeDetectedFacePosition
		// It is a function; cast it to a Function
			v8::Handle<Function> consumeDetectedFacePosition_fun = v8::Handle<Function>::Cast(consumeDetectedFacePosition_val);

		// Store the function in a Persistent handle, since we also want that to remain after this call returns
			consumeDetectedFacePosition_ = v8::Persistent<Function>::New(consumeDetectedFacePosition_fun);

		//Update consumeDetectedFacePosition
			setConsumeDetectedFacePosition(consumeDetectedFacePosition_);
}

void ScriptingDetectedFacePositionConsumer::quit(){
	consumeDetectedFacePosition_.Dispose();
}

/*******************************************IDetectedFacePositionConsumer implementation ********************************/
void ScriptingDetectedFacePositionConsumer::consumeDetectedFacePosition(bool isFaceDetected, double x, double y)
{
	//Print
		printf("C++ side : consumeDetectedFacePosition called. isfaceDetected: %s, x: %lf, y:%lf  \n",(isFaceDetected)?"Verdadero":"Falso", x,y);

	// Create a handle scope to keep the temporary object references.
		v8::HandleScope handle_scope;

   // Enter the new context so all the following operations take place within it.
		v8::Context::Scope context_scope(context_);
		v8::Handle<String> consumeDetectedFacePosition_name = v8::String::New("consumeDetectedFacePosition");
		v8::Handle<Value> consumeDetectedFacePosition_val;
		consumeDetectedFacePosition_val= context_->Global()->Get(consumeDetectedFacePosition_name);

	// If there is no consumeDetectedFacePosition function, or if it is not a function, bail out
		if (!consumeDetectedFacePosition_val->IsFunction())return ;

	//Obtain the consumeDetectedFacePosition
		consumeDetectedFacePosition_= getConsumeDetectedFacePosition();

	// Wrap the C++ request object in a JavaScript wrapper
		v8::Handle<Value> par_bool = v8::Boolean::New(isFaceDetected);
		v8::Handle<Value> par_double1=v8::Number::New(x);
		v8::Handle<Value> par_double2=v8::Number::New(y);

	// Set up an exception handler before calling the Process function
		 TryCatch try_catch;

	// Invoke the process function, giving the global object as 'this' and one argument, the request.
		const int argc =3;
		v8::Handle<Value> argv[argc] = { par_bool, par_double1, par_double2};
		v8::Handle<Value> result = consumeDetectedFacePosition_->Call(context_->Global(), argc, argv);
		if (result.IsEmpty()) {
			String::Utf8Value error(try_catch.Exception());
			ScriptingGeneral *sg= new ScriptingGeneral();
			sg->Log(*error);
		}
		return;
}

/******************************** Implementations for required interfaces methods ***********************/
// Callbacks implementations. Just clean the objects and invoke actual code
// Callbacks are called from JavaScript code, when the function, that is mapped
// to this (FunctionTemplate) callback, is called

// IDetectedFacePositionConsumer
v8::Handle<Value> ScriptingDetectedFacePositionConsumer::consumeDetectedFacePositionRequiredCallback(const Arguments& args) {
	printf("C++ side : consumeDetectedFacePositionCallback called\n");
	if (args.Length() < 4) return v8::Undefined();
	v8::HandleScope scope;
	v8::Handle<Value> arg0 = args[0];
	ScriptingGeneral *sg = new ScriptingGeneral();
	ScriptingDetectedFacePositionConsumer *sc=sg->UnwrapClass<ScriptingDetectedFacePositionConsumer>(v8::Handle<Object>::Cast(arg0));
	v8::Handle<Value> arg1 = args[1];
	bool val1= static_cast<bool>(arg1->BooleanValue());
	v8::Handle<Value> arg2 = args[2];
	double val2= static_cast<double>(arg2->NumberValue());
	v8::Handle<Value> arg3 = args[3];
	double val3= static_cast<float>(arg3->NumberValue());
	sc->consumeDetectedFacePositionRequired(val1,val2,val3);
	return v8::Undefined();
}

void ScriptingDetectedFacePositionConsumer::consumeDetectedFacePositionRequired(bool isFaceDetected, double x, double y) {
	printf("C++ side : consumeDetectedFacePositionRequired called\n");
	printf("C++ side : isfaceDetected: %s, x: %lf, y:%lf  \n",(isFaceDetected)?"Verdadero":"Falso", x,y);

	ScriptingSpark::myDetectedFacePositionConsumer->consumeDetectedFacePosition(isFaceDetected,x,y);
}

void ScriptingDetectedFacePositionConsumer::setProcedures(v8::Handle<ObjectTemplate> global){
	global->Set(String::New("consumeDetectedFacePositionRequired"), FunctionTemplate::New(consumeDetectedFacePositionRequiredCallback));
}

