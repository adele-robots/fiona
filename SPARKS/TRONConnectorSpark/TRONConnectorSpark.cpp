/**
 *
 * @author Abel Castro Suárez
 * @version 0.1 20/2/2014
 */

//#include "stdAfx.h"

#include <list>
#include <ctime>
#include "TRONConnectorSpark.h"
#include "jsoncpp/value.h"
#include "jsoncpp/reader.h"
#include "jsoncpp/writer.h"
#include "curlpp/cURLpp.hpp"
#include "curlpp/Easy.hpp"
#include "curlpp/Options.hpp"


#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
		)
{
	if (!strcmp(componentType, "TRONConnectorSpark")) {
			return new TRONConnectorSpark(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif

void TRONConnectorSpark::init(void) {
	connected = false;
}

void TRONConnectorSpark::quit(void) {}


void TRONConnectorSpark::processData(Json::Value* messageJSON)
{
	if(isConnected()){
		(*messageJSON)["response"] = "ERROR";
		string s(Json::FastWriter().write(*messageJSON).c_str());
		myFlowChar->processData(const_cast<char*>(s.c_str()));
		return;
	}

    /* Call getAPIData(*messageJSON) and send reply through myFlowChar->processData() */
	try {
		Json::Value reply = getAPIData(*messageJSON);
		LoggerInfo("%s",Json::StyledWriter().write(reply).c_str());
		string s(Json::FastWriter().write(reply));
		myFlowChar->processData(const_cast<char*>(s.c_str()));
	} catch(Exception & e) {
		LoggerError("[TRONConnectorSpark] Error making the request: %s", e.msg);
		Json::Value reply = (*messageJSON);
		reply["response"] = "ERROR";
		string s(Json::FastWriter().write(reply).c_str());
		myFlowChar->processData(const_cast<char*>(s.c_str()));
	}
}

Json::Value TRONConnectorSpark::getAPIData(Json::Value request)
{
	string reqAddres = getComponentConfiguration()->getString(const_cast<char*>("Address"));
	string usermail = getComponentConfiguration()->getString(const_cast<char*>("User_Mail"));
	string avatarId = getComponentConfiguration()->getString(const_cast<char*>("Avatar_Id"));
	string voice = getComponentConfiguration()->getString(const_cast<char*>("Voice"));
	string r5server = getComponentConfiguration()->getString(const_cast<char*>("Red5_Server"));
	string randomRoom = getGlobalConfiguration()->getString(const_cast<char*>("room"));
	std::string requestURI = reqAddres + "?action=checkTron&user=" + usermail + "&av=" + avatarId + "&voice=" + voice + "&stream=" + r5server + randomRoom;


		LoggerInfo("TRONConnectorSpark::getAPIData> Request URI: %s", requestURI.c_str());

		std::stringstream APIResponse;

		try {

			curlpp::Cleanup myCleanup;

			APIResponse << curlpp::options::Url(requestURI);
		}
		catch( curlpp::RuntimeError &e )
		{
			LoggerError("TRONConnectorSpark::getAPIData> Runtime Error: %s", e.what());
			request["response"] = "ERROR";
			return request;
		}
		catch( curlpp::LogicError &e )
		{
			LoggerError("TRONConnectorSpark::getAPIData> Logic Error: %s", e.what());
			request["response"] = "ERROR";
			return request;
		}

		request["response"] = APIResponse.str();

		if(APIResponse.str() != "0")
			connected = true;

		return request;
}

bool TRONConnectorSpark::isConnected() {
	return connected;
}
