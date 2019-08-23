#ifndef SCRIPTINGTHREADPROC_H_
#define SCRIPTINGTHREADPROC_H_

#include "ScriptingSpark.h"
#include "ScriptingGeneral.h"
#include "IThreadProc.h"

#include <v8.h>
#include <string>
#include <map>
using namespace std;
using namespace v8;

class ScriptingThreadProc:
	public IThreadProc
{
	public:

		void setProcedures(v8::Handle<ObjectTemplate> global);

		//IThreadProc implementation
		void process();

		//IThreadProc JavaScript required method wrap
		void processRequired();

		//Other Functions
		void chargeFunctions(Persistent<v8::Context> context_, v8::Handle<ObjectTemplate> global);
		void quit();

		//IThreadProc
		static v8::Handle<Value> processRequiredCallback(const Arguments& args);

		//Getters and Setters

		Persistent<v8::Context> getContext() const
		{
			return context_;
		}

		void setContext(Persistent<v8::Context> context)
		{
			context_ = context;
		}

		Persistent<v8::Function> getProcess() const
		{
			return process_;
		}

		void setProcess(Persistent<v8::Function> process)
		{
			process_ = process;
		}

private:
    static Persistent<v8::Context> context_;

    // Pointers to offered interfaces functions
    	// IThreadProc
    	static Persistent<Function> process_;
};

#endif /* SCRIPTINGTHREADPROC_H_ */
