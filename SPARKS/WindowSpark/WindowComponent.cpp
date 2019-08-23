/// @file WindowComponent.cpp
/// @brief WindowComponent class implementation.

#include "stdAfx.h"
#include <map>
#include "Logger.h"
#include "ErrorHandling.h"
#ifdef _WIN32
#include "Events.h"
#else
#endif
#include "Configuration.h"
#include "WindowComponent.h"
#include <signal.h>


#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "WindowComponent")) {
			return new WindowComponent(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif

// OJO varias instancias
#define WINDOW_CLASS_NAME "PsisbanWindowClass"

bool running = true;
void WindowComponent::hide(void) {
#ifdef _WIN32
	ShowWindow(hWnd, SW_HIDE);
	UpdateWindow(hWnd);
#else
	/********************************************************************************/
	/* dpy 	Especifica la conexión con el servidor X.								*/
	/* win 	Especifica la ventana.													*/
	/********************************************************************************/
	// La funcion hace un unmap a la ventana especificada y hace que el servidor X
	// genere un evento UnmapNotify.
	// Si la ventana especificada ya está asignada, XUnmapWindow () no tiene efecto.
	// Cualquier ventana secundaria ya no sera visible hasta que otra llamada map se
	// haga sobre la matriz.
	XUnmapWindow(dpy,win);
#endif
}


void WindowComponent::show(void) {
#ifdef _WIN32
	ShowWindow(hWnd, SW_SHOW);
	UpdateWindow(hWnd);
#else
	/********************************************************************************/
	/* dpy 	Especifica la conexión con el servidor X.								*/
	/* win 	Especifica la ventana.													*/
	/********************************************************************************/
	// La funcion asigna la ventana y todas las sub-ventanas que han tenido peticiones
	// del mapa.
	XMapWindow(dpy,win);

	/********************************************************************************/
	/* dpy 	Especifica la conexión con el servidor X.								*/
	/* win 	Especifica la ventana.													*/
	/********************************************************************************/
	// Esta funcion es similar a XMapWindow() que mapea en la ventana y en todas sus
	// sub-ventanas que han tenido solicitudes del mapa. Sin embargo, tambien plantea
	// la ventana especificada para la parte superior de la pila.
	XMapRaised(dpy,win);
#endif
}


/*	This Code Creates Our OpenGL Window.  Parameters Are:					*
 *	title			- Title To Appear At The Top Of The Window				*
 *	width			- Width Of The GL Window Or Fullscreen Mode				*
 *	height			- Height Of The GL Window Or Fullscreen Mode			*
 *	bits			- Number Of Bits To Use For Color (8/16/24/32)			*
 *	fullscreenflag	- Use Fullscreen Mode (TRUE) Or Windowed Mode (FALSE)	*/
#ifdef _WIN32
WindowHandle WindowComponent::create(
	char* title, 
	int width, 
	int height, 
	int bits, 
	bool fullscreenflag
)
{


	this->bits = bits;
	WNDCLASS	wc;						// Windows Class Structure
	DWORD		dwExStyle;				// Window Extended Style
	DWORD		dwStyle;				// Window Style
	RECT		WindowRect;				// Grabs Rectangle Upper Left / Lower Right Values
	WindowRect.left=(long)0;			// Set Left Value To 0
	WindowRect.right=(long)width;		// Set Right Value To Requested Width
	WindowRect.top=(long)0;				// Set Top Value To 0
	WindowRect.bottom=(long)height;		// Set Bottom Value To Requested Height

	fullscreen=fullscreenflag;			// Set The Global Fullscreen Flag

	HINSTANCE hInstance;
	hInstance			= GetModuleHandle(NULL);				// Grab An Instance For Our Window
	wc.style			= CS_HREDRAW | CS_VREDRAW | CS_OWNDC;	// Redraw On Size, And Own DC For Window.
	wc.lpfnWndProc		= (WNDPROC) WindowComponent::WndProc;			// WndProc Handles Messages
	wc.cbClsExtra		= 0;									// No Extra Window Data
	wc.cbWndExtra		= 0;									// No Extra Window Data
	wc.hInstance		= hInstance;							// Set The Instance
	wc.hIcon			= LoadIcon(NULL, IDI_WINLOGO);			// Load The Default Icon
	wc.hCursor			= LoadCursor(NULL, IDC_ARROW);			// Load The Arrow Pointer
	wc.hbrBackground	= NULL;									// No Background Required For GL
	wc.lpszMenuName		= NULL;									// We Don't Want A Menu
	wc.lpszClassName	= WINDOW_CLASS_NAME;					// Set The Class Name

	if (!RegisterClass(&wc))									// Attempt To Register The Window Class
	{
		ERR("Failed To Register The Window Class.");
	}
	
	if (fullscreen)												// Attempt Fullscreen Mode?
	{
		DEVMODE dmScreenSettings;								// Device Mode
		memset(&dmScreenSettings,0,sizeof(dmScreenSettings));	// Makes Sure Memory's Cleared
		dmScreenSettings.dmSize=sizeof(dmScreenSettings);		// Size Of The Devmode Structure
		dmScreenSettings.dmPelsWidth	= width;				// Selected Screen Width
		dmScreenSettings.dmPelsHeight	= height;				// Selected Screen Height
		dmScreenSettings.dmBitsPerPel	= bits;					// Selected Bits Per Pixel
		dmScreenSettings.dmFields=DM_BITSPERPEL|DM_PELSWIDTH|DM_PELSHEIGHT;

		// Try To Set Selected Mode And Get Results.  NOTE: CDS_FULLSCREEN Gets Rid Of Start Bar.
		if (ChangeDisplaySettings(&dmScreenSettings,CDS_FULLSCREEN)!=DISP_CHANGE_SUCCESSFUL)
		{
			ERR("Full screen mode failed");
		}
	}

	if (fullscreen)												// Are We Still In Fullscreen Mode?
	{
		dwExStyle=WS_EX_APPWINDOW;								// Window Extended Style
		dwStyle=WS_POPUP;										// Windows Style
		ShowCursor(FALSE);										// Hide Mouse Pointer
	}
	else
	{
		dwExStyle=WS_EX_APPWINDOW | WS_EX_WINDOWEDGE;			// Window Extended Style
		dwStyle=WS_OVERLAPPEDWINDOW;							// Windows Style
	}

	AdjustWindowRectEx(&WindowRect, dwStyle, FALSE, dwExStyle);		// Adjust Window To True Requested Size

	// Create The Window

	hWnd = CreateWindowEx(
			dwExStyle,							// Extended Style For The Window
			WINDOW_CLASS_NAME,					// Class Name
			title,								// Window Title
			dwStyle |							// Defined Window Style
			WS_CLIPSIBLINGS |					// Required Window Style
			WS_CLIPCHILDREN,					// Required Window Style
			0, 0,								// Window Position
			WindowRect.right-WindowRect.left,	// Calculate Window Width
			WindowRect.bottom-WindowRect.top,	// Calculate Window Height
			NULL,								// No Parent Window
			NULL,								// No Menu
			hInstance,							// Instance
			this								// Pass 'this' pointer to static message loop
	);

	if (!hWnd) ERR("Window Creation Error");

	#ifdef GENERAL_INI
	int WindowPositionX = getGlobalConfiguration()->getInt(const_cast<char *>("AudioVideoConfig.Local.WindowPositionX"));
	int WindowPositionY = getGlobalConfiguration()->getInt(const_cast<char *>("AudioVideoConfig.Local.WindowPositionY"));
	#else
	int WindowPositionX = getComponentConfiguration()->getInt(const_cast<char *>("AudioVideoConfig.Local.WindowPositionX"));
	int WindowPositionY = getComponentConfiguration()->getInt(const_cast<char *>("AudioVideoConfig.Local.WindowPositionY"));
	#endif

	bool windowHasBorder = getGlobalConfiguration()->getBool("AudioVideoConfig.WindowHasBorder");
	if (!windowHasBorder) {
		
		DWORD dwStyle = GetWindowLong(hWnd, GWL_STYLE);
		dwStyle &= ~(WS_CAPTION|WS_THICKFRAME);
		SetWindowLong(hWnd, GWL_STYLE, dwStyle);

		SetWindowPos(hWnd, HWND_TOP, 0, 0, width, height, 
            SWP_NOZORDER|SWP_DRAWFRAME 
		);
	}

	SetWindowPos(
		hWnd, 
		HWND_TOP, 
		WindowPositionX, 
		WindowPositionY, 
		width, 
		height, 
		SWP_NOZORDER 
	);

	return hWnd;

}

WindowHandle WindowComponent::getWindowHandle(void) {
	return hWnd;
}

#else

/* function called when our window is resized (should only happen in window mode) */
void resizeGLScene(unsigned int width, unsigned int height)
{
    if (height == 0)    /* Prevent A Divide By Zero If The Window Is Too Small */
        height = 1;

    /********************************************************************************/
    /* Los dos primeros parametros especifican la esquina inferior izquierda de la 	*/
    /* vista dentro de la ventana													*/
    /* width ancho de las dimensiones en pixels.									*/
    /* height alto de las dimensiones en pixels.									*/
    /********************************************************************************/
    // Las vistas se fijan mediante la llamada a la función de la librería gl, glViewport.
    // Un error muy frecuente es llamar a esta función con dos parejas de números
    // especificando el área de dibujo en la ventana. Se produciría un resultado erróneo,
    // ya que en realidad se trata de las dimensiones en lugar de un segundo punto
    // En definitiva, con esta llamada a la funcion se fija el nuevo tamaño del area
    // de presentacion (vista),es decir, la porcion de ventana sobre la que se dibuja.
    /* Reset The Current Viewport And Perspective Transformation */
    glViewport(0, 0, width, height);

    // La funcion activa la matriz de proyeccion
    // La matriz de proyección especifica el tamaño y la forma del volumen de visualización.
    // El volumen de visualización es aquel cuyo contenido es el que se representa en
    // pantalla.
    // Está delimitado por una serie de planos de trabajo. De estos planos, los más
    // importantes son los planos de corte, que son los que nos acotan el volumen de
    // visualización por delante y por detrás.
    // En el plano más cercano a la cámara (znear) es donde se proyecta la escena para
    // luego pasarla a la pantalla.
    // Todo lo que esté más adelante del plano de corte más alejado de la cámara (zfar)
    // no se representa.
    glMatrixMode(GL_PROJECTION);

    // Funcion que carga una matriz identidad sobre la que realizar una multiplicación
    // Hay que tener en cuenta el factor de proporcionalidad para que la imagen no se
    // deforme.
    glLoadIdentity();

    /********************************************************************************/
    /* angulo: Angulo para el campo de vision en sentido vertical					*/
    /* aspecto: Relacion entre la altura y la anchura								*/
    /* znear y zfar : Son los planos que acotan el fustrum al observador. No son las*/
    /* posiciones																	*/
    /* de esos planos en el espacio 3D, representan la distancia desde el centro de */
    /* proyección,con valor positivo hacia delante y negativo hacia atrás			*/
    /********************************************************************************/
    // Todos los parametros son flotantes.
    // Funcion para definir la proyeccion
    gluPerspective(45.0f, (GLfloat)width / (GLfloat)height, 0.1f, 100.0f);

    // Activa la matriz del modelador.
    // La matriz del modelador es una matriz 4x4 que representa el sistema de coordenadas
    // transformado que se está usando para colocar y orientar los objetos. Si se
    // multiplica la matriz del vértice (de tamaño 1x4) por ésta se obtiene otra matriz
    // 1x4 con los vértices transformados sobre ese sistema de coordenadas.
    glMatrixMode(GL_MODELVIEW);
}

Window WindowComponent::create(
			char* title,
			int width,
			int height,
			int bits,
			bool fullscreenflag
		)
{

	/* attributes for a single buffered visual in RGBA format with at least 4 bits per
	 * color and a 16 bit depth buffer */

	// En el formato RGBA un color se expresa con 4 bytes. El primero es el componente
	// Rojo,el segundo el componente Verde y el tercero el componente Azul
	// (Red-Green-Blue).Hasta aquí es formato RGB.
	// El RGBA incluye otro byte más que se suele llamar Alpha, e indica la transparencia.
	static int attrListSgl[] = {GLX_RGBA, GLX_RED_SIZE, 4,
	    GLX_GREEN_SIZE, 4,
	    GLX_BLUE_SIZE, 4,
	    GLX_DEPTH_SIZE, 16,
	    None};

	/* attributes for a double buffered visual in RGBA format with at least 4 bits per
	 * color and a 16 bit depth buffer */
	static int attrListDbl[] = { GLX_RGBA, GLX_DOUBLEBUFFER,
		    GLX_RED_SIZE, 4,
		    GLX_GREEN_SIZE, 4,
		    GLX_BLUE_SIZE, 4,
		    GLX_DEPTH_SIZE, 16,
		    None };

	// handle to the root window
	root = DefaultRootWindow(dpy);

	// default screen number referenced by XOpenDisplay
	screen = DefaultScreen(dpy);

	/* get an appropriate visual. We prefer double buffered instead of single
	 * buffered */
	// Buscamos el formato de pixel con las caracteristicas de la pantalla en
	// que trabajamos y cargamos la informacion en una estructura
	vi = glXChooseVisual(dpy, screen, attrListDbl);

	if (vi == NULL)
	    {
	        vi = glXChooseVisual(dpy, screen, attrListSgl);

	        // En este caso sera SingleBuffered
	        doubleBuffered = False;
	    }
	    else
	    {
	    	// En este caso sera DoubleBuffered
	        doubleBuffered = True;
	    }

	 // create GLX contexts
	 // GLX context to render
	 // Creamos un contexto para OpenGL. Indicamos la pantalla,
	 // informacion visual, contextos con los que comparte display lists
	 // (ninguno para nosotros) y si accede directamente al hardware.
	 ctx2 = glXCreateContext(dpy, vi, 0, GL_TRUE);

	 // GLX context to initialize
	 // Creamos un contexto para OpenGL. Indicamos la pantalla,
	 // informacion visual, contextos con los que comparte display lists
	 // (ninguno para nosotros) y si accede directamente al hardware.
	 ctx = glXCreateContext(dpy, vi, ctx2, GL_TRUE);

	 // create a color map
	 // Creamos ya la ventana.
	 // Damos los valores que queremos a la estructura cmap
	 // Indicamos el colormap, la estructura de colores.
	 // Si no fallara al crear la ventana. Lo hacemos en base a vi.
	 cmap = XCreateColormap(dpy, RootWindow(dpy, vi->screen),vi->visual, AllocNone);

	 // fill struct swa
	 swa.colormap = cmap;

	 // El tipo de pixel del borde.
	 swa.border_pixel = 0;

	 //events recognize by our window
	 swa.event_mask = ExposureMask | KeyPressMask | ButtonPressMask |StructureNotifyMask | SubstructureNotifyMask;

	 //create a window in window mode (it isn't showed)
	 win = XCreateWindow(dpy, //Pantalla
						root, //Ventana padre
						0,0, // Posicion de la ventana
						width,height, //Tamaño de la ventana
						0, //Ancho del borde
						vi->depth, // Profundidad respecto a otras ventanas
						InputOutput, // Tipo de ventana. Debe ser este para poder pintar.
						vi->visual, // Informacion visual
						CWBorderPixel | CWColormap | CWEventMask, // Valores que pasamos en cmap.
						&swa // Estructura con atributos de la ventana.
						);

	  /* only set window title and handle wm_delete_events if in windowed mode */
	 // Definimos un "Atom", un nombre o indicador para una caracteristica o evento de la ventana.
	 // Asociamos este Atom al evento de borrado de la ventana "WM_DELETE_WINDOW".
	 wmDelete = XInternAtom(dpy, "WM_DELETE_WINDOW", True);

	 // Indicamos a la ventana que queremos controlar el evento de borrado pasandole el Atom wmDelete.
	 // Mas tarde, cuando comprobemos los eventos tendremos que comprobar el evento "ClientMessage"
	 // para ver si se ha producido el cierre de la ventana (u otro que hubieramos definido igual).
	 // Por su parte, la ventana, en vez de cerrarse, lo que hara sera notificar el evento, no se borrara.
	 // El borrado o lo que creamos conveniente, lo haremos nosotros.
	 XSetWMProtocols(dpy, win, &wmDelete, 1);

	 // Ponemos mas propiedades a la ventana como el titulo o el icono.
	 XSetStandardProperties(dpy, win, title,title, None, NULL, 0, NULL);

	 /********************************************************************************/
	 /* dpy 	Especifica la conexión con el servidor X.							 */
	 /* win 	Especifica la ventana.												 */
	 /* title 	Especifica el nombre de la ventana, que debe ser una cadena terminada*/
	 /* en nulo.																	 */
	 /********************************************************************************/
	 // La función asigna el nombre de la ventana a la ventana determinada.
	 XStoreName(dpy,win,title);

	 // Asocio la ventana y el contexto GLX
	 glXMakeCurrent(dpy, win, ctx);

	 /********************************************************************************/
	 /* dpy 	Especifica la conexion con el servidor X							 */
	 /* ctx		Especifica el contexto de GLX que se consulta						 */
	 /********************************************************************************/
	 // La funcion devuelve true si ctx es un contexto de representacion directa y
	 // false en caso contrario.
	 // Contextos directos:  Los comandos de renderizado son pasados directamente desde
	 // el espacio de direcciones del proceso que realiza la llamada al sistema de
	 // representación, sin pasar por el servidor X.
	 // Contextos no directos: Los comandos de renderizado son pasados por el servidor
	 // X para pasar al sistema de representación.
	 if(glXIsDirect(dpy,ctx))

	 /* general OpenGL initialization function */

	 // Funcion para rellenar de color los poligonos
	 // GL_SMOOTH rellenara el poligono interpolando los colores activos en la definición
	 // de cada vértice
	 glShadeModel(GL_SMOOTH);

	 // Color con que limpia la pantalla cada frame
	 glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

	 // Define el valor para limpiar
	 glClearDepth(1.0f);

	 // Funcion que activa la caracteristica "depth_test" en la que se introduce uno de los
	 // buffers que ogl pone a nuestra disposición, el “depth buffer” (buffer de profundidad,
	 // también conocido como “z-buffer”). En él se almacenan “las zetas” o distancias desde
	 // el punto de vista a cada píxel de los objetos de la escena y, a la hora de pintarlos
	 // por pantalla, hace una comprobación de que no haya ninguna primitiva que esté por
	 // delante tapando a lo que se va a pintar en ese lugar.
	 glEnable(GL_DEPTH_TEST);

	 // GL_LEQUAL True si Z de referencia <= Z de profundidad. ( Normal )
	 glDepthFunc(GL_LEQUAL);

	 // Esta función sirve para optimizar el renderizado, y se utiliza para muchas cosas.
	 // En este caso es para hacer un buen cálculo de la perspectiva perdiendo un poco de
	 // rendimiento
	 glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);

	 /* we use resizeGLScene once to set up our initial perspective */
	 resizeGLScene(width, height);

	 // Funcion que hace que todos los comandos de ogl que estén en espera se ejecuten.
	 // Fuerza a OpenGL a ejecutar los comandos que le hemos dado, esto garantiza que se
	 // realizara y completara en un tiempo finito.
	 glFlush();
	return win;
}
Window WindowComponent::getWindowHandle(void)
{
	return win;
}

