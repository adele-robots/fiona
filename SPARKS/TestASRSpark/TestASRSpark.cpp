/// @file TestASRSpark.cpp
/// @brief TestASRSpark class implementation.


#include "stdAfx.h"
#include "TestASRSpark.h"


#include <sphinxbase/err.h>
#include <sphinxbase/ad.h>
#include <sphinxbase/cont_ad.h>

#include "pocketsphinx.h"

#include <sys/types.h>
#include <sys/time.h>

#include <stdio.h>
#include <stdlib.h>

#include "ckd_alloc.h"




#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "TestASRSpark")) {
			return new TestASRSpark(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif


/* Sleep for specified msec */
	void
	TestASRSpark::sleep_msec(int32 ms)
	{
	#if (defined(WIN32) && !defined(GNUWINCE)) || defined(_WIN32_WCE)
	    Sleep(ms);
	#else
	    /* ------------------- Unix ------------------ */
	    struct timeval tmo;

	    tmo.tv_sec = 0;
	    tmo.tv_usec = ms * 1000;

	    select(0, NULL, NULL, NULL, &tmo);
	#endif
	}

/// Initializes TestASRSpark component.
void TestASRSpark::init(void){


}

/// Unitializes the TestASRSpark component.
void TestASRSpark::quit(void){
}

//**To change for your convenience**
//Example of provided interface implementation
//IUpdateable1 implementation
void TestASRSpark::run(void){
	FILE *fh;
	int16 *bufTo;
	mensaje mensajeRecibido;

	int pid = getpid();

	// Recuperar cola de mensajes ya creada
	int idCola = msgget(pid,0);



	fh = fopen("goforward.raw", "rb");


	fseek (fh , 0 , SEEK_END);
	long filesize = ftell(fh);

	cout << "****************Filesize: " << filesize << endl;

	rewind(fh);

	bufTo = (int16*) malloc (sizeof(int16)*filesize);
	long nsamps = fread(bufTo, sizeof(int16), filesize, fh);


	cout << "*************NUM. SAMPLES: " << nsamps << endl;

	myAudioConsumer->consumeAudioBuffer(bufTo,nsamps);

	// Esperar respuesta
	memset(mensajeRecibido.contenido,'\0',sizeof(mensajeRecibido.contenido));
	if(msgrcv(idCola,&mensajeRecibido,sizeof(mensajeRecibido.contenido),2,0)<0){
		cout << "Error al recibir mensaje de tipo 2\n" << endl;
	}else {
		cout << "Elvira answered:" << mensajeRecibido.contenido << endl;
	}

	free(bufTo);

}

// IAsyncFatalErrorHandler implementation
void TestASRSpark::handleError(char *msg)
{
		LoggerError(msg);
}


void TestASRSpark::sayThis(char *prompt){



}

void TestASRSpark::waitEndOfSpeech(void){
}

void TestASRSpark::stopSpeech(void){
}




