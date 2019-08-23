#ifndef __LAUNCHER_H
#define __LAUNCHER_H


#include <vector>
#include <string>
#include <map>
#include "XMLParser.h"
#include "Configuration.h"
#include "IApplication.h"


class Component;



class ComponentDescription {
public:
	std::string id;
	std::string type;
	std::string plugin;
	std::vector <std::string> providedInterfaces;
	std::vector <std::string> requiredInterfaces;
	std::vector <std::string> parameterNames;
};



typedef struct {
	Component *component;
	ComponentDescription *componentDescription;
} ComponentInstanceData;

	
class ComponentInstances {
public:

	ComponentInstanceData *getInstance(char *instanceName);
	void addInstance(char *instanceName, ComponentInstanceData *);

private:
	typedef std::string TInstanceName;

	std::vector<std::pair<TInstanceName, ComponentInstanceData *> > componentInstances;

	friend class InterfaceImplementationsRepository;
	friend class ComponentSystem;
};	




class InterfaceImplementationsRepository {
public:
	Component *getInterfaceProvidingComponent(
		const char *requestingComponentInstanceName,
		const char *interfaceName
	);

	void addInterfaceImplementor(
		char *requestingComponentInstanceName,
		char *interfaceName,
		char *providingComponentInstanceName
	);

private:
	typedef std::string TRequestingComponentInstanceName;
	typedef std::string TInterfaceName;
	typedef std::string TProvidingComponentInstanceName;
	
	std::map<
		std::pair<TRequestingComponentInstanceName, TInterfaceName>, 
		TProvidingComponentInstanceName
	> interfaceImplementationsRepositoryData;

	ComponentInstances instances;

	friend class ComponentSystem;
};



class ComponentSystem
{
public:
	void run(char *componentNetworkFileName, char * configFile);


public:
	psisban::Config globalConfiguration;
	InterfaceImplementationsRepository interfaceImplementationsRepository;

private:
	void loadConfiguration(char * configFile);
	void loadTypesConversion();
	void loadInterfacesConversion();
	void saveSetting(libconfig::Config &, libconfig::Config &, libconfig::Setting &);

	void processComponentRequiredInterfaces(
		const char *componentId, 
		ComponentDescription *
	);

	void processComponentProvidedInterfaces(
		const char *componentId, 
		ComponentDescription *
	);

	void readComponentDescriptions();
	void readComponentInstances();
	void readComponentConnections();
	void connectComponentInstances();
	void startComponents();
	void stopComponents();

	void initializeComponents();
	void uninitializeComponents();

	ComponentDescription *getComponentDescription(char *componentId);
	IApplication *getApplication();

private:
	XMLParser xmlParser;
	typedef std::string TComponentId;
	typedef std::string OriginalNameType;
	typedef std::string FriendlyNameType;
	std::map<TComponentId, ComponentDescription *> componentDescriptions;

	static std::map<FriendlyNameType, OriginalNameType> typesConversion;
	static std::map<FriendlyNameType, OriginalNameType> interfacesConversion;
};


// Name and type of component factory method in plugins

#define CREATE_COMPONENT_FUNCTION_NAME "createComponent"

extern "C" typedef Component * (*CreateComponentFunction)(
	char *componentInstanceName, 
	char *componentType,
	ComponentSystem *
);




#endif
