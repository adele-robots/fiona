#ifndef __TRONConnectorSpark_H
#define __TRONConnectorSpark_H

#include "Component.h"
#include "IFlow.h"

class TRONConnectorSpark :
	public Component,
	public IFlow<Json::Value*>
{

	public:
		TRONConnectorSpark(
				char *instanceName,
				ComponentSystem *cs
		) : Component(instanceName, cs)
		{}

	public:
		//Mandatory
		void init(void);
		void quit(void);

		//IFlow<char *> implementation
		void processData(Json::Value*);

	private:
		struct cachedReply_t{
			time_t timeStamp;
			std::string query;
			std::string reply;
		};

	private:
		Json::Value getAPIData(Json::Value);
		bool isConnected();

	private:

		std::list<std::string> acceptedParameters;

		bool connected;

	private:
		IFlow<char* >* myFlowChar;

		void initializeRequiredInterfaces() {
			requestRequiredInterface<IFlow<char*> >(&myFlowChar);
		}

};

#endif
