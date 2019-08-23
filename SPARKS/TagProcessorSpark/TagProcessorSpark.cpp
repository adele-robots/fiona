/**
 *
 * This Spark accomplish two purposes: 
 *
 * First:
 *  When a sentene is received over the IFlow<char*> link (e.g. from RebeccaAIMLSpark),
 *  if it cointains tags, the sentence is caches and all found tangs are published 
 *  through IPublisher<Json::Value*>, each tag with its generated id (UUID), type and parameters.
 * 
 * Second:
 *  When a tag reply is received, it must contain its original id, and response value. Then, 
 *  the tag part of the sentence will be turned in to the tag response value. 
 *  When all tags in a sentence have been replaced, the the reply sentence will be sent back 
 *  through IFlow<char*> link.
 * 
 *
 * @author Abel Castro Suárez
 * @version 0.2 17/02/2014
 */

//#include "stdAfx.h"
#include <iostream>
#include <string>
#include <vector>
#include <list>
#include <algorithm>
#include "TagProcessorSpark.h"
#include "jsoncpp/value.h"
#include "jsoncpp/reader.h"
#include "jsoncpp/writer.h"
#include <boost/uuid/uuid.hpp>
#include <boost/uuid/uuid_generators.hpp>
#include <boost/uuid/uuid_io.hpp>
#include <boost/lexical_cast.hpp>

using namespace std;

#ifdef _WIN32
#else
extern "C" Component *createComponent(char *componentInstanceName,
		char *componentType, ComponentSystem *componentSystem) {
	if (!strcmp(componentType, "TagProcessorSpark")) {
		return new TagProcessorSpark(componentInstanceName,
				componentSystem);
	} else {
		return NULL;
	}
}
#endif

void TagProcessorSpark::init(void) 
{

	//ToDo Obtain these parameters from .ini file.

	commandTagBeginToken = "<";
	commandTagEndToken = ">";
	commandTagDelimiterToken = ";";
	commandTagParamDeclarator = "=";

	// Load the Spark sources that can call this spark
	int numTypes = getComponentConfiguration()->getLength(const_cast<char *>("Spark_Types"));

	for (int i = 0; i < numTypes; i++) {
		char typeIndex[80];
		snprintf(typeIndex, 80, "Spark_Types.[%d]", i);
		types.push_back(getComponentConfiguration()->getString(typeIndex));
	}
}

void TagProcessorSpark::quit(void) {}

void TagProcessorSpark::processData(Json::Value* messageJSON) {}


void TagProcessorSpark::processData(char* message)
{
	string receivedMessage(message);

	Json::Value connectorReplyJSON;

	/* It's a tag reply (JSON) */
	if (Json::Reader().parse(receivedMessage, connectorReplyJSON))
	{
		LoggerInfo("[FIONA-logger] TagProcessorSpark::processData(char*)> Processing Connector tag reply: %s", message);

		std::string id = connectorReplyJSON.get("id", "").asString();
		std::string responseValue = connectorReplyJSON.get("response", "").asString();
		std::string type = connectorReplyJSON.get("type", "").asString();

		TagProcessorSpark::replaceTag(id, responseValue);

		int replysCount = TagProcessorSpark::sendAvailableReplys(type);

		LoggerInfo("[FIONA-logger] TagProcessorSpark::processData(char*)> Replys sent to Rebecca: %u", replysCount);
	}
	/* I'ts a text string value, with or without tags in it */
	else
	{
		LoggerInfo("[FIONA-logger] TagProcessorSpark::processData(char*)> Processing tags in Rebecca reply: %s", message);

		tag_sentence_t taggedSentence = TagProcessorSpark::parseSentence(receivedMessage);

		std::list<tag_t>::size_type tagCount;

		if ((tagCount = taggedSentence.tags.size()) > 0)
		{
			if(find(types.begin(), types.end(), taggedSentence.type) == types.end())
				return;

			LoggerInfo("[FIONA-logger] TagProcessorSpark::processData(char*)> Sentence contains %u tags.", tagCount);

			sentences.push_back(taggedSentence);

			publishTags(taggedSentence);
		}
		else
		{
			LoggerInfo("[FIONA-logger] TagProcessorSpark::processData(char*)> No tags found, ignoring: %s", message);
			//myFlowChar->processData(message);
		}
	}
}

Json::Value TagProcessorSpark::getJSON(tag_t tag)
{
	Json::Value tagJSON;

	tagJSON["id"] = tag.id;
	tagJSON["type"] = tag.type;

	for (std::pair<string, string> parameter : tag.parameters)
		tagJSON[parameter.first] = parameter.second;

	return tagJSON;

}

void TagProcessorSpark::publishTags(tag_sentence_t taggedSentence)
{
	for (tag_t tag : taggedSentence.tags)
	{
		Json::Value tagJSON = TagProcessorSpark::getJSON(tag);

		LoggerInfo("[FIONA-logger] TagProcessorSpark::publishTags> Publishing tag: %s", Json::StyledWriter().write(tagJSON).c_str());

		myFlowJSON->processData(&tagJSON);
	}
}

