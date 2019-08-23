#ifndef __SugarCRMSpark_H
#define __SugarCRMSpark_H

#include "Component.h"
#include "IFlow.h"

#include "jsoncpp/value.h"
#include "jsoncpp/reader.h"
#include "jsoncpp/writer.h"
#include "curlpp/cURLpp.hpp"
#include "curlpp/Easy.hpp"
#include "curlpp/Options.hpp"
#include <curlpp/Exception.hpp>

class SugarCRMSpark :
	public Component,
	public IFlow<char*>
{

	public:
		SugarCRMSpark(
				char *instanceName,
				ComponentSystem *cs
		) : Component(instanceName, cs)
		{}

	public:
		//Mandatory
		void init(void);
		void quit(void);

		//IFlow<char *> implementation
		void processData(char*);

		string getEmailFromName(string);
		string getBase64FileFromDocumentName(string);

	private:
		IFlow<char* >* myFlowChar;

		void initializeRequiredInterfaces() {
			requestRequiredInterface<IFlow<char*> >(&myFlowChar);
		}

		string login(string, string);
		string getEmail(string, string);
		string getIDFile(string, string);
		string getBase64File(string, string);

		string username;
		string password;
		string url;
		string session;
		string argsLogin;

};

#endif
