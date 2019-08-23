/// @file IPCSpark.cpp
/// @brief IPCSpark class implementation.


// Includes mecanismos IPC
#include <sys/ipc.h>
#include <sys/msg.h>

#include <string.h>
#include <fstream>
#include <stdlib.h>

#include "IPCSpark.h"
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

extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "IPCSpark")) {
			return new IPCSpark(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}

/* Identificador único de la cola de mensajes */
int idColaMsg;
/* Contenido del mensaje recibido */
mensaje receivedMessage;

void IPCSpark::signalHandler(int sig){
	//LoggerInfo("Se ha capturado la señal sig en IPCSpark");

	// Liberar cola de mensajes
	msgctl(idColaMsg, IPC_RMID,(struct msqid_ds *)NULL);

	// Asignar a esa señal el manejador por defecto
	signal(sig,SIG_DFL);

	// Volver a lanzar la señal de terminación
	raise(sig);

}

/// Initializes IPCSpark component.
void IPCSpark::init(void){

    string session = getGlobalConfiguration()->getString(const_cast<char*>("session"));

	std::ifstream inputFile(("/tmp/" + session + "_ChatSpark").c_str());
	inputFile >> pid;
	inputFile.close();

	// Inicializar cola de Mensajes
	idColaMsg = msgget(pid, PERMISOS|IPC_CREAT);
}

/// Unitializes the IPCSpark component.
void IPCSpark::quit(void){

	// NO Liberar cola de mensajes, lo hace el ChatSpark
	//if(msgctl(idColaMsg, IPC_RMID,(struct msqid_ds *) NULL)<0)
	//	getError(E_LIB_COLA_MSG);
}

void IPCSpark::processData(char *prompt){
	// Recuperamos la respuesta del gestor
	mensaje mensajeDevuelto;
	// El tipo del mensaje enviado al Chat será 1
	mensajeDevuelto.tipo = 1;

	memset(mensajeDevuelto.contenido,'\0',sizeof(mensajeDevuelto.contenido));
	strcpy(mensajeDevuelto.contenido, prompt);

	if (msgsnd(idColaMsg, &mensajeDevuelto,sizeof(mensajeDevuelto.contenido), 0) != 0) {
		getError(E_ENV_COLA_MSG);
	}
}

//IThreadProc implementation
void IPCSpark::process() {
	//SignalHandler signalHandler;

	// Esperar la llegada de mensajes
		memset(receivedMessage.contenido,'\0',sizeof(receivedMessage.contenido));
		// Esperando mensaje de tipo 2
		msgrcv(idColaMsg, &receivedMessage, sizeof(receivedMessage.contenido), 2,0);//llamada bloqueante

		// No hacer nada con el mensaje
}

/* Gestión de errores */
void IPCSpark::getError(int err){

	switch (err) {

		case E_CREA_COLA_MSG:
			ERR("<ERROR==> IPCSpark> No se puede crear la cola de mensajes.\n");

			exit(-1);
			break;

		case E_ENV_COLA_MSG:
			ERR("<ERROR==> IPCSpark> Al enviar mensaje de respuesta a cola.\n");

			exit(-1);
			break;
		case E_LIB_COLA_MSG:
			ERR("<ERROR==> IPCSpark> Al tratar de liberar la cola de mensajes.\n");

			exit(-1);
			break;
	}

}
