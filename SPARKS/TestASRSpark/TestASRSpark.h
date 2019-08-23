/// @file TestASRSpark.h
/// @brief Component TestASRSpark main class.


#ifndef __TestASRSpark_H
#define __TestASRSpark_H

#include <iostream>
using namespace std;


#include "Component.h"
#include "IApplication.h"
#include "IAsyncFatalErrorHandler.h"
#include "IVoice.h"


#include "IAudioConsumer.h"


// Include para pocketsphinx
#include <pocketsphinx.h>

// Mecanismos IPC
#include <sys/ipc.h>
#include <sys/msg.h>
#include <sys/types.h>


// Mensajes a intercambiar en la aplicación:
// - Tipo del mensaje (PID_proceso, 1)
// - Contenido
typedef struct{
	long int tipo;
	char contenido[1024];
} mensaje;

/// @brief This is the main class for component TestASRSpark.
///
/// 

class TestASRSpark :
	public Component,
	//**Component class must extend provided interfaces**
	public IApplication,
	public IAsyncFatalErrorHandler,
	public IVoice
{
public:
		TestASRSpark(
				char *instanceName,
				ComponentSystem *cs
		) : Component(instanceName, cs)
		{}

private:

	IAudioConsumer *myAudioConsumer;

	void initializeRequiredInterfaces() {	
		requestRequiredInterface<IAudioConsumer>(&myAudioConsumer);
	}

public:
	//Mandatory
	void init(void);
	void quit(void);
	
	//Implementation of IApplication
	void run(void);

	// IAsyncFatalErrorHandler implementation
	void handleError(char*);
	
	// IVoice implementation
	void sayThis(char *prompt);
	void waitEndOfSpeech(void);
	void stopSpeech(void);

private:
	//Put class attributes here

private:
	//Put class private methods here
	void sleep_msec(int32 ms);
	
};

#endif
