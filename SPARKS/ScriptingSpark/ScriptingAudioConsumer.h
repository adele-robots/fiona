#ifndef SCRIPTINGAUDIOCONSUMER_H_
#define SCRIPTINGAUDIOCONSUMER_H_

#include "ScriptingSpark.h"
#include "ScriptingGeneral.h"
#include "IAudioConsumer.h"

#include <v8.h>
#include <string>
#include <map>

using namespace std;
using namespace v8;

class ScriptingAudioConsumer:
	public IAudioConsumer
{
	public:
			void setProcedures(v8::Handle<ObjectTemplate> global);

		//IAudioConsumer implementation
			void consumeAudioBuffer(int16_t *audioBuffer, int bufferSizeInBytes);

		//IAudioConsumer JavaScript required method wrap
			void consumeAudioBufferRequired(int16_t *audioBuffer, int bufferSizeInBytes);

		//Other Functions
			void chargeFunctions(Persistent<v8::Context> context_,v8::Handle<ObjectTemplate> global);

			void quit();

		//IAudioConsumer
			static v8::Handle<Value> consumeAudioBufferRequiredCallback(const Arguments& args);

		// Getters and Setters
			Persistent<v8::Context> getContext() const {
				return context_;
			}

			void setContext(Persistent<v8::Context> context) {
				context_ = context;
			}

			Persistent<v8::Function> getConsumeAudioBuffer() const {
				return consumeAudioBuffer_;
			}

			void setConsumeAudioBuffer(Persistent<v8::Function> consumeAudioBuffer) {
				consumeAudioBuffer_ = consumeAudioBuffer;
			}

	private:
		static Persistent<v8::Context> context_;

		// Pointers to offered interfaces functions

		  // IAudioConsumer
			static Persistent<Function> consumeAudioBuffer_;
};

#endif /* SCRIPTINGAUDIOCONSUMER_H_ */
