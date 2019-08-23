/*
 * ServiceFilterSpark.h
 *
 *  Created on: 30/09/2013
 *      Author: guille
 */

/// @file ServiceFilterSpark.h
/// @brief Component ServiceFilterSpark main class.


#ifndef __ServiceFilterSpark_H
#define __ServiceFilterSpark_H


#include "Component.h"
#include "IFlow.h"

//XML-DOM parser
#include "pugixml.hpp"

#ifdef SPARK_DEBUG
#include <sys/syscall.h>
#include "stacktrace/call_stack.hpp"
#include "stacktrace/stack_exception.hpp"
#endif


/// @brief This is the main class for component ServiceFilterSpark.
///
/// 

class ServiceFilterSpark :
	public Component,
	public IFlow<char*>
{
public:
		ServiceFilterSpark(
				char *instanceName,
				ComponentSystem *cs
		) : Component(instanceName, cs)
		{}
		virtual ~ServiceFilterSpark(){}

private:
	IFlow<char *> *myFlow;

	void initializeRequiredInterfaces() {	
		requestRequiredInterface<IFlow<char *> >(&myFlow);
	}

public:
	//Mandatory
	void init(void);
	void quit(void);

	//IFlow<char*> implementation
	void processData(char * prompt);

};

#endif
