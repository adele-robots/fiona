#ifndef __I_FRAME_EVENT_PUBLISHER_H
#define __I_FRAME_EVENT_PUBLISHER_H

#include "FrameEventSubscriber.h"

class IFrameEventPublisher 
{
public:
    virtual void addFrameEventSubscriber(FrameEventSubscriber *frameEventSubscriber) = 0;
};

#endif
