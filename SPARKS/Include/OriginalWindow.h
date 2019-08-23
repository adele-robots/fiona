#ifndef ORIGINALWINDOW_H
#define ORIGINALWINDOW_H

#include "Window.h"


class OriginalWindow : public Window
{
public:

	OriginalWindow(psisban::IEventHandler *ev):
	  Window(ev)
		{}
public:
	virtual LRESULT virtualWndProc(
		HWND	hWnd,			
		UINT	uMsg,			
		WPARAM	wParam,	
		LPARAM	lParam	
	)const;
//private:
//	static Window* thisOriginalWindow;
	
};

#endif