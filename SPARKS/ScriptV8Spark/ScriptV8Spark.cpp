/*
 * ScriptV8Spark.cpp
 *
 *  Created on: 15/11/2012
 *      Author: guille
 */

/// @file ScriptV8Spark.cpp
/// @brief ScriptV8Spark class implementation.

//#include "stdAfx.h"
#include "ScriptV8Spark.h"
#include <unistd.h>
#include <segvcatch.h>
#include <stdexcept>

#ifdef _WIN32
#else
extern "C" Component *createComponent(char *componentInstanceName,
		char *componentType, ComponentSystem *componentSystem) {
	if (!strcmp(componentType, "ScriptV8Spark")) {
		return new ScriptV8Spark(componentInstanceName, componentSystem);
	} else {
		return NULL;
	}
}
#endif

//Static definitions
Persistent<ObjectTemplate> ScriptV8Spark::sc_template_;

// Extracts a C string from a V8 Utf8Value.
const char* ToCString(const String::Utf8Value& value) {
	return *value ? *value : "<string conversion failed>";
}

//Unwrap 'obj' from Javascript side into a 'class T' type pointer
template<class T> T* UnwrapClass(Handle<Object> obj) {
	Handle<External> field = Handle<External>::Cast(obj->GetInternalField(0));
	void* ptr = field->Value();
	return static_cast<T *>(ptr);
}

// SIGSEGV handler
void handle_segv()
{
    throw std::runtime_error("SEGV Exception");
}

/// Initializes ScriptV8Spark component.
void ScriptV8Spark::init(void) {
	segvcatch::init_segv(&handle_segv);
}

/// Unitializes the ScriptV8Spark component.
void ScriptV8Spark::quit(void) {
	// Dispose the persistent handles.  When no one else has any
	// references to the objects stored in the handles they will be
	// automatically reclaimed.

	/** !!!!!!!!!!!!!!!!!!! HAY QUE LIMPIAR MÁS COSAS !!!!!!!!!!!!!!!!!!!!! **/
	context_.Dispose();
}

