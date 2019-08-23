/*
 * NuanceAPIWrapper.cpp
 *
 *  Created on: 08/05/2015
 *      Author: adele
 */

#include "NuanceAPIWrapper.h"

NuanceAPIWrapper::NuanceAPIWrapper() {
	mainRc = 0;
	nErrcode = 0;
	fPCM = NULL;
	pText = NULL;
	i = 0, j = 0, k = 0;
	nItem = 0, nItem2 = 0, nItem3 = 0;
	pBinBrokerInfo = NULL;
	u32BrokerSize = 0;
	iRes = 0;

	memset(szTextFile, 0, VE_MAX_STRING_LENGTH);
	memset(szOutFile, 0, VE_MAX_STRING_LENGTH);
	cTtsParam = 0;
	bExtraInfo = NUAN_FALSE;
	bNoOutput = NUAN_FALSE;
	cDirScanning = -1;
	eTextFormat = VE_NORM_TEXT;
	pPcmData = NULL;
	pMrkData = NULL;
	g_u16NbrOfDataInstallPaths = 0;
	memset(g_szBrokerInfoFile, 0, VE_MAX_STRING_LENGTH);
	hHeap = NULL;
	hDataClass = NULL;

	stopSynth = false;
	ready = false;
	/*pthread_cond_init(&condition_stop, NULL);
	pthread_mutex_init(&mutex, NULL);*/
}

NuanceAPIWrapper::~NuanceAPIWrapper() {
	close();
}

/*------------------------------------------------------------------*/
char * NuanceAPIWrapper::NUAN_APP_tcscpy(char * dst, const PLATFORM_TCHAR * src)
{
    char * cp = dst;

    /*lint --e(571) */
    while( (*cp = (char)*src) != 0 ) { cp++; src++; }/* Copy src to end of dst */

    return( dst );
} /* NUAN_APP_tcscpy */

/*------------------------------------------------------------------*/
void NuanceAPIWrapper::NUAN_APP_Print(const char *szNString, const PLATFORM_TCHAR *szPString)
{
    char szBuf[VE_MAX_STRING_LENGTH];
    char *cp;
    size_t len;

    /* copy char */
    strcpy(szBuf, szNString);

    /* copy PLATFORM_TCHAR to char */
    len = strlen(szBuf);
    cp = &szBuf[len];
    /*lint --e(571) */
    while( ((*cp = (char) *szPString) != 0) && (len < VE_MAX_STRING_LENGTH-1) ) { cp++; szPString++; len++;}
    *cp = 0;

    vplatform_printf("%s", szBuf);
}

/*------------------------------------------------------------------*/
int NuanceAPIWrapper::ParseInputArgsFromArgv(
  const char *			NUANCE_PATH,
  const char * 		voice,
  const char *			voiceModel,
  int			hz)
{
  int i;
  NUAN_BOOL bLanguage = NUAN_FALSE, bVoice = NUAN_FALSE;

  /* Initialize params with default values */

  memset(TtsParam, 0, sizeof(TtsParam));
  cTtsParam = 0;

  TtsParam[cTtsParam].eID = VE_PARAM_MARKER_MODE;
  TtsParam[cTtsParam++].uValue.usValue = (NUAN_U16) VE_MRK_ON;

  TtsParam[cTtsParam].eID = VE_PARAM_WAITFACTOR;
  TtsParam[cTtsParam++].uValue.usValue = 1;

  /* init mode : no modules loaded */
  TtsParam[cTtsParam].eID = VE_PARAM_INITMODE;
  TtsParam[cTtsParam++].uValue.usValue = VE_INITMODE_LOAD_OPEN_ALL_EACH_TIME  ;


  /* init install path */
  g_aszDataInstallPaths[0][0] = 0;
  g_u16NbrOfDataInstallPaths = 0;

  // -I
  strcpy(g_aszDataInstallPaths[g_u16NbrOfDataInstallPaths], NUANCE_PATH);
  g_u16NbrOfDataInstallPaths++;

  // -T
  TtsParam[cTtsParam].eID = VE_PARAM_TYPE_OF_CHAR;
  TtsParam[cTtsParam].uValue.usValue = VE_TYPE_OF_CHAR_UTF8;
  cTtsParam++;

  // -V
  bVoice = NUAN_TRUE;
  TtsParam[cTtsParam].eID = VE_PARAM_VOICE;
  strcpy(TtsParam[cTtsParam++].uValue.szStringValue, voice);

  // -F
  TtsParam[cTtsParam].eID = VE_PARAM_FREQUENCY;
  TtsParam[cTtsParam++].uValue.usValue = hz;

  // -m
  TtsParam[cTtsParam].eID = VE_PARAM_VOICE_MODEL;
  strcpy(TtsParam[cTtsParam++].uValue.szStringValue, voiceModel);

  // -o
  strcpy(szOutFile, "/tmp/out.pcm");

  /* Check required options */
  if ((g_aszDataInstallPaths[0][0] == 0) && (g_szBrokerInfoFile[0] == 0) && (cDirScanning == -1))
  {
    vplatform_printf("No install path or broker info file specified\n");
    return -1;
  }
  if ((! bLanguage) && (! bVoice))
  {
    vplatform_printf("No language and/or voice specified\n");
    return -1;
  }

  /* init pointers to install paths */
  for (i = 0; i < g_u16NbrOfDataInstallPaths; i++)
  {
    g_apDataInstallPaths[i] = g_aszDataInstallPaths[i];
  }

  return 0;
} /* ParseInputArgsFromArgv */


