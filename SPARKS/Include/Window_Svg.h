#ifndef WINDOW_SVG_H
#define WINDOW_SVG_H

#include "stdAfx.h"
#include "Window.h"

#include "platform/agg_platform_support.h"
//#include "the_application.h"
#include "my_application.h"
// openGL context creation
#pragma comment(lib, "opengl32.lib")

class Window_Svg : public Window
{
public:
	Window_Svg(psisban::IEventHandler *ev):
	  Window(ev)
	  {
		  app_svg=new my_application();
	  }

	Window_Svg(psisban::IEventHandler *ev,agg::pix_format_e format, bool flip_y); 

public:
	void markWindow(unsigned width,unsigned height);
	//void copyApp(the_application* app_svg_copy);

public:
	my_application* app_svg;
	
	virtual LRESULT virtualWndProc(
			HWND	hWnd,			
			UINT	uMsg,			
			WPARAM	wParam,	
			LPARAM	lParam	
			)const;

	};


#endif