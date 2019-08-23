/*
 * SubscriberSpark.cpp
 *
 *  Created on: 08/10/2012
 *      Author: guille
 */

/// @file SubscriberSpark.cpp
/// @brief SubscriberSpark class implementation.

#include "SubscriberSpark.h"


#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "SubscriberSpark<char*>")) {
			return new SubscriberSpark<char *>(componentInstanceName, componentSystem);
	}
	else if (!strcmp(componentType, "SubscriberSpark<OutgoingImage*>")) {
			return new SubscriberSpark<OutgoingImage *>(componentInstanceName, componentSystem);
	}
	else if (!strcmp(componentType, "SubscriberSpark<Image*>")) {
				return new SubscriberSpark<Image *>(componentInstanceName, componentSystem);
	}
	else if (!strcmp(componentType, "SubscriberSpark<Json::Value*>")) {
			return new SubscriberSpark<Json::Value *>(componentInstanceName, componentSystem);
	}
	/* We use type 'int' to instantiate SubscriberSpark when we want to connect any
	 * required interface, different from IFlow, to several implementations*/
	else if (!strcmp(componentType, "SubscriberSpark<int>")) {
		return new SubscriberSpark<int>(componentInstanceName, componentSystem);
	}
	else {
			return NULL;
		}
}
#endif

