#ifndef SCRIPTINGFRAMEEVENTPUBLISHER_H_
#define SCRIPTINGFRAMEEVENTPUBLISHER_H_

#include "ScriptingSpark.h"
#include "ScriptingGeneral.h"
#include "IFrameEventPublisher.h"
#include <FrameEventSubscriber.h>

#include <v8.h>
#include <string>
#include <map>
using namespace std;
using namespace v8;

class ScriptingFrameEventPublisher:
	public IFrameEventPublisher
{
	public:
		void setProcedures(v8::Handle<ObjectTemplate> global);

		//IFrameEventPublisher implementation
		void addFrameEventSubscriber(FrameEventSubscriber *frameEventSubscriber);

		//IFrameEventPublisher JavaScript required method wrap
		void addFrameEventSubscriberRequired(FrameEventSubscriber *frameEventSubscriber);

		//Other functions
		void chargeFunctions(Persistent<v8::Context> context_, v8::Handle<ObjectTemplate> global);
		void quit();

		//IFrameEventPublisher
		static v8::Handle<Value> addFrameEventSubscriberRequiredCallback(const Arguments& args);


    //Getters and Setters
    Persistent<Function> getAddFrameEventSubscriber() const
    {
        return addFrameEventSubscriber_;
    }

    void setAddFrameEventSubscriber(Persistent<v8::Function> addFrameEventSubscriber)
    {
        addFrameEventSubscriber_ = addFrameEventSubscriber;
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
    // IFrameEventPublisher
    static Persistent<Function> addFrameEventSubscriber_;
   };

#endif /* SCRIPTINGFRAMEEVENTPUBLISHER_H_ */
