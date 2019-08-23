/// @file Logger.cpp
/// @brief Logging functions on top of log4cpp library services.

#include "stdafx.h"
#include <log4cplus/logger.h>
#include <log4cplus/configurator.h>
#include <log4cplus/helpers/loglog.h>
#include <log4cplus/helpers/stringhelper.h>

#include <sys/stat.h> 

#include "Logger.h"
#include "environment.h"

// OJO
#ifdef _DEBUG
#pragma comment(lib, "log4cplusD.lib")
#else
#pragma comment(lib, "log4cplus.lib")
#endif



using namespace log4cplus;
using namespace log4cplus::helpers;


/// Initialization previous to the loading of the properties of the logging system.

void LoggerBasicInit(void) {
    BasicConfigurator config;
    config.configure();
}


/// Initialization of the logging system.
/// The properties file must be in %PSISBAN_APPLICATION_DATA%/psisban-logger.properties

void LoggerInit(void) {
	char *psisbanDataDir = getenv(PSISBAN_APPLICATION_DATA_DIR_ENVIRIONMEN_VAR);
	if (psisbanDataDir == NULL) {
		fprintf(
			stderr,
			"Undefined environment variable '%s' indicating "
			"the absolute path of the application data directory", 
			PSISBAN_APPLICATION_DATA_DIR_ENVIRIONMEN_VAR
		);
		exit(1);
	}

	// remove trailing backslash if any
	int lastCharIndex = (int)strlen(psisbanDataDir) - 1;
	if (psisbanDataDir[lastCharIndex] == '\\') {
		psisbanDataDir[lastCharIndex] = 0;
	}

	char loggerPropertiesFile[2048];
	_snprintf(
		loggerPropertiesFile, 
		2048, 
#ifdef _WIN32
		"%s\\psisban-logger.properties",
#else
		"%s/psisban-logger.properties",
#endif
		psisbanDataDir
	);

	struct stat stFileInfo; 

	if (stat(loggerPropertiesFile, &stFileInfo) != 0) {
		fprintf(stderr, "Error abriendo el fichero de configuraci�n de logging.\n");
		perror(loggerPropertiesFile);
		exit(1);
	}


	PropertyConfigurator::doConfigure(LOG4CPLUS_C_STR_TO_TSTRING(loggerPropertiesFile));
}


void LoggerInfo(const char *msg, ...) {
	char logMessage[MAX_LOG_MESSAGE_LENGHT];
    va_list pArgs;
    va_start(pArgs, msg);
    vsprintf_s(logMessage, MAX_LOG_MESSAGE_LENGHT, msg, pArgs);
    va_end(pArgs);

	Logger logger = Logger::getInstance(LOG4CPLUS_C_STR_TO_TSTRING(__FUNCTION__));
	LOG4CPLUS_INFO(logger, logMessage);
}

void LoggerWarn(const char *msg, ...) {
	char logMessage[MAX_LOG_MESSAGE_LENGHT];
    va_list pArgs;
    va_start(pArgs, msg);
    vsprintf_s(logMessage, MAX_LOG_MESSAGE_LENGHT, msg, pArgs);
    va_end(pArgs);

	Logger logger = Logger::getInstance(LOG4CPLUS_C_STR_TO_TSTRING(__FUNCTION__));
	LOG4CPLUS_WARN(logger, logMessage);
}

void LoggerError(const char *msg, ...) {
	char logMessage[MAX_LOG_MESSAGE_LENGHT];
    va_list pArgs;
    va_start(pArgs, msg);
    vsprintf_s(logMessage, MAX_LOG_MESSAGE_LENGHT, msg, pArgs);
    va_end(pArgs);

	Logger logger = Logger::getInstance(LOG4CPLUS_C_STR_TO_TSTRING(__FUNCTION__));
	LOG4CPLUS_ERROR(logger, logMessage);
}


//=== WIDE CHAR

void LoggerInfo(const wchar_t *msg, ...) {
	wchar_t logMessage[MAX_LOG_MESSAGE_LENGHT];
    va_list pArgs;
    va_start(pArgs, msg);
    vswprintf_s(logMessage, MAX_LOG_MESSAGE_LENGHT, msg, pArgs);
    va_end(pArgs);

	Logger logger = Logger::getInstance(LOG4CPLUS_C_STR_TO_TSTRING(__FUNCTION__));
	LOG4CPLUS_INFO(logger, logMessage);
}

void LoggerWarn(const wchar_t *msg, ...) {
	wchar_t logMessage[MAX_LOG_MESSAGE_LENGHT];
    va_list pArgs;
    va_start(pArgs, msg);
    vswprintf_s(logMessage, MAX_LOG_MESSAGE_LENGHT, msg, pArgs);
    va_end(pArgs);

	Logger logger = Logger::getInstance(LOG4CPLUS_C_STR_TO_TSTRING(__FUNCTION__));
	LOG4CPLUS_WARN(logger, logMessage);
}

void LoggerError(const wchar_t *msg, ...) {
	wchar_t logMessage[MAX_LOG_MESSAGE_LENGHT];
    va_list pArgs;
    va_start(pArgs, msg);
    vswprintf_s(logMessage, MAX_LOG_MESSAGE_LENGHT, msg, pArgs);
    va_end(pArgs);

	Logger logger = Logger::getInstance(LOG4CPLUS_C_STR_TO_TSTRING(__FUNCTION__));
	LOG4CPLUS_ERROR(logger, logMessage);
}

