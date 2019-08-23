/// @file NewsConnectorSpark.cpp
/// @brief Receives sentenes over the IFlow<char*> link (e.g. from Rebecca) and publish the contained command tags to all the subscribers linked by IFlow<Json::Value*>.

//#include "stdAfx.h"
#include <iostream>
#include <string>
#include <queue>
#include <unordered_map>
#include <cstdlib>
#include <ctime>
#include <cstring>
#include "NewsConnectorSpark.h"
#include "jsoncpp/value.h"
#include "jsoncpp/reader.h"
#include "jsoncpp/writer.h"

#ifdef _WIN32
#else
extern "C" Component *createComponent(char *componentInstanceName,
		char *componentType, ComponentSystem *componentSystem) {
	if (!strcmp(componentType, "NewsConnectorSpark")) {
		return new NewsConnectorSpark(componentInstanceName,
				componentSystem);
	} else {
		return NULL;
	}
}
#endif

bool running = true;

/// Initializes NewsConnectorSpark component.
void NewsConnectorSpark::init(void) {
}

/// Unitializes the NewsConnectorSpark component.
void NewsConnectorSpark::quit(void) {

	running = false;
}

void NewsConnectorSpark::processData(char* reply) {

	running = false;

	string receivedMessage(reply);

	Json::Value replyJSON;

	if (Json::Reader().parse(receivedMessage, replyJSON)) {

		LoggerInfo(
				"[FIONA-logger] NewsConnectorSpark::processData(char*)> Received Reply: %s",
				Json::StyledWriter().write(replyJSON).c_str());

		replyJSON["type"] = "news";

		string flowReply = Json::FastWriter().write(replyJSON);

		myFlowChar->processData(const_cast<char*>(flowReply.c_str()));

		LoggerInfo(
				"[FIONA-logger] NewsConnectorSpark::processData(char*)> CommandDispatcher reply message sent: %s",
				Json::StyledWriter().write(replyJSON).c_str());

	} else {
		LoggerInfo(
				"[FIONA-logger] NewsConnectorSpark::processData> Error, received reply format is incorrect: %s",
				reply);
	}

	running = true;
}

/**
 * Petition incoming from CommandDispatcherSpark must be processed and reformatted for Weather type information provider Sparks connected.
 */
void NewsConnectorSpark::processData(Json::Value* requestJSON) {
	if(requestJSON->get("type", "").asString() == "news"){
		LoggerInfo("NewsConnectorSpark Llega una peticion de news");
		// In this case only forwarding is done.
		myFlowJSON->processData(requestJSON);
	}

}
