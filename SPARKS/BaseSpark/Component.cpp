#include "stdAfx.h"
#include "Component.h"

using namespace std;


psisban::Config *Component::getGlobalConfiguration() {
	return &(componentSystem->globalConfiguration);
}


psisban::Config *Component::getComponentConfiguration() {
	if (NULL == componentConfiguration) {
		LoggerError("[FIONA-logger]Component instance %s lacks configuration", getInstanceName());
		ERR("Component instance %s lacks configuration", getInstanceName());
	}
	return componentConfiguration;
}


char *Component::getInstanceName() {
	return instanceName;
}
