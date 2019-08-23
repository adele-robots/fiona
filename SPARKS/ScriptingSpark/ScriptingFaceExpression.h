#ifndef SCRIPTINGFACEEXPRESSION_H_
#define SCRIPTINGFACEEXPRESSION_H_


#include "ScriptingSpark.h"
#include "ScriptingGeneral.h"
#include "IFaceExpression.h"

#include <v8.h>
#include <string>
#include <map>
using namespace std;
using namespace v8;

class ScriptingFaceExpression:
	public IFaceExpression
{
	public:
		void setProcedures(v8::Handle<ObjectTemplate> global);

		//IFaceExpression implementation
		void setFaceExpression(char *expressionName,float intensity);

		//IFaceExpression JavaScript required method wrap
		void setFaceExpressionRequired (char *expressionName,float intensity);

		// Other functions
	    void chargeFunctions(Persistent<v8::Context> context_, v8::Handle<ObjectTemplate>global);

	    void quit();

		//IFaceExpression
		static v8::Handle<Value> setFaceExpressionRequiredCallback(const Arguments& args);

		//Getters and Setters
		Persistent<v8::Context> getContext() const {
			return context_;
		}

		void setContext(Persistent<v8::Context> context) {
			context_ = context;
		}

		Persistent<v8::Function> getSetFaceExpression() const {
			return setFaceExpression_;
		}

		void setSetFaceExpression(Persistent<v8::Function> setFaceExpression) {
			setFaceExpression_ = setFaceExpression;
		}


	private:

		static Persistent<v8::Context> context_;

		// Pointers to offered interfaces functions

		  // IFaceExpression
			static Persistent<Function> setFaceExpression_;

};

#endif /* SCRIPTINGFACEEXPRESSION_H_ */
