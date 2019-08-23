#include "ScriptingWindow.h"

//Class static members must be defined in source code
	Persistent<v8::Context> ScriptingWindow::context_;
	Persistent<v8::Function> ScriptingWindow::getWindowDisplay_;
	Persistent<v8::Function> ScriptingWindow::show_;
	Persistent<v8::Function> ScriptingWindow::hide_;
	Persistent<v8::Function> ScriptingWindow::getColorDepth_;
	Persistent<v8::Function> ScriptingWindow::makeCurrentopenGlThread_;
	Persistent<v8::Function> ScriptingWindow::openGlSwapBuffers_;

void ScriptingWindow::chargeFunctions(Persistent<v8::Context> contextPasado_, v8::Handle<ObjectTemplate>global){
	//Update the context
		setContext(contextPasado_);

	// Enter the new context so all the following operations take place within it.
		v8::Context::Scope context_scope(context_);
		v8::HandleScope handle_scope;

		v8::Handle<String> getWindowDisplay_name = v8::String::New("getWindowDisplay");
		v8::Handle<Value> getWindowDisplay_val;
		getWindowDisplay_val= context_->Global()->Get(getWindowDisplay_name);

		v8::Handle<String> show_name = v8::String::New("show");
		v8::Handle<Value> show_val;
		show_val= context_->Global()->Get(show_name);

		v8::Handle<String> hide_name = v8::String::New("hide");
		v8::Handle<Value> hide_val;
		hide_val= context_->Global()->Get(hide_name);

		v8::Handle<String> getColorDepth_name = v8::String::New("getColorDepth");
		v8::Handle<Value> getColorDepth_val;
		getColorDepth_val= context_->Global()->Get(getColorDepth_name);

		v8::Handle<String> makeCurrentopenGlThread_name = v8::String::New("makeCurrentopenGlThread");
		v8::Handle<Value> makeCurrentopenGlThread_val;
		makeCurrentopenGlThread_val= context_->Global()->Get(makeCurrentopenGlThread_name);

		v8::Handle<String> openGlSwapBuffers_name = v8::String::New("openGlSwapBuffers");
		v8::Handle<Value> openGlSwapBuffers_val;
		openGlSwapBuffers_val= context_->Global()->Get(openGlSwapBuffers_name);

	// If there is no getWindowDisplay function, or if it is not a function, bail out
		if (!getWindowDisplay_val->IsFunction()) return ;

	// If there is no show function, or if it is not a function, bail out
		if (!show_val->IsFunction()) return ;

	// If there is no hide function, or if it is not a function, bail out
		if (!hide_val->IsFunction()) return ;

	// If there is no getColorDepth function, or if it is not a function, bail out
		if (!getColorDepth_val->IsFunction()) return ;

	// If there is no makeCurrentopenGlThread function, or if it is not a function, bail out
		if (!makeCurrentopenGlThread_val->IsFunction()) return ;

	// If there is no openGlSwapBuffers function, or if it is not a function, bail out
		if (!openGlSwapBuffers_val->IsFunction()) return ;

	//Obtain the getWindowDisplay
		// It is a function; cast it to a Function
			v8::Handle<Function> getWindowDisplay_fun = v8::Handle<Function>::Cast(getWindowDisplay_val);

		// Store the function in a Persistent handle, since we also want that to remain after this call returns
			getWindowDisplay_ = v8::Persistent<Function>::New(getWindowDisplay_fun);

		//Update getWindowDisplay
			setGetWindowDisplay(getWindowDisplay_);

	//Obtain the show
		// It is a function; cast it to a Function
			v8::Handle<Function> show_fun = v8::Handle<Function>::Cast(show_val);

		// Store the function in a Persistent handle, since we also want that to remain after this call returns
			show_ = v8::Persistent<Function>::New(show_fun);

		//Update show
			setShow(show_);

	//Obtain the hide
		// It is a function; cast it to a Function
			v8::Handle<Function> hide_fun = v8::Handle<Function>::Cast(hide_val);

		// Store the function in a Persistent handle, since we also want that to remain after this call returns
			hide_ = v8::Persistent<Function>::New(hide_fun);

		//Update hide
			setHide(hide_);

	//Obtain the getColorDepth
		// It is a function; cast it to a Function
			v8::Handle<Function> getColorDepth_fun = v8::Handle<Function>::Cast(getColorDepth_val);

		// Store the function in a Persistent handle, since we also want that to remain after this call returns
			getColorDepth_ = v8::Persistent<Function>::New(getColorDepth_fun);

		//Update getColorDepth
			setGetColorDepth(getColorDepth_);

	//Obtain the makeCurrentopenGlThread
		// It is a function; cast it to a Function
			v8::Handle<Function> makeCurrentopenGlThread_fun = v8::Handle<Function>::Cast(makeCurrentopenGlThread_val);

		// Store the function in a Persistent handle, since we also want that to remain after this call returns
			makeCurrentopenGlThread_ = v8::Persistent<Function>::New(makeCurrentopenGlThread_fun);

		//Update makeCurrentopenGlThread
			setMakeCurrentopenGlThread(makeCurrentopenGlThread_);

	//Obtain the openGlSwapBuffers
		// It is a function; cast it to a Function
			v8::Handle<Function> openGlSwapBuffers_fun = v8::Handle<Function>::Cast(openGlSwapBuffers_val);

		// Store the function in a Persistent handle, since we also want that to remain after this call returns
			openGlSwapBuffers_ = v8::Persistent<Function>::New(openGlSwapBuffers_fun);

		//Update openGlSwapBuffers
			setOpenGlSwapBuffers(openGlSwapBuffers_);
}

