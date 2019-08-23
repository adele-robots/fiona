/// @file VapixPtzController.h
/// @brief CVapixPtzContoller class definition.

#ifndef __VAPIX_PTZ_CONTROLLER_H
#define __VAPIX_PTZ_CONTROLLER_H


#include <curl/curl.h>


/// \brief Pan and tilt motor control for AXIS network camaras.
///
/// This class offers functionality for driving the pan-tilt-zoom motors of a network
/// camara obeying the VAPIX 2.0 protocol. It is asumed that the camara has no password.

class CVapixPtzContoller 
{

public:
	CVapixPtzContoller();
	~CVapixPtzContoller();

	// Numeric IPs and internet host names
	void CVapixPtzContoller::SetCamaraIPAddress(char *camaraIPAddress);

	// (x, y) is in the coordinate space of a bitmap snapshot.
	// The point is then used by the server to calculate the pan/tilt move
	// required to (approximately) center the clicked point. 		
	void Center(int x, int y);


	// Movement step by step, roughly 5 degrees each step. 
	// Posible values of 'movement': home, up, down, left, right, upleft, upright, 
	// downleft, downright
	void MoveOneStep(char *movement);



	// Do absolute pan (horizontal rotation)
	// Software limits -180 < v < 180. Hardware limits are device dependant.
	void Pan(float v);


	// Do absolute pan (horizontal rotation)
	// Software limits -180 < v < 180. Hardware limits are device dependant.
	void Tilt(float v);


	// Do absolute Zoom.
	// 0 < z < 9999
	void Zoom(int z);

	// set pan/tilt motors speed. 
	//-100 < vx, vy < 100
	void SetAngularSpeed(int vx, int vy);


	// The same as SetAngularSpeed(v, v)
	//-100 < v < 100
	void SetSpeed(int v);

	// Obtain pan, tilt and zoom values reported by server
	void QueryPTZ(float *pan, float *tilt, int *zoom);


	// Query whether autofocus and autoiris are enabled
	void QueryAutoFocusAndAutoIris(bool *autoFocus, bool *autoIris);

	// Query pan, tilt, zoom and set pan/tilt motors in a single HTTP request
	void CVapixPtzContoller::QueryPanTiltZoomSetAngularSpeed(
		float *pan, 
		float *tilt,
		int *zoom,
		int vx, 
		int vy
	);



private:
	char *ptz_url;
	CURL *curl;
};


#endif
