/// @file VoiceStartSpark.cpp
/// @brief VoiceStartSpark class implementation.

#include "stdAfx.h"
#include <stdlib.h>

#include "VoiceStartSpark.h"

#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "VoiceStartSpark")) {
			return new VoiceStartSpark(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif

/// Initializes VoiceStartSpark component.
void VoiceStartSpark::init(void){
	movingLips=false;
	alfa=0.0;

	strSayConfigurationText = getComponentConfiguration()->getString(const_cast<char *>("Tts_Text"));
	sayConfigurationText = &strSayConfigurationText[0];
	strLipsFakeMovementViseme = getComponentConfiguration()->getString(const_cast<char *>("Lips_Fake_Movement_Viseme"));
	lipsFakeMovementViseme = &strLipsFakeMovementViseme[0];

	myFrameEventPublisher->addFrameEventSubscriber(this);
}

/// Unitializes the VoiceStartComponent component.
void VoiceStartSpark::quit(void){
}

void VoiceStartSpark::startSpeaking(void){
	LoggerInfo("[VoiceStartSpark::startSpeaking]Estoy en VoiceStart voy a hacer processData\n");

	movingLips=true;
	myFlow->processData(sayConfigurationText);
}

void VoiceStartSpark::startVoice(void){
	movingLips=true;
}

void VoiceStartSpark::stopSpeaking(void){
	movingLips=false;
}

//FrameEventSubscriber implementation
void VoiceStartSpark::notifyFrameEvent()
{
	if (movingLips){
		myFaceExpression->setFaceExpression(lipsFakeMovementViseme,alfa);

		if (alfa == 0.0){
			alfa=0.5;
		}
		else if (alfa == 0.5){
			alfa=1;
		}
		else if (alfa == 1){
			alfa=0;
		}
	}
	else{
		myFaceExpression->setFaceExpression(lipsFakeMovementViseme,0.0);
	}
}







