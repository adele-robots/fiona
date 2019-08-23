/// @file EventDrivenDialogManager.h
/// @brief EventDrivenDialogManager class definition.


#ifndef __LOQUENDO_DIALOG_MANAGER
#define __LOQUENDO_DIALOG_MANAGER


#include "EventDrivenDialogManager.h"
#include "LipSynch.h"
#include "Integration.h"
#include "InterruptionPrompts.h"
#include "ASR.h"
#include "Events.h"
#include "SCXMLStateMachine.h"


enum States {
	Initial,
	PlayingPrompt,
	Listening,
	IntegrationStoppingTts,
	IntegrationStoppingAsr,
};


/// \brief Stand alone dialog manager.
///
/// Stand alone implementation of DialogManger that does no integrate with other
/// applications.

class EventDrivenDialogManager : public psisban::IEventHandler
{
public:
	EventDrivenDialogManager(IAsyncFatalErrorHandler *afeh) :
		asyncFatalErrorHandler(afeh)
	{}
	void init(
		LipSynch *lipSynch,
		ASR *asr
	);
	void run(void);
	void loadStateMachine(char *scxmlStateMachineXmlFile);

	// OJO
	void say(char *s);

public:
	ASR *asr;
	States state;
	IStateMachine *stateMachine;	
	bool dieAfterFirstPrompt;
	bool hasBargeIn;
	bool hasInterruptionPrompts;



private:
	void readPromptFile(char *promptFilePath);
	void playPrompt(void);
	void quit(void);
	void signalDialogEnd(void);
	void onInit(void);
	void onEndOfRecognition(int retCode);
	void onAudioStart(void);
	void onEndOfTTSPrompt(void);
	void onExternalStateChange(int state);
	void onBargeIn(int nReason);
	void enterDialogState(void);
	void executeExternalStateChangeRequest(void);
	void handleEvent(psisban::Messages, WPARAM, LPARAM);
	char *getStateName(void);

private:
	LipSynch *lipSynch;
	char promptList[2048];
	char promptFileName[256];
	static const int MAX_PROMPT_LENGHT=4096;
	char prompt[MAX_PROMPT_LENGHT];
	char goTo[256];
	bool finalState;
	char rule[256];
	char stateNumberBuff[256];
	int stateNumber;
	InterruptionPrompts interruptionPrompts;
	Integration *integration;
	IAsyncFatalErrorHandler *asyncFatalErrorHandler;
};


#endif
