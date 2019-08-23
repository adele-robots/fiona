/// @file Logger.h
/// @brief Application logging functions declarations, both ASCII and wide-character.

#ifndef __LOGGER_H
#define __LOGGER_H


const int MAX_LOG_MESSAGE_LENGHT = 2048;


void LoggerBasicInit(void);
void LoggerInit();

void LoggerInfo(const char *msg, ...);
void LoggerWarn(const char *msg, ...);
void LoggerError(const char *msg, ...);

void LoggerInfo(const wchar_t *msg, ...);
void LoggerWarn(const wchar_t *msg, ...);
void LoggerError(const wchar_t *msg, ...);


#endif 