void ScriptV8Spark::process() {
	static bool firstTime = true;
	if (firstTime) {
		firstTime = false;
		Locker locker;

		// Create a handle scope to hold the temporary references.
		HandleScope handle_scope;

		// Create a template for the global object where we set the
		// built-in global functions.
		Handle<ObjectTemplate> global = ObjectTemplate::New();
		global->Set(String::New("log"),
				FunctionTemplate::New(LogCallback));
		global->Set(String::New("print"),
				FunctionTemplate::New(PrintCallback));
		global->Set(String::New("alert"),
				FunctionTemplate::New(AlertCallback));
		global->Set(String::New("usleep"),
				FunctionTemplate::New(SleepCallback));
		global->Set(String::New("setFaceExpressionRequired"),
				FunctionTemplate::New(setFaceExpressionRequiredCallback));
		global->Set(String::New("setCameraPositionRequired"),
				FunctionTemplate::New(setCameraPositionRequiredCallback));
		global->Set(String::New("setCameraRotationRequired"),
				FunctionTemplate::New(setCameraRotationRequiredCallback));
		global->Set(String::New("setCameraParametersRequired"),
				FunctionTemplate::New(setCameraParametersRequiredCallback));
		global->Set(String::New("rotateHeadRequired"),
				FunctionTemplate::New(rotateHeadRequiredCallback));
		global->Set(String::New("rotateEyeRequired"),
				FunctionTemplate::New(rotateEyeRequiredCallback));
		global->Set(String::New("processDataRequired"),
				FunctionTemplate::New(processDataRequiredCallback));
		global->Set(String::New("startSpeakingRequired"),
				FunctionTemplate::New(startSpeakingRequiredCallback));
		global->Set(String::New("stopSpeakingRequired"),
				FunctionTemplate::New(stopSpeakingRequiredCallback));
		global->Set(String::New("startVoiceRequired"),
				FunctionTemplate::New(startVoiceRequiredCallback));
		global->Set(String::New("playAnimationRequired"),
				FunctionTemplate::New(playAnimationRequiredCallback));
		global->Set(String::New("setJointTransMatRequired"),
				FunctionTemplate::New(setJointTransMatRequiredCallback));
		global->Set(String::New("rotateJointPartRequired"),
				FunctionTemplate::New(rotateJointPartRequiredCallback));
		global->Set(String::New("moveJointPartRequired"),
				FunctionTemplate::New(moveJointPartRequiredCallback));
		global->Set(String::New("moveDiamondJointRequired"),
				FunctionTemplate::New(moveDiamondJointRequiredCallback));
		global->Set(String::New("GetJointRotationRequired"),
				FunctionTemplate::New(GetJointRotationRequiredCallback));
		global->Set(String::New("GetJointPositionRequired"),
				FunctionTemplate::New(GetJointPositionRequiredCallback));
		global->Set(String::New("findNodeRequired"),
				FunctionTemplate::New(findNodeRequiredCallback));

		// Each processor gets its own context so different processors don't affect each other.
		// Context::New returns a persistent handle which
		// is what we need for the reference to remain after we return from this method.
		// That persistent handle has to be disposed in the destructor.
		context_ = Context::New(NULL, global);

		// Enter the new context so all the following operations take place
		// within it.
		Context::Scope context_scope(context_);

		// Set the Component object as a property on the global object.
		if (sc_template_.IsEmpty()) {
			HandleScope handle_scope2;
			Handle<ObjectTemplate> result = ObjectTemplate::New();
			result->SetInternalFieldCount(1);
			//Again, return the result through the current handle scope.
			Handle<ObjectTemplate> raw_template = handle_scope2.Close(result);
			sc_template_ = Persistent<ObjectTemplate>::New(raw_template);
		}

		Handle<ObjectTemplate> templ = sc_template_;
		Handle<Object> result = templ->NewInstance();
		Handle<External> sc_ptr = External::New(this);

		result->SetInternalField(0, sc_ptr);

		HandleScope new_scope;
		Handle<Object> comp_obj = new_scope.Close(result);

		// Set the component object as a property on the global object.
		context_->Global()->Set(String::New("component"), comp_obj);

		char userSparkData[1024];
		getComponentConfiguration()->getString(const_cast<char*>("User_Spark_Data"), userSparkData, 1024);
		char jsFilename[1024];
		getComponentConfiguration()->getString(const_cast<char*>("Script_Filename"), jsFilename, 1024);
		std::string final(std::string(userSparkData) + std::string(jsFilename) );

		script_ = ReadFile(final);

		if (script_.IsEmpty()) {
			fprintf(stderr, "Error reading script.js.\n");
			return;
		}

		// Compile and run the script
		if (!ExecuteScript(script_))
			return;

		//Map functions implemented in Javascript
		setFaceExpression_ = getFunction(
				const_cast<char*>("setFaceExpression"));
		if (setFaceExpression_.IsEmpty()) {
			fprintf(stderr, "Error retrieving setFaceExpression function\n");
			return;
		}
		setCameraPosition_ = getFunction(
				const_cast<char*>("setCameraPosition"));
		if (setCameraPosition_.IsEmpty()) {
			fprintf(stderr, "Error retrieving setCameraPosition function\n");
			return;
		}
		setCameraRotation_ = getFunction(
				const_cast<char*>("setCameraRotation"));
		if (setCameraRotation_.IsEmpty()) {
			fprintf(stderr, "Error retrieving setCameraRotation function\n");
			return;
		}
		setCameraParameters_ = getFunction(
				const_cast<char*>("setCameraParameters"));
		if (setCameraParameters_.IsEmpty()) {
			fprintf(stderr, "Error retrieving setCameraParameters function\n");
			return;
		}
		notifyFrameEvent_ = getFunction(const_cast<char*>("notifyFrameEvent"));
		if (notifyFrameEvent_.IsEmpty()) {
			fprintf(stderr, "Error retrieving notifyFrameEvent function\n");
			return;
		}
		processData_ = getFunction(const_cast<char*>("processData"));
		if (processData_.IsEmpty()) {
			fprintf(stderr, "Error retrieving processData function\n");
			return;
		}
		startSpeaking_ = getFunction(const_cast<char*>("startSpeaking"));
		if (startSpeaking_.IsEmpty()) {
			fprintf(stderr, "Error retrieving startSpeaking function\n");
			return;
		}
		stopSpeaking_ = getFunction(const_cast<char*>("stopSpeaking"));
		if (stopSpeaking_.IsEmpty()) {
			fprintf(stderr, "Error retrieving stopSpeaking function\n");
			return;
		}
		startVoice_ = getFunction(const_cast<char*>("startVoice"));
		if (startVoice_.IsEmpty()) {
			fprintf(stderr, "Error retrieving startVoice function\n");
			return;
		}
	}
	//puts("[ScriptV8Spark::process] - Running !!");
	usleep(200000);
}

