/*
 * PicoTTSSpark.cpp
 *
 *  Created on: 14/01/2013
 *      Author: guille
 */

/// @file PicoTTSSpark.cpp
/// @brief PicoTTSSpark class implementation.

#include "stdAfx.h"
#include "PicoTTSSpark.h"

#ifdef _WIN32
#else
extern "C" Component *createComponent(char *componentInstanceName,
		char *componentType, ComponentSystem *componentSystem) {
	if (!strcmp(componentType, "PicoTTSSpark")) {
		return new PicoTTSSpark(componentInstanceName, componentSystem);
	} else {
		return NULL;
	}
}
#endif

bool newMessage = false;

/* adaptation layer defines */
#define PICO_MEM_SIZE       2500000
#define DummyLen 100000000

/* string constants */
#define MAX_OUTBUF_SIZE     128

std::string PICO_LINGWARE_PATH;
const char * PICO_VOICE_NAME = "PicoVoice";

/* supported voices
 Pico does not seperately specify the voice and locale.   */
const char * picoInternalTaLingware[] = { "en-US_ta.bin", "en-GB_ta.bin",
		"de-DE_ta.bin", "es-ES_ta.bin", "fr-FR_ta.bin", "it-IT_ta.bin" };
const char * picoInternalSgLingware[] = { "en-US_lh0_sg.bin",
		"en-GB_kh0_sg.bin", "de-DE_gl0_sg.bin", "es-ES_zl0_sg.bin",
		"fr-FR_nk0_sg.bin", "it-IT_cm0_sg.bin" };

const std::string voicesArray[] = { "en-US", "en-GB", "de-DE", "es-ES", "fr-FR",
		"it-IT" };

/* adapation layer global variables */
void * picoMemArea = NULL;
pico_System picoSystem = NULL;
pico_Resource picoTaResource = NULL;
pico_Resource picoSgResource = NULL;
pico_Resource picoUtppResource = NULL;
pico_Engine picoEngine = NULL;
pico_Char * picoTaFileName = NULL;
pico_Char * picoSgFileName = NULL;
pico_Char * picoUtppFileName = NULL;
pico_Char * picoTaResourceName = NULL;
pico_Char * picoSgResourceName = NULL;
pico_Char * picoUtppResourceName = NULL;
pico_Retstring outMessage;

int ret;
int voicePosition;

/* Returns the size of an array */
template<typename T, std::size_t N>
inline std::size_t sizeof_array(T (&)[N]) {
	return N;
}

/* Returns the position of the given voice within a voices array */
unsigned int getVoicePosition(std::string voice) {
	for (unsigned int i = 0; i < sizeof_array(voicesArray); i++) {
		if (voicesArray[i].compare(voice) == 0)
			return i;
	}
	return -1;
}

/* Pico TTS auxiliar functions */
void disposeEngine() {
	if (picoEngine) {
		pico_disposeEngine(picoSystem, &picoEngine);
		pico_releaseVoiceDefinition(picoSystem, (pico_Char *) PICO_VOICE_NAME);
		picoEngine = NULL;
	}
}
void unloadUtppResource() {
	if (picoUtppResource) {
		pico_unloadResource(picoSystem, &picoUtppResource);
		picoUtppResource = NULL;
	}
}
void unloadSgResource() {
	if (picoSgResource) {
		pico_unloadResource(picoSystem, &picoSgResource);
		picoSgResource = NULL;
	}
}
void unloadTaResource() {
	if (picoTaResource) {
		pico_unloadResource(picoSystem, &picoTaResource);
		picoTaResource = NULL;
	}
}
void terminate() {
	if (picoSystem) {
		pico_terminate(&picoSystem);
		picoSystem = NULL;
	}
	exit(ret);
}

/// Initializes PicoTTSSpark component.
void PicoTTSSpark::init(void) {
	//ffmpeg initialization
	avcodec_register_all();
	//Set the queue size (in samples, 1 byte/sample)
	try {
		audioQueueSize = getComponentConfiguration()->getInt(const_cast<char *>("AudioQueueSizeBytes"));
	} catch(::Exception &e){
		LoggerWarn("[FIONA-logger]Default size: 44100");
		audioQueueSize = 44100;
	}
	audioQueue.init(audioQueueSize);


	valid_sample_rate =
			getComponentConfiguration()->getInt(
					const_cast<char *>("AudioVideoConfig_Remote_AvatarStream_AudioSampleRate"));
	PICO_LINGWARE_PATH = getComponentConfiguration()->getString(
			const_cast<char*>("Pico_Lingware_Path"));

	std::string voice = getComponentConfiguration()->getString(
			const_cast<char*>("Voice"));
	voicePosition = getVoicePosition(voice);

	ctx = audio_resample_init(1, 1, valid_sample_rate, SAMPLE_FREQ_16KHZ);

	colaMensajes = new std::queue<std::string>();

	initializePico();

	pthread_cond_init(&cond_full, NULL);
	pthread_mutex_init(&mutex, NULL);
}