void ScriptingWindow::quit(){
	getWindowDisplay_.Dispose();
	show_.Dispose();
	hide_.Dispose();
	getColorDepth_.Dispose();
	makeCurrentopenGlThread_.Dispose();
	openGlSwapBuffers_.Dispose();
}

/*********************************IWindow implementation***********************************************/
Display* ScriptingWindow::getWindowDisplay(){
	//Print
		printf("C++ side : getWindowDisplay called. \n");

	// Create a handle scope to keep the temporary object references.
			v8::HandleScope handle_scope;

   // Enter the new context so all the following operations take place within it.
		v8::Context::Scope context_scope(context_);
		v8::Handle<String> getWindowDisplay_name = v8::String::New("getWindowDisplay");
		v8::Handle<Value> getWindowDisplay_val;
		getWindowDisplay_val = context_ ->Global()->Get(getWindowDisplay_name);

	// TODO: Comprobar que vale con retornar 0
	// If there is no getWindowDisplay function, or if it is not a function, bail out
		if (!getWindowDisplay_val->IsFunction())return 0 ;

	//Obtain the getWindowDisplay_
		getWindowDisplay_= getGetWindowDisplay();

	// Wrap the C++ request object in a JavaScript wrapper (in this case isn't necessary)

	// Set up an exception handler before calling the Process function
		 TryCatch try_catch;

	// Invoke the process function, giving the global object as 'this' and one argument, the request.
		const int argc =0;
		v8::Handle<Value> argv[argc]={};

		v8::Handle<Value> result = getWindowDisplay_->Call(context_->Global(), argc, argv);
		if (result.IsEmpty()) {
			String::Utf8Value error(try_catch.Exception());
			ScriptingGeneral *sg= new ScriptingGeneral();
			sg->Log(*error);
		}

		// TODO: Revisar en tiempo de ejecución
		ScriptingGeneral *sg = new ScriptingGeneral();
		Handle<Object> objeto = v8::Handle<Object>::Cast(result);
		Display* d = sg->UnwrapClass<Display>(objeto);

		return d;
}

