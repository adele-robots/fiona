#pragma once

#include "config.h"

#ifndef _WIN32_WINNT		// Permitir el uso de caracter�sticas espec�ficas de Windows XP o posterior.                   
#define _WIN32_WINNT 0x0501	// Cambiar al valor apropiado correspondiente a otras versiones de Windows.
#endif						

#include <stdio.h>

#ifdef _WIN32
#include <tchar.h>
#else
#endif

#include "ErrorHandling.h"
#include "Configuration.h"

