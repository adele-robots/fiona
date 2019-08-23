/// @file WeatherSimpleConnectorSpark.cpp
/// @brief Receives sentenes over the IFlow<char*> link (e.g. from Rebecca) and publish the contained command tags to all the subscribers linked by IFlow<Json::Value*>.

//#include "stdAfx.h"
#include <iostream>
#include <string>
#include <queue>
#include <cstdlib>
#include <ctime>
#include <cstring>
#include "WeatherSimpleConnectorSpark.h"
#include "jsoncpp/value.h"
#include "jsoncpp/reader.h"
#include "jsoncpp/writer.h"

#ifdef _WIN32
#else
extern "C" Component *createComponent(char *componentInstanceName,
		char *componentType, ComponentSystem *componentSystem) {
	if (!strcmp(componentType, "WeatherSimpleConnectorSpark")) {
		return new WeatherSimpleConnectorSpark(componentInstanceName,
				componentSystem);
	} else {
		return NULL;
	}
}
#endif

/// Initializes WeatherSimpleConnectorSpark component.
void WeatherSimpleConnectorSpark::init(void) {
	acceptedTagTypes.insert("weather");
	//acceptedTagTypes.insert("WEATHER");
	parametersMap.clear();
}

/// Unitializes the WeatherSimpleConnectorSpark component.
void WeatherSimpleConnectorSpark::quit(void) {

}

/**
 * Receive strings containing a JSON with format:
 *
 * { "tempC" : 26,
 * 	 "tempC" : 77,
 * 	 "windDir": "WNW"
 * }
 */

void WeatherSimpleConnectorSpark::processData(char* reply) {

	string receivedMessage(reply);

	Json::Value receivedJSON;
	Json::Value replyJSON;

	if (Json::Reader().parse(receivedMessage, receivedJSON)) {

		LoggerInfo(
				"[FIONA-logger] WeatherSimpleConnectorSpark::processData(char*)> Received Reply: %s",
				Json::StyledWriter().write(receivedJSON).c_str());

		string id = receivedJSON.get("id", "N/A").asString();
		replyJSON["id"] = id;
		replyJSON["type"] = "weather";
		replyJSON["response"] = "";
		list<string> parametros = parametersMap[id];
		for(list<string>::iterator it = parametros.begin(); it != parametros.end(); it++) {
			LoggerInfo("%s -> %s", (*it).c_str(), receivedJSON["data"].get(*it, "").asString().c_str());
			string dato = receivedJSON["data"].get(*it, "").asString();
			if(dato == "ERROR" || dato == "TOOFAR") {
				replyJSON["response"] = dato;
				break;
			}
			replyJSON["response"] = replyJSON.get("response", "").asString() +
					(replyJSON.get("response", "").asString() == "" ? "" : " ") +
					((*it) == "location" ? "[location] " : "") +
					dato;
		}
		parametersMap.erase(id);

		myFlowChar->processData(const_cast<char*>(Json::StyledWriter().write(replyJSON).c_str()));

		/*LoggerInfo(
				"[FIONA-logger] WeatherSimpleConnectorSpark::processData(char*)> CommandDispatcher reply message sent: %s",
				Json::StyledWriter().write(replyJSON).c_str());*/

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
				"[FIONA-logger] WeatherSimpleConnectorSpark::processData> Error, received reply format is incorrect: %s",
				reply);
	}
}

/**
 * Petition incoming from CommandDispatcherSpark must be processed and reformatted for Weather type information provider Sparks connected.
 */
void WeatherSimpleConnectorSpark::processData(Json::Value* requestJSON) {

	// An id must be used to identify each request replys in processData(char*).

	// If the type is correct
	if (acceptedTagTypes.find(requestJSON->get("type", "").asString()) == acceptedTagTypes.end())
		return;

	string param = (*requestJSON).get("parameters", "").asString();
	list<string> lista;
	(*requestJSON)["parameters"] = Json::Value(Json::arrayValue);
	string::size_type pos = param.find(",");
	while(pos != string::npos) {
		Json::Value v;
		v["value"] = param.substr(0, pos);
		lista.push_back(param.substr(0, pos));
		(*requestJSON)["parameters"].append(v);
		param.erase(0, pos+1);
		pos = param.find(",");
	}
	Json::Value v;
	v["value"] = param;
	lista.push_back(param);
	(*requestJSON)["parameters"].append(v);

	parametersMap.insert(pair<string, list<string> >(requestJSON->get("id", "").asString(), lista));

	/*(*requestJSON)["parameters"] = Json::Value(Json::arrayValue);
	Json::Value v1;
	v1["value"] = "tempC";
	Json::Value v2;
	v2["value"] = "tempF";
	(*requestJSON)["parameters"].append(v1);
	(*requestJSON)["parameters"].append(v2);*/
	LoggerInfo("[FIONA-logger] WeatherSimpleConnectorSpark::processData> antes de enviar: %s", (*requestJSON).toStyledString().c_str());
	// In this case only forwarding is done.
	myFlowJSON->processData(requestJSON);

	/**
	 * Iniciar timeout y
	 */
}

/**
 * Processes all received WorldWeatherOnline API responsoses for one petition, unenqueues it and sends response to Rebecca.
 */
void WeatherSimpleConnectorSpark::process() {
	sleep(1);

//	LoggerInfo("[FIONA-logger] WeatherSimpleConnectorSpark::process> Checkpoint");
//
//	while (running) {
//
//	for (Json::Value JSONReply : replys){
//
//		LoggerInfo("[FIONA-logger] WeatherSimpleConnectorSpark::process> Processing reply: %s", Json::StyledWriter().write(JSONReply).c_str());
//
//		std::string reply = "En " + JSONReply.get("city", "").asString() + " hace " + JSONReply.get("temperature", "").asString() + "grados.";
//
//		char *cstr = new char[reply.length() + 1];
//		strcpy(cstr, reply.c_str());
//
//		myFlowChar->processData(cstr);
//
//		LoggerInfo("[FIONA-logger] WeatherSimpleConnectorSpark::process> Chat mesasge sent: %s", cstr);
//
//		delete[] cstr;
//
//		replys.erase(replys.begin());
//	}
//	}
}

