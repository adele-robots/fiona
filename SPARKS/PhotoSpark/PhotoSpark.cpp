/// @file PhotoSpark.cpp
/// @brief PhotoSpark class implementation.


#include "stdAfx.h"
#include "PhotoSpark.h"


#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "PhotoSpark")) {
			return new PhotoSpark(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif

/// Initializes PhotoSpark component.
void PhotoSpark::init(void){
	takePhoto = false;
	photoText = getComponentConfiguration()->getString(const_cast<char*>("PhotoText"));
	photoPath = getComponentConfiguration()->getString(const_cast<char*>("PhotoPath"));
	if(isJPG())
		quality = getComponentConfiguration()->getInt(const_cast<char*>("CV_IMWRITE_JPEG_QUALITY"));
	else if(isPNG())
		quality = getComponentConfiguration()->getInt(const_cast<char*>("CV_IMWRITE_PNG_COMPRESSION"));
}

/// Unitializes the PhotoSpark component.
void PhotoSpark::quit(void){
	//deletePhoto();
}


// Implement IFlow<char *>
void PhotoSpark::processData(char * prompt) {
	std::string text(prompt);
	if(text.find(photoText) != std::string::npos)
		takePhoto = true;
}

// Implement IFlow<Image *>
void PhotoSpark::processData(Image * image) {
	if(takePhoto) {
		LoggerInfo("Saving image to: %s", photoPath.c_str());
		cv::Mat mat(image->getIplImage());
	    vector<int> compression_params;
	    if(isJPG()) {
	    	compression_params.push_back(CV_IMWRITE_JPEG_QUALITY);
	    }
	    else if(isPNG()) {
	    	compression_params.push_back(CV_IMWRITE_PNG_COMPRESSION);
	    }
    	compression_params.push_back(quality);
	    try {
	    	if(! imwrite(photoPath.c_str(), mat, compression_params))
	    		throw Exception("imwrite returned false");
	    }
	    catch (Exception& ex) {
	        LoggerError("Exception converting image to PNG format: %s\n", ex.msg);
	    }
		takePhoto = false;
	}
}

bool PhotoSpark::isJPG() {
	if(photoPath.find("jpg", photoPath.size() - 3))
		return true;
	return false;
}

bool PhotoSpark::isPNG() {
	if(photoPath.find("png", photoPath.size() - 3))
		return true;
	return false;
}
