/*
 * PandorabotsSpark.cpp
 *
 *  Created on: 07/06/2013
 *      Author: Alejandro
 */

/// @file PandorabotsSpark.cpp
/// @brief PandorabotsSpark class implementation.


#include "PandorabotsSpark.h"

#include <list>

#include "rapidjson/document.h"

#include <cstdio>

#include <curlpp/cURLpp.hpp>
#include <curlpp/Easy.hpp>
#include <curlpp/Options.hpp>
#include <curlpp/Exception.hpp>


#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "PandorabotsSpark")) {
			return new PandorabotsSpark(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif

/// Initializes PandorabotsSpark component.
void PandorabotsSpark::init(void){
	botid = getComponentConfiguration()->getString(const_cast<char*>("Bot_Id"));
	userkey = getComponentConfiguration()->getString(const_cast<char*>("User_key"));
	firstTime = true;
	count = 0;
	response = NULL;
	sessionid = 0;
}

/// Unitializes the PandorabotsSpark component.
void PandorabotsSpark::quit(void){
}


//IFlow<char*> implementation
void PandorabotsSpark::processData(char * prompt) {

	//Url parameters
	string domain = "https://playground.pandorabots.com";
	string api_resource = "talk";
	string querystring = "?user_key=" + userkey;

	//Url construction
	string url = domain + "/" + api_resource + "/" +
			botid +  querystring;

	//POST parameters construction
	string postfields = "&input=" + string(prompt);

	//Add session
	if(!firstTime){
		std::ostringstream oss;
		oss << sessionid;
		postfields = postfields + "&sessionid=" + oss.str();
	}

	//Connection
	try {
		curlpp::Cleanup cleaner;
		curlpp::Easy request;

		//Sets the url
		request.setOpt(new curlpp::options::Url(url));
		//request.setOpt(new curlpp::options::Verbose(true));

		//Headers config
		std::list<std::string> header;
		header.push_back("Connection: keep-alive");
		header.push_back("Content-Type: application/x-www-form-urlencoded");
		header.push_back("Accept: */*");
		request.setOpt(new curlpp::options::HttpHeader(header));

		//POST parameters config
		request.setOpt(new curlpp::options::PostFields(postfields));
		request.setOpt(new curlpp::options::PostFieldSize(postfields.length()));

		//Response config
		std::stringstream curlresponse;
		request.setOpt(new curlpp::options::WriteStream(&curlresponse));

		//Call
		request.perform();

		//JSON response parser
		const std::string& tmp = curlresponse.str();
		const char* cresponse = tmp.c_str();

		rapidjson::Document json_response;
		json_response.Parse<0>(cresponse);

		//If response ok
		string json_status = json_response["status"].GetString();
		if(!json_status.compare("ok")){
			firstTime = false;

			//DEBUG ONLY: print session id
			/*
			sessionid = json_response["sessionid"].GetInt();
			std::stringstream str;
			str << sessionid;
			myFlow->processData(const_cast<char *> (str.str().c_str()));
			*/

			//Catch bot responses from the response
			const rapidjson::Value& responses = json_response["responses"];
			for (rapidjson::Value::ConstValueIterator itr = responses.Begin(); itr != responses.End(); ++itr)
				myFlow->processData(const_cast<char *>(itr->GetString()));

		}
		//If response is not ok
		else {
			string message = json_response["message"].GetString();
			myFlow->processData(const_cast<char *>(message.c_str()));
		}
	}

	catch ( curlpp::LogicError & e ) {
		std::cout << e.what() << std::endl;
		LoggerError("[FIONA-logger]PandorabotsSpark::processData | LogicError: %s\n", e.what() );
	}
	catch ( curlpp::RuntimeError & e ) {
		std::cout << e.what() << std::endl;
		LoggerError("[FIONA-logger]PandorabotsSpark::processData | RuntimeError: %s\n", e.what() );
	}
}

