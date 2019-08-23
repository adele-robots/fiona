/// @file ScxmlEventHandler.h
/// @brief ScxmlEventHandler class definition.


#ifndef __SCXML_EVENT_HANDLER_H
#define __SCXML_EVENT_HANDLER_H


#include <ISScxml.h>
#include "LocalVideoInput.h"


/// Dispatcher of SCXML declarative events to its implementations.

class ScxmlEventHandler : public SSCXML::IEventHandler
{
public:
	virtual bool handleEvent(
		const SSCXML::ScxmlString& _senderID, 
		const SSCXML::ScxmlString& _event, 
		const SSCXML::VALUEMAP* _data, 
		const SSCXML::ScxmlString& _hints
	);
};


#endif
