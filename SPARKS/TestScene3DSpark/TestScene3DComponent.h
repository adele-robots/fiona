/// @file TestScene3DComponent.cpp
/// @brief Component TestScene3DComponent main class.


#ifndef __TestScene3DComponent_H
#define __TestScene3DComponent_H

#include "Component.h"
#include "IApplication.h"

#include "IRenderizable.h"


#include "Configuration.h"
/// @brief This is the main class for component TestScene3DComponent.
///
/// Renders an avatar loaded from disk

class TestScene3DComponent :
	public Component,

	public IApplication
{
public:
	TestScene3DComponent(
		char *instanceName, 
		ComponentSystem *componentSystem
	) : Component(instanceName, componentSystem)
	{}

private:
	IRenderizable *myIRenderizable;

	void initializeRequiredInterfaces() {	
		requestRequiredInterface<IRenderizable>(&myIRenderizable);
	}


public:

	void init(void);
	void quit(void);

	void run(void);

	psisban::Config bodyConfiguration; 
};


#endif
