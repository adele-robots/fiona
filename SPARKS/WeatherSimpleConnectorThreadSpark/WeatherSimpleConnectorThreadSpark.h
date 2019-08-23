/// @file WeatherSimpleConnectorThreadSpark.h
/// @brief Component WeatherSimpleConnectorThreadSpark main class.


#ifndef __WeatherSimpleConnectorThreadSpark_H
#define __WeatherSimpleConnectorThreadSpark_H


#include "Component.h"
#include "IAsyncFatalErrorHandler.h"
#include "IFlow.h"
#include "IThreadProc.h"
#include "IConcurrent.h"
#include "../WeatherSimpleConnectorSpark/WeatherSimpleConnectorSpark.h"
#include "../ThreadSpark/ThreadComponent.h"


/// @brief This is the main class for component WeatherSimpleConnectorThreadSpark.
///
/// 

class WeatherSimpleConnectorThreadSpark :
	public Component,
	//**Component class must extend provided interfaces**
	public IConcurrent,
	public IFlow<char*>,
	public IFlow<Json::Value*>
{
public:
		WeatherSimpleConnectorThreadSpark(
				char *instanceName,
				ComponentSystem *cs
		) : Component(instanceName, cs)
		{
			myWeatherSimpleConnectorSpark=new WeatherSimpleConnectorSpark(instanceName, cs);
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
	WeatherSimpleConnectorSpark* myWeatherSimpleConnectorSpark;
	ThreadComponent* myThreadComponent;

private:
	//Put class private methods here
	

};

#endif
