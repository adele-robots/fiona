#include "ScriptingSpark.h"

#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem)
{
	if (!strcmp(componentType, "ScriptingSpark")) {
		return new ScriptingSpark(componentInstanceName, componentSystem);
	}
	else {
		return NULL;
	}
}
#endif

//Class static members must be defined in source code
	Persistent<v8::Context> ScriptingSpark::context_;
	Persistent<ObjectTemplate> ScriptingSpark::sc_template_;

	 IAnimation *ScriptingSpark::myAnimation;
	 IApplication *ScriptingSpark::myApplication;
	 IAsyncFatalErrorHandler *ScriptingSpark::myAsyncFatalErrorHandler;
	 IAudioQueue *ScriptingSpark::myAudioQueue;
	 ICamera *ScriptingSpark::myCamera;
	 IConcurrent *ScriptingSpark::myConcurrent;
	 IControlVoice *ScriptingSpark::myControlVoice;
	 IDetectedFacePositionConsumer *ScriptingSpark::myDetectedFacePositionConsumer;
	 IEyes *ScriptingSpark::myEyes;
	 IFaceExpression *ScriptingSpark::myFaceExpression;
	 IFrameEventPublisher *ScriptingSpark::myFrameEventPublisher;
	 FrameEventSubscriber *ScriptingSpark::myFrameEventSubscriber;
	 INeck *ScriptingSpark::myNeck;
	 IRenderizable *ScriptingSpark::myRenderizable;
	 IThreadProc *ScriptingSpark::myThreadProc;
	 IVoice *ScriptingSpark::myVoice;
	 IWindow *ScriptingSpark::myWindow;

// Initializes the ScriptingSpark component.

void ScriptingSpark::init(void)
{
	// Create a handle scope to hold the temporary references.
		v8::HandleScope handle_scope;

	// Create a template for the global object where we set the built-in global functions.
		v8::Handle<ObjectTemplate> global = ObjectTemplate::New();

	//Set all Procedures
		ScriptingGeneral *sg= new ScriptingGeneral();
		sg->setAllProcedures(global);

	// Each processor gets its own context so different processors don't affect each other.
	// Context::New returns a persistent handle which
	// is what we need for the reference to remain after we return from this method.
	// That persistent handle has to be disposed in the destructor.
		context_ = v8::Context::New(NULL, global);

	// Enter the new context so all the following operations take place within it.
		v8::Context::Scope context_scope(context_);

	// Set the Component object as a property on the global object.
		if (sc_template_.IsEmpty()) {
			v8::HandleScope handle_scope2;
			v8::Handle<ObjectTemplate> result = v8::ObjectTemplate::New();
			result->SetInternalFieldCount(1);
			//Again, return the result through the current handle scope.
			v8::Handle<ObjectTemplate> raw_template =handle_scope2.Close(result);
			sc_template_ = v8::Persistent<ObjectTemplate>::New(raw_template);
		}

		v8::Handle<ObjectTemplate> templ = sc_template_;

		v8::Handle<Object> result = templ->NewInstance();

		v8::Handle<External> sc_ptr = v8::External::New(this);

		result->SetInternalField(0, sc_ptr);

		v8::HandleScope new_scope;
		v8::Handle<Object> comp_obj=new_scope.Close(result);

	// Set the component object as a property on the global object.
		context_->Global()->Set(v8::String::New("component"), comp_obj);

		// Read .js file. This should be changed and taken from configuration somehow
		char jsPath[256];

		//nameUser is the path of user, in this case, script.js should be in ApplicationDataDevelopment
		char* nameUser= getenv("PSISBAN_APPLICATION_DATA");
		getComponentConfiguration()->getString("JSPath", jsPath, 256);
		std::string final(std::string(nameUser) + std::string(jsPath) );

		//printf("Voy a mostrar el valor de final: %s \n", final.c_str());
		script_=sg->ReadFile(final);

		if (script_.IsEmpty()) {
			fprintf(stderr, "Error reading script.js.\n");
			return;
		}

	// Compile and run the script
		if (!ExecuteScript(script_))
			return;

		ScriptingGeneral *sg3= new ScriptingGeneral();
		sg3->ChargeAllFunctions(context_,global);

		return;
}

