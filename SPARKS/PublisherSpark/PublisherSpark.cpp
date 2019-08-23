/*
 * PublisherSpark.cpp
 *
 *  Created on: 08/10/2012
 *      Author: guille
 */

/// @file PublisherSpark.cpp
/// @brief PublisherSpark class implementation.


#include "PublisherSpark.h"


#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "PublisherSpark<char*>")) {
			return new PublisherSpark<char *>(componentInstanceName, componentSystem);
	}
	else if (!strcmp(componentType, "PublisherSpark<OutgoingImage*>")) {
		return new PublisherSpark<OutgoingImage *>(componentInstanceName, componentSystem);
	}
	else if (!strcmp(componentType, "PublisherSpark<Image*>")) {
			return new PublisherSpark<Image *>(componentInstanceName, componentSystem);
	}
	else if (!strcmp(componentType, "PublisherSpark<Json::Value*>")) {
			return new PublisherSpark<Json::Value *>(componentInstanceName, componentSystem);
	}
	/* We use type 'int' to instantiate PublisherSpark when we want to connect any
	 * required interface, different from IFlow, to several implementations*/
	else if (!strcmp(componentType, "PublisherSpark<int>")) {
		return new PublisherSpark<int>(componentInstanceName, componentSystem);
	}
	else {
			return NULL;
		}
}
#endif

