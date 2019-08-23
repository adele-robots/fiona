#include "stdAfx.h"
#include "Logger.h"
#include "ErrorHandling.h"
#include "fileops.h"
#include "Component.h"
#include "IConcurrent.h"
#include "Configuration.h"
#include "ComponentSystem.h"
#include <fstream>
#include <iostream>
#include <sstream>

using namespace std;

map<string, string> ComponentSystem::typesConversion;
map<string, string> ComponentSystem::interfacesConversion;

void ComponentSystem::initializeComponents() {
	LoggerInfo("[FIONA-logger]Initializing components...");

	for (
		int i = 0; 
		i < interfaceImplementationsRepository.instances.componentInstances.size();
		i++
	)
	{
		LoggerInfo("[FIONA-logger]Initializing instance %s", interfaceImplementationsRepository.instances.componentInstances[i].first.c_str());
		interfaceImplementationsRepository.instances.componentInstances[i].second->component->init();
	}
}


void ComponentSystem::uninitializeComponents() {
	/* reverse iterator */
	map<ComponentInstances::TInstanceName, ComponentInstanceData *>::reverse_iterator it;

	LoggerInfo("Quitting components...");

	for (int i = interfaceImplementationsRepository.instances.componentInstances.size() - 1;
		i >= 0;
		i--
	)
	{
		LoggerInfo(
			"Uninittializing instance %s", 
			interfaceImplementationsRepository.instances.componentInstances[i].first.c_str()
		);
		interfaceImplementationsRepository.instances.componentInstances[i].second->component->quit();
	}
}



void ComponentSystem::connectComponentInstances() {
	for (
		int i = 0;
		i < interfaceImplementationsRepository.instances.componentInstances.size();
		i++
	)
	{
		interfaceImplementationsRepository.instances.componentInstances[i].second->component->initializeRequiredInterfaces();
	}
}



IApplication *ComponentSystem::getApplication() {
	pugi::xpath_node_set mainApplicationNodeSet;

	mainApplicationNodeSet = xmlParser.xpathQuery(
		"/ComponentNetwork/ApplicationDescription[@applicationComponent]"
	);

	if (mainApplicationNodeSet.empty()) {
		ERR("No /ComponentNetwork/ApplicationDescription elements with attribute 'applicationComponent' found "
			"in component network XML"
		);
	}

	if (mainApplicationNodeSet.size() > 1) {
		ERR("Only one /ComponentNetwork/ApplicationDescription element with attribute 'applicationComponent' allowed "
			"in component network XML"
		);
	}

	const char *appInstanceName = mainApplicationNodeSet.first().node().attribute("applicationComponent").value();

	Component *c = interfaceImplementationsRepository.
			instances.getInstance((char *)appInstanceName)->component;

	IApplication *app;
	app = dynamic_cast<IApplication *>(c);
	if (NULL == app) {
		ERR("Component instance %s does not implement IApplication", appInstanceName);
	}

	return app;
}


void ComponentSystem::startComponents() {
	for (
		int idx = 0;
		idx < interfaceImplementationsRepository.instances.componentInstances.size();
		idx++
	)
	{
		vector<string> &providedInterfaces = interfaceImplementationsRepository.instances.componentInstances[idx].second->componentDescription->providedInterfaces;
		for (int i = 0; i < providedInterfaces.size(); i++) {
			if (!strcmp("IConcurrent", providedInterfaces[i].c_str())) {
				IConcurrent *concurrentComponent;
				concurrentComponent = dynamic_cast<IConcurrent *>(interfaceImplementationsRepository.instances.componentInstances[idx].second->component);
				if (NULL == concurrentComponent) {
					ERR("Component instance %s does not implement IConcurrent",
						interfaceImplementationsRepository.instances.componentInstances[idx].first.c_str()
					);
				}
				LoggerInfo("Starting component instance: %s", interfaceImplementationsRepository.instances.componentInstances[idx].first.c_str());
				concurrentComponent->start();
				break;
			}
		}
	}
}


void ComponentSystem::stopComponents() {
	for (
		int idx = 0;
		idx < interfaceImplementationsRepository.instances.componentInstances.size();
		idx++
	)
	{
		vector<string> &providedInterfaces = interfaceImplementationsRepository.instances.componentInstances[idx].second->componentDescription->providedInterfaces;
		for (int i = 0; i < providedInterfaces.size(); i++) {
			if (!strcmp("IConcurrent", providedInterfaces[i].c_str())) {
				IConcurrent *concurrentComponent;
				concurrentComponent = dynamic_cast<IConcurrent *>(interfaceImplementationsRepository.instances.componentInstances[idx].second->component);
				if (NULL == concurrentComponent) {
					ERR("Component instance %s does not implement IConcurrent",
						interfaceImplementationsRepository.instances.componentInstances[idx].first.c_str()
					);
				}
				LoggerInfo("Stopping component instance: %s", interfaceImplementationsRepository.instances.componentInstances[idx].first.c_str());
				concurrentComponent->stop();
				break;
			}
		}
	}
}


