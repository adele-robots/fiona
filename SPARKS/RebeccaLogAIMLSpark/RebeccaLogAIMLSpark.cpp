/*
 * RebeccaLogAIMLSpark.cpp
 *
 *  Created on: 17/01/2013
 *      Author: guille
 */

/// @file RebeccaLogAIMLSpark.cpp
/// @brief RebeccaLogAIMLSpark class implementation.


//#include "stdAfx.h"
#include "RebeccaLogAIMLSpark.h"


#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "RebeccaLogAIMLSpark")) {
			return new RebeccaLogAIMLSpark(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif

string convertInt(int number)
{
   stringstream ss;//create a stringstream
   ss << number;//add number to the stream
   return ss.str();//return a string with the contents of the stream
}


/// Initializes RebeccaLogAIMLSpark component.
void RebeccaLogAIMLSpark::init(void){
	CERR("RebeccaLogAIMLSpark::init\n");
	// Call base class initialization (load AIML files, etc)
	RebeccaAIMLSpark::init();
	// Get user's working directory
	string logFilename = getGlobalConfiguration()->getUserDir();
/*
	// Generate uuid to make an unique filename (log per session)
	boost::uuids::uuid uuid = boost::uuids::random_generator()();
	const string tmp = boost::lexical_cast<string>(uuid);
*/
	// Main thread ID
	int threadId = syscall(SYS_gettid);

	logFilename.append("/");
	//logFilename.append(boost::lexical_cast<string>(threadId));
	logFilename.append(convertInt(threadId));
	logFilename.append("_history.log");
	CERR("El working directory es: " << logFilename << endl);

	// Initialize log session
	myLogger = Logger::getInstance(LOG4CPLUS_TEXT("CHAT_LOG"));
	LogLog::getLogLog()->setInternalDebugging(true);

	SharedAppenderPtr append_1(new RollingFileAppender(LOG4CPLUS_TEXT(logFilename)));
	append_1->setName(LOG4CPLUS_TEXT("RebeccaLogAIML"));
	append_1->setLayout( std::auto_ptr<Layout>(new PatternLayout("%d{%y-%m-%d %H:%M:%S} - %m%n")) );
	myLogger.addAppender(append_1);
	// Parent loggers will not log 'myLogger' messages
	myLogger.setAdditivity(false);
}

void RebeccaLogAIMLSpark::processData(char *prompt){
	//Logging to filesystem
	//NDCContextCreator _context(LOG4CPLUS_TEXT("loop"));
	string inputMsg = "Chat: ";
	string outputMsg = "Rebecca: ";
	inputMsg.append(prompt);
	LOG4CPLUS_INFO(myLogger,LOG4CPLUS_TEXT(inputMsg));

	//Gets the response to the prompt received
	StringPimpl response;
	try{
		response = builder.getResponse(removeAccentMarks(prompt));
	}catch(rebecca::impl::Exception &e){
		cout << e.what() << endl;
	}
	//Sends that response
	if(strcmp(response.c_str(),"")){
		outputMsg.append(response.c_str());
		LOG4CPLUS_INFO(myLogger,LOG4CPLUS_TEXT(outputMsg));
		myFlow->processData(response.c_str());
	}
	else{
		outputMsg.append("I don't understand that symbol");
		LOG4CPLUS_INFO(myLogger,LOG4CPLUS_TEXT(outputMsg));
		myFlow->processData("I don't understand that symbol");
	}
}
