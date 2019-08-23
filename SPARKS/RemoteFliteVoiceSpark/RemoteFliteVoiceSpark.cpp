/// @file RemoteFliteVoiceSpark.cpp
/// @brief  class implementation.

// Third party libraries are linked explicitly once in the project.
// #pragma comment(lib, "thirdPartyLib.lib")

#include "stdAfx.h"
#include "RemoteFliteVoiceSpark.h"

#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "RemoteFliteVoiceSpark")) {
			return new RemoteFliteVoiceSpark(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}

extern "C" cst_voice *register_cmu_us_slt();

#endif

//struct timespec ts1, ts2;

/// Initializes  component.
void RemoteFliteVoiceSpark::init(void){
	flite_init();
	//Set the queue size (in samples, 1 byte/sample)
	audioQueue.init(44100);
	//audioQueue.init(176400);
	//#ifdef GENERAL_INI
	//audioPacketSize = getGlobalConfiguration()->getInt("AudioVideoConfig.Remote.AvatarStream.AudioPacketSize");
	//#else
	audioPacketSize = getGlobalConfiguration()->getInt("AudioVideoConfig_Remote_AvatarStream_AudioPacketSize");
	//#endif

	//set the voice
	v = register_cmu_us_slt();
	//streaming
	asi = new_audio_streaming_info();
	//set the callback
	asi->asc = callback_audio_stream_chunk;
	//min_buffsize takes 1264 as value with 1185 input (¿?)
	asi->min_buffsize = 2000;
	//we need to pass this to handle attributes and methods within the callback
	asi->userdata = this;
	//set streaming feature, needed to use the callback
	feat_set(v->features,"streaming_info",audio_streaming_info_val(asi));
	//set the output synthesis wave sample rate, it seems it isn't working
	feat_set(v->features,"sample_rate",int_val(11025));
	flite_feat_set_int(v->features,"sample_rate",11025);

	hiloTerminado = true;
}

/// Unitializes the  component.
void RemoteFliteVoiceSpark::quit(void){
	//cst_free(asi);
	delete_audio_streaming_info(asi);
}

// OJO borrame
#include <stdio.h>
//#include <sstream>
//#include <string.h>

using namespace std;

//string filename;
//stringstream ss;
//int sufix = 0;

int RemoteFliteVoiceSpark::callback_audio_stream_chunk(const cst_wave *w, int start,
								int size, int last, void *user){
	/* Called with new samples from start for size samples */
	/* last is true if this is the last segment. */

	//Casting to access class attributes and methods through remoteLoquendoVoiceComponent
	RemoteFliteVoiceSpark *remoteFliteVoiceSpark;
	remoteFliteVoiceSpark = (RemoteFliteVoiceSpark *)user;

	static bool beenHere = false;
	//static FILE *f;
	static int new_start = 0;
	static int new_size;

	//		clock_gettime( CLOCK_REALTIME, &ts1 );
	cst_wave *w_copy =	copy_wave(w);
	//		clock_gettime( CLOCK_REALTIME, &ts2 );
	//		fprintf(stderr, "Demora copy (clock_gettime): %f secs\n", (float) ( 1.0*(1.0*ts2.tv_nsec - ts1.tv_nsec*1.0)*1e-9 + 1.0*ts2.tv_sec - 1.0*ts1.tv_sec ) );
	//		clock_gettime( CLOCK_REALTIME, &ts1 );
	cst_wave_resample(w_copy, sampleRate);
	//		clock_gettime( CLOCK_REALTIME, &ts2 );
	//		fprintf(stderr, "Demora resample (clock_gettime): %f secs\n", (float) ( 1.0*(1.0*ts2.tv_nsec - ts1.tv_nsec*1.0)*1e-9 + 1.0*ts2.tv_sec - 1.0*ts1.tv_sec ) );

	if (!beenHere) {
		beenHere =true;		

		//ss << sufix;
		//filename = "/home/sistemas/prueba"+ss.str()+".pcm";

		//f = fopen(filename.c_str(), "wb");
		
		//if (!f) ERR("Error opening /home/sistemas/prueba.pcm");
		//new_size = size * w->num_samples/w_copy->num_samples;
		//new_size = size * (w->num_samples-2048)/w_copy->num_samples;
		//new_size = size * w->num_samples/resampled_samples;
		//new_size = size * (down-2048)/up;

		new_size = size * (w_copy->num_samples-2048)/w->num_samples;
		new_start = new_start - new_size;
	}

	new_start = new_start + new_size;	
	//fwrite((char *)(w_copy->samples)+new_start*2,new_size*2 , 1, f);


	//if (last==1){
		//puts("last = 1");
		//fclose(f);
		//sufix++;
		//ss.str("");
	//}
	//puts("CALLBACK - Metiendo en el buffer!");	//
	// [PRUEBAS] Comprobar que no se excederá el tamaño al
	// introducir los nuevos datos
	//if(remoteFliteVoiceSpark->audioQueue.getStoredAudioSize()+new_size < remoteFliteVoiceSpark->audioQueue.audioBufferSize){
		remoteFliteVoiceSpark->audioQueue.queueAudioBuffer(
									(char *)(w_copy->samples)+new_start*2,
									new_size*2
									);
	//}
	//Free the copy wave
	delete_wave(w_copy);

	/* if you want to stop return CST_AUDIO_STREAM_STOP */
	if (last == 1)
	{
		beenHere = false;
		new_start = 0;
		return CST_AUDIO_STREAM_STOP;
	}
	return CST_AUDIO_STREAM_CONT;
}

