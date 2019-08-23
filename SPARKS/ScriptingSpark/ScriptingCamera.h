#ifndef SCRIPTINGCAMERA_H_
#define SCRIPTINGCAMERA_H_

#include "ScriptingSpark.h"
#include "ScriptingGeneral.h"
#include "ICamera.h"

#include <v8.h>
#include <string>
#include <map>
using namespace std;
using namespace v8;

class ScriptingCamera:
	public ICamera
{
	public:
		void setProcedures(v8::Handle<ObjectTemplate> global);

		//ICamera implementation
		void setCameraPosition(float X,float Y,float Z);
		void setCameraRotation(float X,float Y,float Z);
		void setCameraParameters(bool IsOrtho,float VisionAngle, float nearClippingPlane,float FarClippingPlane);

		//ICamera JavaScript required method wrap

		void setCameraPositionRequired(float X,float Y,float Z);
		void setCameraRotationRequired(float X,float Y,float Z);
		void setCameraParametersRequired(bool IsOrtho,float VisionAngle,float nearClippingPlane,float FarClippingPlane);

		//Other functions
		void chargeFunctions(Persistent<v8::Context> context_,v8::Handle<ObjectTemplate> global);

		void quit();

		//ICamera
		static v8::Handle<Value> setCameraPositionRequiredCallback(const Arguments& args);
		static v8::Handle<Value> setCameraRotationRequiredCallback(const Arguments& args);
		static v8::Handle<Value> setCameraParametersRequiredCallback(const Arguments& args);

		// Getters and Setters
		Persistent<v8::Context> getContext() const {
			return context_;
		}

		void setContext(Persistent<v8::Context> context) {
			context_ = context;
		}

		Persistent<v8::Function> getSetCameraPosition() const {
			return setCameraPosition_;
		}

		void setSetCameraPosition(Persistent<v8::Function> setCameraPosition) {
			setCameraPosition_ = setCameraPosition;
		}

		Persistent<v8::Function> getSetCameraRotation() const {
			return setCameraRotation_;
		}

		void setSetCameraRotation(Persistent<v8::Function> setCameraRotation) {
			setCameraRotation_ = setCameraRotation;
		}

		Persistent<v8::Function> getSetCameraParameters() const {
			return setCameraParameters_;
		}

		void setSetCameraParameters(Persistent<v8::Function> setCameraParameters) {
			setCameraParameters_ = setCameraParameters;
		}

	private:

		static Persistent<v8::Context> context_;

		// Pointers to offered interfaces functions

		  // ICamera
		  static Persistent<Function> setCameraPosition_;
		  static Persistent<Function> setCameraRotation_;
		  static Persistent<Function> setCameraParameters_;
};

#endif /* SCRIPTINGCAMERA_H_ */
