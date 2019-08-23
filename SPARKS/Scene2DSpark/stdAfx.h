// stdafx.h: archivo de inclusion de los archivos de inclusion estandar del sistema
// o archivos de inclusion especificos de un proyecto utilizados frecuentemente,
// pero rara vez modificados
//

#pragma once

#include "config.h"

#ifndef _WIN32_WINNT		// Permitir el uso de caracteristicas especificas de Windows XP o posterior.
#define _WIN32_WINNT 0x0501	// Cambiar al valor apropiado correspondiente a otras versiones de Windows.
#endif

#define WIN32_LEAN_AND_MEAN		// Excluir material rara vez utilizado de encabezados de Windows

// TODO: mencionar aqui los encabezados adicionales que el programa necesita
#ifdef _WIN32
#include <windows.h>
#else
#endif
#ifdef _WIN32
#include <gl/GL.h>
#else
//usr/Include/GL NO DEPENDENCIAS!!!
#include <GL/gl.h>
#endif
//usr/Include/GL NO DEPENDENCIAS!!!!
#include <GL/glext.h>

#include "Configuration.h"
#include "ErrorHandling.h"
#include "Logger.h"
