#ifndef MY_APPLICATION_H
#define MY_APPLICATION_H

#include <stdio.h>
#include <stdlib.h>

#include "agg_basics.h"
#include "agg_rendering_buffer.h"
#include "agg_rasterizer_scanline_aa.h"
#include "agg_scanline_p.h"
#include "agg_renderer_scanline.h"
#include "agg_pixfmt_rgba.h"
#include "agg_svg_parser.h"
#include "agg_path_storage.h"
#include "agg_svg_path_tokenizer.h"
#include "agg_scanline_u.h"
#include "ctrl/agg_rbox_ctrl.h"
#include "ctrl/agg_spline_ctrl.h"
#include "ctrl/agg_gamma_ctrl.h"
#define AGG_RGB24
#include "pixel_formats.h"
#include "agg_span_gradient.h"
#include "agg_span_interpolator_linear.h"
#include "agg_span_allocator.h"
#include "agg_trans_perspective.h"
#include "ctrl/agg_polygon_ctrl.h"
#include "agg_blur.h"
#include "agg_pixfmt_gray.h"
#include "agg_image_filters.h"

#include "platform/agg_platform_support.h"

#ifdef _WIN32
#include <windows.h>
#else
#endif




#include <string>

#include <iostream>
#include <iomanip>
#include <fstream>
#include <cstdlib>

#include "Configuration.h"
//#include "queue.h"

#include "ctrl/agg_cbox_ctrl.h"
#include "ctrl/agg_slider_ctrl.h"
#include "ContenedorDeFicheros.h"


using namespace std;
#ifdef _WIN32
using namespace libconfig;
#else
#endif

using namespace agg;

//la clase aplicacion deriva de la clase platform_support(definida en namespace agg)
class my_application : public agg::platform_support
{
	//para trabajar con ficheros .svg(creacion de objetos)
	//clase_m_paths *ptrTom_paths = new clase_m_paths[*N1];
    
	 agg::svg::path_renderer m_path_total;
	 agg::svg::path_renderer m_path_fondo;


	//para los sliders (en este caso 4 sliders)(creacion de objetos)
    agg::slider_ctrl<agg::rgba8> m_expand;
    agg::slider_ctrl<agg::rgba8> m_gamma;
    agg::slider_ctrl<agg::rgba8> m_scale;
    agg::slider_ctrl<agg::rgba8> m_rotate;

    // Para gradientes
    agg::gamma_ctrl<agg::rgba8>  m_profile;
    agg::spline_ctrl<agg::rgba8> m_spline_r;
    agg::spline_ctrl<agg::rgba8> m_spline_g;
    agg::spline_ctrl<agg::rgba8> m_spline_b;
    agg::spline_ctrl<agg::rgba8> m_spline_a;
    agg::rbox_ctrl<agg::rgba8>   m_rbox;
    double m_scale_grad;
    double m_scale_x_grad;
    double m_scale_y_grad;
    double m_center_x_grad;
    double m_center_y_grad;
    double m_angle_grad;

	
  
	double m_min_x;
    double m_min_y;
    double m_max_x;
    double m_max_y;

    double m_x;
    double m_y;
    double m_dx;
    double m_dy;
    bool   m_drag_flag;

	//MODIFICADO
	//variables auxiliares donde se guardan los vertices
		double x_0;
		double y_0;
		double x_f;
		double y_f;
		double x_i;
		double y_i;
		double x_sum;
		double y_sum;
	public:
		int numeroDeFicheros;
		int numeroDePath;
		agg::svg::path_renderer m_path_svg;

		//estructura donde guardamos los nombres de los ficheros y los MorphTarget
		/*struct ContenedorDeFicheros {
			char nombreMorphTarget[100];
			char nombreFichero[100];
		};*/

		//Vector dinamico de objetos de tipo ContenedorDeFicheros
		ContenedorDeFicheros *m_files_container;
		//Vector dinamico de objetos donde se guardan los objetosm_paths
			agg::svg::path_renderer *m_paths_container;
		//Vector dinamico de objetos donde se guardan los objetos m_alphas
		//cada alpha corresponde con un fichero distinto
			agg::slider_ctrl<agg::rgba8> *m_alphas_container;

