#ifndef __I_OUTGOING_IMAGE_H
#define __I_OUTGOING_IMAGE_H

class OutgoingImage {
public:
	OutgoingImage(int width, int height, int bytesPerPixel, unsigned char *buffer);
	int width;
	int height;
	int bytesPerPixel;
	unsigned char *buffer;
	int bufferSize;
};

#endif
