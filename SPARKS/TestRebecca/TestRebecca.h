/// @file TestRebecca.h
/// @brief Component TestRebecca main class.


#ifndef __TestRebecca_H
#define __TestRebecca_H

#include <iostream>
using namespace std;

#include "Component.h"
#include "IApplication.h"
#include "IVoice.h"



/// @brief This is the main class for component TestRebecca.
///
/// 

class TestRebecca :
	public Component,
	public IApplication,
	public IVoice
{
public:
		TestRebecca(
				char *instanceName,
				ComponentSystem *cs
		) : Component(instanceName, cs)
		{}

private:
	IVoice *myVoice;

	void initializeRequiredInterfaces() {	
		requestRequiredInterface<IVoice>(&myVoice);
	}

public:
	//Mandatory
	void init(void);
	void quit(void);
	
	//Implementation of IApplication
	void run(void);

	//Implementation of IVoice
	void sayThis(char *prompt);
	void waitEndOfSpeech(void);
	void stopSpeech(void);
	
private:
	//Put class attributes here
	bool running;
private:
	//Put class private methods here
	
};

#endif
