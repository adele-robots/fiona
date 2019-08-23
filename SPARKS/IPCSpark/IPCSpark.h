/// @file IPCSpark.h
/// @brief Component IPCSpark main class.


#ifndef __IPCSpark_H
#define __IPCSpark_H


#include "Component.h"

// Required and Provided interface
#include "IFlow.h"
#include "IThreadProc.h"

// Mensajes a intercambiar en la aplicación:
// - Tipo del mensaje (PID_proceso, 1)
// - Contenido
typedef struct{
	long int tipo;
	char contenido[1024];
} mensaje;


/// @brief This is the main class for component IPCSpark.
///
/// Spark that receives a text via a message queue, and communicates with RebecaAIMSSpark to
/// get an answer. After that, it sends that answer again via the message queue.

class IPCSpark :
	public Component,
	//**Component class must extend provided interfaces**
	public IFlow<char*>,
	public IThreadProc
{
public:
		IPCSpark(
				char *instanceName,
				ComponentSystem *cs
		) : Component(instanceName, cs)
		{}

	void initializeRequiredInterfaces() {
	}

public:
	//Mandatory
	void init(void);
	void quit(void);

	// Interface implementation declaration
	//IFlow implementation
	void processData(char *);

	// Implementation of IThreadproc
	void process();

private:

	/* Identificador del proceso */
	int pid;

private:
	//Put class private methods here
	void recibirMensaje(IFlow<char*> *flow);
	/* Gestión de errores */
	void getError(int error);

public:
	static void signalHandler(int sig);

};

#endif
