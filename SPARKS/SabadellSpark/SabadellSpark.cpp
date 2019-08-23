/**
 *
 * @author Jandro
 * @version 0.1 02/02/2015
 */

#include <iostream>
#include <fstream>
#include <string>
#include "SabadellSpark.h"
#include "jsoncpp/value.h"
#include "jsoncpp/reader.h"
#include "jsoncpp/writer.h"
#include "base64.h"
#include <sstream>
#include <algorithm>
#include <locale>

using namespace std;

#ifdef _WIN32
#else
extern "C" Component *createComponent(char *componentInstanceName,
		char *componentType, ComponentSystem *componentSystem) {
	if (!strcmp(componentType, "SabadellSpark")) {
		return new SabadellSpark(componentInstanceName,
				componentSystem);
	} else {
		return NULL;
	}
}
#endif

void SabadellSpark::init(void)
{
	USUARIO_LOGIN = getComponentConfiguration()->getString(const_cast<char*>("Usuario"));
	PIN = getComponentConfiguration()->getString(const_cast<char*>("Pin"));
	CLIENT_ID = "CLI2276814615768hmdrkrafzsefkkfdnqafftqrlgwdgvbrxojtpjdh";
	CLIENT_SECRET = "C4ligul4s";
	srand(time(0));
	state = "";
	std::stringstream ss;
	do {
		ss << random();
	} while(ss.str().size() < 16);
	ss >> state;
	urlSinFirmar = "https://developers.bancsabadell.com/AuthServerBS/oauth/authorize?client_id=" + CLIENT_ID + "&response_type=code&state=" + state + "&redirect_uri=https://developers.bancsabadell.com/AuthServerBS/oauth/token&scope=read%20contable";
	token = "";
	cookie = getGlobalConfiguration()->getUserDir() + "/SabadellSpark/UserFiles/" + state;
	//LoggerInfo("Cookie = %s", cookie.c_str());
	ids.clear();
	cuentaOrdenante.clear();
	numPersOrdenante.clear();
	idFiscalOrdenante.clear();
	cert = getGlobalConfiguration()->getUserDir() + "/SabadellSpark/UserFiles/VeriSign Class 3 International Server CA - G3.crt";

}

void SabadellSpark::quit(void) {
	// Eliminamos la cookie que creamos al iniciar el Spark
	remove(cookie.c_str());
}

