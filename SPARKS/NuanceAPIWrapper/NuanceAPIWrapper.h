/*
 * NuanceAPIWrapper.h
 *
 *  Created on: 08/05/2015
 *      Author: adele
 */

#ifndef NUANCEAPIWRAPPER_H_
#define NUANCEAPIWRAPPER_H_

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

extern "C" {
	#include "vprintf.h"
	#include "ve_ttsapi.h"
	#include "vplatform.h"
	#include "vplatform_tchar.h"
	#include "vheap.h"
	#include "vfile.h"
}

#include <string>
#include <functional>

/* ******************************************************************
**  DEFINITIONS
** ******************************************************************/

#define PCM_BUF_SIZE   0x1000 /* in bytes */
#define MRK_BUF_SIZE      100 /* in VE_MARKINFO structs */

/* ******************************************************************
**  GLOBALS
** ******************************************************************/

#define MAX_DATA_INSTALL_PATHS 64

typedef std::function<void(void*, NUAN_U32)> CallbackType;

class NuanceAPIWrapper {
public:
	NuanceAPIWrapper();
	virtual ~NuanceAPIWrapper();

	// Functions
	int initParams(CallbackType, const char *, const char *, const char *, int);
	void synthesize(std::string);
	void close();
	void stop();
	bool isReady() const;
	NUAN_ERROR CbOutNotifyToFile(VE_HINSTANCE hTtsInst, VE_OUTDEV_HINSTANCE hOutDevInst, VE_CALLBACKMSG * pcbMessage, VE_USERDATA UserData);

private:
	// Functions
	int ParseInputArgsFromArgv(const char * NUANCE_PATH, const char * voice, const char * voiceModel, int hz);
	char * NUAN_APP_tcscpy(char * dst, const PLATFORM_TCHAR * src);
	void NUAN_APP_Print(const char *szNString, const PLATFORM_TCHAR *szPString);
	int exitClose();
	int exitUnInit();
	int exitRelease();
	int exitMain();

	// Attributes

	int  					mainRc;
	NUAN_ERROR				nErrcode;
	VPLATFORM_FILE_H		fPCM;  /* file pointer for output PCM file */
	VE_INSTALL				stInstall;
	VPLATFORM_RESOURCES		stResources;
	VE_HSPEECH				hSpeech;
	VE_HINSTANCE			hTtsInst;
	VE_OUTDEVINFO			stOutDevInfo;
	VE_INTEXT				stInText;
	NUAN_U32				nTextLen;
	void					* pText;
	NUAN_U16				i, j, k;
	NUAN_U16				nItem, nItem2, nItem3;
	VE_LANGUAGE				* pLangList;
	VE_VOICEINFO			* pVoiceList;
	VE_NTSINFO				NtsInfo;
	VE_CLMINFO				ClmInfo;
	VE_SPEECHDBINFO			* pSpeechDBList;
	void					* pBinBrokerInfo;
	NUAN_U32				u32BrokerSize;
	NUAN_U32				iRes;
	VE_PRODUCT_VERSION		ttsProductVersion;

	PLATFORM_TCHAR szTextFile[VE_MAX_STRING_LENGTH];
	PLATFORM_TCHAR szOutFile[VE_MAX_STRING_LENGTH];
	VE_PARAM TtsParam[16];
	NUAN_U16 cTtsParam;
	NUAN_BOOL bExtraInfo;
	NUAN_BOOL bNoOutput;
	NUAN_S16 cDirScanning;
	VE_TEXTFORMAT eTextFormat;
	NUAN_S16 * pPcmData;
	VE_MARKINFO * pMrkData;
	NUAN_U16 g_u16NbrOfDataInstallPaths;
	PLATFORM_TCHAR g_aszDataInstallPaths[MAX_DATA_INSTALL_PATHS][VE_MAX_STRING_LENGTH];
	PLATFORM_TCHAR * g_apDataInstallPaths[MAX_DATA_INSTALL_PATHS];
	PLATFORM_TCHAR g_szBrokerInfoFile[VE_MAX_STRING_LENGTH];
	VE_HEAP_HINSTANCE hHeap;
	VE_DATA_HCLASS    hDataClass;


	CallbackType callback_;
	bool stopSynth;
	bool ready;
	/*pthread_cond_t condition_stop;
	pthread_mutex_t mutex;*/

};

#endif /* NUANCEAPIWRAPPER_H_ */
