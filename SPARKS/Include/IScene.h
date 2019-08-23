#ifndef __ISCENE_H
#define __ISCENE_H

#include "Configuration.h"



//PABLO: Skeleton
class ISkeleton;

//PABLO: MTB MODIFIED
//class MorphTargetBlender;
class IMorphTargetBlender;

class IScene 
{
public:
	virtual void init(int width, int height);
	virtual void quit(void)const=0;

public:
	//PABLO: Skeleton
	//PABLO: MTB MODIFIED
	IScene(psisban::Config *bc, IMorphTargetBlender *bl, ISkeleton *sk) : 
		bodyConfiguration(bc),
		morphTargetBlender(bl),
		skeleton(sk)
	{}
	//PABLO: Skeleton
	ISkeleton *skeleton;
	//void setWidth(int);
	//void setHeight(int);
	//H3DRes _fontMatRes;
	//H3DRes _panelMatRes;

	//H3DRes camaraNode;

	//PABLO: MTB MODIFIED
	//MorphTargetBlender *morphTargetBlender;
	IMorphTargetBlender *morphTargetBlender;

	//H3DNode morphNode; 	// morph node se recalcula redundantemente en morphTargetBlender


	// OJO animacion
	//bool isAnimationEnabled;
	//H3DRes _animationRes;



public:
	psisban::Config *bodyConfiguration;
//PABLO: Scene
//private:
protected:
	//void addResources(void);
	//void loadResourcesFormDisk(void);
	//void addNodesToScene(void);
	//void setInitialMorphs(void);
//PABLO: Scene
//private:
protected:
	//H3DRes pipelineResource;
	//H3DRes lightMatRes;
	//H3DRes sceneResource;
	//int width;
	//int height;

};



#endif