void ScriptingSpark::setContext(Persistent<v8::Context> contextPasado_){
	context_= contextPasado_;
}

Persistent<v8::Context> ScriptingSpark::getContext(){
	return context_;
}

/// Uninitializes the ScriptingSpark component.

void ScriptingSpark::quit(void) {
	// Dispose the persistent handles.  When noone else has any
	// references to the objects stored in the handles they will be
	// automatically reclaimed.

		context_.Dispose();
		ScriptingGeneral *sg = new ScriptingGeneral();
		sg->quitAll();
}

bool ScriptingSpark::ExecuteScript(Handle<String> script) {
	v8::HandleScope handle_scope;

	// We're just about to compile the script; set up an error handler to
	// catch any exceptions the script might throw.
		TryCatch try_catch;

	// Compile the script and check for errors.
		v8::Handle<Script> compiled_script = Script::Compile(script);
		if (compiled_script.IsEmpty()) {
				String::Utf8Value error(try_catch.Exception());
				ScriptingGeneral *sg=new ScriptingGeneral();
				sg->Log(*error);
			// The script failed to compile; bail out.
				return false;
		}

		// Run the script!
		v8::Handle<Value> result = compiled_script->Run();
		if (result.IsEmpty()) {
			// The TryCatch above is still in effect and will have caught the error.
				String::Utf8Value error(try_catch.Exception());
				ScriptingGeneral *sg=new ScriptingGeneral();
				sg->Log(*error);
			// Running the script failed; bail out.
				return false;
		}
		return true;
}

// IAnimation implementation
void  ScriptingSpark::playAnimation(char *animationFileName){
	ScriptingAnimation *sa= new ScriptingAnimation();
	sa->playAnimation(animationFileName);
}

// IApplication implementation
void ScriptingSpark::run(){
	ScriptingApplication *sap = new ScriptingApplication();
	sap->run();
}

//IAsyncFatalErrorHandler implementation
void ScriptingSpark::handleError(char *msg){
	ScriptingAsyncFatalErrorHandler *safe= new ScriptingAsyncFatalErrorHandler();
	safe->handleError(msg);
}

//IAudioQueue implementation
int ScriptingSpark::getStoredAudioSize(){
	ScriptingAudioQueue *saq = new ScriptingAudioQueue();
	return saq->getStoredAudioSize();
}
void ScriptingSpark::dequeueAudioBuffer(char *buffer, int size){
	ScriptingAudioQueue *saq = new ScriptingAudioQueue();
	saq->dequeueAudioBuffer(buffer,size);
}
void ScriptingSpark::queueAudioBuffer(char *buffer, int size){
	ScriptingAudioQueue *saq = new ScriptingAudioQueue();
	saq->queueAudioBuffer(buffer,size);
}

//ICamera implementation
void ScriptingSpark::setCameraPosition(float X,float Y,float Z){
	ScriptingCamera *sc= new ScriptingCamera();
	sc->setCameraPosition(X,Y,Z);
}
void ScriptingSpark::setCameraRotation(float X,float Y,float Z){
	ScriptingCamera *sc= new ScriptingCamera();
	sc->setCameraRotation(X,Y,Z);
}
void ScriptingSpark::setCameraParameters(bool IsOrtho,float VisionAngle, float nearClippingPlane,float FarClippingPlane){
	ScriptingCamera *sc= new ScriptingCamera();
	sc->setCameraParameters(IsOrtho,VisionAngle,nearClippingPlane,FarClippingPlane);
}

//IConcurrent implementation
void ScriptingSpark::start(){
	ScriptingConcurrent *sco = new ScriptingConcurrent();
	sco->start();
}
void ScriptingSpark::stop(){
	ScriptingConcurrent *sco = new ScriptingConcurrent();
	sco->stop();
}