/*------------------------------------------------------------------*/
static NUAN_ERROR SCbOutNotifyToFile(
  VE_HINSTANCE           hTtsInst,
  VE_OUTDEV_HINSTANCE    hOutDevInst,
  VE_CALLBACKMSG       * pcbMessage,
  VE_USERDATA            UserData)
{
	return ((NuanceAPIWrapper * const)UserData)->CbOutNotifyToFile(hTtsInst, hOutDevInst, pcbMessage, UserData);
}


/*------------------------------------------------------------------*/
NUAN_ERROR NuanceAPIWrapper::CbOutNotifyToFile(
  VE_HINSTANCE           hTtsInst,
  VE_OUTDEV_HINSTANCE    hOutDevInst,
  VE_CALLBACKMSG       * pcbMessage,
  VE_USERDATA            UserData)
{
  VE_OUTDATA      *pTtsOutData;
  VPLATFORM_FILE_H fPCM;
  NUAN_U32         i;
  NUAN_ERROR       ret = NUAN_OK;
  VE_LIPSYNC       TtsLipSync;

  fPCM = (VPLATFORM_FILE_H)hOutDevInst;

  /*lint -save -e788 enum constants not used within switch */
  switch (pcbMessage->eMessage)
  {
  case VE_MSG_BEGINPROCESS:
    /*vplatform_printf("VE_MSG_BEGINPROCESS\n");
    if ((bExtraInfo == NUAN_TRUE) || (bNoOutput == NUAN_TRUE))  vplatform_printf("\n");*/
    stopSynth = false;
    break;

  case VE_MSG_ENDPROCESS:
    /*//if ((bExtraInfo == NUAN_FALSE) && (bNoOutput == NUAN_FALSE))  vplatform_printf("\n");
    vplatform_printf("VE_MSG_ENDPROCESS\n");*/
    stopSynth = false;
	  ready = true;
    break;

  case VE_MSG_OUTBUFDONE:
	/*printf("VE_MSG_OUTBUFDONE\n");*/
	if(stopSynth)
		return 0;
    pTtsOutData = (VE_OUTDATA *)pcbMessage->pParam;
    if ((pTtsOutData->ulPcmBufLen != 0) && (fPCM != NULL))
    {
    	callback_(pTtsOutData->pOutPcmBuf, pTtsOutData->ulPcmBufLen);
    }

    if ((bExtraInfo == NUAN_TRUE) && (bNoOutput == NUAN_FALSE) )
    {
        vplatform_printf("VE_MSG_OUTBUFDONE: %u samples, %u markers\n",
        (unsigned int)pTtsOutData->ulPcmBufLen/2,
        (unsigned int)pTtsOutData->ulMrkListLen);
    }

    for (i = 0;i < pTtsOutData->ulMrkListLen ;i++)
    {
      if (pTtsOutData->pMrkList[i].eMrkType == VE_MRK_PHONEME)
      {
        if ((bExtraInfo == NUAN_TRUE) && (bNoOutput == NUAN_FALSE) )
        {
            vplatform_printf("[Phon=%-3u] %-6u\t",
            (unsigned int)pTtsOutData->pMrkList[i].usPhoneme,
            (unsigned int)pTtsOutData->pMrkList[i].ulDestPos);
        }

        ret = ve_ttsGetLipSyncInfo(hTtsInst,
          pTtsOutData->pMrkList[i].usPhoneme, &TtsLipSync);
        if (ret == 0)
        {
          if ((bExtraInfo == NUAN_TRUE) && (bNoOutput == NUAN_FALSE) )
          {
            vplatform_printf("lipSyncInfo (%s): ", TtsLipSync.szLHPhoneme);
            vplatform_printf("%d %d %d %d %d %d %d %d\n",
                    TtsLipSync.sJawOpen, TtsLipSync.sTeethUpVisible,
                    TtsLipSync.sTeethLoVisible, TtsLipSync.sMouthHeight,
                    TtsLipSync.sMouthWidth, TtsLipSync.sMouthUpturn,
              TtsLipSync.sTonguePos, TtsLipSync.sLipTension);
          }
          else if (bNoOutput == NUAN_FALSE)
          {
            //vplatform_printf("%s", TtsLipSync.szLHPhoneme);
          }
        }
        else if ((bExtraInfo == NUAN_TRUE) && (bNoOutput == NUAN_FALSE) )
        {
          vplatform_printf("lipSyncInfo not available\n");
        }
      }
      else  if (pTtsOutData->pMrkList[i].eMrkType ==  VE_MRK_TEXTUNIT)
      {
        if ((bExtraInfo == NUAN_TRUE) && (bNoOutput == NUAN_FALSE) )
        {
            vplatform_printf("[TextUnit] %u\tSrcPos=%u SrcLen=%u\n",
            (unsigned int)pTtsOutData->pMrkList[i].ulDestPos,
            (unsigned int)pTtsOutData->pMrkList[i].ulSrcPos,
            (unsigned int)pTtsOutData->pMrkList[i].ulSrcTextLen);
        }
        else if (bNoOutput == NUAN_FALSE)
        {
            //vplatform_printf("\n");
        }
      }
      else  if (pTtsOutData->pMrkList[i].eMrkType == VE_MRK_PROMPT)
      {
        if ((bExtraInfo == NUAN_TRUE) && (bNoOutput == NUAN_FALSE) )
        {
            vplatform_printf("[Prompt]\tDestPos=%u SrcPos=%u  ",
            (unsigned int)pTtsOutData->pMrkList[i].ulDestPos,
            (unsigned int)pTtsOutData->pMrkList[i].ulSrcPos);
            vplatform_printf("PromptId=%s\n",pTtsOutData->pMrkList[i].szPromptID);
        }
        else if (bNoOutput == NUAN_FALSE)
        {
            //vplatform_printf("\n");
        }
      }
      else if ((bExtraInfo == NUAN_TRUE) && (bNoOutput == NUAN_FALSE) )
      {
        if (pTtsOutData->pMrkList[i].eMrkType == VE_MRK_WORD)
        {
            vplatform_printf("[Word] %u\tSrcPos=%u SrcLen=%u\n",
            (unsigned int)pTtsOutData->pMrkList[i].ulDestPos,
            (unsigned int)pTtsOutData->pMrkList[i].ulSrcPos,
            (unsigned int)pTtsOutData->pMrkList[i].ulSrcTextLen);
        }
        else if (pTtsOutData->pMrkList[i].eMrkType ==
          VE_MRK_BOOKMARK  )
        {
            vplatform_printf("[BookMark=%u] %u\tSrcPos=%u SrcLen=%u\n",
            (unsigned int)pTtsOutData->pMrkList[i].ulDestPos,
            (unsigned int)pTtsOutData->pMrkList[i].ulMrkId,
            (unsigned int)pTtsOutData->pMrkList[i].ulSrcPos,
            (unsigned int)pTtsOutData->pMrkList[i].ulSrcTextLen);
        }
        else
        {
            vplatform_printf("[Type=%u] %u\tSrcPos=%u SrcLen=%u\n",
            (unsigned int)pTtsOutData->pMrkList[i].eMrkType,
            (unsigned int)pTtsOutData->pMrkList[i].ulDestPos,
            (unsigned int)pTtsOutData->pMrkList[i].ulSrcPos,
            (unsigned int)pTtsOutData->pMrkList[i].ulSrcTextLen);
        }
      }
    }

    break;

  case VE_MSG_OUTBUFREQ:
	/*printf("VE_MSG_OUTBUFREQ\n");*/
    pTtsOutData = (VE_OUTDATA *)pcbMessage->pParam;
    pTtsOutData->pOutPcmBuf   = pPcmData;
    pTtsOutData->ulPcmBufLen  = PCM_BUF_SIZE;
    pTtsOutData->pMrkList     = pMrkData;
    pTtsOutData->ulMrkListLen = MRK_BUF_SIZE*sizeof(VE_MARKINFO);
    break;

  case VE_MSG_STOP:
	/*pthread_mutex_lock(&mutex);
    printf("VE_MSG_STOP\n");
	pthread_cond_signal(&condition_stop);
	pthread_mutex_unlock(&mutex);*/
    break;

  default:
    break;
  }

  /*lint -restore */
  return 0;
} /* CbOutNotifyToFile */

