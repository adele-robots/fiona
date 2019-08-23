#ifndef __LOCAL_RENDERER_H
#define __LOCAL_RENDERER_H


#include "Component.h"
#include "IThreadProc.h"
#include "IFrameEventPublisher.h"

#include "IRenderizable.h"
#include "IWindow.h"

#include <vector>


class LocalRendererComponent : 
	public Component,
	public IThreadProc,
	public IFrameEventPublisher
{
public:
	IRenderizable *myRenderizable;
	IWindow *myWindow;

	void initializeRequiredInterfaces() {	
		requestRequiredInterface<IRenderizable>(&myRenderizable);
		requestRequiredInterface<IWindow>(&myWindow);
	}

public:
	LocalRendererComponent(
		char *instanceName, 
		ComponentSystem *cs
	) : Component(instanceName, cs)
	{}


	void init();
	void quit();

	//IThreadProc implementation
	void process();

	//IFrameEventPublisher implementation
	void addFrameEventSubscriber(FrameEventSubscriber *frameEventSubscriber);

private:
	std::vector< FrameEventSubscriber *> frameEventSubscriberArray;

};

#endif

