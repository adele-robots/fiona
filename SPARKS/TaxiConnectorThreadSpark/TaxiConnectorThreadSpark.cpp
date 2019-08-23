/// @file TaxiConnectorThreadSpark.cpp
/// @brief TaxiConnectorThreadSpark class implementation.


//#include "stdAfx.h"
#include "TaxiConnectorThreadSpark.h"


#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "TaxiConnectorThreadSpark")) {
			return new TaxiConnectorThreadSpark(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif

/// Initializes TaxiConnectorThreadSpark component.
void TaxiConnectorThreadSpark::init(void){

	myTaxiConnectorSpark->componentConfiguration = componentConfiguration;
	myThreadComponent->componentConfiguration = componentConfiguration;

	myTaxiConnectorSpark->myFlowChar = myFlowChar;
	myTaxiConnectorSpark->myFlowJSON = myFlowJSON;

	myThreadComponent->myThreadProc = myTaxiConnectorSpark;
	myThreadComponent->myAsyncFatalErrorHandler = myAsyncFatalErrorHandler;

	myTaxiConnectorSpark->init();
	myThreadComponent->init();

}

/// Unitializes the TaxiConnectorThreadSpark component.
void TaxiConnectorThreadSpark::quit(void){

	myTaxiConnectorSpark->quit();
	myThreadComponent->quit();
}

void TaxiConnectorThreadSpark::processData(char* message){
	LoggerInfo("TaxiConnectorThreadSpark::processData> Antes de processData(char)");
	myTaxiConnectorSpark->processData(message);
	LoggerInfo("TaxiConnectorThreadSpark::processData> Despues de processData(char)");
}

void TaxiConnectorThreadSpark::processData(Json::Value* messageJSON){
	LoggerInfo("TaxiConnectorThreadSpark::processData> Antes de processData(JSON)");
	myTaxiConnectorSpark->processData(messageJSON);
	LoggerInfo("TaxiConnectorThreadSpark::processData> Despues de processData(JSON)");
}

void TaxiConnectorThreadSpark::start(){
	myThreadComponent->start();
}

void TaxiConnectorThreadSpark::stop(){
	myThreadComponent->stop();
}
