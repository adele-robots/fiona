/// @file RemoteVideoOutput.h
/// @brief RemoteVideoOutput class declaration.


#ifndef __REMOTE_VIDEO_OUTPUT_H
#define __REMOTE_VIDEO_OUTPUT_H


#include <Horde3D.h>
#include "VideoOutput.h"
//PABLO: Scene
#include "IScene.h"
#include "MotionController.h"
#include "FFMpegAudioVideoDestination.h"
#include "StopWatch.h"
#include "RemoteAudioOutput.h"


class RemoteVideoOutput : public VideoOutput {
public:
	RemoteVideoOutput(
		//PABLO: Scene
		#if defined (PABLO_2D)
		IScene *s,
		#else
		Scene3D *s,
		#endif
		MotionController *m, 
		FFMpegAudioVideoDestination *avd, 
		RemoteAudioOutput *rao,
		//PABLO: Window -> OriginalWindow
		Window *win,
		IAsyncFatalErrorHandler *afeh
	) : 
		remoteAudioOutput(rao), 
		fFMpegAudioVideoDestination(avd), 
		VideoOutput(s, m, win, afeh)
	{}
	void render(void);
	void quit(void);

	// Initialization must be done in by the rendering thread
	void init(void);
	int getWidth(void);
	int getHeight(void);

public:
	H3DRes renderTargetTexture;
	StopWatch avsychStopWatch;
	float lastAudioPacketIssueTime;


private:
	void bindCamaraToRTT(void);
	FFMpegAudioVideoDestination *fFMpegAudioVideoDestination;
	RemoteAudioOutput *remoteAudioOutput; 

private:
	void renderHelper(void);
};


#endif