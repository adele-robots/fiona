/// @file CookieSpark.h
/// @brief Component WeatherSpark main class.


#ifndef __CookieSpark_H
#define __CookieSpark_H


#include "Component.h"
#include "IFlow.h"
#include <fstream>
#include "Cookie.h"
#include "IThreadProc.h"

// Mensajes a intercambiar en la aplicación:
// - Tipo del mensaje (PID_proceso, 1)
// - Contenido
typedef struct{
	long int tipo;
	char contenido[1024];
} mensaje;

/// @brief This is the main class for component WeatherSpark.
///
///

class CookieSpark :
	public Component,
	public IFlow<Json::Value*>,
	public IFlow<char*>,
	public IThreadProc
{


	public:
		CookieSpark(
				char *instanceName,
				ComponentSystem *cs
		) : Component(instanceName, cs)
		{}

	private:

		void initializeRequiredInterfaces() {
			requestRequiredInterface<IFlow<char*> >(&myFlowChar);
		}

	public:
		//Mandatory
		void init(void);
		void quit(void);

		//IFlow<Json::Value *> implementation
		void processData(Json::Value*);

		//IFlow<char *> implementation
		void processData(char*);

		// Implementation of IThreadproc
		void process();

		IFlow<char* >* myFlowChar;

	private:

		void setCookie(string);
		void setCookie(string, string);
		string getCookie(string);
		void loadMapFromFile();
		vector<string> tokenize(const string&, const string&);

		map<string, string> cookies;
		fstream file;
		string _fioUser;
		string path;
		string realUser;

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
