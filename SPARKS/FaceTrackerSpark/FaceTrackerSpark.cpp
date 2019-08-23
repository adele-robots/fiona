/// @file FaceTrackerSpark.cpp
/// @brief FaceTrackerSpark class implementation.

// Third party libraries are linked explicitly once in the project.
// #pragma comment(lib, "thirdPartyLib.lib")

#include "stdAfx.h"
#include "FaceTrackerSpark.h"


using namespace std;

#ifdef _WIN32
#pragma comment(lib, OPENCV_HIGHGUI_LIB_RELEASE)

#pragma comment(lib, OPENCV_CORE_LIB_RELEASE)
#pragma comment(lib, OPENCV_IMGPROC_LIB_RELEASE)
#pragma comment(lib, OPENCV_OBJDETECT_LIB_RELEASE)

#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "FaceTrackerSpark")) {
			return new FaceTrackerSpark(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif



/// Initializes FaceTrackerSpark component.

void FaceTrackerSpark::init(void)
{
	posx = posy = 0;

	int frameQueueSize = getComponentConfiguration()->getInt("FaceTracker_FrameQueueSize");

	videoQueue.init(frameQueueSize);

	faceHorizontalRatio = 0;
	faceVerticalRatio = 0;


	framesPerFace = getComponentConfiguration()->getInt("FaceTracker_FramesPerFace");


	storage = NULL;
	cascade = NULL;

 
	// Load the HaarClassifierCascade

	char cascadeFileName[1024];

	getComponentConfiguration()->getFilePath(
			"FaceTracker_HaarClassifierDataFile",
			cascadeFileName,
			1024
		);

    cascade = (CvHaarClassifierCascade*)cvLoad(cascadeFileName);

	if( !cascade ) {
		ERR("Error opening Haar cascade file '%s'", cascadeFileName);
	}

	// Allocate the memory storage
    storage = cvCreateMemStorage(0);
}


/// Unitializes the FaceTrackerSpark component.

void FaceTrackerSpark::quit(void) {
}

// IFlow implementation
void FaceTrackerSpark::processData(Image *image) {
	videoQueue.queueVideoFrame(image);
}

//IThreadProc implementation
void FaceTrackerSpark::process() {
	Image *image = videoQueue.dequeueVideoFrame();

	// Image NULL on thread cancellation
	if (image == NULL) return;

	IplImage *iplImage = image->getIplImage();
	if (iplImage == NULL) {
		ERR("Error converting image");
	}
	processImage(iplImage);
	delete image; // destroys also IplImage header
}

/// Acquires a video frame and process it for face detection, storing in
/// the locations passed in initialization time, results 0 <= x, y <= 1 indicating
/// the position of the chosen detected face (if any), unmodified if none.
/// Do nothing if we have not grabbed the camara.

void FaceTrackerSpark::processImage(IplImage *image) {
	detectFace(image);
}

void drawDetectedFaces(IplImage *image, CvSeq *faces) {
	if (faces == NULL) return;

	for (int i = 0; i < faces->total; i++) {
		CvRect* r = (CvRect*)cvGetSeqElem(faces, i);
		cvRectangle(image, cvPoint(r->x, r->y), cvPoint(r->x + r->width, r->y + r->height), CV_RGB(0, 255, 0));
	}
}


/// Function to detect and draw any faces that are present in an image

void FaceTrackerSpark::detectFace(IplImage *image) {

	// Clear the memory storage which was used before
    cvClearMemStorage( storage );

    // Find whether the cascade is loaded, to find the faces. If yes, then:
    if( cascade )
    {

        // There can be more than one face in an image. So create a growable sequence of faces.
        // Detect the objects and store them in the sequence
        CvSeq* faces = cvHaarDetectObjects(
			image, 
			cascade, 
			storage,
			1.1, 
			2, 
			CV_HAAR_DO_CANNY_PRUNING,
			cvSize(40, 40)
		);

		

		bool isFaceDetected = (faces != NULL) && (faces->total > 0);


		CvPoint centro;
		if (faces && faces->total > 0) {
			int caraElegida = ElegirCara(faces, image->width, image->height);

			CvRect* r = (CvRect*)cvGetSeqElem( faces, caraElegida);
			centro.x = r->x + r->width/2;
			centro.y = r->y + r->height/2;

			posx = (double)(centro.x - (image->width / 2)) / (double)(image->width / 2);
			posy = (double)(centro.y - (image->height / 2)) / (double)(image->height / 2);
		}
		

		myDetectedFacePositionConsumer->consumeDetectedFacePosition(
			isFaceDetected, 
			posx, 
			posy
		);

		if (getComponentConfiguration()->getBool("FaceTracker_ShowWindow")) {

			static bool faceTrackerWindowInitialized = false;
			const char *faceTrackerWindowName = "Face Tracker";

			if (!faceTrackerWindowInitialized) {
				faceTrackerWindowInitialized = true;
				cvNamedWindow(faceTrackerWindowName);
			}

			CvPoint markerCenter;
			int radius = 20;

			markerCenter.x = (int)((posx/2 + 0.5f) * image->width);
			markerCenter.y = (int)((posy/2 + 0.5f) * image->height);

			cvCircle(image, markerCenter, radius, CV_RGB(255, 0, 0), 3, 8, 0);

			drawDetectedFaces(image, faces);
			
			cvShowImage(faceTrackerWindowName, image);
			cvWaitKey(1);
		}
    }
}
static inline int cuadrado(int x) { return x*x; }


static int cuadradoDistancia(CvPoint c, CvSeq *faces, int faceIndex) {
	CvRect* r = (CvRect*)cvGetSeqElem( faces, faceIndex);
	return cuadrado(c.x - (r->x + r->width/2)) +
		cuadrado(c.y - (r->y + r->height/2));
}


/// if more than one face is detected, the chosen face is that which is deteted nearest
/// to the previous detection. Each time after a fixed number of detections, 
/// a random face gets chosen to change the attention focus deliberately.

int FaceTrackerSpark::ElegirCara(CvSeq *faces, int frameWidth, int frameHeight) {
	static int count = 0;
	static CvPoint centroCaraAnterior;

	int caraElegida = 0;

	if (count++ % framesPerFace) {
		int menorCuadradoDistancia = cuadradoDistancia(centroCaraAnterior, faces, 0);
		for(int i = 0; i < faces->total; i++ ) {
			if (menorCuadradoDistancia > cuadradoDistancia(centroCaraAnterior, faces, i)) {
				menorCuadradoDistancia = cuadradoDistancia(centroCaraAnterior, faces, i);
				caraElegida = i;
			}
		}
	}
	else {
		caraElegida = rand() % faces->total;
	}


	CvRect* r = (CvRect*)cvGetSeqElem( faces, caraElegida);

	centroCaraAnterior.x = r->x + r->width/2;
	centroCaraAnterior.y = r->y + r->height/2;
	
	return caraElegida;
}
