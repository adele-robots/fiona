/// @file ASRSpark.cpp
/// @brief ASRSpark class implementation.


#include "stdAfx.h"
#include "ASRSpark.h"




#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "ASRSpark")) {
			return new ASRSpark(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif



/// Initializes ASRSpark component.
void ASRSpark::init(void){
	char hmmPath[256];
	char lmPath[256];
	char dictPath[256];
	//char mllrPath[256];

	// Parámetros necesarios para el modelo del lenguaje a reconocer
	getComponentConfiguration()->getString("HMMPath", hmmPath, 256);
	//getComponentConfiguration()->getString("MLLRPath", mllrPath, 256);
	getComponentConfiguration()->getString("LMPath", lmPath, 256);
	getComponentConfiguration()->getString("DictionaryPath", dictPath, 256);


	//config = cmd_ln_init(NULL, ps_args(), TRUE,"-hmm", hmmPath,"-lm", lmPath,"-dict", dictPath, NULL);
	config = cmd_ln_init(NULL, ps_args(), TRUE,"-hmm", hmmPath, "-lm", lmPath, "-dict", dictPath, NULL);

	if(config == NULL){
		LoggerError("[ASRSpark]: Se ha producido un error al intentar cargar la configuración del ASRSpark.");
		exit(-1);
	}
	ps = ps_init(config);

	if(ps == NULL){
		LoggerError("[ASRSpark]: Se ha producido un error al intentar inicializar el decodificador.");
		exit(-1);
	}
}

/// Unitializes the ASRSpark component.
void ASRSpark::quit(void){
	// Liberar decodificador
	ps_free(ps);
}


// Provided interface implementation
// IAudioConsumer implementation
#ifdef _WIN32
void ASRSpark::consumeAudioBuffer(__int16 *audioBuffer, int bufferSizeInBytes)
#else
void ASRSpark::consumeAudioBuffer(int16_t *audioBuffer, int bufferSizeInBytes)
#endif
{

	int32 rem;
	const char *hyp;
	char const *uttid;
	int rv;

	// bufferSizeInBytes => number of samples

	LoggerInfo("ASR-Buffer size: %d", bufferSizeInBytes);
	if(bufferSizeInBytes > 0){
		rv = ps_start_utt(ps, NULL);
		//LoggerInfo("ASR-Valor rv %d:",rv);
		if(rv >= 0){
			rem = ps_process_raw(ps,audioBuffer,bufferSizeInBytes,FALSE,TRUE);
			// LoggerInfo("ASR-REM: %d", rem);
			ps_end_utt(ps);
			hyp = ps_get_hyp(ps, NULL, &uttid);
			if(hyp != NULL){
				LoggerInfo("ASR-Recognized: %s", hyp);
				myVoice->sayThis((char *)hyp);
			}
			//LoggerInfo("ASR-REM: %d", rem);

		}// TODO error en caso contrario
	}

}




