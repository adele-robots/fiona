/*
 * TimerSpark.cpp
 *
 *  Created on: 05/11/2012
 *      Author: guille
 */

/// @file TimerSpark.cpp
/// @brief TimerSpark class implementation.


//#include "stdAfx.h"
//#include "Configuration.h"
#include "TimerSpark.h"


#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "TimerSpark")) {
			return new TimerSpark(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif

void print(const boost::system::error_code& /*e*/)
{
  std::cout << "Hello, world!\n";
}

/// Initializes TimerSpark component.
void TimerSpark::init(void){
	//testStopWatch.restart();
	int interfacesNumber = getComponentConfiguration()->getLength("Timer");
	printf("[TimerSpark::init] - Number of interfaces: %d\n",interfacesNumber);
	float test = getComponentConfiguration()->getFloat("Timer.[0].[0].[0]");
	printf("[TimerSpark::init] - Float test: %f\n",test);

//
//	cout << "[TimerSpark::init] Voy a llamar a setfaceexpression A TRAVÉS DEL HILO PRINCIPAL, OU YEAHH!! " << endl;
//	myFaceExpression->setFaceExpression("un_visema_que_vIENE",2.5);


	//boost::asio::io_service io;






//
//
//	count = 0;
//	boost::asio::deadline_timer t(io, boost::posix_time::seconds(1));
//	t.async_wait(boost::bind(this->print, boost::asio::placeholders::error, &t, &count));


}

/// Unitializes the TimerSpark component.
void TimerSpark::quit(void){
}

//IThreadProc implementation
void TimerSpark::process(void){
	//printf("[TimerSpark::process] Elapsed timed - %f\n",testStopWatch.elapsedTime());
	//sleep(1);

//	boost::asio::deadline_timer t(io, boost::posix_time::seconds(5));
//	t.async_wait(print);
//	io.run();
//
//	std::cout << "Final count is " << count << "\n";




	usleep(1000000);
	//myFlow->processData("esto es una prueba desde TimerSpark");
	//cout << "[TimerSpark::process] Voy a llamar a setfaceexpression!! desde otro hilo!!" << endl;
	//myFaceExpression->setFaceExpression("astonishment_ShapeKey",0.5);
	usleep(1000000);
	//cout << "[TimerSpark::process] Voy a llamar a setfaceexpression!! desde otro hilo!!" << endl;
	//myFaceExpression->setFaceExpression("astonishment_ShapeKey",0.0);



	//usleep(300);
	puts("[TimerSpark::process]");
	//myFlow->processData("hey boy! hey girl! superstar dee jays, here we go!");
	myFlow->processData("WHAT IS YOUR GONZALEZ WEB");
//	myFaceExpression->setFaceExpression("astonishment_ShapeKey",1);
}

//void TimerSpark::print(const boost::system::error_code& /*e*/,
//    boost::asio::deadline_timer* t, int* count)
//{
//  if (*count < 5)
//  {
//    std::cout << *count << "\n";
//    ++(*count);
//
//    t->expires_at(t->expires_at() + boost::posix_time::seconds(1));
//    t->async_wait(boost::bind(print,
//          boost::asio::placeholders::error, t, count));
//  }
//}





