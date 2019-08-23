/**
 *
 * @author Abel Castro Suárez
 * @version 0.1 20/2/2014
 */

//#include "stdAfx.h"

#include <list>
#include <ctime>
#include "RSSReaderSpark.h"
#include "jsoncpp/value.h"
#include "jsoncpp/reader.h"
#include "jsoncpp/writer.h"
#include <CkRss.h>
#include <iostream>
#include "stdio.h"
#include "string.h"
#include <fstream>
#include <locale>
#include "stdlib.h"
#include <set>

//#define DOWNLOAD_MEDIA


#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
		)
{
	if (!strcmp(componentType, "RSSReaderSpark")) {
			return new RSSReaderSpark(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif

void RSSReaderSpark::init(void)
{
	titles.clear();
	descriptions.clear();
	rssList.clear();
	numberOfNewsLastTopic = 0;
	initializeCharMap();

	int urlNumber = getComponentConfiguration()->getLength(const_cast<char *>("RSS_List"));
	for (int urlIndex = 0; urlIndex < urlNumber; urlIndex++) {
		string url;

		char setting[80];

		snprintf(setting, 80, "RSS_List.[%d].Url", urlIndex);
		url = getComponentConfiguration()->getString(setting);

		rssList.push_back(url);
	}
}

void RSSReaderSpark::quit(void) {
	charMap.clear();
}

void RSSReaderSpark::processData(char* message) {}

void RSSReaderSpark::processData(Json::Value* messageJSON)
{
	LoggerInfo("RSSReaderSpark Llega una peticion de news");
	Json::Value reply = (*messageJSON);
	std::string topic = reply.get("topic", "").asString();

	if(!topic.empty()) {
		LoggerInfo("Topic = %s", topic.c_str());
		numberOfNewsLastTopic = getNumberOfNews(topic);
		//reply["response"] = numberOfNewsLastTopic;
		reply["response"] = "[NEWS_TITLES] " + getTitlesOfNews();
		LoggerInfo("%s",Json::StyledWriter().write(reply).c_str());
		string s(Json::FastWriter().write(reply));
		myFlowChar->processData(const_cast<char*>(s.c_str()));
	}
	else{
		//Json::Int index = reply.get("read", "1").asInt();
		//if(index != 0) {
		int index = 1;
			LoggerInfo("Noticia numero %d", index);
			reply["response"] = "[NEWS_DESCRIPTION] " + getDescriptionOfNews(index).substr(0,1000);
			LoggerInfo("%s",Json::StyledWriter().write(reply).c_str());
			string s(Json::FastWriter().write(reply));
			myFlowChar->processData(const_cast<char*>(s.c_str()));
		//}
	}
}

void RSSReaderSpark::downloadImage(const char * url) {
#ifdef DOWNLOAD_MEDIA
	if(url) {
		std::string command = "/usr/bin/wget -nc -P " + getComponentConfiguration()->getString("User_Spark_Data") + "images/ " + std::string(url);
		system(command.c_str());
	}
#endif
}

void RSSReaderSpark::downloadVideo(const char * url) {
#ifdef DOWNLOAD_MEDIA
	if(url) {
		std::string command = "/usr/bin/wget -nc -P " + getComponentConfiguration()->getString("User_Spark_Data") + "videos/ " + std::string(url);
		system(command.c_str());
	}
#endif
}

bool RSSReaderSpark::findTopicInCategory(const char * topic, const char * category, const char * title) {
	if(topic) {
		bool ret = stristr(removeAccentMarks(category), topic);
		if(ret) {
			LoggerInfo("Found %s in category %s", topic, category);
			return true;
		}
		ret = stristr(removeAccentMarks(title), topic);
		if(ret) {
			LoggerInfo("Found %s in title %s", topic, title);
			return true;
		}
	}
	LoggerInfo("NOT Found %s in title %s", topic, removeAccentMarks(title));
	return false;
}

int RSSReaderSpark::getNumberOfNews(std::string topic) {
	//LoggerInfo("getNumberOfNews size of list %d", rssList.size());
	int numberOfNews = 0;
	titles.clear();
	descriptions.clear();

	CkRss rss;

	std::set<std::string> vectorGUID;

	bool success;

	//  Download from the feed URL:
	for(int i = 0; i < rssList.size(); i++) {
		LoggerInfo("Trying to download %s", rssList[i].c_str());
		success = rss.DownloadRss(rssList[i].c_str());
		if (success != true) {
			LoggerError("%s\n",rss.lastErrorText());
			return -1;
		}
		LoggerInfo("%s downloaded", rssList[i].c_str());

		//  Get the 1st channel.
		CkRss *rssChannel = 0;

		for(int numChannel = 0; numChannel < rss.get_NumChannels(); numChannel++) {
			//LoggerInfo("Canal %d", numChannel);

			rssChannel = rss.GetChannel(numChannel);

			if (rssChannel == 0 ) {
				LoggerError("No channel %d found in RSS feed.\n", numChannel);
				return -2;
			}

			CkRss * imagen = rssChannel->GetImage();
			downloadImage(imagen->getString("url"));
			long numItems = rssChannel->get_NumItems();

			for (long i = 0; i < numItems; i++) {
				CkRss *rssItem = 0;
				rssItem = rssChannel->GetItem(i);

				if(vectorGUID.find(std::string(rssItem->getString("guid"))) != vectorGUID.end()) {
					continue;
				}

				vectorGUID.insert(std::string(rssItem->getString("guid")));

				long numCategories = rssItem->GetCount("category");
				//for (long j = 0; j < numCategories; j++) {

					//if(findTopicInCategory(topic.c_str(), rssItem->mGetString("category",j), rssItem->getString("title"))) {
					if(findTopicInCategory(topic.c_str(), "", rssItem->getString("title"))) {
						numberOfNews++;
						titles.push_back(std::string(rssItem->getString("title")));
						descriptions.push_back(std::string(rssItem->getString("description")));
						//LoggerInfo("Titulo de noticia: %s", rssItem->getString("title"));
						//break;
					}
				//}

				long numContents = rssItem->GetCount("media:content");

				for (long j = 0; j < numContents; j++) {

					if(!strcmp(rssItem->mGetAttr("media:content", j, "medium"), "image")) {
						// Imagen
						downloadImage(rssItem->mGetAttr("media:content", j, "url"));
						downloadImage(rssItem->mGetAttr("media:thumbnail", j, "url"));
					}

					if(!strcmp(rssItem->mGetAttr("media:content", j, "medium"), "video")) {
						// Video
						downloadVideo(rssItem->mGetAttr("media:content", j, "url"));
						downloadImage(rssItem->mGetAttr("media:thumbnail", j, "url"));
					}
				}

				delete rssItem;
			}

			delete rssChannel;

		}
	}

	return numberOfNews;
}

std::string RSSReaderSpark::getDescriptionOfNews(int index) {
	//LoggerInfo("getDescriptionOfNews");
	if(index < descriptions.size())
		return descriptions[index];
	return std::string("");
}

std::string RSSReaderSpark::getTitlesOfNews() {
	//LoggerInfo("getTitlesOfNews");
	/*std::string title = "";
	for(int i = 0; i < titles.size(); i++)
		title += titles[i] += std::string(", ");
	if(titles.size() > 0)
		title = title.substr(0, title.size() - 2);
	return title;*/
	return titles.size() > 0 ? titles[0] : "";
}

bool RSSReaderSpark::stristr (const char *ch1, const char *ch2)
{
	std::locale loc;
	std::string str1(ch1);
	std::string str2(ch2);
	std::string upper1="";
	std::string upper2="";
	for (std::string::size_type i=0; i<str1.length(); ++i)
	    upper1+=std::toupper(str1[i],loc);
	for (std::string::size_type i=0; i<str2.length(); ++i)
	    upper2+=std::toupper(str2[i],loc);
	//if(upper1==upper2)
	if(upper1.find(upper2) != string::npos)
		return true;
	return false;

}

char * RSSReaderSpark::removeAccentMarks(const char * prompt) {
	string str(prompt);
	size_t pos;
	// Para cada pareja del mapa
	for(map<string, string>::iterator it = charMap.begin(); it != charMap.end(); it++) {
		// Mientras haya coincidencias en la frase
		while((pos = str.find(it->first)) != string::npos){
			// Sustituir
			str.replace(pos, it->first.length(), it->second.c_str(), it->second.length());
		}
	}
	return const_cast<char *>(str.c_str());
}

void RSSReaderSpark::initializeCharMap() {
	charMap.clear();
	charMap["á"] = "a";		charMap["â"] = "a";		charMap["ã"] = "a";		charMap["à"] = "a";		charMap["ä"] = "a";
	charMap["é"] = "e";		charMap["ê"] = "e";		charMap["ẽ"] = "e";		charMap["è"] = "e";		charMap["ë"] = "e";
	charMap["í"] = "i";		charMap["î"] = "i";		charMap["ĩ"] = "i";		charMap["ì"] = "i";		charMap["ï"] = "i";
	charMap["ó"] = "o";		charMap["ô"] = "o";		charMap["õ"] = "o";		charMap["ò"] = "o";		charMap["ö"] = "o";
	charMap["ú"] = "u";		charMap["û"] = "u";		charMap["ũ"] = "u";		charMap["ù"] = "u";		charMap["ü"] = "u";
	charMap["Á"] = "A";		charMap["Â"] = "A";		charMap["Ã"] = "A";		charMap["À"] = "A";		charMap["Ä"] = "A";
	charMap["É"] = "E";		charMap["Ê"] = "E";		charMap["Ẽ"] = "E";		charMap["È"] = "E";		charMap["Ë"] = "E";
	charMap["Í"] = "I";		charMap["Î"] = "I";		charMap["Ĩ"] = "I";		charMap["Ì"] = "I";		charMap["Ï"] = "I";
	charMap["Ó"] = "O";		charMap["Ô"] = "O";		charMap["Õ"] = "O";		charMap["Ò"] = "O";		charMap["Ö"] = "O";
	charMap["Ú"] = "U";		charMap["Û"] = "U";		charMap["Ũ"] = "U";		charMap["Ù"] = "U";		charMap["Ü"] = "U";
}