void SabadellSpark::processData(Json::Value* messageJSON)
{
	// Obtenemos la informacion a través de la API
	Json::Value APIReply = getBankInfo(*messageJSON);

	// Si está vacía es que no se encontró un producto (cuenta, tarjeta) terminado en las 4 cifras proporcionadas
	// Devolvemos el error NOTFOUND para que lo trate Rebecca
	if(APIReply.empty()) {
		APIReply["id"] = (*messageJSON).get("id", "").asString();
		APIReply["type"] = "bank";
		APIReply["response"] = "NOTFOUND";
		string out = Json::FastWriter().write(APIReply);
		myFlowChar->processData(const_cast<char*>(out.c_str()));
		return;
	}

	// Si el token ha expirado
	else if(APIReply.get("error_description", "").asString().find("Access token expired") != string::npos) {
		refreshToken();
		APIReply = getBankInfo(*messageJSON);
	}

	// Si el token es invalido
	else if(APIReply.get("error_description", "").asString().find("Invalid access token") != string::npos) {
		token.clear();
		APIReply = getBankInfo(*messageJSON);
	}

	// Tenemos una respuesta válida
	Json::Value response;

	// Metemos el id y tipo para que TagProcessor pueda hacer su trabajo
	response["id"] = (*messageJSON).get("id", "").asString();
	response["type"] = "bank";

	// Obtenemos el servicio que se solicitó
	string action = (*messageJSON).get("action", "").asString();

	// Si se pidio el saldo
	if(action == "balance") {

		Json::Value::ArrayIndex numProd = 0;

		// Encontramos el ArrayIndex del producto que buscamos
		for(Json::ValueIterator it = APIReply["data"].begin(); it != APIReply["data"].end(); it++) {
			if(compareStr((*messageJSON).get("product", "").asString(), (*it).get("descripcion", "").asString())) {
				string idc = (*it).get("numeroProductoCodificado", "").asString();
				if(idc.substr(idc.size() - 4) == (*messageJSON).get("digits", "").asString()) {
					break;
				}
			}
			numProd++;
		}

		// Obtenemos su saldo
		string balance = APIReply["data"][numProd].get("balance", "").asString();
		cambiarNumero(balance);
		response["response"] = "[balance] " + balance;

		// Si no se encontro el producto
		if(balance == "")
			response["response"] = "NOTFOUND";
	}

	// Se pidio el saldo disponible de la tarjeta
	else if(action == "card_remain") {
		string remain = APIReply["data"].get("saldoDisponible", "").asString();
		cambiarNumero(remain);
		response["response"] = "[card_remain] " + remain;

		// Si no se encontro el producto
		if(remain == "")
			response["response"] = "NOTFOUND";
	}

	// Se pidio el saldo gastado de la tarjeta
	else if(action == "card_spent") {
		string spent = APIReply["data"].get("saldoDispuesto", "").asString();
		cambiarNumero(spent);
		response["response"] = "[card_spent] " + spent;

		// Si no se encontro el producto
		if(spent == "")
			response["response"] = "NOTFOUND";
	}

	// Se pidio la cantidad que se va a cargar en la tarjeta
	else if(action == "card_charge") {
		string charge = APIReply["data"].get("totalOperPendLiquiActual", "").asString();
		cambiarNumero(charge);
		response["response"] = "[card_charge] " + charge;

		// Si no se encontro el producto
		if(charge == "")
			response["response"] = "NOTFOUND";
	}

	// Se pidio hacer una transferencia
	else if(action == "transfer") {
		string transfer;
		if(APIReply["head"].get("errorCode", "").asString() != "")
			transfer = APIReply["head"].get("descripcionError", "ERROR").asString();
		else {
			string com = APIReply["data"]["datosOperacion"]["comisionesTransf"].get("totalComygastEur", "0.00").asString();
			string::size_type pos = com.find(".");
			while(pos != string::npos) {
				com = com.replace(pos, 1, "");
				pos = com.find(".");
			}
			pos = com.find(",");
			if(pos != string::npos) {
				com = com.replace(pos, 1, ".");
			}
			double comisiones = atof(com.c_str());
			//LoggerInfo("Comisiones: %f %s", comisiones, APIReply["data"]["datosOperacion"]["comisionesTransf"].get("totalComygastEur", "0.00").asCString());
			char c [23];
			snprintf(c, 100, "%20.2f", comisiones);
			transfer = c;
			cambiarNumero(transfer, false);
			//LoggerInfo("%s%s", APIReply["data"].get("url", "").asCString(), APIReply["data"].get("codigo", "").asCString());
		}
		response["response"] = "[transfer] " + transfer;
	}

	// Se pidio hacer una operacion de Instant Money
	else if(action == "instantmoney") {
		string instantmoney;
		if(APIReply["head"].get("errorCode", "").asString() != "")
			instantmoney = APIReply["head"].get("descripcionError", "ERROR").asString();
		else {
			Json::Value v = APIReply["data"]["datosOperacion"]["comisiones"];
			double comisiones = 0;

			for(Json::ValueIterator it = v.begin(); it != v.end(); it++) {
				string com = (*it).get("importe", "").asString();
				string::size_type pos = com.find(".");
				while(pos != string::npos) {
					com = com.replace(pos, 1, "");
					pos = com.find(".");
				}
				pos = com.find(",");
				if(pos != string::npos) {
					com = com.replace(pos, 1, ".");
				}
				comisiones += atof(com.c_str());
			}
			char c [23];
			snprintf(c, 100, "%20.2f", comisiones);
			instantmoney = c;
			cambiarNumero(instantmoney, false);
		}
		response["response"] = "[instantmoney] " + instantmoney;
	}


	//LoggerInfo("API salida: %s", APIReply.toStyledString().c_str());
	//LoggerInfo("Spark salida: %s", response.toStyledString().c_str());

	// Convertimos a char* y devolvemos la información
	string out = Json::FastWriter().write(response);
	myFlowChar->processData(const_cast<char*>(out.c_str()));
}

