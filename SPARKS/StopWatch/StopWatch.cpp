/// @file StopWatch.cpp
/// @brief Implementation of the StopWatch class supporting high precision duration measures.

#include "stdafx.h"
#include "StopWatch.h"


/// Time count starts at 0 at constructor's invocation.

StopWatch::StopWatch() {
#ifdef _WIN32
	QueryPerformanceFrequency((LARGE_INTEGER*)&freq);
	QueryPerformanceCounter((LARGE_INTEGER*)&start_count);
#else
	/*if( clock_getres(CLOCK_REALTIME,&clock_resolution) != 0 )
		ERR("ERROR clock_getres");

	if ( clock_gettime(CLOCK_REALTIME, &ts) != 0)
		ERR("ERROR clock_gettime");
	*/
	gettimeofday(&t1,NULL);
#endif
}


/// Reset time count to zero.

void StopWatch::restart(void) {
#ifdef _WIN32
	QueryPerformanceCounter((LARGE_INTEGER*)&start_count);
#else
	/*if ( clock_gettime(CLOCK_REALTIME, &ts) != 0)
			ERR("ERROR clock_gettime");
	*/
	gettimeofday(&t1,NULL);
#endif
}


/// Query the elapsed time if float resolution seconds.

float StopWatch::elapsedTime(void) {

#ifdef _WIN32
	__int64 end_count;
	QueryPerformanceCounter((LARGE_INTEGER*)&end_count);
	float time = (float)(end_count - start_count) / (float)freq;
#else
	timeval t2;
	gettimeofday(&t2,NULL);
	float time = (float)(t2.tv_sec - t1.tv_sec);		//sg
	time += (float)(t2.tv_usec - t1.tv_usec)/1000000.0;	//us to sg
#endif

	return time;
}

