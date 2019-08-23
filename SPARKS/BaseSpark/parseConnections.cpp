#include "stdAfx.h"
#include "Logger.h"
#include "ErrorHandling.h"
#include "fileops.h"
#include "ComponentSystem.h"


using namespace std;


void ComponentSystem::readComponentConnections() {

	pugi::xpath_node_set::const_iterator it;

	map<FriendlyNameType, OriginalNameType>::iterator iter;

	// Read <connect> elements

	pugi::xpath_node_set connectionsNodeSet;
	
	connectionsNodeSet = xmlParser.xpathQuery(
		"/ComponentNetwork/ApplicationDescription/InterfaceConnections/Connect"
	);

	for (
		it = connectionsNodeSet.begin(); 
		it != connectionsNodeSet.end(); 
		++it
	)
	{

		string interfaceToBeConnected;
		string xmlInterface = it->node().attribute("interface").value();

		//Here we change the possible interface's friendly name into the real name
		iter = interfacesConversion.find(xmlInterface);
		if (iter != interfacesConversion.end()) {
			interfaceToBeConnected = iter->second;
		}else{
			interfaceToBeConnected = xmlInterface;
		}

		/*interfaceImplementationsRepository.addInterfaceImplementor(
			(char *)it->node().attribute("requiredBy").value(),
			(char *)it->node().attribute("interface").value(),
			(char *)it->node().attribute("providedBy").value()
		);*/
		interfaceImplementationsRepository.addInterfaceImplementor(
				(char *)it->node().attribute("requiredBy").value(),
				(char *)interfaceToBeConnected.c_str(),
				(char *)it->node().attribute("providedBy").value()
		);
	}


	// Read <ConnectAll> elements

	pugi::xpath_node_set connectAllNodeSet;
	
	connectAllNodeSet = xmlParser.xpathQuery(
		"/ComponentNetwork/ApplicationDescription/InterfaceConnections/ConnectAll"
	);

	// Loop through ConnectAll elements
	for (
		it = connectAllNodeSet.begin(); 
		it != connectAllNodeSet.end(); 
		++it
	)
	{
		string interfaceToBeConnected;
		string xmlInterface = it->node().attribute("interface").value();
		//Here we change the possible interface's friendly name into the real name
		iter = interfacesConversion.find(xmlInterface);
		if (iter != interfacesConversion.end()) {
			interfaceToBeConnected = iter->second;
		}else{
			interfaceToBeConnected = xmlInterface;
		}

		//const char *interfaceName = it->node().attribute("interface").value();
		const char *providingInstance = it->node().attribute("providedBy").value();

		LoggerInfo(
			"ConnectAll components requesting %s to implementation provided by instance %s",
			interfaceToBeConnected.c_str(),
			providingInstance
		);

		// Loop through all instantiated components. 
		for (
			int idx = 0;
			idx < interfaceImplementationsRepository.instances.componentInstances.size();
			idx++
		)
		{

			// Loop through all required interfaces of the components
			vector<string> requiredInterfaces;
			requiredInterfaces = 
				interfaceImplementationsRepository.
					instances.
						componentInstances[idx].
							second->
								componentDescription->
									requiredInterfaces;
			
			for (int i = 0; i < requiredInterfaces.size(); i++) {
				if (!strcmp(
					interfaceToBeConnected.c_str(),
					requiredInterfaces[i].c_str()
					)
				)
				{
					LoggerInfo(
						"Connect all: instance %s implements "
						"interface %s required by instance %s",
						(char *)providingInstance,
						interfaceToBeConnected.c_str(),
						(char *)interfaceImplementationsRepository.instances.componentInstances[idx].first.c_str()
					);

					interfaceImplementationsRepository.addInterfaceImplementor(
						(char *)interfaceImplementationsRepository.instances.componentInstances[idx].first.c_str(),
						(char *)interfaceToBeConnected.c_str(),
						(char *)providingInstance
					);
				}
			}
		}
	}
}
