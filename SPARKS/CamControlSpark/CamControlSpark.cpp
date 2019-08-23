/*
 * CamControlSpark.cpp
 *
 *  Created on: 02/12/2013
 *      Author: guille
 */

/// @file CamControlSpark.cpp
/// @brief CamControlSpark class implementation.


//#include "stdAfx.h"
#include "CamControlSpark.h"


#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "CamControlSpark")) {
			return new CamControlSpark(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif

vector<string> mouseInfo;

/// Initializes CamControlSpark component.
void CamControlSpark::init(void){
	intMouseCurrentPosX = 0;
	intMouseCurrentPosY = 0;
	intMouseStartMovePosX = 0;

	xPosCam = 0.63;
	yPosCam = 175.0;
	zPosCam = 80.0;
	moveCam = false;
	rotateCam = false;
}

/// Unitializes the CamControlSpark component.
void CamControlSpark::quit(void){
}

//IFlow implementation
void CamControlSpark::processData(char *msg){
	//LoggerInfo("CamControlSpark::processData | Message: %s",msg);

	string mouseInformation(msg);
	// Split by whitespace and safe mouse positions independently
	istringstream iss(mouseInformation);
	try{
		copy(istream_iterator<string>(iss),
				istream_iterator<string>(),
				back_inserter<vector<string> >(mouseInfo));

		if(mouseInfo.at(0)=="BUTTON"){
			if(mouseInfo.at(2)=="1"){
				if(mouseInfo.at(1)=="DOWN"){
					LoggerInfo("CamControlSpark::processData | BUTTON 1 DOWN");
					moveCam = true;
					intMouseStartMovePosX = intMouseCurrentPosX;
				}else{
					moveCam = false;
				}
			}

			//myCamera->setCameraPosition(0.63,175.0,120.0);
		}else{
			intMouseCurrentPosX = atoi(mouseInfo.at(0).c_str());
			intMouseCurrentPosY = atoi(mouseInfo.at(1).c_str());
			zPosCam+= atoi(mouseInfo.at(2).c_str())/100;
			//LoggerInfo("CamControlSpark::processData | No BUTTON PRESSED, ZOOM: %f",zPosCam);
			myCamera->setCameraPosition(xPosCam,yPosCam,zPosCam);
		}

		if(moveCam){
			xPosCam+=(intMouseStartMovePosX-intMouseCurrentPosX)/10;
			LoggerInfo("CamControlSpark::processData | Movería la cámara: %d",intMouseStartMovePosX-intMouseCurrentPosX);
			//myCamera->setCameraPosition(xPosCam,yPosCam,zPosCam);
		}

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

