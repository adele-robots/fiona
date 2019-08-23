/// @file LocalAudioInput.h
/// @brief LocalAudioInput class declaration.

#ifndef __LOCAL_AUDIO_INPUT_H
#define __LOCAL_AUDIO_INPUT_H


//#include <LoqASR.h>
#include <windows.h>
#include "AudioInput.h"
#include "IAudioConsumer.h"



typedef int (__stdcall *LASRX_EX_AUDIO_NEW_HANDLE)(int nFormat, void **hAudio);
typedef int (__stdcall *LASRX_EX_AUDIO_DELETE_HANDLE)(void *hAudio);
typedef int (__stdcall *LASRX_EX_AUDIO_SET_DATA)(void *hAudio, char *szData);
typedef int (__stdcall *LASRX_EX_AUDIO_REGISTER)(void *hAudio, void *hInstance);
typedef int (__stdcall *LASRX_EX_AUDIO_UNREGISTER)(void *hAudio);

typedef HMODULE LASRX_EX_DLL_HND;


/// Record the function pointers of the audio source.

typedef struct {
    LASRX_EX_DLL_HND hAudioSourceDll;
    LASRX_EX_AUDIO_NEW_HANDLE pfNew;
    LASRX_EX_AUDIO_DELETE_HANDLE pfDelete;
    LASRX_EX_AUDIO_SET_DATA pfSetData;
    LASRX_EX_AUDIO_REGISTER pfRegister;
    LASRX_EX_AUDIO_UNREGISTER pfUnregister;
} LasrxExDllPointers;



class LocalAudioInput : public AudioInput
{
public:
	LocalAudioInput(IAudioConsumer *ac, IAsyncFatalErrorHandler *afeh) : 
		AudioInput(ac, afeh) 
	{}
	void init();
	void quit(void);

private:
	void loadAudioSource(
		char *szAudioSource, 
		void **phAudio
	);
};


#endif