TagProcessorSpark::tag_sentence_t TagProcessorSpark::parseSentence(std::string sentence)
{
	tag_sentence_t currentSentence;

	size_t position = sentence.find(commandTagBeginToken);

	while (position != std::string::npos)
	{
		size_t tagLength = sentence.find(commandTagEndToken, position) - position;

		position += commandTagBeginToken.length();
		tagLength -= commandTagBeginToken.length();

		std::string tagString = sentence.substr(position, tagLength);

		LoggerInfo("[FIONA-logger] TagProcessorSpark::parseSentence> Tag found: %s", tagString.c_str());

		tag_t tag = TagProcessorSpark::parseTag(tagString);

		sentence.erase(position, tagLength);
		sentence.insert(position, tag.id);

		currentSentence.tags.push_back(tag);
		currentSentence.type = tag.type;

		position = sentence.find(commandTagBeginToken, ++position);
	}

	currentSentence.sentence = sentence;

	return currentSentence;
}

TagProcessorSpark::tag_t TagProcessorSpark::parseTag(std::string tagString)
{
	tag_t tag;

		tag.id = boost::lexical_cast<std::string>(boost::uuids::random_generator()());

		std::string::size_type position = tagString.find(commandTagDelimiterToken);

		tag.type = tagString.substr(0, position);

		LoggerInfo("[FIONA-logger] TagProcessorSpark::parseTag> Processing tag: %s", tag.type.c_str());

		std::string::size_type paramLength;

		while (position != std::string::npos)
		{
			position++;

			paramLength = tagString.find(commandTagDelimiterToken, position) - position;

			std::string paramWithValue = tagString.substr(position, paramLength);

			LoggerInfo("[FIONA-logger] TagProcessorSpark::parseTag> Processing parameter: %s", paramWithValue.c_str());

			std::string paramName = paramWithValue.substr(0, paramWithValue.find(commandTagParamDeclarator));
			std::string paramValue = paramWithValue.substr(paramWithValue.find(commandTagParamDeclarator) + 1);

			tag.parameters.insert(std::pair<std::string, std::string>(paramName, paramValue));

			position = tagString.find(commandTagDelimiterToken, position);
		}

		return tag;
}

bool TagProcessorSpark::replaceTag(std::string id, std::string value)
{

	//for (tag_sentence_t taggedSentence : sentences)
	// No lo voy a tocar porque funciona bien de momento pero solo se ejecuta para sentences[0] si existe
	// No se usa el valor de retorno asi que deberia ser void y retornar hasta que termine el bucle
	for(list<tag_sentence_t>::iterator it = sentences.begin(); it != sentences.end(); it++)
	{
		std::string::size_type tagPosition;
		if ((tagPosition = (*it).sentence.find(id)) != std::string::npos)
		{
			LoggerInfo("[FIONA-logger] TagProcessorSpark::replaceTag> Before: %s", (*it).sentence.c_str());

			(*it).sentence.erase(tagPosition - 1, id.size() + 2);
			(*it).sentence.insert(tagPosition - 1, value);

			LoggerInfo("[FIONA-logger] TagProcessorSpark::replaceTag> After: %s", (*it).sentence.c_str());

			//for (tag_t tag : taggedSentence.tags)
			for(list<tag_t>::iterator tagIt = (*it).tags.begin(); tagIt != (*it).tags.end(); tagIt++)
			{
				if ((*tagIt).id == id){
					(*it).tags.remove(*tagIt);
					tagIt--;
				}
			}

			/*return true;*/
		}
		/*else
		{
			return false;
		}*/
	}

}

int TagProcessorSpark::sendAvailableReplys(std::string type)
{
	int sentReplys = 0;

	//for (tag_sentence_t sentence : sentences)
	for(list<tag_sentence_t>::iterator it = sentences.begin(); it != sentences.end(); /*it++*/)
	{
		// si el tipo de tag a reemplazar no coincide con el de la frase a la que apunta el iterador pasamos a la siguiente frase
		/*list<tag_t>::iterator ite = it->tags.begin();
		//tag_t t = *ite;
		std::cout<<ite->type<<std::endl;
		string ty(ite->type);
		if(ty != type)
			continue;*/
		/*if(type=="taxi") {
			(*it).sentence = "[TAXI] " + (*it).sentence;
		}*/

		if((*it).type != type)
			//continue;
			it = sentences.erase(it);

		(*it).sentence = "[" + type + "] " + (*it).sentence;
		//string out = "[" + type + "] " + (*it).sentence;

		//if ((*it).tags.empty())
		//{
			myFlowChar->processData(const_cast<char*>((*it).sentence.c_str()));
			it = sentences.erase(it);
			sentReplys++;
			/*it--;*/
		//}
	}

	return sentReplys;
}
