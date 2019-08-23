/// @file VisionController.h
/// @brief This file defines the vision controller class.
#ifndef __VISION_CONTROLLER_H
#define __VISION_CONTROLLER_H


#include "Configuration.h"
#include <opencv2/opencv.hpp>

using namespace cv;
using namespace std;

class FaceDetector;
class TrainingSet;
class FaceRecognizer;
class ImageProcessor;


typedef cv::Mat CV2Image;


///\brief Encapsulate all the computer vision functionalities.
///
///Main class of the module. This controller provides all the functionallity
///present in the computer vision module in an easy way.
///The two main functionallities must been initialized (constructor doesn't initializes any of them),
///calling to the init methods. Remenber to initialize only the functionallity your going to use, 
/// in order to avoid un-nedded computacion.
class VisionController {

public:
	
	//Default constructor
	VisionController(psisban::Config* loadedConfiguration);

	//Default destructor
	~VisionController();

	//Creates a new training set (empty)
	void createNewTrainingSet ();

	//This method must be called in order to inizialize the face detection functionallity.
	void initFaceDetection();

	///This method must be called in order to inizialize the face recognition functionallity.
	//It loads all the training data, this can take a while depending on the training data size.
	void initFaceRecognizer();

	//Detects the nearest face (the one with the biggest area) from a cv::Mat image.
	cv::Rect detectSingleFaceFromImage(const CV2Image inputImg);
	
	//Detects multiple faces from a cv::Mat image.
	std::vector <Rect> detectMultipleFaceFromImage (const CV2Image inputImg);
	
	//Add new person to the training set.
	bool trainNewPerson (std::vector <CV2Image>* newPersonImages, string newPersonName);
	
	//Returns a person name from the face detected in the image, and saves confidence and person name.
	string recognizePersonFromImage (const CV2Image inputImg, float& confidence);
	
	//Returns a person name from the face detected in the image, and saves confidence and person name.
	string recognizePersonFromFaceImage (const CV2Image inputFaceImg, float& confidence);
	
	/// Add a new persons to the training set (recognition).
	void trainNewPersonsFaces (std::vector<CV2Image>* newPersonsFaces, std::vector<string>* newPersonsNames);
	
	//Delete a person from the training set.
	void deletePerson (int id);

	//Persistence.
	void loadPersistenceData ();
	void savePersistenceData ();

	//Empty quit method.
	void quit();
	

private:

	///Preprocesses all images before detecting/recognizing a face.
	ImageProcessor* imgProc;
	
	///Provides the face detection functionallity to the application.
	FaceDetector* faceDetect;

	///Manages all the training data.
	TrainingSet* faceTrain;

	///Provides the face recognition functionallity to the application.
	FaceRecognizer* faceRecog;

	///Flag to know if the recognition system has been correctly initialized
	bool recognitionInitializated;

	///Flag to know if the detection system has been correctly initialized
	bool detectionInitializated;

	//Parameters to load from the configuration file.
	//Detection:
	char cascadeClassifierFilePathDetection [1024];
	cv::Size minFeatureSize;
	double searchScaleFactor;
	int minNeighbors;
	//Recognition:
	int trainSize;
	int facesPerPerson;
	int faceWidth;
	int faceHeight;
	int pcaComponents;
	char cascadeClassifierFilePathRecognition [1024];
	char persistenceFilePathTraining [1024];
	char persistenceFilePathRecognition [1024];


	
};


#endif