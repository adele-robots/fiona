#include "stdAfx.h"
#include "LocalRendererComponent.h"

#ifdef _WIN32
#else
#include <GL/glx.h>
#include <stdlib.h>
#endif



#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "LocalRendererComponent")) {
			return new LocalRendererComponent(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif


void LocalRendererComponent::init() {
	myWindow->show();
#ifdef _WIN32
	BOOL ok = wglMakeCurrent(NULL, NULL);
	if (!ok) ERR("wglMakeCurrent, releasing");
#else
	Display *dpy = myWindow->getWindowDisplay();
	bool ok2 = glXMakeCurrent(dpy,None,NULL);
	if (!ok2) ERR("glXMakeCurrent");
#endif
}


void LocalRendererComponent::process(void) {

	// In the first run of process(), mark this the OpenGL rendering thread.
	static bool hasRenderContext = false;
	if (!hasRenderContext) {
		myWindow->makeCurrentopenGlThread();
		hasRenderContext = true;
	}

	//myUpdateable->update();
	//update
	for (unsigned int i = 0; i < frameEventSubscriberArray.size(); i++)
		frameEventSubscriberArray[i]->notifyFrameEvent();


	myRenderizable->render();

	myWindow->openGlSwapBuffers();
}



void LocalRendererComponent::quit()
{

}
//IFrameEventPublisher implementation
void LocalRendererComponent::addFrameEventSubscriber(FrameEventSubscriber *frameEventSubscriber)
{
	//add subscriber to a vector of subscribers
	frameEventSubscriberArray.push_back(frameEventSubscriber);
}


