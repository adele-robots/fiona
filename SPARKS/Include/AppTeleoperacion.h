#ifndef __APP_TELEOPERACION_H
#define __APP_TELEOPERACION_H


#include "Application.h"
#include "WebServer.h"


class AppTeleoperacion : public Application
{
public:
	void init(void);
	void run(void);

private:
	WebServer *webServer;
};


#endif