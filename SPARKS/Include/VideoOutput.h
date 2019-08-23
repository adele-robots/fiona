/// @file VideoOutput.h
/// @brief VideoOutput class declaration.


#ifndef __VIDEO_OUTPUT_H
#define __VIDEO_OUTPUT_H


//PABLO: Scene
#include "IScene.h"
#include "Scene3D.h"
#include "MotionController.h"
#include "OnScreenMonitor.h"
#include "Thread.h"
#include "ErrorHandling.h"
#include "IAsyncFatalErrorHandler.h"
//PABLO: Window -> OriginalWindow
#include "Window.h"


class VideoOutput : public Thread
{
public:
	//PABLO: Window -> OriginalWindow
	//PABLO: Scene
	#if defined (PABLO_2D)
	VideoOutput(IScene *, MotionController *, Window *, IAsyncFatalErrorHandler *);
	#else
	VideoOutput(Scene3D *, MotionController *, Window *, IAsyncFatalErrorHandler *);
	#endif	
	virtual void init(void);
	virtual void render(void);
	void quit(void);
	void process(void);
	virtual int getWidth(void) = 0;
	virtual int getHeight(void) = 0;

public:
	//PABLO: Scene
	#if defined (PABLO_2D)
	IScene *scene;
	#else
	Scene3D *scene;
	#endif

protected:
	//PABLO: Window-> OriginalWindow
	Window *mainWindow;


private:
	MotionController *motionController;
	OnScreenMonitor *controllerMonitor;
	OnScreenMonitor *rendererMonitor;
};


#endif