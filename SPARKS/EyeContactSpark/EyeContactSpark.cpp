/// @file EyeContactSpark.cpp
/// @brief EyeContactSpark class implementation.

// Third party libraries are linked explicitly once in the project.
// #pragma comment(lib, "thirdPartyLib.lib")

#include "stdAfx.h"
#include "EyeContactSpark.h"


#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "EyeContactSpark")) {
			return new EyeContactSpark(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif
const int mean_time_to_next_blink = 8000;

/// Initializes EyeContactSpark component.
void EyeContactSpark::init(void){
	//Load Configuration
	//char bodyConfigurationFileName[1024];
	//getGlobalConfiguration()->getFilePath("Scene.BodyConfig",
	//							bodyConfigurationFileName,
	//							1024
	//						);

	isFaceDetected = false;
	faceTrackedHorizontalPos = 0;
	faceTrackedVerticalPos = 0;
	presenceLevel = 0;

	currentPan = 0;
	currentTilt = 0;
	headPanOffset = 0;
	headTiltOffset = 0;
	hasEyeBones = false;

	//Perlin noise settings for neck movements
	m_pPerlinX = new Perlin(7, 8, 0.5, 42);
	m_pPerlinY = new Perlin(7, 8, 0.5, 43);
	m_pPerlinZ = new Perlin(7, 8, 0.5, 44);

#ifdef LOW_PASS_FILTER
	// OJO 15Hz frecuencia hardcodeada
	const double sampleFrequency = 15;
	const double cutoffFrequency = 0.5;
	neck2dFilter = new LowPass(sampleFrequency, cutoffFrequency);
#endif

	myFrameEventPublisher->addFrameEventSubscriber(this);


}

/// Unitializes the EyeContactSpark component.
void EyeContactSpark::quit(void){
}

//IDetectedFacePositionConsumer implementation
void EyeContactSpark::consumeDetectedFacePosition(bool faceDetected, double x, double y){
	isFaceDetected = faceDetected;
	faceTrackedHorizontalPos = x;
	faceTrackedVerticalPos = y;
}



void EyeContactSpark::updateHeadIdleMovements(void){
	float randomPan, randomTilt;
	updateHeadRandomMovements(&randomPan, &randomTilt);

	float eyeContactPan, eyeContactTilt;
	updateEyeContactHeadPos(&eyeContactPan, &eyeContactTilt);

	presenceLevel = 1;
	// updatePresenceLevel(&presenceLevel);

	//#ifdef GENERAL_INI
	// OJO revisar la l�gica de esto en el tema local/remoto
	//if (!getGlobalConfiguration()->getBool("AudioVideoConfig.Local.Capture.Enabled")) {
	//#else
	if (!getComponentConfiguration()->getBool("AudioVideoConfig_Local_Capture_Enabled")){
	//#endif
		presenceLevel = 0;
	}

	// combinaci�n lineal convexa, con coeficiente 'presenceLevel'
	float combinedPan, combinedTilt;
	
	combinedPan = presenceLevel * eyeContactPan + (1 - presenceLevel) * randomPan;
	combinedTilt = presenceLevel * eyeContactTilt + (1 - presenceLevel) * randomTilt;

	//headPanOffset = bodyConfiguration.getFloat("Body.Joints.HeadPanOffset");
	  headPanOffset =getGlobalConfiguration()->getFloat("Body_Joints_HeadPanOffset");
	//headTiltOffset = bodyConfiguration.getFloat("Body.Joints.HeadTiltOffset");
	  headTiltOffset=getGlobalConfiguration()->getFloat("Body_Joints_HeadTiltOffset");

	float absolutePan = combinedPan + headPanOffset;
	float absoluteTilt = combinedTilt + headTiltOffset;

	#ifdef LOW_PASS_FILTER
		neck2dFilter->filter(absolutePan, absoluteTilt);
	#endif

	myNeck->rotateHead(absolutePan, absoluteTilt);

	//hasEyeBones = bodyConfiguration.getBool("Body.Joints.Eyes.HasEyeBones");
	hasEyeBones = getGlobalConfiguration()->getBool("Body_Joints_Eyes_HasEyeBones");
	if(hasEyeBones){
		myEyes->rotateEye(-absolutePan, -absoluteTilt);
	}
	//we save the absolutePan and absoluteTilt in currentPan and currentTilt to use them 
	//in updateEyeContactHeadPos
	currentPan=absolutePan;
	currentTilt=absoluteTilt;
}

/// Neck has a random movement component added to visual contact.
void EyeContactSpark::updateHeadRandomMovements(float *pPan, float *pTilt){
	static float r1 = 0, r2 = 0, r3 = 0;
	float rx, ry, rz;

	rx = m_pPerlinX->Get1D(r1);
	ry = m_pPerlinY->Get1D(r2);
	rz = m_pPerlinZ->Get1D(r3);

	// Velocidad de los movimientos aleatorios
	static const float inc = 0.0002f;

	r1 += inc;
	r2 += inc;
	r3 += inc;

	static const float random_head_movement_strenght = 0.5f;

	*pPan = random_head_movement_strenght * 180 * rx / 3.1416f;
	*pTilt = random_head_movement_strenght * 180 * ry / 3.1416f;
}

/// Updates the absolute head position by means of a regulator whose input
/// are the resulst of face detection.
void EyeContactSpark::updateEyeContactHeadPos(float *pPan, float *pTilt)
{
	//We get currentPan and currentTilt after a call to rotateEye(pan,tilt)
	if (!isFaceDetected) {
		*pPan = currentPan;
		*pTilt = currentTilt;
		return;
	}

	float target_pan, target_tilt;

	static const float eye_contact_magic_factor = 15;
	target_pan = faceTrackedHorizontalPos * eye_contact_magic_factor;
	target_tilt = -faceTrackedVerticalPos * eye_contact_magic_factor;

	// static const float eye_contact_regulator_k = 0.1f;
	static const float eye_contact_regulator_k = 0.5f;
	float delta_pan, delta_tilt;
	delta_pan = eye_contact_regulator_k * (target_pan - currentPan);
	delta_tilt = eye_contact_regulator_k * (target_tilt - currentTilt);

	*pPan = currentPan + delta_pan;
	*pTilt = currentTilt + delta_tilt;
}

//FrameEventSubscriber implementation
void EyeContactSpark::notifyFrameEvent()
{
	updateHeadIdleMovements();
}


