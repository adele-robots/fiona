#ifndef __SvgGeneral_H
#define __SvgGeneral_H


#include <stdio.h>
#include <stdlib.h>

// Includes necesarios relativos a agg
#include "agg_basics.h"
#include "agg_rendering_buffer.h"
//#include "agg_pixfmt_rgba.h"
#include "agg_svg_path_tokenizer.h"
#include "agg_rasterizer_scanline_aa.h"
#include "agg_scanline_p.h"
#include "agg_renderer_scanline.h"
#include "agg_path_storage.h"
#include "agg_pixfmt_gray.h"
#include "agg_pixfmt_rgb.h"
#include "agg_color_rgba.h"
#include "agg_svg_parser.h"
#include "agg_renderer_base.h"

// Librerias de agg de la carpeta ctrl necesarias
#include "ctrl/agg_slider_ctrl.h"
#include "ctrl/agg_cbox_ctrl.h"

// Librerias de agg de la carpeta platform necesarias
#include "platform/agg_platform_support.h"

#include "ContenedorDeFicheros.h"

// Librerias de agg para el efecto blur
#include "agg_blur.h"
#include "agg_gradient_lut.h"
#include "agg_span_gradient.h"
#include "agg_span_interpolator_linear.h"
#include "agg_span_allocator.h"
#include "ctrl/agg_polygon_ctrl.h"
#include "agg_trans_perspective.h"
#include "ctrl/agg_rbox_ctrl.h"
#include "agg_image_accessors.h"
#include "agg_span_image_filter_rgb.h"

using namespace agg;

//Typedef necesarios
typedef agg::pixfmt_rgb24 pixfmt;
typedef agg::renderer_base<pixfmt> renderer_base_pixfmt;
typedef agg::renderer_scanline_aa_solid<renderer_base_pixfmt> renderer_solid;

typedef agg::image_accessor_clip<pixfmt> source_type;
typedef agg::span_interpolator_linear<> interpolator_type;
typedef agg::gradient_x gradient_func_type;
typedef agg::pod_auto_array<pixfmt, 256> color_array_type;
typedef agg::span_gradient<pixfmt,
                               interpolator_type,
                               gradient_func_type,
                               color_array_type> span_gradient_type;
typedef agg::renderer_base<pixfmt> renderer_base_type;
typedef agg::span_allocator<pixfmt> span_allocator_type;
typedef agg::renderer_scanline_aa<renderer_base_type,
									span_allocator_type,
									span_gradient_type> renderer_gradient_type;

