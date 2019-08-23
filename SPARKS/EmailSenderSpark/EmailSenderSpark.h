/// @file EmailSenderSpark.h
/// @brief Component EmailSenderSpark main class.

#ifndef __EmailSenderSpark_H
#define __EmailSenderSpark_H


#include "Component.h"

// Required and Provided interface
#include "IFlow.h"
#include "../SugarCRMSpark/SugarCRMSpark.h"

class EmailSenderSpark :
	public Component,
	public IFlow<char *>
{
public:
		EmailSenderSpark(
				char *instanceName,
				ComponentSystem *cs
		) : Component(instanceName, cs), crm(instanceName, cs)
		{}

	void initializeRequiredInterfaces() {
		requestRequiredInterface<IFlow<char *> >(&myFlow);
	}

public:
	//Mandatory
	void init(void);
	void quit(void);

	// Interface implementation declaration
	//IFlow implementation
	void processData(char *);

	static const char * buildRequest(string from , string to, string subject, vector<pair<string, string> > headers, string text, vector<string> attachments);

private:
	void makeEmail();
	void parse(string&);
	void clearVariables();
	bool hasAttachment();
	bool isEmail(string);
	bool exists (const std::string& name);
	bool isBase64 (const std::string& name);

private:

	string from;
	string to;
	string subject;
	string cc;
	string bbc;
	vector<string> headers;
	string text;
	string mailserver;
	vector<string> attachments;

	SugarCRMSpark crm;

	IFlow<char*> * myFlow;

};

#endif
