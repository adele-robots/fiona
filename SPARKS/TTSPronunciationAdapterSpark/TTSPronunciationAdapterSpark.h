/// @file TTSPronunciationAdapterSpark.h
/// @brief Component TTSPronunciationAdapterSpark main class.


#ifndef __TTSPronunciationAdapterSpark_H
#define __TTSPronunciationAdapterSpark_H


#include "Component.h"
#include "IFlow.h"


/// @brief This is the main class for component TTSPronunciationAdapterSpark.
///
/// 

class TTSPronunciationAdapterSpark :
	public Component,
	//**Component class must extend provided interfaces**
	public IFlow<char*>
{
public:
		TTSPronunciationAdapterSpark(
				char *instanceName,
				ComponentSystem *cs
		) : Component(instanceName, cs)
		{}

		virtual ~TTSPronunciationAdapterSpark(){};

private:
	//**To change for convenience**
	//Example of a required interface initialization
	IFlow<char*> *myFlow;

	void initializeRequiredInterfaces() {	
		requestRequiredInterface<IFlow<char*> >(&myFlow);
	}

public:
	//Mandatory
	void init(void);
	void quit(void);
	
	void processData(char * promtp);
	
private:
	//Put class attributes here
	map<string, string> charMap;
private:
	//Put class private methods here
	

};

#endif
