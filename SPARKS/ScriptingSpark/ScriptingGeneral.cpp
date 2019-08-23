#include "ScriptingGeneral.h"

//Declarations interfaces
	ScriptingAnimation sa;
	ScriptingApplication sap;
	ScriptingAsyncFatalErrorHandler	saf;
	ScriptingAudioQueue	saq;
	ScriptingCamera	scam;
	ScriptingConcurrent	sco;
	ScriptingControlVoice scv;
	ScriptingDetectedFacePositionConsumer sdf;
	ScriptingEyes se;
	ScriptingFaceExpression	sfe;
	ScriptingFrameEventPublisher sfep;
	ScriptingFrameEventSubscriber sfes;
	ScriptingNeck sn;
	ScriptingRenderizable sr;
	ScriptingThreadProc	stp;
	ScriptingVoice	sv;
	ScriptingWindow sw;

//ObjectTemplates
	static Persistent<ObjectTemplate> sc_template_;
	Persistent<ObjectTemplate> ScriptingGeneral::class_template_;

void ScriptingGeneral::Log(const char* event) {
	printf("C++ side : Log called\n");
	printf("Logged: %s\n", event);
}

v8::Handle<Value> ScriptingGeneral::LogCallback(const Arguments& args) {
	printf("C++ side : LogCallback called\n");
	if (args.Length() < 1) return v8::Undefined();
	v8::HandleScope scope;
	v8::Handle<Value> arg0 = args[0];
	ScriptingGeneral *sgAux= new ScriptingGeneral();
	ScriptingGeneral *sg = sgAux->UnwrapClass<ScriptingGeneral>(v8::Handle<Object>::Cast(arg0));
	v8::Handle<Value> arg1 = args[1];
	v8::String::Utf8Value value(arg1);
	sg->Log(*value);
	return v8::Undefined();
}

// Reads a file into a v8 string.
v8::Handle<String> ScriptingGeneral::ReadFile(const string& name) {
	FILE* file = fopen(name.c_str(), "rb");
	if (file == NULL) return v8::Handle<String>();

	fseek(file, 0, SEEK_END);
	int size = ftell(file);
	rewind(file);

	char* chars = new char[size + 1];
	chars[size] = '\0';
	for (int i = 0; i < size;) {
		int read = fread(&chars[i], 1, size - i, file);
		i += read;
	}
	fclose(file);
	v8::Handle<String> result = String::New(chars, size);
	delete[] chars;
	return result;
}


void ScriptingGeneral::setAllProcedures(v8::Handle<ObjectTemplate> global){
	//Set procedures accesible from JavaScript here
		global->Set(String::New("log"), FunctionTemplate::New(LogCallback));
	//IAnimation
		sa.setProcedures(global);
	//IApplication
		sap.setProcedures(global);
	//IAsyncFatalErrorHandler
		saf.setProcedures(global);
	//IAudioQueue
		saq.setProcedures(global);
	//ICamera
		scam.setProcedures(global);
	//IConcurrent
		sco.setProcedures(global);
	//IControlVoice
		scv.setProcedures(global);
	//IDetectedFacePositionConsumer
		sdf.setProcedures(global);
	//IEyes
		se.setProcedures(global);
	//IFaceExpression
		sfe.setProcedures(global);
	//IFrameEventPublisher
		sfep.setProcedures(global);
	//IFrameEventSubscriber
		sfes.setProcedures(global);
	//INeck
		sn.setProcedures(global);
	//IRenderizable
		sr.setProcedures(global);
	//IThreadProc
		stp.setProcedures(global);
	//IVoice
		sv.setProcedures(global);
	//IWindow
		sw.setProcedures(global);
}

void ScriptingGeneral::quitAll(){
	//IAnimation
		sa.quit();
	//IApplication
		sap.quit();
	//IAsyncFatalErrorHandler
		saf.quit();
	//IAudioQueue
		saq.quit();
	//ICamera
		scam.quit();
	//IConcurrent
		sco.quit();
	//IControlVoice
		scv.quit();
	//IDetectedFacePositionConsumer
		sdf.quit();
	//IEyes
		se.quit();
	//IFaceExpression
		sfe.quit();
	//IFrameEventPublisher
		sfep.quit();
	//FrameEventSubscriber
		sfes.quit();
	//INeck
		sn.quit();
	//IRenderizable
		sr.quit();
	//IThreadProc
		stp.quit();
	//IVoice
		sv.quit();
	//IWindow
		sw.quit();

}

void ScriptingGeneral::ChargeAllFunctions(Persistent<v8::Context> context_, v8::Handle<ObjectTemplate>global){

	//IAnimation
		sa.chargeFunctions(context_,global);
	//IAsyncFatalErrorHandler
		saf.chargeFunctions(context_,global);
	//IAudioQueue
		saq.chargeFunctions(context_,global);
	//ICamera
		scam.chargeFunctions(context_,global);
	//IConcurrent
		sco.chargeFunctions(context_,global);
	//IControlVoice
		scv.chargeFunctions(context_,global);
	//IDetectedFacePositionConsumer
		sdf.chargeFunctions(context_,global);
	//IEyes
		se.chargeFunctions(context_,global);
	//IFaceExpression
		sfe.chargeFunctions(context_,global);
	//IFrameEventPublisher
		sfep.chargeFunctions(context_,global);
	//IFrameEventSubscriber
		sfes.chargeFunctions(context_,global);
	//INeck
		sn.chargeFunctions(context_,global);
	//IRenderizable
		sr.chargeFunctions(context_,global);
	//IThreadProc
		stp.chargeFunctions(context_,global);
	//IVoice
		sv.chargeFunctions(context_,global);
	//IWindow
		sw.chargeFunctions(context_,global);
}

/******************************* FUNCTIONS TEMPLATES ***************************************/

v8::Handle<ObjectTemplate> ScriptingGeneral::MakeCharTemplate() {
		v8::HandleScope handle_scope;
		v8::Handle<ObjectTemplate> result = ObjectTemplate::New();
		result->SetInternalFieldCount(1);
	//Again, return the result through the current handle scope.
		return handle_scope.Close(result);
}

v8::Handle<ObjectTemplate> ScriptingGeneral::MakeFrameEventSubscriberTemplate() {
		v8::HandleScope handle_scope;
		v8::Handle<ObjectTemplate> result = ObjectTemplate::New();
		result->SetInternalFieldCount(1);
	//Again, return the result through the current handle scope.
		return handle_scope.Close(result);
}


