/*
 * RemotePhotoRendererSpark.cpp
 *
 *  Created on: 12/11/2013
 *      Author: guille
 */

/// @file RemotePhotoRendererSpark.cpp
/// @brief RemotePhotoRendererSpark class implementation.


#include "RemotePhotoRendererSpark.h"


#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "RemotePhotoRendererSpark")) {
			return new RemotePhotoRendererSpark(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif

bool running = true;

/// Initializes RemotePhotoRendererSpark component.
void RemotePhotoRendererSpark::init(void){
	// Get rendered image/frame info
	width = getGlobalConfiguration()->getInt(const_cast<char *>("AudioVideoConfig_Width"));
	height = getGlobalConfiguration()->getInt(const_cast<char *>("AudioVideoConfig_Height"));

	// Get audio params
	audioPacketSize = getGlobalConfiguration()->getInt(const_cast<char *>("AudioVideoConfig_Remote_AvatarStream_AudioPacketSize"));
	int audioSampleRate = getComponentConfiguration()->getInt(const_cast<char *>("AudioVideoConfig_Remote_AvatarStream_AudioSampleRate"));
	audioPacketDuration = (float)audioPacketSize/(float)audioSampleRate;

	avsychStopWatch.restart();
}

/// Unitializes the RemotePhotoRendererSpark component.
void RemotePhotoRendererSpark::quit(void){
	running = false;
}

// IApplication implementation
void RemotePhotoRendererSpark::run(void) {
	signal((int) SIGTERM, signalHandler);
	signal((int) SIGINT, signalHandler);

	while (running){
		dispatchPackets();
	}
}

void RemotePhotoRendererSpark::sendVideoFrame(void) {
	// Ask subscribers to update its info
	updater();

	// Get rendered frame
	unsigned char *buffer = static_cast<unsigned char *>(myRenderizable->render());	
	OutgoingImage image(width, height, 3, buffer);
	
	// Send it (mainly to build a video stream and publish it)
	myFlow->processData(&image);
}

void RemotePhotoRendererSpark::sendAudioFrame(void) {
	//LoggerInfo("RemotePhotoRendererSpark::sendAudioFrame | 1");
	int storedAudioSizeInBytes = myAudioQueue->getStoredAudioSize();
	//LoggerInfo("RemotePhotoRendererSpark::sendAudioFrame | 2");
	int audioPacketSizeInBytes = 2 * audioPacketSize;
	char *audioPacket = (char *)malloc(audioPacketSizeInBytes);
	//LoggerInfo("RemotePhotoRendererSpark::sendAudioFrame | 3");
	memset(audioPacket, 0, audioPacketSizeInBytes);
	myAudioQueue->dequeueAudioBuffer(
		audioPacket,
		(storedAudioSizeInBytes > audioPacketSizeInBytes) ? audioPacketSizeInBytes : storedAudioSizeInBytes
	);
	//LoggerInfo("RemotePhotoRendererSpark::sendAudioFrame | 4");
	AudioWrap audioWrap((short *)audioPacket, audioPacketSizeInBytes);
	myAudioFlow->processData(&audioWrap);
	//LoggerInfo("RemotePhotoRendererSpark::sendAudioFrame | 5");
	free(audioPacket);
}


/* A packet unit is one video frame for two audio frames */
void RemotePhotoRendererSpark::sendPacketUnit(void) {
	sendVideoFrame();
	sendAudioFrame();
	sendAudioFrame();
}

void RemotePhotoRendererSpark::dispatchPackets(void) {
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

//IFrameEventPublisher implementation
void RemotePhotoRendererSpark::addFrameEventSubscriber(FrameEventSubscriber *frameEventSubscriber)
{
	#ifdef SPARK_DEBUG
		LoggerInfo("[FIONA-logger]RemotePhotoRendererSpark::addFrameEventSubscriber | Suscrito un nuevo Spark");
	#endif
	//add subscriber to a vector of subscribers
	frameEventSubscriberArray.push_back(frameEventSubscriber);
}

void RemotePhotoRendererSpark::updater(void){
	for (unsigned int i = 0; i < frameEventSubscriberArray.size(); i++)
				frameEventSubscriberArray[i]->notifyFrameEvent();
}

void RemotePhotoRendererSpark::signalHandler(int sig){
	running = false;
}