		//Vector dinamico de objetos donde se guardan los objetos que pintan los checkBox
		//cada checkBox corresponde con una parte de la cara (identificador de path)
			agg::cbox_ctrl<agg::rgba8> *paths_id_container;
		//Vector dinamico de objetos string donde guardamos los nombres de los identificadores
		//de los paths(para las etiquetas de los 'checkbox')
		string *id_path_names_queue;
#ifdef _WIN32
private:
	static LRESULT CALLBACK WindowComponent::WndProc(
		HWND	hWnd,
		UINT	uMsg,
		WPARAM	wParam,
		LPARAM	lParam
	);
#else
#endif
public:
	
	//my_application::my_application():
		my_application():agg::platform_support(agg::pix_format_bgra32, false),
        m_expand(5,     5,    256-5, 11,true),
        m_gamma (5,     5+15, 256-5, 11+15,true),
        m_scale (256+5, 5,    512-5, 11,true),
        m_rotate(256+5, 5+15, 512-5, 11+15,true),
		
		//m_alpha(5,5+30,256-5,11+30), YA NO SE INICIALIZAN LOS ALPHAS AQUI!!!
		m_min_x(0.0),
        m_min_y(0.0),
        m_max_x(0.0),
        m_max_y(0.0),
        m_x(0.0),
        m_y(0.0),
        m_dx(0.0),
        m_dy(0.0),
        m_drag_flag(false),

        // Gradientes
        m_profile(10.0, 10.0, 200.0, 170.0-5.0, false),
        m_spline_r(210, 10,     210+250, 5+40,  6, false),
        m_spline_g(210, 10+40,  210+250, 5+80,  6, false),
        m_spline_b(210, 10+80,  210+250, 5+120, 6, false),
        m_spline_a(210, 10+120, 210+250, 5+160, 6, false),
        m_rbox(10.0, 180.0, 200.0, 300.0, false),
        m_scale_grad(1.0),
        m_scale_x_grad(1.0),
        m_scale_y_grad(1.0),
        m_center_x_grad(1.0),
        m_center_y_grad(1.0),
        m_angle_grad(0)
    {
		//cout<<"CONSTRUCTOR_VACIO...";
		//Relaciona cada slider con su atributo
        add_ctrl(m_expand);
        add_ctrl(m_gamma);
        add_ctrl(m_scale);
        add_ctrl(m_rotate);
		
        m_expand.label("Expand=%3.2f");
        m_expand.range(-1, 1.2);
        m_expand.value(0.0);

        m_gamma.label("Gamma=%3.2f");
        m_gamma.range(0.0, 3.0);
        m_gamma.value(1.0);

        m_scale.label("Scale=%3.2f");
        m_scale.range(0.2, 10.0);
        m_scale.value(0.5);

        m_rotate.label("Rotate=%3.2f");
        m_rotate.range(-180.0, 180.0);
        m_rotate.value(0.0);

    }
	//my_application::my_application(agg::pix_format_e format, bool flip_y):
		my_application(agg::pix_format_e format, bool flip_y):
			agg::platform_support(format, flip_y),
			m_expand(5,     5,    256-5, 11,    !flip_y),
			m_gamma (5,     5+15, 256-5, 11+15, !flip_y),
			m_scale (256+5, 5,    512-5, 11,    !flip_y),
			m_rotate(256+5, 5+15, 512-5, 11+15, !flip_y),
		
		//m_alpha(5,5+30,256-5,11+30), YA NO SE INICIALIZAN LOS ALPHAS AQUI!!!
			m_min_x(0.0),
			m_min_y(0.0),
			m_max_x(0.0),
			m_max_y(0.0),
			m_x(0.0),
			m_y(0.0),
			m_dx(0.0),
			m_dy(0.0),
			m_drag_flag(false),

