/*
 * HttpRequestThreadSpark.h
 *
 *  Created on: 17/06/2013
 *      Author: Alejandro
 */

/// @file HttpRequestThreadSpark.h
/// @brief Component HttpRequestThreadSpark main class.


#ifndef __HttpRequestThreadSpark_H
#define __HttpRequestThreadSpark_H


#include "Component.h"
#include "IFlow.h"
#include "IConcurrent.h"
#include "IAsyncFatalErrorHandler.h"

#include "../HttpRequestSpark/HttpRequestSpark.h"
#include "../ThreadSpark/ThreadComponent.h"



/// @brief This is the main class for component HttpRequestThreadSpark.
///
/// 

class HttpRequestThreadSpark :
	public Component,
	public IFlow<char*>,
	public IConcurrent
{
public:
		HttpRequestThreadSpark(
				char *instanceName,
				ComponentSystem *cs
		) : Component(instanceName, cs)
		{
			//1-Instances
			myHttpRequestComponent=new HttpRequestSpark(instanceName,cs);
			myThreadComponent=new ThreadComponent(instanceName,cs);
		}
		virtual ~HttpRequestThreadSpark() {};

private:
	IFlow<char*> *myFlow;
	IAsyncFatalErrorHandler *myAsyncFatalErrorHandler;
	void initializeRequiredInterfaces() {	
		requestRequiredInterface<IFlow<char *> >(&myFlow);
		requestRequiredInterface<IAsyncFatalErrorHandler>(&myAsyncFatalErrorHandler);
	}

public:
	//Instance sub-components
	HttpRequestSpark *myHttpRequestComponent;
	ThreadComponent *myThreadComponent;

	//Mandatory
	void init(void);
	void quit(void);

	//IFlow<char*> implementation
	void processData(char * prompt);
	
	//IConcurrent implementation
	void start();
	void stop();

private:
	//Put class attributes here
private:
	//Put class private methods here
	
};

#endif
