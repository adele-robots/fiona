#include "ScriptingRenderizable.h"

//Class static members must be defined in source code
	Persistent<v8::Context> ScriptingRenderizable::context_;
	Persistent<v8::Function> ScriptingRenderizable::render_;
	Persistent<v8::Function> ScriptingRenderizable::getCamaraNode_;

void ScriptingRenderizable::chargeFunctions(Persistent<v8::Context> contextPasado_, v8::Handle<ObjectTemplate>global){
	//Update the context
		setContext(contextPasado_);

	// Enter the new context so all the following operations take place within it.
		v8::Context::Scope context_scope(context_);
		v8::HandleScope handle_scope;

		v8::Handle<String> render_name = v8::String::New("render");
		v8::Handle<Value> render_val;
		render_val= context_->Global()->Get(render_name);

		v8::Handle<String> getCamaraNode_name = v8::String::New("getCamaraNode");
		v8::Handle<Value> getCamaraNode_val;
		getCamaraNode_val= context_->Global()->Get(getCamaraNode_name);

	// If there is no render function, or if it is not a function, bail out
		if (!render_val->IsFunction()) return ;

	// If there is no getCamaraNode function, or if it is not a function, bail out
		if (!getCamaraNode_val->IsFunction()) return ;

	//Obtain the render
		// It is a function; cast it to a Function
			v8::Handle<Function> render_fun = v8::Handle<Function>::Cast(render_val);

		// Store the function in a Persistent handle, since we also want that to remain after this call returns
			render_ = v8::Persistent<Function>::New(render_fun);

		//Update render
			setRender(render_);

	//Obtain the getCamaraNode
		// It is a function; cast it to a Function
			v8::Handle<Function> getCamaraNode_fun = v8::Handle<Function>::Cast(getCamaraNode_val);

		// Store the function in a Persistent handle, since we also want that to remain after this call returns
			getCamaraNode_ = v8::Persistent<Function>::New(getCamaraNode_fun);

		//Update getCamaraNode
			setGetCamaraNode(getCamaraNode_);
}


void ScriptingRenderizable::quit(){
	render_.Dispose();
	getCamaraNode_.Dispose();
}

/*****************************IRenderizable implementation *****************************************************/

void ScriptingRenderizable::render()
{
	//Print
		printf("C++ side : render called.\n");

	// Create a handle scope to keep the temporary object references.
		v8::HandleScope handle_scope;

   // Enter the new context so all the following operations take place within it.
		v8::Context::Scope context_scope(context_);

		v8::Handle<String> render_name = v8::String::New("render");
		v8::Handle<Value> render_val;
		v8::Handle<Value> getCamaraNode_val;
		render_val= context_->Global()->Get(render_name);

	// If there is no render function, or if it is not a function, bail out
		if (!render_val->IsFunction())return ;

	//Obtain the render
		render_= getRender();

	// Wrap the C++ request object in a JavaScript wrapper (in this case isn't necessary)

	// Set up an exception handler before calling the Process function
		 TryCatch try_catch;

	// Invoke the process function, giving the global object as 'this' and one argument, the request.
		const int argc =0;
		v8::Handle<Value> argv[argc]={};
		v8::Handle<Value> result = render_->Call(context_->Global(), argc, argv);
		if (result.IsEmpty()) {
			String::Utf8Value error(try_catch.Exception());
			ScriptingGeneral *sg= new ScriptingGeneral();
			sg->Log(*error);
		}
		return;
}

H3DRes  ScriptingRenderizable::getCamaraNode()
{
	//Print
		printf("C++ side : getCamaraNode called. \n");

	// Create a handle scope to keep the temporary object references.
		v8::HandleScope handle_scope;

    // Enter the new context so all the following operations take place within it.
		v8::Context::Scope context_scope(context_);

		v8::Handle<String> getCamaraNode_name = v8::String::New("getCamaraNode");
		v8::Handle<Value> getCamaraNode_val;
		getCamaraNode_val= context_->Global()->Get(getCamaraNode_name);

	// TODO: Ver que pasa con el return
	// If there is no getCamaraNode function, or if it is not a function, bail out
		if (!getCamaraNode_val->IsFunction())return 0;

	//Obtain the getCamaraNode
		getCamaraNode_= getGetCamaraNode();

	// Wrap the C++ request object in a JavaScript wrapper (in this case isn't necessary)

	// Set up an exception handler before calling the Process function
		TryCatch try_catch;

	// Invoke the process function, giving the global object as 'this' and one argument, the request.
		const int argc =0;
		v8::Handle<Value> argv[argc]={};
		v8::Handle<Value> result = getCamaraNode_->Call(context_->Global(), argc, argv);
		if (result.IsEmpty()) {
			String::Utf8Value error(try_catch.Exception());
			ScriptingGeneral *sg= new ScriptingGeneral();
			sg->Log(*error);
		}
		// TODO: Comprobar que el return es correcto, si se pone H3DRes como lo comentado protesta.
		return static_cast<int>(result->Int32Value());

		/*ScriptingGeneral *sg = new ScriptingGeneral();
		return sg->UnwrapClass<H3DRes>(result->ToObject());*/
}

/************************** Implementations for required interfaces methods ***************************/
// Callbacks implementations. Just clean the objects and invoke actual code
// Callbacks are called from JavaScript code, when the function, that is mapped
// to this (FunctionTemplate) callback, is called

// IRenderizable

v8::Handle<Value> ScriptingRenderizable::renderRequiredCallback(const Arguments& args) {
	printf("C++ side : renderRequiredCallback called\n");
	if (args.Length() < 1) return v8::Undefined();
	v8::HandleScope scope;
	v8::Handle<Value> arg0 = args[0];
	ScriptingGeneral *sg = new ScriptingGeneral();
	ScriptingRenderizable *sc=sg->UnwrapClass<ScriptingRenderizable>(v8::Handle<Object>::Cast(arg0));
	sc->renderRequired();
	return v8::Undefined();
}

v8::Handle<Value> ScriptingRenderizable::getCamaraNodeRequiredCallback(const Arguments& args) {
	printf("C++ side : getCamaraNodeRequiredCallback called\n");
	if (args.Length() < 1) return v8::Undefined();
	v8::HandleScope scope;
	v8::Handle<Value> arg0 = args[0];
	ScriptingGeneral *sg = new ScriptingGeneral();
	ScriptingRenderizable *sc=sg->UnwrapClass<ScriptingRenderizable>(v8::Handle<Object>::Cast(arg0));
	sc->getCamaraNodeRequired();
	return v8::Undefined();
}

void ScriptingRenderizable::renderRequired() {
	printf("C++ side : renderRequired called\n");
	ScriptingSpark::myRenderizable->render();
}

int ScriptingRenderizable::getCamaraNodeRequired() {
	printf("C++ side : getCamaraNodeRequired called\n");
	return ScriptingSpark::myRenderizable->getCamaraNode();
}

void ScriptingRenderizable::setProcedures(v8::Handle<ObjectTemplate> global){
	global->Set(String::New("renderRequired"), FunctionTemplate::New(renderRequiredCallback));
	global->Set(String::New("getCamaraNodeRequired"), FunctionTemplate::New(getCamaraNodeRequiredCallback));
}
