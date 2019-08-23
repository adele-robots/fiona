/// @file Thread.h
/// @brief Base class for several concurrent flow abstractions based on the Pthreads library.

#ifndef __THREAD_H
#define __THREAD_H


#include <pthread.h>
#include "IAsyncFatalErrorHandler.h"
#include "ErrorHandling.h"




/// Base class for exception-aware iterative stoppable threads


class Thread {
public:
	Thread(char *n, IAsyncFatalErrorHandler *afeh) : 
	  name(n), 
	  shutDown(false) ,
	  asyncFatalErrorHandler(afeh)
	{}
	void start(void);
	void stop(void);
	virtual void process(void) = 0;

public:
	bool shutDown;
	IAsyncFatalErrorHandler *asyncFatalErrorHandler;

private:
	static void *threadProcedure(void *);

private:
	pthread_t thread_id;
	char *name;
};


#endif
