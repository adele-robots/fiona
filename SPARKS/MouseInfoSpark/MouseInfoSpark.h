/*
 * MouseInfoSpark.h
 *
 *  Created on: 12/11/2013
 *      Author: guille
 */

/// @file MouseInfoSpark.h
/// @brief Component MouseInfoSpark main class.


#ifndef __MouseInfoSpark_H
#define __MouseInfoSpark_H

// Includes mecanismos IPC
#include <sys/ipc.h>
#include <sys/msg.h>

#include <algorithm>
#include <iterator>
#include <stdexcept>

#include "Component.h"

#include "IFlow.h"
#include "IThreadProc.h"
#include "FrameEventSubscriber.h"

// Mensajes a intercambiar en la aplicación:
// - Tipo del mensaje (PID_proceso, 1)
// - Contenido
typedef struct{
	long int tipo;
	char contenido[1024];
} mensaje;

/// @brief This is the main class for component MouseInfoSpark.
///
/// 

class MouseInfoSpark :
	public Component,

	public IFlow<char*>,
	public FrameEventSubscriber,
	public IThreadProc
{
public:
		MouseInfoSpark(
				char *instanceName,
				ComponentSystem *cs
		) : Component(instanceName, cs)
		{}
		virtual ~MouseInfoSpark() {};

	IFlow<char*> *myFlow;

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
	
	//FrameEventSubscriber
	void notifyFrameEvent();

private:
	int pid;
	string mousePositions;
private:
	//Put class private methods here
	void recibirMensaje(IFlow<char*> *flow);
	/* Gestión de errores */
	void getError(int error);
	
};

#endif
