/**
 *
 * @file VCImage.h
 *
 * @brief IImage wrapping an externally supplied VidCapture::CVImage.
 *
 * The supplied image reference count is incremented/decremented in ctor/dtor
 * The colorspace of the resulting image is optionally converted, RGB->BGR 
 * (BGR is openCV's choice).
 *
 */

#ifndef _VC_IMAGE_H
#define _VC_IMAGE_H

#include <windows.h>
#include <VidCapDll.h>
#include <opencv2/opencv.hpp>

#include "IImage.h"


/// VCImage wraps CVImages of vidCapture library

class VCImage : public IImage
{
public:
	VCImage(CVIMAGESTRUCT* image);
	~VCImage();
	int getWidth();
	int getHeigth();

private:
	CVIMAGESTRUCT* vidCaptureImage;
	IplImage *openCvImage;
};


#endif
