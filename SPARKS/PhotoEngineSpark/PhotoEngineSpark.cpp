/*
 * PhotoEngineSpark.cpp
 *
 *  Created on: 12/11/2013
 *      Author: guille
 */

/// @file PhotoEngineSpark.cpp
/// @brief PhotoEngineSpark class implementation.


#include "PhotoEngineSpark.h"


#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "PhotoEngineSpark")) {
			return new PhotoEngineSpark(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif

//Array with mouse positions x -> index[0]; y -> index[1]
vector<string> mousePos;
vector<cv::Mat> avatarSequence;

/// Initializes PhotoEngineSpark component.
void PhotoEngineSpark::init(void){
	//Initialize mouse positions
	mousePosX = "0";
	mousePosY = "0";

	try{
		// Load all the images located within the directory
		string path = getComponentConfiguration()->getString(const_cast<char *>("Avatar_Images_Dir"));
		//LoggerInfo("PhotoEngineSpark::init | path: %s",path.c_str());
		//string path("/home/guille/Descargas/raquel/sueltas/%03d.jpg");
		cv::VideoCapture sequence(path);
		cv::Mat image;
		if (!sequence.isOpened()){
			LoggerError("Failed to open Image Sequence!");
			exit(-1);
		}else{
			int i=1;
			for(;;)
			{
				sequence >> image;
				if(image.empty()){
					LoggerInfo("PhotoEngineSpark::init | End of Sequence !");
					//image.release(); //esto es necesario si está 'empty' ?
					break;
				}else{
					LoggerInfo("PhotoEngineSpark::init | Image #%d",i);
					// Flip image around the x-axis
					cv::flip(image, image,0);
					avatarSequence.push_back(image.clone());
					//image.release();
					i++;
				}
			}
			numFrames = avatarSequence.size();
			LoggerInfo("PhotoEngineSpark::init | avatarSequence size: %d", numFrames);

			frameTrackStep = 640.0/numFrames; //640 es el ancho de la pantalla del reproductor del avatar
			LoggerInfo("PhotoEngineSpark::init | frameTrackStep: %f", frameTrackStep);
		}

		//sequence.release();

		LoggerInfo("PhotoEngineSpark::init | cvLoadImage !");
		//image  = cvLoadImage("/home/guille/Descargas/raquel/raquel55.jpg",-1);
		//image  = cvLoadImage("/home/guille/Installations/ffmpeg/ffmpeg-1.2/cam.png",-1);

//		image1  = cvLoadImage("/home/guille/Descargas/raquel/sueltas/001.jpg",-1);
//		image2  = cvLoadImage("/home/guille/Descargas/raquel/sueltas/002.jpg",-1);
//		image3 = cvLoadImage("/home/guille/Descargas/raquel/sueltas/003.jpg",-1);
//		image4 = cvLoadImage("/home/guille/Descargas/raquel/sueltas/004.jpg",-1);
//		image5 = cvLoadImage("/home/guille/Descargas/raquel/sueltas/005.jpg",-1);
//
//		if (image1 == 0 ) {
//			LoggerError("An error occurred while loading an advertising image\n");
//			exit(-1);
//		}else{
//			// Horizontal flip
//			cvFlip(image1,image1,0);
//			cvFlip(image2,image2,0);
//			cvFlip(image3,image3,0);
//			cvFlip(image4,image4,0);
//			cvFlip(image5,image5,0);
//		}
	}catch(exception const & ex) {
		LoggerError("Caught a std::exception: %s",ex.what());
		exit(-1);
	}
	catch(...) {
		LoggerError("A non-std::exception was thrown! Srsly.");
		exit(-1);
	}

}

/// Unitializes the PhotoEngineSpark component.
void PhotoEngineSpark::quit(void){

	// RELEASE avatarSequence!!
}

//IFlow implementation
void PhotoEngineSpark::processData(char *msg){
	 //LoggerInfo("PhotoEngineSpark::processData | Mensaje : %s",msg);

	 string mousePositions(msg);
	 // Split by whitespace and safe mouse positions independently
	 istringstream iss(mousePositions);
	 try{
		 copy(istream_iterator<string>(iss),
				 istream_iterator<string>(),
				 back_inserter<vector<string> >(mousePos));

		 // Update mouse positions in independent variables and clear vector to hold new values
		 mousePosX = mousePos.at(0);
		 mousePosY = mousePos.at(1);
		 mousePos.clear();

	 }catch(out_of_range& ex){
		 LoggerError("[FIONA-logger]Out of range exception caught: %s", ex.what());
		 std::cerr << "Caught an \"out of range\" exception: " << ex.what() << endl;
	 }catch(exception const & ex) {
		 LoggerError("[FIONA-logger]Caught a std::exception: %s",ex.what());
		 std::cerr << "Caught a std::exception: " << ex.what() << endl;
		 return;
	 }catch(...) {
		 LoggerInfo("[FIONA-logger]A non-std::exception was thrown! Srsly.");
		 std::cerr << "A non-std::exception was thrown! Srsly.\n";
		 return;
	 }
}

// IRenderizable implementation
void* PhotoEngineSpark::render(void)
{
	// *!*! mejor float la posición???
	int intMousePosX = atoi(mousePosX.c_str());
	int frame = intMousePosX/frameTrackStep;
	//LoggerInfo("[FIONA-logger]PhotoEngineSpark::render | MouseCurrentX: %d @ Frame #%d",intMousePosX,frame);

	try{
		if(frame < 0){
			LoggerInfo("[FIONA-logger]PhotoEngineSpark::render | Mouse pos al LÍMITE INFERIOR!!");
			frame = 0;
		}
		if(frame >= numFrames){
			LoggerInfo("[FIONA-logger]PhotoEngineSpark::render | Mouse pos al LÍMITE SUPERIOR!!");
			frame = numFrames-1;
		}

		return static_cast<void *>(avatarSequence.at(frame).data);

//		if(intMousePosX >=0 && intMousePosX < frameTrackStep){
//			return static_cast<void *>(avatarSequence.at(0).data);
//			//return static_cast<void *>(image1->imageData);
//		}else if(intMousePosX >=frameTrackStep && intMousePosX < frameTrackStep*2){
//			return static_cast<void *>(avatarSequence.at(1).data);
//			//return static_cast<void *>(image2->imageData);
//		}else if(intMousePosX >=frameTrackStep*2 && intMousePosX < frameTrackStep*3){
//			return static_cast<void *>(avatarSequence.at(2).data);
//			//return static_cast<void *>(image3->imageData);
//		}else if(intMousePosX >=frameTrackStep*3 && intMousePosX < frameTrackStep*4){
//			return static_cast<void *>(avatarSequence.at(3).data);
//			//return static_cast<void *>(image4->imageData);
//		}else if(intMousePosX >=frameTrackStep*4 && intMousePosX <= frameTrackStep*5){
//			return static_cast<void *>(avatarSequence.at(4).data);
//			//return static_cast<void *>(image5->imageData);
//		}else{
//			return static_cast<void *>(image3->imageData);
//		}
	}catch(out_of_range& ex){
		LoggerError("[FIONA-logger]Out of range exception caught: %s", ex.what());
		std::cerr << "Caught an \"out of range\" exception: " << ex.what() << endl;
		return NULL;
	}catch(exception const & ex) {
		LoggerError("[FIONA-logger]Caught a std::exception: %s",ex.what());
		std::cerr << "Caught a std::exception: " << ex.what() << endl;
		return NULL;
	}catch(...) {
		LoggerInfo("[FIONA-logger]A non-std::exception was thrown! Srsly.");
		std::cerr << "A non-std::exception was thrown! Srsly.\n";
		return NULL;
	}
}

void PhotoEngineSpark::unMapResourceStream(void){

}
