/// @file Updater1.cpp
/// @brief Updater1 class implementation.

// Third party libraries are linked explicitly once in the project.
// #pragma comment(lib, "thirdPartyLib.lib")

#include "stdAfx.h"
#include "Updater1.h"

#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "Updater1")) {
			return new Updater1(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif
/// Initializes Updater1 component.

void Updater1::init(void) {
}


/// Unitializes the Updater1 component.

void Updater1::quit(void) {
}

//IUpdateable implementation

void Updater1::update(void)
{
	myUpdateable1->update();
	//myUpdateable2->update();
}