void *RemoteFliteVoiceSpark::threadProcedure(void *args){
	RemoteFliteVoiceSpark *remoteFliteVoiceSpark;
	remoteFliteVoiceSpark = (RemoteFliteVoiceSpark *)args;
	remoteFliteVoiceSpark->hiloTerminado = false;
	//printf("[RemoteFliteVoiceSpark-INFO] Iniciando hilo\n");
	flite_text_to_wave(remoteFliteVoiceSpark->text,remoteFliteVoiceSpark->v);
	remoteFliteVoiceSpark->hiloTerminado = true;
	//puts("Salgo del hilo");
	//printf("[RemoteFliteVoiceSpark-INFO] Hilo terminado\n");
}

//IVoice implementation
void RemoteFliteVoiceSpark::processData(char *prompt){
	//puts("Creating thread");
	text = prompt;
	printf("ELVIRA has answered: %s\n", text);
	//********Se necesitan elevar excepciones dentro del hilo o mantener algun
	//********tipo de control sobre este !

	if(hiloTerminado){
		//printf("[RemoteFliteVoiceSpark-INFO] Creando hilo en el say this\n");
		int res = pthread_create(
				&thread_id,
				NULL,
				RemoteFliteVoiceSpark::threadProcedure,
				this
		);

		if (res != 0) {
			ERR("pthread_create");
		}
	}else{
		//printf("[RemoteFliteVoiceSpark-INFO] No se crea hilo en el say this\n");
	}

	//flite_text_to_wave(prompt,v);
	//puts("Finalizada la sintesis");
	//flite_text_to_speech(prompt,v,"none");

	//flite_text_to_speech(prompt,v,"flite.wav");
}

/*
void RemoteFliteVoiceSpark::waitEndOfSpeech(){

}

void RemoteFliteVoiceSpark::stopSpeech(){
}
*/

//IAudioQueue implementation
int RemoteFliteVoiceSpark::getStoredAudioSize(){
	return audioQueue.getStoredAudioSize();
}

void RemoteFliteVoiceSpark::queueAudioBuffer(char *buffer, int size){
	audioQueue.queueAudioBuffer(buffer, size);
}

void RemoteFliteVoiceSpark::dequeueAudioBuffer(char *buffer, int size){
	audioQueue.dequeueAudioBuffer(buffer,size);
}
