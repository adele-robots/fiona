/// @file Application.h
/// @brief Application class declaration.


#ifndef __APPLICATION_H
#define __APPLICATION_H


#include "FaceTracker.h"
//#include "ASR.h"
//PABLO: Scene
#include "IScene.h"
#include "Scene3D.h"
#include "Scene2D.h"

#include "LipSynch.h"
#include "MotionController.h"
#include "FFMpegAudioVideoSource.h"
#include "FFMpegAudioVideoDestination.h"
#include "AudioInput.h"
#include "VideoInput.h"
#include "AudioOutput.h"
#include "VideoOutput.h"
#include "Thread.h"
//PABLO: MTB MODIFIED
#include "IMorphTargetBlender.h"
#include "MorphTargetBlender3D.h"
#include "MorphTargetBlender2D.h"

//PABLO: Window -> OriginalWindow
#include "OriginalWindow.h"
#include "Window_Svg.h"

#include "EventDrivenDialogManager.h"
#include "AVInput.h"

//DAVID
#include "MecaTronicaSerial.h"
/// \brief Main application class.
///
/// Class composed of all the main susbsystems, responsible of initialization,
/// thread launching, main loop thread implementation, event processing,
/// graceful death and resource alocation/dealocation.

class Application : public IAsyncFatalErrorHandler 
{
public:
	virtual void init(char *configurationFile);
	virtual void run(void);
	virtual void quit(void);

	// envio eventos
	static void SendPsisbanMessage(UINT msg, WPARAM wParam, LPARAM lParam);

	// AppAsyncFatalErrorHandler implementation. 
	void handleError(char *msg);



private:
	void initRandomNumbers(void);
	void initKioskAudioVideo(void);
	void initStreamingAudioVideo(void);

private:
	FaceTracker *faceTracker;
	ASR *asr;
	#if defined (PABLO_2D)
	Scene2D *scene;
	#else
	Scene3D *scene;
	#endif

	LipSynch *lipSynch;
	MotionController *controller;
	//PABLO: 2d
	#if defined (PABLO_2D)
	MorphTargetBlender2D *morphTargetBlender;
	#else
	//PABLO: MTB MODIFIED
	IMorphTargetBlender *morphTargetBlender;
	#endif
	EventDrivenDialogManager *eventDrivenDialogManager;
	psisban::Config bodyConfiguration;
	void registerScxmlEventHandling(void);
	AVInput *aVInput;

	bool isLocal;
	AudioOutput *audioOutput;
	VideoOutput *videoOutput;
	FFMpegAudioVideoDestination *fFMpegAudioVideoDestination;

	Thread *fFMpegAudioVideoSourceThread;
	Thread *localVideoOutputFramePumpThread;
	Thread *remoteVideoOutputFramePumpThread;

	//PABLO: Window -> OriginalWindow
	OriginalWindow *win;
	#if defined(PABLO_2D)
	Window_Svg *winSvg;
	#endif
	
	//DAVID
	MecaTronicaSerial *mecaTronicaSerial;
};



#endif
