/// @file Scene2DSpark.cpp
/// @brief Scene2DSpark class implementation.

#include "stdAfx.h"
#include <math.h>
#include <vector>
#include "Configuration.h"
#include <sys/syscall.h>
#include <stdlib.h>
#include <stdio.h>
#include "libconfig.h++"

#include "Scene2DSpark.h"

using namespace libconfig;

#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	) {
	if (!strcmp(componentType, "Scene2DSpark")) {
		return new Scene2DSpark(componentInstanceName, componentSystem);
	}
	else {
		return NULL;
	}
}
#endif

/// Initializes Scene2DSpark component.
void Scene2DSpark::init(void){

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

	LoggerInfo("[Scene2DComponent::init] FilePath: %s\n",characterConfigurationFileName);
	//Character parameters have been moved to an own configuration file
	characterConfiguration.loadConfiguration(characterConfigurationFileName);


	svgGeneral = new SvgGeneral();

	width = getGlobalConfiguration()->getInt(const_cast<char*>("AudioVideoConfig_Width"));
	height = getGlobalConfiguration()->getInt(const_cast<char*>("AudioVideoConfig_Height"));
	bytesPerPixel = 3;//getComponentConfiguration()->getInt(const_cast<char*>("BytesPerPixel"));

	maxHeadPan = characterConfiguration.getFloat(const_cast<char*>("Body_Joints_MaxHeadPan"));
	maxHeadTilt = characterConfiguration.getFloat(const_cast<char*>("Body_Joints_MaxHeadTilt"));

	maxEyePan = characterConfiguration.getFloat(const_cast<char*>("Body_Joints_MaxEyePan"));
	maxEyeTilt = characterConfiguration.getFloat(const_cast<char*>("Body_Joints_MaxEyeTilt"));

	// PENDIENTE REVISION :Inicializaciones necesarias al no ser un slider
	svgGeneral->m_expand = 0.0;
	svgGeneral->m_gamma = 1.0;
	svgGeneral->m_scale = 0.5;
	svgGeneral->m_rotate = 0.0;

	// PENDIENTE REVISION: Inicializaciones temporales, ver realmente como deben de ser
	svgGeneral->m_initial_width = width;
	svgGeneral->m_initial_height = height;

	// Espacio para el buffer
	buffer = new unsigned char[width * height * bytesPerPixel];
	memset(buffer, 255, width * height * bytesPerPixel);

	// Inicializo la configuración de los ficheros svg
	initSvgConfiguration();

	// Creacion del objeto MorphTargetBlender
	myOldMorphTargetBlender = new MorphTargetBlender2D(svgGeneral);

}

/// Unitializes the Scene2DSpark component.
void Scene2DSpark::quit(void){
	// Como reservo la zona de memoria con un malloc debo liberarla
	// mediante la función free.
	free (svgGeneral->m_alphas_container);
}


void Scene2DSpark::initSvgConfiguration(){

	printf("Loading svg files...\n");

	svgGeneral->numeroDeFicheros = characterConfiguration.getLength(const_cast<char*>("Config.Targets"));


	//contructor de alphas
	printf("=====================================\n");
	printf("Constructor de alphas \n");
	printf("=====================================\n\n");

	// Inicializo vector de double dinamico
	svgGeneral->m_alphas_container = (double *)malloc((svgGeneral->numeroDeFicheros)*sizeof(double));
	svgGeneral->initAlphas();

	try	{
		svgGeneral->m_files_container=new ContenedorDeFicheros[svgGeneral->numeroDeFicheros];
		//for(int i = 0; i < count; ++i) {
		for(int i = 0; i < svgGeneral->numeroDeFicheros; ++i) {
			char settingName[1024];
			char EmotionName_svg[1024];
			char TargetName_svg[1024];

			_snprintf(settingName, 1024, "Config.Targets.[%d].EmotionName", i);
			characterConfiguration.getString(settingName,EmotionName_svg, 1024);
			strcpy(svgGeneral->m_files_container[i].nombreMorphTarget,EmotionName_svg);

			_snprintf(settingName, 1024, "Config.Targets.[%d].TargetName", i);
			characterConfiguration.getFilePath(settingName,TargetName_svg, 1024);
			strcpy(svgGeneral->m_files_container[i].nombreFichero,TargetName_svg);
		}
	}
	catch(const SettingNotFoundException &nfex) {
		ERR("Error while getting morphTarget names");
	}
	// CONJUNTO DE PATHS
	// Construye un vector dinamico de objetos de la clase path_renderer
	// cada posicion del vector contiene un m_path que esta asociado
	// a un fichero distinto
	printf("============================================================================\n");
	printf("                                 Conjunto de paths\n");
	printf("============================================================================\n\n");
	svgGeneral->m_paths_container= new agg::svg::path_renderer[svgGeneral->numeroDeFicheros];

	try {
		char background[1024];
		characterConfiguration.getFilePath(const_cast<char*>("Background.File"),background,1024);
		svgGeneral->parse_svg_fondo(background);
		//svgGeneral->parse_svg(background, svgGeneral->m_path_fondo);

		svgGeneral->m_paths_container= new agg::svg::path_renderer[svgGeneral->numeroDeFicheros];

		// Asociamos el m_path_total con su fichero(neutro)
		svgGeneral->parse_svg_total(svgGeneral->full_file_name(svgGeneral->m_files_container[0].nombreFichero));
		//svgGeneral->parse_svg(svgGeneral->full_file_name(svgGeneral->m_files_container[0].nombreFichero),svgGeneral->m_path_total);

		// Asociamos todos los paths con sus correspondientes ficheros(morphTargets)
		for(int k=0;k<svgGeneral->numeroDeFicheros;k++) {
			svgGeneral->parse_svg_ficheros(svgGeneral->full_file_name(svgGeneral->m_files_container[k].nombreFichero),k);
		}

		// Cargamos el numero de paths que hay en el fichero
		svgGeneral->numeroDePath=svgGeneral->m_paths_container[0].paths_number();
	}
	catch(agg::svg::exception& e) {
       ERR("Error while extracting information from SVG files: %s", e.msg());
    }
}

