/// @file FaceTrackerSpark.cpp
/// @brief Component FaceTrackerSpark main class.


#ifndef __FaceTrackerSpark_H
#define __FaceTrackerSpark_H

#include "Component.h"

// Provided
#include "IFlow.h"
#include "IThreadProc.h"

// Required
#include "IDetectedFacePositionConsumer.h"

#include "VideoQueue.h"


/// @brief This is the main class for component FaceTrackerSpark.
///
/// Detects faces, selects one, and gives its (x,y) position 

class FaceTrackerSpark :
	public Component,

	public IFlow<Image *>,
	public IThreadProc
{
public:
	FaceTrackerSpark(
		char *instanceName, 
		ComponentSystem *cs
	) : Component(instanceName, cs)
	{}

//private:
	IDetectedFacePositionConsumer *myDetectedFacePositionConsumer;

	void initializeRequiredInterfaces() {	
		requestRequiredInterface<IDetectedFacePositionConsumer>(&myDetectedFacePositionConsumer);
	}

public:
	bool isFaceDetected;
	void init(void);
	void quit(void);
	
	//IFlow implementation
	void processData(Image *);
	
	//IThreadProc implementation
	void process();

private:
	void processImage(IplImage *image);

private:
	int framesPerFace;
	float faceHorizontalRatio;
	float faceVerticalRatio;

private:
	// Memory for calculations
	CvMemStorage* storage;

	// Haar classifier cascade
	CvHaarClassifierCascade* cascade;
	VideoQueue videoQueue;

	int ElegirCara(CvSeq *faces, int frameWidth, int frameHeight);
	void detectFace(IplImage *image);

private:
	double posx;
	double posy;
};


#endif
