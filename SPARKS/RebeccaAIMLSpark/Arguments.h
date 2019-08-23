/*
 * Arguments.h
 *
 *  Created on: 16/07/2012
 *      Author: guille
 */

#ifndef ARGUMENTS_H_
#define ARGUMENTS_H_

//Std includes
#include <iostream>
#include <string>
#include <cstdlib>
using namespace std;

/**
 * Parses and sets the
 * arguments for main().
 */
class Arguments
{
//private:
//		const string basepath_schema = "/home/guille/Installations/RebeccaAIML-src-9871/resources/schema/";
public:

		/**
		 * Constructor that
		 * sets the arguments up
		 * from main() as well
		 * as the program name its
		 * self.
		 *
		 * \param argc The number of arguments
		 * sent in.
		 *
		 * \param args The array of arguments
		 *
		 * \param userPath The base path of the user
		 */
		Arguments(int argc, char* args[], char* userPath)
			: m_aimlSchemaPath(userPath),
			  m_commonTypesSchemaPath(userPath),
			  m_botConfigurationSchemaPath(userPath),
			  m_configurationDirectory(userPath),
			  m_aimlDirectory(userPath),
			  m_currentArgument(NO_ARG)
		{
			//Initialize path members with specific resources
			m_aimlSchemaPath +="resources/schema/AIML.xsd";
			m_commonTypesSchemaPath += "resources/schema/common-types.xsd";
			m_botConfigurationSchemaPath += "resources/schema/bot-configuration.xsd";
			m_configurationDirectory += "conf";
			m_aimlDirectory +="aiml/annotated_alice";

			/*
			 * Iterate over the arguments
			 * and set them
			 */
			for(int i = 1; i < argc; ++i)
			{
				//get string of the argument
				string argument(args[i]);

				if(m_currentArgument == NO_ARG)
				{
					if(argument == "-aimlSchema" ||
					   argument == "-as")
					{
						m_currentArgument = AIML_SCHEMA;
					}
					else if(argument == "-botSchema" ||
				            argument == "-bs")
					{
						m_currentArgument = BOT_SCHEMA;
					}
					else if(argument == "-commonSchema" ||
						    argument == "-cs")
					{
						m_currentArgument = COMMON_SCHEMA;
					}
					else if(argument == "-configurationDirectory" ||
							argument == "-cd")
					{
						m_currentArgument = CONFIGURATION;
					}
					else if(argument == "-aimlDirectory" ||
						    argument == "-ad")
					{
						m_currentArgument = AIML;
					}
					else if(argument == "-help" ||
						    argument == "-h" ||
							argument == "/?" ||
							argument == "--help")
					{
						/*
						 * Display help and exit
						 */
						cout << endl << endl
							 << "[console.exe help]" << endl
							 << "------------------" << endl << endl
							 << "Available switches:" << endl << endl
							 << "[-aimlSchema or -as]" << endl
							 << "    AIML Schema Path (default is ../../resources/schema/AIML.xsd)" << endl << endl
							 << "[-botSchema or -bs] " << endl
							 << "    Bot Schema Path (default is ../resources/schema/bot-configuration.xsd)" << endl << endl
							 << "[-commonSchema or -cs] " << endl
							 << "    Common Schema Path (default is ../resources/schema/common-types.xsd)" << endl << endl
							 << "[-configurationDirectory or -cd] " << endl
							 << "    Configuration directory (default is ../../conf)" << endl << endl
							 << "[-aimlDirectory or -ad] " << endl
							 << "    AIML directory with *.aiml files (default is ../../aiml/annotated_alice)" << endl << endl
							 << endl;
						exit(0);
					}
					else
					{
						cout <<
							"[Illegal argument of " +
							string(args[i]) +
							" found]"
							<< endl;
					}
				}
				else
				{
					/*
					 * We already encountered the switch,
					 * now we just need to set the argument
					 */
					if(m_currentArgument == AIML)
					{
						m_aimlDirectory = argument;
					}
					else if(m_currentArgument == AIML_SCHEMA)
					{
						m_aimlSchemaPath = argument;
					}
					else if(m_currentArgument == BOT_SCHEMA)
					{
						m_botConfigurationSchemaPath = argument;
					}
					else if(m_currentArgument == COMMON_SCHEMA)
					{
						m_commonTypesSchemaPath = argument;
					}
					else if(m_currentArgument == CONFIGURATION)
					{
						m_configurationDirectory = argument;
					}
					else
					{
						cout << "Programmer error "
							    "this should not be reached"
							 << endl;
					}

					m_currentArgument = NO_ARG;
				}
			}
		}

		/**
		 * Returns the configuration
		 * directory that has been set.
		 *
		 * \return The configuration directory
		 * where needed configuration files
		 * are stored.
		 */
		string getConfigurationDirectory() const
		{
			return m_configurationDirectory;
		}

		/**
		 * Returns the AIML
		 * directory that has been set.
		 *
		 * \return The AIML directory
		 * where aiml files are stored.
		 */
		string getAimlDirectory() const
		{
			return m_aimlDirectory;
		}

		/**
		 * Returns the AIML schema
		 * path that has been set.
		 *
		 * \return The AIML schema
		 * path that has been set.
		 */
		string getAimlSchemaPath() const
		{
			return m_aimlSchemaPath;
		}

		/**
		 * Returns the common types
		 * schema path that has been set.
		 *
		 * \return The common types
		 * schema path that has been set.
		 */
		string getCommonTypesSchemaPath() const
		{
			return m_commonTypesSchemaPath;
		}

		/**
		 * Returns the bot configuration
		 * schema path that has been set.
		 *
		 * \return the bot configuration
		 * schema path that has been set.
		 */
		string getBotConfigurationSchemaPath() const
		{
			return m_botConfigurationSchemaPath;
		}

		/**
		 * Enumeration of possible switches you
		 * can give rebecca
		 */
		enum arguments{ NO_ARG, AIML, AIML_SCHEMA,
			            COMMON_SCHEMA, BOT_SCHEMA,
						CONFIGURATION};
		/**
		 * The current argument state.
		 */
		arguments m_currentArgument;

		/**
		 * The location of RebeccaAIML's
		 * configuration directory.
		 *
		 * If this is not set, it will default
		 * to the current working directory
		 * + "../../conf".  This is where
		 * needed configuration files are
		 * stored.
		 */
		string m_configurationDirectory;

		/**
		 * The location of a AIML directory.
		 *
		 * If this is not set, it will default
		 * to the current working directory
		 * + "../../aiml/annotated_alice".
		 */
		string m_aimlDirectory;

		/**
		 * The path to the aiml xsd file.
		 *
		 * If this is not set, it will default
         * to the current working directory +
		 * "../../resources/schema/AIML.xsd"
		 */
		string m_aimlSchemaPath;

		/**
		 * The path to the the common types
		 * schema file.
		 *
		 * If this is not set, it will default
         * to the current working directory +
		 * "../resources/schema/common-types.xsd"
		 */
		string m_commonTypesSchemaPath;

		/**
		 * The path to the bot configuration
		 * schema file.
		 *
		 * If this is not set, it will default
         * to the current working directory +
		 * "../resources/schema/bot-configuration.xsd"
		 */
		string m_botConfigurationSchemaPath;


};

#endif /* ARGUMENTS_H_ */
