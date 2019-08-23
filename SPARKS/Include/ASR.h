/// @file ASR.h
/// @brief ASR class definition for speech recognition.

#ifndef __ASR_H
#define __ASR_H

//#include <LoqASR.h>
#include "ErrorHandling.h"
#include "IAudioConsumer.h"

/// \brief This class encapsulates the Loquendo speech recognition engine.
///
/// At initialization time the class builds an on-the-fly recognition object
/// that is stored to disk, starting with a plain text big grammar file.\n
/// Voice recognition is done by specifying a certain rule of that grammar,
/// that becomes a virtual root rule of the grammar, thus implimenting state
/// dependent grammars.

class ASR : public IAudioConsumer {
public:
	void init(void);
	void stopRecog(void);
	void quit(void);
	void buildRO(char *grammarFilePath, char *szName);
	void deleteRO(char *szName);
	unsigned int getSampleRate(void);
	char *getRecognitionResults(void);

	// ASR Engine callback
//	static lasrxResultType LASRX_STDCALL eventCallback(lasrxHandleType hInstance, lasrxPointerType pUser, lasrxIntType nEvent, lasrxIntType nReason, lasrxStringType szId, lasrxPointerType pEventData, lasrxIntType nEventDataSize);

	// IAudioConsumer
	void consumeAudioBuffer(__int16 *buff, int samplesPerBuffer);


public:
	//lasrxHandleType hInstance;
	void notifyEndOfPrompt(void);
	void setCurrentRecognitionGrammarRule(char *rule);
	void launchRecognition(char *rule);

private:
	//lasrxHandleType hSession;
};


/// Non-fatal exception indicating voice mistmatches against hypothesis

class SpeechRecognitionException : public Exception {
public:
	SpeechRecognitionException(char *s) : Exception(s) {}
};

#endif
