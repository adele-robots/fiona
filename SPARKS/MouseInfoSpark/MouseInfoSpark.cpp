/*
 * MouseInfoSpark.cpp
 *
 *  Created on: 12/11/2013
 *      Author: guille
 */

/// @file MouseInfoSpark.cpp
/// @brief MouseInfoSpark class implementation.


#include "stdAfx.h"
#include "MouseInfoSpark.h"
#include <typeinfo>
#include <cxxabi.h>
#include <fstream>


/* Códigos de error para la cola de mensajes */
#define E_CREA_COLA_MSG 	-3
#define E_ENV_COLA_MSG		-4
#define E_LIB_COLA_MSG		-5

// Necesario para las colas de mensajes
#define CLAVE_IPC 12500
#define PERMISOS 0666


#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "MouseInfoSpark")) {
			return new MouseInfoSpark(componentInstanceName, componentSystem);
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

/// Initializes MouseInfoSpark component.
void MouseInfoSpark::init(void){
	//Initialize mouse positions
	mousePositions = "0 0";

	// Inicializar cola de Mensajes
	// Pid del proceso que se usará como clave IPC
	pid = getpid() << 5;

	int pidOffset = getComponentConfiguration()->getInt(const_cast<char*>("Pid_Offset"));

	idColaMsg = msgget(pid + pidOffset, PERMISOS|IPC_CREAT);

	if (idColaMsg<0)
	{
		getError(E_CREA_COLA_MSG);
	}
	else
	{
		std::string className(abi::__cxa_demangle(typeid(*this).name(),0,0,0));
		std::ofstream outputFile(("/tmp/" + getGlobalConfiguration()->getString(const_cast<char*>("session")) + "_" + className).c_str());
		outputFile << (pid + pidOffset);
		outputFile.close();
	}
}

/// Unitializes the MouseInfoSpark component.
void MouseInfoSpark::quit(void){
	LoggerInfo("MouseInfoSpark::quit | Releasing message queue...");
	// Liberar cola de mensajes
	if(msgctl(idColaMsg, IPC_RMID,(struct msqid_ds *) NULL)<0)
		getError(E_LIB_COLA_MSG);
}

//IThreadProc implementation
void MouseInfoSpark::process(){
	// Waiting for messages
	memset(receivedMessage.contenido,'\0',sizeof(receivedMessage.contenido));
	// Waiting for messages type 1
	int bytesRecibidos = msgrcv(idColaMsg, &receivedMessage, sizeof(receivedMessage.contenido), 1,0);//blocking call
	//Update mouse positions message
	if(bytesRecibidos > 0){
		mousePositions = receivedMessage.contenido;
	}
}

//IFlow implementation
void MouseInfoSpark::processData(char *msg){

}

//FrameEventSubscriber implementation
void MouseInfoSpark::notifyFrameEvent(){
	// Send mouse positions message
	//LoggerInfo("MouseInfoSpark::notifyFrameEvent | Text: %s",mousePositions.c_str());
	myFlow->processData(const_cast<char *>(mousePositions.c_str()));
}

/* Gestión de errores */
void MouseInfoSpark::getError(int err){
	switch (err) {
		case E_CREA_COLA_MSG:
			LoggerError("MouseInfoSpark> Al tratar de liberar la cola de mensajes.");
			ERR("<ERROR==> MouseInfoSpark> No se puede crear la cola de mensajes.\n");

			exit(-1);
			break;

		case E_ENV_COLA_MSG:
			LoggerError("MouseInfoSpark> Al tratar de liberar la cola de mensajes.");
			ERR("<ERROR==> MouseInfoSpark> l enviar mensaje de respuesta a cola.\n");

			exit(-1);
			break;
		case E_LIB_COLA_MSG:
			LoggerError("MouseInfoSpark> Al tratar de liberar la cola de mensajes.");
			ERR("<ERROR==> MouseInfoSpark> Al tratar de liberar la cola de mensajes.\n");

			exit(-1);
			break;
	}
}
