#include "NuanceTTSSpark.h"

#include <Logger.h>

#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "NuanceTTSSpark")) {
			return new NuanceTTSSpark(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif

using namespace std;

NuanceTTSSpark * self;
bool stopSent = false;

void callback(char* data, size_t size)
{
	if(stopSent) {
		self->m.lock();
		self->reset();
		stopSent = false;
		self->m.unlock();
		return;
	}
	short * buffer = new short[size*2];
	int new_size = audio_resample(self->ctx, buffer, (short*)data, size/2);
	self->queueAudioBuffer((char*)buffer, new_size*2);
	delete [] buffer;
}


void NuanceTTSSpark::init(void) {
	self = this;
	try {
		audioQueueSize = getComponentConfiguration()->getInt(const_cast<char *>("AudioQueueSizeBytes"));
	} catch(::Exception &e){
		LoggerWarn("[FIONA-logger]Default size: 44100");
		audioQueueSize = 44100;
	}
	audioQueue.init(audioQueueSize);

	valid_sample_rate = getComponentConfiguration()->getInt(const_cast<char *>("AudioVideoConfig_Remote_AvatarStream_AudioSampleRate"));

	ctx = audio_resample_init(1, 1, valid_sample_rate, 22050);

	ttsClient.connectClient(callback);

	voice = getComponentConfiguration()->getString(const_cast<char *>("Voice"));

	voiceModel = getComponentConfiguration()->getString(const_cast<char *>("VoiceModel"));

	ttsClient.initServer(voice, voiceModel);

}

void NuanceTTSSpark::quit(void) {
	ttsClient.disconnectClient();
}

string trim(string texto) {
	while(!texto.empty() && std::isblank(texto[0]))
		texto = texto.erase(0, 1);
	while(!texto.empty() && std::isblank(texto[texto.size()-1]))
		texto = texto.erase(texto.size()-1, 1);
	return texto;
}

void NuanceTTSSpark::processData(char *prompt) {
	if(trim(string(prompt)).empty()) return;
	ttsClient.stop();
	stopSent = true;
	m.lock();
	audioQueue.reset();
	ttsClient.synthezise(prompt);
	stopSent = false;
	m.unlock();
}


int NuanceTTSSpark::getStoredAudioSize() {
	return audioQueue.storedAudioSize;
}

void NuanceTTSSpark::queueAudioBuffer(char *buffer, int size) {
	audioQueue.queueAudioBuffer(buffer, size);
}

void NuanceTTSSpark::dequeueAudioBuffer(char *buffer, int size) {
	audioQueue.dequeueAudioBuffer(buffer,size);
}

void NuanceTTSSpark::reset() {
	audioQueue.reset();
}
