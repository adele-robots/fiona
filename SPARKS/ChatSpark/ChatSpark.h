/// @file CHatSpark.h
/// @brief Component CHatSpark main class.


#ifndef __ChatSpark_H
#define __ChatSpark_H


#include "Component.h"

// Required and Provided interface
#include "IFlow.h"
#include "IThreadProc.h"

/* LOG4CPLUS Headers */
#include <log4cplus/logger.h>
#include <log4cplus/fileappender.h>
#include <log4cplus/layout.h>
#include <log4cplus/ndc.h>
#include <log4cplus/helpers/loglog.h>

#include <syscall.h>

using namespace log4cplus;
using namespace log4cplus::helpers;

#define DEBUG 0

#ifndef CERR
#define CERR(msg) if(DEBUG) {std::cerr << __FILE__ << ":" << std::dec << __LINE__ << ":" <<__FUNCTION__ << "(): " << msg;}
#endif

#ifndef COUT
#define COUT std::cout << __FILE__ << ":" << std::dec << __LINE__ << " : "
#endif

// Mensajes a intercambiar en la aplicación:
// - Tipo del mensaje (PID_proceso, 1)
// - Contenido
typedef struct{
	long int tipo;
	char contenido[1024];
} mensaje;


/// @brief This is the main class for component CHatSpark.
///
/// Spark that receives a text via a message queue, and communicates with RebecaAIMSSpark to
/// get an answer. After that, it sends that answer again via the message queue.

class ChatSpark :
	public Component,
	//**Component class must extend provided interfaces**
	public IFlow<char*>,
	public IThreadProc
{
public:
		ChatSpark(
				char *instanceName,
				ComponentSystem *cs
		) : Component(instanceName, cs)
		{}

//private:
	// Required interface initialization
	IFlow<char*> *myFlow;

	void initializeRequiredInterfaces() {	
		requestRequiredInterface<IFlow<char *> >(&myFlow);
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
	// Elimina marcas html del log
	string& stripHTMLTags(string& s);

public:
	static void signalHandler(int sig);

};

#endif
