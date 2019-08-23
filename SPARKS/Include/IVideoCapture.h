#ifndef __I_VIDEO_CAPTURE_H
#define __I_VIDEO_CAPTURE_H

#include "IVideoConsumer.h"


class IVideoCapture {
	virtual void init(IVideoConsumer *);
	virtual void start(void);
	virtual void stop(void);
	virtual void quit(void);
};


#endif
