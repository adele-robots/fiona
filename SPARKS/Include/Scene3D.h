#ifndef __SCENE3D_H
#define __SCENE3D_H

#include "IScene.h"
#include "Horde3D.h"

#if defined (HORDE_10B4)
#define HORDE_LOG_FILE_NAME "EngineLog.html"
#elif defined (HORDE_10B5)
#define HORDE_LOG_FILE_NAME "Horde3D_Log.html"
#else
#error No se ha definido la versión del motor 3D en la cabecera de configuración.
#endif


#define HORDE_ERR(...) {															\
	LoggerError("Horde3D Error. See %S for further details.", HORDE_LOG_FILE_NAME);	\
	ERR(__VA_ARGS__);																\
	h3dutDumpMessages();															\
}


//PABLO: Skeleton
class ISkeleton;

//PABLO: MTB MODIFIED
//class MorphTargetBlender;
class IMorphTargetBlender;

/// \brief This class encapsulates the 3D scene rendering implementation.
///
/// Scenes are 3D, time variying geometries. To render them we use a 3D enging.
/// This class exploits the engine facilities. The main structure is the
/// Scene Tree. It has a root node. The rest of the nodes have transformations,
/// (traslations, rotations and scale). They are compounded as one traverses
/// the Tree. Other scene elements are meshes, textures, camara and lights.

class Scene3D : public IScene 
{
//public:
//IScene(psisban::Config *bc, IMorphTargetBlender *bl, ISkeleton *sk)
public:
	virtual void init(int width, int height);
	virtual void quit(void)const;
	void createLights(void);

public:
	//PABLO: Skeleton
	//PABLO: MTB MODIFIED
	Scene3D(psisban::Config *bc, IMorphTargetBlender *bl, ISkeleton *sk) : 
		IScene(bc,bl,sk)
	{}
	//PABLO: Skeleton
//	ISkeleton *skeleton;
//	void setWidth(int);
//	void setHeight(int);
	H3DRes _fontMatRes;
	H3DRes _panelMatRes;

	H3DRes camaraNode;

	//PABLO: MTB MODIFIED
	//MorphTargetBlender *morphTargetBlender;
//	IMorphTargetBlender *morphTargetBlender;

	H3DNode morphNode; 	// morph node se recalcula redundantemente en morphTargetBlender


	// OJO animacion
	bool isAnimationEnabled;
	H3DRes _animationRes;



//public:
//	psisban::Config *bodyConfiguration;

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