// Reads a file into a v8 string.
Handle<String> ScriptV8Spark::ReadFile(const string& name) {
	FILE* file = fopen(name.c_str(), "rb");
	if (file == NULL)
		return Handle<String>();

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
	Handle<String> result = String::New(chars, size);
	delete[] chars;
	return result;
}

bool ScriptV8Spark::ExecuteScript(Handle<String> script) {
	HandleScope handle_scope;

	// We're just about to compile the script; set up an error handler to
	// catch any exceptions the script might throw.
	TryCatch try_catch;

	// Compile the script and check for errors.
	Handle<Script> compiled_script = Script::Compile(script);
	if (compiled_script.IsEmpty()) {
		String::Utf8Value error(try_catch.Exception());
		Log(*error, 3);
		// The script failed to compile; bail out.
		return false;
	}

	// Run the script!
	Handle<Value> result = compiled_script->Run();
	if (result.IsEmpty()) {
		// The TryCatch above is still in effect and will have caught the error.
		String::Utf8Value error(try_catch.Exception());
		Log(*error, 3);
		// Running the script failed; bail out.
		return false;
	}
	return true;
}

Persistent<Function> ScriptV8Spark::getFunction(char *functionName) {
	// The script compiled and ran correctly.  Now we fetch out the
	// function from the global object.
	Handle<String> function_name = String::New(functionName);
	Handle<Value> function_val = context_->Global()->Get(function_name);
	Persistent<Function> result;
	// If there is no function, or if it is not a function, bail out
	if (!function_val->IsFunction())
		return result;

	// It is a function; cast it to a Function
	Handle<Function> function_fun = Handle<Function>::Cast(function_val);

	result = Persistent<Function>::New(function_fun);

	return result;
}

Handle<Value> ScriptV8Spark::LogCallback(const Arguments& args) {
	Locker locker;
	if (args.Length() < 2) {
		Log("LogCallback called with insuficient arguments", 3);
		return Undefined();
	}
	HandleScope scope;
	Handle<Value> arg0 = args[0];
	String::Utf8Value value(arg0);
	Handle<Value> arg1 = args[1];
	int logLevel = static_cast<int>(arg1->NumberValue());

	ScriptV8Spark::Log(*value, logLevel);
	return Undefined();
}

void ScriptV8Spark::Log(const char* event, int logLevel) {
	std::string message("[FIONA-logger] ");
	message = message + event;
	switch (logLevel) {
	case 1:
		LoggerInfo(message.c_str());
		break;
	case 2:
		LoggerWarn(message.c_str());
		break;
	case 3:
		LoggerError(message.c_str());
		break;
	}
}

// The callback that is invoked by v8 whenever the JavaScript 'print'
// function is called.  Prints its arguments on stdout separated by
// spaces and ending with a newline.
Handle<Value> ScriptV8Spark::PrintCallback(const Arguments& args) {
	Locker locker;
	bool first = true;
	for (int i = 0; i < args.Length(); i++) {
		HandleScope handle_scope;
		if (first) {
			first = false;
		} else {
			printf(" ");
		}
		String::Utf8Value str(args[i]);
		const char* cstr = ToCString(str);
		printf("%s", cstr);
	}
	printf("\n");
	fflush(stdout);
	return Undefined();
}

// The callback that is invoked by v8 whenever the JavaScript 'alert'
// function is called.  Displays a GTK+ notification.
Handle<Value> ScriptV8Spark::AlertCallback(const Arguments& args) {
	Locker locker;
	/*String::Utf8Value str(args[0]); // Convert first argument to V8 String
	const char* cstr = ToCString(str); // Convert V8 String to C string*/
	Log("Alert function is not implemented", 2);
	/*
	 //Codigo para C con libnotify ; #include <libnotify/libnotify.h>
	 notify_init ("Basic");
	 NotifyNotification * n = notify_notification_new ("Alert from Javascript", cstr, "dialog-information");
	 notify_notification_show (n, NULL);
	 */
	return Undefined();
}

//Called from Javascript side
Handle<Value> ScriptV8Spark::SleepCallback(const Arguments& args) {
	Locker locker;
	if (args.Length() < 1)
		return Undefined();
	HandleScope scope;
	Handle<Value> arg0 = args[0];
	int val1 = static_cast<int>(arg0->NumberValue());

	usleep(val1);

	return Undefined();
}

/*
 * Interfaces callbacks called from Javascript.
 */