/// Unitializes the PicoTTSSpark component.
void PicoTTSSpark::quit(void) {
	audio_resample_close(ctx);

	delete colaMensajes;
}

//IThreadProc implementation
void PicoTTSSpark::process() {
	// Waiting for messages
	// Synthesize received message
	pthread_mutex_lock(&mutex);
	if(colaMensajes->empty())
		pthread_cond_wait(&cond_full, &mutex);
	//flite_text_to_wave(receivedMessage.contenido,v);
	//LoggerInfo("[FIONA-logger]Antes de synthesize");
	string text = colaMensajes->front();
	synthesize(text.c_str());
	colaMensajes->pop();
	//LoggerInfo("[FIONA-logger]Acabó synthesize");
	pthread_mutex_unlock(&mutex);
}

void PicoTTSSpark::processData(char *prompt) {
	colaMensajes->push(string(prompt));
	//LoggerInfo("[FIONA-logger]new message arrived!");
	newMessage = true;
	pthread_cond_signal(&cond_full);
}

int PicoTTSSpark::getStoredAudioSize() {
	return audioQueue.storedAudioSize;
}

void PicoTTSSpark::queueAudioBuffer(char *buffer, int size) {
	audioQueue.queueAudioBuffer(buffer, size);
}

void PicoTTSSpark::dequeueAudioBuffer(char *buffer, int size) {
	audioQueue.dequeueAudioBuffer(buffer, size);
}

void PicoTTSSpark::reset() {
	audioQueue.reset();
}

void PicoTTSSpark::synthesize(const char *text) {
	newMessage = false;
	int getstatus;
	pico_Char * inp = NULL;
	pico_Char * local_text = NULL;
	short outbuf[MAX_OUTBUF_SIZE / 2];
	pico_Int16 bytes_sent, bytes_recv, text_remaining, out_data_type;

	local_text = (pico_Char *) text;
	text_remaining = strlen((const char *) local_text) + 1;
	inp = (pico_Char *) local_text;
	bool primeraMuestra = true;

	/* synthesis loop   */
	while (text_remaining) {
		/* Feed the text into the engine.   */
		if ((ret = pico_putTextUtf8(picoEngine, inp, text_remaining,
				&bytes_sent))) {
			pico_getSystemStatusMessage(picoSystem, ret, outMessage);
			fprintf(stderr, "Cannot put Text (%i): %s\n", ret, outMessage);
			disposeEngine();
		}
		text_remaining -= bytes_sent;
		inp += bytes_sent;
		do {
			/* Retrieve the samples and add them to the buffer. */
			getstatus = pico_getData(picoEngine, (void *) outbuf,
					MAX_OUTBUF_SIZE, &bytes_recv, &out_data_type);

			if ((getstatus != PICO_STEP_BUSY)
					&& (getstatus != PICO_STEP_IDLE)) {
				pico_getSystemStatusMessage(picoSystem, getstatus, outMessage);
				fprintf(stderr, "Cannot get Data (%i): %s\n", getstatus,
						outMessage);
				disposeEngine();
			}
			if (bytes_recv) {
				// Si es la primera vez que pasamos por aqui vaciamos la cola
				if (primeraMuestra) {
					reset();
					primeraMuestra = false;
					//LoggerInfo("[FIONA-logger]reset!");
				}
				// FIN COMPROBACIÓN
				//short * buffer = new short[(bytes_recv*2)-4];
				//short * buffer = new short[bytes_recv];
				//short * buffer = new short[44];
				short * buffer = new short[(valid_sample_rate*(bytes_recv/2))/SAMPLE_FREQ_16KHZ];
				int new_size = audio_resample(ctx, buffer, outbuf,
						bytes_recv / 2);
				queueAudioBuffer((char *) buffer, new_size * 2);
				delete [] buffer;

			}
		} while (PICO_STEP_BUSY == getstatus && newMessage == false);
		/* This chunk of synthesis is finished*/
		if (newMessage) {
			//LoggerInfo("[FIONA-logger]reset y dispose!");
			pico_resetEngine(picoEngine, PICO_RESET_SOFT);
			reset();
			//text_remaining = 0;
		}
	}
}

