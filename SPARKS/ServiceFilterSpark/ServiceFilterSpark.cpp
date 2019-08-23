/*
 * ServiceFilterSpark.cpp
 *
 *  Created on: 30/09/2013
 *      Author: guille
 */

/// @file ServiceFilterSpark.cpp
/// @brief ServiceFilterSpark class implementation.


//#include "stdAfx.h"
#include "ServiceFilterSpark.h"

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
	if (!strcmp(componentType, "ServiceFilterSpark")) {
			return new ServiceFilterSpark(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif



/// Initializes ServiceFilterSpark component.
void ServiceFilterSpark::init(void){
	//getComponentConfiguration()->getString(const_cast<char*>("Bot_Id"));
}

/// Unitializes the ServiceFilterSpark component.
void ServiceFilterSpark::quit(void){
}

//IFlow<char*> implementation
void ServiceFilterSpark::processData(char * prompt) {
	#ifdef SPARK_DEBUG
		stacktrace::call_stack st;
		LoggerInfo("[FIONA-logger]ServiceFilterSpark::processData | STACKTRACE\n%s",st.to_string().c_str());
	#endif

	// Crea un fichero de salida
	ofstream fs("/home/guille/Escritorio/entrada_ServiceFilterSpark.txt");
	fs << prompt << endl;
	fs.close();

	string textStr(prompt);
	// The <service></service> content is encapsulated into a div tag to become transparent for the chat-user
	size_t found = textStr.find("<div");
	if (found!=std::string::npos){

		ofstream fs2("/home/guille/Escritorio/entrada_ServiceFilterSpark_found.txt");
		fs2 << textStr.substr(found).c_str() << endl;
		fs2.close();
		// Start the XML-parsing process
		pugi::xml_document doc;
		// Remove the output text (keep only the 'xml' structure) and check if it loads without errors
		if(doc.load(textStr.substr(found).c_str())){
			pugi::xml_node service = doc.child("div").child("service");
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
							LoggerInfo("[FIONA-logger] Param #%d -> %s = %s",j, param.attribute("name").value(), param.attribute("value").value());
						#endif
						body.append(param.attribute("name").value());
						body.append("=");
						body.append(param.attribute("value").value());
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
					myFlow->processData(const_cast<char *>(requestStr.c_str()));
				}
			}else{
				// HACER ALGO??
				LoggerWarn("[FIONA-logger] XML-structured text loaded without errors, but <service> node is not available");
			}
		}else{
			//  HACER ALGO??
			LoggerWarn("[FIONA-logger] XML-structured text loaded with errors");
		}
	}
}







