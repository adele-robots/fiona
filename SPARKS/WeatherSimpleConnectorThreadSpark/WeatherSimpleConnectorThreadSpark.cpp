/// @file WeatherSimpleConnectorThreadSpark.cpp
/// @brief WeatherSimpleConnectorThreadSpark class implementation.


//#include "stdAfx.h"
#include "WeatherSimpleConnectorThreadSpark.h"


#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "WeatherSimpleConnectorThreadSpark")) {
			return new WeatherSimpleConnectorThreadSpark(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif

/// Initializes WeatherSimpleConnectorThreadSpark component.
void WeatherSimpleConnectorThreadSpark::init(void){

	myWeatherSimpleConnectorSpark->componentConfiguration = componentConfiguration;
	myThreadComponent->componentConfiguration = componentConfiguration;

	myWeatherSimpleConnectorSpark->myFlowChar = myFlowChar;
	myWeatherSimpleConnectorSpark->myFlowJSON = myFlowJSON;

	myThreadComponent->myThreadProc = myWeatherSimpleConnectorSpark;
	myThreadComponent->myAsyncFatalErrorHandler = myAsyncFatalErrorHandler;

	myWeatherSimpleConnectorSpark->init();
	myThreadComponent->init();

}

/// Unitializes the WeatherSimpleConnectorThreadSpark component.
void WeatherSimpleConnectorThreadSpark::quit(void){

	myWeatherSimpleConnectorSpark->quit();
	myThreadComponent->quit();
}

void WeatherSimpleConnectorThreadSpark::processData(char* message){
	LoggerInfo("WeatherSimpleConnectorThreadSpark::processData> Antes de processData(char)");
	myWeatherSimpleConnectorSpark->processData(message);
	LoggerInfo("WeatherSimpleConnectorThreadSpark::processData> Despues de processData(char)");
}

void WeatherSimpleConnectorThreadSpark::processData(Json::Value* messageJSON){
	LoggerInfo("WeatherSimpleConnectorThreadSpark::processData> Antes de processData(JSON)");
	myWeatherSimpleConnectorSpark->processData(messageJSON);
	LoggerInfo("WeatherSimpleConnectorThreadSpark::processData> Despues de processData(JSON)");
}

void WeatherSimpleConnectorThreadSpark::start(){
	myThreadComponent->start();
}

void WeatherSimpleConnectorThreadSpark::stop(){
	myThreadComponent->stop();
}
