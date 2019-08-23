/// @file MorphTargetBlender.h
/// @brief Class to control the weights of the Morph Targets of a character.

#include "stdAfx.h"
#include "MorphTargetBlender.h"
#include "Horde3D.h"
#include "Horde3DUtils.h"


#pragma comment (lib, "Horde3D.lib")
#pragma comment (lib, "Horde3DUtils.lib")

/// Initializes the Morph Target Blender. 
/// \param morphNodeName es el valor del atributo "name" del elemento "model" 
/// correspondiente al personaje en el fichero de descripci�n de escena
/// *.scene.xml.


void MorphTargetBlender::init(char *morphNodeName) {
	LoggerInfo("Initializing morph target blender");
	strncpy_s(this->morphNodeName, morphNodeName, MAX_MORPH_NODE_NAME);
}


/// @brief Da valor al peso de un Morph Target
///
/// @param morphTargetName nombre del Morph Target.
/// @v valor del peso del Morph Target.
/// Los nombres de los Morph Targets se encuentran en un comentario 
/// generado autom�ticamente por la herramienta de conversi�n
/// a continuaci�n del elemento "model" del fichero de descripci�n 
/// de escena *.scene.xml.
/// Los pesos de los morph targets son aditivos y no normalizados. Su suma
/// no tiene que valer 1.
/// Todos los pesos a 0 producen la expresi�n neutra.
/// Todos los pesos a 0 salvo uno puesto a 1 producen la expresi�n 
/// correspondiente al target.
/// El rango de pesos sugerido es entre 0 y 1, pero se admiten valores
/// fuera del rango.

void MorphTargetBlender::setMorphTargetValue(char *morphTargetName, float v) {

	int numFoundNodes;

	numFoundNodes = h3dFindNodes(H3DRootNode, morphNodeName, H3DNodeTypes::Undefined);
	if (numFoundNodes != 1) {
		ERR("Error finding node '%s'. Expeted 1, found %d", morphNodeName, numFoundNodes);
	}

	H3DNode morphNode = h3dGetNodeFindResult(0);
	if (morphNode == 0) ERR("h3dGetNodeFindResult");

	bool ok = h3dSetModelMorpher(morphNode, morphTargetName, v);
	if (!ok) ERR("morph target not found: %s", morphTargetName);
}


