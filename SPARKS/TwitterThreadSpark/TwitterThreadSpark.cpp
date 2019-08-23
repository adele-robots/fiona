/// @file TwitterThreadSpark.cpp
/// @brief TwitterThreadSpark class implementation.


#include "TwitterThreadSpark.h"


#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "TwitterThreadSpark")) {
			return new TwitterThreadSpark(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif

/// Initializes TwitterThreadSpark component.
void TwitterThreadSpark::init(void){
	//2-configurations
	myTwitterComponent->componentConfiguration = componentConfiguration;
	myThreadComponent->componentConfiguration = componentConfiguration;

	//3-connections
	//required interfaces of TwitterSpark
	myTwitterComponent->myFlow = myFlow;

	//required interfaces of ThreadComponent
	myThreadComponent->myThreadProc = myTwitterComponent;

	//4-initializations
	myTwitterComponent->init();
	myThreadComponent->init();
}

/// Unitializes the TwitterThreadSpark component.
void TwitterThreadSpark::quit(void){
	myTwitterComponent->quit();
	myThreadComponent->quit();
}

//IFlow implementation
void TwitterThreadSpark::processData(char *prompt){
	myTwitterComponent->processData(prompt);
}

//IConcurrent implementation
void TwitterThreadSpark::start(void){
	myThreadComponent->start();
}

void TwitterThreadSpark::stop(void){
	myThreadComponent->stop();
}




