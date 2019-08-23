/// @file ErrorHandling.h
/// @brief Global exception class definition, and error macro to throw it.

#ifndef __ERROR_HANDLING_H
#define __ERROR_HANDLING_H

#include <string.h>


const int MAX_EXCEPTION_MESSAGE_LENGHT = 4096;

#ifdef _WIN32

#define ERR(...)																\
{																				\
	char __errorHandlingMsg[MAX_EXCEPTION_MESSAGE_LENGHT];						\
	char __errorHandlingErr[MAX_EXCEPTION_MESSAGE_LENGHT];						\
	sprintf_s(__errorHandlingErr, MAX_EXCEPTION_MESSAGE_LENGHT, __VA_ARGS__);	\
	sprintf_s(																	\
		__errorHandlingMsg,														\
		MAX_EXCEPTION_MESSAGE_LENGHT,											\
		"Function: %s\nModule: %s (%d)\nMessage: \n%s\n",						\
		__FUNCTION__,															\
		__FILE__,																\
		__LINE__,																\
		__errorHandlingErr														\
	);																			\
	throw (::Exception(__errorHandlingMsg));									\
}

#else

#define ERR(...)																\
{																				\
	char __errorHandlingMsg[MAX_EXCEPTION_MESSAGE_LENGHT];						\
	char __errorHandlingErr[MAX_EXCEPTION_MESSAGE_LENGHT];						\
	snprintf(__errorHandlingErr, MAX_EXCEPTION_MESSAGE_LENGHT, __VA_ARGS__);	\
	snprintf(																	\
		__errorHandlingMsg,														\
		MAX_EXCEPTION_MESSAGE_LENGHT,											\
		"Function: %s\nModule: %s (%d)\nMessage: \n%s\n",						\
		__FUNCTION__,															\
		__FILE__,																\
		__LINE__,																\
		__errorHandlingErr														\
	);																			\
	throw (::Exception(__errorHandlingMsg));									\
}

#endif

/// Top level exception class carrying a message.

class Exception {
public:
	char msg[MAX_EXCEPTION_MESSAGE_LENGHT];
	Exception(char *s) { 
		strncpy(
			msg, 
			s,
			MAX_EXCEPTION_MESSAGE_LENGHT
		);
	}
};


#endif