void ScriptingWindow::show()
{
	//Print
		printf("C++ side : show called. \n");

	// Create a handle scope to keep the temporary object references.
			v8::HandleScope handle_scope;

   // Enter the new context so all the following operations take place within it.
		v8::Context::Scope context_scope(context_);
		v8::Handle<String> show_name = v8::String::New("show");
		v8::Handle<Value> show_val;
		show_val= context_->Global()->Get(show_name);

	// If there is no show function, or if it is not a function, bail out
		if (!show_val->IsFunction())return ;

	//Obtain the show
		show_= getShow();

	// Wrap the C++ request object in a JavaScript wrapper (in this case isn't necessary)

	// Set up an exception handler before calling the Process function
		 TryCatch try_catch;

	// Invoke the process function, giving the global object as 'this' and one argument, the request.
		const int argc =0;
		v8::Handle<Value> argv[argc]={};

		v8::Handle<Value> result = show_->Call(context_->Global(), argc, argv);
		if (result.IsEmpty()) {
			String::Utf8Value error(try_catch.Exception());
			ScriptingGeneral *sg= new ScriptingGeneral();
			sg->Log(*error);
		}
		return;
}

void ScriptingWindow::hide()
{
	//Print
		printf("C++ side : hide called.  \n");

	// Create a handle scope to keep the temporary object references.
		v8::HandleScope handle_scope;

    // Enter the new context so all the following operations take place within it.
		v8::Context::Scope context_scope(context_);
		v8::Handle<String> hide_name = v8::String::New("hide");
		v8::Handle<Value> hide_val;
		hide_val= context_->Global()->Get(hide_name);

	// If there is no hide function, or if it is not a function, bail out
		if (!hide_val->IsFunction())return ;

	//Obtain the hide
		hide_= getHide();

	// Wrap the C++ request object in a JavaScript wrapper (in this case isn't necessary)

	// Set up an exception handler before calling the Process function
		 TryCatch try_catch;

	// Invoke the process function, giving the global object as 'this' and one argument, the request.
		const int argc =0;
		v8::Handle<Value> argv[argc]={};

		v8::Handle<Value> result = hide_->Call(context_->Global(), argc, argv);
		if (result.IsEmpty()) {
			String::Utf8Value error(try_catch.Exception());
			ScriptingGeneral *sg= new ScriptingGeneral();
			sg->Log(*error);
		}
		return;
}

int  ScriptingWindow::getColorDepth()
{
	//Print
		printf("C++ side : getColorDepth called. \n");

	// Create a handle scope to keep the temporary object references.
		v8::HandleScope handle_scope;

   // Enter the new context so all the following operations take place within it.
		v8::Context::Scope context_scope(context_);
		v8::Handle<String> getColorDepth_name = v8::String::New("getColorDepth");
		v8::Handle<Value> getColorDepth_val;
		getColorDepth_val= context_->Global()->Get(getColorDepth_name);

	// TODO: Comprobar que vale con retornar 0
	// If there is no getColorDepth function, or if it is not a function, bail out
		if (!getColorDepth_val->IsFunction())return 0;

	//Obtain the getColorDepth
		getColorDepth_= getGetColorDepth();

	// Wrap the C++ request object in a JavaScript wrapper (in this case isn't necessary)

	// Set up an exception handler before calling the Process function
		 TryCatch try_catch;

	// Invoke the process function, giving the global object as 'this' and one argument, the request.
		const int argc =0;
		v8::Handle<Value> argv[argc]={};

		v8::Handle<Value> result = getColorDepth_->Call(context_->Global(), argc, argv);
		if (result.IsEmpty()) {
			String::Utf8Value error(try_catch.Exception());
			ScriptingGeneral *sg= new ScriptingGeneral();
			sg->Log(*error);
		}
		//TODO: Comprobar que el retorno es correcto
		return static_cast<int>(result->Int32Value());
}

