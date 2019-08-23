/// @file WeatherSpark.h
/// @brief Component WeatherSpark main class.


#ifndef __WeatherSpark_H
#define __WeatherSpark_H


#include "Component.h"
#include "IFlow.h"


/// @brief This is the main class for component WeatherSpark.
///
///

class WorldWeatherOnlineSpark :
	public Component,
	public IFlow<Json::Value*>
{


	public:
		WorldWeatherOnlineSpark(
				char *instanceName,
				ComponentSystem *cs
		) : Component(instanceName, cs)
		{}

	private:
		IFlow<char* >* myFlowChar;

		map<string, string> Map;
		string language;

		void initializeRequiredInterfaces() {
			requestRequiredInterface<IFlow<char*> >(&myFlowChar);
		}

	public:
		//Mandatory
		void init(void);
		void quit(void);

		//IFlow<Json::Value *> implementation
		void processData(Json::Value*);

	private:
		Json::Value getWeatherInfo(const Json::Value&);
		void initMap();
		string translate(string, string, string);

};

#endif
