/**
 *
 * @author Abel Castro Suárez
 * @version 0.1 20/04/2015
 */

#include <iostream>
#include <fstream>
#include <string>
#include "SalesforceSpark.h"
#include "base64.h"
#include <sstream>
#include <algorithm>
#include <locale>
#include <unordered_set>

#ifdef _WIN32
#else
extern "C" Component *createComponent(char *componentInstanceName,
		char *componentType, ComponentSystem *componentSystem) {
	if (!strcmp(componentType, "SalesforceSpark")) {
		return new SalesforceSpark(componentInstanceName,
				componentSystem);
	} else {
		return NULL;
	}
}
#endif

void SalesforceSpark::init(void)
{
}

void SalesforceSpark::quit(void)
{
}

void SalesforceSpark::processData(Json::Value* messageJSON)
{
	std::string actionValue = std::string(messageJSON->get("action", "").asString());

	if (actionValue == "create_ticket")
	{
		bool debug = messageJSON->get("debug", false).asBool();
		std::string debugEmail = messageJSON->get("debugEmail", "").asString();
		std::string orgId = messageJSON->get("orgId", "").asString();
		std::string name = messageJSON->get("name", "").asString();
		std::string email = messageJSON->get("email", "").asString();
		std::string phone = messageJSON->get("phone", "").asString();
		std::string subject = messageJSON->get("subject", "").asString();
		std::string description = messageJSON->get("description", "").asString();
		std::string company = messageJSON->get("company", "").asString();

		submitData(orgId, name, email, phone, subject, description, company, debug, debugEmail);
	}
}

void submitData(std::string orgId, std::string name, std::string email, std::string phone, std::string subject,
		std::string description, std::string company, bool debug, std::string debugEmail)
{
	try
	{
		curlpp::Cleanup clean;
		curlpp::Easy request;

		request.setOpt<curlpp::options::Url>("https://www.salesforce.com/servlet/servlet.WebToCase?encoding=UTF-8");

		std::list<std::string> header;
		header.push_back("Content-Type: application/x-www-form-urlencoded");
		request.setOpt<curlpp::options::HttpHeader>(header);

		std::string formFields = std::string("debug") + "=" + (debug ? "1" : "0");
		formFields += std::string("&") + "debugEmail" + "=" + debugEmail;
		formFields += std::string("&") + "orgid" + "=" + orgId;
		formFields += std::string("&") + "name" + "=" + name;
		formFields += std::string("&") + "email" + "=" + email;
		formFields += std::string("&") + "phone" + "=" + phone;
		formFields += std::string("&") + "subject" + "=" + subject;
		formFields += std::string("&") + "description" + "=" + description;
		formFields += std::string("&") + "company" + "=" + company;

		request.setOpt<curlpp::options::PostFields>(formFields);
		request.setOpt<curlpp::options::PostFieldSize>(formFields.length());
		request.perform();
	}
	catch(curlpp::LogicError& e )
	{
		LoggerError("Error making request 5a : %s", e.what());
	}
	catch(curlpp::RuntimeError& e)
	{
		LoggerError("Error making request 5b", e.what());
	}
}
