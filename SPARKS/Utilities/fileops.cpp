/// @file fileops.cpp
/// @brief os-independent file operations implementation.

#include "stdafx.h"
#include "environment.h"
//OJO puesto por mi para LINUX
#include "fileops.h"

/// Obtain file lenght in bytes.

int fileLength(FILE *f)
{
	int pos;
	int end;

	pos = ftell (f);
	fseek (f, 0, SEEK_END);
	end = ftell (f);
	fseek (f, pos, SEEK_SET);

	return end;
}


static void replaceSlashes(char *s) {
	char *p = s;

	while (*p) {
		if (*p == '/') *p = '\\';
		p++;
	}
}


void getApplicationDataFileFullPath(
	/* IN  */ char *filePathRelativeToAppplicationData,
	/* OUT */ char *fullOsDependentFilePaht,
	/* IN */ int length
)
{
	char filePathRelativeToAppplicationDataCopy[16384];
	strncpy(filePathRelativeToAppplicationDataCopy, filePathRelativeToAppplicationData, 16384);

	if (filePathRelativeToAppplicationDataCopy[0] != '/') {
		ERR("Path '%s' does not begin with '/'", filePathRelativeToAppplicationDataCopy);
	}
#ifdef _WIN32
	// change '/' by '\'.
	replaceSlashes(filePathRelativeToAppplicationDataCopy);
#else
#endif

	// Application directory in win32 format
	char *appDataDir = getenv(PSISBAN_APPLICATION_DATA_DIR_ENVIRIONMEN_VAR);
	if (NULL == appDataDir) {
		ERR(
			"Environment variable '%s' not found", 
			PSISBAN_APPLICATION_DATA_DIR_ENVIRIONMEN_VAR
		);
	}

	// Eliminate appDataDir trailing '\' if any
	if (appDataDir[strlen(appDataDir) - 1] == '\\') {
		appDataDir[strlen(appDataDir) - 1] = 0;
	}

	_snprintf(fullOsDependentFilePaht, length, "%s%s", appDataDir, filePathRelativeToAppplicationDataCopy);
}



void getApplicationTestFileFullPath(
	/* IN  */ char *filePathRelativeToAppplicationData,
	/* OUT */ char *fullOsDependentFilePaht,
	/* IN */ int length
)
{
	if (filePathRelativeToAppplicationData[0] != '/') {
		ERR("Path '%s' does not begin with '/'", filePathRelativeToAppplicationData);
	}

	// change '/' by '\'.
	replaceSlashes(filePathRelativeToAppplicationData);

	// Test directory in win32 format
	char *appDataDir = getenv(PSISBAN_TEST_DATA_DIR_ENVIRIONMEN_VAR);
	if (NULL == appDataDir) {
		ERR(
			"Environment variable '%s' not found", 
			PSISBAN_TEST_DATA_DIR_ENVIRIONMEN_VAR
		);
	}

	// Eliminate testDataDir trailing '\' if any
	if (appDataDir[strlen(appDataDir) - 1] == '\\') {
		appDataDir[strlen(appDataDir) - 1] = 0;
	}

	_snprintf(fullOsDependentFilePaht, length, "%s%s", appDataDir, filePathRelativeToAppplicationData);
}



/// Reads a text file into a heap allocated buffer that must be freed by the caller.

void readTextFile(char *absoluteFilePath, char *buffer, int len) {
	FILE *f;

	f = NULL;
#ifdef _WIN32
	fopen_s(&f, absoluteFilePath, "r");
#else
	freopen(absoluteFilePath,"r",f);
#endif
	if (!f) ERR("Error abriendo fichero '%s'", absoluteFilePath);

	//int longitudFichero = fileLength(f);

	int r = (int)fread(buffer, 1, len, f);

	if (r >= len) {
		ERR(
			"Buffer too small reading text file %s, buffer size: %d",
			absoluteFilePath,
			len
		);
	}

	buffer[r] = 0;

	fclose(f);
}


char *basename(char *inputFilePath) {
	static char outputFileBaseName[1024];

	char *p = NULL;

	// buscar �ltimo backslash
	p = strrchr(inputFilePath, '\\');
	
	// si no se encuentra o si s�
	char *output = (p == NULL) ? inputFilePath : p + 1;

	strncpy(outputFileBaseName, output, 1024);

	return outputFileBaseName;
}

