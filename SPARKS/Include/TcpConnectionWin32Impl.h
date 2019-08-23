/// @file TcpConnectionWin32Impl.h
/// @brief Implementation of the TcpConnectionWin32Impl class implementing the TcpConnection interface 


#ifndef __TCP_CONNECTION_WIN32_IMPL
#define __TCP_CONNECTION_WIN32_IMPL

#include <winsock2.h>
#include "TcpConnection.h"


/// A win32 implementation of a TCP socket.

class TcpConnectionWin32Impl : public TcpConnection
{
private:
	SOCKET mainSocket;
	SOCKET connectedSocket;

public:
	bool initialized;
	TcpConnectionWin32Impl(void);
	void init(void);
	void connect(char *hostName, __int16 port);
	void accept(__int16 port);
	void setNonBlocking(void);
	void send(char *, int len);
	int recv(char *buf, int len, bool *wouldBlock);
	void close(void);
};

#endif
