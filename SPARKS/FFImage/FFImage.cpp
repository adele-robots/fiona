/// @file FFImage.cpp
/// @brief FFImage class implementation.


#include "stdafx.h"
#include "FFImage.h"

#ifdef _WIN32
#pragma comment(lib, OPENCV_IMGPROC_LIB_RELEASE)
#else
#endif

FFImage::FFImage(int w, int h) : Image(w, h) {
	int imgBufferSize = avpicture_get_size( 
		PIX_FMT_BGR24,
		width, 
		height
	);

	avFrame.data[0] = (uint8_t *)malloc(imgBufferSize);

	// OJO: necesario?
	avpicture_fill(
		(AVPicture*)&avFrame, 
		avFrame.data[0],
		PIX_FMT_BGR24, 
		width, 
		height
	);
}


FFImage::~FFImage() {
	free(avFrame.data[0]);
}


IplImage *FFImage::getIplImage() {
	iplImage = cvCreateImageHeader(
		cvSize(width, height), 
		IPL_DEPTH_8U,			// depth per channel
		3
	);


	cvSetData(iplImage, avFrame.data[0], avFrame.linesize[0] );

	return iplImage;
}

