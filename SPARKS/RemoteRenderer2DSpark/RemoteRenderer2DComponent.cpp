#include "stdAfx.h"
#include <math.h>
#include "OutgoingImage.h"
#include "AudioWrap.h"
#include "RemoteRenderer2DComponent.h"
#include <sys/syscall.h>

#include <GL/glx.h>
#include <stdlib.h>

#include <stdio.h>

void RemoteRenderer2DComponent::init() {
	// Obtenemos los valores del fichero .ini correspondiente
	audioPacketSize = getGlobalConfiguration()->getInt(const_cast<char *>("AudioVideoConfig_Remote_AvatarStream_AudioPacketSize"));
	width = getGlobalConfiguration()->getInt(const_cast<char *>("AudioVideoConfig_Width"));
	height = getGlobalConfiguration()->getInt(const_cast<char *>("AudioVideoConfig_Height"));
	int audioSampleRate = getComponentConfiguration()->getInt(const_cast<char *>("AudioVideoConfig_Remote_AvatarStream_AudioSampleRate"));
	bytesPerPixel = 3;//getComponentConfiguration()->getInt(const_cast<char*>("BytesPerPixel"));

	// Calculamos la duracion del paquete de audio
	audioPacketDuration = (float)audioPacketSize/(float)audioSampleRate;

	lastAudioPacketIssueTime = 0;

	avsychStopWatch.restart();

	fpsSynch.restart();

}

void RemoteRenderer2DComponent::sendVideoFrame(void) {

	updater();
	
	unsigned char *buffer = static_cast<unsigned char *>(myRenderizable->render());

	OutgoingImage image(width, height, bytesPerPixel, buffer);

	myFlow->processData(&image);

	myRenderizable->unMapResourceStream();
}

void RemoteRenderer2DComponent::sendAudioFrame(void) {

	int storedAudioSizeInBytes = myAudioQueue->getStoredAudioSize();

	if (storedAudioSizeInBytes == 0){

		myControlVoice->stopSpeaking();
	}
	else{

		myControlVoice->startVoice();
	}

	int audioPacketSizeInBytes = 2 * audioPacketSize;

	//el siguiente malloc donde se libera??
	char *audioPacket = (char *)malloc(audioPacketSizeInBytes);

	memset(audioPacket, 0, audioPacketSizeInBytes);

	// En este punto se dispone a sacar las cosas del buffer
	myAudioQueue->dequeueAudioBuffer(
		audioPacket,
		(storedAudioSizeInBytes > audioPacketSizeInBytes) ? audioPacketSizeInBytes : storedAudioSizeInBytes
	);

	AudioWrap audioWrap((short *)audioPacket, audioPacketSizeInBytes);
	myAudioFlow->processData(&audioWrap);

	// Se libera dos veces la mima zona de memoria?
	free(audioPacket);
}


/* A packet unit is one video frame for two audio frames */

void RemoteRenderer2DComponent::sendPacketUnit(void) {
	sendVideoFrame();
	sendAudioFrame();
	sendAudioFrame();
}

void RemoteRenderer2DComponent::dispatchPackets(void) {
	static int sentPacketUnits = 0;

	// Apuntando a un frame por dos paquetes de audio
	float packetUnitPeriod = audioPacketDuration * 2;

	// La funcion floor calcula el valor integral mas grande que no sea mayor del valor
	// pasado como parametro. Redondea hacia abajo.
	int totalPreviousPacketUnits = floor(avsychStopWatch.elapsedTime() / packetUnitPeriod);

	int pendingPacketUnits = totalPreviousPacketUnits - sentPacketUnits;

	for (int i = 0; i < pendingPacketUnits; i++) {

		sendPacketUnit();

		sentPacketUnits++;
	}
}

void RemoteRenderer2DComponent::updater(void) {

	for (unsigned int i = 0; i < frameEventSubscriberArray.size(); i++){

		frameEventSubscriberArray[i]->notifyFrameEvent();

	}
}

void RemoteRenderer2DComponent::quit() {
}

//IThreadProc implementation
void RemoteRenderer2DComponent::process(void) {

	// En la primera ejecucion de process() se marca el hilo de OpenGL como renderizado
	static bool hasRenderContext = false;
	if (!hasRenderContext) {
		myControlVoice->startSpeaking();

		hasRenderContext = true;
		fpsSynch.restart();
	}

	//float timeToSleepInMicroseconds = (1000000 / fps) - (fpsSynch.elapsedTime() * 1000000);
	float timeToSleepInMicroseconds = (audioPacketDuration * 2 * 1000000) - (fpsSynch.elapsedTime() * 1000000) - 600;
	if(timeToSleepInMicroseconds > 0) {
		usleep(timeToSleepInMicroseconds);
	}
	fpsSynch.restart();

	dispatchPackets();
}

//IFrameEventPublisher implementation
void RemoteRenderer2DComponent::addFrameEventSubscriber(FrameEventSubscriber *frameEventSubscriber) {

	// Se añade el subscriber al vector de subscribers
	frameEventSubscriberArray.push_back(frameEventSubscriber);

}
