#ifndef __SalesforceSpark_H
#define __SalesforceSpark_H

#include "Component.h"
#include "IFlow.h"
#include "curlpp/cURLpp.hpp"
#include "curlpp/Easy.hpp"
#include "curlpp/Options.hpp"
#include <curlpp/Exception.hpp>
#include <curlpp/Infos.hpp>
#include "jsoncpp/value.h"
#include "jsoncpp/reader.h"
#include "jsoncpp/writer.h"

class SalesforceSpark :
	public Component,
	public IFlow<Json::Value*>
{

	public:
		SalesforceSpark(
				char *instanceName,
				ComponentSystem *cs
		) : Component(instanceName, cs)
		{}

	public:
		//Mandatory
		void init(void);
		void quit(void);

		//IFlow<Json::Value *> implementation
		void processData(Json::Value*);

	private:
		IFlow<char* >* myFlowChar;

	private:

		void submitData(std::string orgId, std::string name, std::string email, std::string phone, std::string subject,
				std::string description, std::string company, bool debug = false, std::string debugEmail = "");

		void initializeRequiredInterfaces() {
			requestRequiredInterface<IFlow<char*> >(&myFlowChar);
		}

};

#endif