//IControlVoice implementation
void ScriptingSpark::startSpeaking(void){
	ScriptingControlVoice *scv = new ScriptingControlVoice();
	scv->startSpeaking();
}
void ScriptingSpark::stopSpeaking(void){
	ScriptingControlVoice *scv = new ScriptingControlVoice();
	scv->stopSpeaking();
}
void ScriptingSpark::startVoice(void){
	ScriptingControlVoice *scv = new ScriptingControlVoice();
	scv->startVoice();
}

//IDetectedFacePositionConsumer implementation
void ScriptingSpark::consumeDetectedFacePosition(bool isFaceDetected, double x, double y){
	ScriptingDetectedFacePositionConsumer *sdfpc = new ScriptingDetectedFacePositionConsumer();
	sdfpc->consumeDetectedFacePosition(isFaceDetected,x,y);
}

//IEyes implementation
void ScriptingSpark::rotateEye(float pan,float tilt){
	ScriptingEyes *se = new ScriptingEyes();
	se->rotateEye(pan,tilt);
}

//IFaceExpression implementation
void ScriptingSpark::setFaceExpression(char *expressionName,float intensity){
	ScriptingFaceExpression *sfe = new ScriptingFaceExpression();
	sfe->setFaceExpression(expressionName, intensity);
}

//IFrameEventPublisher implementation
void ScriptingSpark::addFrameEventSubscriber(FrameEventSubscriber *frameEventSubscriber){
	ScriptingFrameEventPublisher *sfep = new ScriptingFrameEventPublisher();
	sfep->addFrameEventSubscriber(frameEventSubscriber);
}

//IFrameEventSubscriber implementation
void ScriptingSpark::notifyFrameEvent(){
	ScriptingFrameEventSubscriber *sfes = new ScriptingFrameEventSubscriber();
	sfes->notifyFrameEvent();
}

//INeck implementation
void ScriptingSpark::rotateHead(float pan, float tilt){
	ScriptingNeck *sn= new ScriptingNeck();
	sn->rotateHead(pan,tilt);
}

//IRenderizable implementation
void ScriptingSpark::render(void){
	ScriptingRenderizable *sr= new ScriptingRenderizable();
	sr->render();
}

H3DRes ScriptingSpark::getCamaraNode(){
	ScriptingRenderizable *sr= new ScriptingRenderizable();
	return sr->getCamaraNode();
}

//IThreadProc implementation
void ScriptingSpark::process(){
	ScriptingThreadProc *stp = new ScriptingThreadProc();
	stp->process();
}

//IVoice implementation
void ScriptingSpark::sayThis(char *prompt){
	ScriptingVoice *sv= new ScriptingVoice();
	sv->sayThis(prompt);
}

void ScriptingSpark::waitEndOfSpeech(void){
	ScriptingVoice *sv= new ScriptingVoice();
	sv->waitEndOfSpeech();
}

void ScriptingSpark::stopSpeech(void){
	ScriptingVoice *sv= new ScriptingVoice();
	sv->stopSpeech();
}

//IWindow implementation
Display* ScriptingSpark::getWindowDisplay(void){
	ScriptingWindow *sw = new ScriptingWindow();
	return sw->getWindowDisplay();
}

void ScriptingSpark::show(void){
	ScriptingWindow *sw = new ScriptingWindow();
	sw->show();
}

void ScriptingSpark::hide(void){
	ScriptingWindow *sw = new ScriptingWindow();
	sw->hide();
}

int ScriptingSpark::getColorDepth(void){
	ScriptingWindow *sw = new ScriptingWindow();
	return sw->getColorDepth();
}

void ScriptingSpark::makeCurrentopenGlThread(void){
	ScriptingWindow *sw = new ScriptingWindow();
	sw->makeCurrentopenGlThread();
}

void ScriptingSpark::openGlSwapBuffers(void){
	ScriptingWindow *sw = new ScriptingWindow();
	sw->openGlSwapBuffers();
}

