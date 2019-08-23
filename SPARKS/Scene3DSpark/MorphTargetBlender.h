/// @file MorphTargetBlender.h
/// @brief Class to control the weights of the Morph Targets of a character.

#ifndef __MORPH_TARGET_BLENDER_H
#define __MORPH_TARGET_BLENDER_H


#define MAX_MORPH_NODE_NAME 80


/// Class to control the weights of the Morph Targets of a character.

class MorphTargetBlender 
{
private:
	char morphNodeName[MAX_MORPH_NODE_NAME];

public:
	void init(char *morphNodeName);
	void setMorphTargetValue(char *morphTargetName, float v);
};


#endif
