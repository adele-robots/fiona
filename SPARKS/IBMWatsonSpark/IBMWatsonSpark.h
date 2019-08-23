/// @file IBMWatsonSpark.h
/// @brief Component IBMWatsonSpark main class.


#ifndef __IBMWatsonSpark_H
#define __IBMWatsonSpark_H

#define UNKONWN_SPEAKER "unkown"

#include "Component.h"

// Provided interface
#include "IFlow.h"

#include "jsoncpp/json.h"
#include <restclient-cpp/restclient.h>

/// @brief This is the main class for component IBMWatsonSpark.
///
/// 

class IBMWatsonSpark :
	public Component,
	//**Component class must extend provided interfaces**
	public IFlow<char* >
{
public:
		IBMWatsonSpark(
				char *instanceName,
				ComponentSystem *cs
		) : Component(instanceName, cs)
		{}

private:
	// Required interface initialization
	void initializeRequiredInterfaces() {	
		requestRequiredInterface<IFlow<char *> >(&myCharFlow);
	}

public:
	//Mandatory
	void init(void);
	void quit(void);
	
	// IFlow<AudioWrap *> implementation
	void processData(char*);

private:

	std::string knowledgeTopic;
	std::string defaultAnswer;
	std::string APIUsername;
	std::string APIPassword;
	std::string APIURL;

public:
	IFlow<char *> *myCharFlow;

private:

	std::string getRequestBody(std::string questionText);
	std::string getBestAnswer(std::string questionText);
	std::string trimAnswerLength(std::string);

};

#endif
