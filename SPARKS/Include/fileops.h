/// @file fileops.h
/// @brief os-independent file operations declarations.

#ifndef __FILEOPS_H
#define __FILEOPS_H

#include <stdio.h>
#include <stdlib.h>


/// Get the length of a file

int fileLength(FILE *f);


/// Transform a string containing file paths in configuration file format, that is,
/// in unix format and relative to the virtual root of the application data directory
/// defined in the PSISBAN_APPLICATION_DATA environment variable, into OS-friendly
/// full file paths. This complete path ends with no slash.
/// \param filePathRelativeToAppplicationData input path
/// \param fullOsFriendlyFilePath output string with os-dependent path
/// \param len supplied output string lenght

void getApplicationDataFileFullPath(
	char *filePathRelativeToAppplicationData,
	char *fullOsFriendlyFilePath,
	int lenght
);


/// Transform string containing file paths in configuration file format, that is,
/// in unix format and relative to the virtual root of the test data directory
/// defined in the PSISBAN_TEST_DATA environment variable, into OS-friendly
/// full file paths. This complete path ends with no slash.
/// \param filePathRelativeToAppplicationData input path
/// \param fullOsDependentFilePaht string buffer to store the output os-dependent path.
/// \param len, fullOsDependentFilePaht string buffer lenght

void getApplicationTestFileFullPath(
	/* IN  */ char *filePathRelativeToAppplicationData,
	/* OUT */ char *fullOsDependentFilePaht,
	/* IN */ int length
);


/// Reads the content of a text file into a memory buffer of length 'len'.

void readTextFile(char *absoluteFilePath, char *buffer, int len);


/// Strips leading path if any

void basename(char *inputFilePath, char *outputFileBaseName, int outputFileBaseNameLength);


#endif
