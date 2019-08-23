/// @file Scene.cpp
/// @brief Scene class implementation.

#include "stdAfx.h"
#include <math.h>
#include <vector>
#include "Horde3D.h"
#include "Horde3DUtils.h"
#include "Skeleton.h"
#include "MorphTargetBlender.h"
#include "Scene.h"



#pragma comment (lib, "Horde3D.lib")
#pragma comment (lib, "Horde3DUtils.lib")
#pragma comment (lib, "opengl32.lib")


using namespace std;



void Scene::addResources(void) {
	char pipelineXML[80];
	mainConfig.getString("Scene.PipelineXML", pipelineXML, 80);
	pipelineResource = h3dAddResource( H3DResTypes::Pipeline, pipelineXML, 0 );


	h3dResizePipelineBuffers(pipelineResource, width, height);

	char lightMaterialXML[80];
	mainConfig.getString("Scene.LightMaterial", lightMaterialXML, 80);
	lightMatRes = h3dAddResource( H3DResTypes::Material, lightMaterialXML, 0 );

	char sceneGraphXml[80];
	bodyConfiguration->getString("SceneXML", sceneGraphXml, 80);
	sceneResource = h3dAddResource( H3DResTypes::SceneGraph, sceneGraphXml, 0 );
	if (sceneResource == 0) HORDE_ERR("Error adding scene resources");


	// Recursos para la sobreimpresi�n de texto
	_fontMatRes = h3dAddResource( H3DResTypes::Material, "overlays/font.material.xml", 0 );
	if (_fontMatRes == 0) HORDE_ERR("Error loading font resource");

	_panelMatRes = h3dAddResource( H3DResTypes::Material, "overlays/panel.material.xml", 0 );
	if (_panelMatRes == 0) HORDE_ERR("Error loading panel resource");
}



void Scene::loadResourcesFormDisk(void) {
	char commonContentPath[1024];
	mainConfig.getFilePath("Scene.CommonContentPath", commonContentPath, 1024);
	
	char sceneContentPath[1024];
	bodyConfiguration->getFilePath("SceneContentPath", sceneContentPath, 1024);

	char contentPaths[2050];
	_snprintf(contentPaths, 2050, "%s|%s", commonContentPath, sceneContentPath);

	LoggerInfo("Loading 3D resources from disk...");
	bool ok = h3dutLoadResourcesFromDisk(contentPaths);
	
	if (!ok) HORDE_ERR("Error loading 3D resources from disk, at path %s", contentPaths);
	LoggerInfo("...Done.");
}




void Scene::createLights(void) {
	int lightNumber = bodyConfiguration->getLength("Scene.Lights");

	for (int lightIndex = 0; lightIndex < lightNumber; lightIndex++) {
		float x, y, z;
		float lightRadius;
		
		char setting[80];

		_snprintf(setting, 80, "Scene.Lights.[%d].X", lightIndex);
		x = bodyConfiguration->getFloat(setting);

		_snprintf(setting, 80, "Scene.Lights.[%d].Y", lightIndex);
		y = bodyConfiguration->getFloat(setting);

		_snprintf(setting, 80, "Scene.Lights.[%d].Z", lightIndex);
		z = bodyConfiguration->getFloat(setting);

		_snprintf(setting, 80, "Scene.Lights.[%d].LightRadius", lightIndex);
		lightRadius = bodyConfiguration->getFloat(setting);

		// Light
		char lightName[80];
		_snprintf(lightName, 80, "light%d", lightIndex);
		H3DNode light = h3dAddLightNode( H3DRootNode, lightName, lightMatRes, "LIGHTING", "SHADOWMAP" );
		if (light == 0) HORDE_ERR("Error en h3dAddLightNode()");


		h3dSetNodeParamF( light, H3DLight::RadiusF, 0, lightRadius );
		h3dSetNodeParamF( light, H3DLight::FovF, 0, 360 );
		h3dSetNodeParamI( light, H3DLight::ShadowMapCountI, 0 );
		h3dSetNodeParamF( light, H3DLight::ShadowSplitLambdaF, 0, 0.5f );
		h3dSetNodeParamF( light, H3DLight::ShadowMapBiasF, 0, 0.005f );
		h3dSetNodeParamF( light, H3DLight::ColorF3, 0, 1.0f );
		h3dSetNodeParamF( light, H3DLight::ColorF3, 1, 1.0f );
		h3dSetNodeParamF( light, H3DLight::ColorF3, 2, 1.0f );


		h3dSetNodeTransform( light, x, y, z, 0, 0, 0, 1, 1, 1 );
	}
}



void Scene::addNodesToScene(void) {

	// Scene tree
	H3DNode _node = h3dAddNodes(H3DRootNode, sceneResource);
	if (_node == 0) HORDE_ERR("Error adding scene tree resource");

	// Lights
	createLights();
	
	// Camara
	const char *camaraName = "Camara";

	camaraNode = h3dAddCameraNode( H3DRootNode, camaraName, pipelineResource );
	if (camaraNode == 0) HORDE_ERR("Error adding camara node");

	h3dSetNodeParamI(camaraNode, H3DCamera::ViewportXI, 0 );
	h3dSetNodeParamI(camaraNode, H3DCamera::ViewportYI, 0 );
	h3dSetNodeParamI(camaraNode, H3DCamera::ViewportWidthI, width );
	h3dSetNodeParamI(camaraNode, H3DCamera::ViewportHeightI, height );


	bool isOrtho = bodyConfiguration->getBool("Scene.Camara.IsOrtho");
	if (isOrtho) {
		h3dSetNodeParamI(camaraNode, H3DCamera::OrthoI, 1);

		h3dSetupCameraView(
			camaraNode, 
			bodyConfiguration->getFloat("Scene.Camara.CamaraParameters.VisionAngle"),
			(float)width / (float)height,
			bodyConfiguration->getFloat("Scene.Camara.CamaraParameters.NearClippingPlane"),
			bodyConfiguration->getFloat("Scene.Camara.CamaraParameters.FarClippingPlane")
		);
	}
	else {
		h3dSetNodeParamI(camaraNode, H3DCamera::OrthoI, 0);
	}




	h3dSetNodeTransform(
		camaraNode, 
		bodyConfiguration->getFloat("Scene.Camara.Position.X"), 
		bodyConfiguration->getFloat("Scene.Camara.Position.Y"), 
		bodyConfiguration->getFloat("Scene.Camara.Position.Z"),
		0,0,0,	/* rotation */
		1,1,1	/* scale */
	);
}