void SabadellSpark::cambiarNumero(string& numero, bool esp) {
	if(esp) {
		// Sustituimos el numero de la forma XX.XXX,YY a XXXXX YY para que lo pueda tratar Rebecca
		string::size_type pos = numero.find(".");
		if(pos != string::npos) {
			numero = numero.replace(pos, 1, "");
		}
		pos = numero.find(",");
		if(pos != string::npos) {
			numero = numero.replace(pos, 1, " ");
		}
	}
	else {
		// Sustituimos el numero de la forma XXXXX.YY a XXXXX YY para que lo pueda tratar Rebecca
		string::size_type pos = numero.find(" ");
		while(pos != string::npos) {
			numero = numero.replace(pos, 1, "");
			pos = numero.find(" ");
		}
		pos = numero.find(".");
		if(pos != string::npos) {
			numero = numero.replace(pos, 1, " ");
		}
	}
}

Json::Value SabadellSpark::getBankInfo(Json::Value jsonValue) {
	// Si no tenemos token con la que hacer la peticion necesitamos una
	if(token.empty()) {
		irALogin(urlSinFirmar);
		hacerLogin();
		code = autorizarAplicacion(urlSinFirmar);
		token = obtenerToken(code);
	}

	// Una vez tenemos el token procedemos a hacer la consulta
	Json::Value datos = realizarConsulta(token, jsonValue);

	return datos;
}

void SabadellSpark::irALogin(string url) {
	try {
		curlpp::Cleanup cleaner;
		curlpp::Easy request;
		request.setOpt(new curlpp::options::Url(url));
		request.setOpt(new curlpp::options::CookieJar(cookie));
		request.setOpt(new curlpp::options::CaInfo(cert));
		request.setOpt(new curlpp::options::FollowLocation(true));

		request.perform();
	}
	catch(curlpp::LogicError& e )
	{
		LoggerError("Error making request 1a : %s", e.what());
	}
	catch(curlpp::RuntimeError& e)
	{
		LoggerError("Error making request 1b : %s", e.what());
	}
}

void SabadellSpark::hacerLogin() {
	string url = "https://developers.bancsabadell.com/AuthServerBS/j_spring_security_check";
	try {
		curlpp::Cleanup cleaner;
		curlpp::Easy request;
		string parameters = "j_username=" + USUARIO_LOGIN + "&j_password=" + PIN + "&j_product=apibs.authserver.login.producto.particular.procedencia&requestId=JS&j_acceso=ID_NIF&j_client_id=" + CLIENT_ID;
		request.setOpt(new curlpp::options::Url(url));
		request.setOpt(new curlpp::options::CookieJar(cookie));
		request.setOpt(new curlpp::options::CookieFile(cookie));
		request.setOpt(new curlpp::options::CaInfo(cert));
		request.setOpt(new curlpp::options::FollowLocation(true));
		std::list<std::string> headers;
		headers.push_back("Referer: https://developers.bancsabadell.com/AuthServerBS/login");
		headers.push_back("Content-Type: application/x-www-form-urlencoded");
		request.setOpt(new curlpp::options::HttpHeader(headers));
        request.setOpt(new curlpp::options::PostFields(parameters));

		request.perform();
	}
	catch(curlpp::LogicError& e )
	{
		LoggerError("Error making request 2a : %s", e.what());
	}
	catch(curlpp::RuntimeError& e)
	{
		LoggerError("Error making request 2b : %s", e.what());
	}
}

