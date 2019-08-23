/// @file HTMLRemoverSpark.h
/// @brief Component HTMLRemoverSpark main class.


#ifndef __HTMLRemoverSpark_H
#define __HTMLRemoverSpark_H


#include "Component.h"

// Required and Provided interface
#include "IFlow.h"

class HTMLRemoverSpark :
	public Component,
	//**Component class must extend provided interfaces**
	public IFlow<char*>
{
public:
		HTMLRemoverSpark(
				char *instanceName,
				ComponentSystem *cs
		) : Component(instanceName, cs)
		{}

//private:
	// Required interface initialization
	IFlow<char*> *myFlow;

	void initializeRequiredInterfaces() {
		requestRequiredInterface<IFlow<char *> >(&myFlow);
	}

public:
	//Mandatory
	void init(void);
	void quit(void);

	// Interface implementation declaration
	//IFlow implementation
	void processData(char *);

private:
	//Put class attributes here

private:
	string& stripHTMLTags(string& s);

public:


};

#endif
