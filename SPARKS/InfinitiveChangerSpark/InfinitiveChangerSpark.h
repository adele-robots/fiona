/// @file InfinitiveChangerSpark.h
/// @brief Component InfinitiveChangerSpark main class.


#ifndef __InfinitiveChangerSpark_H
#define __InfinitiveChangerSpark_H


#include "Component.h"

//Example of required interface
#include "IFlow.h"
#include "freeling.h"
#include "freeling/morfo/traces.h"
	using namespace std;
	using namespace freeling;



/// @brief This is the main class for component InfinitiveChangerSpark.
///
/// 

class InfinitiveChangerSpark :
	public Component,
	//**Component class must extend provided interfaces**
	public IFlow<char*>
{
public:
		InfinitiveChangerSpark(
				char *instanceName,
				ComponentSystem *cs
		) : Component(instanceName, cs)
		{}

private:
	//**To change for convenience**
	//Example of a required interface initialization
	IFlow<char*> *myFlow;

	void initializeRequiredInterfaces() {	
		requestRequiredInterface<IFlow<char*> >(&myFlow);
	}

public:
	//Mandatory
	void init(void);
	void quit(void);
	
	//Example of interface implementation declaration
	//IFlow implementation
	void processData(char*);

	
private:
	//Put class attributes here
	string freelingPath;
	tokenizer * tk;
	splitter * sp;
	maco * morfo;
	alternatives * alts_ort;
	alternatives * alts_phon;

	bool ChangeInfinitive;
	bool ChangeSingular;
	bool ChangeUnknown;

private:
	
};

#endif