#endif

#ifdef _WIN32
LRESULT CALLBACK WindowComponent::WndProc(
	HWND	hWnd,			// Window handle
	UINT	uMsg,			// Message For This Window
	WPARAM	wParam,			// Additional Message Information
	LPARAM	lParam			// Additional Message Information
)
{
	static WindowComponent *thisWindowComponent = NULL;

	CREATESTRUCT *cs;

	switch (uMsg)									// Check For Windows Messages
	{
		case WM_CREATE:	
			cs = (CREATESTRUCT *)lParam;
			thisWindowComponent = (WindowComponent *)cs->lpCreateParams;
			break;

        // Process other messages.   

		case WM_SYSCOMMAND:							// Intercept System Commands
		{
			switch (wParam)							// Check System Calls
			{
				case SC_SCREENSAVE:					// Screensaver Trying To Start?
				case SC_MONITORPOWER:				// Monitor Trying To Enter Powersave?
				return 0;							// Prevent From Happening
			}
			break;									// Exit
		}

		case WM_CLOSE:								// Did We Receive A Close Message?
		{
			PostQuitMessage(0);						// Send A Quit Message
			return 0;								// Jump Back
		}

		case WM_SIZE:								// Resize The OpenGL Window
		{
			break;
		}

        case WM_PAINT:
            // Get a handle to the device context & Validate the invalid region
			PAINTSTRUCT ps;
            HDC hdc = BeginPaint(hWnd, &ps);
            // Draw the text string
            TextOut(hdc, 50, 50, "Hello world!", 12);
            // Release the device context
            EndPaint(hWnd, &ps);
			break;

	}	

	// if ((WM_APP <= uMsg) && (uMsg < 0xC000))

	if (thisWindowComponent) {	/* there are pre WM_CREATE messages */
		psisban::Messages psisbanMessage = (psisban::Messages)uMsg;
		std::map<psisban::Messages, EventCallback>::iterator it;
		it = thisWindowComponent->eventListeners.find(psisbanMessage);
		if (it != thisWindowComponent->eventListeners.end()) {
			EventCallback eventCallback;
			eventCallback = it->second;

			// call callback
			eventCallback((void *)wParam, (void *)lParam);

			return 0;
		}
	}


	// Pass All Unhandled Messages To DefWindowProc
	return DefWindowProc(hWnd,uMsg,wParam,lParam);
}
#else

