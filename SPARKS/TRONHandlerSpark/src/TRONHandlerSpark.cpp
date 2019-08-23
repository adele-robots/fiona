/*
 * TRONHandlerSpark.cpp
 *
 *  Created on: 01/02/2013
 *      Author: guille
 */

/// @file TRONHandlerSpark.cpp
/// @brief TRONHandlerSpark class implementation.


#include "TRONHandlerSpark.h"
#include <boost/thread.hpp>


#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "TRONHandlerSpark")) {
			return new TRONHandlerSpark(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif
string ipTRONGlobal;
/// Initializes TRONHandlerSpark component.
void TRONHandlerSpark::init(void){

	//LoggerInfo("[FIONA-logger]TRONHandlerSpark::init");
	rebeccaCall = true;
	ignoreRebecca = false;
	countMessages = false;
	currentMessages = 0;
	context = new list<string>();
	isOperatorConnected = false;

	port = getComponentConfiguration()->getInt(const_cast<char*>("Port"));
	wsPort = getComponentConfiguration()->getString(const_cast<char*>("Ws_Port"));
	MAX_NUM_MESSAGES = getComponentConfiguration()->getInt(const_cast<char*>("Max_Num_Messages_Context"));
	ipTRONGlobal = getComponentConfiguration()->getString(const_cast<char*>("Tron"));
	teleoperationMode = getComponentConfiguration()->getInt(const_cast<char*>("Teleoperation_Mode"));
	pass = getComponentConfiguration()->getString(const_cast<char*>("Rand"));
	errorFrase = getComponentConfiguration()->getString(const_cast<char*>("Frase_Error"));
	useReaderComponent = getComponentConfiguration()->getBool(const_cast<char*>("Use_Reader_Component"));

	if(teleoperationMode == DIRECTLY)
		ignoreRebecca = true;
	else if(teleoperationMode == AFTER_X_MESSAGES) {
		countMessages = true;
		numMessages = getComponentConfiguration()->getInt(const_cast<char*>("Messages_To_Operate"));
	}
	else if(teleoperationMode == UNTIL_TAG)
		tag = getComponentConfiguration()->getString(const_cast<char*>("Tag"));

	if (useReaderComponent) {
		readerComponent->myAudioFlow = this->myAudioFlow;
		readerComponent->myImageFlow = this->myImageFlow;
		readerComponent->componentConfiguration = this->componentConfiguration;
		readerComponent->init();
	}

	//pthread_cond_init(&condition_tag_received, NULL);
	//pthread_mutex_init(&mutex, NULL);
	condition_tag_received_bool = false;
}

/// Unitializes the TRONHandlerSpark component.
void TRONHandlerSpark::quit(void){
	//LoggerInfo("[FIONA-logger]TRONHandlerSpark::quit\n");
	delete endpoint;

	if (useReaderComponent){
		readerComponent->quit();
	}

	//delete readerComponent;
}

//IThreadProc implementation
void TRONHandlerSpark::process() {
	//LoggerInfo("[FIONA-logger]TRONHandlerSpark::process");
	static	bool firstTime = true;
	if(firstTime){
		firstTime = false;

		initWS();
	}

	if (useReaderComponent){
		readerComponent->process();
	}
}

//IFlow<char*> implementation
void TRONHandlerSpark::processData(char * prompt) {
	stacktrace::call_stack st;
	//LoggerInfo("[FIONA-logger]TRONHandlerSpark::processData | STACKTRACE\n%s",st.to_string().c_str());

	if(teleoperationMode==UNTIL_OPERATOR || teleoperationMode==UNTIL_TAG) {
		if(! isOperatorConnected && handler->isOperatorConnected()) {
			isOperatorConnected = true;
			sendContext();
			ignoreRebecca = true;
		}
		else if(isOperatorConnected && ! handler->isOperatorConnected()) {
			isOperatorConnected = false;
			ignoreRebecca = false;
		}
		if(!isWSInit() && teleoperationMode==UNTIL_TAG && string(prompt).find(tag)!=string::npos) {
			//pthread_cond_signal(&condition_tag_received);
			initWS();
		}
	}

	if (std::string::npos != st.to_string().find("Rebecca")){
		LoggerInfo("[FIONA-logger]TRONHandlerSpark::processData | ME LLAMA REBECCA con texto: %s",prompt);
		rebeccaCall = true;
	}else{
		LoggerInfo("[FIONA-logger]TRONHandlerSpark::processData | ME LLAMA EL CHAT con texto: %s",prompt);
		rebeccaCall = false;
	}

	if (!rebeccaCall){
		saveMsgToContext(string(prompt));
		//rebeccaCall = true;
		//LoggerInfo("[FIONA-logger]TRONHandlerSpark::processData | ME LLAMA EL CHAT!");
		//LoggerInfo("[FIONA-logger]TRONHandlerSpark::processData | ME LLAMARON con texto: %s",prompt);
		if(ignoreRebecca) {
			//handler->send(string("Chat:")+prompt);
			sendContext();
			if(teleoperationMode == ONE_ERROR)
				ignoreRebecca = false;
		}
		if(!strcmp(prompt, "/operadora")) {
			sendContext();
			ignoreRebecca = true;
			myCharFlow->processData(const_cast<char *>("Déjame que piense unos instantes..."));
		}
		if(!strcmp(prompt, "/stopoperadora")) {
			ignoreRebecca = false;
			myCharFlow->processData(const_cast<char *>("Te dejo con Rebecca"));
			//rebeccaCall = false;
		}
		if(countMessages && !ignoreRebecca) {
			currentMessages++;
			if(currentMessages >= numMessages) {
				sendContext();
				ignoreRebecca = true;
				currentMessages = 0;
			}
		}
		else
			currentMessages = 0;
	}else{
		//saveMsgToContext(string("Rebecca:")+prompt);
		//rebeccaCall = false;
		//LoggerInfo("[FIONA-logger]TRONHandlerSpark::processData | ME LLAMARON con texto: %s",prompt);
		//LoggerInfo("[FIONA-logger]TRONHandlerSpark::processData | hice handler->send !");
		if( (string(prompt) != errorFrase) && ! ignoreRebecca){
			if(string(prompt).find("<agente")==string::npos)
				saveMsgToContext(string("Rebecca:")+prompt);
			myCharFlow->processData(prompt);
		}
		else {
			if(ignoreRebecca)
				//handler->send(string("Rebecca:")+prompt);
				sendContext();
			else { // !strcmp(prompt, "I don't understand that symbol")
				if(string(prompt).find("<agente")==string::npos)
					saveMsgToContext(string("Rebecca:")+prompt);
				if(teleoperationMode!=UNTIL_ERROR && teleoperationMode!=ONE_ERROR)
					myCharFlow->processData(prompt);
				else {
					sendContext();
					//if(teleoperationMode == UNTIL_ERROR)
						ignoreRebecca = true;
					myCharFlow->processData(const_cast<char*>("Buena pregunta... déjame que piense un segundo."));
				}
			}
		}
	}

}

void TRONHandlerSpark::setTRONReaderComponent(TRONReaderComponent* ReaderComponent){

	LoggerInfo("[FIONA-logger]TRONHandlerSpark::setReaderComponentInstance received TRONReaderComponent instance: %p", ReaderComponent);

	this->readerComponent = ReaderComponent;
}

void TRONHandlerSpark::notifyOperatorConnected(){
	if(useReaderComponent)
		readerComponent->operatorConnected();
}

void TRONHandlerSpark::sendContext() {
	for(list<string>::iterator it = context->begin(); it != context->end(); it++)
		handler->send(*it);
	context->clear();
}

void TRONHandlerSpark::saveMsgToContext(string prompt) {
	if(context->size() == MAX_NUM_MESSAGES)
		context->erase(context->begin());
	context->insert(context->end()--, prompt);
}

void TRONHandlerSpark::stop() {
	handler->close();
}

void TRONHandlerSpark::initWS() {
	condition_tag_received_bool = true;

	if(teleoperationMode==UNTIL_TAG) {
		LoggerInfo("[FIONA-logger]TRONHandlerSpark::Rand antes = %s", const_cast<char*>(pass.c_str()));
		string configFile = getComponentConfiguration()->getUserDir()+"/../TRONHandlerThreadSpark.ini";
		getComponentConfiguration()->loadConfiguration(const_cast<char*>(configFile.c_str()));
		pass = getComponentConfiguration()->getString(const_cast<char*>("Rand"));
		LoggerInfo("[FIONA-logger]TRONHandlerSpark::Rand despues = %s", const_cast<char*>(pass.c_str()));
	}

	try {
		//LoggerInfo("[FIONA-logger]TRONHandlerSpark::init | The ID of this thread is: %u\n", (unsigned int)pthread_self());
		handler->setPtrs(myCharFlow, myAudioFlow, this);
			//LoggerInfo("[FIONA-logger]TRONHandlerSpark::process | dirección del puntero: %p",myCharFlow);
			endpoint = new client(handler);
			client::connection_ptr con;

			LoggerInfo("[FIONA-logger]TRONHandlerSpark::process | el pass es %s , Voy a conectar con el WebSocket server",pass.c_str());
			std::string uri;
			uri.append("ws://");
			uri.append(ipTRONGlobal);
			uri.append(":");
			uri.append(wsPort);
			uri.append("/robot");
			uri.append(websocketpp::md5_hash_hex("operate" + std::string(pass)));
			con = endpoint->get_connection(uri);

			con->add_request_header("User-Agent","WebSocket++/0.2.0 WebSocket++Chat/0.2.0");
			con->add_request_header("Upgrade","websocket");
			con->set_origin("TRONHandlerSpark");

			endpoint->connect(con);

			LoggerInfo("[FIONA-logger]TRONHandlerSpark::process | antes de  run");
			boost::thread t(boost::bind(&client::run, endpoint, false));

			LoggerInfo("[FIONA-logger]TRONHandlerSpark::process | despues de  run");

		} catch (std::exception& e) {
			LoggerError("[FIONA-logger]TRONHandlerSpark::process | Exception %s",e.what());
		}

}

bool TRONHandlerSpark::isWSInit() {
	return condition_tag_received_bool;
}
