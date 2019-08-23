/// @file WindowComponent.cpp
/// @brief Component WindowComponent main class.

#ifndef __WindowComponent_H
#define __WindowComponent_H

#include "Component.h"

// Provided
#include "IWindow.h"
#include "IApplication.h"
#include "IAsyncFatalErrorHandler.h"
#include "IEventQueue.h"

#ifdef _WIN32
#else
#include <X11/X.h>
#include <X11/Xlib.h>
#include <GL/gl.h>
#include <GL/glx.h>
#include <GL/glu.h>
#endif

/// @brief This is the main class for component WindowComponent.

class WindowComponent :
	public Component,
	public IWindow,		
	public IApplication,		
	public IEventQueue,
	public IAsyncFatalErrorHandler
{
public:
	WindowComponent(
		char *instanceName, 
		ComponentSystem *cs
	) : Component(instanceName, cs)
	{}

private:
	void initializeRequiredInterfaces() {	
		// no required interfaces
	}

public:
	static void signalHandler(int sig);
	void init();
	void quit();

#ifdef _WIN32
	// IWindow implementation
	WindowHandle create(
		char* title, 
		int width, 
		int height, 
		int bits, 
		bool fullscreenflag
	);

	WindowHandle getWindowHandle(void);
#else
	Window create(
			char* title,
			int width,
			int height,
			int bits,
			bool fullscreenflag
		);
	Window getWindowHandle(void);
	Display* getWindowDisplay(void);

#endif
	void makeCurrentopenGlThread();
	void openGlSwapBuffers();

	int getColorDepth(void);
	
	void show(void);
	void hide(void);
	
	// IApplication implementation
	void run();

	// IEventQueue implementation
	void postEvent(psisban::Messages, void *data1, void *data2);
	void sendEvent(psisban::Messages, void *data1, void *data2);
	void registerListener(psisban::Messages, EventCallback);
	void eventLoop(void);

	// IAsyncFatalErrorHandler implementation
	void handleError(char *msg);

#ifdef _WIN32
public:
	// Win32 Window handle
	HWND hWnd;
	// GDI window's Drawing Context
	HDC hDC;

	// Open GL Rendering Context handles
	HGLRC hRC1;		// Initialization
	HGLRC hRC2;		// Rendering thread
#else
public:

	// Puntero para acceso a la pantalla. En realidad es del servidor X
	// pero a efectos paracticos es nuestra pantalla
	Display *dpy;

	// La ventana en si
	Window win;

	int screen;

	// Estructura con la informacion del visual
	XVisualInfo *vi;

	// Mapa de colores para el visual
	Colormap cmap;

	// Ventana raíz de nuestro monitor (Escritorio)
	Window root;

	// Estructura para atributos de ventana.
	XSetWindowAttributes swa;

	// Contexto grafico de opengl
	GLXContext ctx;

	// Contexto grafico de opengl
	GLXContext ctx2;

	// Marcaremos si usamos doble buffer o no
	Bool doubleBuffered;

	Atom wmDelete;

#endif

private:
	bool fullscreen;
	int width;
	int height;
	int bits;
#ifdef _WIN32
private:
	static LRESULT CALLBACK WindowComponent::WndProc(
		HWND	hWnd,			
		UINT	uMsg,			
		WPARAM	wParam,	
		LPARAM	lParam	
	);
#else
#endif
};


#endif