#endif

void WindowComponent::postEvent(psisban::Messages msg, void *data1, void *data2) {
#ifdef _WIN32
	// dont wait return, post to the event queue
	::PostMessage(hWnd, msg, WPARAM(data1), LPARAM(data2));
#else
#endif
}

void WindowComponent::sendEvent(psisban::Messages msg, void *data1, void *data2) {
#ifdef _WIN32
	// wait return guaranteeing validity of stack pointers in event handlers
	// direct window proc call without passing for the event loop?
	::SendMessage(hWnd, msg, WPARAM(data1), LPARAM(data2));
#else
#endif
}

void WindowComponent::registerListener(psisban::Messages msg, EventCallback eventCallback) {
#ifdef _WIN32
	eventListeners.insert(std::pair<psisban::Messages, EventCallback>(msg, eventCallback));
#else
#endif
}

void WindowComponent::eventLoop(void) {
#ifdef _WIN32
	MSG msg;
	BOOL bRet;

	// OJO, hace falta llamar a TranslateAccelerator()?
	while( (bRet = GetMessage( &msg, NULL, 0, 0 )) != 0)
	{ 
		if (bRet == -1)
		{
			ERR("Error in the core message loop, GetMessage");
		}
		else
		{
			TranslateMessage(&msg); 
			DispatchMessage(&msg); 
		}
	}
#else

	// Estructura para la gestion de eventos
	XEvent event;

	// Bucle principal
	Bool done;

	done = False;

	// Definimos un "Atom", un nombre o indicador para una caracteristica o evento de la ventana.
	// Asociamos este Atom al evento de borrado de la ventana "WM_DELETE_WINDOW".
	// En este caso es False.
	Atom wmDeleteMessage = XInternAtom(dpy, "WM_DELETE_WINDOW", False);

	/* wait for events*/
	    while (!done && running)
	    {
	    	usleep(40000);
	    	/* handle the events in the queue */
	    	while (XPending(dpy) > 0 && running)
	    	{
	    	  /********************************************************************************/
	    	  /* dpy 	Especifica la conexión con el servidor X							  */
	    	  /********************************************************************************/
			  // La funcion bloquea todos las demás hilos del uso de la pantalla especificada.
			  // Otros subprocesos que intenten utilizar la pantalla se bloquearán hasta que se
	    	  // desbloquee este hilo.
			  // La pantalla en realidad no se desbloqueará hasta que XUnlockDisplay () ha sido
	    	  // llamado el mismo número de veces XLockDisplay ().
	    	  XLockDisplay(dpy);

	    	  /********************************************************************************/
			  /* dpy 	Especifica la conexión con el servidor X							  */
	    	  /* event	Estructura para la gestion de eventos							      */
			  /********************************************************************************/
	    	  // Obtengo el siguiente mensaje y lo pongo en "evento".
	    	  XNextEvent(dpy, &event);

	    	  /********************************************************************************/
	    	  /* dpy Especifica la conexión con el servidor X								  */
	    	  /********************************************************************************/
	    	  // La funcion permite que otros subprocesos puedan utilizar la pantalla especificada
	    	  // de nuevo.
	    	  // A los temas que han sido bloqueados en la pantalla se les permite continuar.
	    	  // IMPORTANTE!! Si XLockDisplay () ha sido llamado varias veces de un hilo, entonces
	    	  // XUnlockDisplay debe ser llamado un número igual de veces antes de que la pantalla
	    	  // está en realidad desbloqueado.
	    	  XUnlockDisplay(dpy);

	    	  // Trato el evento segun el tipo
	    	  switch (event.type)
	    	  {
	    		    case CreateNotify:
	    		    	// En este caso la ventana esta creada
	    		    	break;

	    		    case VisibilityNotify:
	    		    	// En este caso la ventana esta visible
	    		    	break;

	    		    case Expose:
		    		    // Si es necesario redibujar la pantalla
						if (event.xexpose.count != 0)
							break;
						break;


	    		    case ConfigureNotify:
	    		    	// Si se ha reconfigurado la ventana, cambio de tamaño por ejemplo.
	    		    	/* call resizeGLScene only if our window-size changed */
						if ((event.xconfigure.width != width) || (event.xconfigure.height != height))
						{
							width = event.xconfigure.width;
							height = event.xconfigure.height;
							resizeGLScene(event.xconfigure.width, event.xconfigure.height);
						}
						break;


	    	        case ButtonPress:
	    	        	/* exit in case of a mouse button press */
						// Si es la presion de un boton del raton.
						done = True;
						break;

	    	        case KeyPress:
						// Si es la presion de una tecla.
						if (XLookupKeysym(&event.xkey, 0) == XK_Escape)
						{
							// Tecla de Escape pulsada
							done = True;
						}
						if (XLookupKeysym(&event.xkey,0) == XK_F1)
						{
							// Tecla F1 pulsada
						}
						if (XLookupKeysym(&event.xkey,0) == XK_F2)
	    	            {
							// Tecla F2 pulsada

							/********************************************************************************/
							/* dpy Especifica la conexión con el servidor X							        */
							/* win Especifica la ventana													*/
							/********************************************************************************/
							// La funcion hace un unmap a la ventana especificada y hace que el servidor X
							// genere un evento UnmapNotify.
							// Si la ventana especificada ya está asignada, XUnmapWindow () no tiene efecto.
							// Cualquier ventana secundaria ya no sera visible hasta que otra llamada map se
							// haga sobre la matriz.
	    	                XUnmapWindow(dpy,win);
	    	            }
						if (XLookupKeysym(&event.xkey,0) == XK_F3)
	    	            {
							// Tecla F3 pulsada

							/********************************************************************************/
							/* dpy Especifica la conexión con el servidor X							        */
							/* win Especifica la ventana													*/
							/********************************************************************************/
							// La funcion asigna la ventana y todas las sub-ventanas que han tenido peticiones
							// del mapa.
	    	                XMapWindow(dpy,win);
	    	            }
	    	            break;

	    		    case DestroyNotify:
	    		    	// Ventana destruida
	    		    	break;

	    		    case ClientMessage:
	    		    	 // Si el mensaje es producido desde un Atom que hayamos indicado a la ventana.
	    	            if (*XGetAtomName(dpy, event.xclient.message_type) == *"WM_PROTOCOLS")
	    	            {
	    	                 done = True;
	    	            }
	    	            break;

	    	            default:
	    	                break;
	    	     }
	    	  }
	    }
	    if( win != 0 )
	    {
	    	XLockDisplay(dpy);
	    	XDestroyWindow( dpy, win );
	    	XUnlockDisplay(dpy);
	    	win = 0;
	    }
	    if( ctx != NULL )
	    {
	    	XLockDisplay(dpy);
	    	glXDestroyContext( dpy, ctx );
	    	XUnlockDisplay(dpy);
	    	ctx = NULL;
	    }
	    if( cmap != 0 )
	    {
	    	XFreeColormap( dpy, cmap);
	    	cmap = 0;
	    }

	    if( vi != NULL )
	    {
	    	XFree( vi );
	    	vi = NULL;
	    }
#endif
}

