/// @file TwitterSpark.cpp
/// @brief TwitterSpark class implementation.

#include "TwitterSpark.h"
#include <algorithm>
#include <boost/tokenizer.hpp>

/* LOG4CPLUS Headers */
#include <log4cplus/logger.h>
#include <log4cplus/fileappender.h>
#include <log4cplus/layout.h>
#include <log4cplus/ndc.h>
#include <log4cplus/helpers/loglog.h>

#include <syscall.h>

using namespace std;

using namespace log4cplus;
using namespace log4cplus::helpers;

Logger myLogger;

extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "TwitterSpark")) {
			return new TwitterSpark(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}

struct less_than_key
{
    inline bool operator() (const pair<string, Json::Value>& struct1, const pair<string, Json::Value>& struct2)
    {
        return (struct1.first < struct2.first);
    }
};

/// Initializes TwitterSpark component.
void TwitterSpark::init(void){

	user = getComponentConfiguration()->getString(const_cast<char*>("User"));
	password = getComponentConfiguration()->getString(const_cast<char*>("Password"));
    //LoggerInfo("user = %s", user.c_str() );
    //LoggerInfo("password = %s", password.c_str() );

    std::string tmpStr, tmpStr2;
    std::string replyMsg;

    /* Set twitter username and password */
    twitterObj.setTwitterUsername( user );
    twitterObj.setTwitterPassword( password );

    myOAuthAccessConsumerKey = getComponentConfiguration()->getString(const_cast<char*>("Consumer_Key"));
    myOAuthAccessConsumerSecret = getComponentConfiguration()->getString(const_cast<char*>("Consumer_Secret"));
    //LoggerInfo("ckey = %s", myOAuthAccessConsumerKey.c_str() );
    //LoggerInfo("csecret = %s", myOAuthAccessConsumerSecret.c_str() );

    /* OAuth flow begins */
    /* Step 0: Set OAuth related params. These are got by registering your app at twitter.com */
    twitterObj.getOAuth().setConsumerKey(myOAuthAccessConsumerKey);
    twitterObj.getOAuth().setConsumerSecret(myOAuthAccessConsumerSecret);

    myOAuthAccessTokenKey = getComponentConfiguration()->getString(const_cast<char*>("Token_Key"));
    myOAuthAccessTokenSecret = getComponentConfiguration()->getString(const_cast<char*>("Token_Secret"));
    //LoggerInfo("tkey = %s", myOAuthAccessTokenKey.c_str() );
    //LoggerInfo("tsecret = %s", myOAuthAccessTokenSecret.c_str() );

    if( myOAuthAccessTokenKey.size() && myOAuthAccessTokenSecret.size() )
    {
        /* If we already have these keys, then no need to go through auth again */
        //LoggerInfo( "Using:\nKey: %s\nSecret: %s", myOAuthAccessTokenKey.c_str(), myOAuthAccessTokenSecret.c_str() );

        twitterObj.getOAuth().setOAuthTokenKey( myOAuthAccessTokenKey );
        twitterObj.getOAuth().setOAuthTokenSecret( myOAuthAccessTokenSecret );
    }
    else {
    	ERR("Invalid token authentication");
    }

    /* Account credentials verification */
    if( twitterObj.accountVerifyCredGet() )
    {
        twitterObj.getLastWebResponse( replyMsg );
        //Json::Value accountJSON;
        //Json::Reader().parse(replyMsg, accountJSON);
        //printf( "\ntwitterClient:: twitCurl::accountVerifyCredGet web response:\n%s\n", Json::StyledWriter().write(accountJSON).c_str() );
    }
    else
    {
        twitterObj.getLastCurlError( replyMsg );
        LoggerError( "twitterClient:: twitCurl::accountVerifyCredGet error:\n%s", replyMsg.c_str() );
        ERR("Account verification failed");
    }

    lastIdReplied = "";
    firstTime = true;
    timePoll = getComponentConfiguration()->getFloat(const_cast<char*>("Time_Polling"));
    delay = getComponentConfiguration()->getInt(const_cast<char*>("Delay"));


	// Get user's working directory
	string logFilename = getGlobalConfiguration()->getUserDir();

	// Main thread ID
	int threadId = syscall(SYS_gettid);

	logFilename.append("/");
	//logFilename.append(boost::lexical_cast<string>(threadId));
	time_t  timestamp = time(0);
	char hostname[1024];
    size_t len = 1024;
    gethostname(hostname, len);
    string session = getGlobalConfiguration()->getString(const_cast<char*>("session"));
	stringstream nombre;
	nombre << timestamp << "_" << hostname << "_" << threadId << "_" << session << "_twitter.log";
	logFilename.append(nombre.str());

	// Initialize log session
	myLogger = Logger::getInstance(LOG4CPLUS_TEXT("TWITTERLOG"));
	LogLog::getLogLog()->setInternalDebugging(true);

	SharedAppenderPtr append_1(new RollingFileAppender(LOG4CPLUS_TEXT(logFilename)));
	append_1->setName(LOG4CPLUS_TEXT("TwitterLog"));
	append_1->setLayout( std::auto_ptr<Layout>(new PatternLayout("%D{%y-%m-%d %H:%M:%S} - %m%n")) );
	myLogger.addAppender(append_1);
	// Parent loggers will not log 'myLogger' messages
	myLogger.setAdditivity(false);

	LOG4CPLUS_INFO(myLogger,LOG4CPLUS_TEXT("INIT"));


    stopWatch.restart();
}




/// Unitializes the TwitterSpark component.
void TwitterSpark::quit(void){

}

// IFlow<char*> implementation
void TwitterSpark::processData(char *prompt){
	string text(prompt);
	if(text.empty() || unrepliedMentions.empty())
		return;
	if(text == "[RESPONSE_NOT_FOUND]") {
		lastIdReplied = unrepliedMentions.front().first;
		unrepliedMentions.erase(unrepliedMentions.begin());

		text = "<error>El avatar no ha respondido a esta pregunta.";
		string outputMsg = "Avatar: ";
		outputMsg.append(text);
		LOG4CPLUS_INFO(myLogger,LOG4CPLUS_TEXT(outputMsg));

		return;
	}

	string outputMsg = "Avatar: ";
	outputMsg.append(text);
	LOG4CPLUS_INFO(myLogger,LOG4CPLUS_TEXT(text));

	string id = unrepliedMentions.front().first;
	//LoggerInfo("Got from Rebecca: %s", prompt);
	reply(text, id);
}



//IThreadProc implementation
void TwitterSpark::process() {
	if(firstTime) {
		myFlow->processData("[TWITTER]");

		getLastIdReplied();

		firstTime = false;
	}
	//LoggerInfo("process");
	if(stopWatch.elapsedTime() >= timePoll) {
		getMentions(lastIdReplied);
		//LoggerInfo("%d new mentions", unrepliedMentions.size());
		while(!unrepliedMentions.empty()) {

			sleep(delay);

			string text = unrepliedMentions.front().second.get("text", "").asString();

			string outputMsg = "User: ";
			outputMsg.append(text);
			LOG4CPLUS_INFO(myLogger,LOG4CPLUS_TEXT(outputMsg));

			string::size_type pos = text.find("@" + user + " ");
			if(pos != string::npos) {
				text = text.erase(pos, user.size() + 2);
			}
			else {
				pos = text.find("@" + user);
				if(pos != string::npos) {
					text = text.erase(pos, user.size() + 1);
				}
			}
			char * msg = const_cast<char*>(text.c_str());
			//LoggerInfo("Sent to Rebecca: %s", msg);
			myFlow->processData(msg);
		}
		stopWatch.restart();
	}
	else
		usleep(200000);
}

bool TwitterSpark::newMentions() {
	return !unrepliedMentions.empty();
}

/* Get mentions */
void TwitterSpark::getMentions(string sinceId = "") {
	//LoggerInfo("getMentions id %s", sinceId.c_str());
	string replyMsg = "";
	Json::Value mentions;
	Json::Value mensajes;
	if( twitterObj.mentionsGet(sinceId) )
	{
		twitterObj.getLastWebResponse( replyMsg );
		Json::Reader().parse(replyMsg, mentions);
		//LoggerInfo("%s", Json::StyledWriter().write(mentions).c_str());
		if(!mentions.isArray()) {
			LoggerError("code: %d\tmessage: %s", mentions["errors"][0u].get("code", "").asInt(), mentions["errors"][0u].get("message", "").asString().c_str());
			return;
		}
		int i = 0;
		// Por cada mencion a partir del id pasado
		for(Json::ValueIterator it = mentions.begin(); it != mentions.end(); it++) {
			string id = mentions[i].get("id_str", "").asString();
			//if(std::find(unrepliedMentions.begin(), unrepliedMentions.end(), id) == unrepliedMentions.end())
			vector<pair<string, Json::Value> >::iterator it2;
			// Si no esta en los no respondidos
			for(it2 = unrepliedMentions.begin(); it2 != unrepliedMentions.end(); it2++) {
				if(it2->first == id)
					break;
			}
			if(it2 == unrepliedMentions.end()) {
				// Lo añado
				mensajes.clear();
				mensajes["user"] = mentions[i]["user"].get("screen_name", "").asString();
				mensajes["id"] = id;
				mensajes["text"] = mentions[i].get("text", "").asString();

				//LoggerInfo("unrepliedMentions.push_back %s %s", id.c_str(), mentions[i].get("text", "").asString().c_str());
				unrepliedMentions.push_back(pair<string, Json::Value>(id, mensajes));
				std::sort(unrepliedMentions.begin(), unrepliedMentions.end(), less_than_key());
			}

			i++;
	}
	//printf( "\ntwitterClient:: twitCurl::mentionsGet web response:\n%s\n", Json::StyledWriter().write(mensajes).c_str() );
	}
	else
	{
		twitterObj.getLastCurlError( replyMsg );
		LoggerWarn( "twitterClient:: twitCurl::mentionsGet error:\n%s", replyMsg.c_str() );
	}
}

/* Post a new reply */
void TwitterSpark::reply(string msg, string id) {
	//LoggerInfo("reply %s %s", msg.c_str(), id.c_str());
	string replyMsg = "";
	if(!msg.empty() && !id.empty()) {
		uint i;
		string user = "";
		uint size = unrepliedMentions.size();
		for(i = 0; i < size; i++) {
			if(unrepliedMentions[i].first == id) {
				user = unrepliedMentions[i].second.get("user", "").asString();
				unrepliedMentions.erase(unrepliedMentions.begin() + i);
				break;
			}
		}
		if(i == size) {
			LoggerWarn("No such id in mentions");
		}
		else {
			//LoggerInfo("%s, %s", user.c_str(), id.c_str());
			if( twitterObj.statusUpdate( "@" + user + " " + msg, id ) )
			{
				twitterObj.getLastWebResponse( replyMsg );
				Json::Value response;
				Json::Reader().parse(replyMsg, response);
				if(!response["errors"].isNull())
					LoggerWarn("twitterClient:: twitCurl::statusUpdate web response:\n%s", replyMsg.c_str() );
				//LoggerInfo("twitterClient:: twitCurl::statusUpdate web response:\n%s", replyMsg.c_str() );
			}
			else
			{
				twitterObj.getLastCurlError( replyMsg );
				LoggerWarn( "twitterClient:: twitCurl::statusUpdate error:\n%s", replyMsg.c_str() );
			}
			lastIdReplied = id;
		}
	}
}

void TwitterSpark::getLastIdReplied() {
    string replyMsg = "";
    //printf( "\nGetting user timeline\n" );
    if( twitterObj.timelineUserGet( true, true, 0 ) )
    {
        twitterObj.getLastWebResponse( replyMsg );
        Json::Value userTimelineJSON;
        Json::Reader().parse(replyMsg, userTimelineJSON);
		/*if(!userTimelineJSON["errors"].isNull())
			LoggerWarn("twitterClient:: twitCurl::statusUpdate web response:\n%s", replyMsg.c_str() );*/
        string lastId = "";
        uint i = 0;
        for(Json::ValueIterator it = userTimelineJSON.begin(); it != userTimelineJSON.end(); it++) {
        	lastId = userTimelineJSON[i].get("in_reply_to_status_id_str", "").asString();
        	// TODO: deberiamos comprobar tambien los numero para coger el mayor por si acaso? Parece que no
        	if(! lastId.empty()) {
        		lastIdReplied = lastId;
        		break;
        	}
        	i++;
        }
    }
    else
    {
        twitterObj.getLastCurlError( replyMsg );
        LoggerWarn("twitterClient:: twitCurl::timelineUserGet error:\n%s", replyMsg.c_str() );
    }
    //LoggerInfo("TwitterSpark::LastIdReplied = %s", lastIdReplied.c_str());
}