string SabadellSpark::autorizarAplicacion(string url) {
	try {
		curlpp::Cleanup cleaner;
		curlpp::Easy request;
		string parameters = "user_oauth_approval=true";
		request.setOpt(new curlpp::options::Url(url));
		request.setOpt(new curlpp::options::CookieJar(cookie));
		request.setOpt(new curlpp::options::CookieFile(cookie));
		request.setOpt(new curlpp::options::CaInfo(cert));
		request.setOpt(new curlpp::options::FollowLocation(true));
		std::list<std::string> headers;
		string cadena = CLIENT_ID + ":" + CLIENT_SECRET;
		string b64str = base64_encode(reinterpret_cast<const unsigned char *>(cadena.c_str()), cadena.size());
		headers.push_back("Authorization: Basic " + b64str);
		headers.push_back("Content-Type: application/x-www-form-urlencoded");
		request.setOpt(new curlpp::options::HttpHeader(headers));
        request.setOpt(new curlpp::options::PostFields(parameters));

		request.perform();
		curlpp::infos::EffectiveUrl effUrl;
		std::string effective_url = effUrl.get(request);
		//LoggerInfo("%s", effective_url.c_str());
		code = effective_url.substr(effective_url.find("code=") + 5, effective_url.find("&",effective_url.find("code=")) - (effective_url.find("code=") + 5));
		LoggerInfo("[SabadellSpark] code  = %s", code.c_str());
	}
	catch(curlpp::LogicError& e )
	{
		LoggerError("Error making request 3a : %s", e.what());
	}
	catch(curlpp::RuntimeError& e)
	{
		LoggerError("Error making request 3b : %s", e.what());
	}

	return code;
}

string SabadellSpark::obtenerToken(string code) {
	string url = "https://developers.bancsabadell.com/AuthServerBS/oauth/token";
	try {
		curlpp::Cleanup cleaner;
		curlpp::Easy request;
		curlpp::types::WriteFunctionFunctor functor(this, &WriteCallback);
		curlpp::options::WriteFunction *callback = new curlpp::options::WriteFunction(functor);
		request.setOpt(callback);
		string parameters = "grant_type=authorization_code&code=" + code + "&redirect_uri=https://developers.bancsabadell.com/AuthServerBS/oauth/token";
		request.setOpt(new curlpp::options::Url(url));
		request.setOpt(new curlpp::options::CookieJar(cookie));
		request.setOpt(new curlpp::options::CookieFile(cookie));
		request.setOpt(new curlpp::options::CaInfo(cert));
		std::list<std::string> headers;
		string cadena = CLIENT_ID + ":" + CLIENT_SECRET;
		string b64str = base64_encode(reinterpret_cast<const unsigned char *>(cadena.c_str()), cadena.size());
		headers.push_back("Authorization: Basic " + b64str);
		headers.push_back("Content-Type: application/x-www-form-urlencoded");
		headers.push_back("Accept: application/json");
		request.setOpt(new curlpp::options::HttpHeader(headers));
        request.setOpt(new curlpp::options::PostFields(parameters));

		request.perform();
	}
	catch(curlpp::LogicError& e )
	{
		LoggerError("Error making request 4a : %s", e.what());
	}
	catch(curlpp::RuntimeError& e)
	{
		LoggerError("Error making request 4b : %s", e.what());
	}

	return token;
}

