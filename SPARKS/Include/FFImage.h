/// @file FFImage.h
/// @brief FFImage class declaration.


#ifndef __FF_IMAGE_H
#define __FF_IMAGE_H

#ifdef _WIN32
#pragma warning(disable:4244) // FFMpeg silly warnings
#else
#endif


extern "C" {
#include <libavcodec/avcodec.h>
#include <libavformat/avformat.h>
#include <libswscale/swscale.h>
#include <libavcodec/opt.h>
}

#include "Image.h"


// Image with an ffmpeg's AVFrame in BGR24 colorspace
// Image space allocated by the class

class FFImage : public Image
{
public:
	FFImage(int width, int heigth);
	~FFImage(void);
	int getWidth();
	int getHeigth();
	IplImage *getIplImage();

public:
	AVFrame avFrame;
};


#endif
