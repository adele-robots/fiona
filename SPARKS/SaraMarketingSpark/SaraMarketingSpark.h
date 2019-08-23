/*
 * SaraMarketingSpark.h
 *
 *  Created on: 07/02/2013
 *      Author: guille
 */

/// @file SaraMarketingSpark.h
/// @brief Component SaraMarketingSpark main class.


#ifndef __SaraMarketingSpark_H
#define __SaraMarketingSpark_H

#include "Component.h"
#include "myCallBacks.h"
#include "Arguments.h"
#include "IFlow.h"

/// @brief This is the main class for component SaraMarketingSpark.
///
/// 

class SaraMarketingSpark :
	public Component,
	public IFlow<char*>
{
public:
		SaraMarketingSpark(
				char *instanceName,
				ComponentSystem *cs
		) : Component(instanceName, cs),
			builder(aiml.getGraphBuilder())
		{
			LoggerInfo("SaraMarketingSpark - CONSTRUCTOR");
			cout << "SaraMarketingSpark - CONSTRUCTOR" << endl << endl << endl;
		}
		virtual ~SaraMarketingSpark(){}


protected:
	IFlow<char *> *myFlow;

	void initializeRequiredInterfaces() {
		requestRequiredInterface<IFlow<char *> >(&myFlow);
	}

public:
	//Mandatory
	void init(void);
	void quit(void);

	//IFlow implementation
	void processData(char *);
	
protected:
	/*
	 * This is responsible for memory management of
	 * GraphBuilder.
	 */
	AimlFacade aiml;

	GraphBuilder &builder;

	/*
	 * Create an instantiation of our custom
	 * callbacks we created above.
	 */
	myCallBacks callback;

private:
	//Put class private methods here
	
};

#endif
