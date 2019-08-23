/// @file TwitterSpark.h
/// @brief Component TwitterSpark main class.


#ifndef __TwitterSpark_H
#define __TwitterSpark_H


#include "Component.h"

// Required and Provided interface
#include "IFlow.h"
#include "IThreadProc.h"

#include "StopWatch.h"

#include "twitterClient.h"

#include "jsoncpp/value.h"
#include "jsoncpp/reader.h"
#include "jsoncpp/writer.h"


/// @brief This is the main class for component TwitterSpark.
///
/// Spark that receives a text via Twitter, and communicates with RebecaAIMSSpark to
/// get an answer. After that, it sends that answer again via Twitter.

class TwitterSpark :
	public Component,
	//**Component class must extend provided interfaces**
	public IFlow<char*>,
	public IThreadProc
{
public:
		TwitterSpark(
				char *instanceName,
				ComponentSystem *cs
		) : Component(instanceName, cs)
		{}

//private:
	// Required interface initialization
	IFlow<char*> *myFlow;

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
	
	// Implementation of IThreadproc
	void process();

private:

	/* Identificador del proceso */
	int pid;

	/* user */
	string user;

	/* password */
	string password;

	/* App token key */
	string myOAuthAccessTokenKey;

	/* App token secret */
	string myOAuthAccessTokenSecret;

	/* App consumer key */
	string myOAuthAccessConsumerKey;

	/* App consumer secret */
	string myOAuthAccessConsumerSecret;

	/* Twitter object */
	twitCurl twitterObj;

	/* vector that stores unreplied mentions and their ids */
	vector<pair<string, Json::Value> > unrepliedMentions;

	/* Last Tweet replied */
	string lastIdReplied;

	/* Clock for polling */
	StopWatch stopWatch;

	/* Time for polling in secs */
	float timePoll;

	/* Response delay in secs */
	int delay;

	/* First Time variable */
	bool firstTime;

private:

	void getMentions(string sinceId);

	bool newMentions();

	void reply(string msg, string id);

	void getLastIdReplied();

};

#endif
