/*
 * ZendeskSpark.cpp
 *
 *  Created on: 12/02/2015
 *      Author: Adrián
 */

/// @file ZendeskSpark.cpp
/// @brief ZendeskSpark class implementation.


#include "ZendeskSpark.h"

#include <list>

#include <curlpp/cURLpp.hpp>
#include <curlpp/Easy.hpp>
#include <curlpp/Options.hpp>
#include <curlpp/Exception.hpp>

#include "base64.h"
#include <boost/regex.hpp>

#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "ZendeskSpark")) {
			return new ZendeskSpark(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif

/// Initializes ZendeskSpark component.
void ZendeskSpark::init(void){
	domain = getComponentConfiguration()->getString(const_cast<char*>("domain"));
	agent_username = getComponentConfiguration()->getString(const_cast<char*>("agent_username"));
	//agent_password = getComponentConfiguration()->getString(const_cast<char*>("agent_password"));
	api_token = getComponentConfiguration()->getString(const_cast<char*>("api_token"));
}

/// Unitializes the ZendeskSpark component.
void ZendeskSpark::quit(void){
}


/************************Validation functions***********************/
bool isCharacter(const char Character){
	return ( (Character >= 'a' && Character <= 'z') || (Character >= 'A' && Character <= 'Z'));
	//Checks if a Character is a Valid A-Z, a-z Character, based on the ascii value
}


bool isNumber(const char Character){
	return ( Character >= '0' && Character <= '9');
	//Checks if a Character is a Valid 0-9 Number, based on the ascii value
}


bool isValidEmailAddress(const char * EmailAddress){
	if (!strcmp(EmailAddress, "no.email@found.es")) //From SpeechFilterSpark
		return 0;
	if(!EmailAddress) // If cannot read the Email Address...
		return 0;
	if(!isCharacter(EmailAddress[0])) // If the First character is not A-Z, a-z
		return 0;
	int AtOffset = -1;
	int DotOffset = -1;
	unsigned int Length = strlen(EmailAddress); // Length = StringLength (strlen) of EmailAddress
	for(unsigned int i = 0; i < Length; i++){
		if(EmailAddress[i] == '@') // If one of the characters is @, store it's position in AtOffset
			AtOffset = (int)i;
		else if(EmailAddress[i] == '.') // Same, but with the dot
			DotOffset = (int)i;
	}
	if(AtOffset == -1 || DotOffset == -1) // If cannot find a Dot or a @
		return 0;
	if(AtOffset > DotOffset) // If the @ is after the Dot
		return 0;
	return !(DotOffset >= ((int)Length-1)); //Chech there is some other letters after the Dot
}

bool isValidTicketId(std::string& id){
	std::string::const_iterator it = id.begin();
	while (it != id.end() && std::isdigit(*it)) ++it;
	return !id.empty() && it == id.end();

}

/************************End of validation functions***********************/

Json::Value createJSONResponse (std::string type, std::string id, std::string message){
	Json::Value json_output(Json::objectValue);
	json_output["type"] = type;
	json_output["id"] = id;
	json_output["response"] = message;
	return json_output;

}

bool matchRegExp(const std::string& input, boost::regex e){
   // Return false for partial match, true for full match, or throw for
   // impossible match based on what we have so far...
   boost::match_results<std::string::const_iterator> what;
   if(0 == boost::regex_match(input, what, e, boost::match_default | boost::match_partial)){
      // the input so far could not possibly be valid so reject it:
	   //Changed to return false
      return false;
   }
   // OK so far so good, but have we finished?
   if(what[0].matched){
      // excellent, we have a result:
      return true;
   }
   // what we have so far is only a partial match...
   return false;
}


//IFlow<Json::Value *> implementation
void ZendeskSpark::processData(Json::Value * prompt) {
	validId = true;
	validEmail = true;

	/****************************Request data******************************/
	//Autentication construction
	/*
	//User and pass
	std::string auth_string_noencoded = agent_username + ":" + agent_password;
	std::string auth_string = base64_encode(reinterpret_cast<const unsigned char*>(
			auth_string_noencoded.c_str()), auth_string_noencoded.length());
	*/

	//User and token
	std::string token_string_noencoded = agent_username + "/token:" + api_token;
	std::string token_string = base64_encode(reinterpret_cast<const unsigned char*>(
				token_string_noencoded.c_str()), token_string_noencoded.length());

	/**********************End of request data*****************************/

	//JSON type
	std::string action = prompt->get("action", "").asString();
	LoggerInfo("[FIONA-logger]ZendeskSpark::processData | Type: %s\n", action.c_str());

	/**********************Common cUrl options*****************************/
	curlpp::Cleanup cleaner;
	curlpp::Easy request;

	//NO ACTIVAR EN PRODUCCION -> PETA!!!!!
	//request.setOpt(new curlpp::options::Verbose(true));

	//Headers config
	std::list<std::string> header;
	header.push_back("Connection: keep-alive");
	header.push_back("Content-Type: application/json");
	header.push_back("Accept: */*");
	header.push_back("Authorization: Basic " + token_string);
	header.push_back("");
	request.setOpt(new curlpp::options::HttpHeader(header));

	//Response config
	std::stringstream curlresponse;
	request.setOpt(new curlpp::options::WriteStream(&curlresponse));
	/*******************End of common cUrl options**************************/

	/*******************REQUEST CONFIGURARION**************************/
	if (!action.compare("create_ticket")){
		//Url parameters
		std::string app = "https://" + domain + ".zendesk.com/";
		std::string api = "api/v2/";
		std::string api_resource_tickets = "tickets.json";

		//Url construction
		std::string url = app + api + api_resource_tickets;

		/******************************User data*******************************/
		//TicketConnector JSON parse
		std::string requester_email = prompt->get("email", "").asString();

		//Base 64 fill (string.lenght multiple of 4, filled with "=")
		std::string requester_email_filled = requester_email;
		while(requester_email_filled.length() % 4 != 0)
			requester_email_filled = requester_email_filled + "=";

		//If is_base64(email)
		boost::regex emailRe("^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$");
		if(matchRegExp(requester_email_filled, emailRe))
			requester_email = base64_decode(requester_email_filled);

		//User data
		std::string requester_name = prompt->get("name", "").asString();
		std::string requester_subject = prompt->get("subject", "").asString();
		std::string requester_description = prompt->get("description", "").asString();

		/****************************End of user data******************************/

		/****************************JSON Request data*****************************/
		//JSON construction with Jsoncpp
		Json::Value json_request(Json::objectValue);

		Json::Value ticket(Json::objectValue);
		ticket["subject"] = requester_subject;
		ticket["description"] = requester_description;

		Json::Value requester(Json::objectValue);
		requester["name"] = requester_name;
		requester["email"] = requester_email;
		ticket["requester"] = requester;

		json_request["ticket"]= ticket;

		//JSON to string
		Json::FastWriter writter;
		std::string json_request_string = writter.write(json_request);
		/*************************End of JSON Request data*************************/

		//Validate data
		validEmail = isValidEmailAddress(requester_email.c_str());

		/*************************Specific cUrl options*************************/
		//Set the url
		request.setOpt(new curlpp::options::Url(url));

		//POST parameters config
		request.setOpt(new curlpp::options::PostFields(json_request_string));
		request.setOpt(new curlpp::options::PostFieldSize(json_request_string.length()));


	} else if (!action.compare("ticket_status")){
		//TicketConnector JSON parse
		std::string ticket_id = prompt->get("ticket_id", "").asString();
		validId = isValidTicketId(ticket_id);

		//Url parameters
		std::string app = "https://" + domain + ".zendesk.com/";
		std::string api = "api/v2/";
		std::string api_resource_tickets = "tickets/" + ticket_id + ".json";

		//Url construction
		std::string url = app + api + api_resource_tickets;

		LoggerInfo("[FIONA-logger]ZendeskSpark::processData | Url: %s\n", url.c_str() );

		//Set the url
		request.setOpt(new curlpp::options::Url(url));

	}
	/****************END OF REQUEST CONFIGURARION***********************/

	//Validate user email
	if (!validEmail){
		Json::FastWriter writter;
		myFlow->processData(const_cast<char *>(writter
				.write(createJSONResponse("ticket", prompt->get("id", "").asString()
						, "Invalid email address")).c_str()));
	} else if (!validId){
		Json::FastWriter writter;
		myFlow->processData(const_cast<char *>(writter
				.write(createJSONResponse("ticket", prompt->get("id", "").asString()
						, "Invalid id")).c_str()));
	} else {
		//Connection
		try {
			//Call

			request.perform();

			/*******************RESPONSE PARSER**************************/
			//JSONcpp response parser
			Json::Value json_response;
			Json::Reader reader;
			reader.parse(curlresponse, json_response, false);

			/***************Ticket creation response**********************/
			if (!action.compare("create_ticket")){
				//Ticket handler
				//If there is a ticket
				if(!json_response["ticket"].isNull()){
					//Get the ticket
					const Json::Value& ticket_response = json_response["ticket"];

					//Get the id and convert it to string
					int ticket_id_int = ticket_response["id"].asInt();
					std::stringstream ss;
					ss << ticket_id_int;
					std::string ticket_id_string;
					ss >> ticket_id_string;

					//Send info to the avatar
					Json::FastWriter writter;
					myFlow->processData(const_cast<char *>(writter
							.write(createJSONResponse("ticket", prompt->get("id", "").asString()
									, "id " + ticket_id_string)).c_str()));

				} else {
					//If there isn't a ticket, error message
					Json::FastWriter writter;
					myFlow->processData(const_cast<char *>(writter
							.write(createJSONResponse("ticket", prompt->get("id", "").asString()
									, "No ticket")).c_str()));
				}
			}
			/**************End of ticket creation response******************/
			/*****************Ticket status response************************/
			else if (!action.compare("ticket_status")){
				//Ticket handler
				//If there is a ticket
				if(!json_response["ticket"].isNull()){
					//Get the ticket
					const Json::Value& ticket_response = json_response["ticket"];

					//Get the ticket status
					std::string ticket_status = ticket_response["status"].asString();

					//Send info to the avatar
					Json::FastWriter writter;
					myFlow->processData(const_cast<char *>(writter
							.write(createJSONResponse("ticket", prompt->get("id", "").asString()
									, "status " + ticket_status)).c_str()));

				} else {
					//If there isn't a ticket or an error, error message
					Json::FastWriter writter;
					myFlow->processData(const_cast<char *>(writter
							.write(createJSONResponse("ticket", prompt->get("id", "").asString()
									, "Wrong id")).c_str()));
				}
			}
			/***************End of ticket status response******************/
			/*****************END OF RESPONSE PARSER***********************/

		}
		//Logic error
		catch ( curlpp::LogicError & e ) {
			std::cout << e.what() << std::endl;
			LoggerError("[FIONA-logger]ZendeskSpark::processData | LogicError: %s\n", e.what() );

			Json::FastWriter writter;
			myFlow->processData(const_cast<char *>(writter
					.write(createJSONResponse("ticket", prompt->get("id", "").asString()
							, "Request error")).c_str()));


		}
		//Runtime error
		catch ( curlpp::RuntimeError & e ) {
			std::cout << e.what() << std::endl;
			LoggerError("[FIONA-logger]ZendeskSpark::processData | RuntimeError: %s\n", e.what() );

			Json::FastWriter writter;
			myFlow->processData(const_cast<char *>(writter
					.write(createJSONResponse("ticket", prompt->get("id", "").asString()
							, "Request error")).c_str()));
		}
	}
}



