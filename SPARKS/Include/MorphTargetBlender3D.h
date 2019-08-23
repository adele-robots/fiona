#ifndef __MORPH_TARGET_BLENDER3D_H
#define __MORPH_TARGET_BLENDER3D_H

#include "IMorphTargetBlender.h"

class MorphTargetBlender3D : public IMorphTargetBlender 
{
public:
	virtual void setMorphTargetValue(char *morphTargetName, float v)const;
};


#endif
