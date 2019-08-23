/// @file WeatherSpark.cpp
/// @brief WeatherSpark class implementation.


//#include "stdAfx.h"

#include <list>
#include <unordered_set>
#include <ctime>
#include <algorithm>
#include "WorldWeatherOnlineSpark.h"
#include "jsoncpp/value.h"
#include "jsoncpp/reader.h"
#include "jsoncpp/writer.h"
#include "curlpp/cURLpp.hpp"
#include "curlpp/Easy.hpp"
#include "curlpp/Options.hpp"


#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
		)
{
	if (!strcmp(componentType, "WorldWeatherOnlineSpark")) {
			return new WorldWeatherOnlineSpark(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif

void WorldWeatherOnlineSpark::init(void){
	language = "es";
	initMap();
}

void WorldWeatherOnlineSpark::quit(void){
}


void WorldWeatherOnlineSpark::processData(Json::Value* messageJSON) {

	Json::Value APIReply = getWeatherInfo(*messageJSON);
	std::string APIReplyString = Json::StyledWriter().write(APIReply);
	if(APIReply["data"].get("location", "").asString() == "TOOFAR") {
		myFlowChar->processData(const_cast<char*>(APIReplyString.c_str()));
		return;
	}

	Json::Value sparkReply;

	sparkReply["id"] = (*messageJSON).get("id", "").asString();
	string date = (*messageJSON).get("date", "").asString();

	for(Json::ValueIterator it = (*messageJSON)["parameters"].begin(); it != (*messageJSON)["parameters"].end(); it++) {
		map<string, string>::iterator i = Map.find((*it).get("value", "").asString());
		if(i != Map.end()) {
			Json::Value dateWeather;
			if(date=="") {
				dateWeather = APIReply["data"]["current_condition"][0u];
			}
			else {
				dateWeather = APIReply["data"]["weather"][0u];
			}
			// Description se puede utilizar solo o si es el ultimo parametro
			if(i->first == "description") {
				if(i->second == "lang_en")
					sparkReply["data"][i->first] = dateWeather["weatherDesc"][0u].get("value", "ERROR").asString();
				else
					sparkReply["data"][i->first] = dateWeather[i->second][0u].get("value", "ERROR").asString();
			}
			else {
				sparkReply["data"][i->first] = dateWeather.get(i->second, "ERROR").asString();
			}
		}
		/*for(map<string, string>::iterator i = Map.begin(); i != Map.end(); i++) {
			if(i->first == (*it).get("value", "")) {
				sparkReply["data"][i->first] = APIReply["data"]["current_condition"][0u].get(i->second, "").asString();
				break;
			}
		}*/
	}

	string locationEn = APIReply["data"]["request"][0u].get("query", "ERROR").asString();
	string locationEs = translate(locationEn, "en", "es");
	sparkReply["data"]["location"] = locationEs;

	/*sparkReply["data"]["city"] = APIReply["data"]["request"][0u].get("query", "").asString();

	sparkReply["data"]["temperature"] = APIReply["data"]["current_condition"][0u].get("temp_C", "").asString();*/

	std::string sparkReplyString = Json::StyledWriter().write(sparkReply);

	LoggerInfo("WorldWeatherOnlineSpark::processData(JSON)> Received reply from API: %s", APIReplyString.c_str());

	myFlowChar->processData(const_cast<char*>(sparkReplyString.c_str()));
}


/*
 *	{
 *		"data": {
 *			"current_condition": [
 *				{
 *					"cloudcover": "0",
 *					"humidity": "81",
 *					"observation_time": "09:24 AM",
 *					"precipMM": "0.0",
 *					"pressure": "1042",
 *					"temp_C": "8",
 *					"temp_F": "46",
 *					"visibility": "10",
 *					"weatherCode": "113",
 *					"weatherDesc": [
 *						{
 *							"value": "Sunny"
 *						}
 *					],
 *					"weatherIconUrl": [
 *						{
 *							"value": "http:\/\/cdn.worldweatheronline.net\/images\/wsymbols01_png_64\/wsymbol_0001_sunny.png"
 *						}
 *					],
 *					"winddir16Point": "WSW",
 *					"winddirDegree": "240",
 *					"windspeedKmph": "11",
 *					"windspeedMiles": "7"
 *				}
 *			],
 *			"request": [
 *				{
 *					"query": "Gijon, Spain",
 *					"type": "City"
 *				}
 *			],
 *			"weather": [
 *				{
 *					"date": "2015-01-09",
 *					"precipMM": "0.0",
 *					"tempMaxC": "18",
 *					"tempMaxF": "64",
 *					"tempMinC": "9",
 *					"tempMinF": "48",
 *					"weatherCode": "113",
 *					"weatherDesc": [
 *						{
 *							"value": "Sunny"
 *						}
 *					],
 *					"weatherIconUrl": [
 *						{
 *							"value": "http:\/\/cdn.worldweatheronline.net\/images\/wsymbols01_png_64\/wsymbol_0001_sunny.png"
 *						}
 *					],
 *					"winddir16Point": "W",
 *					"winddirDegree": "267",
 *					"winddirection": "W",
 *					"windspeedKmph": "12",
 *					"windspeedMiles": "7"
 *				}
 *			]
 *		}
 *	}
 */
Json::Value WorldWeatherOnlineSpark::getWeatherInfo(const Json::Value& requestJSON){

		language = requestJSON.get("lang","es").asString();
		Map["description"] = "lang_" + language;

		string location = requestJSON.get("location", "").asString();
		location = translate(location, "es", "en");
		std::replace(location.begin(), location.end(), ' ', '+');

		std::string requestURI = "http://api.worldweatheronline.com/free/v1/weather.ashx?q=" + location + "&lang=" + language + "&format=json&key=sv4emab5f2sr55vgp8w92wrw";
		// La fecha solo deja hasta dentro de 4 dias, sino pone la de hoy
		string date = requestJSON.get("date","").asString();
		if(date != "") {
			if(date == "today" || date == "tomorrow") {
				requestURI.append("&date=" + date);
			}
			else {
				if(date.size() == 1 && date[0] >= '1' && date[0] <= '7') {
					if(date[0] == '7')
						date = "0";
					int diaWeather = atoi(date.c_str());
					//tratar como dia de la semana
					time_t t1 = time(NULL);
					tm * t2 = localtime(&t1);
					// lunes = 1 sabado = 6 domingo = 0
					int diaHoy = t2->tm_wday;
					// Si estamos en el mismo dia que se pide
					if(diaWeather == diaHoy) {
						// No se hace nada
					}
					// Si el dia que se pide esta en la misma semana
					else if(diaWeather > diaHoy && diaWeather <= (diaHoy + 4)) {
						// Se añade la diferencia de dias
						t2->tm_mday += diaWeather-diaHoy;
					}
					// SI el dia que se pide esta en la siguiente semana
					else if(diaWeather < diaHoy && diaWeather <= (diaHoy - 7 + 4)) {
						// Se añade la diferencia de dias
						t2->tm_mday += diaWeather-diaHoy + 7;
					}
					else {
						LoggerWarn("La previsión no llega tan lejos en el tiempo. Se mostrará la de hoy.");

						Json::Value JSONresponseContent;
						JSONresponseContent["data"]["location"] = "TOOFAR";
						JSONresponseContent["id"] = requestJSON.get("id", "").asString();

						return JSONresponseContent;
					}
					time_t next = mktime(t2);

					//if(difftime(t1, next) <= 4 * 24 * 60 * 60) {
						t2 = localtime(&next);
						string yyyyMMdd = std::to_string(t2->tm_year + 1900) + "-" + std::to_string(t2->tm_mon + 1) + "-" + std::to_string(t2->tm_mday);
						LoggerInfo("Se pide el dia %s", yyyyMMdd.c_str());
						requestURI.append("&date=" + yyyyMMdd);
					//}
				}
				/*else {
					//tratar como fecha yyyy-MM-dd
				}*/
			}
		}

		//LoggerInfo("WorldWeatherOnlineSpark::getWeatherInfo> Request City: %s", requestJSON["parameters"].get("city", "").asCString());
		LoggerInfo("WorldWeatherOnlineSpark::getWeatherInfo> Request URI: %s", requestURI.c_str());

		std::stringstream responseContent;

		try {

			curlpp::Cleanup myCleanup;

			responseContent << curlpp::options::Url(requestURI);
		}
		catch( curlpp::RuntimeError &e )
		{
			LoggerInfo("WorldWeatherOnlineSpark::getWeatherInfo> Runtime Error: %s", e.what());
		}

		catch( curlpp::LogicError &e )
		{
			LoggerInfo("WorldWeatherOnlineSpark::getWeatherInfo> Logic Error: %s", e.what());
		}

		Json::Value JSONresponseContent;

		Json::Reader().parse(responseContent.str(), JSONresponseContent);

		return JSONresponseContent;

}

void WorldWeatherOnlineSpark::initMap()
{
	Map.clear();

	Map.insert(pair<string, string>("cloudcover", "cloudcover"));
	Map.insert(pair<string, string>("humidity", "humidity"));
	Map.insert(pair<string, string>("precipMM", "precipMM"));
	Map.insert(pair<string, string>("pressure", "pressure"));
	Map.insert(pair<string, string>("tempC", "temp_C"));
	Map.insert(pair<string, string>("tempF", "temp_F"));
	Map.insert(pair<string, string>("maxTempC", "tempMaxC"));
	Map.insert(pair<string, string>("maxTempF", "tempMaxF"));
	Map.insert(pair<string, string>("minTempC", "tempMinC"));
	Map.insert(pair<string, string>("minTempF", "tempMinF"));
	Map.insert(pair<string, string>("windDir", "winddir16Point"));
	Map.insert(pair<string, string>("windDirDegree", "winddirDegree"));
	Map.insert(pair<string, string>("windSpeedKmph", "windspeedKmph"));
	Map.insert(pair<string, string>("windSpeedMph", "windspeedMiles"));
	Map.insert(pair<string, string>("description", "lang_" + language));
}

string WorldWeatherOnlineSpark::translate(string location, string idiomaOrigen, string idiomaSalida) {
	std::replace(location.begin(), location.end(), ' ', '+');
	std::stringstream responseContent;
	std::string requestURI = "https://translate.yandex.net/api/v1.5/tr.json/translate?key=trnsl.1.1.20150114T101509Z.63eca2d4c6e67abd.648b39fecb83476509f44b5832f08341a2570244&lang=" + idiomaOrigen + "-" + idiomaSalida + "&text=" + location;

	try {
		curlpp::Cleanup myCleanup;
		responseContent << curlpp::options::Url(requestURI);
	}
	catch( curlpp::RuntimeError &e )
	{
		LoggerInfo("WorldWeatherOnlineSpark::translate> Runtime Error: %s", e.what());
	}
	catch( curlpp::LogicError &e )
	{
		LoggerInfo("WorldWeatherOnlineSpark::translate> Logic Error: %s", e.what());
	}

	Json::Value JSONresponseContent;
	Json::Reader().parse(responseContent.str(), JSONresponseContent);
	string traduccion = JSONresponseContent["text"].get(0u,"").asString();
	return traduccion;
}
