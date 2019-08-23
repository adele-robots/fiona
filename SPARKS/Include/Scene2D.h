#ifndef __SCENE2D_H
#define __SCENE2D_H

#include "Configuration.h"
#include "Horde3D.h"
#include "Configuration.h"

#include "Window_Svg.h"


//PABLO: Skeleton
class ISkeleton;

//PABLO: MTB MODIFIED
//class MorphTargetBlender;
class IMorphTargetBlender;

class Scene2D : public IScene 
{
public:
	virtual void init(int width, int height);
	virtual void quit(void)const;
	void copyWinSvg(Window_Svg *winSvgCopy);
	Window_Svg *winSvg;

public:
	//PABLO: Skeleton
	//PABLO: MTB MODIFIED
	Scene2D(psisban::Config *bc, IMorphTargetBlender *bl, ISkeleton *sk) : 
		IScene(bc,bl,sk)
	{}
private:
	void initSvgConfiguration(char*);
};



#endif
