/// @file Scene.h
/// @brief Scene class definition.


#ifndef __SCENE_H
#define __SCENE_H

#include "Configuration.h"
#include "Horde3D.h"


#define HORDE_LOG_FILE_NAME "Horde3D_Log.html"


#define HORDE_ERR(...) {															\
	LoggerError("Horde3D Error. See %S for further details.", HORDE_LOG_FILE_NAME);	\
	ERR(__VA_ARGS__);																\
	h3dutDumpMessages();															\
}


class Skeleton;
class MorphTargetBlender;


/// \brief This class encapsulates the 3D scene rendering implementation.
///
/// Scenes are 3D, time variying geometries. To render them we use a 3D enging.
/// This class exploits the engine facilities. The main structure is the
/// Scene Tree. It has a root node. The rest of the nodes have transformations,
/// (traslations, rotations and scale). They are compounded as one traverses
/// the Tree. Other scene elements are meshes, textures, camara and lights.

class Scene 
{
public:
	void init(int width, int height);
	void quit(void);
	void createLights(void);

public:
	Scene(psisban::Config *bc, MorphTargetBlender *bl, Skeleton *sk) : 
		bodyConfiguration(bc),
		morphTargetBlender(bl),
		skeleton(sk)
	{}
	
	Skeleton *skeleton;
	void setWidth(int);
	void setHeight(int);

	H3DRes _fontMatRes;
	H3DRes _panelMatRes;
	H3DRes camaraNode;

	MorphTargetBlender *morphTargetBlender;

	H3DNode morphNode; 	// TODO: morph node se recalcula redundantemente en morphTargetBlender


	// OJO animacion
	bool isAnimationEnabled;
	H3DRes _animationRes;



public:
	psisban::Config *bodyConfiguration;

private:
	void addResources(void);
	void loadResourcesFormDisk(void);
	void addNodesToScene(void);
	void setInitialMorphs(void);

private:
	H3DRes pipelineResource;
	H3DRes lightMatRes;
	H3DRes sceneResource;
	int width;
	int height;

};



#endif