void WindowComponent::signalHandler(int sig){
	running = false;
}

void sasigaction(int sig, siginfo_t * info, void * w){
	running = false;
	LoggerError("SIGTERM send by %d", info->si_pid);
}

void WindowComponent::run(void) {
	struct sigaction act;
	act.sa_flags = SA_SIGINFO;
	act.sa_sigaction = &sasigaction;
	sigaction(SIGTERM, &act, NULL);
	//signal((int) SIGTERM, signalHandler);
	signal((int) SIGINT, signalHandler);
	eventLoop();	// IEventQueue method
}

#ifdef _WIN32
BOOL WINAPI CtrlCHandlerProc(DWORD dwCtrlType) {


	HWND hWnd = FindWindow(WINDOW_CLASS_NAME, NULL);
	if (hWnd == NULL) {
		LoggerError("Couldn't find window of class '%s' to handle ctrl+c");
	}
	else {
		::PostMessage(hWnd, WM_QUIT, 0, 0);
	}

	return TRUE;
}
#else
#endif

void WindowComponent::init(void) {

	//We must call this to make X "thread safe"
	XInitThreads();

	// open connection with the server
	// Localizo una pantalla que usar. Normalmente, en un PC, deberia haber almenos una.
	// Si se llama con NULL obtendremos la pantalla por defecto.
	dpy = XOpenDisplay(NULL);

	if(dpy == NULL)
	{
		ERR("Cannot open display");
	}

	char windowTitle[80];

	getComponentConfiguration()->getString(const_cast<char *>("AudioVideoConfig_WindowTitle"), windowTitle, 80);

	width = getGlobalConfiguration()->getInt(const_cast<char *>("AudioVideoConfig_Width"));
	height = getGlobalConfiguration()->getInt(const_cast<char *>("AudioVideoConfig_Height"));

	bool isFullScreen = getComponentConfiguration()->getBool(const_cast<char *>("AudioVideoConfig_IsFullScreen"));

	create(windowTitle, width, height, 32, isFullScreen);

#ifdef _WIN32
	BOOL ok;
	ok = SetConsoleCtrlHandler(CtrlCHandlerProc, TRUE);
	if (!ok) ERR("SetConsoleCtrlHandler");
#else
#endif
}

