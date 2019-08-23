/// @file IControlVoice.h
/// @brief IControlVoice interface definition


#ifndef __I_CONTROL_VOICE_H
#define __I_CONTROL_VOICE_H

class IControlVoice {
public:
	virtual void startSpeaking(void) = 0;
	virtual void stopSpeaking(void) = 0;
	virtual void startVoice(void) = 0;

};


#endif
