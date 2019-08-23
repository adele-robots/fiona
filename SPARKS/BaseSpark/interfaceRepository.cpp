#include "stdAfx.h"
#include "ErrorHandling.h"
#include "ComponentSystem.h"
#include <Logger.h>


using namespace std;


Component *InterfaceImplementationsRepository::getInterfaceProvidingComponent(
	const char *requestingComponentInstanceName,
	const char *interfaceName
)
{
	string requestingComponentInstanceNameString = requestingComponentInstanceName;
	string interfaceNameString = interfaceName;
	
	// build the pair which will be the key for search within the map
	pair<TRequestingComponentInstanceName, TInterfaceName> searchKey(
		requestingComponentInstanceNameString,
		interfaceNameString
	);

	map<
		pair<TRequestingComponentInstanceName, TInterfaceName>, 
		TProvidingComponentInstanceName
	>::iterator it;

	// search if there is a ProvidingComponent which implements InterfaceName for RequestingComponent
	it = interfaceImplementationsRepositoryData.find(searchKey);
	// find returns end() if not found.
	if (it == interfaceImplementationsRepositoryData.end()) {
		if(strstr(requestingComponentInstanceName,"Subscriber")==NULL && strstr(requestingComponentInstanceName,"Script")==NULL){
			LoggerWarn("[FIONA-logger]Component instance '%s' can't get an implementation for required interface %s",
					requestingComponentInstanceName,
					interfaceName);
		}
		/*ERR("Component instance '%s' can't get an implementation for required interface %s",
			requestingComponentInstanceName,
			interfaceName
		);*/
		// Need to connect interface to an instance of BlanSpark, to provide an empty implementation
		// and this way, avoid the avatar to crash if there is a not-connected required interface
		TProvidingComponentInstanceName providingInstanceName = "instanceBlankSpark";
		Component *providingComponent = instances.getInstance(
				(char *)providingInstanceName.c_str()
		)->component;

		return providingComponent;
	}

	TProvidingComponentInstanceName providingInstanceName;

	providingInstanceName = it->second;

	Component *providingComponent = instances.getInstance(
		(char *)providingInstanceName.c_str()
	)->component;

	return providingComponent;
}



void InterfaceImplementationsRepository::addInterfaceImplementor(
	char *requestingComponentInstanceName,
	char *interfaceName,
	char *providingComponentInstanceName
)
{
	string requestingComponentInstanceNameString = requestingComponentInstanceName;
	string providingComponentInstanceNameString = providingComponentInstanceName;
	string interfaceNameString = interfaceName;
	
	pair<TRequestingComponentInstanceName, TInterfaceName> searchKey(
		requestingComponentInstanceNameString,
		interfaceNameString
	);

	map<
		pair<TRequestingComponentInstanceName, TInterfaceName>, 
		TProvidingComponentInstanceName
	>::iterator it;

	it = interfaceImplementationsRepositoryData.find(searchKey);
	if (it != interfaceImplementationsRepositoryData.end()) {
		ERR("Component instance %s is a redundant implementation " 
			"for interface %s required by component %s. "
			"Interface already implemented by instance %s",
			providingComponentInstanceName,
			interfaceName,
			requestingComponentInstanceName,
			it->second.c_str()
		);
	}

	interfaceImplementationsRepositoryData[
		pair<TRequestingComponentInstanceName, TInterfaceName>(
			requestingComponentInstanceName, 
			interfaceName
		)
	] = providingComponentInstanceName;

}
