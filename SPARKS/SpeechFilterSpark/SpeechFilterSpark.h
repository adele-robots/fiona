/*
 * SpeechFilterSpark.h
 *
 *  Created on: 30/09/2013
 *      Author: guille
 */

/// @file SpeechFilterSpark.h
/// @brief Component SpeechFilterSpark main class.


#ifndef __SpeechFilterSpark_H
#define __SpeechFilterSpark_H

#include "Component.h"
#include "IFlow.h"

//XML-DOM parser
#include "pugixml.hpp"

//Stacktrace
#include <sys/syscall.h>
#include "stacktrace/call_stack.hpp"
#include "stacktrace/stack_exception.hpp"
// Regular expressions
#include <boost/regex.hpp>
// base64 home-made library
#include "base64.h"

typedef map<string, string>::const_iterator MapIterator;

/// @brief This is the main class for component SpeechFilterSpark.
///
/// 

class SpeechFilterSpark :
	public Component,
	public IFlow<char*>
{
public:
		SpeechFilterSpark(
				char *instanceName,
				ComponentSystem *cs
		) : Component(instanceName, cs)
		{}
		virtual ~SpeechFilterSpark(){}

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

	//Different source implementations
	void sourceImplementation1(char * prompt);
	void sourceImplementation2(char * prompt);

	// Auxiliary function to extract <xml>TEXT WITH XML-TAGS</xml>
	string xmlExtract(string input, string tag);
	// Tokenizing a string
	vector<string> tokenizer( const string& p_pcstStr, char delim );
private:
	map<string,string> userDetails;
	vector<string> sparkSources;
	string userInput;
	string aimlInput;
};

#endif
