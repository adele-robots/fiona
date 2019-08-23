/// @file LocalAudioOutput.h
/// @brief LocalAudioOutput class declaration.


#ifndef __LOCAL_VIDEO_OUTPUT_H	
#define __LOCAL_VIDEO_OUTPUT_H	


//PABLO: Scene
#include "IScene.h"
#include "Scene3D.h"
#include "MotionController.h"
#include "VideoOutput.h"


class LocalVideoOutput : public VideoOutput
{
public:
	public:
	//PABLO: Window -> OriginalWindow
	//PABLO: Scene
	LocalVideoOutput(
#if defined (PABLO_2D)
		IScene *s,
#else 
		Scene3D *s,
#endif 
		MotionController *m, Window *win, IAsyncFatalErrorHandler *afeh) 
		: VideoOutput(s, m, win, afeh) 
	{}
	void quit(void);
	void render(void);
	int getWidth(void);
	int getHeight(void);
	static void *process(void *);

	// Initialization must be done in by the rendering thread
	void init(void);
};

#endif
