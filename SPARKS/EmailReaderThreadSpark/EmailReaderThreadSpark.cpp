/// @file EmailReaderThreadSpark.cpp
/// @brief EmailReaderThreadSpark class implementation.


#include "EmailReaderThreadSpark.h"


#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "EmailReaderThreadSpark")) {
			return new EmailReaderThreadSpark(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif

/// Initializes EmailReaderThreadSpark component.
void EmailReaderThreadSpark::init(void){
	//2-configurations
	myEmailReaderComponent->componentConfiguration = componentConfiguration;
	myThreadComponent->componentConfiguration = componentConfiguration;

	//3-connections
	//required interfaces of EmailReaderSpark
	myEmailReaderComponent->myFlow = myFlow;

	//required interfaces of ThreadComponent
	myThreadComponent->myThreadProc = myEmailReaderComponent;

	//4-initializations
	myEmailReaderComponent->init();
	myThreadComponent->init();
}

/// Unitializes the EmailReaderThreadSpark component.
void EmailReaderThreadSpark::quit(void){
	myEmailReaderComponent->quit();
	myThreadComponent->quit();
}

//IFlow implementation
void EmailReaderThreadSpark::processData(char *prompt){
	myEmailReaderComponent->processData(prompt);
}

//IConcurrent implementation
void EmailReaderThreadSpark::start(void){
	myThreadComponent->start();
}

void EmailReaderThreadSpark::stop(void){
	myThreadComponent->stop();
}




