/// @file MorphTargetBlender2D.h
/// @brief Class to control the weights of the Morph Targets of a character.

#ifndef __MORPH_TARGET_BLENDER2D_H
#define __MORPH_TARGET_BLENDER2D_H


#define MAX_MORPH_NODE_NAME 80

#include "SvgGeneral.h"

/// Class to control the weights of the Morph Targets of a character.

class MorphTargetBlender2D
{
private:
	char morphNodeName[MAX_MORPH_NODE_NAME];
	SvgGeneral *svgGeneral;

public:
	void init(char *morphNodeName);
	void setMorphTargetValue(char *morphTargetName, float v);
	MorphTargetBlender2D(SvgGeneral *svgGeneralOld);
};


#endif

