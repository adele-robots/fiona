/// @file StopWatch.h
/// @brief Declaration of the StopWatch class supporting high precision duration measures.

#ifndef __STOP_WATCH_H
#define __STOP_WATCH_H

#ifdef _WIN32
#else
#include <sys/time.h>
#endif
/// Cronometer based in the high-precission performance counter.

class StopWatch
{
#ifdef _WIN32
	__int64 start_count;
	__int64 freq;
	//struct timespec clock_resolution;
	//struct timespec ts;
#else
	timeval t1;
#endif

public:
	StopWatch();
	void restart(void);
	float elapsedTime(void);
};


#endif
