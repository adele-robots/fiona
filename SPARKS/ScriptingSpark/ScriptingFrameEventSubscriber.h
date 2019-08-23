#ifndef SCRIPTINGFRAMEEVENTSUBSCRIBER_H_
#define SCRIPTINGFRAMEEVENTSUBSCRIBER_H_

#include "ScriptingSpark.h"
#include "ScriptingGeneral.h"
#include "FrameEventSubscriber.h"

#include <v8.h>
#include <string>
#include <map>
using namespace std;
using namespace v8;

class ScriptingFrameEventSubscriber:
	public FrameEventSubscriber
{
	public:
		void setProcedures(v8::Handle<ObjectTemplate> global);

		//FrameEventSubscriber implementation
		void notifyFrameEvent();

		//FrameEventSubscriber JavaScript required method wrap
		void notifyFrameEventRequired();

		//Other functions
		void chargeFunctions(Persistent<v8::Context> context_, v8::Handle<ObjectTemplate> global);
		void quit();

		//FrameEventSubscriber
		static v8::Handle<Value> notifyFrameEventRequiredCallback(const Arguments& args);


    //Getters and Setters

    Persistent<Function> getNotifyFrameEvent() const
    {
        return notifyFrameEvent_;
    }

    void setNotifyFrameEvent(Persistent<v8::Function> notifyFrameEvent)
    {
        notifyFrameEvent_ = notifyFrameEvent;
    }


    Persistent<v8::Context> getContext() const
    {
        return context_;
    }

    void setContext(Persistent<v8::Context> context)
    {
        context_ = context;
    }

private:
    static Persistent<v8::Context> context_;
    // Pointers to offered interfaces functions
    // FrameEventSubscriber
    static Persistent<Function> notifyFrameEvent_;
};

#endif /* SCRIPTINGFRAMEEVENTSUBSCRIBER_H_ */
