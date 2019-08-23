#ifndef SCRIPTINGCONTROLVOICE_H_
#define SCRIPTINGCONTROLVOICE_H_

#include "ScriptingSpark.h"
#include "ScriptingGeneral.h"
#include "IControlVoice.h"

#include <v8.h>
#include <string>
#include <map>
using namespace std;
using namespace v8;

class ScriptingControlVoice:
	public IControlVoice
{
	public:
		void setProcedures(v8::Handle<ObjectTemplate> global);

		//IControlVoice implementation
		void startSpeaking(void);
		void stopSpeaking(void);
		void startVoice(void);

		//IControlVoice JavaScript required method wrap
		void startSpeakingRequired(void);
		void stopSpeakingRequired(void);
		void startVoiceRequired(void);

		//Other Functions
		void chargeFunctions(Persistent<v8::Context> contextPasado_, v8::Handle<ObjectTemplate>global);

		void quit();

		//IControlVoice
		static v8::Handle<Value> startSpeakingRequiredCallback(const Arguments& args);
		static v8::Handle<Value> stopSpeakingRequiredCallback(const Arguments& args);
		static v8::Handle<Value> startVoiceRequiredCallback(const Arguments& args);

		//Getters and Setters
		Persistent<v8::Context> getContext() const {
			return context_;
		}

		void setContext(Persistent<v8::Context> context) {
			context_ = context;
		}

		Persistent<v8::Function> getStartSpeaking() const {
			return startSpeaking_;
		}

		void setStartSpeaking(Persistent<v8::Function> startSpeaking) {
			startSpeaking_ = startSpeaking;
		}

		Persistent<v8::Function> getStopSpeaking() const {
			return stopSpeaking_;
		}

		void setStopSpeaking(Persistent<v8::Function> stopSpeaking) {
			stopSpeaking_ = stopSpeaking;
		}

		Persistent<v8::Function> getStartVoice() const {
			return startVoice_;
		}

		void setStartVoice(Persistent<v8::Function> startVoice) {
			startVoice_ = startVoice;
		}

	private:
		static Persistent<v8::Context> context_;

		// Pointers to offered interfaces functions

		  // IControlVoice
			static Persistent<Function> startSpeaking_;
			static Persistent<Function> stopSpeaking_;
			static Persistent<Function> startVoice_;
};

#endif /* SCRIPTINGCONTROLVOICE_H_ */