// IFaceExpression method called from Javascript side
Handle<Value> ScriptV8Spark::setFaceExpressionRequiredCallback(
		const Arguments& args) {
	Locker locker;
	if (args.Length() < 3)
		return Undefined();
	HandleScope scope;
	Handle<Value> arg0 = args[0];

	ScriptV8Spark *scriptV8Spark = UnwrapClass<ScriptV8Spark>(
			Handle<Object>::Cast(arg0));

	Handle<Value> arg1 = args[1];
	String::Utf8Value val1(arg1);
	Handle<Value> arg2 = args[2];
	float val2 = static_cast<float>(arg2->NumberValue());

	scriptV8Spark->myFaceExpression->setFaceExpression(*val1, val2);
	return Undefined();
}

// ICamera method called from Javascript side
Handle<Value> ScriptV8Spark::setCameraPositionRequiredCallback(
		const Arguments& args) {
	Locker locker;
	if (args.Length() < 4)
		return Undefined();
	HandleScope scope;
	Handle<Value> arg0 = args[0];

	ScriptV8Spark *scriptV8Spark = UnwrapClass<ScriptV8Spark>(
			Handle<Object>::Cast(arg0));

	Handle<Value> arg1 = args[1];
	Handle<Value> arg2 = args[2];
	Handle<Value> arg3 = args[3];
	float val1 = static_cast<float>(arg1->NumberValue());
	float val2 = static_cast<float>(arg2->NumberValue());
	float val3 = static_cast<float>(arg3->NumberValue());

	scriptV8Spark->myCamera->setCameraPosition(val1, val2, val3);
	return Undefined();
}

Handle<Value> ScriptV8Spark::setCameraRotationRequiredCallback(
		const Arguments& args) {
	Locker locker;
	if (args.Length() < 4)
		return Undefined();
	HandleScope scope;
	Handle<Value> arg0 = args[0];

	ScriptV8Spark *scriptV8Spark = UnwrapClass<ScriptV8Spark>(
			Handle<Object>::Cast(arg0));

	Handle<Value> arg1 = args[1];
	Handle<Value> arg2 = args[2];
	Handle<Value> arg3 = args[3];
	float val1 = static_cast<float>(arg1->NumberValue());
	float val2 = static_cast<float>(arg2->NumberValue());
	float val3 = static_cast<float>(arg3->NumberValue());

	scriptV8Spark->myCamera->setCameraRotation(val1, val2, val3);
	return Undefined();
}

Handle<Value> ScriptV8Spark::setCameraParametersRequiredCallback(
		const Arguments& args) {
	Locker locker;
	if (args.Length() < 5)
		return Undefined();
	HandleScope scope;
	Handle<Value> arg0 = args[0];

	ScriptV8Spark *scriptV8Spark = UnwrapClass<ScriptV8Spark>(
			Handle<Object>::Cast(arg0));

	Handle<Value> arg1 = args[1];
	Handle<Value> arg2 = args[2];
	Handle<Value> arg3 = args[3];
	Handle<Value> arg4 = args[4];
	bool isOrtho = static_cast<bool>(arg1->BooleanValue());
	float val1 = static_cast<float>(arg2->NumberValue());
	float val2 = static_cast<float>(arg3->NumberValue());
	float val3 = static_cast<float>(arg4->NumberValue());

	scriptV8Spark->myCamera->setCameraParameters(isOrtho, val1, val2, val3);
	return Undefined();
}

// INeck method called from Javascript side
Handle<Value> ScriptV8Spark::rotateHeadRequiredCallback(const Arguments& args) {
	Locker locker;
	if (args.Length() < 3)
		return Undefined();
	HandleScope scope;
	Handle<Value> arg0 = args[0];

	ScriptV8Spark *scriptV8Spark = UnwrapClass<ScriptV8Spark>(
			Handle<Object>::Cast(arg0));

	Handle<Value> arg1 = args[1];
	Handle<Value> arg2 = args[2];
	float val1 = static_cast<float>(arg1->NumberValue());
	float val2 = static_cast<float>(arg2->NumberValue());

	scriptV8Spark->myNeck->rotateHead(val1, val2);
	return Undefined();
}

