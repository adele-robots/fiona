#ifndef SCRIPTINGAPPLICATION_H_
#define SCRIPTINGAPPLICATION_H_

#include "ScriptingSpark.h"
#include "ScriptingGeneral.h"

#include "IApplication.h"

#include <v8.h>
#include <string>
#include <map>
using namespace std;
using namespace v8;

class ScriptingApplication:
	public IApplication
{
public:
	void setProcedures(v8::Handle<ObjectTemplate> global);

	//IApplication implementation
	void run();

	//IApplication JavaScript required method wrap
	void runRequired();

	//Other functions
	void chargeFunctions(Persistent<v8::Context> context_, v8::Handle<ObjectTemplate>global);

	void quit();

	//IAnimation
	 static v8::Handle<Value> runRequiredCallback(const Arguments& args);

	// Getters and Setters
	Persistent<v8::Context> getContext() const {
		return context_;
	}

	void setContext(Persistent<v8::Context> context) {
		context_ = context;
	}

	Persistent<v8::Function> getRun() const {
		return run_;
	}

	void setRun(Persistent<v8::Function> run) {
		run_ = run;
	}

private:

	static Persistent<v8::Context> context_;

		// Pointers to offered interfaces functions

			// IApplication
				static	Persistent<Function> run_;

};

#endif /* SCRIPTINGAPPLICATION_H_ */
