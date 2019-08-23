/*
 * ProactiveSpark.h
 *
 *  Created on: 03/10/2013
 *      Author: guille
 */

/// @file ProactiveSpark.h
/// @brief Component ProactiveSpark main class.


#ifndef __ProactiveSpark_H
#define __ProactiveSpark_H


#include "Component.h"
#include "IFlow.h"
#include "IThreadProc.h"

#include "StopWatch.h"
#include <signal.h>

/// @brief This is the main class for component ProactiveSpark.
///
/// 

class ProactiveSpark :
	public Component,

	public IFlow<char*>,
	public IThreadProc
{
public:
		ProactiveSpark(
				char *instanceName,
				ComponentSystem *cs
		) : Component(instanceName, cs), firstEngageDone(false), closingProactivity(false)
		{}
		virtual ~ProactiveSpark(){}

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
	
	// Implementation of IThreadproc
	void process();

private:
	StopWatch timeCounter;
	float elapsedTime;
	float downtime;
	int consecutiveClosingAttempts;
	int closingAttempt;
	int timeUntilKill;
	bool firstEngageDone;
	bool closingProactivity;
	bool flagKill;
private:
	void killMyself(int seconds);
};

#endif
