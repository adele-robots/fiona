#ifndef SCRIPTINGRENDERIZABLE_H_
#define SCRIPTINGRENDERIZABLE_H_

#include "ScriptingSpark.h"
#include "ScriptingGeneral.h"
#include "IRenderizable.h"
#include "Horde3D.h"

#include <v8.h>
#include <string>
#include <map>
using namespace std;
using namespace v8;

class ScriptingRenderizable:
	public IRenderizable
{
	public:

		void setProcedures(v8::Handle<ObjectTemplate> global);

		//IRenderizable implementation
		void render(void);
		H3DRes getCamaraNode();

		//IRenderizable JavaScript required method wrap
		void renderRequired(void);
		H3DRes getCamaraNodeRequired();

		//Other Functions
		void chargeFunctions(Persistent<v8::Context> context_, v8::Handle<ObjectTemplate> global);
		void quit();

		//IRenderizable
		static v8::Handle<Value> renderRequiredCallback(const Arguments& args);
		static v8::Handle<Value> getCamaraNodeRequiredCallback(const Arguments& args);

		//Getters and Setters
		Persistent<v8::Context> getContext() const
		{
			return context_;
		}

		void setContext(Persistent<v8::Context> context)
		{
			context_ = context;
		}

		Persistent<v8::Function> getGetCamaraNode() const
		{
			return getCamaraNode_;
		}

		void setGetCamaraNode(Persistent<v8::Function> getCamaraNode)
		{
			getCamaraNode_ = getCamaraNode;
		}

		Persistent<Function> getRender() const
		{
			return render_;
		}

		void setRender(Persistent<Function> render)
		{
			render_ = render;
		}

private:
    static Persistent<v8::Context> context_;
    // Pointers to offered interfaces functions
    	// IRenderizable
    	static Persistent<Function> render_;
    	static Persistent<Function> getCamaraNode_;
};

#endif /* SCRIPTINGRENDERIZABLE_H_ */
