/**
 * This Spark requires jsoncpp and restclient-cpp libraries to work.
 */

#include "IBMWatsonSpark.h"
#include <string>

//#define SAVE_TO_FILE

#ifdef _WIN32
#else
extern "C" Component *createComponent(char *componentInstanceName,
		char *componentType, ComponentSystem *componentSystem) {
	if (!strcmp(componentType, "IBMWatsonSpark")) {
		return new IBMWatsonSpark(componentInstanceName,
				componentSystem);
	} else {
		return NULL;
	}
}
#endif

/// Initializes IBMWatsonSpark component.
void IBMWatsonSpark::init(void)
{
	this->APIURL = getComponentConfiguration()->getString(
			const_cast<char*>("API_URL"));

	this->APIUsername = getComponentConfiguration()->getString(
			const_cast<char*>("API_Username"));

	this->APIPassword = getComponentConfiguration()->getString(
			const_cast<char*>("API_Password"));

	this->knowledgeTopic = getComponentConfiguration()->getString(
			const_cast<char*>("Knowledge_Topic"));

	this->defaultAnswer = getComponentConfiguration()->getString(
				const_cast<char*>("Default_Answer"));
}

/// Unitializes the ASRSpark component.
void IBMWatsonSpark::quit(void)
{
}


void IBMWatsonSpark::processData(char* questionText)
{
	LoggerInfo("[IBMWatsonSpark] Received question: %s", questionText);

	std::string bestAnswer = getBestAnswer(std::string(questionText));

	if (bestAnswer.empty())
	{
		LoggerInfo("[IBMWatsonSpark] Unknown answer. Default reply sent.");
		this->myCharFlow->processData(const_cast<char*>(this->defaultAnswer.c_str()));
	}
	else
	{
		bestAnswer = trimAnswerLength(bestAnswer);

		LoggerInfo("[IBMWatsonSpark] Known reply sent: %s", bestAnswer.substr(0, 40).append("...").c_str());
		this->myCharFlow->processData(const_cast<char*>(bestAnswer.c_str()));
	}

}

std::string IBMWatsonSpark::getRequestBody(std::string questionText)
{
	return std::string(
			"{"
			"\"question\": {"
			"\"questionText\":\"" + questionText + "\"}"
			"}"
		   );
}

/**
 * Return the best available answer or empty std::string if no reply is available
 */
std::string IBMWatsonSpark::getBestAnswer(std::string questionText)
{
	std::string requestURL(this->APIURL + this->knowledgeTopic);
	std::string requestType("application/json");

	RestClient::setAuth(this->APIUsername, this->APIPassword);

	RestClient::response IBMWatsonResponse = RestClient::post(requestURL, requestType,
			getRequestBody(questionText));

	Json::Reader JSONReader;
	Json::Value IBMWatsonResponseJSON;

	JSONReader.parse(IBMWatsonResponse.body, IBMWatsonResponseJSON, false);

	return IBMWatsonResponseJSON[0]["question"]["evidencelist"][0]["text"].asString();

}

std::string IBMWatsonSpark::trimAnswerLength(std::string answer)
{
	answer = answer.substr(0, 1024);

	std::string::size_type lastStopPosition = answer.find_last_of(". ", std::string::npos, 1);

	if (lastStopPosition != std::string::npos)
	{
		answer = answer.substr(0, lastStopPosition + 1);
	}

	return answer;
}