void ComponentSystem::loadConfiguration(char * configFile) {
	pugi::xpath_node_set mainAppConfigFileNodeSet;

	mainAppConfigFileNodeSet = xmlParser.xpathQuery(
		"/ComponentNetwork/ApplicationDescription[@applicationConfig]"
	);

	if (mainAppConfigFileNodeSet.empty()) {
		ERR("No /ComponentNetwork/ApplicationDescription element with attribute 'applicationConfig' found "
			"in component network XML"
		);
	}

	if (mainAppConfigFileNodeSet.size() > 1) {
		ERR("Only one /ComponentNetwork/ApplicationDescription element with attribute 'applicationConfig' allowed "
			"in component network XML"
		);
	}

	// Get config file path realtive to app data environment variable
	const char *appConfigFile = mainAppConfigFileNodeSet.first().node().attribute("applicationConfig").value();

	// Get filesystem config file path
	char filesystemConfigFilePaht[1024];
	getApplicationDataFileFullPath((char *)appConfigFile, filesystemConfigFilePaht, 1024);

	// Load session configuration file
	libconfig::Config temporal;
	if(configFile) {
		globalConfiguration.loadConfiguration(configFile);
		LoggerInfo("%s loaded", configFile);
		try{
			for(int i = 0; i < globalConfiguration.configuration.getRoot().getLength(); i++) {
				saveSetting(temporal, globalConfiguration.configuration, globalConfiguration.configuration.getRoot()[i]);
			}
		} catch(libconfig::SettingNotFoundException &e){
			LoggerError("loadConfiguration:%s",e.what());
		} catch(libconfig::SettingTypeException &e){
			LoggerError("loadConfiguration:%s",e.what());
		}
	}

	// Load configuration file
	globalConfiguration.loadConfiguration(filesystemConfigFilePaht);
	LoggerInfo("%s loaded", filesystemConfigFilePaht);

	// Add session settings to configuration
	try {
		for(int i = 0; i < temporal.getRoot().getLength(); i++) {
			saveSetting(globalConfiguration.configuration, temporal, temporal.getRoot()[i]);
		}
	} catch(libconfig::SettingNotFoundException &e){
		LoggerError("loadConfiguration:%s",e.what());
	} catch(libconfig::SettingTypeException &e){
		LoggerError("loadConfiguration:%s",e.what());
	}
}

void ComponentSystem::loadTypesConversion() {
	ifstream is;
	string line, originalNameType, friendlyNameType;
	is.open ("/adele/dev/typesMap", ifstream::in);
	if(!is)
	{
		LoggerWarn("Could not find typesMap file, it must be placed in /adele/dev directory.");
		LoggerWarn("'Friendly names' will be disabled");
		return;
	}

	while(!is.eof()){
		getline(is, line);
		if(line.compare("")!=0){
			istringstream iss(line);
			iss >> friendlyNameType;
			iss >> originalNameType;
			typesConversion.insert(
					pair<FriendlyNameType, OriginalNameType>(friendlyNameType, originalNameType)
				);
		}
	}
}

void ComponentSystem::loadInterfacesConversion() {
	ifstream is;
	string line, originalNameType, friendlyNameType;
	is.open ("/adele/dev/interfacesMap", ifstream::in);
	if(!is)
	{
		LoggerWarn("Could not find interfacesMap file, it must be placed in /adele/dev directory.");
		LoggerWarn("'Friendly names' will be disabled");
		return;
	}

	while(!is.eof()){
		getline(is, line);
		if(line.compare("")!=0){
			istringstream iss(line);
			iss >> friendlyNameType;
			iss >> originalNameType;			
			interfacesConversion.insert(
					pair<FriendlyNameType, OriginalNameType>(friendlyNameType, originalNameType)
				);
		}
	}
}


void ComponentSystem::run(char *componentNetworkFileName, char * configFile) {

	// Use PugiXML to parse component XML.
	xmlParser.parseXmlFile(componentNetworkFileName);

	// Local global configuration (path included in XML)
	loadConfiguration(configFile);

	// Load the mapping names between friendly and real names
	loadTypesConversion();
	loadInterfacesConversion();

	// Interpret component XML 
	readComponentDescriptions();
	readComponentInstances();
	readComponentConnections();
	connectComponentInstances();

	
	/*** Run application ***/

	initializeComponents();

	startComponents();

	try {
		IApplication *application = getApplication();
		application->run();
	}
	catch(Exception &ex) {
		LoggerError(ex.msg);
	}
	catch(...) {
		LoggerError("Unknown exception");
	}

	stopComponents();

	uninitializeComponents();
}

void ComponentSystem::saveSetting(libconfig::Config & destConfig, libconfig::Config & sourceConfig, libconfig::Setting & setting) {
	const char * name = setting.getName();
	// If the param exists, delete it and store the newest(from session)
	if(destConfig.getRoot().exists(name)) {
		LoggerWarn("Setting \"%s\" already exists. Updating...", name);
		destConfig.getRoot().remove(name);
	}
	int type = setting.getType();
	libconfig::Setting & set = destConfig.getRoot().add(name, setting.getType());
	switch(type) {
		case 5:
			bool b;
			sourceConfig.lookupValue(name, b);
			set = b;
			return;
		case 3:
			float f;
			sourceConfig.lookupValue(name, f);
			set = f;
			return;
		case 1:
			int i;
			sourceConfig.lookupValue(name, i);
			set = i;
			return;
		case 2:
			long long i64;
			sourceConfig.lookupValue(name, i64);
			set = i64;
			return;
		case 4:
			string s;
			sourceConfig.lookupValue(name, s);
			set = s;
			return;
	}
}
