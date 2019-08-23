#ifndef SCRIPTINGCONCURRENT_H_
#define SCRIPTINGCONCURRENT_H_

#include "ScriptingSpark.h"
#include "ScriptingGeneral.h"
#include "IConcurrent.h"

#include <v8.h>
#include <string>
#include <map>
using namespace std;
using namespace v8;

class ScriptingConcurrent:
	public IConcurrent
{
public:
		void setProcedures(v8::Handle<ObjectTemplate> global);

		//IConcurrent implementation
		void start();
		void stop();

		//IConcurrent JavaScript required method wrap
		void startRequired();
		void stopRequired();

		//Other Functions
		void chargeFunctions(Persistent<v8::Context> context_,v8::Handle<ObjectTemplate> global);

		void quit();

		//IConcurrent
		static v8::Handle<Value> startRequiredCallback(const Arguments& args);
		static v8::Handle<Value> stopRequiredCallback(const Arguments& args);

		// Getters and Setters
		Persistent<v8::Context> getContext() const {
			return context_;
		}

		void setContext(Persistent<v8::Context> context) {
			context_ = context;
		}

		Persistent<v8::Function> getStart() const {
			return start_;
		}

		void setStart(Persistent<v8::Function> start) {
			start_ = start;
		}

		Persistent<v8::Function> getStop() const {
			return stop_;
		}

		void setStop(Persistent<v8::Function> stop) {
			stop_ = stop;
		}

	private:
		static Persistent<v8::Context> context_;

		// Pointers to offered interfaces functions

		  // IConcurrent
			static Persistent<Function> start_;
			static Persistent<Function> stop_;
};

#endif /* SCRIPTINGCONCURRENT_H_ */
