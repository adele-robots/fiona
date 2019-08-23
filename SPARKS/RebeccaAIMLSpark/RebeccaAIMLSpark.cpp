/// @file RebeccaAIMLSpark.cpp
/// @brief RebeccaAIMLSpark class implementation.

#include "RebeccaAIMLSpark.h"


#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "RebeccaAIMLSpark")) {
			return new RebeccaAIMLSpark(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif

/// Initializes RebeccaAIMLSpark component.
void RebeccaAIMLSpark::init(void){
	try
	{
		/*
		 * Create the arguments object
		 * and pass it the arguments
		 * for parsing
		 */
		char userPath[256];
		char userSparkData[1024];
		//getGlobalConfiguration()->getString("AudioVideoConfig.UserPath", userPath, 256);
		getComponentConfiguration()->getString("UserPath", userPath, 256);
		getComponentConfiguration()->getString("User_Spark_Data", userSparkData, 1024);
		//Right now, we don't want to pass arguments
		char* args[]{""};
		Arguments arguments(1, args,userPath);


		/* Give the address to Rebecca for usesage.
		 * Rebecca DOES NOT delete it.
		 */
		builder.setCallBacks(&callback);
		cout << "[Rebecca loading]" << endl;

		/*
		 * Set the schemas for the AIML XML (AIML.xsd)
		 * and for Rebecca's own configuration files.
		 * The schema's have to be relative to where the files
		 * you are going to parse are going to be at.
		 */
		builder.setAIMLSchema(arguments.getAimlSchemaPath().c_str());
		builder.setCommonTypesSchema(arguments.getCommonTypesSchemaPath().c_str());
		builder.setBotConfigurationSchema(arguments.getBotConfigurationSchemaPath().c_str());

		/*
		 * Set that "yes" we do want to do XML validation on
		 * both the AIML XML and Rebecca's own configuration
		 * files.
		 */
		builder.setAIMLValidation();
		builder.setBotConfigurationValidation();

		/*
		 * Parse Rebecca's configuration files to setup
		 * Rebecca's ability to handle input subsitutions,
		 * what a sentence splitter is, and what bot properties
		 * she should have.
		 */
		string substitutions_xml = arguments.getConfigurationDirectory() + "/substitutions.xml";
		builder.parseSubstitutionFile(substitutions_xml.c_str());

		string sentence_splitters_xml = arguments.getConfigurationDirectory() + "/sentence-splitters.xml";
		builder.parseSentenceSplitterFile(sentence_splitters_xml.c_str());

		string properties_xml = arguments.getConfigurationDirectory() + "/properties.xml";
		builder.parsePropertiesFile(properties_xml.c_str());

		/*
		 * Add the entire directory.  Every file that has the
		 * extension ".aiml" will be added to the internal queue
		 * for latter processing.
		 */
		builder.addDirectory(arguments.getAimlDirectory().c_str());

		/*
		 * If a userConfiguration path exists, add all the .aiml files
		 * this directory contains, to the AIML brain
		 */
		try{
		builder.addDirectory(userSparkData);
		}catch(DirectoryNotFoundException dnfe){
			cout << "User Spark Path not found" << endl;
		}
		/*
		 * No other files to add to the internal queue.
		 * So, let's create the AIML graph, the internal
		 * data structures.
		 */
		builder.createGraph();

		/*
		 * Get the number of AIML categories loaded in total.
		 */
		int size = builder.getSize();

		//Print out the number of categories loaded.
		cout << endl << endl;
		cout << "[Rebecca now fully loaded]" << endl;
		cout << "[Number of categories loaded: " << size << "]" << endl;
		cout << "[Type /exit to exit]" << endl << endl << endl;

		/*
		 * Get the botName which should be Rebecca since that is
		 * the name give in the configuration file properties.xml
		 * which we parsed above.
		 */
		string botName = builder.getBotPredicate("name").c_str();

		/*
		 * Send a initial conversation of "connect" to
		 * annotated alice and get the response.
		 */
		//StringPimpl response = builder.getResponse("who is Frank?");

		//Send the initial opening line of the bot
		//cout << botName << " says: " << response.c_str() << endl;

        initializeCharMap();
	}
	/*
	 * All the exceptions are grouped here but you
	 * might not want this since it's a bit harder
	 * to determine where they came from.
	 */
	catch(DirectoryNotFoundException &e)
	{
		cout << "[A Directory Was Not Found Terminating]" << endl;
		cout << "[" << e.what() << "]" << endl;
		return;
	}
	catch(FileNotFoundException &e)
	{
		cout << "[A File Was Not Found Terminating]" << endl;
		cout << "[" << e.what() << "]" << endl;
		return;
	}
	catch(IllegalArgumentException &e)
	{
		cout << "[IllegalArgument Found Terminating]" << endl;
		cout << "[" << e.what() << "]" << endl;
		return;
	}
	catch(InitializationException &e)
	{
		cout << "[Initialization Exception Found Terminating]" << endl;
		cout << "[" << e.what() << "]" << endl;
		return;
	}
	catch(XMLErrorException &e)
	{
		cout << "[XMLError Exception Found Terminating]" << endl;
		cout << "[" << e.what() << "]" << endl;
		return;
	}
	catch(rebecca::impl::Exception &e)
	{
		cout << "[An uknown exception occured, Terminating program]" << endl;
		cout << "[" << e.what() << "]";
		return;
	}


}

/// Unitializes the RebeccaAIMLSpark component.
void RebeccaAIMLSpark::quit(void){
}


void RebeccaAIMLSpark::processData(char *prompt){
	//Gets the response to the prompt received
	StringPimpl response;
	try{
		response = builder.getResponse(removeAccentMarks(prompt));
	}catch(rebecca::impl::Exception &e){
		cout << e.what() << endl;
	}
	//Sends that response
	if(strcmp(response.c_str(),""))
		myFlow->processData(response.c_str());
	else
		myFlow->processData("I don't understand that symbol");
}

char * RebeccaAIMLSpark::removeAccentMarks(char * prompt) {
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

void RebeccaAIMLSpark::initializeCharMap() {
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