Json::Value SabadellSpark::realizarConsulta(string token, Json::Value data) {

	// Si no tenemos los productos los obtenemos
	if(ids.empty())
		obtenerIds();

	string consulta = data.get("service", "").asString();

	// Si lo que se pidio fue el saldo la información está en esa estructura
	if(consulta == "productos")
		return ids;

	// si se quiere hacer una transferencia primero necesitamos informacion sobre el ordenante
	if(data.get("action", "").asString() == "transfer") {
		if(cuentaOrdenante.empty() || numPersOrdenante.empty() || idFiscalOrdenante.empty())
				obtenerOrdenante(data.get("product", "").asString(), data.get("digits", "").asString());
		cuentaOrdenante = obtenerNumeroProducto(data.get("product", "").asString(), data.get("digits", "").asString());

		if(cuentaOrdenante.empty()) {
			Json::Value JSONresponseContent;
			return JSONresponseContent;
		}
	}

	// Reemplazamos la cadena {cardNumber} por su id
	try {
		consulta = reemplazarId(consulta, data.get("product", "").asString(), data.get("digits", "").asString());
	} catch(Exception &e) {
		LoggerWarn("%s", e.msg);
		Json::Value error;
		return error;
	}

	// Hacemos la llamada a la API
	string url = "https://developers.bancsabadell.com/ResourcesServerBS/oauthservices/v1.0.0/" + consulta;

	try {
		curlpp::Cleanup cleaner;
		curlpp::Easy request;
		curlpp::types::WriteFunctionFunctor functor(this, &WriteCallback);
		curlpp::options::WriteFunction *callback = new curlpp::options::WriteFunction(functor);
		request.setOpt(callback);
		request.setOpt(new curlpp::options::Url(url));
		request.setOpt(new curlpp::options::CookieJar(cookie));
		request.setOpt(new curlpp::options::CookieFile(cookie));
		request.setOpt(new curlpp::options::CaInfo(cert));
		std::list<std::string> headers;
		headers.push_back("Authorization: Bearer " + token);
		string parameters;

		// Si es una transferencia necesitamos pasar datos adicionales por POST
		if(data.get("action", "").asString() == "transfer") {
			Json::Value datos;
			datos["cuentaBeneficiario"] = data.get("to", "").asString();
			datos["nombreBeneficiario"] = data.get("to", "").asString();
			datos["cuentaOrdenante"] = cuentaOrdenante;
			datos["numPersOrdenante"] = numPersOrdenante;
			datos["idFiscalOrdenante"] = idFiscalOrdenante;
			datos["importeTransferencia"] = data.get("amount", "").asString();
			datos["concepto"] = data.get("concept", "Transference powered by FIONA").asString();
			parameters = Json::FastWriter().write(datos);
	        request.setOpt(new curlpp::options::PostFields(parameters));
			headers.push_back("Content-Type: application/json");
		}
		// Si es una accion de Instant Money necesitamos pasar datos adicionales por POST
		else if(data.get("action", "").asString() == "instantmoney") {

			cuentaOrdenante = obtenerNumeroProducto(data.get("product", "").asString(), data.get("digits", "").asString());

			Json::Value datos;
			datos["idTarj"] = cuentaOrdenante;
			datos["telfMovil"] = data.get("phone", "").asString();
			datos["importe"] = data.get("amount", "").asString();
			datos["concepto"] = data.get("concept", "Transference powered by FIONA").asString();
			parameters = Json::FastWriter().write(datos);
	        request.setOpt(new curlpp::options::PostFields(parameters));
			headers.push_back("Content-Type: application/json");
		}
		request.setOpt(new curlpp::options::HttpHeader(headers));

        //LoggerInfo("URL:\n%s", url.c_str());
        //LoggerInfo("Parametros:\n%s", parameters.c_str());

		request.perform();
	}
	catch(curlpp::LogicError& e )
	{
		LoggerError("Error making request 5a : %s", e.what());
	}
	catch(curlpp::RuntimeError& e)
	{
		LoggerError("Error making request 5b : %s", e.what());
	}

	Json::Value JSONresponseContent;

	Json::Reader().parse(datos, JSONresponseContent);

	//LoggerInfo("%s", datos.c_str());

	return JSONresponseContent;
}

void SabadellSpark::obtenerIds() {
	string url = "https://developers.bancsabadell.com/ResourcesServerBS/oauthservices/v1.0.0/productos";
	try {
		curlpp::Cleanup cleaner;
		curlpp::Easy request;
		curlpp::types::WriteFunctionFunctor functor(this, &idsCallback);
		curlpp::options::WriteFunction *callback = new curlpp::options::WriteFunction(functor);
		request.setOpt(callback);
		request.setOpt(new curlpp::options::Url(url));
		request.setOpt(new curlpp::options::CookieJar(cookie));
		request.setOpt(new curlpp::options::CookieFile(cookie));
		request.setOpt(new curlpp::options::CaInfo(cert));
		std::list<std::string> headers;
		headers.push_back("Authorization: Bearer " + token);
		request.setOpt(new curlpp::options::HttpHeader(headers));

		request.perform();
	}
	catch(curlpp::LogicError& e )
	{
		LoggerError("Error making request 5a : %s", e.what());
	}
	catch(curlpp::RuntimeError& e)
	{
		LoggerError("Error making request 5b : %s", e.what());
	}
}

