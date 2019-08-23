/// @file TestScene3DComponent.cpp
/// @brief TestScene3DComponent class implementation.

#include "stdAfx.h"
#include <stdlib.h>
#include <Horde3D.h>
#include <Horde3DUtils.h>
#include "GL/glfw.h"
#include <math.h>
#include "TestScene3DComponent.h"

#pragma comment (lib, "Horde3D.lib")
#pragma comment (lib, "Horde3DUtils.lib")
#pragma comment (lib, "opengl32.lib")
#pragma comment (lib, "glfwdll.lib")

#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "TestScene3DComponent")) {
			return new TestScene3DComponent(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif


bool running = true;

void fin(void) {
	//puts("adios");
	h3dutDumpMessages();
}

int GLFWCALL windowCloseListener()
{
	//puts("adios listener");
	running = false;
	return 0;
}

/// Initializes TestScene3DComponent component.

void TestScene3DComponent::init(void) 
{

	glfwInit();

	atexit(fin);

	int width = 800;//getComponentConfiguration()->getInt("AudioVideoConfig.Local.Width");
	int height= 600;//getComponentConfiguration()->getInt("AudioVideoConfig.Local.Height");

	bool fullscreen =getComponentConfiguration()->getBool("AudioVideoConfig_IsFullScreen");

	int res = glfwOpenWindow( width, height, 8, 8, 8, 8, 24, 8, fullscreen ? GLFW_FULLSCREEN : GLFW_WINDOW );
	if (res == GL_FALSE) 
	{
		glfwTerminate();
	}

	// Disable vertical synchronization
	glfwSwapInterval( 0 );

	// Close window callback
	glfwSetWindowCloseCallback(windowCloseListener);		

}


/// Unitializes the TestScene3DComponent component.

void TestScene3DComponent::quit(void) 
{
	glfwTerminate();
}

//IApplication implementation
void TestScene3DComponent::run(void) 
{	
	while (running) 
	{
		myIRenderizable->render();
		glfwSwapBuffers();
	}
}