// IEye method called from Javascript side
Handle<Value> ScriptV8Spark::rotateEyeRequiredCallback(const Arguments& args) {
	Locker locker;
	if (args.Length() < 3)
		return Undefined();
	HandleScope scope;
	Handle<Value> arg0 = args[0];

	ScriptV8Spark *scriptV8Spark = UnwrapClass<ScriptV8Spark>(
			Handle<Object>::Cast(arg0));

	Handle<Value> arg1 = args[1];
	Handle<Value> arg2 = args[2];
	float val1 = static_cast<float>(arg1->NumberValue());
	float val2 = static_cast<float>(arg2->NumberValue());

	scriptV8Spark->myEyes->rotateEye(val1, val2);
	return Undefined();
}

// IFlow<char*> method called from Javascript side
Handle<Value> ScriptV8Spark::processDataRequiredCallback(
		const Arguments& args) {
	Locker locker;
	if (args.Length() < 2)
		return Undefined();
	HandleScope scope;
	Handle<Value> arg0 = args[0];

	ScriptV8Spark *scriptV8Spark = UnwrapClass<ScriptV8Spark>(
			Handle<Object>::Cast(arg0));

	Handle<Value> arg1 = args[1];
	String::Utf8Value val1(arg1);

	scriptV8Spark->myFlow->processData(*val1);
	return Undefined();
}

// IControlVoice methods called from Javascript side
Handle<Value> ScriptV8Spark::startSpeakingRequiredCallback(
		const Arguments& args) {
	Locker locker;
	if (args.Length() < 1)
		return Undefined();
	HandleScope scope;
	Handle<Value> arg0 = args[0];

	ScriptV8Spark *scriptV8Spark = UnwrapClass<ScriptV8Spark>(
			Handle<Object>::Cast(arg0));

	Handle<Value> arg1 = args[1];
	String::Utf8Value val1(arg1);

	scriptV8Spark->myControlVoice->startSpeaking();
	return Undefined();
}

Handle<Value> ScriptV8Spark::stopSpeakingRequiredCallback(
		const Arguments& args) {
	Locker locker;
	if (args.Length() < 1)
		return Undefined();
	HandleScope scope;
	Handle<Value> arg0 = args[0];

	ScriptV8Spark *scriptV8Spark = UnwrapClass<ScriptV8Spark>(
			Handle<Object>::Cast(arg0));

	Handle<Value> arg1 = args[1];
	String::Utf8Value val1(arg1);

	scriptV8Spark->myControlVoice->stopSpeaking();
	return Undefined();
}

Handle<Value> ScriptV8Spark::startVoiceRequiredCallback(const Arguments& args) {
	Locker locker;
	if (args.Length() < 1)
		return Undefined();
	HandleScope scope;
	Handle<Value> arg0 = args[0];

	ScriptV8Spark *scriptV8Spark = UnwrapClass<ScriptV8Spark>(
			Handle<Object>::Cast(arg0));

	Handle<Value> arg1 = args[1];
	String::Utf8Value val1(arg1);

	scriptV8Spark->myControlVoice->startVoice();
	return Undefined();
}

// IAnimation method called from Javascript side
Handle<Value> ScriptV8Spark::playAnimationRequiredCallback(
		const Arguments& args) {
	Locker locker;
	if (args.Length() < 2)
		return Undefined();
	HandleScope scope;
	Handle<Value> arg0 = args[0];

	ScriptV8Spark *scriptV8Spark = UnwrapClass<ScriptV8Spark>(
			Handle<Object>::Cast(arg0));

	Handle<Value> arg1 = args[1];
	String::Utf8Value val1(arg1);

	scriptV8Spark->myAnimation->playAnimation(*val1);
	return Undefined();
}

// IJoint methods called from Javascript side
Handle<Value> ScriptV8Spark::setJointTransMatRequiredCallback(
		const Arguments& args) {
	Locker locker;
	if (args.Length() < 3)
		return Undefined();
	HandleScope scope;
	Handle<Value> arg0 = args[0];

	ScriptV8Spark *scriptV8Spark = UnwrapClass<ScriptV8Spark>(
			Handle<Object>::Cast(arg0));

	Handle<Value> arg1 = args[1];
	Handle<Value> arg2 = args[2];
	String::Utf8Value val1(arg1);
	Handle<Array> val2 = Handle<Array>::Cast(arg2);
	if(val2->Length() != 16)
		return Undefined();

	float mat[] = {
			(float)val2->Get(0)->NumberValue(), (float)val2->Get(1)->NumberValue(), (float)val2->Get(2)->NumberValue(), (float)val2->Get(3)->NumberValue(),
			(float)val2->Get(4)->NumberValue(), (float)val2->Get(5)->NumberValue(), (float)val2->Get(6)->NumberValue(), (float)val2->Get(7)->NumberValue(),
			(float)val2->Get(8)->NumberValue(), (float)val2->Get(9)->NumberValue(), (float)val2->Get(10)->NumberValue(), (float)val2->Get(11)->NumberValue(),
			(float)val2->Get(12)->NumberValue(), (float)val2->Get(13)->NumberValue(), (float)val2->Get(14)->NumberValue(), (float)val2->Get(15)->NumberValue()};
	scriptV8Spark->myJoint->setJointTransMat(*val1, mat);
	return Undefined();
}

