#include "stdAfx.h"
#include <math.h>
#include "OutgoingImage.h"
#include "AudioWrap.h"
#include "RemoteRendererComponent.h"

#include <opencv2/opencv.hpp>

#ifdef _WIN32
#else
#include <GL/glx.h>
#include <stdlib.h>
//*********OJO quitar!
#include <stdio.h>
#endif

// Buffer to hold the background image + HORDE3D rendered frame
uchar* mixedImage;

void RemoteRendererComponent::init() {

	audioPacketSize = getGlobalConfiguration()->getInt(const_cast<char *>("AudioVideoConfig_Remote_AvatarStream_AudioPacketSize"));
	width = getGlobalConfiguration()->getInt(const_cast<char *>("AudioVideoConfig_Width"));
	height = getGlobalConfiguration()->getInt(const_cast<char *>("AudioVideoConfig_Height"));
	int audioSampleRate = getComponentConfiguration()->getInt(const_cast<char *>("AudioVideoConfig_Remote_AvatarStream_AudioSampleRate"));

	audioPacketDuration = (float)audioPacketSize/(float)audioSampleRate;

	renderTargetTexture = h3dCreateTexture(
		"RenderTargetTexture",
		width,
		height,
		H3DFormats::TEX_BGRA8,
		H3DResFlags::TexRenderable | H3DResFlags::NoTexMipmaps
	);

	if (renderTargetTexture == 0) ERR("Error creating render target texture'");

	h3dSetNodeParamI(
		myRenderizable->getCamaraNode(),
		H3DCamera::OutTexResI,
		renderTargetTexture
	);

	lastAudioPacketIssueTime = 0;
	avsychStopWatch.restart();
#ifdef _WIN32
	BOOL ok = wglMakeCurrent(NULL, NULL);
	if (!ok) ERR("wglMakeCurrent, releasing");
#else
	Display *dpy = myWindow->getWindowDisplay();
	bool ok2 = glXMakeCurrent(dpy,None,NULL);
	if (!ok2) ERR("glXMakeCurrent");
#endif

	/** Advertising **/
	try{
		hasAdvertising = getGlobalConfiguration()->getBool(const_cast<char *>("Has_Advertising"));
		if(hasAdvertising){
			string advertisingImage = getGlobalConfiguration()->getString(const_cast<char *>("Advertising_Image_Filepath"));
			backgroundImage  = cvLoadImage(advertisingImage.c_str(),-1);
			if (backgroundImage == 0 ) {
				LoggerError("An error occurred while loading an advertising image\n");
				exit(-1);
			}else{
				// Save total number of pixels. Needed to loop the image.
				pixelsPerImage = width*height;
				// Horizontal flip
				cvFlip(backgroundImage,backgroundImage,0);
				// Allocate memory for the mixed image with avatar and background
				mixedImage = new uchar[width*height*NUM_HORDE_IMAGE_CHANNELS];
			}
		}
	} catch(exception const & ex) {
		LoggerError("Caught a std::exception: %s",ex.what());
		hasAdvertising = false;
	} catch(::Exception &e){
		LoggerError("No advertisement will be displayed");
		hasAdvertising = false;
	} catch(...) {
		LoggerError("A non-std::exception was thrown! Srsly.");
		hasAdvertising = false;
	}

	fpsSynch.restart();
}


void RemoteRendererComponent::sendVideoFrame(void) {
	updater();

	myRenderizable->render();
	// Get a pointer to the HORDE 3D rendered frame
	void *p = h3dMapResStream(
		renderTargetTexture, 
		H3DTexRes::ImageElem, 
		0, 
		H3DTexRes::ImgPixelStream, 
		true, false
	);

	if (p == NULL) ERR("Error mapping render target texture stream");

	unsigned char *buffer = static_cast<unsigned char *>(p);

	if(hasAdvertising){
		for(int pixel = 0; pixel < pixelsPerImage; pixel++){
			cnB = pixel*NUM_HORDE_IMAGE_CHANNELS + 0;
			cnG = pixel*NUM_HORDE_IMAGE_CHANNELS + 1;
			cnR = pixel*NUM_HORDE_IMAGE_CHANNELS + 2;
			cnA = pixel*NUM_HORDE_IMAGE_CHANNELS + 3;
			// Use the alpha channel to do the image compositing
			uchar hordeAlpha = buffer[cnA]; // A

			if(hordeAlpha == 0){
				mixedImage[cnB] = buffer[cnB]; // B
				mixedImage[cnG] = buffer[cnG]; // G
				mixedImage[cnR] = buffer[cnR]; // R
				mixedImage[cnA] = hordeAlpha; // A
			}else{
				mixedImage[cnB] = backgroundImage->imageData[pixel*backgroundImage->nChannels + 0]; // B
				mixedImage[cnG] = backgroundImage->imageData[pixel*backgroundImage->nChannels + 1]; // G
				mixedImage[cnR] = backgroundImage->imageData[pixel*backgroundImage->nChannels + 2]; // R
				mixedImage[cnA] = hordeAlpha; // A
			}
		}

		OutgoingImage image(width, height, NUM_HORDE_IMAGE_CHANNELS, mixedImage);
		myFlow->processData(&image);

	}else{
		OutgoingImage image(width, height, NUM_HORDE_IMAGE_CHANNELS, buffer);
		myFlow->processData(&image);
	}

	h3dUnmapResStream(renderTargetTexture);
}

void RemoteRendererComponent::sendAudioFrame(void) {
	int storedAudioSizeInBytes = myAudioQueue->getStoredAudioSize();
	if (storedAudioSizeInBytes == 0)
	{
		myControlVoice->stopSpeaking();
	}
	else
		myControlVoice->startVoice();

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

void RemoteRendererComponent::sendPacketUnit(void) {
	sendVideoFrame();
	sendAudioFrame();
	sendAudioFrame();
}

void RemoteRendererComponent::dispatchPackets(void) {
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

void RemoteRendererComponent::process(void) {
	// In the first run of process(), mark this the OpenGL rendering thread.
	static bool hasRenderContext = false;
	if (!hasRenderContext) {
		myControlVoice->startSpeaking();
		myWindow->makeCurrentopenGlThread();

		hasRenderContext = true;
		// Horde3d docs: "The h3dInit function can be called several times on different 
		// rendering contexts in order to initialize them"
		//sleep(1);
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

void RemoteRendererComponent::quit() {
	delete [] mixedImage;
}

//IFrameEventPublisher implementation
void RemoteRendererComponent::addFrameEventSubscriber(FrameEventSubscriber *frameEventSubscriber)
{
	//add subscriber to a vector of subscribers
	frameEventSubscriberArray.push_back(frameEventSubscriber);
}

void RemoteRendererComponent::updater(void){

	for (unsigned int i = 0; i < frameEventSubscriberArray.size(); i++)
				frameEventSubscriberArray[i]->notifyFrameEvent();
}





