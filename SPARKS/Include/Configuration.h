/// @file Configuration.h
/// @brief psisban::Config class and mainConfig global variable declarations.

#ifndef __CONFIGURATION_H
#define __CONFIGURATION_H


#pragma warning( disable : 4290 ) /* libconfig throw specs */

#include <libconfig.h++>
#include <string>

namespace psisban {

/// \brief Libconfig wrapper. 
///
/// Offers configuration-file resources access integrated with the
/// application error handling machinery.\n
/// File paths are stored in unix format referred to a virtual root defined
/// in the environment variable PSISBAN_APPLICATION_DATA.

class Config  {
	public:
		libconfig::Config configuration;
		void loadConfiguration(char *configurationFileAbsolutePath);
		#ifdef _WIN32
		void psisban::Config::getString(char *settingPath, char *buff, int len);
		std::string psisban::Config::getString(char *settingPath);
		#else
		void getString(char *settingPath,char *buff,int len);
		std::string getString(char *settingPath);
		#endif
		int getInt(char *settingPaht);
		float getFloat(char *settingPath);
		bool getBool(char *settingPath);
		int getLength(char *settingPath);
		void getFilePath(char *settingPath, char *filePath, int len);
		std::string getUserDir();
};

}

extern psisban::Config mainConfig;


#endif
