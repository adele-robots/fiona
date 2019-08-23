#ifndef __WeatherSpark_H
#define __WeatherSpark_H

#include "Component.h"
#include "IFlow.h"

class RSSReaderSpark :
	public Component,
	public IFlow<char*>,
	public IFlow<Json::Value*>
{

	public:
		RSSReaderSpark(
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

	private:
		void downloadImage(const char * url);
		void downloadVideo(const char * url);
		bool findTopicInCategory(const char * topic, const char * category, const char * title);
		int getNumberOfNews(std::string topic);
		std::string getDescriptionOfNews(int index);
		std::string getTitlesOfNews();
		bool stristr(const char *, const char *);
		char * removeAccentMarks(const char *);
		void initializeCharMap();

	private:

		std::vector<std::string> titles;
		std::vector<std::string> descriptions;
		std::vector<std::string> rssList;

		std::map<std::string, std::string> charMap;

		int numberOfNewsLastTopic;

	private:
		IFlow<char* >* myFlowChar;
		IFlow<Json::Value*>* myFlowJSON;

		void initializeRequiredInterfaces() {
			requestRequiredInterface<IFlow<char*> >(&myFlowChar);
			requestRequiredInterface<IFlow<Json::Value*> >(&myFlowJSON);
		}

};

#endif
