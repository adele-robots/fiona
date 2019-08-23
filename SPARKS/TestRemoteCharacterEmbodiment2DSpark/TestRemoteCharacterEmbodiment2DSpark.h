/// @file TestRemoteCharacterEmbodiment2DSpark.cpp
/// @brief Component TestRemoteCharacterEmbodiment2DSpark main class.

#ifndef __TestRemoteCharacterEmbodiment2DSpark_H
#define __TestRemoteCharacterEmbodiment2DSpark_H

#include "Component.h"


#include "IFaceExpression.h"
#include "IEyes.h"

#include "Configuration.h"
/// @brief This is the main class for component TestRemoteCharacterEmbodiment2DSpark.
///
/// Renders an avatar loaded from disk

class TestRemoteCharacterEmbodiment2DSpark :
	public Component
{
public:
	TestRemoteCharacterEmbodiment2DSpark(
		char *instanceName,
		ComponentSystem *componentSystem
	) : Component(instanceName, componentSystem)
	{}

private:
	IFaceExpression *myFaceExpression;
	IEyes *myEyes;

	void initializeRequiredInterfaces() {
		requestRequiredInterface<IFaceExpression>(&myFaceExpression);
		requestRequiredInterface<IEyes>(&myEyes);
	}


public:

	void init(void);
	void quit(void);

	psisban::Config bodyConfiguration;

	bool running;
};


#endif
