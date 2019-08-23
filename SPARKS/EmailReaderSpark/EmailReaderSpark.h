/// @file EmailReaderSpark.h
/// @brief Component EmailReaderSpark main class.

#ifndef __EmailReaderSpark_H
#define __EmailReaderSpark_H


#include "Component.h"

// Required and Provided interface
#include "IFlow.h"
#include "IThreadProc.h"

#include <CkImap.h>
#include <CkMessageSet.h>
#include <CkEmailBundle.h>
#include <CkEmail.h>
#include <CkByteData.h>
#include <CkMailMan.h>
#include <CkString.h>
#include <stdio.h>

#include <StopWatch.h>

class EmailReaderSpark :
	public Component,
	public IThreadProc,
	public IFlow<char *>
{
public:
		EmailReaderSpark(
				char *instanceName,
				ComponentSystem *cs
		) : Component(instanceName, cs)
		{}

	void initializeRequiredInterfaces() {
		requestRequiredInterface<IFlow<char *> >(&myFlow);
	}

public:
	//Mandatory
	void init(void);
	void quit(void);

	// Implementation of IThreadproc
	void process();

	// Implementation of IFlow<char *>
	void processData(char * prompt);

private:
	std::string trim(std::string &);
	void connectToIMAPServer();

public:
	IFlow<char*> * myFlow;

private:

	string serverIMAP;
	string serverSMTP;
	int portSMTP;
	string user;
	string pass;
	float timePoll;
	string templateMail;
	CkEmail * outcomingEmail;
	string imapSearch;
	string mailbox;

	CkImap imap;
	string fromEmail;
	string subject;

	StopWatch stopWatch;
};

#endif
