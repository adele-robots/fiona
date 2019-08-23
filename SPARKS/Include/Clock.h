/// @file Clock.h
/// @brief Clock class definition.

#ifndef __CLOCK_H
#define __CLOCK_H

#include <semaphore.h>

/// This class is a waitable timer with a semaphore. The semaphore is P-actuated in waitTick,
/// and is V-actuated in the callback of its associated, high precision, timer-queue timer 
/// (a timer of the win32 timer queue). 

class Clock
{
public:
	HANDLE timerQueueTimer;
	int millisecondPeriod;
	sem_t clockSemaphore;

	void init(int millisecondPeriod);	/* milisegundos */
	void start(void);
	static void CALLBACK timerCallback(PVOID lpParam, BOOLEAN TimerOrWaitFired);
	void waitTick(void);
	void stop(void);		
	void quit(void);
	void reset(void);
};


#endif
