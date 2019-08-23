/// @file EmptyApplicationSpark.cpp
/// @brief EmptyApplicationSpark class implementation.

#include <map>
#include "Logger.h"
#include "ErrorHandling.h"
#include "Configuration.h"
#include "EmptyApplicationSpark.h"
#include <signal.h>

extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "EmptyApplicationSpark")) {
			return new EmptyApplicationSpark(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}

bool running = true;


void EmptyApplicationSpark::eventLoop(void) {
	while(running)
		usleep(200000);
}

void EmptyApplicationSpark::signalHandler(int sig){
	running = false;
}

void sasigaction(int sig, siginfo_t * info, void * w){
	running = false;
	LoggerError("SIGTERM send by %d", info->si_pid);
}

void EmptyApplicationSpark::run(void) {
	struct sigaction act;
	act.sa_flags = SA_SIGINFO;
	act.sa_sigaction = &sasigaction;
	sigaction(SIGTERM, &act, NULL);
	//signal((int) SIGTERM, signalHandler);
	signal((int) SIGINT, signalHandler);
	eventLoop();	// IEventQueue method
}

void EmptyApplicationSpark::init(void) {

}

void EmptyApplicationSpark::quit(void) {

}

void EmptyApplicationSpark::handleError(char *s) {
	LoggerError(s);
}
