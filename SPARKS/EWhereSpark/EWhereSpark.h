#ifndef __WeatherSpark_H
#define __WeatherSpark_H

#include "Component.h"
#include "IFlow.h"

class EWhereSpark :
	public Component,
	public IFlow<char*>,
	public IFlow<Json::Value*>
{

	public:
		EWhereSpark(
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
		void processData(char*);

	private:
		struct cachedReply_t{
			time_t timeStamp;
			std::string query;
			std::string reply;
		};

	private:
		Json::Value getAPIData(Json::Value);
		Json::Value getReply(Json::Value);

	private:
		uint cachedReplyExpirationSeconds;

		std::string urlValidar;
		std::string urlCrear;
		std::list<std::string> acceptedParameters;

	private:
		IFlow<char* >* myFlowChar;
		IFlow<Json::Value*>* myFlowJSON;

		void initializeRequiredInterfaces() {
			requestRequiredInterface<IFlow<char*> >(&myFlowChar);
			requestRequiredInterface<IFlow<Json::Value*> >(&myFlowJSON);
		}

};

#endif
