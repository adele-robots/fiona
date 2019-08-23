#ifndef SCRIPTINGVOICE_H_
#define SCRIPTINGVOICE_H_

#include "ScriptingSpark.h"
#include "ScriptingGeneral.h"
#include "IVoice.h"

#include <v8.h>
#include <string>
#include <map>
using namespace std;
using namespace v8;

class ScriptingVoice:
	public IVoice
{
	public:

		void setProcedures(v8::Handle<ObjectTemplate> global);

		//IVoice implementation
		void sayThis(char *prompt);
		void waitEndOfSpeech(void);
		void stopSpeech(void);

		//IVoice JavaScript required method wrap
		void sayThisRequired(char *prompt);
		void waitEndOfSpeechRequired(void);
		void stopSpeechRequired(void);

		//Other Functions
		void chargeFunctions(Persistent<v8::Context> context_, v8::Handle<ObjectTemplate> global);
		void quit();

		//IVoice
		static v8::Handle<Value> sayThisRequiredCallback(const Arguments& args);
		static v8::Handle<Value> waitEndOfSpeechRequiredCallback(const Arguments& args);
		static v8::Handle<Value> stopSpeechRequiredCallback(const Arguments& args);

		//Getters and Setters
		Persistent<v8::Context> getContext() const
		{
			return context_;
		}

		void setContext(Persistent<v8::Context> context)
		{
			context_ = context;
		}

		Persistent<v8::Function> getSayThis() const
		{
			return sayThis_;
		}

		void setSayThis(Persistent<v8::Function> sayThis)
		{
			sayThis_ = sayThis;
		}

		Persistent<v8::Function> getStopSpeech() const
		{
			return stopSpeech_;
		}

		void setStopSpeech(Persistent<v8::Function> stopSpeech)
		{
			stopSpeech_ = stopSpeech;
		}

		Persistent<v8::Function> getWaitEndOfSpeech() const
		{
			return waitEndOfSpeech_;
		}

		void setWaitEndOfSpeech(Persistent<v8::Function> waitEndOfSpeech)
		{
			waitEndOfSpeech_ = waitEndOfSpeech;
		}

private:
    static Persistent<v8::Context> context_;
		// Pointers to offered interfaces functions
			// IVoice
			static Persistent<Function> sayThis_;
			static Persistent<Function> waitEndOfSpeech_;
			static Persistent<Function> stopSpeech_;
};

#endif /* SCRIPTINGVOICE_H_ */
