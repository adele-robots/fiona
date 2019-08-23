/**
 *
 * @file IAsyncFatalErrorHandler.h
 *
 * @brief Threads must have this handler to give up with a gobal fatal error.
 *
 * Asynchronous errors are produced in non-main threads,
 * be it created by Thread::start() or by external dependencies
 * doing asychronous callbacks such as in video aquisition.
 *
 * Being fatal errors, error handlers, called by those threads,
 * should cause the ending of the main loop, by an ad-hoc mechanism.
 *
 */

#ifndef __I_ASYNC_FATAL_ERROR_HANDLER_H
#define __I_ASYNC_FATAL_ERROR_HANDLER_H

class IAsyncFatalErrorHandler {
public:
	virtual void handleError(char *msg) = 0;
};


#endif
