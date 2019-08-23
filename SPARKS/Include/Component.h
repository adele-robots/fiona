/// @file Component.cpp
/// @brief Component class implementation.


#ifndef __COMPONENT_H
#define __COMPONENT_H
#include <iostream>

#include <typeinfo> 
#include <map>
#include <string>
#include "ComponentSystem.h"
#include "Logger.h"
#include "ErrorHandling.h"
using namespace std;
#ifdef _WIN32
#else
#include <cxxabi.h>
#endif
// Convertir el nombre de tipo de "class NombreClase" a "NombreClase"
// TODO: add demangle call for g++


#ifdef _WIN32
#define TYPE_ID(x) (												\
	strncmp(typeid(x).name(), "class ", strlen("class")) ?			\
		typeid(x).name() :											\
		typeid(x).name() + strlen("class ")							\
)
#else

#define TYPE_ID(x)	abi::__cxa_demangle(typeid(x).name(),0,0,NULL)
#endif




/// All components derive from this class. They are forced to implement 
/// initializeRequiredInterfaces(). The component loader instantiates the components, 
/// creates the interface repository, and calls the initializeRequiredInterfaces()
/// of all instantiated components, thus filling the required interface pointers.


class Component {
public:
	Component(
		char *in, 
		ComponentSystem *cs
	) : 
	instanceName(in), componentSystem(cs) 
	{}
	

	virtual void init() = 0;
	virtual void quit() = 0;

	virtual void initializeRequiredInterfaces() = 0;


protected:
	// Component shared configuration
	psisban::Config *getGlobalConfiguration();

	// Per-component configuration
	psisban::Config *getComponentConfiguration();

	char *getInstanceName();


private:
	char *instanceName;
	ComponentSystem *componentSystem;
public:
	psisban::Config *componentConfiguration;


	// Unresolved externals if code in cpp due to uninstanced template.

public:
	template<class InterfaceType> 
	void requestRequiredInterface(
		InterfaceType **ifPlaceholder 
	)
	{
		Component *interfaceProviderComponent;

#ifdef _WIN32
		const char *interfaceName = TYPE_ID(InterfaceType);
//#error PORTABILITY issue, mangled/demangled TYPE_ID en GNU/GCC.
#else
		const char *interfaceName = TYPE_ID(InterfaceType);
#endif
		LoggerInfo("NOMBRE DE LAS INTERFACES");
		puts(interfaceName);

		interfaceProviderComponent = componentSystem->interfaceImplementationsRepository.getInterfaceProvidingComponent(
			instanceName,
			interfaceName
		);

		if (NULL == interfaceProviderComponent) {
			ERR("Implementation for interface %s required by component instance %s not found.",
				interfaceName,
				instanceName
			)
		}

		InterfaceType *myIntefacePointer;
		myIntefacePointer = dynamic_cast<InterfaceType *>(interfaceProviderComponent);

		if (myIntefacePointer == NULL) { 
			ERR("Component instance '%s' doesn't implement %s", 
				interfaceProviderComponent->instanceName,
				TYPE_ID(InterfaceType)
			);
		}
		if (!strcmp(interfaceProviderComponent->instanceName, "instanceBlankSpark")) {
			if(strstr(instanceName,"Subscriber")==NULL && strstr(instanceName,"Script")==NULL){
				LoggerWarn(
						"[FIONA-logger]DANGER! DANGER! Required interface %s of instance %s is not connected!! Auto-Providing an empty implementation. Be careful, it might crash",
						TYPE_ID(InterfaceType),
						instanceName
				);
			}
		}
		else{
			LoggerInfo(
					"Implementing interface %s of instance %s with component instance %s",
					TYPE_ID(InterfaceType),
					instanceName,
					interfaceProviderComponent->instanceName
			);
		}

		*ifPlaceholder = myIntefacePointer;
	}

	friend class ComponentSystem;
};



#endif


