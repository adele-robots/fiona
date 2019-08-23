/// @file PhotoSpark.h
/// @brief Component PhotoSpark main class.


#ifndef __PhotoSpark_H
#define __PhotoSpark_H


#include "Component.h"
#include "IFlow.h"


/// @brief This is the main class for component PhotoSpark.
///
/// 

class PhotoSpark :
	public Component,
	public IFlow<char *>,
	public IFlow<Image *>
{
public:
		PhotoSpark(
				char *instanceName,
				ComponentSystem *cs
		) : Component(instanceName, cs)
		{}

private:
		IFlow<char *> *myCharFlow;
		IFlow<Image *> *myImageFlow;

	void initializeRequiredInterfaces() {
	}

public:
	//Mandatory
	void init(void);
	void quit(void);
	
	void processData(char * prompt);
	void processData(Image * image);

	
private:
	//Put class attributes here
	bool takePhoto;
	std::string photoText;
	std::string photoPath;
	int quality;

private:
	//Put class private methods here
	bool isJPG();
	bool isPNG();
	
};

#endif
