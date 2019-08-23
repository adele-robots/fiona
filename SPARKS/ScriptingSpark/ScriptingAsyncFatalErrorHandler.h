#ifndef SCRIPTINGASYNCFATALERRORHANDLER_H_
#define SCRIPTINGASYNCFATALERRORHANDLER_H_

#include "ScriptingSpark.h"
#include "ScriptingGeneral.h"
#include "IAsyncFatalErrorHandler.h"

#include <v8.h>
#include <string.h>
#include <map>
#include <assert.h>
#include <fcntl.h>

#include <stdio.h>
#include <stdlib.h>

using namespace std;
using namespace v8;

class ScriptingAsyncFatalErrorHandler:
	public IAsyncFatalErrorHandler
{
public:
		void setProcedures(v8::Handle<ObjectTemplate> global);

		//IAsyncFatalErrorHandler implementation
		void handleError(char *msg);

		//IAsyncFatalErrorHandler JavaScript required method wrap
		void handleErrorRequired(char *msg);

		//Other functions
		void chargeFunctions(Persistent<v8::Context> context_, v8::Handle<ObjectTemplate> global);

		void quit();

	    //IAsyncFatalErrorHandler
		static v8::Handle<Value> handleErrorRequiredCallback(const Arguments& args);

		// Getters and Setters
		Persistent<v8::Context> getContext() const {
			return context_;
		}

		void setContext(Persistent<v8::Context> context) {
			context_ = context;
		}

		Persistent<v8::Function> getHandleError() const {
			return handleError_;
		}

		void setHandleError(Persistent<v8::Function> handleError) {
			handleError_ = handleError;
		}

private:
		static Persistent<v8::Context> context_;

		// Pointers to offered interfaces functions

		  // IAsyncFatalErrorHandler
				static Persistent<Function> handleError_;


};

#endif /* SCRIPTINGASYNCFATALERRORHANDLER_H_ */