int NuanceAPIWrapper::initParams(CallbackType callback, const char * path, const char * voice, const char * voiceModel, int hz) {

	callback_ = callback;

	  nErrcode = ve_ttsGetProductVersion(&ttsProductVersion);
	  if (nErrcode == NUAN_OK)
	  {
	      //vplatform_printf("\nVocalizer Expressive v%d.%d.%d\n\n", ttsProductVersion.major, ttsProductVersion.minor, ttsProductVersion.maint);
	  } /* end if */

	  /* Parse the command line arguments */
	  if (ParseInputArgsFromArgv(path, voice, voiceModel, hz))
	  {
		  vplatform_printf("Error parsing input arguments\n");
	    return 1;
	  }

	  //vplatform_printf("Initialize\n");

	  memset(&stInstall, 0, sizeof(stInstall));
	  stInstall.fmtVersion = VE_CURRENT_VERSION;

	  /* Get the platform dependent interfaces */
	  memset(&stResources, 0, sizeof(stResources));
	  stResources.fmtVersion = VPLATFORM_CURRENT_VERSION;
	  if (cDirScanning != 1)
	  {
	    stResources.u16NbrOfDataInstall = g_u16NbrOfDataInstallPaths;
	    stResources.apDataInstall = g_apDataInstallPaths;
	  }
	  if (cDirScanning != -1)
	  {
	    stResources.szBinaryBrokerFile  = _T("binbroker.txt");
	    stResources.szFileListFile      = _T("filelist.txt");
	  }
	  nErrcode = vplatform_GetInterfaces(&stInstall, &stResources);
	  if (nErrcode != NUAN_OK)
	  {
	    vplatform_printf("Error (vplatform_GetInterfaces) : 0x%x\n", (unsigned int)nErrcode);
	    mainRc = 1;
	    return exitMain();
	  }

	  /* init globals */
	  hHeap = stInstall.hHeap;
	  hDataClass = stInstall.hDataClass;

	  /* Allocate our PCM and marker buffers */
	  pPcmData = (NUAN_S16 *) vplatform_heap_Malloc(hHeap, PCM_BUF_SIZE);
	  if (pPcmData == NULL)
	  {
	    vplatform_printf("Failed to allocate memory\n");
	    mainRc = 1;
	    return exitRelease();
	  }

	  pMrkData = (VE_MARKINFO *) vplatform_heap_Malloc(hHeap, MRK_BUF_SIZE * sizeof(VE_MARKINFO));
	  if (pMrkData == NULL)
	  {
	    vplatform_printf("Failed to allocate memory\n");
	    mainRc = 1;
	    return exitRelease();
	  }

	  /* Open the output PCM file */
	  if ((szOutFile[0] != 0) && (bNoOutput == NUAN_FALSE))
	  {
	    nErrcode = vplatform_file_Open(hDataClass, hHeap, szOutFile, _T("wb"), &fPCM);
	    if (nErrcode != NUAN_OK)
	    {
	      NUAN_APP_Print("Can't open ", szOutFile);
	      vplatform_printf("\n");
	      mainRc = 1;
	      return exitRelease();
	    }
	  }
	  else
	  {
	    fPCM = NULL;
	  }


	  /* Initialize the engine */
	  nErrcode = ve_ttsInitialize(&stInstall, &hSpeech);
	  if (pBinBrokerInfo != NULL)
	  {
	    vplatform_heap_Free(hHeap, pBinBrokerInfo);
	    /* assure vplatform_ReleaseInterfaces cannot free this as well */
	    stInstall.pBinBrokerInfo = NULL;
	  }
	  if (nErrcode != NUAN_OK)
	  {
	    vplatform_printf("Error (vauto_ttsInitialize) : 0x%x\n", (unsigned int)nErrcode);
	    mainRc = 1;
	    return exitRelease();
	  }

	  /* Optionally print the list of available languages, voices, and
	  ** speechbases */
	  if (bExtraInfo)
	  {
	    vplatform_printf("Number of languages available: ");
	    nErrcode = ve_ttsGetLanguageList(hSpeech, NULL, &nItem);
	    if (nErrcode != NUAN_OK)
	    {
	      vplatform_printf("\nError (ve_ttsGetLanguageList) : 0x%x\n",
	        (unsigned int)nErrcode);
	      mainRc = 1;
	      return exitUnInit();
	    }
	    vplatform_printf("%u\n", nItem);

	    pLangList = (VE_LANGUAGE *)
	      vplatform_heap_Malloc(hHeap, sizeof(VE_LANGUAGE) * nItem);
	    if ((pLangList) == NULL)
	    {
	      vplatform_printf("Failed to allocate memory");
	      return exitUnInit();
	    }

	    /* vplatform_printf("Get the list of languages available\n"); */
	    nErrcode = ve_ttsGetLanguageList(hSpeech, pLangList, &nItem);
	    if (nErrcode != NUAN_OK)
	    {
	      vplatform_printf("Error (ve_ttsGetLanguageList) : 0x%x\n",
	        (unsigned int)nErrcode);
	      if (pLangList) vplatform_heap_Free(hHeap, pLangList);
	      mainRc = 1;
	      return exitUnInit();
	    }

	    for (i = 0; i < nItem; i++)
	    {
	      vplatform_printf("%d>> %s, %s, %d, %s\n", i,
	                        pLangList[i].szLanguage,
	                        pLangList[i].szLanguageTLW,
	                        pLangList[i].u16LangId,
	                        pLangList[i].szVersion);

	      nErrcode = ve_ttsGetClmInfo(hSpeech, pLangList[i].szLanguage,
	        0,&ClmInfo);
	      if (nErrcode != NUAN_OK)
	      {
	        vplatform_printf("\nError (ve_ttsGetClmInfo) : 0x%x\n", (unsigned int)nErrcode);
	      }
	      vplatform_printf("  %d>> CLM File version: %s \n", j,
	                        ClmInfo.szFileVersion);
	      vplatform_printf("  %d>> CLM Source version: %s \n", j,
	                        ClmInfo.szSrcVersion);
	      vplatform_printf("  %d>> CLM Destination version: %s \n", j,
	                        ClmInfo.szDstVersion);

	      nErrcode = ve_ttsGetNtsInfo(hSpeech, pLangList[i].szLanguage,
	        0,&NtsInfo);
	      if (nErrcode != NUAN_OK)
	      {
	        vplatform_printf("\nError (ve_ttsGetNtsInfo) : 0x%x\n", (unsigned int)nErrcode);
	      }
	      vplatform_printf("  %d>> NTS: %s\n", j,
	                        NtsInfo.szVersion);

	      vplatform_printf("  Number of voices available: ");
	      nErrcode = ve_ttsGetVoiceList(hSpeech, pLangList[i].szLanguage,
	        0, NULL, &nItem2);
	      if (nErrcode != NUAN_OK)
	      {
	        vplatform_printf("\nError (ve_ttsGetVoiceList) : 0x%x\n", (unsigned int)nErrcode);
	        if (pLangList) vplatform_heap_Free(hHeap, pLangList);
	        mainRc = 1;
	        return  exitUnInit();
	      }
	      vplatform_printf("%u\n", nItem2);

	      pVoiceList = (VE_VOICEINFO *)
	        vplatform_heap_Malloc(hHeap, sizeof(VE_VOICEINFO) * nItem2);
	      if ((pVoiceList) == NULL)
	      {
	        vplatform_printf("Failed to allocate memory");
	        if (pLangList) vplatform_heap_Free(hHeap, pLangList);
	        return exitUnInit();
	      }

	      /* vplatform_printf("  Get the list of voices available\n");*/
	      nErrcode = ve_ttsGetVoiceList(hSpeech, pLangList[i].szLanguage,
	        0, pVoiceList, &nItem2);
	      if (nErrcode != NUAN_OK)
	      {
	        vplatform_printf("Error (ve_ttsGetVoiceList) : 0x%x\n", (unsigned int)nErrcode);
	        mainRc = 1;
	        if (pVoiceList) vplatform_heap_Free(hHeap, pVoiceList);
	        if (pLangList) vplatform_heap_Free(hHeap, pLangList);
	        return exitUnInit();
	      }

	      for (j = 0; j < nItem2; j++)
	      {
	        vplatform_printf("  %d>> %s, %s, %s\n", j,
	                          pVoiceList[j].szVoiceName,
	                          pVoiceList[j].szVoiceAge,
	                          pVoiceList[j].szVoiceType);

	        vplatform_printf("    Number of speech DBs available: ");
	        nErrcode = ve_ttsGetSpeechDBList(hSpeech,
	          pLangList[i].szLanguage, 0, pVoiceList[j].szVoiceName, NULL,
	          &nItem3);
	        if (nErrcode != NUAN_OK)
	        {
	          vplatform_printf("\nError (ve_ttsGetSpeechDBList) : 0x%x\n",
	            (unsigned int)nErrcode);
	          if (pVoiceList) vplatform_heap_Free(hHeap, pVoiceList);
	          if (pLangList) vplatform_heap_Free(hHeap, pLangList);
	          mainRc = 1;
	          return exitUnInit();
	        }
	        vplatform_printf("%u\n", nItem3);

	        pSpeechDBList = (VE_SPEECHDBINFO *)
	          vplatform_heap_Malloc(hHeap, sizeof(VE_SPEECHDBINFO) * nItem3);
	        if ((pSpeechDBList) == NULL)
	        {
	          vplatform_printf("Failed to allocate memory");
	          if (pLangList) vplatform_heap_Free(hHeap, pLangList);
	          if (pVoiceList) vplatform_heap_Free(hHeap, pVoiceList);
	          return exitUnInit();
	        }

	        /* vplatform_printf("    Get the list of speech DBs available\n");*/
	        nErrcode = ve_ttsGetSpeechDBList(hSpeech,
	          pLangList[i].szLanguage, 0, pVoiceList[j].szVoiceName,
	          pSpeechDBList, &nItem3);
	        if (nErrcode != NUAN_OK)
	        {
	          vplatform_printf("Error (ve_ttsGetSpeechDBList) : 0x%x\n",
	            (unsigned int)nErrcode);
	          if (pSpeechDBList) vplatform_heap_Free(hHeap, pSpeechDBList);
	          if (pVoiceList) vplatform_heap_Free(hHeap, pVoiceList);
	          if (pLangList) vplatform_heap_Free(hHeap, pLangList);
	          mainRc = 1;
	          return exitUnInit();
	        }

	        for (k = 0; k < nItem3; k++)
	        {
	          vplatform_printf("    %d>> %s, %hu kHz, %s\n", k,
	                             pSpeechDBList[k].szVoiceModel,
	                             pSpeechDBList[k].u16Freq,
	                             pSpeechDBList[k].szVersion);
	        }

	        vplatform_heap_Free(hHeap, pSpeechDBList);
	        pSpeechDBList = NULL;
	      }

	      vplatform_heap_Free(hHeap, pVoiceList);
	      pVoiceList = NULL;
	    }



	    vplatform_heap_Free(hHeap, pLangList);
	  }

	  /* Create the TTS instance */
	  //vplatform_printf("Create TTS instance\n");
	  nErrcode = ve_ttsOpen(hSpeech, stInstall.hHeap, stInstall.hLog,
	    &hTtsInst, (VE_USERDATA)this);
	  if (nErrcode != NUAN_OK)
	  {
	    vplatform_printf("Error (ve_ttsOpen) : 0x%x\n", (unsigned int)nErrcode);
	    mainRc = 1;
	    return exitUnInit();
	  }
	  /* Set parameters, these are initialized from the command line arguments */
	  //vplatform_printf("Set TTS parameters\n");
	  nErrcode = ve_ttsSetParamList(hTtsInst , &TtsParam[0] , cTtsParam);
	  if (nErrcode != NUAN_OK)
	  {
	    vplatform_printf("Error (ve_ttsSetParamList) : 0x%x\n", (unsigned int)nErrcode);
	    mainRc = 1;
	    return exitClose();
	  }

	  /* Configure the system to load all modules at startup */
	  TtsParam[0].eID = VE_PARAM_INITMODE;
	  TtsParam[0].uValue.usValue = VE_INITMODE_LOAD_ONCE_OPEN_ALL;
	  nErrcode = ve_ttsSetParamList(hTtsInst , &TtsParam[0] , 1);
	  if (nErrcode != NUAN_OK)
	  {
	    vplatform_printf("Error (ve_ttsSetParamList: VE_PARAM_INITMODE) : 0x%x\n", (unsigned int)nErrcode);
	    mainRc = 1;
	    return exitClose();
	  }

	  /* Set the output device */
	  memset(&stOutDevInfo, 0, sizeof(stOutDevInfo));
	  stOutDevInfo.hOutDevInstance = (VE_OUTDEV_HINSTANCE) fPCM;
	  stOutDevInfo.pfOutNotify = SCbOutNotifyToFile;
	  nErrcode = ve_ttsSetOutDevice(hTtsInst, &stOutDevInfo);
	  if (nErrcode != NUAN_OK)
	  {
	    vplatform_printf("Error (ve_ttsSetOutDevice) : 0x%x\n", (unsigned int)nErrcode);
	    mainRc = 1;
	    return exitClose();
	  }

		ready = true;

	  return NUAN_OK;
}