void ScriptingWindow::makeCurrentopenGlThread()
{
	//Print
		printf("C++ side : makeCurrentopenGlThread called. \n");

	// Create a handle scope to keep the temporary object references.
		v8::HandleScope handle_scope;

    // Enter the new context so all the following operations take place within it.
		v8::Context::Scope context_scope(context_);
		v8::Handle<String> makeCurrentopenGlThread_name = v8::String::New("makeCurrentopenGlThread");
		v8::Handle<Value> makeCurrentopenGlThread_val;
		makeCurrentopenGlThread_val= context_->Global()->Get(makeCurrentopenGlThread_name);

	// If there is no makeCurrentopenGlThread function, or if it is not a function, bail out
		if (!makeCurrentopenGlThread_val->IsFunction())return ;

	//Obtain the makeCurrentopenGlThread
		makeCurrentopenGlThread_= getMakeCurrentopenGlThread();

	// Wrap the C++ request object in a JavaScript wrapper (in this case isn't necessary)

	// Set up an exception handler before calling the Process function
		 TryCatch try_catch;

	// Invoke the process function, giving the global object as 'this' and one argument, the request.
		const int argc =0;
		v8::Handle<Value> argv[argc]={};
		v8::Handle<Value> result = makeCurrentopenGlThread_->Call(context_->Global(), argc, argv);
		if (result.IsEmpty()) {
			String::Utf8Value error(try_catch.Exception());
			ScriptingGeneral *sg= new ScriptingGeneral();
			sg->Log(*error);
		}
		return;
}

void ScriptingWindow::openGlSwapBuffers()
{
	//Print
		printf("C++ side : openGlSwapBuffers called. \n");

	// Create a handle scope to keep the temporary object references.
		v8::HandleScope handle_scope;

   // Enter the new context so all the following operations take place within it.
		v8::Context::Scope context_scope(context_);
		v8::Handle<String> openGlSwapBuffers_name = v8::String::New("openGlSwapBuffers");
		v8::Handle<Value> openGlSwapBuffers_val;
		openGlSwapBuffers_val= context_->Global()->Get(openGlSwapBuffers_name);

	// If there is no openGlSwapBuffers function, or if it is not a function, bail out
		if (!openGlSwapBuffers_val->IsFunction())return ;

	//Obtain the openGlSwapBuffers
		openGlSwapBuffers_= getOpenGlSwapBuffers();

	// Wrap the C++ request object in a JavaScript wrapper (in this case isn't necessary)

	// Set up an exception handler before calling the Process function
		 TryCatch try_catch;

	// Invoke the process function, giving the global object as 'this' and one argument, the request.
		const int argc =0;
		v8::Handle<Value> argv[argc]={};
		v8::Handle<Value> result = openGlSwapBuffers_->Call(context_->Global(), argc, argv);
		if (result.IsEmpty()) {
			String::Utf8Value error(try_catch.Exception());
			ScriptingGeneral *sg= new ScriptingGeneral();
			sg->Log(*error);
		}
		return;
}

/**************************** Implementations for required interfaces methods *****************************************/

// Callbacks implementations. Just clean the objects and invoke actual code
// Callbacks are called from JavaScript code, when the function, that is mapped
// to this (FunctionTemplate) callback, is called

// IWindow
v8::Handle<Value> ScriptingWindow::getWindowDisplayRequiredCallback(const Arguments& args) {
	printf("C++ side : getWindowDisplayCallback called\n");
	if (args.Length() < 1) return v8::Undefined();
	v8::HandleScope scope;
	v8::Handle<Value> arg0 = args[0];
	ScriptingGeneral *sg = new ScriptingGeneral();
	ScriptingWindow *sc=sg->UnwrapClass<ScriptingWindow>(v8::Handle<Object>::Cast(arg0));
	sc->getWindowDisplayRequired();
	return v8::Undefined();
}

v8::Handle<Value> ScriptingWindow::showRequiredCallback(const Arguments& args) {
	printf("C++ side : showCallback called\n");
	if (args.Length() < 1) return v8::Undefined();
	v8::HandleScope scope;
	v8::Handle<Value> arg0 = args[0];
	ScriptingGeneral *sg = new ScriptingGeneral();
	ScriptingWindow *sc=sg->UnwrapClass<ScriptingWindow>(v8::Handle<Object>::Cast(arg0));
	sc->showRequired();
	return v8::Undefined();
}

