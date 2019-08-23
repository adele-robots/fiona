/*
 * SaraNegocioSpark.h
 *
 *  Created on: 07/02/2013
 *      Author: guille
 */

/// @file SaraNegocioSpark.h
/// @brief Component SaraNegocioSpark main class.


#ifndef __SaraNegocioSpark_H
#define __SaraNegocioSpark_H


#include "Component.h"
#include "myCallBacks.h"
#include "Arguments.h"
#include "IFlow.h"

/// @brief This is the main class for component SaraNegocioSpark.
///
/// 

class SaraNegocioSpark :
	public Component,
	public IFlow<char*>
{
public:
		SaraNegocioSpark(
				char *instanceName,
				ComponentSystem *cs
		) : Component(instanceName, cs),
			builder(aiml.getGraphBuilder())
		{
			LoggerInfo("SaraNegocioSpark - CONSTRUCTOR");
			cout << "SaraNegocioSpark - CONSTRUCTOR" << endl << endl << endl;
		}
		virtual ~SaraNegocioSpark(){}

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