void NuanceAPIWrapper::synthesize(std::string text) {
	if(text == "") {
		stopSynth = false;
		return;
	}
		ready = false;
	  /* Specify the text to synthesize */
	  stInText.eTextFormat  = VE_NORM_TEXT;
	  stInText.ulTextLength = text.size();
	  stInText.szInText     = const_cast<char*>(text.c_str());
	  stInText.eTextFormat  = eTextFormat;
	  /* Synthesis */
	  nErrcode = ve_ttsProcessText2Speech(hTtsInst, &stInText);
	  if (nErrcode != NUAN_OK)
	  {
	    switch(nErrcode) {
		case NUAN_E_TTS_USERSTOP:
			break;
		case NUAN_E_INVALIDARG:
			stopSynth = false;
			ready = true;
			break;
		case NUAN_E_TTS_NOINPUTTEXT:
			stopSynth = false;
			ready = true;
			break;
		default:
			vplatform_printf("Error (ve_ttsProcessText2Speech) : 0x%x\n", (unsigned int)nErrcode);
			stopSynth = false;
			ready = true;
			break;
	    }
	  }
}

void NuanceAPIWrapper::stop() {
	stopSynth = true;
	int err = ve_ttsStop(hTtsInst);
	switch(err) {
		case NUAN_OK:				/*printf("stop OK\n");*/
									/*pthread_mutex_lock(&mutex);
									if(stopSynth)
										pthread_cond_wait(&condition_stop, &mutex);
									pthread_mutex_unlock(&mutex);*/
									//while(stopSynth);
									break;
		case NUAN_E_INVALIDHANDLE:	printf("Invalid handler\n");
									break;
		case NUAN_E_WRONG_STATE:	/*printf("Instance is not synthesizing\n");*/
									break;
		default:
									printf("ve_ttsStop returned %d\n", err);
									break;
	}
}

