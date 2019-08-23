#ifndef SCRIPTINGDETECTEDFACEPOSITIONCONSUMER_H_
#define SCRIPTINGDETECTEDFACEPOSITIONCONSUMER_H_

#include "ScriptingSpark.h"
#include "ScriptingGeneral.h"
#include "IDetectedFacePositionConsumer.h"

#include <v8.h>
#include <string>
#include <map>
using namespace std;
using namespace v8;

class ScriptingDetectedFacePositionConsumer:
	public IDetectedFacePositionConsumer
{
	public:
		void setProcedures(v8::Handle<ObjectTemplate> global);

		//IDetectedFacePositionConsumer implementation
		void consumeDetectedFacePosition(bool isFaceDetected, double x, double y);

		//IDetectedFacePositionConsumer JavaScript required method wrap
		void consumeDetectedFacePositionRequired(bool isFaceDetected, double x, double y);

		//Other functions
		void chargeFunctions(Persistent<v8::Context> context_,v8::Handle<ObjectTemplate> global);

		void quit();

		//IDetectedFacePositionConsumer
		static v8::Handle<Value> consumeDetectedFacePositionRequiredCallback(const Arguments& args);

		// Getters and Setters
		Persistent<v8::Context> getContext() const {
			return context_;
		}

		void setContext(Persistent<v8::Context> context) {
			context_ = context;
		}

		Persistent<v8::Function> getConsumeDetectedFacePosition() const {
			return consumeDetectedFacePosition_;
		}

		void setConsumeDetectedFacePosition(Persistent<v8::Function> consumeDetectedFacePosition) {
			consumeDetectedFacePosition_ = consumeDetectedFacePosition;
		}

	private:
		static Persistent<v8::Context> context_;

		// Pointers to offered interfaces functions

		  // IDetectedFacePositionConsumer
		  static Persistent<Function> consumeDetectedFacePosition_;
};

#endif /* SCRIPTINGDETECTEDFACEPOSITIONCONSUMER_H_ */
