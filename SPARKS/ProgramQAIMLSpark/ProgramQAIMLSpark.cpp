/*
 * ProgramQAIMLSpark.cpp
 *
 *  Created on: 30/01/2013
 *      Author: guille
 */

/// @file ProgramQAIMLSpark.cpp
/// @brief ProgramQAIMLSpark class implementation.


//#include "stdAfx.h"
#include "ProgramQAIMLSpark.h"
#include "aimlparser.h"


#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "ProgramQAIMLSpark")) {
			return new ProgramQAIMLSpark(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif

/// Initializes ProgramQAIMLSpark component.
void ProgramQAIMLSpark::init(void){
	// Get ProgramQ working directory
	QString programQWorkingDir = QString::fromStdString(getComponentConfiguration()->getString(const_cast<char *>("Program_Path")));

	//Create the parser
	parser = new AIMLParser(false,programQWorkingDir);
	if (!parser->loadAIMLSet(QString::fromStdString(getComponentConfiguration()->getString(const_cast<char *>("Aiml_Set")))))
	{
		printf("Check errors in debug.log!\n");
		ERR("Check selected AIMLSet in working directory: %s",programQWorkingDir.toUtf8().constData());
		exit(-1);
	}
}

/// Unitializes the ProgramQAIMLSpark component.
void ProgramQAIMLSpark::quit(void){
	delete parser;
}

void ProgramQAIMLSpark::processData(char *prompt){
	QString input = QString::fromUtf8(prompt);
	//LoggerInfo("[FIONA-logger]ProgramQAIMLSpark::processData | Texto a string (Utf8): %s ",input.toUtf8().data());

	// Get the response from AIML interpreter
	QString result = parser->getResponse(input);

	// Remove 'html:' expression
	result.replace("html:", "");

	//LoggerInfo("[FIONA-logger]ProgramQAIMLSpark::processData | Result a string (Utf8): %s ",result.toUtf8().data());

	myFlow->processData(result.toUtf8().data());

	//QString html = Qt::convertFromPlainText(result);
}



