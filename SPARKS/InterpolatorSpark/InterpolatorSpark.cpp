/*
 * InterpolatorSpark.cpp
 *
 *  Created on: 28/11/2013
 *      Author: guille
 */

/// @file InterpolatorSpark.cpp
/// @brief InterpolatorSpark class implementation.


//#include "stdAfx.h"
#include "InterpolatorSpark.h"


#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "InterpolatorSpark")) {
			return new InterpolatorSpark(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif


//Auxiliary function
string convertInt(int number)
{
   stringstream ss;//create a stringstream
   ss << number;//add number to the stream
   return ss.str();//return a string with the contents of the stream
}

/// Initializes InterpolatorSpark component.
void InterpolatorSpark::init(void){
	// Load flash player dimensions
	playerWidth = getComponentConfiguration()->getInt(const_cast<char *>("Player_Width"));
	playerHeight = getComponentConfiguration()->getInt(const_cast<char *>("Player_Height"));

	mouseIncrement = getComponentConfiguration()->getInt(const_cast<char *>("Mouse_Increment"));

	// Load noise parameters
	noiseFactor = getComponentConfiguration()->getInt(const_cast<char *>("Noise_Factor"));
	offsetIncrement = getComponentConfiguration()->getInt(const_cast<char *>("Offset_Increment"));
	numScales = getComponentConfiguration()->getInt(const_cast<char *>("Scales"));
	persistence = getComponentConfiguration()->getFloat(const_cast<char *>("Persistence"));
	loBound = getComponentConfiguration()->getInt(const_cast<char *>("Low_Bound"));
	hiBound = getComponentConfiguration()->getInt(const_cast<char *>("High_Bound"));

	//Initialize variables
	mouseCurrentPosX = playerWidth/2;
	mouseCurrentPosY = playerHeight/2;
	xoff = 1.0;
	currentNoise = 0;
	noiseTarget = scaled_octave_noise_2d(numScales,persistence,1,loBound,hiBound,xoff,0);
}

/// Unitializes the InterpolatorSpark component.
void InterpolatorSpark::quit(void){
}

//IFlow<char*> implementation
void InterpolatorSpark::processData(char * prompt) {
	//LoggerInfo("InterpolatorSpark::processData | Entrada Mensaje : %s",prompt);

	string mouseInformation(prompt);
	// Split by whitespace and safe mouse positions independently
	istringstream iss(mouseInformation);
	try{
		// Parse and save information received
		copy(istream_iterator<string>(iss),
				istream_iterator<string>(),
				back_inserter<vector<string> >(mouseInfo));

		// Check if mouse position info is sent (not button clicked info wanted)
		if(mouseInfo.at(0) != "BUTTON"){
			// Update mouse positions in independent variables and clear vector to hold new values
			mouseTargetPosX = atoi(mouseInfo.at(0).c_str());
			mouseTargetPosY = atoi(mouseInfo.at(1).c_str());

			updateNoise();
			updateMouse();
			//LoggerInfo("InterpolatorSpark::processData | MA: %d @ NA: %d @ MT: %d @ NT: %d",mouseCurrentPosX,currentNoise,mouseTargetPosX,noiseTarget);

			// Convert new mouse position values to string and send them. Check if they are in valid range
			int newMousePosX = mouseCurrentPosX+currentNoise;
			if(newMousePosX < 0){
				newMousePosX = 0;
			}else if(newMousePosX > 640){
				newMousePosX = 640;
			}
			string mousePositions(convertInt(newMousePosX)+" "+convertInt(mouseTargetPosY));
			myFlow->processData(const_cast<char *>(mousePositions.c_str()));
		}
		// Clear vector to hold new values the next call
		mouseInfo.clear();
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

void InterpolatorSpark::updateNoise(void){
	if(currentNoise/noiseFactor == noiseTarget/noiseFactor){
		//Update offset, lower increment values, smoother noise
		xoff+=offsetIncrement;
		// Calculate new noise target
		noiseTarget = scaled_octave_noise_2d(numScales,persistence,1,loBound,hiBound,xoff,0);
	} else if (currentNoise > noiseTarget){
		currentNoise-=noiseFactor;
	}
	else{
		currentNoise+=noiseFactor;
	}
}

void InterpolatorSpark::updateMouse(void){
	if(mouseCurrentPosX/mouseIncrement != mouseTargetPosX/mouseIncrement){
		if (mouseCurrentPosX > mouseTargetPosX){
			mouseCurrentPosX-= mouseIncrement;
		}else{
			mouseCurrentPosX+= mouseIncrement;
		}
	}
}



