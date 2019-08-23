/// @file Integration.h
/// @brief Integration class declaration.

#ifndef __INTEGRATION_H
#define __INTEGRATION_H

#include <windows.h>
#include "Thread.h"
#include "TcpConnection.h"


/// Integration thread to connect this process with external apps via a network
/// connection and a custom app-level protocol. The thread generates events for 
/// the app to process.

//TODO: MUJA implementation.

class Integration : public Thread
{
public:
	Integration(char *name, IAsyncFatalErrorHandler *afeh) : Thread(name, afeh) {}
	void init(void);
	void sendStateToExternalApp(int stateNumber);
	void sendAckToExternalApp(void);
	void quit(void);

private:
	virtual void process(void);
	void state2protocol(int stateNumber, BYTE buff[]);
	void protocol2state(BYTE buff[], int *stateNumber);


private:
	TcpConnection *stateReceptorFromExternalAppConnection;
	TcpConnection *stateEmitterToExternalAppConnection;
};


#endif
