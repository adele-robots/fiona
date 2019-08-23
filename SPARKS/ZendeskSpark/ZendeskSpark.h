/*
 * ZendeskSpark.h
 *
 *  Created on: 12/02/2015
 *      Author: Adrián
 */

/// @file ZendeskSpark.h
/// @brief Component ZendeskSpark main class.


#ifndef __ZendeskSpark_H
#define __ZendeskSpark_H

#include "jsoncpp/json.h"

#include "Component.h"
#include "IFlow.h"

/// @brief This is the main class for component ZendeskSpark.
///
/// 

class ZendeskSpark :
	public Component,
	public IFlow<Json::Value*>
{
public:
	ZendeskSpark(
			char *instanceName,
			ComponentSystem *cs
	) : Component(instanceName, cs)
	{}

	virtual ~ZendeskSpark() {};


	void initializeRequiredInterfaces() {	
		requestRequiredInterface<IFlow<char *> >(&myFlow);
	}

	IFlow<char *> *myFlow;

public:
	//Mandatory
	void init(void);
	void quit(void);

	//IFlow<char*> implementation
	void processData(Json::Value * prompt);

	
private:
	//Put class attributes here
	std::string domain;
	std::string agent_username;
	//std::string agent_password;
	std::string	api_token;
	bool validEmail;
	bool validId;

};

#endif
