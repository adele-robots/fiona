/// @file TestChatSpark.h
/// @brief Component TestChatSpark main class.


#ifndef __TestChatSpark_H
#define __TestChatSpark_H

#include <iostream>
using namespace std;

#include <sys/ipc.h>
#include <sys/msg.h>
#include <sys/types.h>


#include "Component.h"

//Example of provided interface
#include "IApplication.h"
#include "IAsyncFatalErrorHandler.h"

// Mensajes a intercambiar en la aplicación:
// - Tipo del mensaje (PID_proceso, 1)
// - Contenido
typedef struct{
	long int tipo;
	char contenido[1024];
} mensaje;


//Example of required interface



/// @brief This is the main class for component TestChatSpark.
///
/// 

class TestChatSpark :
	public Component,
	//**Component class must extend provided interfaces**
	public IApplication,
	public IAsyncFatalErrorHandler
{
public:
		TestChatSpark(
				char *instanceName,
				ComponentSystem *cs
		) : Component(instanceName, cs)
		{}

private:

	void initializeRequiredInterfaces() {

	}


public:
	//Mandatory
	void init(void);
	void quit(void);
	
	//Implementation of IApplication
	void run(void);
	
	//IAsyncFatalErrorHandler implementation
	void handleError(char*);


private:
	//Put class attributes here
	bool running;
private:
	//Put class private methods here
	
};

#endif
