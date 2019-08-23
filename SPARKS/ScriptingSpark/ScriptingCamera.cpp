#include "ScriptingCamera.h"

//Class static members must be defined in source code
	Persistent<v8::Context> ScriptingCamera::context_;
	Persistent<v8::Function> ScriptingCamera::setCameraPosition_;
	Persistent<v8::Function> ScriptingCamera::setCameraRotation_;
	Persistent<v8::Function> ScriptingCamera::setCameraParameters_;

void ScriptingCamera::chargeFunctions(Persistent<v8::Context> contextPasado_, v8::Handle<ObjectTemplate>global){
	//Update the context
		setContext(contextPasado_);

	// Enter the new context so all the following operations take place within it.
		v8::HandleScope handle_scope;

		v8::Handle<String> setCameraPosition_name = v8::String::New("setCameraPosition");
		v8::Handle<Value> setCameraPosition_val;
		setCameraPosition_val= context_->Global()->Get(setCameraPosition_name);

		v8::Handle<String> setCameraRotation_name = v8::String::New("setCameraRotation");
		v8::Handle<Value> setCameraRotation_val;
		setCameraRotation_val= context_->Global()->Get(setCameraRotation_name);

		v8::Handle<String> setCameraParameters_name = v8::String::New("setCameraParameters");
		v8::Handle<Value> setCameraParameters_val;
		setCameraParameters_val= context_->Global()->Get(setCameraParameters_name);

	// If there is no setCameraPosition function, or if it is not a function, bail out
		if (!setCameraPosition_val->IsFunction()) return ;

	// If there is no setCameraRotation function, or if it is not a function, bail out
		if (!setCameraRotation_val->IsFunction()) return ;

	// If there is no setCameraParameters function, or if it is not a function, bail out
		if (!setCameraParameters_val->IsFunction()) return ;

	//Obtain the setCameraPosition
		// It is a function; cast it to a Function
			v8::Handle<Function> setCameraPosition_fun = v8::Handle<Function>::Cast(setCameraPosition_val);

		// Store the function in a Persistent handle, since we also want that to remain after this call returns
			setCameraPosition_ = v8::Persistent<Function>::New(setCameraPosition_fun);

		//Update setFaceExpression_
			setSetCameraPosition(setCameraPosition_);

	//Obtain the setCameraRotation
		// It is a function; cast it to a Function
			v8::Handle<Function> setCameraRotation_fun = v8::Handle<Function>::Cast(setCameraRotation_val);

		// Store the function in a Persistent handle, since we also want that to remain after this call returns
			setCameraRotation_ = v8::Persistent<Function>::New(setCameraRotation_fun);

		//Update setCameraRotation
			setSetCameraRotation(setCameraRotation_);

	//Obtain the setCameraParameters
		// It is a function; cast it to a Function
			v8::Handle<Function> setCameraParameters_fun = v8::Handle<Function>::Cast(setCameraParameters_val);

		// Store the function in a Persistent handle, since we also want that to remain after this call returns
			setCameraParameters_ = v8::Persistent<Function>::New(setCameraParameters_fun);

		//Update setCameraParameters
			setSetCameraParameters(setCameraParameters_);
}

void ScriptingCamera::quit(){
	setCameraPosition_.Dispose();
	setCameraRotation_.Dispose();
	setCameraParameters_.Dispose();
}
/***************************************ICamera implementation****************************************************/

void ScriptingCamera::setCameraPosition(float X,float Y,float Z)
{
	//Print
		printf("C++ side : setCameraPosition called. x: %f, y:%f , z:%f \n",X,Y,Z);

	//Create a handle scope to keep the temporary object references.
		v8::HandleScope handle_scope;

   // Enter the new context so all the following operations take place within it.
		v8::Context::Scope context_scope(context_);
		v8::Handle<String> setCameraPosition_name = v8::String::New("setCameraPosition");
		v8::Handle<Value> setCameraPosition_val;
		setCameraPosition_val= context_->Global()->Get(setCameraPosition_name);

	// If there is no setCameraPosition function, or if it is not a function, bail out
		if (!setCameraPosition_val->IsFunction())return ;

	//Obtain the setCameraPosition
		setCameraPosition_= getSetCameraPosition();

	// Wrap the C++ request object in a JavaScript wrapper
		v8::Handle<Value> par_float1 = v8::Number::New(X);
		v8::Handle<Value> par_float2 = v8::Number::New(Y);
		v8::Handle<Value> par_float3 = v8::Number::New(Z);

	// Set up an exception handler before calling the Process function
		 TryCatch try_catch;

	// Invoke the process function, giving the global object as 'this' and one argument, the request.
		const int argc =3;
		v8::Handle<Value> argv[argc] = { par_float1, par_float2, par_float3};

		v8::Handle<Value> result = setCameraPosition_->Call(context_->Global(), argc, argv);
		if (result.IsEmpty()) {
			String::Utf8Value error(try_catch.Exception());
			ScriptingGeneral *sg= new ScriptingGeneral();
			sg->Log(*error);
		}
		return;
}

