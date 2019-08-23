#include "stdAfx.h"
#include "AudioWrap.h"
#include "AudioRendererSpark.h"

void AudioRendererSpark::init() {

	audioPacketSize = getGlobalConfiguration()->getInt(const_cast<char *>("AudioVideoConfig_Remote_AvatarStream_AudioPacketSize"));
	int audioSampleRate = getComponentConfiguration()->getInt(const_cast<char *>("AudioVideoConfig_Remote_AvatarStream_AudioSampleRate"));

	audioPacketDuration = (float)audioPacketSize/(float)audioSampleRate;
	lastAudioPacketIssueTime = 0;
	avsychStopWatch.restart();
}

void AudioRendererSpark::sendAudioFrame(void) {
	int storedAudioSizeInBytes = myAudioQueue->getStoredAudioSize();
	if (storedAudioSizeInBytes == 0)
	{
		myControlVoice->stopSpeaking();
	}
	else {
		myControlVoice->startVoice();
	}

	int audioPacketSizeInBytes = 2 * audioPacketSize;
	//*********************
	//el siguiente malloc donde se libera??
	char *audioPacket = (char *)malloc(audioPacketSizeInBytes);

	memset(audioPacket, 0, audioPacketSizeInBytes);
	myAudioQueue->dequeueAudioBuffer(
		audioPacket,
		(storedAudioSizeInBytes > audioPacketSizeInBytes) ? audioPacketSizeInBytes : storedAudioSizeInBytes
	);
	AudioWrap audioWrap((short *)audioPacket, audioPacketSizeInBytes);
	myAudioFlow->processData(&audioWrap);
	//*****************
	//Se libera dos veces la mima zona de memoria?
	free(audioPacket);
}


/* A packet unit is one video frame for two audio frames */

void AudioRendererSpark::sendPacketUnit(void) {
	sendAudioFrame();
	sendAudioFrame();
}

void AudioRendererSpark::dispatchPackets(void) {
	static int sentPacketUnits = 0;

	// Aiming at one frame per two audio packets.
	float packetUnitPeriod = audioPacketDuration * 2;

	int totalPreviousPacketUnits = floor(avsychStopWatch.elapsedTime() / packetUnitPeriod);
	int pendingPacketUnits = totalPreviousPacketUnits - sentPacketUnits;

	for (int i = 0; i < pendingPacketUnits; i++) {
		sendPacketUnit();
		sentPacketUnits++;
	}
}

void AudioRendererSpark::process(void) {
	static bool hasRenderContext = false;
	if (!hasRenderContext) {
		myControlVoice->startSpeaking();
		hasRenderContext = true;
	}
	dispatchPackets();
}

void AudioRendererSpark::quit() {
}
