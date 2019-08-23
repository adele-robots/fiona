#include "stdAfx.h"
#include "Logger.h"
#include "ErrorHandling.h"
#include "fileops.h"
#include "Component.h"
#include "ComponentSystem.h"

#ifndef _WIN32
#include  <dlfcn.h>
#else
#endif


using namespace std;


ComponentInstanceData *ComponentInstances::getInstance(char *instanceName) {
	string instanceNameString = instanceName;

	for (int i = 0; i < componentInstances.size(); i++) {
		if (componentInstances[i].first == instanceName) return componentInstances[i].second;
	}

	ERR("Can't find component instance %s", instanceName);
}



void ComponentInstances::addInstance(
	char *instanceName, 
	ComponentInstanceData *componentInstanceData
) 
{
	string instanceNameString = instanceName;

	for (int i = 0; i < componentInstances.size(); i++) {
		if (componentInstances[i].first == instanceName) {
			ERR("[FIONA-logger]Duplicated instance %s", instanceName);
		}
	}

	componentInstances.push_back(
		pair<string, ComponentInstanceData *>(
			instanceNameString, 
			componentInstanceData
		)
	);
}



void ComponentSystem::readComponentInstances() {
	pugi::xpath_node_set componentInstancesNodeSet;
	
	componentInstancesNodeSet = xmlParser.xpathQuery(
		"/ComponentNetwork/ApplicationDescription/ComponentInstances/Instance"
	);

	pugi::xpath_node_set::const_iterator it;
	for (
		it = componentInstancesNodeSet.begin(); 
		it != componentInstancesNodeSet.end(); 
		++it
	)
	{
		const char *instanceName = it->node().attribute("instanceName").value();
		const char *componentId = it->node().attribute("componentID").value();
		
		LoggerInfo("Creating instance %s of component with id %s",instanceName,componentId);
		//printf("Creating instance %s of component with id %s",instanceName,componentId);

		ComponentDescription *componentDescription;
		componentDescription = getComponentDescription((char *)componentId);

		ComponentInstanceData *componentInstanceData;
		componentInstanceData = new ComponentInstanceData();



		componentInstanceData->componentDescription = componentDescription;

#ifndef _WIN32
//#error TODO: portability

		void *pluginHandle;

		pluginHandle = dlopen(NULL, RTLD_NOW|RTLD_GLOBAL);
		if (!pluginHandle)
		{
			ERR("Error loading plugin");
		}


		//pluginHandle = dlopen("/home/tico/Escritorio/COMPO/Programacion/Plugin/Debug/libPlugin.so",RTLD_LAZY);
		pluginHandle = dlopen(componentDescription->plugin.c_str(), RTLD_LAZY);


		if (!pluginHandle)
		{
			ERR("Error loading plugin: %s %s", componentDescription->plugin.c_str(),dlerror());
		}

		void *functionPointer;
		functionPointer = dlsym(pluginHandle, CREATE_COMPONENT_FUNCTION_NAME);
		if (!functionPointer)
		{
			ERR(
				"Plugin '%s' doesn't contain factory method '%s'",
				componentDescription->plugin.c_str(),
				CREATE_COMPONENT_FUNCTION_NAME
				);
		}


#else

		HMODULE pluginHandle = ::LoadLibrary(componentDescription->plugin.c_str());
		if (!pluginHandle) {
			ERR("Error loading plugin: %s", componentDescription->plugin.c_str());
		}

		PROC functionPointer;
		functionPointer = GetProcAddress(pluginHandle, CREATE_COMPONENT_FUNCTION_NAME);
		if (!functionPointer) {
		ERR(
			"Plugin '%s' doesn't contain factory method '%s'",
			 componentDescription->plugin.c_str(),
			CREATE_COMPONENT_FUNCTION_NAME
			);
		}
#endif
		CreateComponentFunction createComponentFunction;
		createComponentFunction = (CreateComponentFunction)functionPointer;

		Component *createdComponent = createComponentFunction(
			(char *)instanceName,
			(char *)componentDescription->type.c_str(),
			this
		);

		if (createdComponent == NULL) {
			ERR(
				"Error creating component of type %s in plugin %s",
				componentDescription->type.c_str(),
				componentDescription->plugin.c_str()
			);
		}

		// Load per-component configuration if present

		// Configuration file path relative to application data folder
		const char *configFile = it->node().attribute("configuration").value();

		/* Check if we are using friendly names, if so, file '.ini' configuration will
		   have the wrong name (e.g /DialogManager.ini instead /RebeccaAIMLSpark.ini)
		   and it is necessary to compose it*/
		map<FriendlyNameType, OriginalNameType>::iterator iter;
		string componentIdString = componentId;
		componentIdString.erase(0,2);

		if (strlen(configFile) == 0) {
			createdComponent->componentConfiguration = NULL;
		}
		else {
			char filesystemConfigurationFilePath[1024];
			iter = typesConversion.find(componentIdString);
			if (iter != typesConversion.end()) {
				// Compose the correct configuration '.ini' filename
				string configFileString = "/"+iter->second+".ini";
				getApplicationDataFileFullPath((char *)configFileString.c_str(), filesystemConfigurationFilePath, 1024);
				createdComponent->componentConfiguration = new psisban::Config();
				createdComponent->componentConfiguration->loadConfiguration(filesystemConfigurationFilePath);
			}else{
				getApplicationDataFileFullPath((char *)configFile, filesystemConfigurationFilePath, 1024);
				createdComponent->componentConfiguration = new psisban::Config();
				createdComponent->componentConfiguration->loadConfiguration(filesystemConfigurationFilePath);
			}
		}

		componentInstanceData->component = createdComponent;

		interfaceImplementationsRepository.instances.addInstance(
			(char *)instanceName, 
			componentInstanceData
		);
	}
}
