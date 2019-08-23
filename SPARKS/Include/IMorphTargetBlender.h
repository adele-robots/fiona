#ifndef __IMORPH_TARGET_BLENDER_H
#define __IMORPH_TARGET_BLENDER_H


#define MAX_MORPH_NODE_NAME 80


class IMorphTargetBlender 
{
//PABLO: MTB MODIFIED
//private:
protected:
	char morphNodeName[MAX_MORPH_NODE_NAME];

public:
	void init(char *morphNodeName);
	//PABLO: MTB MODIFIED
	virtual void setMorphTargetValue(char *morphTargetName, float v)const=0;
};


#endif
