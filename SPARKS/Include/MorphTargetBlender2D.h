#include "stdafx.h"
#include "MorphTargetBlender3D.h"
#ifndef __MORPH_TARGET_BLENDER2D_H
#define __MORPH_TARGET_BLENDER2D_H

#include "IMorphTargetBlender.h"
#include "my_application.h"

class MorphTargetBlender2D : public IMorphTargetBlender 
{
public:
	public:
	void copyAppSvg(my_application *app_svg_original);
	virtual void setMorphTargetValue(char *morphTargetName, float v)const;
private:
	my_application *app_svg;
};

#endif
