#include "ScriptingFaceExpression.h"

//Class static members must be defined in source code
	Persistent<v8::Context> ScriptingFaceExpression::context_;
	Persistent<v8::Function> ScriptingFaceExpression::setFaceExpression_;

void ScriptingFaceExpression::chargeFunctions(Persistent<v8::Context> contextPasado_, v8::Handle<ObjectTemplate>global){
	//Update the context
		setContext(contextPasado_);

	// Enter the new context so all the following operations take place within it.
		v8::HandleScope handle_scope;
		v8::Handle<String> setFaceExpression_name = v8::String::New("setFaceExpression");
		v8::Handle<Value> setFaceExpression_val;
		setFaceExpression_val= context_->Global()->Get(setFaceExpression_name);

	// If there is no setFaceExpression function, or if it is not a function, bail out
		if (!setFaceExpression_val->IsFunction()) return ;

	//Obtain the setFaceExpression
		// It is a function; cast it to a Function
			v8::Handle<Function> setFaceExpression_fun = v8::Handle<Function>::Cast(setFaceExpression_val);

		// Store the function in a Persistent handle, since we also want that to remain after this call returns
			setFaceExpression_ = v8::Persistent<Function>::New(setFaceExpression_fun);

		//Update setFaceExpression
			setSetFaceExpression(setFaceExpression_);
}

void ScriptingFaceExpression::quit(){
	setFaceExpression_.Dispose();
}

/************************************ IFaceExpression implementation **************************************************/

void ScriptingFaceExpression::setFaceExpression(char *expressionName,float intensity)
{
	printf("C++ side : setFaceExpression called. expressionName:%s , intensity:%f\n",expressionName,intensity);

	// Create a handle scope to keep the temporary object references.
		v8::HandleScope handle_scope;

	// Enter the new context so all the following operations take place within it.
		v8::Context::Scope context_scope(context_);
		v8::Handle<String> setFaceExpression_name = v8::String::New("setFaceExpression");
		v8::Handle<Value> setFaceExpression_val;
		setFaceExpression_val= context_->Global()->Get(setFaceExpression_name);

	// If there is no setFaceExpression function, or if it is not a function, bail out
		if (!setFaceExpression_val->IsFunction()) return;

	//Obtain the setFaceExpression
		//setFaceExpression_= chargeSetFaceExpression(context_, setFaceExpression_val);
		setFaceExpression_=getSetFaceExpression();

	// Wrap the C++ request object in a JavaScript wrapper
		ScriptingGeneral *sg= new ScriptingGeneral();
		v8::Handle<Object> par_char= sg->WrapClass(expressionName,&(ScriptingGeneral::MakeCharTemplate));
		v8::Handle<Value> par_float = v8::Number::New(intensity);

	// Set up an exception handler before calling the Process function
		TryCatch try_catch;

	// Invoke the process function, giving the global object as 'this' and one argument, the request.
		const int argc =2;
		v8::Handle<Value> argv[argc] = { par_char, par_float};

		v8::Handle<Value> result = setFaceExpression_->Call(context_->Global(), argc, argv);
		if (result.IsEmpty()) {
			String::Utf8Value error(try_catch.Exception());
			ScriptingGeneral *sg= new ScriptingGeneral();
			sg->Log(*error);
		}
		return;
}

/*************************************** Implementations for required interfaces methods******************************/
// Callbacks implementations. Just clean the objects and invoke actual code
// Callbacks are called from JavaScript code, when the function, that is mapped
// to this (FunctionTemplate) callback, is called

// IFaceExpression
v8::Handle<Value> ScriptingFaceExpression::setFaceExpressionRequiredCallback(const Arguments& args) {
	printf("C++ side : setFaceExpressionRequiredCallback called\n");
	if (args.Length() < 3) return v8::Undefined();
	v8::HandleScope scope;
	v8::Handle<Value> arg0 = args[0];
	ScriptingGeneral *sg = new ScriptingGeneral();
	ScriptingFaceExpression *sc=sg->UnwrapClass<ScriptingFaceExpression>(v8::Handle<Object>::Cast(arg0));
	v8::Handle<Value> arg1 = args[1];
	String::Utf8Value val1(arg1);
	v8::Handle<Value> arg2 = args[2];
	float val2= static_cast<float>(arg2->NumberValue());
	sc->setFaceExpressionRequired(*val1,val2);
	return v8::Undefined();
}

void ScriptingFaceExpression::setFaceExpressionRequired(char *expressionName,float intensity) {
	printf("C++ side : setFaceExpressionRequired called\n");
	printf("C++ side : expressionName:%s , intensity:%f\n",expressionName,intensity);
	ScriptingSpark::myFaceExpression->setFaceExpression(expressionName, intensity);
}

void ScriptingFaceExpression::setProcedures(v8::Handle<ObjectTemplate> global){
	global->Set(String::New("setFaceExpressionRequired"), FunctionTemplate::New(setFaceExpressionRequiredCallback));
}