void SabadellSpark::obtenerOrdenante(string producto, string cifras) {
	cuentaOrdenante = obtenerNumeroProducto(producto, cifras);

	string url = "https://developers.bancsabadell.com/ResourcesServerBS/oauthservices/v1.0.0/transferencias/nacional/" + cuentaOrdenante + "/ordenantes";
	//LoggerInfo("URL = %s", url.c_str());
	try {
		curlpp::Cleanup cleaner;
		curlpp::Easy request;
		curlpp::types::WriteFunctionFunctor functor(this, &ordenantesCallback);
		curlpp::options::WriteFunction *callback = new curlpp::options::WriteFunction(functor);
		request.setOpt(callback);
		request.setOpt(new curlpp::options::Url(url));
		request.setOpt(new curlpp::options::CookieJar(cookie));
		request.setOpt(new curlpp::options::CookieFile(cookie));
		request.setOpt(new curlpp::options::CaInfo(cert));
		std::list<std::string> headers;
		headers.push_back("Authorization: Bearer " + token);
		request.setOpt(new curlpp::options::HttpHeader(headers));

		request.perform();
	}
	catch(curlpp::LogicError& e )
	{
		LoggerError("Error making request 5a : %s", e.what());
	}
	catch(curlpp::RuntimeError& e)
	{
		LoggerError("Error making request 5b : %s", e.what());
	}
}

void SabadellSpark::refreshToken() {
	string url = "https://developers.bancsabadell.com/AuthServerBS/oauth/token";
	try {
		curlpp::Cleanup cleaner;
		curlpp::Easy request;
		curlpp::types::WriteFunctionFunctor functor(this, &WriteCallback);
		curlpp::options::WriteFunction *callback = new curlpp::options::WriteFunction(functor);
		request.setOpt(callback);
		string parameters = "grant_type=refresh_token&refresh_token=" + refresh_token + "&redirect_uri=https://developers.bancsabadell.com/AuthServerBS/oauth/token";
		request.setOpt(new curlpp::options::Url(url));
		request.setOpt(new curlpp::options::CookieJar(cookie));
		request.setOpt(new curlpp::options::CookieFile(cookie));
		request.setOpt(new curlpp::options::CaInfo(cert));
		std::list<std::string> headers;
		string cadena = CLIENT_ID + ":" + CLIENT_SECRET;
		string b64str = base64_encode(reinterpret_cast<const unsigned char *>(cadena.c_str()), cadena.size());
		headers.push_back("Authorization: Basic " + b64str);
		headers.push_back("Content-Type: application/x-www-form-urlencoded");
		headers.push_back("Accept: application/json");
		request.setOpt(new curlpp::options::HttpHeader(headers));
        request.setOpt(new curlpp::options::PostFields(parameters));

		request.perform();
	}
	catch(curlpp::LogicError& e )
	{
		LoggerError("Error making request 4a : %s", e.what());
	}
	catch(curlpp::RuntimeError& e)
	{
		LoggerError("Error making request 4b : %s", e.what());
	}
}

size_t SabadellSpark::WriteCallback(char* ptr, size_t size, size_t nmemb)
{
	// Calculate the real size of the incoming buffer
	size_t realsize = size * nmemb;

	string out(ptr);

	Json::Value JSONresponseContent;

	json += out;

	//LoggerInfo("[SabadellSparkW] %s", json.c_str());

		if(Json::Reader().parse(json, JSONresponseContent)) {
			if(JSONresponseContent.get("access_token", "").asString() != "") {
				token = JSONresponseContent.get("access_token", token).asString();
				refresh_token = JSONresponseContent.get("refresh_token", token).asString();
				LoggerInfo("[SabadellSpark] token = %s", token.c_str());
			}
			else {
				datos = json;
			}
			//LoggerInfo("[SabadellSpark] %s", JSONresponseContent.toStyledString().c_str());
			json.clear();
		}


	// return the real size of the buffer...
	return realsize;
}

