/// @file vidCaptureUtils.h
/// @brief Utility functions declaration for VidCapture library.


#ifndef __VID_CAPTURE_UTILS_H
#define __VID_CAPTURE_UTILS_H

#include "VidCapture.h"


char *getVidCaptureErrorMessage(CVRES cvRes);

void getVideoModeSring(
	CVVidCapture *vidCap, 
	CVVidCapture::VIDCAP_MODE &vidcapMode, 
	char *buff, 
	int len
);


#endif
