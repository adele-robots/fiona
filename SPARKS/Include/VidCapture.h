/// @file VidCapture.cpp
/// @brief VidCapture class implementation.


#ifndef __VidCapture_H
#define __VidCapture_H

#include <windows.h>
#include <VidCapDll.h>
#include "IAsyncFatalErrorHandler.h"
#include "IVideoConsumer.h"


/// @brief The class VidCapture is responsible from video aquisition,
/// via VidCapture library, of video frames obtained from camaras or
/// any DirectShow sources available.

class VidCapture {
public:
	VidCapture(IAsyncFatalErrorHandler *afeh) : asyncFatalErrorHandler(afeh) {}
	void init(IVideoConsumer *);
	void start(void);
	void stop(void);
	void quit(void);

	static BOOL WINAPI frameCallback(
		CVRES status, 
		struct CVIMAGESTRUCT* imagePtr, 
		void* userParam
	);

	static void getVideoModeSring(
	  	CVVIDCAPSYSTEM vidCapSystem,
		int videoModeIndex,
		char *buff, 
		int len
	);

	static char *getVidCaptureErrorMessage(CVRES);


private:
  	CVVIDCAPSYSTEM vidCapSystem;
	int videoModeIndex;
	CVCAPHANDLE capHandle;
	IAsyncFatalErrorHandler *asyncFatalErrorHandler;
	IVideoConsumer *videoConsumer;
};


#endif
