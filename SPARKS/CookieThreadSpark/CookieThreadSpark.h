/// @file CookieThreadSpark.h
/// @brief Component CookieThreadSpark main class.


#ifndef __CookieThreadSpark_H
#define __CookieThreadSpark_H


#include "Component.h"
// Required
#include "IAsyncFatalErrorHandler.h"

// Provided
#include "IFlow.h"
#include "IThreadProc.h"
#include "IConcurrent.h"


#include "../CookieSpark/CookieSpark.h"
#include "../ThreadSpark/ThreadComponent.h"

/// @brief This is the main class for component CookieThreadSpark.
///
/// 

class CookieThreadSpark :
	public Component,
	//**Component class must extend provided interfaces**
	public IFlow<Json::Value*>,
	public IConcurrent
{
public:
		CookieThreadSpark(
				char *instanceName,
				ComponentSystem *cs
		) : Component(instanceName, cs)
		{
			//1-Instances
			myCookieComponent=new CookieSpark(instanceName,cs);
			myThreadComponent=new ThreadComponent(instanceName,cs);
		}

private:
	IFlow<char*> *myFlow;
	IAsyncFatalErrorHandler *myAsyncFatalErrorHandler;

	void initializeRequiredInterfaces() {	
		requestRequiredInterface<IFlow<char *> >(&myFlow);
		requestRequiredInterface<IAsyncFatalErrorHandler>(&myAsyncFatalErrorHandler);
	}

public:
	//Instance sub-components
	CookieSpark *myCookieComponent;
	ThreadComponent *myThreadComponent;

	//Mandatory
	void init(void);
	void quit(void);
	
	//IFlow implementation
	void processData(Json::Value *);

	//IConcurrent implementation
	void start();
	void stop();
	
private:
	//Put class attributes here
private:
	//Put class private methods here
	
};

#endif
