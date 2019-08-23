/// @file TestRebecca.cpp
/// @brief TestRebecca class implementation.

#include "TestRebecca.h"


#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "TestRebecca")) {
			return new TestRebecca(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif

/// Initializes TestRebecca component.
void TestRebecca::init(void){
	running = true;
}

/// Unitializes the TestRebecca component.
void TestRebecca::quit(void){
}

void TestRebecca::run(void){
	while (running){
		myVoice->sayThis("how old are you");
		sleep(3);
	}
}

void TestRebecca::sayThis(char *prompt){
	cout << "TestRebecca is answered with: " << prompt << endl;
}

void TestRebecca::waitEndOfSpeech(void){
}

void TestRebecca::stopSpeech(void){
}
