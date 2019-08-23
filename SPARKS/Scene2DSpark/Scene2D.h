/// @file Scene.h
/// @brief Scene class definition.

#ifndef __SCENE_H
#define __SCENE_H

#include "Configuration.h"
//#include "Window_Svg.h"
// Siguiendo criterio 3D
//#include "IScene.h"

//Cosa de vero por el tema de quitar el window
#include "my_application.h"


class Skeleton2D;
class MorphTargetBlender2D;

// Siguiendo criterio 3D
// :public IScene
class Scene2D {
public:
	void init(int width, int height);
	void quit(void);
	//void copyWinSvg(Window_Svg *winSvgCopy);
	//Window_Svg *winSvg;
public:
	psisban::Config *bodyConfiguration;
	MorphTargetBlender2D *morphTargetBlender;
	Skeleton2D *skeleton;

public:

	Scene2D(psisban::Config *bc, MorphTargetBlender2D *bl, Skeleton2D *sk)
	:	bodyConfiguration(bc),
		morphTargetBlender(bl),
		skeleton(sk)
	{}


private:
	void initSvgConfiguration(char*);

private:
	my_application *app_svg;
};

#endif
