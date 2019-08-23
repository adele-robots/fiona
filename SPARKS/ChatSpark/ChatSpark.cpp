/// @file CHatSpark.cpp
/// @brief CHatSpark class implementation.


#include "stdAfx.h"
// Includes mecanismos IPC
#include <sys/ipc.h>
#include <sys/msg.h>

#include <string.h>
#include <typeinfo>
#include <cxxabi.h>
#include <iostream>
#include <stdlib.h>

#include "ChatSpark.h"
#include <signal.h>
#include <unistd.h>

using namespace std;


/* Códigos de error para la cola de mensajes */
#define E_CREA_COLA_MSG 	-3
#define E_ENV_COLA_MSG		-4
#define E_LIB_COLA_MSG		-5

// Necesario para las colas de mensajes
#define CLAVE_IPC 12500
#define PERMISOS 0666


Logger myLogger;


#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "ChatSpark")) {
			return new ChatSpark(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif

/* Idetificador único de la cola de mensajes */
int idColaMsg;
/* Contenido del mensaje recibido */
mensaje receivedMessage;

void ChatSpark::signalHandler(int sig){
	//LoggerInfo("Se ha capturado la señal sig en ChatSpark");

	// Liberar cola de mensajes
	msgctl(idColaMsg, IPC_RMID,(struct msqid_ds *)NULL);

	// Asignar a esa señal el manejador por defecto
	signal(sig,SIG_DFL);

	// Volver a lanzar la señal de terminación
	raise(sig);

}

/// Initializes CHatSpark component.
void ChatSpark::init(void){

	//signal((int) SIGTERM, signalHandler);
	//signal((int) SIGINT, signalHandler);
	//signalHandler.setupSignalHandlers();

	// Get user's working directory
	string logFilename = getGlobalConfiguration()->getUserDir();

	// Main thread ID
	int threadId = syscall(SYS_gettid);

	logFilename.append("/");
	//logFilename.append(boost::lexical_cast<string>(threadId));
	time_t  timestamp = time(0);
	char hostname[1024];
    size_t len = 1024;
    gethostname(hostname, len);
    string session = getGlobalConfiguration()->getString(const_cast<char*>("session"));
	stringstream nombre;
	nombre << timestamp << "_" << hostname << "_" << threadId << "_" << session << "_chat.log";
	logFilename.append(nombre.str());
	CERR("El working directory es: " << logFilename << endl);

	// Initialize log session
	myLogger = Logger::getInstance(LOG4CPLUS_TEXT("CHATLOG"));
	LogLog::getLogLog()->setInternalDebugging(true);

	SharedAppenderPtr append_1(new RollingFileAppender(LOG4CPLUS_TEXT(logFilename)));
	append_1->setName(LOG4CPLUS_TEXT("ChatLog"));
	append_1->setLayout( std::auto_ptr<Layout>(new PatternLayout("%D{%y-%m-%d %H:%M:%S} - %m%n")) );
	myLogger.addAppender(append_1);
	// Parent loggers will not log 'myLogger' messages
	myLogger.setAdditivity(false);

	// Inicializar cola de Mensajes
	// Pid del proceso que se usará como clave IPC
	pid = getpid() << 5;

	int pidOffset = getComponentConfiguration()->getInt(const_cast<char*>("Pid_Offset"));

	idColaMsg = msgget(pid + pidOffset, PERMISOS|IPC_CREAT);
	LoggerInfo("key = %d idCola = %d", pid + pidOffset, idColaMsg);

	if (idColaMsg<0)
	{
		getError(E_CREA_COLA_MSG);
	}
	else
	{
		std::string className(abi::__cxa_demangle(typeid(*this).name(),0,0,0));
		std::ofstream outputFile(("/tmp/" + session + "_" + className).c_str());
		outputFile << (pid + pidOffset);
		outputFile.close();
	}
	LOG4CPLUS_INFO(myLogger,LOG4CPLUS_TEXT("INIT"));
}




/// Unitializes the CHatSpark component.
void ChatSpark::quit(void){

	// Liberar cola de mensajes
	if(msgctl(idColaMsg, IPC_RMID,(struct msqid_ds *) NULL)<0)
		getError(E_LIB_COLA_MSG);

	LOG4CPLUS_INFO(myLogger,LOG4CPLUS_TEXT("QUIT"));
}

//**To change for your convenience**
//Example of provided interface implementation

void ChatSpark::processData(char *prompt){	
	string outputMsg = "Avatar: ";
	string cadena(prompt);
	//outputMsg.append(stripHTMLTags(cadena));
	outputMsg.append(cadena);
	LOG4CPLUS_INFO(myLogger,LOG4CPLUS_TEXT(outputMsg));
	// Recuperamos la respuesta del gestor
	mensaje mensajeDevuelto;
	// El tipo del mensaje enviado a JAVA será 2
	mensajeDevuelto.tipo = 2;

	memset(mensajeDevuelto.contenido,'\0',sizeof(mensajeDevuelto.contenido));
	strcpy(mensajeDevuelto.contenido, prompt);

	if (msgsnd(idColaMsg, &mensajeDevuelto,sizeof(mensajeDevuelto.contenido), 0) != 0) {
		getError(E_ENV_COLA_MSG);
	}
}



//IThreadProc implementation
void ChatSpark::process() {
	//SignalHandler signalHandler;
	
	// Esperar la llegada de mensajes
		memset(receivedMessage.contenido,'\0',sizeof(receivedMessage.contenido));
		// Esperando mensaje de tipo 1
		int bytesRecibidos = msgrcv(idColaMsg, &receivedMessage, sizeof(receivedMessage.contenido), 1,0);//llamada bloqueante

		// Procesamiento del mensaje de entrada
		// Enviamos al gestor de diálogo la entrada
		// de texto del usuario
		if(bytesRecibidos > 0){			
			//Log
			string inputMsg = "User: ";
			inputMsg.append(receivedMessage.contenido);
			LOG4CPLUS_INFO(myLogger,LOG4CPLUS_TEXT(inputMsg));
			myFlow->processData(receivedMessage.contenido);
		}
}



/* Gestión de errores */
void ChatSpark::getError(int err){

	switch (err) {

		case E_CREA_COLA_MSG:
			ERR("<ERROR==> CHatSpark> No se puede crear la cola de mensajes.\n");

			exit(-1);
			break;

		case E_ENV_COLA_MSG:
			ERR("<ERROR==> CHatSpark> l enviar mensaje de respuesta a cola.\n");

			exit(-1);
			break;
		case E_LIB_COLA_MSG:
			ERR("<ERROR==> CHatSpark> Al tratar de liberar la cola de mensajes.\n");

			exit(-1);
			break;
	}




}


string& ChatSpark::stripHTMLTags(string& s) {
	static bool inTag = false;
	bool done = false;
	while (!done) {
		if (inTag) {
// The previous line started an HTML tag
// but didn't finish. Must search for '>'.
			size_t rightPos = s.find('>');
			if (rightPos != string::npos) {
				inTag = false;
				s.erase(0, rightPos + 1);
			} else {
				done = true;
				s.erase();
			}
		} else {
			// Look for start of tag:
			size_t leftPos = s.find('<');
			if (leftPos != string::npos) {
				// See if tag close is in this line:
				size_t rightPos = s.find('>');
				if (rightPos == string::npos) {
					inTag = done = true;
					s.erase(leftPos);
				} else
					s.erase(leftPos, rightPos - leftPos + 1);
			} else
				done = true;
		}
	}
	return s;
}
