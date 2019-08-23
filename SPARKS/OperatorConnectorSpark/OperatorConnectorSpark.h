/// @file OperatorConnectorSpark.h
/// @brief Component OperatorConnectorSpark main class.


#ifndef __OperatorConnectorSpark_H
#define __OperatorConnectorSpark_H


#include "Component.h"
#include "IFlow.h"


/// @brief This is the main class for component OperatorConnectorSpark.
///
///

class OperatorConnectorSpark :
	public Component,
	public IFlow<char*>,
	public IFlow<Json::Value*>
{
public:
		OperatorConnectorSpark(
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
	//Put class private methods here


};

#endif
