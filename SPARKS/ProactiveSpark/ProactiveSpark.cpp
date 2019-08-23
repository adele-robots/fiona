/*
 * ProactiveSpark.cpp
 *
 *  Created on: 03/10/2013
 *      Author: guille
 */

/// @file ProactiveSpark.cpp
/// @brief ProactiveSpark class implementation.


//#include "stdAfx.h"
#include "ProactiveSpark.h"


#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "ProactiveSpark")) {
			return new ProactiveSpark(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif

/// Initializes ProactiveSpark component.
void ProactiveSpark::init(void){
	downtime = getComponentConfiguration()->getFloat(const_cast<char *>("Downtime"));
	consecutiveClosingAttempts = getComponentConfiguration()->getInt(const_cast<char *>("Closing_Attempts"));
	timeUntilKill = getComponentConfiguration()->getInt(const_cast<char *>("Time_To_Kill"));

	//Initialize closing attempts
	closingAttempt = 0;

	flagKill = false;
}

/// Unitializes the ProactiveSpark component.
void ProactiveSpark::quit(void){
}

//IThreadProc implementation
void ProactiveSpark::process() {
	usleep(200000);
	elapsedTime = timeCounter.elapsedTime();
	// Check if time since a user input is longer than a given (by config) inactivity time
	if(elapsedTime > downtime){
		timeCounter.restart();
		if(closingProactivity){
			if(closingAttempt < consecutiveClosingAttempts){
				closingAttempt++;
				myFlow->processData(const_cast<char*>("CIERRE"));
			}else{
				//closingAttempt = 0;
				if(!firstEngageDone){
					firstEngageDone = true;
					myFlow->processData(const_cast<char*>("REENGANCHE 1"));
				}else{
					myFlow->processData(const_cast<char*>("REENGANCHE 2"));
					killMyself(timeUntilKill);
				}
			}
		}else if(!firstEngageDone){
			firstEngageDone = true;
			myFlow->processData(const_cast<char*>("REENGANCHE 1"));
		}else{
			myFlow->processData(const_cast<char*>("REENGANCHE 2"));
			/* otra forma distinta al sleep?? mientras dura esta espera si le preguntan algo va a responder...*/
			killMyself(timeUntilKill);
		}
	}
	if(flagKill)
		killMyself(timeUntilKill);
}

//IFlow implementation
void ProactiveSpark::processData(char * text){
	string aimlResponse(text);

	// If elapsed time is less than half a second, it is considered to be the AIML-response
	// to the "proactive message", not a user input
	elapsedTime = timeCounter.elapsedTime();
	if(elapsedTime > 0.5f){
		firstEngageDone = false;
		if(closingProactivity){
			closingAttempt = 0;
		}
	}
	//Restart stopwatch to check user inactivity
	timeCounter.restart();

	// Analyze AIML response
	if (string::npos != aimlResponse.find("<cierre>")){
		closingProactivity = true;
	}
	if (string::npos != aimlResponse.find("<kill>")){
		flagKill = true;
	}
}


void ProactiveSpark::killMyself(int seconds){
	//Sleep some seconds to allow user to read last message
	sleep(seconds);
	pid_t pid = getpid();
	kill(pid, SIGTERM);
}