Handle<Value> ScriptV8Spark::rotateJointPartRequiredCallback(
		const Arguments& args) {
	Locker locker;
	if (args.Length() < 5)
		return Undefined();
	HandleScope scope;
	Handle<Value> arg0 = args[0];

	ScriptV8Spark *scriptV8Spark = UnwrapClass<ScriptV8Spark>(
			Handle<Object>::Cast(arg0));

	Handle<Value> arg1 = args[1];
	Handle<Value> arg2 = args[2];
	Handle<Value> arg3 = args[3];
	Handle<Value> arg4 = args[4];
	String::Utf8Value val1(arg1);
	float val2 = static_cast<float>(arg2->NumberValue());
	float val3 = static_cast<float>(arg3->NumberValue());
	float val4 = static_cast<float>(arg4->NumberValue());

	scriptV8Spark->myJoint->rotateJointPart(*val1, val2, val3, val4);
	return Undefined();
}

Handle<Value> ScriptV8Spark::moveJointPartRequiredCallback(
		const Arguments& args) {
	Locker locker;
	if (args.Length() < 5)
		return Undefined();
	HandleScope scope;
	Handle<Value> arg0 = args[0];

	ScriptV8Spark *scriptV8Spark = UnwrapClass<ScriptV8Spark>(
			Handle<Object>::Cast(arg0));

	Handle<Value> arg1 = args[1];
	Handle<Value> arg2 = args[2];
	Handle<Value> arg3 = args[3];
	Handle<Value> arg4 = args[4];
	String::Utf8Value val1(arg1);
	float val2 = static_cast<float>(arg2->NumberValue());
	float val3 = static_cast<float>(arg3->NumberValue());
	float val4 = static_cast<float>(arg4->NumberValue());

	scriptV8Spark->myJoint->moveJointPart(*val1, val2, val3, val4);
	return Undefined();
}

Handle<Value> ScriptV8Spark::moveDiamondJointRequiredCallback(
		const Arguments& args) {
	Locker locker;
	if (args.Length() < 4)
		return Undefined();
	HandleScope scope;
	Handle<Value> arg0 = args[0];

	ScriptV8Spark *scriptV8Spark = UnwrapClass<ScriptV8Spark>(
			Handle<Object>::Cast(arg0));

	Handle<Value> arg1 = args[1];
	Handle<Value> arg2 = args[2];
	Handle<Value> arg3 = args[3];
	float val1 = static_cast<float>(arg1->NumberValue());
	float val2 = static_cast<float>(arg2->NumberValue());
	float val3 = static_cast<float>(arg3->NumberValue());

	scriptV8Spark->myJoint->moveDiamondJoint(val1, val2, val3);
	return Undefined();
}

Handle<Value> ScriptV8Spark::GetJointRotationRequiredCallback(
		const Arguments& args) {
	Locker locker;
	if (args.Length() < 2)
		return Undefined();
	HandleScope scope;
	Handle<Value> arg0 = args[0];

	ScriptV8Spark *scriptV8Spark = UnwrapClass<ScriptV8Spark>(
			Handle<Object>::Cast(arg0));

	Handle<Value> arg1 = args[1];
	String::Utf8Value val1(arg1);
	float val2 = 0.0;
	float val3 = 0.0;
	float val4 = 0.0;

	scriptV8Spark->myJoint->GetJointRotation(*val1, &val2, &val3, &val4);
	Handle<Array> array = Array::New(3);
	array->Set(0, Number::New(val2));
	array->Set(1, Number::New(val3));
	array->Set(2, Number::New(val4));
	return scope.Close(array);
}

