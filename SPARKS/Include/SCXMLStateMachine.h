/// @file SCXMLStateMachine.h
/// @brief IStateMachine factory and state machine operation declarations.


#ifndef __SCXML_STATE_MACHINE_H
#define __SCXML_STATE_MACHINE_H


#include <windows.h>
#include <ISScxml.h>
#include <ISSCXMLFactory.h>

using namespace SSCXML;

SSCXML::IFactory* getStateMachineFactory(void);

// Al parametro 'fileName' se le a�ade el sufijo ".scxml" 
// Se carga y devuelve la m�quina de estados ah� descrita
IStateMachine *getStateMachine(
	SSCXML::IFactory *factory, 
	SSCXML::ScxmlString fileName
);


// Los eventos se pueden desdencadenar program�ticamente y enviarse a la m�quina o
// se pueden disparar declarativamente desde el SCXML de la m�quina y procesarse por programa.
void sendEventToStateMachine(
	IStateMachine *sm,
	SSCXML::ScxmlString& anEvent
); 


// Registra el manejador de los eventos desde la m�quina hacia al programa
// eventHandlerName debe coincidir con el especificado en SCXML
void registerEventHandler(
	IStateMachine *sm,
	SSCXML::ScxmlString& eventHandlerName, 
	SSCXML::IEventHandler *eventHandler
);


// update hace que se llamen los manejadores de evento si viene al caso
void updateStateMachine(IStateMachine *sm);

// Transforma cadenas 'true' or 'false' a valor booleano
bool trueOrFalse(std::string s);


#endif
