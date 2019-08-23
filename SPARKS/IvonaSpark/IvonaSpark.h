/// @file IvonaSpark.h
/// @brief Component IvonaSpark main class.

#ifndef IVONASPARK_H_
#define IVONASPARK_H_

#include <Component.h>
//#include "IVoice.h"
#include "IFlow.h"
#include "AudioQueue.h"
#include "IAudioQueue.h"
#include "IThreadProc.h"
#include "ivona_tts.h"
#include <queue>

extern "C" {
#include "libavcodec/avcodec.h"
}

#define NUM_CHANNELS		1      /* number of PCM channels */
#define RIFF_FORMAT_PCM		0x001  /* RIFF PCM sample format */
#define VERBOSEMSG(...) if(_global_verbose) { fprintf(stderr, __VA_ARGS__); }
#define STREQ(A, B) (strcmp(A, B) == 0)
#define MARK_SEC_OFFSET(w, i) \
		((float)(num_samples + (w)->marks[i].sample_offset) \
			/ (w)->sample_rate)

// Mensajes a intercambiar en la aplicación:
// - Tipo del mensaje (PID_proceso, 1)
// - Contenido
typedef struct{
	long int tipo;
	char contenido[1024];
} mensaje;

/// @brief This is the main class for component IvonaSpark.
///
///

class IvonaSpark: public Component,
	//public IVoice,
	public IFlow<char*>,
	public IAudioQueue,
	public IThreadProc {
public:
	IvonaSpark(
					char *instanceName,
					ComponentSystem *cs
			) : Component(instanceName, cs)
			{}
	virtual ~IvonaSpark() {}

public:
	//Mandatory
	void init(void);
	void quit(void);

	//IFlow<char*> implementation
	void processData(char* prompt);

	// IAudioQueue
	int getStoredAudioSize();
	void queueAudioBuffer(char *buffer, int size);
	void dequeueAudioBuffer(char *buffer, int size);
	void reset();

	// Implementation of IThreadproc
	void process();

	static void signalHandler(int _ignored);

private:
	AudioQueue audioQueue;
	int audioQueueSize;
	int _global_verbose;
	const char *_voice_name;
	const char *_voxdb_name;
	const char *_wave_file;
	float _speed;
	int _block;
	tts_instance *tts;
	tts_voice *v;
	tts_voice **voices;

	std::queue<std::string> colaMensajes;

	ReSampleContext* ctx;
	int valid_sample_rate;

	pthread_cond_t cond_full;
	pthread_mutex_t mutex;

private:
	void initializeRequiredInterfaces(){
		}
	int synthesize(tts_voice **voices, const char *text, float speed, int samples_per_block, const char *wavefile);
	void print_streamer_warnings(tts_streamer *s);
	void print_wave_marks(const tts_wave *w, size_t prefix_samples, const char *text);
	int voice_param_int(tts_voice *v, const char *pv);
	int voice_param_str(tts_voice *v, const char *pv);
	int riff_init_fd(FILE *fd, int sample_rate);
	int riff_append_samples_fd(FILE *fd, short *samples, int num_samples);
	tts_voice **append_voice(tts_voice **voices, tts_voice *voice);
	void unload_voices(tts_voice **voices);

};

#endif /* IVONASPARK_H_ */
