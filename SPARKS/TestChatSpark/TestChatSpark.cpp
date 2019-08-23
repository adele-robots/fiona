/// @file TestChatSpark.cpp
/// @brief TestChatSpark class implementation.


#include "stdAfx.h"
#include "TestChatSpark.h"
#include <errno.h>


#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "TestChatSpark")) {
			return new TestChatSpark(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif

/// Initializes TestChatSpark component.
void TestChatSpark::init(void){

	running = true;
	errno = 0;

}

/// Unitializes the TestChatSpark component.
void TestChatSpark::quit(void){
}


//Example of provided interface implementation
//IApplication implementation
void TestChatSpark::run(void){

		mensaje mensajeUsuario;
		mensaje mensajeRecibido;
		string entrada;
		int pid = getpid();
		while(running){
			cout << "Escriba una pregunta:" << endl;
			// Recibir entrada de usuario
			char entrada[1024] ;
			cin.width(1024);
			cin.getline(entrada,1024);


			// Construimos mensaje
			mensajeUsuario.tipo = 1;
			memset(mensajeUsuario.contenido,'\0',sizeof(mensajeUsuario.contenido));
			//mensajeUsuario.contenido = entrada;
			strcpy(mensajeUsuario.contenido,entrada);

			// Recuperar cola de mensajes ya creada
			int idCola = msgget(pid,0);

			if(idCola < 0 ){
				cout << "Error al recuperar la cola de mensajes\n" << endl;
			}

			// Enviar mensaje del usuario a la cola de mensajes
			if(msgsnd(idCola,&mensajeUsuario,sizeof(mensajeUsuario.contenido),0)<0){
				cout << "Error al enviar el mensaje a la cola de mensajes\n";
				cout << "Errno value:" << errno << endl;
			}


			// Esperar respuesta
			memset(mensajeRecibido.contenido,'\0',sizeof(mensajeRecibido.contenido));
			if(msgrcv(idCola,&mensajeRecibido,sizeof(mensajeRecibido.contenido),2,0)<0){
				cout << "Error al recibir mensaje de tipo 2\n" << endl;
			}else {
				cout << "Elvira answered:" << mensajeRecibido.contenido << endl;
				if(strcmp(entrada,"/exit")==0){
					running = false;
					cout << "Exit the program" << endl;
				}
			}
		}

}

// IAsyncFatalErrorHandler implementation
void TestChatSpark::handleError(char *msg)
{
		LoggerError(msg);
}


