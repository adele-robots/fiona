#ifndef __I_EVENT_QUEUE_H
#define __I_EVENT_QUEUE_H

#include <map>
#include "Events.h"

typedef void (*EventCallback)(void *data1, void *data2);

class IEventQueue {
public:
	virtual void postEvent(psisban::Messages, void *data1, void * data2) = 0;
	virtual void registerListener(psisban::Messages, EventCallback) = 0;
	virtual void eventLoop(void) = 0;

	std::map<psisban::Messages, EventCallback> eventListeners;
};


#endif
