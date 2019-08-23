#ifndef SCRIPTINGAUDIOQUEUE_H_
#define SCRIPTINGAUDIOQUEUE_H_

#include "ScriptingSpark.h"
#include "ScriptingGeneral.h"
#include "IAudioQueue.h"

#include <v8.h>
#include <string>
#include <map>
using namespace std;
using namespace v8;

class ScriptingAudioQueue:
	public IAudioQueue
{
	public:
		void setProcedures(v8::Handle<ObjectTemplate> global);

		//IAudioQueue implementation
		int getStoredAudioSize();
		void dequeueAudioBuffer(char *buffer, int size);
		void queueAudioBuffer(char *buffer, int size);

		//IAudioQueue JavaScript required method wrap
		int getStoredAudioSizeRequired();
		void dequeueAudioBufferRequired(char *buffer, int size);
		void queueAudioBufferRequired(char *buffer, int size);

		//Other Functions
		void chargeFunctions(Persistent<v8::Context> context_,v8::Handle<ObjectTemplate> global);

		void quit();

		//IAudioQueue
		static v8::Handle<Value> getStoredAudioSizeRequiredCallback(const Arguments& args);
		static v8::Handle<Value> dequeueAudioBufferRequiredCallback(const Arguments& args);
		static v8::Handle<Value> queueAudioBufferRequiredCallback(const Arguments& args);

		// Getters and Setters
		Persistent<v8::Context> getContext() const {
			return context_;
		}

		void setContext(Persistent<v8::Context> context) {
			context_ = context;
		}

		Persistent<v8::Function> getGetStoredAudioSize() const {
			return getStoredAudioSize_;
		}

		void setGetStoredAudioSize(Persistent<v8::Function> getStoredAudioSize) {
			getStoredAudioSize_ = getStoredAudioSize;
		}

		Persistent<v8::Function> getDequeueAudioBuffer() const {
			return dequeueAudioBuffer_;
		}

		void setDequeueAudioBuffer(Persistent<v8::Function> dequeueAudioBuffer) {
			dequeueAudioBuffer_ = dequeueAudioBuffer;
		}

		Persistent<v8::Function> getQueueAudioBuffer() const {
			return queueAudioBuffer_;
		}

		void setQueueAudioBuffer(Persistent<v8::Function> queueAudioBuffer) {
			queueAudioBuffer_ = queueAudioBuffer;
		}

	private:

		static Persistent<v8::Context> context_;

		// Pointers to offered interfaces functions

		  // IAudioQueue
		  static Persistent<Function> getStoredAudioSize_;
		  static Persistent<Function> dequeueAudioBuffer_;
		  static Persistent<Function> queueAudioBuffer_;
};

#endif /* SCRIPTINGAUDIOQUEUE_H_ */
