/*
 * TicketConnectorSpark.cpp
 *
 *  Created on: 19/02/2015
 *      Author: Adrián
 */
/// @file TicketConnectorSpark.cpp
/// @brief Receives sentences over the IFlow<char*> link (e.g. from Rebecca) and publish the contained command tags to all the subscribers linked by IFlow<Json::Value*>.

#include "TicketConnectorSpark.h"

#ifdef _WIN32
#else
extern "C" Component *createComponent(char *componentInstanceName,
		char *componentType, ComponentSystem *componentSystem) {
	if (!strcmp(componentType, "TicketConnectorSpark")) {
		return new TicketConnectorSpark(componentInstanceName,
				componentSystem);
	} else {
		return NULL;
	}
}
#endif

/// Initializes TicketConnectorSpark component.
void TicketConnectorSpark::init(void) {
	acceptedTagTypes.insert("ticket");
}

/// Unitializes the TicketConnectorSpark component.
void TicketConnectorSpark::quit(void) {

}


void TicketConnectorSpark::processData(char* reply) {

	string receivedMessage(reply);

	Json::Value receivedJSON;
	Json::Value replyJSON;

	if (Json::Reader().parse(receivedMessage, receivedJSON)) {

		//LoggerInfo("TicketConnector recibe de un spark de tickets: %s", reply);
		myFlowChar->processData(reply);

	} else {
		LoggerInfo(
				"[FIONA-logger] TicketConnectorSpark::processData> Error, received reply format is incorrect: %s",
				reply);
	}
}

/**
 * Petition incoming from CommandDispatcherSpark must be processed and reformatted for Ticket type information provider Sparks connected.
 */
void TicketConnectorSpark::processData(Json::Value* requestJSON) {

	// An id must be used to identify each request replys in processData(char*).

	// If the type is correct
	if (acceptedTagTypes.find(requestJSON->get("type", "").asString()) == acceptedTagTypes.end())
		return;

	//LoggerInfo("[FIONA-logger] TicketConnectorSpark::processData> antes de enviar: %s", (*requestJSON).toStyledString().c_str());
	// In this case only forwarding is done.
	myFlowJSON->processData(requestJSON);

}


