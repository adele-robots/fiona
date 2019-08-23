#ifndef __ScriptingGeneral_H
#define __ScriptingGeneral_H

#include <v8.h>
#include <string>
#include <map>

#include <Image.h>
#include "IVideoConsumer2.h"
#include "IEventQueue.h"
#include <FrameEventSubscriber.h>

#include "ScriptingSpark.h"

#include "ScriptingAnimation.h"
#include "ScriptingApplication.h"
#include "ScriptingAsyncFatalErrorHandler.h"
#include "ScriptingAudioQueue.h"
#include "ScriptingCamera.h"
#include "ScriptingConcurrent.h"
#include "ScriptingControlVoice.h"
#include "ScriptingDetectedFacePositionConsumer.h"
#include "ScriptingEyes.h"
#include "ScriptingFaceExpression.h"
#include "ScriptingFrameEventPublisher.h"
#include "ScriptingFrameEventSubscriber.h"
#include "ScriptingNeck.h"
#include "ScriptingRenderizable.h"
#include "ScriptingThreadProc.h"
#include "ScriptingVoice.h"
#include "ScriptingWindow.h"

using namespace std;
using namespace v8;
using namespace psisban;

class ScriptingGeneral
{
public:

	static Persistent<ObjectTemplate> class_template_;

	v8::Handle<String> ReadFile(const string& name);

	void ChargeAllFunctions(Persistent<v8::Context> context_, v8::Handle<ObjectTemplate>global);

	void setAllProcedures(v8::Handle<ObjectTemplate> global);

	void initializeAllRequiredInterfaces();

	void quitAll();

	void Log(const char* event);

	static v8::Handle<Value> LogCallback(const Arguments& args);

	// Wraps

		//Template
			static v8::Handle<ObjectTemplate> MakeCharTemplate();
			static v8::Handle<ObjectTemplate> MakeFrameEventSubscriberTemplate();

		//Generals for all Scriptings
			template<class T> T* UnwrapClass(v8::Handle<Object> obj){
				v8::Handle<External> field = v8::Handle<External>::Cast(obj->GetInternalField(0));
				void*  ptr = field->Value();
				return static_cast<T *> ( ptr);
			}


			template<class T> v8::Handle<Object> WrapClass(T* y, v8::Handle<ObjectTemplate> (*fun)()){
				// Handle scope for temporary handles,
					v8::HandleScope handle_scope;

				// Fetch the template for creating JavaScript map wrappers.
				// It only has to be created once, which we do on demand.
					if (class_template_.IsEmpty()) {
						v8::Handle<ObjectTemplate> raw_template = fun();
						class_template_ = v8::Persistent<ObjectTemplate>::New(raw_template);
					}

					v8::Handle<ObjectTemplate> templ = class_template_;

				// Create an empty Scripting"X" wrapper.
					v8::Handle<Object> result = templ->NewInstance();

				// Wrap the raw C++ pointer in an External so it can be referenced from within JavaScript.
					v8::Handle<External> class_ptr = v8::External::New(static_cast<T*>(y));

				// Store the map pointer in the JavaScript wrapper.
				// Point the 0 index Filed to the c++ pointer for unwrapping later
					result->SetInternalField(0, class_ptr);

				// Return the value, releasing the other handles on its way.
				// Return the result through the current handle scope. Since each of these handles will go
				// away when the handle scope is deleted we need to call Close to let one, the result, escape
				// into the outer handle scope.
					return handle_scope.Close(result);
			}

private:


};

#endif
