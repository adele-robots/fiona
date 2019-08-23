/// @file MorphTargetBlender2D.h
/// @brief Class to control the weights of the Morph Targets of a character.

#include "stdAfx.h"
#include "MorphTargetBlender2D.h"

void MorphTargetBlender2D::init(char *morphNodeName) {
	LoggerInfo("Initializing morph target blender 2D ");
	strncpy_s(this->morphNodeName, morphNodeName, MAX_MORPH_NODE_NAME);

}

void MorphTargetBlender2D::setMorphTargetValue(char *morphTargetName, float v) {
	//printf("MorphTargetBlender2D: setMorphTargetValue\n");
	for(int i=0;i<svgGeneral->numeroDeFicheros;i++)
		if(!strcmp(morphTargetName,svgGeneral->m_files_container[i].nombreMorphTarget)) {
			svgGeneral->m_alphas_container[i]=v;
			break;
	}
	svgGeneral->force_redraw();
}

MorphTargetBlender2D::MorphTargetBlender2D(SvgGeneral *svgGeneralOld){
	svgGeneral = new SvgGeneral(svgGeneralOld);
}
