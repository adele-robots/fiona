/// @file Httpd.cpp
/// @brief Httpd class implementation.


#ifndef __WEB_SERVER_H
#define __WEB_SERVER_H

#include "Thread.h"

#include <stdio.h>
#include "httpd.h"



/// @brief The class Httpd implements a single threaded web server.
///
/// The class Httpd implements a single thread web server.


class HttpRequest {
public:
	HttpRequest(httpd *s) : server(s) {}
	bool getParameter(
		char *parameterName, 
		char *parameterValue, 
		int parameterValueLenght
	);
	void writeResponse(char *);

private:
	httpd *server;
};


/// Required interface, invoked on each incoming HTTP request
/// Requests are listened on URL http://***:port/actionName
/// where port and actionName are given in the init() method.

class IHttpRequestHandler {
public:
	virtual void handleHttpRequest(HttpRequest *r) = 0;
};

class WebServer : public Thread {
public:
	WebServer(IAsyncFatalErrorHandler *afeh) : Thread("embedded-web-server-thread", afeh) {}
	void init(
		int port, 
		char *actionName,
		IHttpRequestHandler *httpRequestHandler
	);
	void process();
	void quit(void);

public:
	httpd *server;
	IHttpRequestHandler *httpRequestHandler;

private:
	char *actionName;
};


#endif