void Scene::init(int width, int height) {
	this->width = width;
	this->height = height;

	LoggerInfo("Initializing Scene");


	h3dSetOption( H3DOptions::LoadTextures, 1 );
	h3dSetOption( H3DOptions::TexCompression, 0 );
	h3dSetOption( H3DOptions::MaxAnisotropy, 4 );
	h3dSetOption( H3DOptions::ShadowMapSize, 2048 );
	h3dSetOption( H3DOptions::FastAnimation, 1 );

	// OJO
	// h3dSetOption( H3DOptions::WireframeMode, 1 );

	int antialisingSampleCount = mainConfig.getInt("Scene.AntialisingSampleCount");
	h3dSetOption( H3DOptions::SampleCount, (float)antialisingSampleCount );

	addResources();
	//addAnimationResources();
	loadResourcesFormDisk();
	addNodesToScene();


	if (bodyConfiguration->getBool("Body.Joints.ArmsNeedDowning")) skeleton->armsDown();


	// OJO
	/* 
	LoggerInfo("Node list:");
	int count = h3dFindNodes(H3DRootNode, "", H3DNodeTypes::Undefined);
	for (int i = 0; i < count; i++) {
		H3DNode node = h3dGetNodeFindResult(i);
		LoggerInfo("Node %d: %s", i, h3dGetNodeParamStr(node, H3DNodeParams::NameStr));
	}
	*/


	// Morphs iniciales
	setInitialMorphs();

	// Incializar esqueleto
	skeleton->init();

	// Morph node
	char morphNodeName[80];
	bodyConfiguration->getString("Body.MorphNodeName", morphNodeName, 80);

	int numFoundNodes;
	numFoundNodes = h3dFindNodes(H3DRootNode, morphNodeName, H3DNodeTypes::Undefined);
	if (numFoundNodes != 1) {
		ERR("Error finding node '%s'. Expeted 1, found %d", morphNodeName, numFoundNodes);
	}

	morphNode = h3dGetNodeFindResult(0);
	if (morphNode == 0) ERR("h3dGetNodeFindResult");


	h3dSetNodeTransform(
		morphNode, 
		bodyConfiguration->getFloat("Scene.Transformation.Traslation.X"), 
		bodyConfiguration->getFloat("Scene.Transformation.Traslation.Y"), 
		bodyConfiguration->getFloat("Scene.Transformation.Traslation.Z"), 

		bodyConfiguration->getFloat("Scene.Transformation.Rotation.X"), 
		bodyConfiguration->getFloat("Scene.Transformation.Rotation.Y"), 
		bodyConfiguration->getFloat("Scene.Transformation.Rotation.Z"), 

		bodyConfiguration->getFloat("Scene.Transformation.Scale.X"), 
		bodyConfiguration->getFloat("Scene.Transformation.Scale.Y"), 
		bodyConfiguration->getFloat("Scene.Transformation.Scale.Z")
	);
}

void Scene::setInitialMorphs(void) {
	int morphNumber = bodyConfiguration->getLength("Body.InitialMorphs");

	for (int morphIndex = 0; morphIndex < morphNumber; morphIndex++) {
		
		char setting[80];
		char name[80];
		float value;

		_snprintf(setting, 80, "Body.InitialMorphs.[%d].Name", morphIndex);
		bodyConfiguration->getString(setting, name, 80);

		_snprintf(setting, 80, "Body.InitialMorphs.[%d].Value", morphIndex);
		value = bodyConfiguration->getFloat(setting);

		morphTargetBlender->setMorphTargetValue(name, value);
	}
}



void Scene::quit(void) {
}

/*
void Scene::playAnimation(char *animationFile) {
	_animTime = 0;
	
	H3DRes animationResource;

	animationResource = h3dFindResource(
		H3DResTypes::Animation, 
		animationFile
	);

	if (animationResource == 0) ERR("Animation %s not found", animationFile);

	h3dSetupModelAnimStage( morphNode, 0, animationResource, 0, "", false );
}

void Scene::stopAnimation(void) {
	h3dSetupModelAnimStage( morphNode, 0, NULL, 0, "", false );
}



void Scene::update(void) {

	// Update animations

	h3dSetModelAnimParams( morphNode, 0, _animTime, 1.0);
	_animTime += 1.0f;

}

void Scene::addAnimationResources(void) {

	int numFiles = bodyConfiguration->getLength("Animations.FileList");
	for (int i = 0; i < numFiles; i++) {
		char settingName[80];
		_snprintf(settingName, 80, "Animations.FileList.[%d]", i);

		char fileName[80];
		bodyConfiguration->getString(settingName, fileName, 80);

		LoggerInfo("Loading animation: %s", fileName);

		H3DRes res = h3dAddResource( H3DResTypes::Animation, fileName, 0 );
		if (res == 0) ERR("Error adding animatino resource %s", fileName);
	}
}
*/

