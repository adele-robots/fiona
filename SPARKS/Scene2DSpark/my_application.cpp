#include "stdAfx.h"
#include "my_application.h"


//enum { flip_y = false };

	void my_application::initAlphas()
	{
		//Relaciona los sliders con sus atributos. Ademas los inicializa y establece
		//su rango
		for(int i=1;i<numeroDeFicheros;i++)
		{
			add_ctrl(m_alphas_container[i]);
			m_alphas_container[i].range(-1.0,1.0);
			m_alphas_container[i].value(0.0);
		}
	}

	void my_application::parse_svg_prueba(const char* fname)
    {	//objeto de la clase parser
        agg::svg::parser p(m_path_svg);
		//construccion
        p.parse(fname, false);
		//para la ventana...tamaño y titulo
        m_path_svg.arrange_orientations();
        m_path_svg.bounding_rect(&m_min_x, &m_min_y, &m_max_x, &m_max_y);
		//caption(p.title());
		//caption("Analizando ficheros SVG mediante AGG");
    }

	//Parseriza el fichero fname(que sera el neutro) y lo relaciona con el
	//m_path_total(es decir, guarda las coordenadas el fichero en
	//el m_storage de m_path_total
	void my_application::parse_svg_fondo(const char* fname)
    {	//objeto de la clase parser
        agg::svg::parser p(m_path_fondo);
		//construccion
        p.parse(fname, false);
		//para la ventana...tamaño y titulo
        m_path_total.arrange_orientations();
        m_path_total.bounding_rect(&m_min_x, &m_min_y, &m_max_x, &m_max_y);
		//caption(p.title());
		caption("Analizando ficheros SVG mediante AGG");
    }

	//Parseriza el fichero fname(que sera el neutro) y lo relaciona con el
	//m_path_total(es decir, guarda las coordenadas el fichero en
	//el m_storage de m_path_total
	void my_application::parse_svg_total(const char* fname)
    {	//objeto de la clase parser
        agg::svg::parser p(m_path_total);
		//construccion
        p.parse(fname, true);
		//para la ventana...tamaño y titulo
        m_path_total.arrange_orientations();
        m_path_total.bounding_rect(&m_min_x, &m_min_y, &m_max_x, &m_max_y);
		//caption(p.title());
		caption("Analizando ficheros SVG mediante AGG");
    }

	//Parseriza el fichero fname(que seran los morphTargets) y lo relaciona con el
	//m_path_container[i] correspondiente(es decir, guarda las coordenadas el fichero en
	//el m_storage del m_path_container[i]
	void my_application::parse_svg_ficheros(const char* fname,int indice)
	{
		agg::svg::parser p(m_paths_container[indice]);
		p.parse(fname, false);
		//para la ventana...tamaño y titulo
        m_paths_container[indice].arrange_orientations();
        m_paths_container[indice].bounding_rect(&m_min_x, &m_min_y, &m_max_x, &m_max_y);
		//caption(p.title());
		caption("Analizando ficheros SVG mediante AGG");
    }

	void my_application::on_resize(int cx, int cy)
    {

    }

	/*void my_application::on_paint(HDC paintDC)
	{
			m_specific->m_current_dc = paintDC;
            if(m_specific->m_redraw_flag)
            {
                on_draw();
                m_specific->m_redraw_flag = false;
            }
            m_specific->display_pmap(paintDC,&rbuf_window());
            on_post_draw(paintDC);
            m_specific->m_current_dc = 0;

	}*/
	/*void my_application::on_lButtonDown(LPARAM lParam,WPARAM wParam)
	{
			SetCapture(m_specific->m_hwnd);
            m_specific->m_cur_x = int16(LOWORD(lParam));
            if(flip_y())
            {
                m_specific->m_cur_y = rbuf_window().height() - int16(HIWORD(lParam));
            }
            else
            {
                m_specific->m_cur_y = int16(HIWORD(lParam));
            }
            m_specific->m_input_flags = mouse_left | get_key_flags(wParam);

            m_ctrls.set_cur(m_specific->m_cur_x,
                            m_specific->m_cur_y);
            if(m_ctrls.on_mouse_button_down(m_specific->m_cur_x,
                                            m_specific->m_cur_y))
            {
                on_ctrl_change();
                force_redraw();
            }
            else
            {
                if(m_ctrls.in_rect(m_specific->m_cur_x,
                                   m_specific->m_cur_y))
                {
                    if(m_ctrls.set_cur(m_specific->m_cur_x,
                                       m_specific->m_cur_y))
                    {
                        on_ctrl_change();
                        force_redraw();
                    }
                }
                else
                {
                    on_mouse_button_down(m_specific->m_cur_x,
                                         m_specific->m_cur_y,
                                         m_specific->m_input_flags);
                }
            }

//            if(!app->wait_mode())
//            {
//                app->on_idle();
//            }
}*/
	/*void my_application::on_lButtonUp(LPARAM lParam,WPARAM wParam)
	{
			ReleaseCapture();
            m_specific->m_cur_x = int16(LOWORD(lParam));
            if(flip_y())
            {
                m_specific->m_cur_y = rbuf_window().height() - int16(HIWORD(lParam));
            }
            else
            {
                m_specific->m_cur_y = int16(HIWORD(lParam));
            }
            m_specific->m_input_flags = mouse_left | get_key_flags(wParam);

            if(m_ctrls.on_mouse_button_up(m_specific->m_cur_x,
                                               m_specific->m_cur_y))
            {
                on_ctrl_change();
                force_redraw();
            }
            on_mouse_button_up(m_specific->m_cur_x,
                                    m_specific->m_cur_y,
                                    m_specific->m_input_flags);

//            if(!app->wait_mode())
//            {
//                app->on_idle();
//            }

	}*/


	/*void my_application::on_rButtonDown(LPARAM lParam,WPARAM wParam)
	{
            SetCapture(m_specific->m_hwnd);
            m_specific->m_cur_x = int16(LOWORD(lParam));
            if(flip_y())
            {
                m_specific->m_cur_y = rbuf_window().height() - int16(HIWORD(lParam));
            }
            else
            {
                m_specific->m_cur_y = int16(HIWORD(lParam));
            }
            m_specific->m_input_flags = mouse_right | get_key_flags(wParam);
            on_mouse_button_down(m_specific->m_cur_x,
                                 m_specific->m_cur_y,
                                 m_specific->m_input_flags);

//            if(!app->wait_mode())
//            {
//                app->on_idle();
//            }

	}*/

	/*void my_application::on_rButtonUp(LPARAM lParam,WPARAM wParam)
	{
            ReleaseCapture();
            m_specific->m_cur_x = int16(LOWORD(lParam));
            if(flip_y())
            {
                m_specific->m_cur_y = rbuf_window().height() - int16(HIWORD(lParam));
            }
            else
            {
                m_specific->m_cur_y = int16(HIWORD(lParam));
            }
            m_specific->m_input_flags = mouse_right | get_key_flags(wParam);
            on_mouse_button_up(m_specific->m_cur_x,
                               m_specific->m_cur_y,
                               m_specific->m_input_flags);

//            if(!app->wait_mode())
//            {
//                app->on_idle();
//            }

	}*/
	/*void my_application::on_mouseMove(LPARAM lParam,WPARAM wParam)
	{
			m_specific->m_cur_x = int16(LOWORD(lParam));
            if(flip_y())
            {
                m_specific->m_cur_y = rbuf_window().height() - int16(HIWORD(lParam));
            }
            else
            {
                m_specific->m_cur_y = int16(HIWORD(lParam));
            }
            m_specific->m_input_flags = get_key_flags(wParam);


            if(m_ctrls.on_mouse_move(
               m_specific->m_cur_x,
               m_specific->m_cur_y,
                (m_specific->m_input_flags & mouse_left) != 0))
            {
                on_ctrl_change();
                force_redraw();
            }
            else
            {
                if(!m_ctrls.in_rect(m_specific->m_cur_x,
                                    m_specific->m_cur_y))
                {
                    on_mouse_move(m_specific->m_cur_x,
                                  m_specific->m_cur_y,
                                  m_specific->m_input_flags);
                }
            }

//            if(!app->wait_mode())
//            {
//                app->on_idle();
//            }

	}*/
	/*void my_application::on_keyDown(LPARAM lParam,WPARAM wParam)
	{
			m_specific->m_last_translated_key = 0;
            switch(wParam)
            {
                case VK_CONTROL:
                    m_specific->m_input_flags |= kbd_ctrl;
                    break;

                case VK_SHIFT:
                    m_specific->m_input_flags |= kbd_shift;
                    break;

                default:
                    m_specific->translate(wParam);
                    break;
            }

            if(m_specific->m_last_translated_key)
            {
                bool left  = false;
                bool up    = false;
                bool right = false;
                bool down  = false;

                switch(m_specific->m_last_translated_key)
                {
                case key_left:
                    left = true;
                    break;

                case key_up:
                    up = true;
                    break;

                case key_right:
                    right = true;
                    break;

                case key_down:
                    down = true;
                    break;

                case key_f2:
                    copy_window_to_img(agg::platform_support::max_images - 1);
                    save_img(agg::platform_support::max_images - 1, "screenshot");
                    break;
                }

                if(window_flags() & window_process_all_keys)
                {
                    on_key(m_specific->m_cur_x,
                           m_specific->m_cur_y,
                           m_specific->m_last_translated_key,
                           m_specific->m_input_flags);
                }
                else
                {
                    if(m_ctrls.on_arrow_keys(left, right, down, up))
                    {
                        on_ctrl_change();
                        force_redraw();
                    }
                    else
                    {
                        on_key(m_specific->m_cur_x,
                               m_specific->m_cur_y,
                               m_specific->m_last_translated_key,
                               m_specific->m_input_flags);
                    }
                }

//            if(!app->wait_mode())
//            {
//                app->on_idle();
//            }

	}*/
	/*void my_application::on_keyUp(LPARAM lParam,WPARAM wParam)
	{
			m_specific->m_last_translated_key = 0;
            switch(wParam)
            {
                case VK_CONTROL:
                    m_specific->m_input_flags &= ~kbd_ctrl;
                    break;

                case VK_SHIFT:
                    m_specific->m_input_flags &= ~kbd_shift;
                    break;
            }
	}*/
	void my_application::on_draw2()
	{
		//inicializacion de la ventana
        typedef agg::pixfmt_bgra32 pixfmt;
        typedef agg::renderer_base<pixfmt> renderer_base;
        typedef agg::renderer_scanline_aa_solid<renderer_base> renderer_solid;

        pixfmt pixf(rbuf_window());
        renderer_base rb(pixf);
        renderer_solid ren(rb);

        rb.clear(agg::rgba(1,1,1));

        agg::rasterizer_scanline_aa<> ras;
        agg::scanline_p8 sl;

		agg::slider_ctrl<agg::rgba8> m_scale2 (256+5, 5,    512-5, 11,    true);
		add_ctrl(m_scale);
		//m_scale2.label("Scale=%3.2f");
        //m_scale2.range(0.2, 10.0);
        //m_scale2.value(0.5);
		//m_scale2.no_transform();
		//
		//agg::render_ctrl(ras, sl, rb, m_scale2);

/*
		agg::trans_affine mtx;//matriz para guardar las transformaciones sucesivas.
							  //(se usa en trans_affine)
		ras.gamma(agg::gamma_power(m_gamma.value()));
		mtx *= agg::trans_affine_translation((m_min_x + m_max_x) * -0.5, (m_min_y + m_max_y) * -0.5);
        mtx *= agg::trans_affine_scaling(m_scale.value());
        mtx *= agg::trans_affine_rotation(agg::deg2rad(m_rotate.value()));
		mtx *= agg::trans_affine_translation((m_min_x + m_max_x) * 0.5 + m_x, (m_min_y + m_max_y) * 0.5 + m_y + 30);
		*/
		//
		//agg::render_ctrl(ras, sl, rb, m_scale);
		//agg::render_ctrl(ras, sl, rb, m_rotate);

		//m_path_svg.render(ras,sl,ren,mtx,rb.clip_box(),1.0,-1);

		//cout<<"on my_draw2()";
		//force_redraw();

		//cout<<"PINTANDO on_draw2";

	}

	void my_application::on_draw() {

		//inicializacion de la ventana
        typedef agg::pixfmt_bgra32 pixfmt;
        typedef agg::renderer_base<pixfmt> renderer_base;
        typedef agg::renderer_scanline_aa_solid<renderer_base> renderer_solid;

        pixfmt pixf(rbuf_window());
        renderer_base rb(pixf);
        renderer_solid ren(rb);

        rb.clear(agg::rgba(1,1,1));

        agg::rasterizer_scanline_aa<> ras;
        agg::scanline_p8 sl;

		agg::slider_ctrl<agg::rgba8> m_scale2 (256+5, 5,    512-5, 11,    true);
		add_ctrl(m_scale);
		m_scale2.label("Scale=%3.2f");
        m_scale2.range(0.2, 10.0);
        //m_scale2.value(0.5);
		//m_scale2.no_transform();

        //m_scale2.cambioLugar(5, 5,    256-5, 11,    true);
		//agg::render_ctrl(ras, sl, rb, m_scale2);

		//RESIZE PARA EL PAISAJE
		double valueAux=0.0;
		double valueAux2=0.0;
		width();
        height();
        initial_width();
        initial_height();
		valueAux=width()/initial_width();
		valueAux2=height()/initial_height();
		agg::trans_affine mtx2;
		mtx2 *= agg::trans_affine_scaling(valueAux,1.0);
		mtx2 *= agg::trans_affine_scaling(1.0,valueAux2);


		//TRANSFORMACIONES DE LA CARA
        agg::trans_affine mtx;//matriz para guardar las transformaciones sucesivas.
							  //(se usa en trans_affine)
		ras.gamma(agg::gamma_power(m_gamma.value()));
		mtx *= agg::trans_affine_translation((m_min_x + m_max_x) * -0.5, (m_min_y + m_max_y) * -0.5);
        mtx *= agg::trans_affine_scaling(m_scale.value());
        mtx *= agg::trans_affine_rotation(agg::deg2rad(m_rotate.value()));
		mtx *= agg::trans_affine_translation((m_min_x + m_max_x) * 0.5 + m_x, (m_min_y + m_max_y) * 0.5 + m_y + 30);

		unsigned num_ver = m_paths_container[0].total_vertices();

		start_timer();

		for(unsigned idx = 0; idx < num_ver; idx++) {
			//PUNTOS INICIALES
			m_paths_container[0].vertex(idx,&x_0,&y_0);

			//formula del sumatorio
			//en i=0 guarda los vertices del fichero neutro(en i=0 NO SE UTILIZAN ALPHA)
			//en i>=1 hace el sumatorio teniendo en cuenta la formula
			for(int i=0;i<numeroDeFicheros;i++)	{
				if(i==0){
					x_sum=x_0;
					y_sum=y_0;
				}
				else{
					m_paths_container[i].vertex(idx,&x_i,&y_i);
					x_sum=((m_alphas_container[i].value())*(x_i-x_0))+x_sum;
					y_sum=((m_alphas_container[i].value())*(y_i-y_0))+y_sum;
				}
			}//fin de for segundo

			//coordenadas del path resultante
			x_f=x_sum;
			y_f=y_sum;
			//guardo las coordenadas en el path que se pintara en la ventana
			m_path_total.modify_vertex(idx, x_f, y_f);

	//prueba para 3 ficheros---------------------------------------------------------------
	//m_paths_container[1].vertex(idx,&x1,&y1);
	//m_paths_container[2].vertex(idx,&x2,&y2);
	//x_f=x_0+(m_alphas_container[1].value())*(x1-x_0)+(m_alphas_container[2].value())*(x2-x_0);
	//y_f=y_0+(m_alphas_container[1].value())*(y1-y_0)+(m_alphas_container[2].value())*(y2-y_0);
	//, comando_utilizado);

		}//fin de for primero


			//m_path_fondo.render(ras, sl, ren, mtx2, rb.clip_box(), 1.0,-1);
		//PINTA EL FICHERO EN LA VENTANA
		//pintamos solo las partes de la cara que estan "chequeadas"
		for(int i=0;i<(int16_t)(m_paths_container[0].paths_number());i++)
			if(paths_id_container[i].status())
				m_path_total.render(ras, sl, ren, mtx, rb.clip_box(), 1.0,i);

		//double tm = elapsed_time();
		//dibuja los sliders menos los de alpha
        ras.gamma(agg::gamma_none());
        agg::render_ctrl(ras, sl, rb, m_expand);
        agg::render_ctrl(ras, sl, rb, m_gamma);
        agg::render_ctrl(ras, sl, rb, m_scale);
        agg::render_ctrl(ras, sl, rb, m_rotate);

		//dibuja los sliders de los alphas
		char namelabel[63]="Alpha";
		for(int i=1;i<numeroDeFicheros;i++) 	{
			strcpy(namelabel,m_files_container[i].nombreMorphTarget);
			strcat(namelabel,"=%3.2f");
			m_alphas_container[i].label(namelabel);
			agg::render_ctrl(ras,sl,rb,m_alphas_container[i]);
		}

		//dibuja los check_box
		for(unsigned i=0;i<m_paths_container[0].paths_number();i++) {
			add_ctrl(paths_id_container[i]);
			char* aux=new char [id_path_names_queue[i].length()+1];
			id_path_names_queue[i].copy(aux,id_path_names_queue[i].length(),0);
			aux[id_path_names_queue[i].length()]=0;
			paths_id_container[i].label(aux);
			delete[]aux;
			agg::render_ctrl(ras, sl, rb, paths_id_container[i]);
		}

/*
		 char buf[128];
        agg::gsv_text t;
        t.size(10.0);
        t.flip(true);

        agg::conv_stroke<agg::gsv_text> pt(t);
        pt.width(1.5);

        sprintf(buf,"Time=%.3f ms",tm);

		cout<<"\n Time: "<<tm<<" ms";

        t.start_point(60.0, 40.0);
        t.text(buf);

        ras.add_path(pt);
        ren.color(agg::rgba(0,0,0));
        agg::render_scanlines(ras, sl, ren);
        */
cout<<"on_paint()\n";
        //agg::gamma_lut<> gl(m_gamma.value());
        //unsigned x, y;
        //unsigned w = unsigned(width());
        //unsigned h = unsigned(height());
        //for(y = 0; y < h; y++)
        //{
        //    for(x = 0; x < w; x++)
        //    {
        //        agg::rgba8 c = rb.pixel(x, y);
        //        c.r = gl.inv(c.r);
        //        c.g = gl.inv(c.g);
        //        c.b = gl.inv(c.b);
        //        rb.copy_pixel(x, y, c);
        //    }
        //}
    }

	void my_application::on_draw_gradient()
    {
        agg::rasterizer_scanline_aa<> ras;

        typedef agg::pixfmt_bgra32 pixfmt;
        typedef agg::renderer_base<pixfmt> renderer_base;
        agg::scanline_u8 sl;

        pixfmt pixf(rbuf_window());
        renderer_base rb(pixf);
        rb.clear(agg::rgba(0, 0, 0));

        m_profile.text_size(8.0);

        agg::render_ctrl(ras, sl, rb, m_profile);
        agg::render_ctrl(ras, sl, rb, m_spline_r);
        agg::render_ctrl(ras, sl, rb, m_spline_g);
        agg::render_ctrl(ras, sl, rb, m_spline_b);
        agg::render_ctrl(ras, sl, rb, m_spline_a);
        agg::render_ctrl(ras, sl, rb, m_rbox);

        double ini_scale = 1.0;
        const double center_x = 350;
        const double center_y = 280;

        agg::trans_affine mtx1;
        mtx1 *= agg::trans_affine_scaling(ini_scale, ini_scale);
        mtx1 *= agg::trans_affine_rotation(agg::deg2rad(0.0));
        mtx1 *= agg::trans_affine_translation(center_x, center_y);
        mtx1 *= trans_affine_resizing();

        agg::ellipse e1;
        e1.init(0.0, 0.0, 110.0, 110.0, 64);

        agg::trans_affine mtx_g1;
        mtx_g1 *= agg::trans_affine_scaling(ini_scale, ini_scale);
        mtx_g1 *= agg::trans_affine_scaling(m_scale_grad, m_scale_grad);
        mtx_g1 *= agg::trans_affine_scaling(m_scale_grad, m_scale_grad);
        mtx_g1 *= agg::trans_affine_rotation(m_angle_grad);
        mtx_g1 *= agg::trans_affine_translation(m_center_x_grad, m_center_y_grad);
        mtx_g1 *= trans_affine_resizing();
        mtx_g1.invert();


        color_type color_profile[256]; // color_type is defined in pixel_formats.h
        int i;
        for(i = 0; i < 256; i++)
        {
            color_profile[i] = color_type(agg::rgba(m_spline_r.spline()[i],
                                                    m_spline_g.spline()[i],
                                                    m_spline_b.spline()[i],
                                                    m_spline_a.spline()[i]));
        }

        agg::conv_transform<agg::ellipse, agg::trans_affine> t1(e1, mtx1);

        gradient_polymorphic_wrapper<agg::gradient_radial>       gr_circle;
        gradient_polymorphic_wrapper<agg::gradient_diamond>      gr_diamond;
        gradient_polymorphic_wrapper<agg::gradient_x>            gr_x;
        gradient_polymorphic_wrapper<agg::gradient_xy>           gr_xy;
        gradient_polymorphic_wrapper<agg::gradient_sqrt_xy>      gr_sqrt_xy;
        gradient_polymorphic_wrapper<agg::gradient_conic>        gr_conic;

        gradient_polymorphic_wrapper_base* gr_ptr = &gr_circle;

//        gr_circle.m_gradient.init(150, 80, 80);

        switch(m_rbox.cur_item())
        {
            case 1: gr_ptr = &gr_diamond; break;
            case 2: gr_ptr = &gr_x;       break;
            case 3: gr_ptr = &gr_xy;      break;
            case 4: gr_ptr = &gr_sqrt_xy; break;
            case 5: gr_ptr = &gr_conic;   break;
        }

        typedef agg::span_interpolator_linear<> interpolator_type;
        typedef agg::span_gradient<color_type,
                                   interpolator_type,
                                   gradient_polymorphic_wrapper_base,
                                   color_function_profile> gradient_span_gen;
        typedef agg::span_allocator<gradient_span_gen::color_type> gradient_span_alloc;

        gradient_span_alloc    span_alloc;
        color_function_profile colors(color_profile, m_profile.gamma());
        interpolator_type      inter(mtx_g1);
        gradient_span_gen      span_gen(inter, *gr_ptr, colors, 0, 150);

        ras.add_path(t1);
        agg::render_scanlines_aa(ras, sl, rb, span_alloc, span_gen);
    }

	void my_application::on_draw_blur()
	    {
	        typedef agg::renderer_base<agg::pixfmt_bgr24> ren_base;
	        typedef agg::conv_curve<agg::path_storage> shape_type;
	        agg::rasterizer_scanline_aa<> m_ras;
	        agg::scanline_p8              m_sl;
	        agg::rendering_buffer         m_rbuf2;
	        agg::rect_d m_shape_bounds;
	        agg::rbox_ctrl<agg::rgba8>    m_method(10.0, 10.0, 130.0, 70.0, false);
	        agg::slider_ctrl<agg::rgba8>  m_radius;//(130 + 10.0, 10.0 + 4.0, 130 + 300.0, 10.0 + 8.0 + 4.0, false);
	        agg::polygon_ctrl<agg::rgba8> m_shadow_ctrl(4);
	        agg::cbox_ctrl<agg::rgba8>    m_channel_r;
	        agg::cbox_ctrl<agg::rgba8>    m_channel_g;
	        agg::cbox_ctrl<agg::rgba8>    m_channel_b;
	        agg::path_storage             m_path;
	        shape_type                    m_shape(m_path);
	        agg::recursive_blur<agg::rgba8, agg::recursive_blur_calc_rgb<> > m_recursive_blur;

	        agg::pixfmt_bgr24 pixf(rbuf_window());
	        ren_base renb(pixf);
	        renb.clear(agg::rgba(1, 1, 1));
	        m_ras.clip_box(0,0, width(), height());

	        agg::trans_perspective shadow_persp(m_shape_bounds.x1, m_shape_bounds.y1,
	                                            m_shape_bounds.x2, m_shape_bounds.y2,
	                                            m_shadow_ctrl.polygon());

	        agg::conv_transform<shape_type,
	                            agg::trans_perspective> shadow_trans(m_shape,
	                                                                 shadow_persp);

	        // Render shadow
	        m_ras.add_path(shadow_trans);
	        agg::render_scanlines_aa_solid(m_ras, m_sl, renb, agg::rgba(0.2,0.3,0));

	        // Calculate the bounding box and extend it by the blur radius
	        agg::rect_d bbox;
	        agg::bounding_rect_single(shadow_trans, 0, &bbox.x1, &bbox.y1, &bbox.x2, &bbox.y2);

	        bbox.x1 -= m_radius.value();
	        bbox.y1 -= m_radius.value();
	        bbox.x2 += m_radius.value();
	        bbox.y2 += m_radius.value();

	        if(m_method.cur_item() == 1)
	        {
	            // The recursive blur method represents the true Gussian Blur,
	            // with theoretically infinite kernel. The restricted window size
	            // results in extra influence of edge pixels. It's impossible to
	            // solve correctly, but extending the right and top areas to another
	            // radius value produces fair result.
	            //------------------
	            bbox.x2 += m_radius.value();
	            bbox.y2 += m_radius.value();
	        }

	        start_timer();
	        if(m_method.cur_item() != 2)
	        {
	            // Create a new pixel renderer and attach it to the main one as a child image.
	            // It returns true if the attachment suceeded. It fails if the rectangle
	            // (bbox) is fully clipped.
	            //------------------
	            agg::pixfmt_bgr24 pixf2(m_rbuf2);
	            if(pixf2.attach(pixf, int(bbox.x1), int(bbox.y1), int(bbox.x2), int(bbox.y2)))
	            {
	                // Blur it
	                if(m_method.cur_item() == 0)
	                {
	                    // More general method, but 30-40% slower.
	                    //------------------
	                    //m_stack_blur.blur(pixf2, agg::uround(m_radius.value()));

	                    // Faster, but bore specific.
	                    // Works only for 8 bits per channel and only with radii <= 254.
	                    //------------------
	                    agg::stack_blur_rgb24(pixf2, agg::uround(m_radius.value()),
	                                                 agg::uround(m_radius.value()));
	                }
	                else
	                {
	                    // True Gaussian Blur, 3-5 times slower than Stack Blur,
	                    // but still constant time of radius. Very sensitive
	                    // to precision, doubles are must here.
	                    //------------------
	                    m_recursive_blur.blur(pixf2, m_radius.value());
	                }
	            }
	        }
	        else
	        {
	            // Blur separate channels
	            //------------------
	            if(m_channel_r.status())
	            {
	                typedef agg::pixfmt_alpha_blend_gray<
	                    agg::blender_gray8,
	                    agg::rendering_buffer,
	                    3, 2> pixfmt_gray8r;

	                pixfmt_gray8r pixf2r(m_rbuf2);
	                if(pixf2r.attach(pixf, int(bbox.x1), int(bbox.y1), int(bbox.x2), int(bbox.y2)))
	                {
	                    agg::stack_blur_gray8(pixf2r, agg::uround(m_radius.value()),
	                                                  agg::uround(m_radius.value()));
	                }
	            }

	            if(m_channel_g.status())
	            {
	                typedef agg::pixfmt_alpha_blend_gray<
	                    agg::blender_gray8,
	                    agg::rendering_buffer,
	                    3, 1> pixfmt_gray8g;

	                pixfmt_gray8g pixf2g(m_rbuf2);
	                if(pixf2g.attach(pixf, int(bbox.x1), int(bbox.y1), int(bbox.x2), int(bbox.y2)))
	                {
	                    agg::stack_blur_gray8(pixf2g, agg::uround(m_radius.value()),
	                                                  agg::uround(m_radius.value()));
	                }
	            }

	            if(m_channel_b.status())
	            {
	                typedef agg::pixfmt_alpha_blend_gray<
	                    agg::blender_gray8,
	                    agg::rendering_buffer,
	                    3, 0> pixfmt_gray8b;

	                pixfmt_gray8b pixf2b(m_rbuf2);
	                if(pixf2b.attach(pixf, int(bbox.x1), int(bbox.y1), int(bbox.x2), int(bbox.y2)))
	                {
	                    agg::stack_blur_gray8(pixf2b, agg::uround(m_radius.value()),
	                                                  agg::uround(m_radius.value()));
	                }
	            }
	        }
	        double tm = elapsed_time();

	        agg::render_ctrl(m_ras, m_sl, renb, m_shadow_ctrl);

	        // Render the shape itself
	        //------------------
	        m_ras.add_path(m_shape);
	        agg::render_scanlines_aa_solid(m_ras, m_sl, renb, agg::rgba(0.6,0.9,0.7, 0.8));

	        char buf[64];
	        agg::gsv_text t;
	        t.size(10.0);

	        agg::conv_stroke<agg::gsv_text> st(t);
	        st.width(1.5);

	        sprintf(buf, "%3.2f ms", tm);
	        t.start_point(140.0, 30.0);
	        t.text(buf);

	        m_ras.add_path(st);
	        agg::render_scanlines_aa_solid(m_ras, m_sl, renb, agg::rgba(0,0,0));


	        agg::render_ctrl(m_ras, m_sl, renb, m_method);
	        agg::render_ctrl(m_ras, m_sl, renb, m_radius);
	        agg::render_ctrl(m_ras, m_sl, renb, m_channel_r);
	        agg::render_ctrl(m_ras, m_sl, renb, m_channel_g);
	        agg::render_ctrl(m_ras, m_sl, renb, m_channel_b);
	    }

	void my_application::on_mouse_button_down(int x, int y, unsigned flags) {
        m_dx = x - m_x;
        m_dy = y - m_y;
        m_drag_flag = true;
    }

	void my_application::on_mouse_move(int x, int y, unsigned flags) {
        if(flags == 0) {
            m_drag_flag = false;
        }

        if(m_drag_flag) {
            m_x = x - m_dx;
            m_y = y - m_dy;
            force_redraw();
        }
    }

	void my_application::on_mouse_button_up(int x, int y, unsigned flags) {
        m_drag_flag = false;
    }

	void my_application::on_key(int x, int y, unsigned key, unsigned flags) {
        if(key == ' ') {

            agg::trans_affine mtx;
            mtx *= agg::trans_affine_translation((m_min_x + m_max_x) * -0.5, (m_min_y + m_max_y) * -0.5);
            mtx *= agg::trans_affine_scaling(m_scale.value());
            mtx *= agg::trans_affine_rotation(agg::deg2rad(m_rotate.value()));
			mtx *= agg::trans_affine_translation((m_min_x + m_max_x) * 0.5, (m_min_y + m_max_y) * 0.5);
            mtx *= agg::trans_affine_translation(m_x, m_y);


            double m[6];
            mtx.store_to(m);

            char buf[128];
            sprintf(buf, "%3.3f, %3.3f, %3.3f, %3.3f, %3.3f, %3.3f",
                         m[0], m[1], m[2], m[3], m[4], m[5]);

            message(buf);
            FILE* fd = fopen(full_file_name("transform.txt"), "a");
            fprintf(fd, "%s\n", buf);
            fclose(fd);
        }
    }