Handle<Value> ScriptV8Spark::GetJointPositionRequiredCallback(
		const Arguments& args) {
	Locker locker;
	if (args.Length() < 2)
		return Undefined();
	HandleScope scope;
	Handle<Value> arg0 = args[0];

	ScriptV8Spark *scriptV8Spark = UnwrapClass<ScriptV8Spark>(
			Handle<Object>::Cast(arg0));

	Handle<Value> arg1 = args[1];
	String::Utf8Value val1(arg1);
	float val2 = 0.0;
	float val3 = 0.0;
	float val4 = 0.0;

	scriptV8Spark->myJoint->GetJointPosition(*val1, &val2, &val3, &val4);
	Handle<Array> array = Array::New(3);
	array->Set(0, Number::New(val2));
	array->Set(1, Number::New(val3));
	array->Set(2, Number::New(val4));
	return scope.Close(array);
}

Handle<Value> ScriptV8Spark::findNodeRequiredCallback(
	const Arguments& args) {
	Locker locker;
	if (args.Length() < 2)
		return Undefined();
	HandleScope scope;
	Handle<Value> arg0 = args[0];

	ScriptV8Spark *scriptV8Spark = UnwrapClass<ScriptV8Spark>(
			Handle<Object>::Cast(arg0));

	Handle<Value> arg1 = args[1];
	String::Utf8Value val1(arg1);

	if(scriptV8Spark->myJoint->findNode(*val1))
		return True();
	else
		return False();
}

/*
 * Interfaces methods implemented in Javascript.
 */

void ScriptV8Spark::notifyFrameEvent() {
	Locker locker;

	// Create a handle scope to keep the temporary object references.
	HandleScope handle_scope;
	// Enter this context so all the remaining operations take place there
	Context::Scope context_scope(context_);

	// Set up an exception handler before calling the Process function
	TryCatch try_catch;
	// Invoke the function implemented in Javascript
	const int argc = 0;
	Handle<Value> argv[argc] = { };

	Handle<Value> result = notifyFrameEvent_->Call(context_->Global(), argc,
			argv);
	if (result.IsEmpty()) {
		String::Utf8Value error(try_catch.Exception());
		Log(*error, 3);
	}
	return;
}

// IFaceExpression implementation
void ScriptV8Spark::setFaceExpression(char *expressionName, float intensity) {
	Locker locker;

	// Create a handle scope to keep the temporary object references.
	HandleScope handle_scope;
	// Enter this context so all the remaining operations take place there
	Context::Scope context_scope(context_);

	// Wrap the C++ request objects in a JavaScript wrapper
	Handle<Value> par_char = String::New(expressionName);
	Handle<Value> par_float = Number::New(intensity);

	// Set up an exception handler before calling the Process function
	TryCatch try_catch;

	// Invoke the function implemented in Javascript
	const int argc = 2;
	Handle<Value> argv[argc] = { par_char, par_float };

	Handle<Value> result = setFaceExpression_->Call(context_->Global(), argc,
			argv);
	if (result.IsEmpty()) {
		String::Utf8Value error(try_catch.Exception());
		Log(*error, 3);
	}
	return;
}

//ICamera implementation
void ScriptV8Spark::setCameraPosition(float x, float y, float z) {
	Locker locker;

	// Create a handle scope to keep the temporary object references.
	HandleScope handle_scope;
	// Enter this context so all the remaining operations take place there
	Context::Scope context_scope(context_);

	// Wrap the C++ request objects in a JavaScript wrapper
	Handle<Value> float_x = Number::New(x);
	Handle<Value> float_y = Number::New(y);
	Handle<Value> float_z = Number::New(z);

	// Set up an exception handler before calling the Process function
	TryCatch try_catch;

	// Invoke the function implemented in Javascript
	const int argc = 3;
	Handle<Value> argv[argc] = { float_x, float_y, float_z };

	Handle<Value> result = setCameraPosition_->Call(context_->Global(), argc,
			argv);
	if (result.IsEmpty()) {
		String::Utf8Value error(try_catch.Exception());
		Log(*error, 3);
	}
	return;
}

void ScriptV8Spark::setCameraRotation(float x, float y, float z) {
	Locker locker;

	// Create a handle scope to keep the temporary object references.
	HandleScope handle_scope;
	// Enter this context so all the remaining operations take place there
	Context::Scope context_scope(context_);

	// Wrap the C++ request objects in a JavaScript wrapper
	Handle<Value> float_x = Number::New(x);
	Handle<Value> float_y = Number::New(y);
	Handle<Value> float_z = Number::New(z);

	// Set up an exception handler before calling the Process function
	TryCatch try_catch;

	// Invoke the function implemented in Javascript
	const int argc = 3;
	Handle<Value> argv[argc] = { float_x, float_y, float_z };

	Handle<Value> result = setCameraRotation_->Call(context_->Global(), argc,
			argv);
	if (result.IsEmpty()) {
		String::Utf8Value error(try_catch.Exception());
		Log(*error, 3);
	}
	return;
}

