/// @file TaxiConnectorSpark.h
/// @brief Component TaxiConnectorSpark main class.


#ifndef __TaxiConnectorSpark_H
#define __TaxiConnectorSpark_H


#include "Component.h"
//**To change for convenience**
//Example of provided interface
#include "IFlow.h"
#include "IThreadProc.h"

//**To change for convenience**
//Example of required interface
//#include "IRequiredInterface1.h"


/// @brief This is the main class for component TaxiConnectorSpark.
///
///

class TaxiConnectorSpark :
	public Component,
	public IFlow<char*>,
	public IFlow<Json::Value*>,
	public IThreadProc
{
public:
		TaxiConnectorSpark(
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

private:
	//Put class private methods here


};

#endif
