#ifndef __EVENTS_H
#define __EVENTS_H
#ifdef _WIN32
#include <windows.h>
#else
#endif


namespace psisban {

typedef enum Messages {
	ECA_INIT = 0x8000,//WM_APP,
	ECA_CTRL_C,
	ECA_LASRX_EVENT_END_RECOG,
	ECA_TTSEVT_AUDIOSTART,
	ECA_TTSEVT_ENDOFSPEECH,
	ECA_LASRX_EVENT_PROMPT_STOP,
	ECA_EXTERNAL_STATE_CHANGE,

	// OJO ad-hoc target teleoperaci�n
	ECA_SPEAK
};

class IEventHandler {
public:
#ifdef _WIN32
	virtual void handleEvent(Messages, WPARAM, LPARAM) = 0;
#else
#endif
};

}

#endif
