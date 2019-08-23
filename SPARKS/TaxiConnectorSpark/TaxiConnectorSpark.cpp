/// @file TaxiConnectorSpark.cpp
/// @brief Receives sentenes over the IFlow<char*> link (e.g. from Rebecca) and publish the contained command tags to all the subscribers linked by IFlow<Json::Value*>.

//#include "stdAfx.h"
#include <iostream>
#include <string>
#include <queue>
#include <unordered_map>
#include <cstdlib>
#include <ctime>
#include <cstring>
#include "TaxiConnectorSpark.h"
#include "jsoncpp/value.h"
#include "jsoncpp/reader.h"
#include "jsoncpp/writer.h"

#ifdef _WIN32
#else
extern "C" Component *createComponent(char *componentInstanceName,
		char *componentType, ComponentSystem *componentSystem) {
	if (!strcmp(componentType, "TaxiConnectorSpark")) {
		return new TaxiConnectorSpark(componentInstanceName,
				componentSystem);
	} else {
		return NULL;
	}
}
#endif

bool running = true;

/// Initializes TaxiConnectorSpark component.
void TaxiConnectorSpark::init(void) {
}

/// Unitializes the TaxiConnectorSpark component.
void TaxiConnectorSpark::quit(void) {

	running = false;
}

/**
 * Receive strings containing a JSON with format:
 *
 * { "type" : providerType,
 * 	 "values" : {
 * 	 	"city" : "Oviedo",
 * 	 	"temperatureC" : 25,
 * 	 	"temperatureF" : 77
 * 	 }
 * }
 */
void TaxiConnectorSpark::processData(char* reply) {

	running = false;

	string receivedMessage(reply);

	Json::Value replyJSON;

	if (Json::Reader().parse(receivedMessage, replyJSON)) {

		LoggerInfo(
				"[FIONA-logger] TaxiConnectorSpark::processData(char*)> Received Reply: %s",
				Json::StyledWriter().write(replyJSON).c_str());

		replyJSON["type"] = "taxi";

		string flowReply = Json::FastWriter().write(replyJSON);

		myFlowChar->processData(const_cast<char*>(flowReply.c_str()));

		LoggerInfo(
				"[FIONA-logger] TaxiConnectorSpark::processData(char*)> CommandDispatcher reply message sent: %s",
				Json::StyledWriter().write(replyJSON).c_str());

		//ToDo Processes WorldWeatherOnline API responsose (JSON, XML...) and enqueue it.
		//Tip: unordered_map<int, std::queue<Json::Value> > ???
		//Tip2: unordered_map<int, std::vector<Json::Value> > (looks better) ???

		//Random ID generation
//		srand(time(NULL));
//		rand();

		/**
		 * Si timeout != 0
		 */

		//replys.push_back(replyJSON);

//		std::string reply = "En " + replyJSON["data"].get("city", "").asString()
//				+ " hace " + replyJSON["data"].get("temperature", "").asString()
//				+ " grados.";

//				replys.erase(replys.begin());

	} else {
		LoggerInfo(
				"[FIONA-logger] TaxiConnectorSpark::processData> Error, received reply format is incorrect: %s",
				reply);
	}

	running = true;
}

/**
 * Petition incoming from CommandDispatcherSpark must be processed and reformatted for Weather type information provider Sparks connected.
 */
void TaxiConnectorSpark::processData(Json::Value* requestJSON) {

	// An id must be used to identify each request replys in processData(char*).

	// If the type is correct
	if (requestJSON->get("type", "").asString() != "taxi")
		return;

	// In this case only forwarding is done.
	myFlowJSON->processData(requestJSON);

	/**
	 * Iniciar timeout y
	 */
}

/**
 * Processes all received WorldWeatherOnline API responsoses for one petition, unenqueues it and sends response to Rebecca.
 */
void TaxiConnectorSpark::process() {

//	LoggerInfo("[FIONA-logger] TaxiConnectorSpark::process> Checkpoint");
//
//	while (running) {
//
//	for (Json::Value JSONReply : replys){
//
//		LoggerInfo("[FIONA-logger] TaxiConnectorSpark::process> Processing reply: %s", Json::StyledWriter().write(JSONReply).c_str());
//
//		std::string reply = "En " + JSONReply.get("city", "").asString() + " hace " + JSONReply.get("temperature", "").asString() + "grados.";
//
//		char *cstr = new char[reply.length() + 1];
//		strcpy(cstr, reply.c_str());
//
//		myFlowChar->processData(cstr);
//
//		LoggerInfo("[FIONA-logger] TaxiConnectorSpark::process> Chat mesasge sent: %s", cstr);
//
//		delete[] cstr;
//
//		replys.erase(replys.begin());
//	}
//	}
}

