/// @file CookieThreadSpark.cpp
/// @brief CookieThreadSpark class implementation.


#include "CookieThreadSpark.h"


#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "CookieThreadSpark")) {
			return new CookieThreadSpark(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif

/// Initializes CookieThreadSpark component.
void CookieThreadSpark::init(void){
	//2-configurations
	myCookieComponent->componentConfiguration = componentConfiguration;
	myThreadComponent->componentConfiguration = componentConfiguration;

	//3-connections
	//required interfaces of CookieSpark
	myCookieComponent->myFlowChar = myFlow;

	//required interfaces of ThreadComponent
	myThreadComponent->myThreadProc = myCookieComponent;
	myThreadComponent->myAsyncFatalErrorHandler = myAsyncFatalErrorHandler;

	//4-initializations
	myCookieComponent->init();
	myThreadComponent->init();
}

/// Unitializes the CookieThreadSpark component.
void CookieThreadSpark::quit(void){
	myCookieComponent->quit();
	myThreadComponent->quit();
}

//IFlow implementation
void CookieThreadSpark::processData(Json::Value *prompt){
	myCookieComponent->processData(prompt);
}

//IConcurrent implementation
void CookieThreadSpark::start(void){
	myThreadComponent->start();
}

void CookieThreadSpark::stop(void){
	myThreadComponent->stop();
}




