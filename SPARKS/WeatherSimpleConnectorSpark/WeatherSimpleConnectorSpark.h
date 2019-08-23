/// @file WeatherSimpleConnectorSpark.h
/// @brief Component WeatherSimpleConnectorSpark main class.


#ifndef __WeatherSimpleConnectorSpark_H
#define __WeatherSimpleConnectorSpark_H


#include "Component.h"
//**To change for convenience**
//Example of provided interface
#include "IFlow.h"
#include "IThreadProc.h"
#include <unordered_set>
#include <list>

//**To change for convenience**
//Example of required interface
//#include "IRequiredInterface1.h"


/// @brief This is the main class for component WeatherSimpleConnectorSpark.
///
/// 

class WeatherSimpleConnectorSpark :
	public Component,
	public IFlow<char*>,
	public IFlow<Json::Value*>,
	public IThreadProc
{
public:
		WeatherSimpleConnectorSpark(
				char *instanceName,
				ComponentSystem *cs
		) : Component(instanceName, cs)
		{}

private:
	//**To change for convenience**
	//Example of a required interface initialization
	void initializeRequiredInterfaces() {	
		requestRequiredInterface<IFlow<char*> >(&myFlowChar);
		requestRequiredInterface<IFlow<Json::Value*> >(&myFlowJSON);
	}

public:
	//Mandatory
	void init(void);
	void quit(void);
	
	//**To change for convenience**
	//Example of interface implementation declaration

	//IFlow<char*> implementation
	void processData(char*);
	void processData(Json::Value*);
	
	//IThreadProc implementation
	void process();

public:
	IFlow<char*>* myFlowChar;
	IFlow<Json::Value*>* myFlowJSON;

private:

//	std::unordered_map<int, std::queue<int, Json::Value> > requestsMap;
	vector<Json::Value> replys;
	std::unordered_set<std::string> acceptedTagTypes;
	std::map<std::string, std::list<std::string> > parametersMap;

private:
	//Put class private methods here
	

};

#endif