void ScriptV8Spark::setCameraParameters(bool isOrtho, float visionAngle,
		float nearClippingPlane, float farClippingPlane) {
	Locker locker;

	// Create a handle scope to keep the temporary object references.
	HandleScope handle_scope;
	// Enter this context so all the remaining operations take place there
	Context::Scope context_scope(context_);

	// Wrap the C++ request objects in a JavaScript wrapper
	Handle<Value> boolean_val = Boolean::New(isOrtho);
	Handle<Value> float_1 = Number::New(visionAngle);
	Handle<Value> float_2 = Number::New(nearClippingPlane);
	Handle<Value> float_3 = Number::New(farClippingPlane);

	// Set up an exception handler before calling the Process function
	TryCatch try_catch;

	// Invoke the function implemented in Javascript
	const int argc = 4;
	Handle<Value> argv[argc] = { boolean_val, float_1, float_2, float_3 };

	Handle<Value> result = setCameraParameters_->Call(context_->Global(), argc,
			argv);
	if (result.IsEmpty()) {
		String::Utf8Value error(try_catch.Exception());
		Log(*error, 3);
	}
	return;
}

//IFlow<char*> implementation
void ScriptV8Spark::processData(char *text) {
	Locker locker;

	// Create a handle scope to keep the temporary object references.
	HandleScope handle_scope;
	// Enter this context so all the remaining operations take place there
	Context::Scope context_scope(context_);
	// Wrap the C++ request objects in a JavaScript wrapper
	Handle<Value> text_value = String::New(text);

	// Set up an exception handler before calling the Process function
	TryCatch try_catch;
	// Invoke the function implemented in Javascript
	const int argc = 1;
	Handle<Value> argv[argc] = { text_value };
	Handle<Value> result = processData_->Call(context_->Global(), argc, argv);
	if (result.IsEmpty()) {
		String::Utf8Value error(try_catch.Exception());
		Log(*error, 3);
	}
	return;
}

//IControlVoice implementation
void ScriptV8Spark::startSpeaking() {
	while(1) {
		try {
			Locker locker;

			// Create a handle scope to keep the temporary object references.
			HandleScope handle_scope;
			// Enter this context so all the remaining operations take place there
			Context::Scope context_scope(context_);

			// Set up an exception handler before calling the Process function
			TryCatch try_catch;
			// Invoke the function implemented in Javascript
			const int argc = 0;
			Handle<Value> argv[argc] = { };

			Handle<Value> result = startSpeaking_->Call(context_->Global(), argc, argv);
			if (result.IsEmpty()) {
				String::Utf8Value error(try_catch.Exception());
				Log(*error, 3);
			}
			return;
		} catch(std::exception& e) {
			LoggerWarn("ScriptV8Spark::startSpeaking %s", e.what());
			usleep(200000);
		}
	}
}

void ScriptV8Spark::stopSpeaking() {
	Locker locker;

	// Create a handle scope to keep the temporary object references.
	HandleScope handle_scope;
	// Enter this context so all the remaining operations take place there
	Context::Scope context_scope(context_);

	// Set up an exception handler before calling the Process function
	TryCatch try_catch;
	// Invoke the function implemented in Javascript
	const int argc = 0;
	Handle<Value> argv[argc] = { };

	Handle<Value> result = stopSpeaking_->Call(context_->Global(), argc, argv);
	if (result.IsEmpty()) {
		String::Utf8Value error(try_catch.Exception());
		Log(*error, 3);
	}
	return;
}

void ScriptV8Spark::startVoice() {
	Locker locker;

	// Create a handle scope to keep the temporary object references.
	HandleScope handle_scope;
	// Enter this context so all the remaining operations take place there
	Context::Scope context_scope(context_);

	// Set up an exception handler before calling the Process function
	TryCatch try_catch;
	// Invoke the function implemented in Javascript
	const int argc = 0;
	Handle<Value> argv[argc] = { };

	Handle<Value> result = startVoice_->Call(context_->Global(), argc, argv);
	if (result.IsEmpty()) {
		String::Utf8Value error(try_catch.Exception());
		Log(*error, 3);
	}
	return;
}
