/// @file TestNoVoiceSpark.cpp
/// @brief TestNoVoiceSpark class implementation.


#include "stdAfx.h"
#include "TestNoVoiceSpark.h"


#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "TestNoVoiceSpark")) {
			return new TestNoVoiceSpark(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif

/// Initializes TestNoVoiceSpark component.
void TestNoVoiceSpark::init(void){
}

/// Unitializes the TestNoVoiceSpark component.
void TestNoVoiceSpark::quit(void){
}

//**To change for your convenience**
//Example of provided interface implementation
int TestNoVoiceSpark::getStoredAudioSize()
{
	return 0;
}
void TestNoVoiceSpark::dequeueAudioBuffer(char *buffer, int size)
{
	//nothing
}

//IControlVoice
void TestNoVoiceSpark::startSpeaking(void)
{
	//nothing
}
void TestNoVoiceSpark::stopSpeaking(void)
{
	//nothing
}
void TestNoVoiceSpark::startVoice(void)
{
	//nothing
}


