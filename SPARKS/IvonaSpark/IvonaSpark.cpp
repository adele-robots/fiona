/// @file IvonaSpark.cpp
/// @brief IvonaSpark class implementation.

// Third party libraries are linked explicitly once in the project.
// #pragma comment(lib, "thirdPartyLib.lib")

#include "stdAfx.h"
#include "IvonaSpark.h"
#include <sstream>

#include <signal.h>

#include <Logger.h>
#include <sys/syscall.h>

//#define SAVE_WAVE

#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "IvonaSpark")) {
			return new IvonaSpark(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif

using namespace std;

bool newMessage = false;

void IvonaSpark::init(void) {
	signal((int) SIGTERM, signalHandler);
	signal((int) SIGINT, signalHandler);
	//signalHandler.setupSignalHandlers();

	avcodec_register_all();
	//Set the queue size (in samples, 1 byte/sample)
	try {
		audioQueueSize = getComponentConfiguration()->getInt(const_cast<char *>("AudioQueueSizeBytes"));
	} catch(::Exception &e){
		LoggerWarn("[FIONA-logger]Default size: 44100");
		audioQueueSize = 44100;
	}
	audioQueue.init(audioQueueSize);

	valid_sample_rate = getComponentConfiguration()->getInt(const_cast<char *>("AudioVideoConfig_Remote_AvatarStream_AudioSampleRate"));

	_voice_name = NULL;
	_voxdb_name = NULL;
	_wave_file = "salida.wav";
	_speed = -1.0f;
	_block = getGlobalConfiguration()->getInt(const_cast<char *>("AudioVideoConfig_Remote_AvatarStream_AudioPacketSize"))*2;
	char voz[256];
	getComponentConfiguration()->getString(const_cast<char *>("Voice"), voz, 256);
	int freq = getComponentConfiguration()->getInt(const_cast<char *>("Hz"));
	_global_verbose = 0;//set to 1 for debugging

	tts = tts_create();
	v = NULL;
	voices = NULL;

	if(NULL == tts) {
		fprintf(stderr, "Failed to create a tts_instance: %s\n", tts_errmsg());
		exit(1);
	}

	char cert_file[256];
	getComponentConfiguration()->getString(const_cast<char *>("CERTFILE"), cert_file, 256);
	if(NULL == tts_load_certificate_file(tts, cert_file)) {
		LoggerError("**Error: Failed to initialize TTS instance from certificate file %s: %s\n", cert_file, tts_errmsg());
			tts_destroy(tts);
			exit(1);
	}

	/* Process voice parameters */
	stringstream ssvoice, ssvox;
	string svoice, svox;
	char voice_p[256];
	getComponentConfiguration()->getString(const_cast<char *>("VOICEPATH"), voice_p, 256);
	char vox_p[256];
	getComponentConfiguration()->getString(const_cast<char *>("VOXPATH"), vox_p, 256);
	ssvoice << voice_p << "libvoice_" << voz << ".so.1.6";
	ssvoice >> svoice;
	_voice_name = svoice.c_str();
	ssvox << vox_p << "vox_" << voz << freq << "v";
	ssvox >> svox;
	_voxdb_name = svox.c_str();
	if(_voice_name && _voxdb_name) {
		/* Load the voice */
		if((v = tts_load_voice(tts, _voice_name, _voxdb_name)) == NULL) {
			LoggerError("**Error: Can't load voice %s: %s\n",
					_voice_name, tts_errmsg());
			tts_destroy(tts);
			exit(1);
		}
		VERBOSEMSG("VERBOSE: %s loaded\n", v->voice->name);

		voices = append_voice(voices, v);
		_voice_name = NULL;
		_voxdb_name = NULL;
	}

	if(!voices) {
		fprintf(stderr, "Please specify path to the voice.\n"
				"Run without arguments for more information.\n");
	}

	pthread_cond_init(&cond_full, NULL);
	pthread_mutex_init(&mutex, NULL);
}

void IvonaSpark::quit(void) {

	/* Unloading voice data */
	unload_voices(voices);

	/* Destroying text-to-speech instance */
	tts_destroy(tts);
}

void IvonaSpark::processData(char *prompt) {
	//LoggerInfo("[IvonaSpark::processData]Thread number %ld", pthread_self());
	//LoggerInfo("[IvonaSpark::processData]Thread id (gettid return) %ld\n", syscall(SYS_gettid));
	colaMensajes.push(string(prompt));
	newMessage = true;
	pthread_cond_signal(&cond_full);
}


int IvonaSpark::getStoredAudioSize() {
	//printf("IvonaSpark::getStoredAudioSize:%d\n",audioQueue.storedAudioSize);
	return audioQueue.storedAudioSize;
}

void IvonaSpark::queueAudioBuffer(char *buffer, int size) {
	//printf("%d\tIvonaSpark::queueAudioBuffer:%d\n", i, size);
	audioQueue.queueAudioBuffer(buffer, size);
}

void IvonaSpark::dequeueAudioBuffer(char *buffer, int size) {
	//printf("%d\tIvonaSpark::dequeueAudioBuffer:%d\n", i, size);
	audioQueue.dequeueAudioBuffer(buffer,size);
}

void IvonaSpark::reset() {
	audioQueue.reset();
}

//IThreadProc implementation
void IvonaSpark::process() {
	//SignalHandler signalHandler;

	pthread_mutex_lock(&mutex);

	// Esperar la llegada de mensajes
	if(colaMensajes.empty())
		pthread_cond_wait(&cond_full, &mutex);

	pthread_mutex_unlock(&mutex);

	string text = colaMensajes.front();
	synthesize(voices, text.c_str(), _speed, _block, _wave_file);
	colaMensajes.pop();

}

void IvonaSpark::signalHandler(int sig){
	LoggerInfo("Se ha capturado la señal SIGTERM en IvonaSpark");

	// Asignar a esa señal el manejador por defecto
	signal(sig,SIG_DFL);

	// Volver a lanzar la señal de terminación
	raise(sig);

}

/**
 * Synthesize text with given voice(s), at given speed (x realtime), each time
 * receiving given number of samples, writing output waveform to given file.
 */
int IvonaSpark::synthesize(tts_voice **voices, const char *text, float speed,
		int samples_per_block, const char *wavefile)
{
	tts_wave *w;
#ifdef SAVE_WAVE
	FILE *riff_fd = NULL;
#endif
	size_t i;
	bool primeraMuestra = true;
	newMessage = false;

	tts_streamer *s = tts_streamer_start(voices[0], text, speed);
	if(s == NULL) {
		fprintf(stderr, "**Error: Can't create streamer: %s\n",
				tts_errmsg());
		return -1;
	}

	for(i = 1; voices[i]; i++) {
		if(!tts_streamer_add_voice(s, voices[i])) {
			fprintf(stderr, "**Error: Cannot add voice %s\n",
					voices[i]->voice->lector);
			return -1;
		}
	}

	VERBOSEMSG("VERBOSE: streamer created\n");

	print_streamer_warnings(s);

#ifdef SAVE_WAVE
	if(wavefile) {
		if((riff_fd = fopen(wavefile, "w+b"))) {
			riff_init_fd(riff_fd, voices[0]->sample_rate);
			VERBOSEMSG("VERBOSE: file %s opened\n", wavefile);
		} else {
			fprintf(stderr, "**Error: Can't create file %s: %s\n",
					wavefile, strerror(errno));
			exit(2);
		}
	}
#endif

	{
		size_t num_samples = 0;

		ctx = audio_resample_init(1, 1, valid_sample_rate, s->voice_ptr->sample_rate);
/******************** Main synthesis loop ********************************************************/
		while((w = tts_streamer_synth(s, samples_per_block)) && newMessage == false) {
			// Si es la primera vez que pasamos por aqui vaciamos la cola
                        if (primeraMuestra) {
                        	reset();
                        	primeraMuestra = false;
                        }
			//int buffer_size = s->voice_ptr->sample_rate > valid_sample_rate ? w->num_samples*s->voice_ptr->sample_rate/valid_sample_rate : w->num_samples;
			short * buffer = new short[w->num_samples*2];//buffer_size];
			int new_size = audio_resample(ctx, buffer, w->samples, w->num_samples);
			queueAudioBuffer((char *)buffer, new_size*2);
			VERBOSEMSG("VERBOSE: got %d samples\n", w->num_samples);
			print_streamer_warnings(s);

#ifdef SAVE_WAVE
			if(riff_fd) {
				if(riff_append_samples_fd(riff_fd, w->samples,
							w->num_samples) == -1)
				{
					fprintf(stderr, "**Error: Can't write to file %s: %s\n",
							wavefile, strerror(errno));
					exit(2);
				}
			}
#endif

			if(_global_verbose) {
				print_wave_marks(w, num_samples, text);
			}

			num_samples += w->num_samples;
			tts_wave_delete(w);
		}
		audio_resample_close(ctx);

		if(tts_errno() != TTS_STATUS_OK)
			fprintf(stderr, "**Error: synthesis error: %s\n",
					tts_errmsg());

		print_streamer_warnings(s);
		
		/* This chunk of synthesis is finished*/
		if (newMessage) {
			reset();
		}
	}

#ifdef SAVE_WAVE
	if(riff_fd)
		fclose(riff_fd);
#endif

	/* Stopping streamer */
	tts_streamer_stop(s);

	VERBOSEMSG("VERBOSE: streamer stopped\n");
	return 0;
}

/**
 * Display stacked input warnings.
 * These are issued for recoverable errors in input SSML document.
 */
void IvonaSpark::print_streamer_warnings(tts_streamer *s)
{
	const char *msg;
	int byte_offset, line;
	enum tts_input_warning_code code;
	while(TTS_INPUT_WARNING_NONE !=
			(code = tts_get_input_warning(s, &byte_offset, &line, &msg))) {
		fprintf(stderr, "Warning at line %d (byte %d): %s\n",
				line, byte_offset, msg);
	}
}

/**
 * Print marks contained in the tts_wave structure.
 * Assume that there have already been prefix_samples samples of speech before
 * this block.
 */
void IvonaSpark::print_wave_marks(const tts_wave *w, size_t num_samples,
		const char *text)
{
/*
 * Convert sample number in current block
 * to offset in seconds in the whole speech.
 */
	size_t i;
	for(i = 0; i < w->num_marks; ++i) {
		const struct tts_mark_struct *m = &w->marks[i];
		switch(m->type) {
			case TTS_MARK_VISEME:
				printf("%10.3f  viseme %s\n",
						MARK_SEC_OFFSET(w, i),
						m->name);
				break;
			case TTS_MARK_PHONEME:
				printf("%10.3f  phoneme %s\n",
						MARK_SEC_OFFSET(w, i),
						m->name);
				break;
			case TTS_MARK_WORD:
				printf("%10.3f  word  %10d..%-5d  \"%.*s\"\n",
						MARK_SEC_OFFSET(w, i),
						m->start_text_offset,
						m->end_text_offset,
						m->end_text_offset - m->start_text_offset,
						text + m->start_text_offset);
				break;
			case TTS_MARK_SSML:
				printf("%10.3f  ssml-mark %14d..%-5d  \"%s\"\n",
						MARK_SEC_OFFSET(w, i),
						m->start_text_offset, m->end_text_offset, m->name);
				break;
			case TTS_MARK_SENTENCE:
				printf("%10.3f  sentence  %10d..%-5d \"%.*s\"\n",
						MARK_SEC_OFFSET(w, i),
						m->start_text_offset,
						m->end_text_offset,
						m->end_text_offset - m->start_text_offset,
						text + m->start_text_offset);
				break;
		}
	}
}

int IvonaSpark::voice_param_int(tts_voice *v, const char *pv)
{
	int ret = 0;
	char *cmd = NULL;
	const char *equals = strchr(pv, '=');
	if(equals != NULL) {
		int value = atoi(equals + 1);
		cmd = static_cast<char *>(malloc(5 + (equals - pv)));
		strcpy(cmd, "set:");
		strncat(cmd, pv, equals - pv);

		if(tts_voice_param(v, cmd, &value)) {
			printf("Param %.*s set to %d\n", (int) (equals - pv),
					pv, value);
		} else {
			fprintf(stderr, "**Error setting voice param '%.*s': %s\n",
					(int) (equals - pv), pv, tts_errmsg());
			ret = -1;
		}
	} else {
		int value;
		cmd = static_cast<char *>(malloc(5 + strlen(pv)));
		strcpy(cmd, "get:");
		strcat(cmd, pv);

		if(tts_voice_param(v, cmd, &value)) {
			printf("%s = %d\n", pv, value);
		} else {
			fprintf(stderr, "**Error getting voice param '%s': %s\n",
					pv, tts_errmsg());
			ret = -1;
		}
	}
	free(cmd);
	return ret;
}

/**
 * Set or display a string parameter.
 * These include: input_type, text_encoding, base_uri.
 * The parameter name may be followed with ":min", ":max" or ":def".
 */
int IvonaSpark::voice_param_str(tts_voice *v, const char *pv)
{
	int ret = 0;
	char *cmd = NULL;
	const char *equals = strchr(pv, '=');
	if(equals != NULL) {
		const char *value = equals + 1;
		cmd = static_cast<char *>(malloc(5 + (equals - pv)));
		strcpy(cmd, "set:");
		strncat(cmd, pv, equals - pv);

		if(tts_voice_param(v, cmd, &value)) {
			printf("Param %.*s set to \"%s\"\n",
					(int) (equals - pv), pv, value);
		} else {
			fprintf(stderr, "**Error setting voice param '%.*s': %s\n",
					(int) (equals - pv), pv, tts_errmsg());
			ret = -1;
		}
	} else {
		const char *value;
		cmd = static_cast<char *>(malloc(5 + strlen(pv)));
		strcpy(cmd, "get:");
		strcat(cmd, pv);

		if(tts_voice_param(v, cmd, &value)) {
			printf("%s = \"%s\"\n", pv, value ? value : "(null)");
		} else {
			fprintf(stderr, "**Error getting voice param '%s': %s\n",
					pv, tts_errmsg());
			ret = -1;
		}
	}
	free(cmd);
	return ret;
}

/*********************************************************************/
/**                                                                 **/
/**  Simple wave I/O                                                **/
/**                                                                 **/
/*********************************************************************/

/**
 * Write WAVE file header.
 */
int IvonaSpark::riff_init_fd(FILE *fd, int sample_rate)
{
	const char *info;
	short d_short;
	int d_int;
	int num_bytes;

	info = "RIFF";
	if(fwrite(info, 4, 1, fd) != 1)
		return -1;

	num_bytes = 8 + 16 + 12;
	if(fwrite(&num_bytes, 4, 1,fd) != 1)
		return -1;

	info = "WAVE";
	if(fwrite(info, 1, 4, fd) != 4)
		return -1;

	info = "fmt ";
	if(fwrite(info, 1, 4, fd) != 4)
		return -1;

	num_bytes = 16;
	if(fwrite(&num_bytes, 4, 1, fd) != 1)
		return -1;

	d_short = RIFF_FORMAT_PCM;
	if(fwrite(&d_short, 2, 1, fd) != 1)
		return -1;

	d_short = NUM_CHANNELS;
	if(fwrite(&d_short, 2, 1, fd) != 1)
		return -1;

	d_int = sample_rate;
	if(fwrite(&d_int, 4, 1, fd) != 1)
		return -1;

	d_int = (sample_rate * NUM_CHANNELS * sizeof(short));
	if(fwrite(&d_int, 4, 1, fd) != 1)
		return -1;

	d_short = (NUM_CHANNELS * (short)sizeof(short));
	if(fwrite(&d_short, 2, 1, fd) != 1)
		return -1;

	d_short = 2 * 8;
	if(fwrite(&d_short, 2, 1, fd) != 1)
		return -1;

	info = "data";
	if(fwrite(info, 1, 4, fd) != 4)
		return -1;

	d_int = 0;
	if(fwrite(&d_int, 4, 1, fd) != 1)
		return -1;

	return 0;
}

/**
 * Write samples to a wave file.
 */
int IvonaSpark::riff_append_samples_fd(FILE *fd, short *samples, int num_samples)
{
	char info[4];
	int file_bytes, data_bytes, n;
	long data_offset = 0;

	fseek(fd, 0L, SEEK_SET);

	/* Check */

	if(fread(info, 1, 4, fd) != 4 || strncmp(info, "RIFF", 4) != 0)
		return -1;

	if(fread(&file_bytes, 4, 1, fd) != 1)
		return -1;

	if(fread(info, 1, 4, fd) != 4 || strncmp(info, "WAVE", 4) != 0)
		return -1;

	if(fread(info, 1, 4, fd) != 4 || strncmp(info, "fmt ", 4) != 0)
		return -1;

	if(fread(&n, 4, 1, fd) == 1)
		data_offset = ftell(fd) + n;

	fseek(fd, data_offset, SEEK_SET);

	if(fread(info, 1, 4, fd) != 4 || strncmp(info, "data", 4) != 0)
		return -1;

	if(fread(&data_bytes, 4, 1, fd) != 1)
		return -1;

	/* Update */

	n = sizeof(short) * NUM_CHANNELS * num_samples;

	fseek(fd, 8 /* ChunkID + ChunkSize */ + file_bytes, SEEK_SET);
	if(fwrite(samples, sizeof(short), NUM_CHANNELS * num_samples, fd) !=
			(size_t) (NUM_CHANNELS * num_samples)) {
		return -1;
	}

	file_bytes += n;
	data_bytes += n;

	fseek(fd, 4, SEEK_SET);
	if(fwrite(&file_bytes, 4, 1, fd) != 1)
		return -1;

	fseek(fd, 4 /* Subchunk2ID */ + data_offset, SEEK_SET);
	if(fwrite(&data_bytes, 4, 1, fd) != 1)
		return -1;

	return 0;
}

tts_voice ** IvonaSpark::append_voice(tts_voice **voices, tts_voice *voice)
{
	size_t num_voices = 0;

	if(voices) {
		while(voices[num_voices])
			num_voices++;
	}

	voices = static_cast<tts_voice **>(realloc(voices, sizeof(tts_voice *) * (num_voices + 2)));
	voices[num_voices] = voice;
	voices[num_voices + 1] = NULL;

	return voices;
}

void IvonaSpark::unload_voices(tts_voice **voices)
{
	if(voices) {
		size_t i;

		for(i = 0; voices[i]; i++) {
			VERBOSEMSG("VERBOSE: %s unloaded\n",
					voices[i]->voice->name);
			tts_unload_voice(voices[i]);
		}


		free(voices);
	}
}

