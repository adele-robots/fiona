/*
 * ProgramQAIMLSpark.h
 *
 *  Created on: 30/01/2013
 *      Author: guille
 */

/// @file ProgramQAIMLSpark.h
/// @brief Component ProgramQAIMLSpark main class.


#ifndef __ProgramQAIMLSpark_H
#define __ProgramQAIMLSpark_H

#include "Component.h"
#include "IFlow.h"
#include "aimlparser.h"
#include <stdio.h>
#include <iostream>
#include <signal.h>
#include <QTextEdit>

/// @brief This is the main class for component ProgramQAIMLSpark.
///
/// 

class ProgramQAIMLSpark :
	public Component,

	public IFlow<char*>
{
public:
		ProgramQAIMLSpark(
				char *instanceName,
				ComponentSystem *cs
		) : Component(instanceName, cs)//, regexp("&lt;(/*)html:(.+)&gt;")
		{
//			QString userWorkingDir = QString::fromStdString(getGlobalConfiguration()->getUserDir());
//			userWorkingDir.append("/ProgramQ");
//			parser(false,userWorkingDir);
		}
		virtual ~ProgramQAIMLSpark() {};

private:
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

private:
	AIMLParser* parser;
	//QRegExp regexp;
private:
	//Put class private methods here
	
};

#endif
