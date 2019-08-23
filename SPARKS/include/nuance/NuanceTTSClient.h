#ifndef NUANCETTSCLIENT_H_
#define NUANCETTSCLIENT_H_

#define ASIO_STANDALONE

#include <iostream>
#include <functional>
#include <thread>
#include <asio.hpp>

#include <jsoncpp/json.h>

#define PACKET_SIZE 4096

class NuanceTTSClient
{
	public:
		NuanceTTSClient(const std::string & serverIP, const std::string & serverPort);
		NuanceTTSClient();
		virtual ~NuanceTTSClient();

		void connectClient(std::function<void(char*, size_t)> receivedDataCallaback);
		void disconnectClient();

		void initServer(const std::string & voice, const std::string & voiceModel);
		void synthezise(const std::string & text);
		void stop();


	private:
		void receiveData();
		void closeConnection();


	private:
		std::string serverIP;
		std::string serverPort;
		bool connected;

		asio::io_service io_service;
		asio::ip::tcp::socket socket;

		std::function<void(char*, size_t)> receivedDataCallaback;

		std::thread receiveDataThread;
};

#endif