class SvgGeneral:
	public agg::platform_support
{

public:
	    // Variables necesarias
		double m_expand ;
		double m_gamma;
		double m_scale ;
		double m_rotate;

	    double m_min_x;
		double m_min_y;
		double m_max_x;
		double m_max_y;

		double m_x;
		double m_y;
		double m_dx;
		double m_dy;

		// Variables auxiliares donde se guardan los vertices
		double x_0;
		double y_0;
		double x_f;
		double y_f;
		double x_i;
		double y_i;
		double x_sum;
		double y_sum;

		bool   m_drag_flag;

		// Variables para trabajar con ficheros svg
		agg::svg::path_renderer m_path_total;
		agg::svg::path_renderer m_path_fondo;

		// Variable entera para guardar el numero de ficheros que se leen
		int numeroDeFicheros;

		// Variable entera para guardar el numero de paths que se leen
		int numeroDePath;

		// Vector dinamico de objetos de tipo ContenedorDeFicheros
		ContenedorDeFicheros *m_files_container;

		// Vector dinamico de double donde se guardan los valores de m_alphas
		// cada alpha corresponde con un fichero distinto
		double *m_alphas_container;

		// Vector dinamico de objetos donde se guardan los objetos m_paths
		agg::svg::path_renderer *m_paths_container;

		agg::rasterizer_scanline_aa<> ras;
		agg::trans_affine mtx;
		agg::scanline_p8 sl;

		float cameraX;
		float cameraY;

		// Numero de vertices
		unsigned numeroDeVertices;

		// Vectores de filtros
		std::vector<agg::image_filter_lut>	m_fondo_filters;
		std::vector<agg::image_filter_lut>	m_neutro_filters;
		std::vector<agg::image_filter_lut>	m_ficheros_filters;

public:

		// Constructor vacio
		SvgGeneral():
			agg::platform_support(agg::pix_format_rgb24, false),
			m_min_x(0.0),
			m_min_y(0.0),
			m_max_x(0.0),
			m_max_y(0.0),
			m_x(0.0),
			m_y(0.0),
			m_dx(0.0),
			m_dy(0.0),
			m_drag_flag(false),
			numeroDeFicheros(0),
			cameraX(140.0),
			cameraY(-140.0)  {
			}
	
		// Constructor normal con parametros
		SvgGeneral(agg::pix_format_e format, bool flip_y):
			agg::platform_support(format, flip_y),
			m_min_x(0.0),
			m_min_y(0.0),
			m_max_x(0.0),
			m_max_y(0.0),
			m_x(0.0),
			m_y(0.0),
			m_dx(0.0),
			m_dy(0.0),
			m_drag_flag(false),
			cameraX(140.0),
			cameraY(-140.0) {
			}

		// Constructor por copia
		SvgGeneral(SvgGeneral *svgGeneralOld):
			agg::platform_support(svgGeneralOld->format(), svgGeneralOld->flip_y())	{
			m_expand=svgGeneralOld->m_expand;
			m_gamma=svgGeneralOld->m_gamma;
			m_scale=svgGeneralOld->m_scale;
			m_rotate=svgGeneralOld->m_rotate;
			m_min_x=svgGeneralOld->m_min_x;
			m_min_y=svgGeneralOld->m_min_y;
			m_max_x=svgGeneralOld->m_max_x;
			m_max_y=svgGeneralOld->m_max_y;
			m_x=svgGeneralOld->m_x;
			m_y=svgGeneralOld->m_y;
			m_dx=svgGeneralOld->m_dx;
			m_dy=svgGeneralOld->m_dy;
			x_0=svgGeneralOld->x_0;
			y_0=svgGeneralOld->y_0;
			x_f=svgGeneralOld->x_f;
			y_f=svgGeneralOld->y_f;
			x_i=svgGeneralOld->x_i;
			y_i=svgGeneralOld->y_i;
			x_sum=svgGeneralOld->x_sum;
			y_sum=svgGeneralOld->y_sum;
			m_drag_flag=svgGeneralOld->m_drag_flag;
			numeroDeFicheros=svgGeneralOld->numeroDeFicheros;
			numeroDePath=svgGeneralOld->numeroDePath;
			numeroDeVertices=svgGeneralOld->numeroDeVertices;
			m_files_container = svgGeneralOld->m_files_container;
			m_alphas_container=svgGeneralOld->m_alphas_container;
			m_files_container = svgGeneralOld->m_files_container;
			m_paths_container= svgGeneralOld->m_paths_container;
			//m_path_total = svgGeneralOld->m_path_total;
			//m_path_fondo = svgGeneralOld->m_path_fondo;
			mtx= svgGeneralOld->mtx;
			//sl= svgGeneralOld->sl;
			cameraX=svgGeneralOld->cameraX;
			cameraY=svgGeneralOld->cameraY;
			}


		// Funcion para inicializar las Alphas,
		// estas ya no se inicializan en el constructor
		void initAlphas();

		//void parse_svg(const char* fname, agg::svg::path_renderer name_render);

		// Funcion para parsear el fondo
		void parse_svg_fondo(const char* fname);

		// Funcion para parsear el fichero neutro
		void parse_svg_total(const char* fname);

		// Funcion para parsear el resto de ficheros
		void parse_svg_ficheros(const char* fname,int indice);


		void resizePaisaje(renderer_solid ren, renderer_base_pixfmt rb, agg::rendering_buffer rbuf);

		agg::trans_affine transformacionesCara();

		void calculoCoordenadas(renderer_solid ren, renderer_base_pixfmt rb);

		void setCameraPosition(float x, float y, float z);

		float absoluteValue(float v);

		float sign(float x) ;
};

#endif
