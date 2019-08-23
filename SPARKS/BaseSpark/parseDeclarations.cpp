#include "stdAfx.h"
#include "Logger.h"
#include "ErrorHandling.h"
#include "ComponentSystem.h"
#include <iostream>

using namespace std;

ComponentDescription *ComponentSystem::getComponentDescription(char *componentId) {
	map<TComponentId, ComponentDescription *>::iterator it;
	string componentIdString = componentId;
	it = componentDescriptions.find(componentIdString);
	if (it == componentDescriptions.end()) {
		ERR("Can't find component description for component with id '%s'", componentId);
	}
	return it->second;
}

void ComponentSystem::processComponentProvidedInterfaces(
	const char *componentId, 
	ComponentDescription *componentDescription
) 
{
	char requiredInterfaceQuery[256];
	_snprintf(
		requiredInterfaceQuery, 
		256, 
		"/ComponentNetwork/ComponentDeclarations/Component[@id='%s']/ProvidedInterfaces/Interface",
		componentId
	);

	pugi::xpath_node_set providedInterfacesNodeSet;

	map<FriendlyNameType, OriginalNameType>::iterator iter;

	providedInterfacesNodeSet = xmlParser.xpathQuery(requiredInterfaceQuery);

	pugi::xpath_node_set::const_iterator it;
	for (it = providedInterfacesNodeSet.begin(); it != providedInterfacesNodeSet.end(); ++it)
	{
		LoggerInfo("Provided Interface: %s", it->node().attribute("name").value());

		string providedInterfaceString;
		string xmlProvidedInterface = it->node().attribute("name").value();

		//Here we change the possible interface's friendly name into the real name
		iter = interfacesConversion.find(xmlProvidedInterface);
		if (iter != interfacesConversion.end()) {
			providedInterfaceString = iter->second;
		}else{
			providedInterfaceString = xmlProvidedInterface;
		}
		componentDescription->providedInterfaces.push_back(providedInterfaceString);
	}
}


void ComponentSystem::processComponentRequiredInterfaces(
	const char *componentId, 
	ComponentDescription *componentDescription
) 
{
	char requiredInterfaceQuery[256];
	_snprintf(
		requiredInterfaceQuery, 
		256, 
		"/ComponentNetwork/ComponentDeclarations/Component[@id='%s']/RequiredInterfaces/Interface",
		componentId
	);

	pugi::xpath_node_set requiredInterfacesNodeSet;

	map<FriendlyNameType, OriginalNameType>::iterator iter;

	requiredInterfacesNodeSet = xmlParser.xpathQuery(requiredInterfaceQuery);

	pugi::xpath_node_set::const_iterator it;
	for (it = requiredInterfacesNodeSet.begin(); it != requiredInterfacesNodeSet.end(); ++it)
	{
		LoggerInfo("Required Interface: %s", it->node().attribute("name").value());
		
		string requiredInterfaceString;
		string xmlRequiredInterface = it->node().attribute("name").value();

		//Here we change the possible interface's friendly name into the real name
		iter = interfacesConversion.find(xmlRequiredInterface);
		if (iter != interfacesConversion.end()) {
			requiredInterfaceString = iter->second;
		}else{
			requiredInterfaceString = xmlRequiredInterface;
		}
		componentDescription->requiredInterfaces.push_back(requiredInterfaceString);
	}
}


void ComponentSystem::readComponentDescriptions() {
	pugi::xpath_node_set componentDescriptionsNodeSet;
	
	componentDescriptionsNodeSet = xmlParser.xpathQuery(
		"/ComponentNetwork/ComponentDeclarations/Component"
	);

	map<FriendlyNameType, OriginalNameType>::iterator iter;

	pugi::xpath_node_set::const_iterator it;
	for (
		it = componentDescriptionsNodeSet.begin(); 
		it != componentDescriptionsNodeSet.end(); 
		++it
	)
	{
		const char *componentId = it->node().attribute("id").value();

		LoggerInfo("[FIONA-logger]Component id: %s", componentId);

		ComponentDescription *componentDescription;
		componentDescription = new ComponentDescription();

		componentDescription->id = componentId;

		string xmlSparkType = it->node().attribute("type").value();

		//Here we change the possible type friendly name into the real name
		iter = typesConversion.find(xmlSparkType);
		if (iter != typesConversion.end()) {
			componentDescription->type = iter->second;
			// remove the type (e.g <char*>) of templated sparks
			size_t found = iter->second.find("<");
			if (string::npos != found)
			{
				componentDescription->plugin = "lib"+iter->second.substr (0,found)+".so";
			}else{
				componentDescription->plugin = "lib"+iter->second+".so";
			}
		}else{
			componentDescription->type = xmlSparkType;
			componentDescription->plugin = it->node().attribute("plugin").value();
		}

		processComponentProvidedInterfaces(
			componentId, 
			componentDescription
		);

		processComponentRequiredInterfaces(
			componentId, 
			componentDescription
		);

		componentDescriptions.insert(
			pair<string, ComponentDescription *>(componentId, componentDescription)
		);

	}
}
