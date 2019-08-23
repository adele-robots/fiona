/*
 * TRONHandlerSpark.h
 *
 *  Created on: 01/02/2013
 *      Author: guille
 */

/// @file TRONHandlerSpark.h
/// @brief Component TRONHandlerSpark main class.


#ifndef __TRONHandlerSpark_H
#define __TRONHandlerSpark_H


#include "Component.h"
#include "IFlow.h"
#include "IThreadProc.h"
#include "websocket_client.h"
#include "happyhttp.h"
#include <cstring>
#include <sys/syscall.h>
#include "stacktrace/call_stack.hpp"
#include "stacktrace/stack_exception.hpp"
#include "TRONReaderComponent.h"

/// @brief This is the main class for component TRONHandlerSpark.
///
/// 

//Modos de teleoperacion:

const int DIRECTLY = 1;
const int UNTIL_OPERATOR = 2;
const int ONE_ERROR = 3;
const int UNTIL_ERROR = 4;
const int AFTER_X_MESSAGES = 5;
const int UNTIL_TAG = 6;

class TRONHandlerSpark :
	public Component,
	public IFlow<char*>,
	public IThreadProc
{
public:
		TRONHandlerSpark(
				char *instanceName,
				ComponentSystem *cs
		) : Component(instanceName, cs), handler(new websocket_client())
		{
			//readerComponent = new TRONReaderComponent(instanceName, cs);
		}
		virtual ~TRONHandlerSpark() {};


		IFlow<char *> *myCharFlow;
		IFlow<AudioWrap *> *myAudioFlow;
		IFlow<Image *> *myImageFlow;

	void initializeRequiredInterfaces() {	
		requestRequiredInterface<IFlow<char *> >(&myCharFlow);
		requestRequiredInterface<IFlow<AudioWrap *> >(&myAudioFlow);
		requestRequiredInterface<IFlow<Image *> >(&myImageFlow);
	}

public:
	//Mandatory
	void init(void);
	void quit(void);

	//IFlow<char*> implementation
	void processData(char * prompt);

	// Implementation of IThreadproc
	void process();

	void stop();

	void sendContext();

	void setTRONReaderComponent(TRONReaderComponent* ReaderComponent);

	/**
	 * Notifies that the TRON operator is connected.
	 *
	 * @warning setReaderComponentInstance must be called before
	 */
	void notifyOperatorConnected();

	
private:
	//Put class attributes here
	websocket_client_ptr  handler;
	bool rebeccaCall;
	bool ignoreRebecca;
	bool countMessages;
	list<string> * context;
	uint MAX_NUM_MESSAGES;
	int numMessages;
	int currentMessages;
	string ipTRON;
	int port;
	string wsPort;
	int teleoperationMode;
	string pass;
	bool isOperatorConnected;
	string errorFrase;
	client* endpoint;
	bool useReaderComponent;
	string tag;
	pthread_cond_t condition_tag_received;
	pthread_mutex_t mutex;
	bool condition_tag_received_bool;
	TRONReaderComponent* readerComponent;

private:
	void saveMsgToContext(string);
	void initWS();
	bool isWSInit();

};

#endif
