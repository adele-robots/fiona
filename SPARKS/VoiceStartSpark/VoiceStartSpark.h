/// @file VoiceStartSpark.h
/// @brief Component VoiceStartSpark main class.


#ifndef __VoiceStartSpark_H
#define __VoiceStartSpark_H

#include "Component.h"
#include "FrameEventSubscriber.h"

#include "IControlVoice.h"
#include "IFaceExpression.h"
#include "IFlow.h"
#include "IFrameEventPublisher.h"


/// @brief This is the main class for component VoiceStartSpark.
///
/// It controls when the voice and the movements of lips start and finish

class VoiceStartSpark :
	public Component,

	public IControlVoice,
	public FrameEventSubscriber
{
public:
	VoiceStartSpark(
		char *instanceName, 
		ComponentSystem *componentSystem
	) : Component(instanceName, componentSystem)
	{}

private:
	IFlow<char *> *myFlow;
	IFaceExpression *myFaceExpression;
	IFrameEventPublisher *myFrameEventPublisher;

	void initializeRequiredInterfaces() {
		requestRequiredInterface<IFlow<char *> >(&myFlow);
		requestRequiredInterface<IFaceExpression>(&myFaceExpression);
		requestRequiredInterface<IFrameEventPublisher>(&myFrameEventPublisher);
	}

public:
	void init(void);
	void quit(void);

	//IControlVoice
	void startSpeaking();
	void stopSpeaking();
	void startVoice();
	//FrameEventSubscriber implementation
	void notifyFrameEvent();

private:
	bool movingLips;
	float alfa;
	std::string strSayConfigurationText;
	std::string strLipsFakeMovementViseme;
	char * sayConfigurationText;
	char * lipsFakeMovementViseme;
};

#endif
