/// @file NewsConnectorSpark.h
/// @brief Component NewsConnectorSpark main class.


#ifndef __NewsConnectorSpark_H
#define __NewsConnectorSpark_H


#include "Component.h"
//**To change for convenience**
//Example of provided interface
#include "IFlow.h"

//**To change for convenience**
//Example of required interface
//#include "IRequiredInterface1.h"


/// @brief This is the main class for component NewsConnectorSpark.
///
///

class NewsConnectorSpark :
	public Component,
	public IFlow<char*>,
	public IFlow<Json::Value*>
{
public:
		NewsConnectorSpark(
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
