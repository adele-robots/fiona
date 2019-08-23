/// @file TestNoVoiceSpark.h
/// @brief Component TestNoVoiceSpark main class.


#ifndef __TestNoVoiceSpark_H
#define __TestNoVoiceSpark_H


#include "Component.h"
//**To change for convenience**
//Example of provided interface
#include "IAudioQueue.h"
#include "IControlVoice.h"


/// @brief This is the main class for component TestNoVoiceSpark.
///
/// 

class TestNoVoiceSpark :
	public Component,
	//**Component class must extend provided interfaces**
	public IAudioQueue,
	public IControlVoice
{
public:
		TestNoVoiceSpark(
				char *instanceName,
				ComponentSystem *cs
		) : Component(instanceName, cs)
		{}

private:
	//**To change for convenience**
	//Example of a required interface initialization


	void initializeRequiredInterfaces() {	

	}

public:
	//Mandatory
	void init(void);
	void quit(void);
	
	//**To change for convenience**
	//Example of interface implementation declaration
	//IAudioQueue implementation
	int getStoredAudioSize();
	void dequeueAudioBuffer(char *buffer, int size);
	
	//IControlVoice
	void startSpeaking(void);
	void stopSpeaking(void);
	void startVoice(void);

private:
	//Put class attributes here
private:
	//Put class private methods here
	
};

#endif