void ScriptingCamera::setCameraRotation(float X,float Y,float Z)
{
	//Print
		printf("C++ side : setCameraRotation called. x: %f, y:%f , z:%f \n",X,Y,Z);

	//Create a handle scope to keep the temporary object references.
		v8::HandleScope handle_scope;

   // Enter the new context so all the following operations take place within it.
		v8::Context::Scope context_scope(context_);
		v8::Handle<String> setCameraRotation_name = v8::String::New("setCameraRotation");
		v8::Handle<Value> setCameraRotation_val;
		setCameraRotation_val= context_->Global()->Get(setCameraRotation_name);

	// If there is no setCameraRotation function, or if it is not a function, bail out
		if (!setCameraRotation_val->IsFunction())return ;

	//Obtain the setCameraRotation
		setCameraRotation_= getSetCameraRotation();

	// Wrap the C++ request object in a JavaScript wrapper
		v8::Handle<Value> par_float1 = v8::Number::New(X);
		v8::Handle<Value> par_float2 = v8::Number::New(Y);
		v8::Handle<Value> par_float3 = v8::Number::New(Z);

	// Set up an exception handler before calling the Process function
		 TryCatch try_catch;

	// Invoke the process function, giving the global object as 'this' and one argument, the request.
		const int argc =3;
		v8::Handle<Value> argv[argc] = { par_float1, par_float2, par_float3};

		v8::Handle<Value> result = setCameraRotation_->Call(context_->Global(), argc, argv);
		if (result.IsEmpty()) {
			String::Utf8Value error(try_catch.Exception());
			ScriptingGeneral *sg=new ScriptingGeneral();
			sg->Log(*error);
		}
		return;
}

void ScriptingCamera::setCameraParameters(bool IsOrtho,float VisionAngle, float nearClippingPlane,float FarClippingPlane)
{
	//Print
		printf("C++ side : setCameraParameters called. IsOrtho: %s, visionAngle:%f,nearClippingPlane:%f, FarClippingPlane:%f \n",(IsOrtho)? "Verdadero" : "Falso",VisionAngle, nearClippingPlane,FarClippingPlane);

	//Create a handle scope to keep the temporary object references.
		v8::HandleScope handle_scope;

    // Enter the new context so all the following operations take place within it.
		v8::Context::Scope context_scope(context_);
		v8::Handle<String> setCameraParameters_name = v8::String::New("setCameraParameters");
		v8::Handle<Value> setCameraParameters_val;
		setCameraParameters_val= context_->Global()->Get(setCameraParameters_name);

	// If there is no setCameraParameters function, or if it is not a function, bail out
		if (!setCameraParameters_val->IsFunction())return ;

	//Obtain the setCameraParameters
		setCameraParameters_= getSetCameraParameters();

	// Wrap the C++ request object in a JavaScript wrapper
		v8::Handle<Value> par_bool = v8::Boolean::New(IsOrtho);
		v8::Handle<Value> par_float1 = v8::Number::New(VisionAngle);
		v8::Handle<Value> par_float2 = v8::Number::New(nearClippingPlane);
		v8::Handle<Value> par_float3 = v8::Number::New(FarClippingPlane);

	// Set up an exception handler before calling the Process function
		 TryCatch try_catch;

	// Invoke the process function, giving the global object as 'this' and one argument, the request.
		const int argc =4;
		v8::Handle<Value> argv[argc] = { par_bool, par_float1, par_float2, par_float3};

		v8::Handle<Value> result = setCameraParameters_->Call(context_->Global(), argc, argv);
		if (result.IsEmpty()) {
			String::Utf8Value error(try_catch.Exception());
			ScriptingGeneral *sg= new ScriptingGeneral();
			sg->Log(*error);
		}
		return;
}

/******************************************** Implementations for required interfaces methods*************************/
// Callbacks implementations. Just clean the objects and invoke actual code
// Callbacks are called from JavaScript code, when the function, that is mapped
// to this (FunctionTemplate) callback, is called

