/// @file RebeccaAIMLSpark.h
/// @brief Component RebeccaAIMLSpark main class.


#ifndef __RebeccaAIMLSpark_H
#define __RebeccaAIMLSpark_H

#include "Component.h"
#include "myCallBacks.h"
#include "Arguments.h"
#include "IFlow.h"

/// @brief This is the main class for component RebeccaAIMLSpark.
///
/// 

class RebeccaAIMLSpark :
	public Component,
	public IFlow<char*>
{
public:
		RebeccaAIMLSpark(
				char *instanceName,
				ComponentSystem *cs
		) : Component(instanceName, cs),
		   builder(aiml.getGraphBuilder())
		{
			LoggerInfo("RebeccaAIMLSpark - CONSTRUCTOR");
			cout << "RebeccaAIMLSpark - CONSTRUCTOR" << endl << endl << endl;
		}

protected:
	IFlow<char *> *myFlow;

	void initializeRequiredInterfaces() {
		LoggerInfo("*********** RebeccaAIMLSpark - initializeRequiredInterfaces ***********");
		cout << " *********** RebeccaAIMLSpark - initializeRequiredInterfaces ***********" << endl << endl << endl;
		requestRequiredInterface<IFlow<char *> >(&myFlow);
	}

public:
	//Mandatory
	void init(void);
	void quit(void);

	//IFlow implementation
	void processData(char *);

	char * removeAccentMarks(char *);

	
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
	void initializeCharMap();
	
	//Put class private attributes here
	map<string, string> charMap;

};

#endif
