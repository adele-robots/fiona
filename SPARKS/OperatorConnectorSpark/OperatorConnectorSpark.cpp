/// @file OperatorConnectorSpark.cpp
/// @brief Receives sentenes over the IFlow<char*> link (e.g. from Rebecca) and publish the contained command tags to all the subscribers linked by IFlow<Json::Value*>.

//#include "stdAfx.h"
#include <iostream>
#include <string>
#include <queue>
#include <unordered_map>
#include <cstdlib>
#include <ctime>
#include <cstring>
#include "OperatorConnectorSpark.h"
#include "jsoncpp/value.h"
#include "jsoncpp/reader.h"
#include "jsoncpp/writer.h"

#ifdef _WIN32
#else
extern "C" Component *createComponent(char *componentInstanceName,
		char *componentType, ComponentSystem *componentSystem) {
	if (!strcmp(componentType, "OperatorConnectorSpark")) {
		return new OperatorConnectorSpark(componentInstanceName,
				componentSystem);
	} else {
		return NULL;
	}
}
#endif

/// Initializes OperatorConnectorSpark component.
void OperatorConnectorSpark::init(void) {
}

/// Unitializes the OperatorConnectorSpark component.
void OperatorConnectorSpark::quit(void) {

}

void OperatorConnectorSpark::processData(char* reply) {

	string receivedMessage(reply);

	Json::Value replyJSON;

	if (Json::Reader().parse(receivedMessage, replyJSON)) {

		LoggerInfo(
				"[FIONA-logger] OperatorConnectorSpark::processData(char*)> Received Reply: %s",
				Json::StyledWriter().write(replyJSON).c_str());

		replyJSON["type"] = "agente";

		string flowReply = Json::FastWriter().write(replyJSON);

		myFlowChar->processData(const_cast<char*>(flowReply.c_str()));

		LoggerInfo(
				"[FIONA-logger] OperatorConnectorSpark::processData(char*)> CommandDispatcher reply message sent: %s",
				Json::StyledWriter().write(replyJSON).c_str());

	} else {
		LoggerInfo(
				"[FIONA-logger] OperatorConnectorSpark::processData> Error, received reply format is incorrect: %s",
				reply);
	}

}

/**
 * Petition incoming from CommandDispatcherSpark must be processed and reformatted for Weather type information provider Sparks connected.
 */
void OperatorConnectorSpark::processData(Json::Value* requestJSON) {

	// An id must be used to identify each request replys in processData(char*).
	if((*requestJSON)["type"]!="agente")
		return;

	// In this case only forwarding is done.
	myFlowJSON->processData(requestJSON);

}
