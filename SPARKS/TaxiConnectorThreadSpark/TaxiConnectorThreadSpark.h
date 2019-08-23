/// @file TaxiConnectorThreadSpark.h
/// @brief Component TaxiConnectorThreadSpark main class.


#ifndef __TaxiConnectorThreadSpark_H
#define __TaxiConnectorThreadSpark_H


#include "Component.h"
#include "IAsyncFatalErrorHandler.h"
#include "IFlow.h"
#include "IThreadProc.h"
#include "IConcurrent.h"
#include "../TaxiConnectorSpark/TaxiConnectorSpark.h"
#include "../ThreadSpark/ThreadComponent.h"


/// @brief This is the main class for component TaxiConnectorThreadSpark.
///
///

class TaxiConnectorThreadSpark :
	public Component,
	//**Component class must extend provided interfaces**
	public IConcurrent,
	public IFlow<char*>,
	public IFlow<Json::Value*>
{
public:
		TaxiConnectorThreadSpark(
				char *instanceName,
				ComponentSystem *cs
		) : Component(instanceName, cs)
		{
			myTaxiConnectorSpark=new TaxiConnectorSpark(instanceName, cs);
			myThreadComponent=new ThreadComponent(instanceName, cs);
		}

// protected ???
private:
	//**To change for convenience**
	//Example of a required interface initialization
	IFlow<char*>* myFlowChar;
	IFlow<Json::Value*>* myFlowJSON;
	IAsyncFatalErrorHandler* myAsyncFatalErrorHandler;

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

	// IConcurrent implementation
	void start();
	void stop();

private:
	//Put class attributes here
	TaxiConnectorSpark* myTaxiConnectorSpark;
	ThreadComponent* myThreadComponent;

private:
	//Put class private methods here


};

#endif
