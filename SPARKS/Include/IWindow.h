#ifndef __I_WINDOW_H
#define __I_WINDOW_H


#ifdef _WIN32
#include <windows.h>
typedef HWND WindowHandle;
#else
#include <X11/X.h>
#include <X11/Xlib.h>
typedef XID WindowHandle;
#endif

class IWindow {
public:
#ifdef _WIN32
	virtual WindowHandle create(
		char* title, 
		int width, 
		int height, 
		int bits, 
		bool fullscreenflag
	) = 0;
	virtual WindowHandle getWindowHandle(void) = 0;
#else
	virtual Display* getWindowDisplay(void) = 0;
#endif

	virtual void show(void) = 0;
	virtual void hide(void) = 0;


	virtual int getColorDepth(void) = 0;
	
	virtual void makeCurrentopenGlThread(void) = 0;
	virtual void openGlSwapBuffers(void) = 0;

};


#endif
