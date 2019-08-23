/// @file config.h
/// @brief Solution-wide header file for compile-time configuration.

#ifndef __CONFIG_H
#define __CONFIG_H

//Para los movimientos de la cabeza
//#define DAVID_MTRON

//LINUX
#ifdef _WIN32
#else
#define _snprintf snprintf
#define vsprintf_s vsnprintf
#define vswprintf_s vswprintf
#define strncpy_s strncpy

#endif

//Para cambiar entre un solo ini y un ini por componente
//#define GENERAL_INI
//Para funcionar en 2D y evitar cargar codigo de 3D
//#define PABLO_2D

// Evitar warnings de funciones tradicionales de la biblioteca est�ndar de C.
#define _CRT_SECURE_NO_DEPRECATE 1
#define _CRT_SECURE_NO_WARNINGS 1

// Usado si se compila contra la versi�n estable de Horde3d, 1.0 beta 4
//#define HORDE_10B4 

// Usado si se compila contra la versi�n del SVN de Horde3d, 1.0 beta 5
#define HORDE_10B5

// FFMpeg needs this
#define __STDC_CONSTANT_MACROS 1


// And this. In ffmpeg headers:
// conversi�n de 'int64_t' a 'int32_t'; posible p�rdida de datos
#ifdef _WIN32
#pragma warning (disable:4244) 
#else
#endif

// /GS no puede proteger los par�metros ni variables locales de la saturaci�n del 
// b�fer local porque las optimizaciones est�n deshabilitadas en la funci�n
// OJO
#ifdef _WIN32
#pragma warning(disable:4748) 
#else
#endif

// Los nombres de las bibliotecas de OpenCV cambian constantemente

#define OPENCV_CORE_LIB_RELEASE			"opencv_core231.lib"//"opencv_core229.lib"
#define OPENCV_IMGPROC_LIB_RELEASE		"opencv_imgproc231.lib"
#define OPENCV_OBJDETECT_LIB_RELEASE	"opencv_objdetect231.lib"
#define OPENCV_HIGHGUI_LIB_RELEASE		"opencv_highgui231.lib"
#define OPENCV_VIDEO_LIB_RELEASE		"opencv_video231.lib"
#define OPENCV_LEGACY_LIB_RELEASE		"opencv_legacy231.lib"


#define OPENCV_CORE_LIB_DEBUG			"opencv_core231d.lib"
#define OPENCV_IMGPROC_LIB_DEBUG		"opencv_imgproc231d.lib"
#define OPENCV_OBJDETECT_LIB_DEBUG		"opencv_objdetect231d.lib"
#define OPENCV_HIGHGUI_LIB_DEBUG		"opencv_highgui231d.lib"
#define OPENCV_VIDEO_LIB_DEBUG			"opencv_video231d.lib"
#define OPENCV_LEGACY_LIB_DEBUG			"opencv_legacy231d.lib"


// Integraci�n con app de terceros mediante custom proto TCP/IP
// usado en Muja, Mijas y Robotcion
// #define FEATURE_CUSTOM_PROTO_TCPIP_INTEGRATION


// Translate HTTP request method name, de OPTIONS a GET
#ifdef _WIN32
#define HTTP_TRANSLATE_OPTIONS_METHOD 1
#else
#endif


#endif
