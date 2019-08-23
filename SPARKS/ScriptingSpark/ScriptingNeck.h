#ifndef SCRIPTINGNECK_H_
#define SCRIPTINGNECK_H_

#include "ScriptingSpark.h"
#include "ScriptingGeneral.h"
#include "INeck.h"

#include <v8.h>
#include <string>
#include <map>
using namespace std;
using namespace v8;

class ScriptingNeck:
	public INeck
{
	public:
		void setProcedures(v8::Handle<ObjectTemplate> global);

		//INeck implementation
			void rotateHead(float pan, float tilt);

		//INeck JavaScript required method wrap
			void rotateHeadRequired(float pan, float tilt);

		//Others Functions
			void chargeFunctions(Persistent<v8::Context> context_, v8::Handle<ObjectTemplate> global);
			void quit();

		//INeck
			static v8::Handle<Value> rotateHeadRequiredCallback(const Arguments& args);

		//Getters and Setters

			Persistent<v8::Context> getContext() const
			{
				return context_;
			}

			void setContext(Persistent<v8::Context> context)
			{
				context_ = context;
			}

			Persistent<v8::Function> getRotateHead() const
			{
				return rotateHead_;
			}

			void setRotateHead(Persistent<v8::Function> rotateHead)
			{
				rotateHead_ = rotateHead;
			}

private:
    static Persistent<v8::Context> context_;
    // Pointers to offered interfaces functions
    	// INeck
    	static Persistent<Function> rotateHead_;
};

#endif /* SCRIPTINGNECK_H_ */
