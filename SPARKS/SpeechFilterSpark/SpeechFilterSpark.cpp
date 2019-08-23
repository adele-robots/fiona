/*
 * SpeechFilterSpark.cpp
 *
 *  Created on: 03/10/2013
 *      Author: guille
 */

/// @file SpeechFilterSpark.cpp
/// @brief SpeechFilterSpark class implementation.


//#include "stdAfx.h"
#include "SpeechFilterSpark.h"
#include "EmailSenderSpark.h"

#include <fstream>

#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "SpeechFilterSpark")) {
			return new SpeechFilterSpark(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif



/// Initializes SpeechFilterSpark component.
void SpeechFilterSpark::init(void){
	// Load the Spark sources that can call this spark
	int numSources = getComponentConfiguration()->getLength(const_cast<char *>("Spark_Sources"));

	for (int i = 0; i < numSources; i++) {
		char sourceIndex[80];
		snprintf(sourceIndex, 80, "Spark_Sources.[%d]", i);
		sparkSources.push_back(getComponentConfiguration()->getString(sourceIndex));
	}
}

/// Unitializes the SpeechFilterSpark component.
void SpeechFilterSpark::quit(void){
}

//IFlow<char*> implementation
void SpeechFilterSpark::processData(char * prompt) {
	// Check source depending on stacktrace
	stacktrace::call_stack st;

	int sourceIndex = -1;
	vector<int> stackSourcePosition;

	for(vector<string>::iterator it = sparkSources.begin(); it != sparkSources.end(); ++it) {
		size_t found = st.to_string().find(*it);
		if (found!=std::string::npos){
			// Keep the position
			stackSourcePosition.push_back(found);
		}else{
			// Insert the largest number 'int' can hold to avoid picking this position as the minimum value
			stackSourcePosition.push_back(numeric_limits<int>::max());
		}
	}
	// Find closest source
	sourceIndex = distance(stackSourcePosition.begin(),min_element(stackSourcePosition.begin(),stackSourcePosition.end()));
	// Depending on Spark source, implement different behavior. Right only 2 sources are provided.
	switch(sourceIndex){
		case 0:
			sourceImplementation1(prompt);
			break;
		case 1:
			sourceImplementation2(prompt);
			break;
		default:
			LoggerWarn("[FIONA-logger]No Spark source matching !!");
			break;
	}
}

void SpeechFilterSpark::sourceImplementation1(char * prompt) {
	// RIGHT NOW SOURCE WILL BE "CHAT"
	// Save text, right now user input
	userInput = prompt;
}

void SpeechFilterSpark::sourceImplementation2(char * prompt) {
	// RIGHT NOW SOURCE WILL BE "AIML"
	// Save text, right now AIML input
	aimlInput = prompt;

	// Analyze input
	size_t tagPosition;
	if ((tagPosition =  aimlInput.find("<service>")) !=std::string::npos){
		string xmlText = xmlExtract(aimlInput, "xml");
		// Start the XML-parsing process
		pugi::xml_document doc;
		// Remove the output text (keep only the 'xml' structure) and check if it loads without errors
		if(doc.load(xmlText.c_str())){
			pugi::xml_node service = doc.child("service");
			// check if the <service> node exists
			if(service){
				int i = 0;
				// Loop through all the requests
				for (pugi::xml_node request = service.child("request"); request; request = request.next_sibling("request"))
				{
					i++;
					string requestStr;
#ifdef SPARK_DEBUG
					LoggerInfo("[FIONA-logger] Request #%d",i);
#endif
					// 'host' attribute is mandatory
					string host(request.attribute("host").value());
					if(!host.empty())
						requestStr += "host=" + host;
					else{
						LoggerError("[FIONA-logger] Request must declare a 'host'");
						break;
					}

					string url(request.attribute("url").value());
					string port(request.attribute("port").value());
					string method(request.attribute("method").value());

					int j=0;
					string body("\"");
					// Loop through all the data params to build the request body
					for (pugi::xml_node param = request.child("param"); param; param = param.next_sibling("param")){
						j++;
#ifdef SPARK_DEBUG
						LoggerInfo("[FIONA-logger] Param #%d -> %s ",j, param.attribute("name").value());
#endif
						string paramName = param.attribute("name").value();
						body.append(param.attribute("name").value());
						body.append("=");
						// Get the param value from the user details map
						MapIterator pos = userDetails.find(paramName);
						if (pos == userDetails.end()) {
						    //Handle the error
							LoggerWarn("[FIONA-logger] Can't find any data stored by 'key' : %s",paramName.c_str());
						} else {
						    string value = pos->second;
						    body.append(value);
						}
						body.append("&");
					}
					// Remove last '&' and append "
					body = body.substr(0, body.size()-1);
					body.append("\"");

					//Build request
					requestStr.append("|url=" + url);
					requestStr.append("|port=" + port);
					requestStr.append("|method=" + method);
					requestStr.append("|body=" + body);
					// Append the necessary header to a POST request
					if(method == "POST"){
						requestStr.append("|headers=\"Content-type,application/x-www-form-urlencoded\"");
					}
#ifdef SPARK_DEBUG
					LoggerInfo("[FIONA-logger] REQUEST #%d -> %s",i,requestStr.c_str());
#endif
					string requestEncoded = base64_encode(reinterpret_cast<const unsigned char*>(requestStr.c_str()), requestStr.length());
					string requestPattern("[REQUEST] ");
					requestPattern.append(requestEncoded);
					myFlow->processData(const_cast<char *>(requestPattern.c_str()));
				}
			}else{
				// Handle error
				LoggerWarn("[FIONA-logger] XML-structured text loaded without errors, but <service> node is not available");
			}
		}else{
			//  Handle error
			LoggerWarn("[FIONA-logger] XML-structured text loaded with errors");
		}
	}else if((tagPosition =  aimlInput.find("<param")) !=std::string::npos){
		// AIML response is telling that previous user input value has to be saved as a new parameter
		//Extract XML-text
		string xmlText = xmlExtract(aimlInput, "xml");
		// Start the XML-parsing process
		pugi::xml_document doc;
		// Remove the output text (keep only the 'xml' structure) and check if it loads without errors
		if(doc.load(xmlText.c_str())){
			pugi::xml_node param = doc.child("param");
			// check if the <param> node exists
			if(param){
				string paramStr(param.attribute("name").value());
				// Save the parameter(with name indicated in AIML) with "value" attribute value
				if(!param.attribute("value").empty())
					userDetails.insert(pair<string,string>(paramStr,param.attribute("value").value()));
				// Save the parameter(with name indicated in AIML) with previous user input value
				else {
					// Parse specific parameters
					if(paramStr == "email"){
						boost::regex emailRe("[a-zA-z0-9._]*+[@]+[a-zA-Z0-9-]*+[.]+[a-zA-Z0-9]*");

						boost::sregex_token_iterator iter(userInput.begin(), userInput.end(), emailRe, 0);
						boost::sregex_token_iterator end;
						string email("no.email@found.es");
						// If find more than one match, keep the last
						for( ; iter != end; ++iter ) {
							email = *iter;
						}
						//Insert parameter in the user details map
						userDetails.insert(pair<string,string>(paramStr,email));
						// base64 encode email parameter
						string emailEncoded = base64_encode(reinterpret_cast<const unsigned char*>(email.c_str()), email.length());
						LoggerInfo("[FIONA-logger] SPARK SpeechFilter |sourceImplementation 2 EL email encoded ES: %s",emailEncoded.c_str());
						string emailPattern("[EMAIL] ");
						emailPattern.append(emailEncoded);
						LoggerInfo("[FIONA-logger] SPARK SpeechFilter |sourceImplementation 2 EL email que mando al AIML ES: %s",emailPattern.c_str());
						// Send email encoded to AIML
						myFlow->processData(const_cast<char *>(emailPattern.c_str()));
					}else{
						//Insert parameter in the user details map
						userDetails.insert(pair<string,string>(paramStr,userInput));
					}
				}
			}
#ifdef SPARK_DEBUG
			LoggerInfo("[FIONA-logger] sourceImplementation 2 EL MAP ESTÁ QUEDANDO: ");
			for( MapIterator it = userDetails.begin(); it != userDetails.end(); ++it )
			{
				string key = it->first;
				string value = it->second;
				LoggerInfo("[FIONA-logger] sourceImplementation 2  | key: %s , value: %s",key.c_str(),value.c_str());
			}
#endif
		}else{
			// Handle errors
			LoggerWarn("[FIONA-logger] XML-structured text loaded with errors");
		}
	}
	else if ((tagPosition =  aimlInput.find("<sendEmail>")) !=std::string::npos){
		string xmlText = xmlExtract(aimlInput, "xml");
		// Start the XML-parsing process
		pugi::xml_document doc;
		// Remove the output text (keep only the 'xml' structure) and check if it loads without errors
		if(doc.load(xmlText.c_str())){
			pugi::xml_node sendEmail = doc.child("sendEmail");
			// check if the <sendEmail> node exists
			if(sendEmail){
				int i = 0;
				// Loop through all the mails
				for (pugi::xml_node mail = sendEmail.child("mail"); mail; mail = mail.next_sibling("mail"))
				{
					i++;
#ifdef SPARK_DEBUG
					LoggerInfo("[FIONA-logger] Request #%d",i);
#endif

					string from(mail.attribute("from").value());
					string to(mail.attribute("to").value());
					string subject(mail.attribute("subject").value());

					int j=0;
					vector<pair<string, string> > headers;
					// Loop through all the data headers to build the request body
					for (pugi::xml_node header = mail.child("header"); header; header = header.next_sibling("header")){
						j++;
#ifdef SPARK_DEBUG
						LoggerInfo("[FIONA-logger] Header #%d -> %s ",j, header.attribute("name").value());
#endif
						string headerName = header.attribute("name").value();
						string headerValue = header.attribute("value").value();
						headers.push_back(pair<string, string>(headerName, headerValue));
					}

					//string body = mail.child("body").child_value();
					//string body = xmlExtract(aimlInput, "cuerpo");
					string body = mail.child("cuerpo").attribute("value").value();
					//string attachment(mail.attribute("attachment").value());

					int k=0;
					vector<string> attachments;
					// Loop through all the attachments to build the request body
					for (pugi::xml_node attachment = mail.child("attachment"); attachment; attachment = attachment.next_sibling("attachment")){
						k++;
#ifdef SPARK_DEBUG
						LoggerInfo("[FIONA-logger] Attachment #%d -> %s ",k, attachment.attribute("value").value());
#endif
						string value = attachment.attribute("value").value();
						attachments.push_back(value);
					}


					//Build request
					string requestStr(EmailSenderSpark::buildRequest(from, to, subject, headers, body, attachments));
#ifdef SPARK_DEBUG
					LoggerInfo("[FIONA-logger] REQUEST #%d -> %s",i,requestStr.c_str());
#endif
					string requestPattern("[SENDEMAIL] ");
					requestPattern.append(requestStr);
					myFlow->processData(const_cast<char *>(requestPattern.c_str()));
				}
			}else{
				// Handle error
				LoggerWarn("[FIONA-logger] XML-structured text loaded without errors, but <sendEmail> node is not available");
			}
		}else{
			//  Handle error
			LoggerWarn("[FIONA-logger] XML-structured text loaded with errors");
			LoggerWarn("%s", xmlText.c_str());
		}
	}
}


/* Auxiliary functions */
// Extract <xml>TEXT WITH XML-TAGS</xml>
string SpeechFilterSpark::xmlExtract(string input, string tag){
	string output("");

	size_t foundStart = input.find("<"+tag+">");
	size_t foundEnd = input.find("</"+tag+">");
	if (foundStart!=std::string::npos && foundEnd!=std::string::npos){
		//Extract <xml>XML-TEXT</xml>
		output = input.substr(foundStart+tag.size()+2,foundEnd-(foundStart+tag.size()+2));
	}
	return output;
}

// Tokenizing a string
vector<string> SpeechFilterSpark::tokenizer( const string& p_pcstStr, char delim )  {
	std::vector<std::string> tokens;
	std::stringstream   mySstream( p_pcstStr );
	std::string         temp;

	while( getline( mySstream, temp, delim ) ) {
		tokens.push_back( temp );
	}

	return tokens;
}





