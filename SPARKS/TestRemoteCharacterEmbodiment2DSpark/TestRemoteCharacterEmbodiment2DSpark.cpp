/// @file TestRemoteCharacterEmbodiment2DSpark.cpp
/// @brief TestRemoteCharacterEmbodiment2DSpark class implementation.

#include "stdAfx.h"
#include <stdlib.h>
#include <math.h>
#include "TestRemoteCharacterEmbodiment2DSpark.h"

#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "TestRemoteCharacterEmbodiment2DSpark")) {
			return new TestRemoteCharacterEmbodiment2DSpark(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif



void TestRemoteCharacterEmbodiment2DSpark::init(){
	running = true;
	//myFaceExpression->setFaceExpression("viseme_a_key",0.3);

	myEyes->rotateEye(12,14);
}




void TestRemoteCharacterEmbodiment2DSpark::quit(void)
{

}
