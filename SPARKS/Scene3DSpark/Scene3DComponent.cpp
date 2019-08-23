/// @file Scene3DComponent.cpp
/// @brief Scene3DComponent class implementation.


#include "stdAfx.h"
#include <math.h>
#include <vector>
#include "Horde3D.h"
#include "Horde3DUtils.h"
//#include "Skeleton.h"
#include "MorphTargetBlender.h"
#include "Configuration.h"

#include "Scene3DComponent.h"

using namespace std;

#ifdef _WIN32
#pragma comment (lib, "Horde3D.lib")
#pragma comment (lib, "Horde3DUtils.lib")
#pragma comment (lib, "opengl32.lib")
#else
#endif


#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "Scene3DComponent")) {
			return new Scene3DComponent(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif


Eye::Eye(char *jointName, bool zSignShift) {
	strncpy(this->jointName, jointName, MAX_JOINT_NAME_LENGTH);

	this->zSignShift = zSignShift;

	float tx, ty, tz;
	float sx, sy, sz;

	int res = h3dFindNodes(H3DRootNode, jointName, H3DNodeTypes::Undefined);
	if (res != 1) {
		ERR("Wrong number of joints found, expected 1, found %d finding '%s'",
			res, jointName
		);
	}

	H3DNode jointNode = h3dGetNodeFindResult(0);
	if (jointNode == 0) ERR("h3dGetNodeFindResult");


	h3dGetNodeTransform(
		jointNode,
		&tx, &ty, &tz,
		&originalRotation.vx,
		&originalRotation.vy,
		&originalRotation.vz,
		&sx, &sy, &sz
	);
}


/// Initializes Scene3DComponent component.

void Scene3DComponent::init(void) 
{

	//Load characterConfiguration
	char characterName[100];
	char characterConfigurationFileName[1024];
	char characterConfigPath[1024];
	getComponentConfiguration()->getString(const_cast<char *>("Character"),
			characterName,
			100
	);

	getComponentConfiguration()->getString(const_cast<char *>("characterConfigPath"),
			characterConfigPath,
			1024
	);

	_snprintf(characterConfigurationFileName, 1024, "%s%s.ini", characterConfigPath, characterName);

	LoggerInfo("[Scene3DComponent::init] FilePath: %s\n",characterConfigurationFileName);
	//Character parameters have been moved to an own configuration file
	characterConfiguration.loadConfiguration(characterConfigurationFileName);

	//create MorphTargetBlender object
	myOldMorphTargetBlender = new MorphTargetBlender();

	width = getGlobalConfiguration()->getInt(const_cast<char *>("AudioVideoConfig_Width"));
	height= getGlobalConfiguration()->getInt(const_cast<char *>("AudioVideoConfig_Height"));
	background = false;

	//Horde3D init. Requires OpenGL window intialized.
	if( !h3dInit() )
	{	
		LoggerError("[FIONA-logger]Error intializing 3d engine");
		ERR("Error intializing 3d engine");
	}

	h3dSetOption( H3DOptions::LoadTextures, 1 );
	h3dSetOption( H3DOptions::TexCompression, 0 );
	h3dSetOption( H3DOptions::MaxAnisotropy, 4 );
	h3dSetOption( H3DOptions::ShadowMapSize, 2048 );
	h3dSetOption( H3DOptions::FastAnimation, 1 );


	// OJO
	// h3dSetOption( H3DOptions::WireframeMode, 1 );

	int antialisingSampleCount = Component::getComponentConfiguration()->getInt(const_cast<char *>("Scene_AntialisingSampleCount"));

	h3dSetOption( H3DOptions::SampleCount, (float)antialisingSampleCount );

	addResources();
	addAnimationResources();
	loadResourcesFormDisk();
	addNodesToScene();

	//MorphTargetBlender init
	char morphNodeName[80];

	characterConfiguration.getString(const_cast<char *>("Body_MorphNodeName"), morphNodeName, 80);
	myOldMorphTargetBlender->init(morphNodeName);

	if (characterConfiguration.getBool(const_cast<char *>("Body_Joints_ArmsNeedDowning")))
	{
		armsDown();
	}

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
	//Initialization skeleton (not in a class)
	initSkeleton();
	int numFoundNodes;
	numFoundNodes = h3dFindNodes(H3DRootNode, morphNodeName, H3DNodeTypes::Undefined);
	if (numFoundNodes != 1) {
		ERR("Error finding node '%s'. Expeted 1, found %d", morphNodeName, numFoundNodes);
	}

	morphNode = h3dGetNodeFindResult(0);
	if (morphNode == 0) ERR("h3dGetNodeFindResult");

	h3dSetNodeTransform(
			morphNode,
			getComponentConfiguration()->getFloat(const_cast<char *>("Scene_Transformation_Traslation_X")),
			getComponentConfiguration()->getFloat(const_cast<char *>("Scene_Transformation_Traslation_Y")),
			getComponentConfiguration()->getFloat(const_cast<char *>("Scene_Transformation_Traslation_Z")),

			getComponentConfiguration()->getFloat(const_cast<char *>("Scene_Transformation_Rotation_X")),
			getComponentConfiguration()->getFloat(const_cast<char *>("Scene_Transformation_Rotation_Y")),
			getComponentConfiguration()->getFloat(const_cast<char *>("Scene_Transformation_Rotation_Z")),

			getComponentConfiguration()->getFloat(const_cast<char *>("Scene_Transformation_Scale_X")),
			getComponentConfiguration()->getFloat(const_cast<char *>("Scene_Transformation_Scale_Y")),
			getComponentConfiguration()->getFloat(const_cast<char *>("Scene_Transformation_Scale_Z"))
	);
}


/// Unitializes the Scene3DComponent component.

void Scene3DComponent::quit(void) 
{
}

// IFaceExpression implementation
void Scene3DComponent::setFaceExpression(char *expressionName,float intensity)
{
	myOldMorphTargetBlender->setMorphTargetValue(expressionName,intensity);
}


//IJoint implementation
void Scene3DComponent::setJointTransMat(const char *jointName, const float *mat4x4){
	setNodeTransMat(jointName,mat4x4);
}
void Scene3DComponent::rotateJointPart(char *jointName, float rotationX,	float rotationY, float rotationZ)
{
	rotateJoint(jointName, rotationX, rotationY, rotationZ);
}

void Scene3DComponent::moveJointPart(char *jointName, float translationX,	float translationY, float translationZ)
{
	moveJoint(jointName,translationX,translationY,translationZ);
}

void Scene3DComponent::moveDiamondJoint(float translationX, float translationY, float translationZ)
{
	//char *jointName="SpineBase"; //Standard joint name
	char *jointName = const_cast<char *>("hip");
	moveJoint(jointName,translationX,translationY,translationZ);
}
void Scene3DComponent::GetJointRotation(char *jointName, float *x, float *y, float *z)
{
	int res = h3dFindNodes(H3DRootNode, jointName, H3DNodeTypes::Undefined);
	if (res != 1)
	{
		ERR("Wrong number of joints found, expeted 1, found %d finding '%s'",
			res, jointName
		);
	}

	H3DNode jointNode = h3dGetNodeFindResult(0);
	if (jointNode == 0) ERR("h3dGetNodeFindResult");

	float tx, ty, tz;
	float rx, ry, rz;
	float sx, sy, sz;

	h3dGetNodeTransform(jointNode,
			&tx, &ty, &tz,
			&rx, &ry, &rz,
			&sx, &sy, &sz
			);

	*x=rx;
	*y=ry;
	*z=rz;

	//printf("In Scene3D X: %f\n", *x);
	//printf("In Scene3D Y: %f\n", *y);
	//printf("In Scene3D Z: %f\n", *z);
}

void Scene3DComponent::GetJointPosition(char *jointName, float *x, float *y, float *z)
{
	int res = h3dFindNodes(H3DRootNode, jointName, H3DNodeTypes::Undefined);
	if (res != 1)
	{
		ERR("Wrong number of joints found, expeted 1, found %d finding '%s'",
			res, jointName
		);
	}

	H3DNode jointNode = h3dGetNodeFindResult(0);
	if (jointNode == 0) ERR("h3dGetNodeFindResult");

	float tx, ty, tz;
	float rx, ry, rz;
	float sx, sy, sz;

	h3dGetNodeTransform(jointNode,
			&tx, &ty, &tz,
			&rx, &ry, &rz,
			&sx, &sy, &sz
			);

	*x=tx;
	*y=ty;
	*z=tz;

	//printf("In Scene3D X: %f\n", *x);
	//printf("In Scene3D Y: %f\n", *y);
	//printf("In Scene3D Z: %f\n", *z);
}

bool Scene3DComponent::findNode(const char *jointName){
	int res = h3dFindNodes(H3DRootNode, jointName, H3DNodeTypes::Undefined);
	if (res != 1) {
		return false;
	}
	return true;
}

// IEyes implementation
void Scene3DComponent::rotateEye(float pan,float tilt)
{
	float mult = leftEye->zSignShift ? -1.0f : 1.0f;
	rotateJoint(
		leftEye->jointName,
		leftEye->originalRotation.vx,
		leftEye->originalRotation.vy - tilt - headTiltOffset,
		mult * (leftEye->originalRotation.vz - pan - headPanOffset)
		);

	float mult2 = rightEye->zSignShift ? -1.0f : 1.0f;
	rotateJoint(
			rightEye->jointName,
			rightEye->originalRotation.vx,
			rightEye->originalRotation.vy - tilt - headTiltOffset,
			mult2 * (rightEye->originalRotation.vz - pan - headPanOffset)
			);
}

// IEyes implementation
void Scene3DComponent::setBlinkLevel(float blinkLevel)
{
	char blinkMorphTargetName[80];
	characterConfiguration.getString(const_cast<char *>("ExtraTargets.Blink"), blinkMorphTargetName, 80);
	myOldMorphTargetBlender->setMorphTargetValue(blinkMorphTargetName,blinkLevel);
}

// INeck implementation
void Scene3DComponent::rotateHead(float pan,float tilt)
{
	rotateHeadSkeleton(pan,tilt);
}

// IAnimation implementation
void Scene3DComponent::playAnimation(char *animationFileName)
{
	_animTime = 0;
	
	H3DRes animationResource;

	animationResource = h3dFindResource(
		H3DResTypes::Animation, 
		animationFileName
	);

	if (animationResource == 0){
		LoggerError("[FIONA-logger]Animation %s not found",animationFileName);
		ERR("Animation %s not found", animationFileName);
	}

	h3dSetupModelAnimStage(morphNode, 0, animationResource, 0, "", false);
}

//ICamera implementation
void Scene3DComponent::setCameraPosition(float x, float y, float z) {

	float pos_x, pos_y, pos_z;
	float rot_x, rot_y, rot_z;
	float scale_x, scale_y, scale_z;

	// get previous values
	h3dGetNodeTransform(
		camaraNode,
		&pos_x, &pos_y, &pos_z,
		&rot_x, &rot_y, &rot_z,
		&scale_x, &scale_y, &scale_z
	);

	// set new values
	h3dSetNodeTransform(
		camaraNode,
		x, y, z,
		rot_x, rot_y, rot_z,
		scale_x, scale_y, scale_z
	);
}


void Scene3DComponent::setCameraRotation(float x, float y, float z) {
	float pos_x, pos_y, pos_z;
	float rot_x, rot_y, rot_z;
	float scale_x, scale_y, scale_z;

	// get previous values
	h3dGetNodeTransform(
		camaraNode,
		&pos_x, &pos_y, &pos_z,
		&rot_x, &rot_y, &rot_z,
		&scale_x, &scale_y, &scale_z
	);

	// set new values
	h3dSetNodeTransform(
		camaraNode,
		pos_x, pos_y, pos_z,
		x, y, z,
		scale_x, scale_y, scale_z
	);
}

void Scene3DComponent::setCameraParameters(bool isOrtho,
												float VisionAngle,
												float nearClippingPlane,
												float FarClippingPlane)
{
	if (isOrtho) {
		h3dSetNodeParamI(camaraNode, H3DCamera::OrthoI, 1);

		h3dSetupCameraView(
			camaraNode,
			VisionAngle,
			(float)width / (float)height,
			nearClippingPlane,
			FarClippingPlane
			);
	}
	else {
		h3dSetNodeParamI(camaraNode, H3DCamera::OrthoI, 0);
	}
}

// Update animations
void Scene3DComponent::update() {
	h3dSetModelAnimParams( morphNode, 0, _animTime, 1.0);
	_animTime += 1.0f;
}

void Scene3DComponent::addResources(void) {
	char pipelineXML[80];

	Component::getComponentConfiguration()->getString(const_cast<char *>("Scene_PipelineXML"), pipelineXML, 80);
	pipelineResource = h3dAddResource( H3DResTypes::Pipeline, pipelineXML, 0 );

	h3dResizePipelineBuffers(pipelineResource, width, height);

	char lightMaterialXML[80];
	Component::getComponentConfiguration()->getString(const_cast<char *>("Scene_LightMaterial"), lightMaterialXML, 80);

	lightMatRes = h3dAddResource( H3DResTypes::Material, lightMaterialXML, 0 );

	char sceneGraphXml[80];
	characterConfiguration.getString(const_cast<char *>("SceneXML"), sceneGraphXml, 80);
	sceneResource = h3dAddResource( H3DResTypes::SceneGraph, sceneGraphXml, 0 );
	if (sceneResource == 0) HORDE_ERR("Error adding scene resources");

	try {
		char sceneBackgroundXml[80];
		characterConfiguration.getString(const_cast<char*>("Scene_BackgroundXML"), sceneBackgroundXml, 80);
		sceneResourcePlane = h3dAddResource( H3DResTypes::SceneGraph, sceneBackgroundXml, 0 );
		if (sceneResourcePlane == 0) HORDE_ERR("Error adding scene resources");
		background = true;
	} catch(::Exception &e){
		LoggerWarn("[FIONA-logger]No background will be displayed");
	}

	// Recursos para la sobreimpresión de texto
	_fontMatRes = h3dAddResource( H3DResTypes::Material, "overlays/font.material.xml", 0 );
	if (_fontMatRes == 0) HORDE_ERR("Error loading font resource");

	_panelMatRes = h3dAddResource( H3DResTypes::Material, "overlays/panel.material.xml", 0 );
	if (_panelMatRes == 0) HORDE_ERR("Error loading panel resource");
}


void Scene3DComponent::loadResourcesFormDisk(void) {

	char commonContentPath[1024];
	Component::getComponentConfiguration()->getFilePath(const_cast<char *>("Scene_CommonContentPath"), commonContentPath, 1024);

	char sceneContentPath[1024];
	characterConfiguration.getFilePath(const_cast<char *>("SceneContentPath"), sceneContentPath, 1024);

	char contentPaths[2050];
	_snprintf(contentPaths, 2050, "%s|%s", commonContentPath, sceneContentPath);

	LoggerInfo("[FIONA-logger]Loading 3D resources from disk...");
	bool ok = h3dutLoadResourcesFromDisk(contentPaths);
	
	if (!ok) HORDE_ERR("Error loading 3D resources from disk, at path %s", contentPaths);
	LoggerInfo("[FIONA-logger]...Done.");
}


void Scene3DComponent::addNodesToScene(void) {

	// Scene tree
	H3DNode _node = h3dAddNodes(H3DRootNode, sceneResource);
	if (_node == 0) HORDE_ERR("Error adding scene tree resource");

	if(background) {
		H3DNode _node1 = h3dAddNodes(H3DRootNode, sceneResourcePlane);
		if (_node1 == 0) HORDE_ERR("Error adding scene tree resource");
	}

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

	bool isOrtho = false;
	if (isOrtho) {
		h3dSetNodeParamI(camaraNode, H3DCamera::OrthoI, 1);

		h3dSetupCameraView(
				camaraNode,
				characterConfiguration.getFloat(const_cast<char *>("Scene_Camera_CamaraParameters_VisionAngle")),
				(float)width / (float)height,
				characterConfiguration.getFloat(const_cast<char *>("Scene_Camera_CamaraParameters_NearClippingPlane")),
				characterConfiguration.getFloat(const_cast<char *>("Scene_Camera_CamaraParameters_FarClippingPlane"))
		);
	}
	else {
		h3dSetNodeParamI(camaraNode, H3DCamera::OrthoI, 0);
	}

	h3dSetNodeTransform(
			camaraNode,
			characterConfiguration.getFloat(const_cast<char *>("Scene_Camera_Position_X")),
			characterConfiguration.getFloat(const_cast<char *>("Scene_Camera_Position_Y")),
			characterConfiguration.getFloat(const_cast<char *>("Scene_Camera_Position_Z")),
			characterConfiguration.getFloat(const_cast<char *>("Scene_Camera_Rotation_X")),
			characterConfiguration.getFloat(const_cast<char *>("Scene_Camera_Rotation_Y")),
			characterConfiguration.getFloat(const_cast<char *>("Scene_Camera_Rotation_Z")),
			1,1,1	// scale
	);
}



void Scene3DComponent::createLights(void) {
	int lightNumber = getComponentConfiguration()->getLength(const_cast<char *>("Scene_Lights"));

	for (int lightIndex = 0; lightIndex < lightNumber; lightIndex++) {
		float x, y, z;
		float lightRadius;
		
		char setting[80];

		_snprintf(setting, 80, "Scene_Lights.[%d].X", lightIndex);
		x = getComponentConfiguration()->getFloat(setting);

		_snprintf(setting, 80, "Scene_Lights.[%d].Y", lightIndex);
		y = getComponentConfiguration()->getFloat(setting);

		_snprintf(setting, 80, "Scene_Lights.[%d].Z", lightIndex);
		z = getComponentConfiguration()->getFloat(setting);

		_snprintf(setting, 80, "Scene_Lights.[%d].LightRadius", lightIndex);
		lightRadius = getComponentConfiguration()->getFloat(setting);

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


void Scene3DComponent::setInitialMorphs(void) {
	int morphNumber = characterConfiguration.getLength(const_cast<char *>("Body_InitialMorphs"));

	for (int morphIndex = 0; morphIndex < morphNumber; morphIndex++) {
		
		char setting[80];
		char name[80];
		float value;

		_snprintf(setting, 80, "Body_InitialMorphs.[%d].Name", morphIndex);
		characterConfiguration.getString(setting, name, 80);

		_snprintf(setting, 80, "Body_InitialMorphs.[%d].Value", morphIndex);
		value = characterConfiguration.getFloat(setting);

		myOldMorphTargetBlender->setMorphTargetValue(name, value);
	}
}


void Scene3DComponent::addAnimationResources(void) {

	int numFiles = characterConfiguration.getLength(const_cast<char *>("Animations.FileList"));

	for (int i = 0; i < numFiles; i++) {
		char settingName[80];
		_snprintf(settingName, 80, "Animations.FileList.[%d]", i);

		char fileName[80];
		characterConfiguration.getString(settingName, fileName, 80);

		LoggerInfo("Loading animation: %s", fileName);

		H3DRes res = h3dAddResource( H3DResTypes::Animation, fileName, 0 );
		if (res == 0){
			LoggerError("[FIONA-logger]Error adding animation resource %s", fileName);
			ERR("Error adding animation resource %s", fileName);
		}
	}
}

// IRenderizable implementation
void Scene3DComponent::render(void)
{
	// Render scene
	h3dRender( camaraNode );

	// Finish rendering of frame
	h3dFinalizeFrame();

	h3dutDumpMessages();
}



H3DRes Scene3DComponent::getCamaraNode() {
	return camaraNode;
}


//Skeleton auxiliar functions

/// Rotate an eye side-by-side and upside-down.

void Scene3DComponent::rotateEyeSkeleton(Eye *eye, float pan, float tilt) {
	float mult = eye->zSignShift ? -1.0f : 1.0f;

	rotateJoint(
		eye->jointName,
		eye->originalRotation.vx ,
		eye->originalRotation.vy - tilt - headTiltOffset,
		mult * (eye->originalRotation.vz - pan - headPanOffset)
	);
}


/// The same initialization as in Skeleton class

void Scene3DComponent::initSkeleton(void) {
	pan = tilt = 0;

	hasEyeBones = characterConfiguration.getBool(const_cast<char *>("Body_Joints_Eyes_HasEyeBones"));
	if (hasEyeBones) {
		char leftEyeJoint[MAX_JOINT_NAME_LENGTH];
		char rightEyeJoint[MAX_JOINT_NAME_LENGTH];

		characterConfiguration.getString(const_cast<char *>("Body_Joints_Eyes_Left"), leftEyeJoint, MAX_JOINT_NAME_LENGTH);
		characterConfiguration.getString(const_cast<char *>("Body_Joints_Eyes_Right"), rightEyeJoint, MAX_JOINT_NAME_LENGTH);

		leftEye = new Eye(leftEyeJoint, true);
		rightEye = new Eye(rightEyeJoint, false);
	}
	else {
		leftEye = NULL;
		rightEye = NULL;
	}

	headPanOffset = characterConfiguration.getFloat(const_cast<char *>("Body_Joints_HeadPanOffset"));
	headTiltOffset = characterConfiguration.getFloat(const_cast<char *>("Body_Joints_HeadTiltOffset"));
}

void Scene3DComponent::setNodeTransMat(const char *jointName, const float *mat4x4){
	int res = h3dFindNodes(H3DRootNode, jointName, H3DNodeTypes::Undefined);
	if (res != 1) {
		ERR("Wrong number of joints found, expected 1, found %d finding '%s'",
				res, jointName
		);
	}

	H3DNode jointNode = h3dGetNodeFindResult(0);
	if (jointNode == 0) ERR("h3dGetNodeFindResult");

	h3dSetNodeTransMat(jointNode,mat4x4);
}

/// Rotates a joint acting on its three euler angles.
void Scene3DComponent::rotateJoint(
	char *jointName,
	float new_rx,
	float new_ry,
	float new_rz
)
{
	int res = h3dFindNodes(H3DRootNode, jointName, H3DNodeTypes::Undefined);
	if (res != 1) {
		ERR("Wrong number of joints found, expected 1, found %d finding '%s'",
			res, jointName
		);
	}

	H3DNode jointNode = h3dGetNodeFindResult(0);
	if (jointNode == 0) ERR("h3dGetNodeFindResult");

	float tx, ty, tz;
	float rx, ry, rz;
	float sx, sy, sz;

	h3dGetNodeTransform(
		jointNode,
		&tx, &ty, &tz,
		&rx, &ry, &rz,
		&sx, &sy, &sz
	);

	h3dSetNodeTransform(
		jointNode,
		tx, ty, tz,
		new_rx, new_ry, new_rz,
		sx, sy, sz
	);
}

void Scene3DComponent::moveJoint(char *jointName, float translationX, float translationY, float translationZ)
{
	float new_tx=translationX;
	float new_ty=translationY;
	float new_tz=translationZ;

	int res = h3dFindNodes(H3DRootNode, jointName, H3DNodeTypes::Undefined);
		if (res != 1) {
			ERR("Wrong number of joints found, expeted 1, found %d finding '%s'",
				res, jointName
			);
		}

		H3DNode jointNode = h3dGetNodeFindResult(0);
		if (jointNode == 0) ERR("h3dGetNodeFindResult");

		float tx, ty, tz;
		float rx, ry, rz;
		float sx, sy, sz;

		h3dGetNodeTransform(
			jointNode,
			&tx, &ty, &tz,
			&rx, &ry, &rz,
			&sx, &sy, &sz
		);

		h3dSetNodeTransform(
			jointNode,
			new_tx, new_ty, new_tz,
			rx, ry, rz,
			sx, sy, sz
		);
}


const float maxPan = 45;
const float maxTilt = 45;


inline float absoluteValue(float v) {
	return v > 0 ? v : -v;
}

inline float sign(float x) {
	return x >= 0 ? 1.0f : -1.0f;
}


/// Gets the pan and tilt angles of the head.
void Scene3DComponent::getHeadPos(float *pPan, float *pTilt) {
	*pPan = pan;
	*pTilt = tilt;
}


/// Rotates the head side-by-side and upside-down.
void Scene3DComponent::rotateHeadSkeleton(float pan, float tilt) {
	float myPan = sign(pan) * min(absoluteValue(pan), characterConfiguration.getFloat(const_cast<char *>("Body_Joints_MaxHeadPan")));
	float myTilt= sign(tilt) * min(absoluteValue(tilt), characterConfiguration.getFloat(const_cast<char *>("Body_Joints_MaxHeadTilt")));

	this->pan = myPan;
	this->tilt = myTilt;

	int neckJointNumber = characterConfiguration.getLength(const_cast<char *>("Body_Joints_NeckJoints"));

	char jointName[128];
	char settingName[128];

	for (
		int neckJointIndex = 0;
		neckJointIndex < neckJointNumber;
		neckJointIndex++
	)
	{
		_snprintf(settingName, 128, "Body_Joints_NeckJoints.[%d].Joint", neckJointIndex);
		characterConfiguration.getString(settingName, jointName, 128);

		if (characterConfiguration.getBool(const_cast<char *>("Body_Joints_NeckFix"))) {
			rotateJoint(jointName, -myPan - headPanOffset, -myTilt - headTiltOffset, 0.0f);
		}
		else {
			rotateJoint(jointName, -myTilt - headTiltOffset, -myPan - headPanOffset, 0.0f);
		}
	}
}


/// Baja los brazos para un personaje situado inicialmente en posición T.
void Scene3DComponent::armsDown(void) {

	char jointName[128];
	char settingName[128];

	int armIndex;
	int leftArmJointNumber;
	int rightArmJointNumber;

	leftArmJointNumber = characterConfiguration.getLength(const_cast<char *>("Body_Joints_LeftArmJoints"));

	for (armIndex = 0; armIndex < leftArmJointNumber; armIndex++) {

		_snprintf(settingName, 128, "Body_Joints_LeftArmJoints.[%d].Joint", armIndex);
		characterConfiguration.getString(settingName, jointName, 128);

		rotateJoint(
			jointName,
			0.0f, 0.0f, -60.0f
		);
	}

	rightArmJointNumber = characterConfiguration.getLength(const_cast<char *>("Body_Joints_RightArmJoints"));

	for (armIndex = 0; armIndex < rightArmJointNumber; armIndex++) {

		_snprintf(settingName, 128, "Body_Joints_RightArmJoints.[%d].Joint", armIndex);
		characterConfiguration.getString(settingName, jointName, 128);

		rotateJoint(
			jointName,
			0.0f, 0.0f, 60.0f
		);
	}
}

//end of Skeleton auxiliar functions


