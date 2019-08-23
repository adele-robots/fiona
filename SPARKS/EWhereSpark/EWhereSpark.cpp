/**
 *
 * @author Abel Castro Suárez
 * @version 0.1 20/2/2014
 */

//#include "stdAfx.h"

#include <list>
#include <ctime>
#include "EWhereSpark.h"
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
	if (!strcmp(componentType, "EWhereSpark")) {
			return new EWhereSpark(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif

void EWhereSpark::init(void)
{

	// Accepted parameters from .ini file
	acceptedParameters.push_back("via"); //location no es un parametro valido en la URI que envia la peticion a la API.
	acceptedParameters.push_back("portal");
	urlValidar = getComponentConfiguration()->getString("Url_Validar");
	urlCrear = getComponentConfiguration()->getString("Url_Crear");
}

void EWhereSpark::quit(void) {}

void EWhereSpark::processData(char* message) {}

void EWhereSpark::processData(Json::Value* messageJSON)
{

    /* Call getAPIData(*messageJSON) and send reply through myFlowChar->processData() */
	try {
		Json::Value reply = getAPIData(*messageJSON);
		LoggerInfo("%s",Json::StyledWriter().write(reply).c_str());
		string s(Json::FastWriter().write(reply));
		myFlowChar->processData(const_cast<char*>(s.c_str()));
	} catch(Exception & e) {
		LoggerError("[EWhereSpark] Error making the request: %s", e.msg);
		Json::Value reply = (*messageJSON);
		reply["response"] = "ERROR";
		string s(Json::FastWriter().write(reply).c_str());
		myFlowChar->processData(const_cast<char*>(s.c_str()));
	}
}

Json::Value EWhereSpark::getAPIData(Json::Value request)
{
	std::string requestURI;

	// Primero validamos la via

	std::string parameterValue = request.get("via", "").asString();
	for (size_t pos = parameterValue.find(' '); pos != string::npos; pos = parameterValue.find(' ', pos)) {
		parameterValue.replace(pos, 1, "%20");
	}

	if (parameterValue != "")
		requestURI = urlValidar + parameterValue;
	else
		throw Exception("No parameter \"via\" was found");

	LoggerInfo("EWhereSpark::getAPIData> Request URI: %s", requestURI.c_str());

	std::stringstream APIResponse;

	try {

		curlpp::Cleanup myCleanup;

		APIResponse << curlpp::options::Url(requestURI);

		if(APIResponse.str() == "-1") {
			//throw Exception("The via does not exist");
			request["response"] = "NOT_FOUND";
			return request;
		}
	}
	catch( curlpp::RuntimeError &e )
	{
		LoggerError("EWhereSpark::getAPIData> Runtime Error: %s", e.what());
		request["response"] = "ERROR";
		return request;
	}
	catch( curlpp::LogicError &e )
	{
		LoggerError("EWhereSpark::getAPIData> Logic Error: %s", e.what());
		request["response"] = "ERROR";
		return request;
	}

	// Despues creamos la solicitud

	parameterValue = request.get("portal", "").asString();

	if (parameterValue != ""){
		requestURI = urlCrear + APIResponse.str() + "/" + parameterValue;
	}
	else
		throw Exception("No parameter \"portal\" was found");

	LoggerInfo("EWhereSpark::getAPIData> Request URI: %s", requestURI.c_str());

	APIResponse.clear();

	try {

		curlpp::Cleanup myCleanup;
		APIResponse << curlpp::options::Url(requestURI);

		if(APIResponse.str() == "-1") {
			//throw Exception("The portal does not exist");
			request["response"] = "NOT_FOUND";
			return request;
		}
	}
	catch( curlpp::RuntimeError &e )
	{
		LoggerError("EWhereSpark::getAPIData> Runtime Error: %s", e.what());
		request["response"] = "ERROR";
		return request;
	}
	catch( curlpp::LogicError &e )
	{
		LoggerError("EWhereSpark::getAPIData> Logic Error: %s", e.what());
		request["response"] = "ERROR";
		return request;
	}

	request["response"] = APIResponse.str();

	return request;
}


