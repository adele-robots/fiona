#ifndef _I_CONCURRENT_H
#define _I_CONCURRENT_H


class IConcurrent {
public:
	virtual void start() = 0;
	virtual void stop() = 0;
};


#endif