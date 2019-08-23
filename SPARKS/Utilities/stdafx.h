#pragma once

#include "config.h"


#ifdef _WIN32
#ifndef _WIN32_WINNT		// Permitir el uso de caracter�sticas espec�ficas de Windows XP o posterior.                   
#define _WIN32_WINNT 0x0501	// Cambiar al valor apropiado correspondiente a otras versiones de Windows.
#endif						

#define WIN32_LEAN_AND_MEAN		// Excluir material rara vez utilizado de encabezados de Windows

#include <winsock2.h>
#include <windows.h>

#else
#endif

//OJO CHANGED!!!!
//#include "Configuration.h"
#include "ErrorHandling.h"