void WindowComponent::quit(void)
{

}

void WindowComponent::handleError(char *s) {
	LoggerError(s);

#ifdef _WIN32
	::PostMessage(hWnd, WM_QUIT, 0, 0);
#else
#endif
}

int WindowComponent::getColorDepth(void) {
	return bits;
}

void WindowComponent::makeCurrentopenGlThread(void) {
#ifdef _WIN32
	bool ok = wglMakeCurrent(hDC, hRC2);
	if (!ok) ERR("wglMakeCurrent");
#else
	/********************************************************************************/
	/* dpy Especifica la conexión con el servidor X									*/
	/********************************************************************************/
	// La funcion bloquea todos las demás hilos del uso de la pantalla especificada.
	// Otros subprocesos que intenten utilizar la pantalla se bloquearán hasta que se
	// desbloquee este hilo.
	// La pantalla en realidad no se desbloqueará hasta que XUnlockDisplay () ha sido
	// llamado el mismo número de veces XLockDisplay ().
	XLockDisplay(dpy);

	/********************************************************************************/
	/* dpy Especifica la conexión con el servidor X									*/
	/* win Debe ser un identificador de ventana X o una identificación GLX pixmap.  */
	/* ctx2 Especifica un contexto de representación GLX que se va a unir a win.    */
	/********************************************************************************/
	// glXMakeCurrent hace dos cosas:
	// Hace que ctx sea el actual contexto de prestación del subproceso de llamada,
	// reemplazando previamente el actual contexto si había uno, y que atribuye un ctx
	// a un GLX drawable, a una ventana o a un pixmap GLX.
	// Como glXMakeCurrent siempre reemplaza el contexto de representación actual con
	// ctx, sólo puede haber un contexto actual por hilo.
	// Si existen órdenes pendientes en el contexto anterior, se vacian antes de que
	// sea puesto en libertad.
	// Para liberar el contexto actual sin asignar uno nuevo, debe hacerse una llamada
	// a  glXMakeCurrent con ningun conjunto dibujable y el ctx se establece en NULL.
	// La funcion retorna True si tiene éxito, False en caso contrario. Si se devuelve
	// FALSE, el contexto de representación es el que era previamente actual y los
	// drawables (si los hay) se mantienen igual.
	// IMPORTANTE!! Porque glXMakeCurrent siempre reemplaza el contexto de
	// representación actual con ctx, sólo puede haber un contexto actual por hilo.
	bool ok2 = glXMakeCurrent(dpy, win, ctx2);

	/********************************************************************************/
	/* dpy Especifica la conexión con el servidor X									*/
	/********************************************************************************/
	// La funcion permite que otros subprocesos puedan utilizar la pantalla especificada
	// de nuevo.
	// A los temas que han sido bloqueados en la pantalla se les permite continuar.
	// IMPORTANTE!! Si XLockDisplay () ha sido llamado varias veces de un hilo, entonces
	// XUnlockDisplay debe ser llamado un número igual de veces antes de que la pantalla
	// está en realidad desbloqueado.
	XUnlockDisplay(dpy);
	if (!ok2) ERR("glXMakeCurrent,ctx2");

#endif
}

