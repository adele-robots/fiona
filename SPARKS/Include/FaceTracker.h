/// @file FaceTracker.h
/// @brief FaceTracker class definition.

#ifndef __FACE_TRACKER_H
#define __FACE_TRACKER_H

#include <opencv2/opencv.hpp>
#include "MotionController.h"
#include "VideoQueue.h"
#include "IVideoConsumer.h"
#include "Thread.h"
#include "IImage.h"


/// Class that wraps openCV algorithm for Face Detection based on Haar classifiers.

class FaceTracker : public IVideoConsumer, public Thread {
public:
	FaceTracker(MotionController *m, IAsyncFatalErrorHandler *afeh) :
		motionController(m), 
		IVideoConsumer(),
		Thread("FaceTracker-Thread", afeh)
	{}

	void init(void);

	void processImage(IplImage *image);

	// IVideoConsumer (queues frame)
	void consumeVideoFrame(IImage *image);

	// dequeue and process frames. Calls processImage.
	void process(void);

	void quit(void);

public:
	int framesPerFace;
	float maxDisplacementFromCenter;

	float faceHorizontalRatio;
	float faceVerticalRatio;

private:
	// Memory for calculations
	CvMemStorage* storage;

	// Haar classifier cascade
	CvHaarClassifierCascade* cascade;

	MotionController *motionController;
	VideoQueue videoQueue;

	int ElegirCara(CvSeq *faces, int frameWidth, int frameHeight);
	void detectFace(IplImage *image);

	float *pHorizontalPosCopy;
	float *pVerticalPosCopy;
	bool *pIsFaceDetectedCopy;

	int camaraDirectShowIndex;

};


#endif