// ICamera
v8::Handle<Value> ScriptingCamera::setCameraPositionRequiredCallback(const Arguments& args) {
	printf("C++ side : setCameraPositionCallback called\n");
	if (args.Length() < 4) return v8::Undefined();
	v8::HandleScope scope;
	v8::Handle<Value> arg0 = args[0];
	ScriptingGeneral *sg = new ScriptingGeneral();
	ScriptingCamera *sc=sg->UnwrapClass<ScriptingCamera>(v8::Handle<Object>::Cast(arg0));
	v8::Handle<Value> arg1 = args[1];
	float val1= static_cast<float>(arg1->NumberValue());
	v8::Handle<Value> arg2 = args[2];
	float val2= static_cast<float>(arg2->NumberValue());
	v8::Handle<Value> arg3 = args[3];
	float val3= static_cast<float>(arg3->NumberValue());
	sc->setCameraPositionRequired(val1,val2,val3);
	return v8::Undefined();
}

v8::Handle<Value> ScriptingCamera::setCameraRotationRequiredCallback(const Arguments& args) {
	printf("C++ side : setCameraRotationCallback called\n");
	if (args.Length() < 4) return v8::Undefined();
	v8::HandleScope scope;
	v8::Handle<Value> arg0 = args[0];
	ScriptingGeneral *sg = new ScriptingGeneral();
	ScriptingCamera *sc=sg->UnwrapClass<ScriptingCamera>(v8::Handle<Object>::Cast(arg0));
	v8::Handle<Value> arg1 = args[1];
	float val1= static_cast<float>(arg1->NumberValue());
	v8::Handle<Value> arg2 = args[2];
	float val2= static_cast<float>(arg2->NumberValue());
	v8::Handle<Value> arg3 = args[3];
	float val3= static_cast<float>(arg3->NumberValue());
	sc->setCameraRotationRequired(val1,val2,val3);
	return v8::Undefined();
}

v8::Handle<Value> ScriptingCamera::setCameraParametersRequiredCallback(const Arguments& args) {
	printf("C++ side : setCameraParametersCallback called\n");
	if (args.Length() < 5) return v8::Undefined();
	v8::HandleScope scope;
	v8::Handle<Value> arg0 = args[0];
	ScriptingGeneral *sg = new ScriptingGeneral();
	ScriptingCamera *sc=sg->UnwrapClass<ScriptingCamera>(v8::Handle<Object>::Cast(arg0));
	v8::Handle<Value> arg1 = args[1];
	bool val1 = static_cast<bool>(arg1->BooleanValue());
	v8::Handle<Value> arg2 = args[2];
	float val2= static_cast<float>(arg2->NumberValue());
	v8::Handle<Value> arg3 = args[3];
	float val3= static_cast<float>(arg3->NumberValue());
	v8::Handle<Value> arg4 = args[4];
	float val4= static_cast<float>(arg4->NumberValue());
	sc->setCameraParametersRequired(val1,val2,val3,val4);
	return v8::Undefined();
}

void ScriptingCamera::setCameraPositionRequired(float X,float Y,float Z) {
	printf("C++ side : setCameraPositionRequired called\n");
	printf("C++ side : params passed -> X : %f, Y: %f, Z: %f \n",X, Y, Z);
	ScriptingSpark::myCamera->setCameraPosition(X,Y,Z);
}

void ScriptingCamera::setCameraRotationRequired(float X,float Y,float Z) {
	printf("C++ side : setCameraRotationRequired called\n");
	printf("C++ side : params passed -> X : %f, Y: %f, Z: %f \n",X, Y,Z);
	ScriptingSpark::myCamera->setCameraRotation(X,Y,Z);
}

void ScriptingCamera::setCameraParametersRequired(bool IsOrtho,float VisionAngle, float nearClippingPlane,float FarClippingPlane) {
	printf("C++ side : setCameraParametersRequired called\n");
	printf("C++ side : IsOrtho: %s, visionAngle:%f,nearClippingPlane:%f, FarClippingPlane:%f \n",(IsOrtho)? "Verdadero" : "Falso",VisionAngle, nearClippingPlane,FarClippingPlane);

	ScriptingSpark::myCamera->setCameraParameters(IsOrtho,VisionAngle,nearClippingPlane, FarClippingPlane);
}

void ScriptingCamera::setProcedures(v8::Handle<ObjectTemplate> global){
	global->Set(String::New("setCameraPositionRequired"), FunctionTemplate::New(setCameraPositionRequiredCallback));
	global->Set(String::New("setCameraRotationRequired"), FunctionTemplate::New(setCameraRotationRequiredCallback));
	global->Set(String::New("setCameraParametersRequired"), FunctionTemplate::New(setCameraParametersRequiredCallback));
}
