/// @file Scene.cpp
/// @brief Scene class implementation.


//#include "Window_Svg.h"
#include "agg_slider_ctrl.h"
//#include "agg_color_rgba.h"

#include "stdAfx.h"
#include <math.h>
#include <vector>

#include "Skeleton2D.h"
#include "MorphTargetBlender2D.h"
#include "Scene2D.h"


using namespace std;

void Scene2D::initSvgConfiguration(char* bodyConfigurationFileName)
{
	LoggerInfo("Loading svg files...");

	//const Setting& root= cfg.getRoot();

	int count=0;

	//const Setting &targets = root["Config"]["Targets"];

	//obtiene el numero de ficheros y lo guarda en su correspondiente variable
	//CONJUNTO DE FICHEROS(y de morphTargets)
	//construye un vector dinamico
	//en cada posicion hay una estructura ContenedorDeFicheros(con su correspondiente
	// fichero y morphTarget)

	//count = targets.getLength();

	count = bodyConfiguration->getLength(const_cast<char *>("Config.Targets"));
	app_svg->numeroDeFicheros=count;

	//contructor de alphas
	app_svg->m_alphas_container=new agg::slider_ctrl<agg::rgba8> [app_svg->numeroDeFicheros];
	app_svg->initAlphas();

	try	{
		app_svg->m_files_container=new ContenedorDeFicheros[app_svg->numeroDeFicheros];
		cout << setw(30)<<left << "EMOTION NAME" << "  "
			<< setw (30)<<left << "TARGET NAME" << "   "
			<< endl;

		for(int i = 0; i < count; ++i) {
			//const Setting &target = targets[i];

			// Only output the record if all of the expected fields are present.

			//const char *emotionName="hola";
			//const char *targetName="caracola";

			char settingName[1024];

			/*
			if( target.lookupValue("EmotionName",emotionName)&& target.lookupValue("TargetName", targetName)) {
				//copia el morphTarget y el nombre del fichero a nuestro vector dinamico
				//strcpy(app.m_files_container[i].nombreMorphTarget,emotionName);
				strcpy(winSvg->app_svg->m_files_container[i].nombreMorphTarget,emotionName);

				//strcpy(app.m_files_container[i].nombreFichero,targetName);
				strcpy(winSvg->app_svg->m_files_container[i].nombreFichero,targetName);
				cout << setw(30)<< left << emotionName << "  "
					<< setw(30)<<left << targetName << "  "
					<< endl;
			 }
			*/

			char EmotionName_svg[1024];
			char TargetName_svg[1024];

			// bodyConfiguration.getString("Config.Targets.EmotionName",EmotionName_svg,256);
			// bodyConfiguration.getString("Config.Targets.TargetName",TargetName_svg,256);

			_snprintf(settingName, 1024, "Config.Targets.[%d].EmotionName", i);
			bodyConfiguration->getString(settingName, EmotionName_svg, 1024);
			strcpy(app_svg->m_files_container[i].nombreMorphTarget,EmotionName_svg);

			_snprintf(settingName, 1024, "Config.Targets.[%d].TargetName", i);

			//bodyConfiguration->getString(settingName,TargetName_svg,128);
			//SVGfiles/Neutro.svg

			bodyConfiguration->getFilePath(settingName,TargetName_svg , 1024);
			strcpy(app_svg->m_files_container[i].nombreFichero,TargetName_svg);
		}
	}
	//catch(const SettingNotFoundException &nfex) {
	//catch(SettingNotFoundException &nfex) {
	catch(Exception e){
		ERR("Error while getting morphTarget names");
	}

	//CONJUNTO DE PATHS
		//construye un vector dinamico de objetos de la clase path_renderer
		//cada posicion del vector contiene un m_path que esta asociado
		//a un fichero distinto
	//app.m_paths_container= new agg::svg::path_renderer[app.numeroDeFicheros];

	app_svg->m_paths_container= new agg::svg::path_renderer[app_svg->numeroDeFicheros];

	try
    {
		//const char* fnameBackground = "Neutro.svg";

		//FILE* fd_ = fopen(winSvg->app_svg->full_file_name(fnameBackground), "r");

		//app.parse_svg_fondo(app.full_file_name(fnameBackground));

		char background[128];
		bodyConfiguration->getFilePath(const_cast<char *>("Background.File"),background,128);

		//winSvg->app_svg->parse_svg_fondo("C:/Users/Pablo/Desktop/ECA_WORK/ECA/Programacion/ProtoPablo/marco_paisaje.svg");

		app_svg->parse_svg_fondo(background);
		app_svg->m_paths_container= new agg::svg::path_renderer[app_svg->numeroDeFicheros];

		//asociamos el m_path_total con su fichero(neutro)
		app_svg->parse_svg_total(app_svg->full_file_name(app_svg->m_files_container[0].nombreFichero));

		//asociamos todos los paths con sus correspondientes ficheros(morphTargets)
		for(int k=0;k<app_svg->numeroDeFicheros;k++)
		{
			app_svg->parse_svg_ficheros(app_svg->full_file_name(app_svg->m_files_container[k].nombreFichero),k);
			cout<<" numero de nodos del fichero: "<<setw(30)<<app_svg->m_files_container[k].nombreFichero<<"= "<<" "
				<< app_svg->m_paths_container[k].total_vertices()<<endl;
		}

		//cargamos el numero de paths que hay en el fichero
		app_svg->numeroDePath=app_svg->m_paths_container[0].paths_number();

		//CONJUNTO DE CHECKBOX(construimos)
		app_svg->paths_id_container=new agg::cbox_ctrl<agg::rgba8> [app_svg->numeroDePath];

		//CONJUNTO DE ETIQUETAS para los checkbox
		app_svg->id_path_names_queue= new string [app_svg->numeroDePath];

		//Sacamos los path_id de la cola y los guardamos en el conjunto de etiquetas
		for(int i=0;i<app_svg->numeroDePath;i++)
			app_svg->m_paths_container[0].get_m_path_name_queue(app_svg->id_path_names_queue[i]);

		//para que al principio aparezca toda la cara(todos los checkbox activados)
		for(int i=0;i<(int16_t)(app_svg->m_paths_container[0].paths_number());i++)
			app_svg->paths_id_container[i].status(true);

	}
    catch(agg::svg::exception& e){
        //app.message(e.msg());
		//win->app_svg->message(e.msg());
		ERR("Error while extracting information from SVG files");
    }
}

/*void Scene2D::copyWinSvg(Window_Svg *winSvgCopy) {
	this->winSvg = winSvgCopy;
}*/

void Scene2D::init(int width, int height) {
	LoggerInfo("Initializing Scene2D");
	skeleton->init();

	//PABLO: 2D
	char bodyConfigurationFileName[1024];
	mainConfig.getFilePath(const_cast<char*>("Scene2D.BodyConfig"), bodyConfigurationFileName, 1024);
	bodyConfiguration->loadConfiguration(bodyConfigurationFileName);

	//PABLO: 2D
	initSvgConfiguration(bodyConfigurationFileName);

}

void Scene2D::quit(void){
	/******************* No tiene contenido***********************/
}


