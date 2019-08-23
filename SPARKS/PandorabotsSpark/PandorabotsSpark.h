/*
 * PandorabotsSpark.h
 *
 *  Created on: 07/06/2013
 *      Author: Alejandro
 */

/// @file PandorabotsSpark.h
/// @brief Component PandorabotsSpark main class.


#ifndef __PandorabotsSpark_H
#define __PandorabotsSpark_H


#include "Component.h"
#include "IFlow.h"

/// @brief This is the main class for component PandorabotsSpark.
///
/// 

class PandorabotsSpark :
	public Component,
	public IFlow<char*>
{
public:
	PandorabotsSpark(
			char *instanceName,
			ComponentSystem *cs
	) : Component(instanceName, cs)
	{}

	virtual ~PandorabotsSpark() {};


	void initializeRequiredInterfaces() {	
		requestRequiredInterface<IFlow<char *> >(&myFlow);
	}

	IFlow<char *> *myFlow;

public:
	//Mandatory
	void init(void);
	void quit(void);

	//IFlow<char*> implementation
	void processData(char * prompt);

	
private:
	//Put class attributes here
	std::string botid;
	int sessionid;
	std::string userkey;
	int count;
	bool firstTime;
	char * response;
};

#endif