void PicoTTSSpark::initializePico() {
	picoMemArea = malloc(PICO_MEM_SIZE);
	/* Initialize Pico system   */
	if ((ret = pico_initialize(picoMemArea, PICO_MEM_SIZE, &picoSystem))) {
		pico_getSystemStatusMessage(picoSystem, ret, outMessage);
		fprintf(stderr, "Cannot initialize pico (%i): %s\n", ret, outMessage);
		::terminate();
	}
	/* Load the text analysis Lingware resource file.   */
	picoTaFileName = (pico_Char *) malloc(
			PICO_MAX_DATAPATH_NAME_SIZE + PICO_MAX_FILE_NAME_SIZE);
	strcpy((char *) picoTaFileName, PICO_LINGWARE_PATH.c_str());
	strcat((char *) picoTaFileName,
			(const char *) picoInternalTaLingware[voicePosition]);
	if ((ret = pico_loadResource(picoSystem, picoTaFileName, &picoTaResource))) {
		pico_getSystemStatusMessage(picoSystem, ret, outMessage);
		fprintf(stderr, "Cannot load text analysis resource file (%i): %s\n",
				ret, outMessage);
		unloadTaResource();
	}
	/* Load the signal generation Lingware resource file.   */
	picoSgFileName = (pico_Char *) malloc(
			PICO_MAX_DATAPATH_NAME_SIZE + PICO_MAX_FILE_NAME_SIZE);
	strcpy((char *) picoSgFileName, PICO_LINGWARE_PATH.c_str());
	strcat((char *) picoSgFileName,
			(const char *) picoInternalSgLingware[voicePosition]);
	if ((ret = pico_loadResource(picoSystem, picoSgFileName, &picoSgResource))) {
		pico_getSystemStatusMessage(picoSystem, ret, outMessage);
		fprintf(stderr,
				"Cannot load signal generation Lingware resource file (%i): %s\n",
				ret, outMessage);
		unloadSgResource();
	}
	/* Get the text analysis resource name.     */
	picoTaResourceName = (pico_Char *) malloc(PICO_MAX_RESOURCE_NAME_SIZE);
	if ((ret = pico_getResourceName(picoSystem, picoTaResource,
			(char *) picoTaResourceName))) {
		pico_getSystemStatusMessage(picoSystem, ret, outMessage);
		fprintf(stderr, "Cannot get the text analysis resource name (%i): %s\n",
				ret, outMessage);
		unloadUtppResource();
	}
	/* Get the signal generation resource name. */
	picoSgResourceName = (pico_Char *) malloc(PICO_MAX_RESOURCE_NAME_SIZE);
	if ((ret = pico_getResourceName(picoSystem, picoSgResource,
			(char *) picoSgResourceName))) {
		pico_getSystemStatusMessage(picoSystem, ret, outMessage);
		fprintf(stderr,
				"Cannot get the signal generation resource name (%i): %s\n",
				ret, outMessage);
		unloadUtppResource();
	}
	/* Create a voice definition.   */
	if ((ret = pico_createVoiceDefinition(picoSystem,
			(const pico_Char *) PICO_VOICE_NAME))) {
		pico_getSystemStatusMessage(picoSystem, ret, outMessage);
		fprintf(stderr, "Cannot create voice definition (%i): %s\n", ret,
				outMessage);
		unloadUtppResource();
	}
	/* Add the text analysis resource to the voice. */
	if ((ret = pico_addResourceToVoiceDefinition(picoSystem,
			(const pico_Char *) PICO_VOICE_NAME, picoTaResourceName))) {
		pico_getSystemStatusMessage(picoSystem, ret, outMessage);
		fprintf(stderr,
				"Cannot add the text analysis resource to the voice (%i): %s\n",
				ret, outMessage);
		unloadUtppResource();
	}
	/* Add the signal generation resource to the voice. */
	if ((ret = pico_addResourceToVoiceDefinition(picoSystem,
			(const pico_Char *) PICO_VOICE_NAME, picoSgResourceName))) {
		pico_getSystemStatusMessage(picoSystem, ret, outMessage);
		fprintf(stderr,
				"Cannot add the signal generation resource to the voice (%i): %s\n",
				ret, outMessage);
		unloadUtppResource();
	}
	/* Create a new Pico engine. */
	if ((ret = pico_newEngine(picoSystem, (const pico_Char *) PICO_VOICE_NAME,
			&picoEngine))) {
		pico_getSystemStatusMessage(picoSystem, ret, outMessage);
		fprintf(stderr, "Cannot create a new pico engine (%i): %s\n", ret,
				outMessage);
		disposeEngine();
	}
}

