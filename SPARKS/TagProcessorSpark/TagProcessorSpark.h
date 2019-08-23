#ifndef __TagProcessorSpark_H
#define __TagProcessorSpark_H

#include "Component.h"
#include "IFlow.h"

class TagProcessorSpark :
	public Component,
	public IFlow<char*>,
	public IFlow<Json::Value*>
{

	public:
		TagProcessorSpark(char *instanceName,
			ComponentSystem *cs) : Component(instanceName, cs) {}

	public:
		//Mandatory
		void init(void);
		void quit(void);

		//IFlow<char *> implementation
		void processData(char*);
		void processData(Json::Value*);

	private:
		struct tag_t
		{
			std::string id;
			std::string type;
			std::map<std::string, std::string> parameters;

			bool operator ==(const tag_t& c)
			{
		    	return (id == c.id);
			}
			tag_t(){}
			tag_t(const tag_t & other){
				id = other.id;
				type = other.type;
				parameters = other.parameters;
			}
			tag_t& operator= (const tag_t& other){
				id = other.id;
				type = other.type;
				parameters = other.parameters;
				return *this;
			}

		};

		struct tag_sentence_t
		{
			std::string sentence;
			std::list<tag_t> tags;
			std::string type;

			bool operator ==(const tag_sentence_t& c)
			{
				return (sentence == c.sentence &&
						type == c.type &&
						tags.size() == c.tags.size() &&
						std::equal(tags.begin(), tags.end(), c.tags.begin()));
			}
			tag_sentence_t(){}
			tag_sentence_t(const tag_sentence_t & other){
				sentence = other.sentence;
				tags = other.tags;
				type = other.type;
			}
			tag_sentence_t& operator= (const tag_sentence_t & other){
				sentence = other.sentence;
				tags = other.tags;
				type = other.type;
				return *this;
			}
		};
		
	private:
		tag_sentence_t parseSentence(std::string);
		tag_t parseTag(std::string);
		void publishTags(tag_sentence_t);
		Json::Value getJSON(tag_t);
		bool replaceTag(std::string, std::string);
		int sendAvailableReplys(std::string);

	private:

		// Command insertion tokens
		std::string commandTagBeginToken;
		std::string commandTagEndToken;
		std::string commandTagDelimiterToken;
		std::string commandTagParamDeclarator;

		// Sentences with tags pending...
		list<tag_sentence_t> sentences;

		// List of allowed types
		list<string> types;

	private:
		IFlow<char* >* myFlowChar;
		IFlow<Json::Value*>* myFlowJSON;


		void initializeRequiredInterfaces() {
			requestRequiredInterface<IFlow<char*> >(&myFlowChar);
			requestRequiredInterface<IFlow<Json::Value*> >(&myFlowJSON);
		}

};

#endif