v8::Handle<Value> ScriptingWindow::hideRequiredCallback(const Arguments& args) {
	printf("C++ side : hideCallback called\n");
	if (args.Length() < 1) return v8::Undefined();
	v8::HandleScope scope;
	v8::Handle<Value> arg0 = args[0];
	ScriptingGeneral *sg = new ScriptingGeneral();
	ScriptingWindow *sc=sg->UnwrapClass<ScriptingWindow>(v8::Handle<Object>::Cast(arg0));
	sc->hideRequired();
	return v8::Undefined();
}

v8::Handle<Value> ScriptingWindow::getColorDepthRequiredCallback(const Arguments& args) {
	printf("C++ side : getColorDepthCallback called\n");
	if (args.Length() < 1) return v8::Undefined();
	v8::HandleScope scope;
	v8::Handle<Value> arg0 = args[0];
	ScriptingGeneral *sg = new ScriptingGeneral();
	ScriptingWindow *sc=sg->UnwrapClass<ScriptingWindow>(v8::Handle<Object>::Cast(arg0));
	sc->getColorDepthRequired();
	return v8::Undefined();
}

v8::Handle<Value> ScriptingWindow::makeCurrentopenGlThreadRequiredCallback(const Arguments& args) {
	printf("C++ side : makeCurrentopenGlThreadRequiredCallback called\n");
	if (args.Length() < 1) return v8::Undefined();
	v8::HandleScope scope;
	v8::Handle<Value> arg0 = args[0];
	ScriptingGeneral *sg = new ScriptingGeneral();
	ScriptingWindow *sc=sg->UnwrapClass<ScriptingWindow>(v8::Handle<Object>::Cast(arg0));
	sc->makeCurrentopenGlThreadRequired();
	return v8::Undefined();
}

v8::Handle<Value> ScriptingWindow::openGlSwapBuffersRequiredCallback(const Arguments& args) {
	printf("C++ side : openGlSwapBuffersCallback called\n");
	if (args.Length() < 1) return v8::Undefined();
	v8::HandleScope scope;
	v8::Handle<Value> arg0 = args[0];
	ScriptingGeneral *sg = new ScriptingGeneral();
	ScriptingWindow *sc=sg->UnwrapClass<ScriptingWindow>(v8::Handle<Object>::Cast(arg0));
	sc->openGlSwapBuffersRequired();
	return v8::Undefined();
}

Display* ScriptingWindow::getWindowDisplayRequired() {
	printf("C++ side : getWindowDisplayRequired called\n");
	return ScriptingSpark::myWindow->getWindowDisplay();
}

void ScriptingWindow::showRequired() {
	printf("C++ side : showRequired called\n");
	ScriptingSpark::myWindow->show();
}

void ScriptingWindow::hideRequired() {
	printf("C++ side : hideRequired called\n");
	ScriptingSpark::myWindow->hide();
}

int ScriptingWindow::getColorDepthRequired() {
	printf("C++ side : getColorDepthRequired called\n");
	return ScriptingSpark::myWindow->getColorDepth();
}

void ScriptingWindow::makeCurrentopenGlThreadRequired() {
	printf("C++ side : makeCurrentopenGlThreadRequired called\n");
	ScriptingSpark::myWindow->makeCurrentopenGlThread();
}

void ScriptingWindow::openGlSwapBuffersRequired() {
	printf("C++ side : openGlSwapBuffersRequired called\n");
	ScriptingSpark::myWindow->openGlSwapBuffers();
}

void ScriptingWindow::setProcedures(v8::Handle<ObjectTemplate> global){
	global->Set(String::New("getWindowDisplayRequired"), FunctionTemplate::New(getWindowDisplayRequiredCallback));
	global->Set(String::New("showRequired"), FunctionTemplate::New(showRequiredCallback));
	global->Set(String::New("hideRequired"), FunctionTemplate::New(hideRequiredCallback));
	global->Set(String::New("getColorDepthRequired"), FunctionTemplate::New(getColorDepthRequiredCallback));
	global->Set(String::New("makeCurrentopenGlThreadRequired"), FunctionTemplate::New(makeCurrentopenGlThreadRequiredCallback));
	global->Set(String::New("openGlSwapBuffersRequired"), FunctionTemplate::New(openGlSwapBuffersRequiredCallback));
}
