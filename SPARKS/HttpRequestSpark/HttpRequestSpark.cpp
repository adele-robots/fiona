/*
 * HttpRequestSpark.cpp
 *
 *  Created on: 11/06/2013
 *      Author: Alejandro
 */

/// @file HttpRequestSpark.cpp
/// @brief HttpRequestSpark class implementation.


#include "HttpRequestSpark.h"

#include "curlpp/cURLpp.hpp"
#include "curlpp/Easy.hpp"
#include "curlpp/Options.hpp"
#include <curlpp/Exception.hpp>
#include <curlpp/Infos.hpp>


#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "HttpRequestSpark")) {
			return new HttpRequestSpark(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif

bool running = true;
std::mutex m;

// Callbacks
/*void HttpRequestSpark::OnBegin( const happyhttp::Response* r, void* userdata )
{
	HttpRequestSpark * httpRequestSpark;
	httpRequestSpark = (HttpRequestSpark *) userdata;

	httpRequestSpark->count = 0;
	if(r->getstatus() != 200)
		LoggerWarn("[FIONA-logger] HttpRequestSpark BEGIN (%d %s)\n", r->getstatus(), r->getreason() );
	if(r->getstatus() == 302)
		LoggerInfo("[FIONA-logger] HttpRequestSpark Location =  %s\n", r->getheader("location") );
}

void HttpRequestSpark::OnData( const happyhttp::Response* r, void* userdata, const unsigned char* data, int n )
{
	HttpRequestSpark * httpRequestSpark;
	httpRequestSpark = (HttpRequestSpark *) userdata;

	if(httpRequestSpark->oncomplete) {
		char * ptr = (char *) realloc(httpRequestSpark->response, httpRequestSpark->count + n + 1);
		if(ptr == NULL) {
			LoggerError("[FIONA-logger] HttpRequestSpark DATA Error while reallocating memory. Try with ondata=true");
		}
		else {
			httpRequestSpark->response = ptr;
			memcpy(httpRequestSpark->response + httpRequestSpark->count, data, n);
		}
	}
	else{
		char respuesta [n+1];
		memcpy(respuesta, data, n);
		memset(respuesta + n, '\0', 1);
		httpRequestSpark->myFlow->processData(respuesta);
	}

	httpRequestSpark->count += n;
}

void HttpRequestSpark::OnComplete( const happyhttp::Response* r, void* userdata )
{
	HttpRequestSpark * httpRequestSpark;
	httpRequestSpark = (HttpRequestSpark *) userdata;
	if(httpRequestSpark->oncomplete){
		memset(httpRequestSpark->response + httpRequestSpark->count, '\0', 1);
		httpRequestSpark->myFlow->processData(const_cast<char *>(httpRequestSpark->response));
		free(httpRequestSpark->response);
		httpRequestSpark->response = NULL;
	}

}*/

size_t HttpRequestSpark::WriteCallback(char* ptr, size_t size, size_t nmemb)
{
	// Calculate the real size of the incoming buffer
	size_t realsize = size * nmemb;

	myFlow->processData(ptr);

	// return the real size of the buffer...
	return realsize;
}

/// Initializes HttpRequestSpark component.
void HttpRequestSpark::init(void){
	response = NULL;
	count = 0;
	oncomplete = false;
	blocking = true;

	pthread_cond_init(&condition_full, NULL);
	pthread_mutex_init(&mutex, NULL);
}

/// Unitializes the HttpRequestSpark component.
void HttpRequestSpark::quit(void){
	running = false;
}

//IFlow<char*> implementation
void HttpRequestSpark::processData(char * prompt) {
	string text(prompt);
	if(text.find("[EMAIL] ")!=string::npos) return;
	if(text.find("[SENDEMAIL] ")!=string::npos) return;
	string requestDecoded;
	if(text.find("[REQUEST] ")!=string::npos) {
		string requestEncoded = text.substr(10);
        while(requestEncoded.length() % 4 != 0)
        	requestEncoded = requestEncoded + "=";
		requestDecoded = base64_decode(requestEncoded);
	}
	else
		requestDecoded = text;
	LoggerInfo("HttpRequestSpark::processData request = %s", requestDecoded.c_str());
	if(requestDecoded.find("non-blocking=true")!=string::npos) blocking = false;
	else if (requestDecoded.find("blocking=true")!=string::npos) blocking = true;
	if(!blocking) {
		if(requests.size() >= 100)
			LoggerWarn("[FIONA-logger]HttpRequestSpark::processData requests queue has reached its limit. This request will NOT be processed");
		else {
			requests.push_back(requestDecoded);
			pthread_cond_signal(&condition_full);
		}
	}
	else {
		request(requestDecoded);
	}
}

//IThreadProc implementation
void HttpRequestSpark::process() {
	pthread_mutex_lock(&mutex);
	if(requests.empty()) {
		pthread_cond_wait(&condition_full, &mutex);
	}
	string text = requests.front();
	requests.erase(requests.begin());
	request(text);
	pthread_mutex_unlock(&mutex);
}

void HttpRequestSpark::request(string & text) {
    m.lock();
	//Parse
	string method;
	string host;
	int port;
	string url;
	string body;
	vector<string> vec;
	try {
		parse(text, method, host, port, url, body, vec);
	}
	catch(Exception& e) {
		LoggerError("[FIONA-logger]HttpRequestSpark::process | Exception: %s\nText= %s", e.msg, text.c_str() );
		m.unlock();
		return;
	}

	/*const char * headers[vec.size()+1];
	if(vec.size() != 0) {
		for(unsigned int i = 0; i < vec.size(); i++) {
			//LoggerInfo("[FIONA-logger] %s", vec[i].c_str());
			headers[i] = vec[i].c_str();
		}
		headers[vec.size()] = '\0';
	}


	conn = new happyhttp::Connection(host.c_str(), port);
	conn->setcallbacks( OnBegin, OnData, OnComplete, this );

	try {
		conn->request( method.c_str(),
				url.c_str(),
				vec.size() != 0 ? headers : 0,
				!body.empty() ? reinterpret_cast<const unsigned char *>(body.c_str()) : 0,
				!body.empty() ? body.length() : 0 );
		while( conn->outstanding() ) {
				conn->pump();
		}
	} catch( happyhttp::Wobbly& e )
	{
		LoggerError("[FIONA-logger]HttpRequestSpark::process | Exception: %s\n", e.what() );
	}*/


	try {
		curlpp::Cleanup cleaner;
		curlpp::Easy request;
		LoggerError("before constructor");
		string urlRequest(host + url);
		/*LoggerError("clear");
		urlRequest.clear();
		LoggerError("append");
		urlRequest.append(host);
		LoggerError("between constructor and function");
		urlRequest.append(url);*/
		LoggerError("after function");
		std::list<std::string> headers(vec.size());
		std::copy(vec.begin(), vec.end(), headers.begin());
		request.setOpt(new curlpp::options::Url(urlRequest));
		request.setOpt(new curlpp::options::Port(port));
		request.setOpt(new curlpp::options::FollowLocation(true));
		request.setOpt(new curlpp::options::HttpHeader(headers));
		if(method == "POST" || method == "post")
			request.setOpt(new curlpp::options::PostFields(body));
		curlpp::types::WriteFunctionFunctor functor(this, &WriteCallback);
		curlpp::options::WriteFunction *callback = new curlpp::options::WriteFunction(functor);
		request.setOpt(callback);

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

	m.unlock();
}

void HttpRequestSpark::parse(string & text, string & method, string & host, int & port, string & url, string & body, vector<string> & vec) {
	//LoggerInfo("[FIONA-logger] %s", text.c_str());

	string str = "url=\"";
	size_t found = text.find(str);
	if(found==string::npos)
		throw Exception(const_cast<char *>("No \"url\" was introduced"));
	url = text.substr(found+str.length(),text.find("\"",found+str.length())-found-str.length());
	if(url.length() == 0)
		throw Exception(const_cast<char *>("No \"url\" was introduced"));
	text.erase(found, str.length() + url.length() );


	str = "body=\"";
	found = text.find(str);
	if(found!=string::npos) {
		body = text.substr(found+str.length(),text.find("\"",found+str.length())-found-str.length());
		text.erase(found, str.length() + body.length() );
	}


	str = "method=";
	found = text.find(str);
	if(found==string::npos) {
		//throw Exception(const_cast<char *>("No \"method\" was introduced"));
		LoggerWarn("[FIONA-logger] HttpRequestSpark No \"method\" was introduced. Default: GET\n" );
		method = "GET";
	}
	else {
		method = text.substr(found+str.length(),text.find("|", found)-found-str.length());
		if(method.length() == 0) {
			//throw Exception(const_cast<char *>("No \"method\" was introduced"));
			LoggerWarn("[FIONA-logger] HttpRequestSpark No \"method\" was introduced. Default: GET\n" );
			method = "GET";
		}
		text.erase(found, str.length() + method.length() );
	}


	str = "host=";
	found = text.find(str);
	if(found==string::npos)
		throw Exception(const_cast<char *>("No \"host\" was introduced"));
	host = text.substr(found+str.length(),text.find("|",found)-found-str.length());
	if(host.length() == 0)
		throw Exception(const_cast<char *>("No \"host\" was introduced"));
	text.erase(found, str.length() + host.length() );


	str = "port=";
	found = text.find(str);
	if(found==string::npos) {
		//throw Exception(const_cast<char *>("No \"port\" was introduced"));
		LoggerWarn("[FIONA-logger] HttpRequestSpark No \"port\" was introduced. Default: 80\n" );
		port = 80;
	}
	else {
		string tmp_port = text.substr(found+str.length(),text.find("|",found)-found-str.length());
		port = atoi(tmp_port.c_str());
		if(port == 0) {
			//throw Exception(const_cast<char *>("No \"port\" was introduced"));
			LoggerWarn("[FIONA-logger] HttpRequestSpark No \"port\" was introduced. Default: 80\n" );
			port = 80;
		}
		text.erase(found, str.length() + tmp_port.length() );
	}


	string tmp_headers;
	str = "headers=\"";
	found = text.find(str);
	if(found!=string::npos) {
		tmp_headers = text.substr(found+str.length(),text.find("\"",found+str.length())-found-str.length());
		text.erase(found, str.length() + tmp_headers.length() );
	}


	size_t pos;
	if(!tmp_headers.empty()) {
		do {
			pos = tmp_headers.find(",");
			if(pos != string::npos) {
				string head = tmp_headers.substr(0,pos);
				tmp_headers = tmp_headers.substr(head.length()+1);
				vec.push_back(head);
			}
			else
				vec.push_back(tmp_headers);
		}while(pos!=string::npos);
	}

	str = "oncomplete=true";
	found = text.find(str);
	if(found != string::npos) {
		oncomplete = true;
		text.erase(found, str.length() );
	}
	else {
		str = "ondata=true";
		found = text.find(str);
		if(found != string::npos) {
			oncomplete = false;
			text.erase(found, str.length() );
		}
	}


	/*LoggerInfo("[FIONA-logger] text %s", text.c_str());
	LoggerInfo("[FIONA-logger] method %s", method.c_str());
	LoggerInfo("[FIONA-logger] host %s", host.c_str());
	LoggerInfo("[FIONA-logger] port %d", port);
	LoggerInfo("[FIONA-logger] url %s", url.c_str());
	LoggerInfo("[FIONA-logger] body %s", !body.empty() ? body.c_str() : "nada");*/
}

