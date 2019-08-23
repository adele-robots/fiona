/*
 * TimerSpark.h
 *
 *  Created on: 05/11/2012
 *      Author: guille
 */

/// @file TimerSpark.h
/// @brief Component TimerSpark main class.


#ifndef __TimerSpark_H
#define __TimerSpark_H

#include <iostream>
#include <boost/asio.hpp>
#include <boost/bind.hpp>
#include <boost/date_time/posix_time/posix_time.hpp>

#include "Component.h"
#include "IThreadProc.h"
#include "IFlow.h"

#include "StopWatch.h"

/*****!!!!!!!!*****/
#include "IFaceExpression.h"

/// @brief This is the main class for component TimerSpark.
///
/// 

class TimerSpark :
	public Component,

	public IThreadProc
{
public:
		TimerSpark(
				char *instanceName,
				ComponentSystem *cs
		) : Component(instanceName, cs)
		{}
		/*****!!!!!!!!*****/
			IFaceExpression *myFaceExpression;
			IFlow<char*> *myFlow;

private:
	//Required interfaces initialization



	void initializeRequiredInterfaces() {	
		/*****!!!!!!!!*****/
		requestRequiredInterface<IFaceExpression>(&myFaceExpression);
		requestRequiredInterface<IFlow<char *> >(&myFlow);
	}

public:
	//Mandatory
	void init(void);
	void quit(void);

	//IThreadProc implementation
	void process();

private:
	StopWatch testStopWatch;
	boost::asio::io_service io;

	int count;
private:
	//Put class private methods here
//	void print(const boost::system::error_code& /*e*/,
//	    boost::asio::deadline_timer* t, int* count);

	//void print(const boost::system::error_code& /*e*/);
};

#endif
