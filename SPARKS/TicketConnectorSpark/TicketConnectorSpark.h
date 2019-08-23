/*
 * TicketConnectorSpark.h
 *
 *  Created on: 19/02/2015
 *      Author: Adrián
 */

/// @file TicketConnectorSpark.h
/// @brief Component TicketConnectorSpark main class.


#ifndef __TicketConnectorSpark_H
#define __TicketConnectorSpark_H


#include "Component.h"

#include "jsoncpp/json.h"
#include "IFlow.h"
#include <unordered_set>

/// @brief This is the main class for component TicketConnectorSpark.
///
/// 

class TicketConnectorSpark :
	public Component,
	public IFlow<char*>,
	public IFlow<Json::Value*>
{
public:
		TicketConnectorSpark(
				char *instanceName,
				ComponentSystem *cs
		) : Component(instanceName, cs)
		{}

		virtual ~TicketConnectorSpark() {};

private:

	void initializeRequiredInterfaces() {	
		requestRequiredInterface<IFlow<char*> >(&myFlowChar);
		requestRequiredInterface<IFlow<Json::Value*> >(&myFlowJSON);
	}

public:
	//Mandatory
	void init(void);
	void quit(void);
	
	void processData(char*);
	void processData(Json::Value*);
	

public:
	IFlow<char*>* myFlowChar;
	IFlow<Json::Value*>* myFlowJSON;

private:

//	std::unordered_map<int, std::queue<int, Json::Value> > requestsMap;
	vector<Json::Value> replys;
	std::unordered_set<std::string> acceptedTagTypes;

};

#endif
