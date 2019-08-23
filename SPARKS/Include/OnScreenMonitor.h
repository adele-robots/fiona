/// @file OnScreenMonitor.h
/// @brief OnScreenMonitor class declaration.


#ifndef __ONSCREEN_MONITOR_H
#define __ONSCREEN_MONITOR_H


#include <vector>
#include <deque>
#include <Horde3D.h>
#include <Horde3DUtils.h>
#include "StopWatch.h"


class OnScreenMonitor {
public:
	OnScreenMonitor(std::string n, int tamanioVentanaMediaMovil, float persistenceTime);
	void start(void);
	void stop(void);
	static void addOnScreenMonitor(OnScreenMonitor *);
	static void renderOnScreenMonitors(H3DRes _fontMatRes);

private:
	std::string name;
	float verticalPosition;
	StopWatch stopWatch;
	float startTime;
	float stopTime;
	float ultimaMediaMovil;
	float persistenceTime;

	int tamanioVentanaMediaMovil;
	std::deque<float> almacenMediaMovil;

	const static float fontScale;
	const static float horizontalPosition;
	const static float verticalPositionIncrement;

	static float currentVerticalPosition;
	static std::vector<OnScreenMonitor *> onScreenMonitors;
};

#endif