	        // Gradientes
	        m_profile(10.0, 10.0, 200.0, 170.0-5.0, !flip_y),
	        m_spline_r(210, 10,     210+250, 5+40,  6, !flip_y),
	        m_spline_g(210, 10+40,  210+250, 5+80,  6, !flip_y),
	        m_spline_b(210, 10+80,  210+250, 5+120, 6, !flip_y),
	        m_spline_a(210, 10+120, 210+250, 5+160, 6, !flip_y),
	        m_rbox(10.0, 180.0, 200.0, 300.0, !flip_y),
	        m_scale_grad(1.0),
	        m_scale_x_grad(1.0),
	        m_scale_y_grad(1.0),
	        m_center_x_grad(1.0),
	        m_center_y_grad(1.0),
	        m_angle_grad(0)
			{
				printf("CONSTRUCTOR_NORMAL \n");

				//Relaciona cada slider con su atributo
				add_ctrl(m_expand);
				add_ctrl(m_gamma);
				add_ctrl(m_scale);
				add_ctrl(m_rotate);

				m_expand.label("Expand=%3.2f");
				m_expand.range(-1, 1.2);
				m_expand.value(0.0);

				m_gamma.label("Gamma=%3.2f");
				m_gamma.range(0.0, 3.0);
				m_gamma.value(1.0);

				m_scale.label("Scale=%3.2f");
				m_scale.range(0.2, 10.0);
				m_scale.value(0.5);
		
				m_rotate.label("Rotate=%3.2f");
				m_rotate.range(-180.0, 180.0);
				m_rotate.value(0.0);
			}


	void initAlphas();
	void parse_svg_prueba(const char* fname);
	void parse_svg_fondo(const char* fname);
	void parse_svg_total(const char* fname);
	void parse_svg_ficheros(const char* fname,int indice);
	
	virtual void on_resize(int cx, int cy);
#ifdef _WIN32
	//virtual void my_application::on_paint(HDC paintDC);
	virtual void on_paint(HDC paintDC);
	virtual void on_lButtonDown(LPARAM lParam,WPARAM wParam);
	virtual void on_lButtonUp(LPARAM lParam,WPARAM wParam);
	virtual void on_rButtonDown(LPARAM lParam,WPARAM wParam);
	virtual void on_rButtonUp(LPARAM lParam,WPARAM wParam);
	virtual void on_mouseMove(LPARAM lParam,WPARAM wParam);
	virtual void on_keyDown(LPARAM lParam,WPARAM wParam);
	virtual void on_keyUp(LPARAM lParam,WPARAM wParam);
#else
#endif

	virtual void on_draw2();
	virtual void on_draw();
	virtual void on_draw_gradient();
	virtual void on_draw_blur();
	virtual void on_mouse_button_down(int x, int y, unsigned flags);
	virtual void on_mouse_move(int x, int y, unsigned flags);
	virtual void on_mouse_button_up(int x, int y, unsigned flags);
	virtual void on_key(int x, int y, unsigned key, unsigned flags);

};

class gradient_polymorphic_wrapper_base
{
public:
    virtual int calculate(int x, int y, int) const = 0;
};

template<class GradientF>
class gradient_polymorphic_wrapper : public gradient_polymorphic_wrapper_base
{
public:
    gradient_polymorphic_wrapper() : m_adaptor(m_gradient) {}

    virtual int calculate(int x, int y, int d) const
    {
        return m_adaptor.calculate(x, y, d);
    }
    GradientF m_gradient;
    agg::gradient_reflect_adaptor<GradientF> m_adaptor;
};



struct color_function_profile
{
    color_function_profile() {}
    color_function_profile(const color_type* colors, const agg::int8u* profile) :
        m_colors(colors), m_profile(profile) {}

    static unsigned size() { return 256; }
    const color_type& operator [] (unsigned v) const
    {
        return m_colors[m_profile[v]];
    }

    const color_type* m_colors;
    const agg::int8u* m_profile;
};

#endif

