/*
 * Copyright (c) 2011, Peter Thorson. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the WebSocket++ Project nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL PETER THORSON BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

#include "websocket_client.h"
#include "Logger.h"
#include "TRONHandlerSpark.h"

using websocketpp::client;

websocket_client::websocket_client() {
	//LoggerInfo("[FIONA-logger]websocket_client::websocket_client | Constructor!!");
	operator_connected = false;
	finished = true;
}

void websocket_client::on_message(connection_ptr con, message_ptr msg) {
	//LoggerInfo("websocket_client::on_message websocketpp::frame::opcode::%d", msg->get_opcode());
//	if(msg->get_opcode() == websocketpp::frame::opcode::BINARY) {
//		string s = msg->get_payload();
//		const char * c = s.c_str();
//		LoggerInfo("websocket_client:: size= %d binarymessage= %s", s.size(), c);
//		int16_t * i = (int16_t*)(c);
//		AudioWrap audio(i, s.size()*2);
//		//LoggerInfo("websocket_client:: sizeof(msg->get_payload)=%d", sizeof(msg->get_payload()));
//		myWebsocketServerAudioFlow->processData(&audio);
//	}
//	else {
		LoggerInfo("[FIONA-logger]websocket_client::on_message | MENSAJE RECIBIDO : %s !!",const_cast<char *>((msg->get_payload()).c_str()));
		std::cout << "Message received: " << msg->get_payload() << std::endl;
		//LoggerInfo("[FIONA-logger]websocket_client::websocket_client | dirección del puntero: %p",myWebsocket_serverFlow);
		if( msg->get_payload() == "[TRON OPERATOR CONNECTED]") {
			operator_connected = true;

			// Notifies TRONHandlerSpark
			tronSpark->notifyOperatorConnected();
			tronSpark->sendContext();
		}
		else if( msg->get_payload() == "[TRON OPERATOR DISCONNECTED]")
			operator_connected = false;
		else if(msg->get_payload() == "[TRON EXIT]")
			kill(getpid(), SIGTERM);//exit(0);
		else
			myWebsocket_serverFlow->processData(const_cast<char *>((msg->get_payload()).c_str()));
//	}
}

void websocket_client::on_fail(connection_ptr con) {
	LoggerError("[FIONA-logger]websocket_client::on_fail!!");
    std::cout << "Connection failed" << std::endl;
}

void websocket_client::on_open(connection_ptr con) {
	LoggerInfo("[FIONA-logger]websocket_client::on_open!!");
    m_con = con;
	operator_connected = false;
	finished = false;
    std::cout << "Successfully connected" << std::endl;
}

void websocket_client::on_close(connection_ptr con) {
	LoggerInfo("[FIONA-logger]websocket_client::on_close!!");
	finished = true;
    m_con = connection_ptr();

    std::cout << "client was disconnected" << std::endl;
}

// CLIENT API
// client api methods will be called from outside the io_service.run thread
//  they need to be careful to not touch unsyncronized member variables.

void websocket_client::send(const std::string &msg) {
	LoggerInfo("[FIONA-logger]websocket_client::send!! | hago send de: %s",msg.c_str());
    if (!m_con) {
    	LoggerError("[FIONA-logger]websocket_client::send | Error: no connected session");
        std::cerr << "Error: no connected session" << std::endl;
        std::exception e;
    	LoggerInfo("[FIONA-logger]websocket_client::send | voy a hacer throw");
        throw e;
        return;
    }

    //LoggerInfo("[FIONA-logger]websocket_client::send | hago  m_con->send(msg)");
    m_con->send(msg);
}

void websocket_client::close() {
	if (!m_con) {
        std::cerr << "Error: no connected session" << std::endl;
        return;
    }
    m_con->close(websocketpp::close::status::NORMAL,"Avatar closed properly");
	while( ! finished )
		;
}

void websocket_client::setPtrs(IFlow<char *> *myCharFlow, IFlow<AudioWrap *> *myAudioFlow, TRONHandlerSpark * tron){
	//LoggerInfo("[FIONA-logger]websocket_client::websocket_client | setIFlowPtr!!");
	myWebsocket_serverFlow = myCharFlow;
	myWebsocketServerAudioFlow = myAudioFlow;
	tronSpark = tron;
	//LoggerInfo("[FIONA-logger]websocket_client::websocket_client | dirección del puntero: %p",myWebsocket_serverFlow);
}

bool websocket_client::isOperatorConnected() {
	return operator_connected;
}