// IFaceExpression implementation
void Scene2DSpark::setFaceExpression(char *expressionName,float intensity){
	myOldMorphTargetBlender->setMorphTargetValue(expressionName,intensity);
}

//IEyes implementation
void Scene2DSpark::rotateEye(float pan,float tilt) {

	myPan = svgGeneral->sign(pan) * min(svgGeneral->absoluteValue(pan), maxEyePan);
	myTilt= svgGeneral->sign(tilt) * min(svgGeneral->absoluteValue(tilt), maxEyeTilt);

	// El valor del pan tiene que estar acotado entre 0 y 1 en este caso
	// dado que funciona como una intensidad para el morphTarget
	PanSvg =svgGeneral->absoluteValue(myPan/maxEyePan);
	if(pan>0){
		myOldMorphTargetBlender->setMorphTargetValue(const_cast<char*>("right_look"),PanSvg);
		//printf("Valor de PanSvg en right_look es %f\n",PanSvg);
	}
	else{
		myOldMorphTargetBlender->setMorphTargetValue(const_cast<char*>("left_look"),PanSvg);
		//printf("Valor de PanSvg en left_look es %f\n",PanSvg);
	}

	// El valor del tilt tiene que estar acotado entre 0 y 1 en este caso
	// dado que funciona como una intensidad para el morphTarget
	TiltSvg=svgGeneral->absoluteValue(myTilt/maxEyeTilt);
	if(tilt>0){
		myOldMorphTargetBlender->setMorphTargetValue(const_cast<char*>("up_look"),TiltSvg);
		//printf("Valor de TiltSvg en up_look es %f\n",TiltSvg);
	}
	else{
		myOldMorphTargetBlender->setMorphTargetValue(const_cast<char*>("down_look"),TiltSvg);
		//printf("Valor de TiltSvg en down_look es %f\n",TiltSvg);
	}

	// Para recolocar esto es una chapuza que esta puesta de momento
	if(pan==0){
		myOldMorphTargetBlender->setMorphTargetValue(const_cast<char*>("right_look"),0);
		myOldMorphTargetBlender->setMorphTargetValue(const_cast<char*>("left_look"),0);
	}
	if(tilt==0){
		myOldMorphTargetBlender->setMorphTargetValue(const_cast<char*>("up_look"),0);
		myOldMorphTargetBlender->setMorphTargetValue(const_cast<char*>("down_look"),0);
	}
}

void Scene2DSpark::setBlinkLevel(float blinkLevel) {

}

//INeck implementation
void Scene2DSpark::rotateHead(float pan, float tilt) {

}

//IAnimation implementation
void Scene2DSpark::update() {

}

void Scene2DSpark::playAnimation(char *animationFileName) {
	_animTime=0;
}

// IRenderizable implementation
void * Scene2DSpark::render(void) {

	agg::rendering_buffer rbuf(buffer,
		                           width,
		                           height,
		                           -width * bytesPerPixel); // Flip Y to go up

	// Se crea un objeto pixel de bajo nivel de representacion
	// se adjunta a la mejoria intermedia de la representacion
	agg::pixfmt_rgb24 pixf(rbuf);
	renderer_base_pixfmt rb(pixf);
	renderer_solid ren(rb);

	// Resize del paisaje
	svgGeneral->resizePaisaje(ren, rb, rbuf);

	// Transformaciones de la cara
	svgGeneral->mtx= svgGeneral->transformacionesCara();

	// Calculo coordenadas y renderiza
	svgGeneral->calculoCoordenadas(ren,rb);

	//return img.get_buf();
	return buffer;
}

void Scene2DSpark::unMapResourceStream () {

}

//ICamera implementation
void Scene2DSpark::setCameraPosition(float X,float Y,float Z){
	svgGeneral->setCameraPosition(X, Y, Z);
}

void Scene2DSpark::setCameraRotation(float X,float Y,float Z){

}

void Scene2DSpark::setCameraParameters(bool isOrtho, float visionAngle,float nearClippingPlane,float farClippingPlane){

}