void WindowComponent::openGlSwapBuffers() {
#ifdef _WIN32
	BOOL ok = SwapBuffers(hDC);
	if (!ok) ERR("SwapBuffers");
#else
	/********************************************************************************/
	/* dpy Especifica la conexión con el servidor X									*/
	/********************************************************************************/
	// La funcion bloquea todos las demás hilos del uso de la pantalla especificada.
	// Otros subprocesos que intenten utilizar la pantalla se bloquearán hasta que se
	// desbloquee este hilo.
	// La pantalla en realidad no se desbloqueará hasta que XUnlockDisplay () ha sido
	// llamado el mismo número
	// de veces XLockDisplay ().
	XLockDisplay(dpy);

	/********************************************************************************/
	/* dpy 	Especifica la conexión con el servidor X.								*/
	/* win 	Especifica la ventana cuyos buffers se intercambian.					*/
	/********************************************************************************/
	// La funcion hace el almacenamiento posterior del buffer visible.
	// La subrutina promueve el contenido del buffer de reserva de win para convertirse
	// en el contenido del buffer frontal de Drawable.
	// El contenido de la memoria intermedia en aquel entonces se convertira en
	// indefinido.
	// La actualización tiene lugar típicamente durante el retrazado vertical de la
	// pantalla, en lugar de inmediatamente después de llamar a glXSwapBuffers.
	// Los siguientes comandos de OpenGL pueden ser emitidos inmediatamente después de
	// llamar a glXSwapBuffers,pero estas órdenes no se ejecutan hasta que el buffer
	// de intercambio se ha completado.
	glXSwapBuffers(dpy,win);

	/*********************************************************************************/
	/* dpy 	Especifica la conexión con el servidor X.								 */
	/*********************************************************************************/
	// La funcion permite que otros subprocesos puedan utilizar la pantalla especificada
	// de nuevo.
	// A los temas que han sido bloqueados en la pantalla se les permite continuar.
	// IMPORTANTE!! Si XLockDisplay () ha sido llamado varias veces de un hilo, entonces
	// XUnlockDisplay debe ser llamado un número igual de veces antes de que la pantalla
	// está en realidad desbloqueado.
	XUnlockDisplay(dpy);
#endif
}

Display* WindowComponent::getWindowDisplay(void)
{
	return dpy;
}