size_t SabadellSpark::idsCallback(char* ptr, size_t size, size_t nmemb)
{
	// Calculate the real size of the incoming buffer
	size_t realsize = size * nmemb;

	//LoggerInfo("[SabadellSpark] %s", ptr);

	string out(ptr);

	Json::Value JSONresponseContent;

	json += out;

	//LoggerInfo("[SabadellSparkI] %s", json.c_str());

	//LoggerInfo("%s", json.c_str());
		if(Json::Reader().parse(json, JSONresponseContent)) {
			ids = JSONresponseContent;
			json.clear();
		}


	// return the real size of the buffer...
	return realsize;
}

size_t SabadellSpark::ordenantesCallback(char* ptr, size_t size, size_t nmemb)
{
	// Calculate the real size of the incoming buffer
	size_t realsize = size * nmemb;

	//LoggerInfo("[SabadellSpark] %s", ptr);

	string out(ptr);

	Json::Value JSONresponseContent;

	json += out;

	//LoggerInfo("[SabadellSparkO] %s", json.c_str());

	//LoggerInfo("%s", json.c_str());
		if(Json::Reader().parse(json, JSONresponseContent)) {
			numPersOrdenante = JSONresponseContent["data"][0u].get("numPers", "").asString();
			idFiscalOrdenante = JSONresponseContent["data"][0u].get("nif", "").asString();
			json.clear();
		}


	// return the real size of the buffer...
	return realsize;
}

string SabadellSpark::reemplazarId(string consulta, string producto, string cifras) {

	// Solo sustituimos los servicios que contengan esta cadena
	if(consulta.find("{cardNumber}") == string::npos)
		return consulta;

	string salida;

	//LoggerInfo("%s", ids.toStyledString().c_str());
	for(Json::ValueIterator it = ids["data"].begin(); it != ids["data"].end(); it++) {
		string descripcion = (*it).get("descripcion", "").asString();
		if(compareStr(producto, descripcion)) {
			// El producto es de tipo tarjeta
			if((*it).get("producto", "").asString() == "TC") {
				// Obtenemos su numero codificado
				string idc = (*it).get("numeroProductoCodificado", "").asString();
				// Si coinciden las 4 ultimas cifras con las proporcionadas
				if(idc.substr(idc.size() - 4) == cifras) {
					// Obtenemos el id con el que hacer las llamadas a la api
					string id = (*it).get("numeroProducto", "").asString();
					// Y sustituimos en la cadena del servicio
					salida = consulta.replace(consulta.find("{cardNumber}"), 12, id);
					return salida;
				}
			}
		}
	}

	// Si la salida está vacía es porque no se encontraron coincidencias
	if(salida.empty())
		throw Exception(const_cast<char*>("No matches"));
	return salida;
}

string SabadellSpark::obtenerProducto(string producto, string cifras, string campo) {
	for(Json::ValueIterator it = ids["data"].begin(); it != ids["data"].end(); it++) {
		if(compareStr(producto, (*it).get("descripcion", "").asString())) {
			string idc = (*it).get("numeroProductoCodificado", "").asString();
			if(idc.substr(idc.size() - 4) == cifras) {
				return (*it).get(campo, "").asString();
			}
		}
	}
	return "";
}

string SabadellSpark::obtenerNumeroProducto(string producto, string cifras) {
	return obtenerProducto(producto, cifras, "numeroProducto");
}

string SabadellSpark::obtenerIdTarjeta(string producto, string cifras) {
	return obtenerProducto(producto, cifras, "idTarjeta");
}

bool SabadellSpark::compareStr(string str1, string str2) {
	 std::transform(str1.begin(), str1.end(), str1.begin(), ::tolower);
	 std::transform(str2.begin(), str2.end(), str2.begin(), ::tolower);
	 return str1 == str2;
}
