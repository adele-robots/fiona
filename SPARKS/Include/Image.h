/// @file IImage.h
/// @brief IImage is a common interface for FFMpeg and CVCapture frames consumed by OpenCV

#ifndef __IMAGE_H
#define __IMAGE_H


#include <opencv2/opencv.hpp>

class Image {
public:
	Image(int w, int h) : width(w), height(h) {}
	int getWidth() { return width; }
	int getHeigth() { return height; }
	virtual IplImage *getIplImage() { return iplImage; }
	virtual ~Image() {}

protected:
	int width;
	int height;
	IplImage *iplImage;
};


#endif
