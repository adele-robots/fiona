#include "stdAfx.h"
#include "OutgoingImage.h"

OutgoingImage::OutgoingImage(int width, int height, int bytesPerPixel, unsigned char *buffer) 
{
	this->width = width;
	this->height = height;
	this->bytesPerPixel = bytesPerPixel;
	this->bufferSize = width * height * bytesPerPixel;
	this->buffer = buffer;
}
