#ifndef SCRIPTINGWINDOW_H_
#define SCRIPTINGWINDOW_H_

#include "ScriptingSpark.h"
#include "ScriptingGeneral.h"
#include "IWindow.h"

#include <v8.h>
#include <string>
#include <map>
using namespace std;
using namespace v8;

class ScriptingWindow:
	public IWindow
{
	public:
		void setProcedures(v8::Handle<ObjectTemplate> global);

		//IWindow implementation

			Display* getWindowDisplay(void);

			void show(void);
			void hide(void);
			int getColorDepth(void);
			void makeCurrentopenGlThread(void);
			void openGlSwapBuffers(void);

		//IWindow JavaScript required method wrap

			Display* getWindowDisplayRequired(void);

			void showRequired(void);
			void hideRequired(void);
			int getColorDepthRequired(void);
			void makeCurrentopenGlThreadRequired(void);
			void openGlSwapBuffersRequired(void);

		//Other Functions
			void chargeFunctions(Persistent<v8::Context> context_, v8::Handle<ObjectTemplate> global);

			void quit();

			static v8::Handle<Value> getWindowDisplayRequiredCallback(const Arguments& args);
			static v8::Handle<Value> showRequiredCallback(const Arguments& args);
			static v8::Handle<Value> hideRequiredCallback(const Arguments& args);
			static v8::Handle<Value> getColorDepthRequiredCallback(const Arguments& args);
			static v8::Handle<Value> makeCurrentopenGlThreadRequiredCallback(const Arguments& args);
			static v8::Handle<Value> openGlSwapBuffersRequiredCallback(const Arguments& args);

		//Getters and Setters
			Persistent<v8::Context> getContext() const
			{
				return context_;
			}

			void setContext(Persistent<v8::Context> context)
			{
				context_ = context;
			}

			Persistent<v8::Function> getGetColorDepth() const
			{
				return getColorDepth_;
			}

			void setGetColorDepth(Persistent<v8::Function> getColorDepth)
			{
				getColorDepth_ = getColorDepth;
			}

			Persistent<v8::Function> getGetWindowDisplay() const
			{
				return getWindowDisplay_;
			}

			void setGetWindowDisplay(Persistent<v8::Function> getWindowDisplay)
			{
				getWindowDisplay_ = getWindowDisplay;
			}

			Persistent<v8::Function> getHide() const
			{
				return hide_;
			}

			void setHide(Persistent<v8::Function> hide)
			{
				hide_ = hide;
			}

			Persistent<v8::Function> getMakeCurrentopenGlThread() const
			{
				return makeCurrentopenGlThread_;
			}

			void setMakeCurrentopenGlThread(Persistent<v8::Function> makeCurrentopenGlThread)
			{
				makeCurrentopenGlThread_ = makeCurrentopenGlThread;
			}

			Persistent<v8::Function> getOpenGlSwapBuffers() const
			{
				return openGlSwapBuffers_;
			}

			void setOpenGlSwapBuffers(Persistent<v8::Function> openGlSwapBuffers)
			{
				openGlSwapBuffers_ = openGlSwapBuffers;
			}

			Persistent<v8::Function> getShow() const
			{
				return show_;
			}

			void setShow(Persistent<v8::Function> show)
			{
				show_ = show;
			}

private:
    static Persistent<v8::Context> context_;
		// Pointers to offered interfaces functions
			// IWindow
				static Persistent<Function> getWindowDisplay_;
				static Persistent<Function> show_;
				static Persistent<Function> hide_;
				static Persistent<Function> getColorDepth_;
				static Persistent<Function> makeCurrentopenGlThread_;
				static Persistent<Function> openGlSwapBuffers_;
};

	//IWindow



#endif /* SCRIPTINGWINDOW_H_ */
