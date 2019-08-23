/*
 * PandorabotsSpark.h
 *
 *  Created on: 11/06/2013
 *      Author: Alejandro
 */

/// @file HttpRequestSpark.h
/// @brief Component HttpRequestSpark main class.


#ifndef __HttpRequestSpark_H
#define __HttpRequestSpark_H


#include "Component.h"
#include "IFlow.h"
#include "IThreadProc.h"
#include "happyhttp.h"
#include <mutex>
#include "base64.h"

/// @brief This is the main class for component HttpRequestSpark.
///
/// 

class HttpRequestSpark :
	public Component,
	public IFlow<char*>,
	public IThreadProc
{
public:
		HttpRequestSpark(
			char *instanceName,
			ComponentSystem *cs
	) : Component(instanceName, cs)
	{}

	virtual ~HttpRequestSpark() {};


	void initializeRequiredInterfaces() {	
		requestRequiredInterface<IFlow<char *> >(&myFlow);
	}

	IFlow<char *> *myFlow;

public:
	//Mandatory
	void init(void);
	void quit(void);

	//IFlow<char*> implementation
	void processData(char * prompt);

	// Implementation of IThreadproc
	void process();

	/*static void OnBegin( const happyhttp::Response* r, void* userdata );
	static void OnData( const happyhttp::Response* r, void* userdata, const unsigned char* data, int n );
	static void OnComplete( const happyhttp::Response* r, void* userdata );*/
	size_t WriteCallback(char* ptr, size_t size, size_t nmemb);

	
private:
	//Put class attributes here
	happyhttp::Connection  * conn;
	int count;
	char * response;
	bool oncomplete;
	bool blocking;
	vector<string> requests;

	pthread_cond_t condition_full;
	pthread_mutex_t mutex;

	void parse(string &, string &, string &, int &, string &, string &, vector<string> &);
	void request(string &);
};

#endif
