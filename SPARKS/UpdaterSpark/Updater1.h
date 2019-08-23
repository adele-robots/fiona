/// @file Updater1.cpp
/// @brief Component Updater1 main class.


#ifndef __Updater1_H
#define __Updater1_H

#include "Component.h"
#include "IUpdateable.h"

#include "IUpdateable1.h"
//#include "IUpdateable2.h"

/// @brief This is the main class for component Updater1.
///
/// This component is the same as UpdaterComponent but with only one interface

class Updater1 :
	public Component,

	public IUpdateable
{
public:
	Updater1(
		char *instanceName, 
		ComponentSystem *cs
	) : Component(instanceName, cs)
	{}

private:
	IUpdateable1 *myUpdateable1;
	//IUpdateable2 *myUpdateable2;

	void initializeRequiredInterfaces() {	
		requestRequiredInterface<IUpdateable1>(&myUpdateable1);
		//requestRequiredInterface<IUpdateable2>(&myUpdateable2);
	}


public:
	

	void init(void);
	void quit(void);

	//IUpdateable implementation
	void update(void);

};


#endif
