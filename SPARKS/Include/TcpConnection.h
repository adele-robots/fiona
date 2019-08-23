/// @file TcpConnection.h
/// @brief Declaration of the TcpConnection interface */

#ifndef __TCPSERVER_H
#define __TCPSERVER_H


/// Interface for OS-independent TCP socket functionality.

class TcpConnection
{
public:
	virtual void init(void) = 0;
	virtual void connect(char *hostName, __int16 port) = 0;
	virtual void accept(__int16 port) = 0;
	virtual void setNonBlocking(void) = 0;
	virtual void send(char *, int len) = 0;
	virtual int recv(char *, int len, /* out */ bool *wouldBlock = NULL) = 0;
	virtual void close(void) = 0;
};


#endif
