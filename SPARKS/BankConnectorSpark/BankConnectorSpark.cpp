/// @file BankConnectorSpark.cpp
/// @brief Receives sentenes over the IFlow<char*> link (e.g. from Rebecca) and publish the contained command tags to all the subscribers linked by IFlow<Json::Value*>.

//#include "stdAfx.h"
#include <iostream>
#include <string>
#include <queue>
#include <cstdlib>
#include <ctime>
#include <cstring>
#include "BankConnectorSpark.h"
#include "jsoncpp/value.h"
#include "jsoncpp/reader.h"
#include "jsoncpp/writer.h"

#ifdef _WIN32
#else
extern "C" Component *createComponent(char *componentInstanceName,
		char *componentType, ComponentSystem *componentSystem) {
	if (!strcmp(componentType, "BankConnectorSpark")) {
		return new BankConnectorSpark(componentInstanceName,
				componentSystem);
	} else {
		return NULL;
	}
}
#endif

/// Initializes BankConnectorSpark component.
void BankConnectorSpark::init(void) {
	acceptedTagTypes.insert("bank");
	parametersMap.clear();
}

/// Unitializes the BankConnectorSpark component.
void BankConnectorSpark::quit(void) {

}


void BankConnectorSpark::processData(char* reply) {

	string receivedMessage(reply);

	Json::Value receivedJSON;
	Json::Value replyJSON;

	if (Json::Reader().parse(receivedMessage, receivedJSON)) {

		//LoggerInfo("BankConnector recibe de un spark de banca: %s", reply);
		myFlowChar->processData(reply);

	} else {
		LoggerInfo(
				"[FIONA-logger] BankConnectorSpark::processData> Error, received reply format is incorrect: %s",
				reply);
	}
}

/**
 * Petition incoming from CommandDispatcherSpark must be processed and reformatted for Weather type information provider Sparks connected.
 */
void BankConnectorSpark::processData(Json::Value* requestJSON) {

	// An id must be used to identify each request replys in processData(char*).

	// If the type is correct
	if (acceptedTagTypes.find(requestJSON->get("type", "").asString()) == acceptedTagTypes.end())
		return;

	string action = (*requestJSON).get("action", "").asString();
	if(action == "balance")
		(*requestJSON)["service"] = "productos";
	else if(action == "card_remain")
		(*requestJSON)["service"] = "tarjetas/{cardNumber}/movimientos?order=D";
	else if(action == "card_spent")
		(*requestJSON)["service"] = "tarjetas/{cardNumber}/movimientos?order=D";
	else if(action == "card_charge")
		(*requestJSON)["service"] = "tarjetas/{cardNumber}/movimientos?order=D";
	else if(action == "transfer")
		(*requestJSON)["service"] = "transferencias/nacional";
	else if(action == "instantmoney")
		(*requestJSON)["service"] = "instantmoney";
	//LoggerInfo("[FIONA-logger] BankConnectorSpark::processData> antes de enviar: %s", (*requestJSON).toStyledString().c_str());
	// In this case only forwarding is done.
	myFlowJSON->processData(requestJSON);

	/**
	 * Iniciar timeout y
	 */
}


