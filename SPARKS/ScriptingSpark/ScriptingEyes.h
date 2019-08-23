#ifndef SCRIPTINGEYES_H_
#define SCRIPTINGEYES_H_

#include "ScriptingSpark.h"
#include "ScriptingGeneral.h"
#include "IEyes.h"

#include <v8.h>
#include <string>
#include <map>
using namespace std;
using namespace v8;

class ScriptingEyes:
	public IEyes
{

public:
	void setProcedures(v8::Handle<ObjectTemplate> global);

	//IEyes implementation
	void rotateEye(float pan,float tilt);

	//IEyes JavaScript required method wrap
	void rotateEyeRequired(float pan,float tilt);

    //Other Functions
    void chargeFunctions(Persistent<v8::Context> context_, v8::Handle<ObjectTemplate> global);
    void quit();

    //IEyes
    static v8::Handle<Value> rotateEyeRequiredCallback(const Arguments& args);

    //Getters and Setters
    Persistent<v8::Context> getContext() const
    {
        return context_;
    }

    void setContext(Persistent<v8::Context> context)
    {
        context_ = context;
    }

    Persistent<v8::Function> getRotateEye() const
    {
        return rotateEye_;
    }

    void setRotateEye( Persistent<v8::Function> rotateEye)
    {
        rotateEye_ = rotateEye;
    }

private:
    static Persistent<v8::Context> context_;
    // Pointers to offered interfaces functions
		// IEyes
		static Persistent<Function> rotateEye_;
};

#endif /* SCRIPTINGEYES_H_ */
