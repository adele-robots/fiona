#ifndef __PSISBAN_WINDOW_H
#define __PSISBAN_WINDOW_H

#include <windows.h>
#include "Events.h"


class Window {
public:

/// Ctor

	Window(psisban::IEventHandler *ev) : eventHandler(ev) {} 

/// Creates an initially hidden window

	void create(
		char* title, 
		int width, 
		int height, 
		int bits, 
		bool fullscreenflag
	);
	void show(void);
	void hide(void);
	
	static void messageLoop(void);
	static void SendPsisbanMessage(UINT msg, WPARAM wParam, LPARAM lParam);

public:
	// Win32 Window handle
	HWND hWnd;
	
	// GDI window's Drawing Context
	HDC hDC;

	// Open GL Rendering Context handles
	HGLRC hRC1;		// Initialization
	HGLRC hRC2;		// Rendering thread

	//MODIFIED
public:
	psisban::IEventHandler *eventHandler;

private:
	bool fullscreen;
	
protected:
	static Window *thisWindow; // for SendPsisbanMessage


	static LRESULT CALLBACK Window::WndProc(
		HWND	hWnd,			
		UINT	uMsg,			
		WPARAM	wParam,	
		LPARAM	lParam	
	);
//MODIFIED
	virtual LRESULT virtualWndProc(
		HWND	hWnd,			
		UINT	uMsg,			
		WPARAM	wParam,	
		LPARAM	lParam	
	)const=0;

};


#endif
