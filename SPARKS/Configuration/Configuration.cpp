/// @file Configuration.cpp
/// @brief psisban::Config class implementation.


#include "stdafx.h"
#include <stdlib.h>

#include <Logger.h>

#pragma warning( disable : 4290 ) /* libconfig throw specifications */

#include "libconfig.h++"

#include "ErrorHandling.h"
#include "Configuration.h"
#include "environment.h"
#include "fileops.h"



using namespace libconfig;

//TODO: depender s�lo de la versi�n de release
#ifdef _WIN32
#ifdef _DEBUG
#pragma comment(lib, "libconfig++D.lib")
#else
#pragma comment(lib, "libconfig++.lib")
#endif
#else
#endif


#define SETTING_PATH(x) (x.getPath() ? x.getPath() : "setting-path-unavailable")

// Global variable for all PSISBAN
psisban::Config mainConfig;


/// Loads a configuration file from persistent storage
/// \param configurationFileAbsolutePath Absolute path of the configuration file.

void psisban::Config::loadConfiguration(char *configurationFileAbsolutePath) {
	FILE *configFile;

	configFile = fopen(configurationFileAbsolutePath, "rt");

	if (!configFile) {
		LoggerError("[FIONA-logger]Error opening configuration. Check RemoteCharacterEmbodiment3DSpark for character selection",
				configurationFileAbsolutePath);
		ERR(
			"Error opening application configuration file '%s'", 
			configurationFileAbsolutePath
		);
	}

	try {
		configuration.read(configFile);
		}
	catch (FileIOException &) {
		LoggerError("[FIONA-logger]Error reading/writing to configuration file ",
				configurationFileAbsolutePath);
		ERR(
			"Error reading/writing to configuration file '%s'", 
			configurationFileAbsolutePath
		);
	}
	catch (SettingTypeException &ex) {
		LoggerError("[FIONA-logger]Setting value of bad type %s in configuration file ",
			SETTING_PATH(ex),
			configurationFileAbsolutePath);
		ERR(
			"Setting value of bad type %s in configuration file %s", 
			SETTING_PATH(ex),
			configurationFileAbsolutePath
		);
	}
	catch (ParseException &ex) {
		LoggerError("[FIONA-logger]Parse error reading configuration file\n(%d): %s",
			ex.getLine(),
			ex.getError());
		ERR("Parse error reading configuration file\n%s (%d): %s",
			configurationFileAbsolutePath,
			ex.getLine(),
			ex.getError()
		);
	}

	configuration.setAutoConvert(true);

	fclose(configFile);
}

#define GET_SETTING_WITH_PSISBAN_EXCEPTIONS(thingsToDo)    \
  try {                                                    \
    thingsToDo;                                            \
  }                                                        \
  catch (SettingTypeException &ex) {                       \
	LoggerError("[FIONA-logger]Bad type in setting '%s'", ex.getPath()); \
    ERR("Bad type in setting '%s'", ex.getPath());         \
  }                                                        \
  catch (SettingNotFoundException &ex) {                   \
	LoggerError("[FIONA-logger]Setting '%s' not found", ex.getPath());\
    ERR("Setting '%s' not found", ex.getPath());           \
  }


/// Reads the string value of a setting.
/// \param settingPath The setting path as a dot separated chain of keywords of the 
/// config file.
/// \param buff Output string store space.
/// \param len Maximum lenght of the output buffer.

void psisban::Config::getString(char *settingPath, char *buff, int len) {
	GET_SETTING_WITH_PSISBAN_EXCEPTIONS(
		strncpy(buff, configuration.lookup(settingPath), len)
	);
}

/// Reads a string.

std::string psisban::Config::getString(char *settingPath) {
	std::string res;

	GET_SETTING_WITH_PSISBAN_EXCEPTIONS(
			res = (const char *)configuration.lookup(settingPath);
	);

	return res;
}

/// Reads an integer.

int psisban::Config::getInt(char *settingPath) {
	int res;

	GET_SETTING_WITH_PSISBAN_EXCEPTIONS(
		res = configuration.lookup(settingPath);
	);

	return res;
}


/// Read a float.

float psisban::Config::getFloat(char *settingPath) {
	float res;

	GET_SETTING_WITH_PSISBAN_EXCEPTIONS(
		res = configuration.lookup(settingPath);
	);

	return res;
}


/// Reads a boolean.

bool psisban::Config::getBool(char *settingPath) {
	bool res;

	GET_SETTING_WITH_PSISBAN_EXCEPTIONS(
		res = configuration.lookup(settingPath);
	);

	return res;
}


/// When a setting is composed of a group of subsettings, returns the number
/// of subsettings.

int psisban::Config::getLength(char *settingPath) {
	GET_SETTING_WITH_PSISBAN_EXCEPTIONS(
		{
			Setting &s = configuration.lookup(settingPath);
			if (!(s.isArray() || s.isList())) {
				ERR("The setting '%s' is not an array or list", settingPath);
			};
			return s.getLength();
		}
	);
}


/// Interprets a setting as a file path relative to application data
/// and returns the OS-friendly absolute file path.

void psisban::Config::getFilePath(char *settingPath, char *filePath, int len) {
	char virtualPath[2048];

	// get unix-style path relative to application data dir
	getString(settingPath, virtualPath, 2048);

	if (virtualPath[0] != '/') {
		ERR("Path '%s' of setting '%s' does not begin with '/'", virtualPath, settingPath);
	}

	getApplicationDataFileFullPath(virtualPath, filePath, len);
}

//Returns the user's working directory
std::string psisban::Config::getUserDir() {
	std::string userWorkingDir;
	char * userDir = getenv(PSISBAN_APPLICATION_DATA_DIR_ENVIRIONMEN_VAR);
	if (userDir == NULL) {
		ERR(
				"Environment variable '%s' not found",
				PSISBAN_APPLICATION_DATA_DIR_ENVIRIONMEN_VAR
		);
	}else{
		userWorkingDir = userDir;
	}
	userWorkingDir.append("/");
	userWorkingDir.append(USER_WORKING_DIRECTORY_NAME);
	return userWorkingDir;
}
