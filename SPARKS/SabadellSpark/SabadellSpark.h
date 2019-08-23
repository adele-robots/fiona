#ifndef __SabadellSpark_H
#define __SabadellSpark_H

#include "Component.h"
#include "IFlow.h"
/*extern "C" {
	#include <oauth.h>
}*/
#include "curlpp/cURLpp.hpp"
#include "curlpp/Easy.hpp"
#include "curlpp/Options.hpp"
#include <curlpp/Exception.hpp>
#include <curlpp/Infos.hpp>

class SabadellSpark :
	public Component,
	public IFlow<Json::Value*>
{

	public:
		SabadellSpark(char *instanceName,
			ComponentSystem *cs) : Component(instanceName, cs) {}

	public:
		//Mandatory
		void init(void);
		void quit(void);

		//IFlow<Json::Value *> implementation
		void processData(Json::Value*);

	private:
		IFlow<char* >* myFlowChar;

		string USUARIO_LOGIN;
		string PIN;
		string CLIENT_ID;
		string CLIENT_SECRET;
		string urlSinFirmar;
		string urlFirmada;
		string state;
		string code;
		string token;
		string refresh_token;
		string cookie;
		string json;
		string datos;
		Json::Value ids;
		string numPersOrdenante;
		string idFiscalOrdenante;
		string cuentaOrdenante;
		string cert;

	private:
		Json::Value getBankInfo(Json::Value);
		void irALogin(string);
		void hacerLogin();
		string autorizarAplicacion(string);
		string obtenerToken(string);
		Json::Value realizarConsulta(string, Json::Value);
		void obtenerIds();
		void obtenerOrdenante(string, string);
		string obtenerProducto(string, string, string);
		string obtenerNumeroProducto(string, string);
		string obtenerIdTarjeta(string, string);
		string reemplazarId(string, string, string);
		void cambiarNumero(string&, bool esp = true);
		void refreshToken();
		bool compareStr(string, string);
		size_t WriteCallback		(char* ptr, size_t size, size_t nmemb);
		size_t idsCallback			(char* ptr, size_t size, size_t nmemb);
		size_t ordenantesCallback	(char* ptr, size_t size, size_t nmemb);



		void initializeRequiredInterfaces() {
			requestRequiredInterface<IFlow<char*> >(&myFlowChar);
		}

};

#endif
