/// @file CookieSpark.cpp
/// @brief CookieSpark class implementation.


#include <list>
#include <unordered_set>
#include <ctime>
#include <algorithm>
#include "CookieSpark.h"
#include "jsoncpp/value.h"
#include "jsoncpp/reader.h"
#include "jsoncpp/writer.h"
#include "curlpp/cURLpp.hpp"
#include "curlpp/Easy.hpp"
#include "curlpp/Options.hpp"

// Includes mecanismos IPC
#include <sys/ipc.h>
#include <sys/msg.h>
#include <signal.h>
#include <typeinfo>
#include <cxxabi.h>

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
	if (!strcmp(componentType, "CookieSpark")) {
			return new CookieSpark(componentInstanceName, componentSystem);
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

void CookieSpark::signalHandler(int sig){
	//LoggerInfo("Se ha capturado la señal sig en ChatSpark");

	// Liberar cola de mensajes
	msgctl(idColaMsg, IPC_RMID,(struct msqid_ds *)NULL);

	// Asignar a esa señal el manejador por defecto
	signal(sig,SIG_DFL);

	// Volver a lanzar la señal de terminación
	raise(sig);

}

void CookieSpark::init(void){
	path.clear();
	realUser.clear();

    string session = getGlobalConfiguration()->getString(const_cast<char*>("session"));
	// Inicializar cola de Mensajes
	// Pid del proceso que se usará como clave IPC
	pid = getpid() << 5;

	int pidOffset = getComponentConfiguration()->getInt(const_cast<char*>("Pid_Offset"));

	idColaMsg = msgget(pid + pidOffset, PERMISOS|IPC_CREAT);
	LoggerInfo("[CookieSpark] key = %d idCola = %d", pid + pidOffset, idColaMsg);

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
}

void CookieSpark::quit(void){
	if(!path.empty()) {
		/*file.open(path.c_str(), std::fstream::out | std::fstream::app);
		file.close();
		file.open(path.c_str(), std::fstream::in | std::fstream::out | std::fstream::app);*/
		file.open(path.c_str(), std::fstream::out);

		for(map<string, string>::iterator it = cookies.begin(); it != cookies.end(); it++) {
			file << Cookie(it->first, it->second) << std::endl;
		}

		file.close();
	}

	// Liberar cola de mensajes
	if(msgctl(idColaMsg, IPC_RMID,(struct msqid_ds *) NULL)<0)
		getError(E_LIB_COLA_MSG);
}


void CookieSpark::processData(Json::Value* messageJSON) {

	string action = (*messageJSON).get("action", "").asString();
	string key = (*messageJSON).get("key", "").asString();
	string value = (*messageJSON).get("value", "").asString();

	/*if( action == "setInitialCookie") {
		vector<string> v = tokenize(value, ";");
		for(uint i = 0; i < v.size(); i++) {
			setCookie(v[i]);
		}
		_fioUser = cookies.find("User")->second;
		path = getComponentConfiguration()->getUserDir() + "/CookieSpark/UserFiles/" + _fioUser;
		loadMapFromFile();

		Json::Value APIReply;
		APIReply["id"] = (*messageJSON).get("id", "").asString();
		APIReply["type"] = "cookie";
		APIReply["response"] = action;

		string out = Json::FastWriter().write(APIReply);
		myFlowChar->processData(const_cast<char*>(out.c_str()));
	}
	else */if(action == "setCookie") {
		setCookie(key, value);

		Json::Value APIReply;
		APIReply["id"] = (*messageJSON).get("id", "").asString();
		APIReply["type"] = "cookie";
		APIReply["response"] = action + " " + key + " " + value;

		string out = Json::FastWriter().write(APIReply);
		myFlowChar->processData(const_cast<char*>(out.c_str()));
	}
	else if(action == "getCookie") {

		Json::Value APIReply;
		APIReply["id"] = (*messageJSON).get("id", "").asString();
		APIReply["type"] = "cookie";
		APIReply["response"] = action + " " + key + " " + getCookie(key);

		string out = Json::FastWriter().write(APIReply);
		myFlowChar->processData(const_cast<char*>(out.c_str()));
	}
}

void CookieSpark::processData(char* username) {
	realUser = string(username);
}

void  CookieSpark::setCookie(string keyValue) {
	Cookie cookie(keyValue);
	setCookie(cookie.getKey(), cookie.getValue());
}

void  CookieSpark::setCookie(string key, string value) {
	cookies[key] = value;

	if(!path.empty()) {
		file.open(path.c_str(), std::fstream::out);

		for(map<string, string>::iterator it = cookies.begin(); it != cookies.end(); it++) {
			file << Cookie(it->first, it->second) << std::endl;
		}

		file.close();
	}
	//LoggerInfo("setCookie");
	/*if(!path.empty()) {
		LoggerInfo("path %s", path.c_str());
		file.open(path.c_str(), std::fstream::out | std::fstream::app);
		file.close();
		file.open(path.c_str(), std::fstream::in | std::fstream::out | std::fstream::app);

		if(file.is_open())
			LoggerInfo("file opened");
		else {
			LoggerInfo("file not opened");
			return;
		}

		LoggerInfo("before for");
		for(map<string, string>::iterator it = cookies.begin(); it != cookies.end(); ++it) {
			LoggerInfo("new data %s %s", it->first.c_str(), it->second.c_str());
			file << Cookie(it->first, it->second) << std::endl;
		}
		LoggerInfo("after for");

		file.close();
		LoggerInfo("file closed");
	}*/
}

string  CookieSpark::getCookie(string key) {
	if(cookies.find(key) == cookies.end())
		return "";
	return cookies[key];
}

void  CookieSpark::loadMapFromFile() {
	//LoggerInfo("loadMapFromFile");
	file.open(path.c_str(), std::fstream::out | std::fstream::app);
	file.close();
	file.open(path.c_str(), std::fstream::in);

	string s;

	while(getline(file, s)) {
		Cookie cookie(s);
		LoggerInfo("[CookieSpark] Cookie read: %s=%s", cookie.getKey().c_str(), cookie.getValue().c_str());
		if(cookies.find(cookie.getKey()) == cookies.end())
			cookies[cookie.getKey()] = cookie.getValue();
	}

	file.close();
}

vector<string> CookieSpark::tokenize(const string& str, const string& delimiters)
{
   vector<string> tokens;
   // Skip delimiters at beginning.
   string::size_type lastPos = str.find_first_not_of(delimiters, 0);
   // Find first "non-delimiter".
   string::size_type pos = str.find_first_of(delimiters, lastPos);

   while (string::npos != pos || string::npos != lastPos)
    {  // Found a token, add it to the vector.
      tokens.push_back(str.substr(lastPos, pos - lastPos));
      // Skip delimiters.  Note the "not_of"
      lastPos = str.find_first_not_of(delimiters, pos);
      // Find next "non-delimiter"
      pos = str.find_first_of(delimiters, lastPos);
   }
    return tokens;
}


//IThreadProc implementation
void CookieSpark::process() {
	//SignalHandler signalHandler;

	// Esperar la llegada de mensajes
		memset(receivedMessage.contenido,'\0',sizeof(receivedMessage.contenido));
		// Esperando mensaje de tipo 1
		int bytesRecibidos = msgrcv(idColaMsg, &receivedMessage, sizeof(receivedMessage.contenido), 1,0);//llamada bloqueante

		// Procesamiento del mensaje de entrada
		// Enviamos al gestor de diálogo la entrada
		// de texto del usuario
		if(bytesRecibidos > 0){
			string inputMsg = receivedMessage.contenido;
			vector<string> vec = tokenize(inputMsg, ";");
			for(uint i = 0; i < vec.size(); i++) {
				if(cookies.find("User") != cookies.end()) {
					_fioUser = cookies.find("User")->second;
					// TODO: si existe realUser
					//path = getComponentConfiguration()->getUserDir() + "/CookieSpark/UserFiles/" + realUser + "/" + _fioUser;
					path = getComponentConfiguration()->getString(const_cast<char*>("User_Spark_Data")) + _fioUser;
				}
				//setCookie(vec[i]);
				Cookie cookie(vec[i]);
				cookies[cookie.getKey()] = cookie.getValue();
			}
			loadMapFromFile();
		}
}



/* Gestión de errores */
void CookieSpark::getError(int err){

	switch (err) {

		case E_CREA_COLA_MSG:
			ERR("<ERROR==> CookieSpark> No se puede crear la cola de mensajes.\n");

			exit(-1);
			break;

		case E_ENV_COLA_MSG:
			ERR("<ERROR==> CookieSpark> Al enviar mensaje de respuesta a cola.\n");

			exit(-1);
			break;
		case E_LIB_COLA_MSG:
			ERR("<ERROR==> CookieSpark> Al tratar de liberar la cola de mensajes.\n");

			exit(-1);
			break;
	}

}
