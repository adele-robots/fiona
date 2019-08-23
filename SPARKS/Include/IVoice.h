#ifndef __I_VOICE_H
#define __I_VOICE_H


class IVoice {
public:
	virtual void sayThis(char *prompt) = 0;		/* Retorna sin esperar */
	virtual void waitEndOfSpeech(void) = 0;
	virtual void stopSpeech(void) = 0;
};


#endif
