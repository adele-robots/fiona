/// @file Run.cpp
/// @brief Run.exe implementation.


#include "stdAfx.h"
#include <stdio.h>
#include <signal.h>
#include "pugixml.hpp"
#include "fileops.h"
#include "ErrorHandling.h"
#include "Logger.h"
#include "ComponentSystem.h"



void doThings(char *componentNetworkFileName, char * configFile) {
	ComponentSystem componentSystem;
	componentSystem.run(componentNetworkFileName, configFile);
}


int main(int argc, char *argv[])
{
	LoggerInit();

	if (argc != 2 && argc != 3) {
		fprintf(stderr, "%s: <component system file> [configuration file]\n", basename(argv[0]));
		return -1;
	}

	try {
		doThings(argv[1], argc==3?argv[2]:NULL);
		/*	A veces, el proceso se queda colgado, muy probablemente relacionado con X11 y OpenGL.
		 *  Por tanto, es necesario que se mate a si mismo para una correcta limpieza.
		 */
		kill(getpid(),SIGKILL);
	}
	catch(Exception &ex) {
		LoggerError(ex.msg);
	}
	catch(...) {
		LoggerError("Unknown exception");
	}

	return 0;
}
