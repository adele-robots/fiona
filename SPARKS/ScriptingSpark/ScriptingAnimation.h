#ifndef SCRIPTINGANIMATION_H_
#define SCRIPTINGANIMATION_H_

#include "ScriptingSpark.h"
#include "ScriptingGeneral.h"

#include "IAnimation.h"

#include <v8.h>
#include <string>
#include <map>
using namespace std;
using namespace v8;

class ScriptingAnimation:
	public IAnimation
{
public:
	void setProcedures(v8::Handle<ObjectTemplate> global);

	//IAnimation implementation
	void playAnimation(char *animationFileName);

	//IAnimation JavaScript required method wrap
	void playAnimationRequired(char *animationFileName);

	//Other functions
	void chargeFunctions(Persistent<v8::Context> context_, v8::Handle<ObjectTemplate>global);

	void quit();

	//IAnimation
	 static v8::Handle<Value> playAnimationRequiredCallback(const Arguments& args);

	// Getters and Setters
	Persistent<v8::Context> getContext() const {
		return context_;
	}

	void setContext(Persistent<v8::Context> context) {
		context_ = context;
	}


	Persistent<v8::Function> getPlayAnimation() const {
		return playAnimation_;
	}

	void setPlayAnimation(Persistent<v8::Function> playAnimation) {
		playAnimation_ = playAnimation;
	}


private:

	static Persistent<v8::Context> context_;

	// Pointers to offered interfaces functions

		// IAnimation
	  	  static Persistent<Function> playAnimation_;
};

#endif /* SCRIPTINGANIMATION_H_ */