void NuanceAPIWrapper::close() {
	exitClose();
}

int NuanceAPIWrapper::exitClose() {
	  /* Close the TTS instance */
	  //vplatform_printf("\nClose TTS instance\n");
	  nErrcode = ve_ttsClose(hTtsInst);
	  if (nErrcode != NUAN_OK)
	  {
	    vplatform_printf("Error (ve_ttsClose) : 0x%x\n", (unsigned int)nErrcode);
	  }

	  return exitUnInit();
}

int NuanceAPIWrapper::exitUnInit() {
	  /* Uninitialize */
	  //vplatform_printf("Uninitialize\n");
	  nErrcode = ve_ttsUnInitialize(hSpeech);
	  if (nErrcode != NUAN_OK)
	  {
	    vplatform_printf("Error (ve_ttsUnInitialize) : 0x%x\n", (unsigned int)nErrcode);
	  }

	  return exitRelease();
}

int NuanceAPIWrapper::exitRelease() {
	  /* Clean up */
	  if (pPcmData != NULL) vplatform_heap_Free(hHeap, pPcmData);
	  pPcmData = NULL;
	  if (pMrkData != NULL) vplatform_heap_Free(hHeap, pMrkData);
	  pMrkData = NULL;
	  if (fPCM != NULL) vplatform_file_Close(fPCM);

	  /* release platform interfaces */
	  vplatform_ReleaseInterfaces(&stInstall);

	  return exitMain();
}

int NuanceAPIWrapper::exitMain() {
	  return mainRc;
}

bool NuanceAPIWrapper::isReady() const{
	  return ready;
}
