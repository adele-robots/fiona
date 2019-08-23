#include "stdAfx.h"
#include "SvgGeneral.h"

// Función que relaciona los sliders con sus atributos.
// Ademas los inicializa y establece su rango
void SvgGeneral::initAlphas() {
	printf("SvgGeneral::initAlphas\n");
	for(int i=1;i<numeroDeFicheros;i++) 	{
		//add_ctrl(m_alphas_container[i]);
		//m_alphas_container[i].range(-1.0,1.0);
		//m_alphas_container[i].value(0.0);
		m_alphas_container[i]=0.0;
	}
}

/*void SvgGeneral::parse_svg(const char* fname, agg::svg::path_renderer name_render){
	//objeto de la clase parser
	    agg::svg::parser p(name_render);

		//construccion
	    p.parse(fname);

		//para la ventana...tamaño y titulo
	    name_render.arrange_orientations();
	    name_render.bounding_rect(&m_min_x, &m_min_y, &m_max_x, &m_max_y);
}*/

// Parseriza el fichero fname(que sera el fondo) y lo relaciona con el
// m_path_total(es decir, guarda las coordenadas el fichero en
// el m_storage de m_path_total
void SvgGeneral::parse_svg_fondo(const char* fname) {
	printf("SvgGeneral::parse_svg_fondo\n");
	//objeto de la clase parser
    agg::svg::parser p(m_path_fondo);

	//construccion
    p.parse(fname, false);

	//para la ventana...tamaño y titulo
    m_path_total.arrange_orientations();
    m_path_total.bounding_rect(&m_min_x, &m_min_y, &m_max_x, &m_max_y);

    //m_fondo_filters = p.m_filters;
}

// Parseriza el fichero fname(que sera el neutro) y lo relaciona con el
// m_path_total(es decir, guarda las coordenadas el fichero en
// el m_storage de m_path_total
void SvgGeneral::parse_svg_total(const char* fname) {
	printf("SvgGeneral::parse_svg_total\n");
	//objeto de la clase parser
    agg::svg::parser p(m_path_total);

	//construccion
    p.parse(fname, true);

	//para la ventana...tamaño y titulo
    m_path_total.arrange_orientations();
    m_path_total.bounding_rect(&m_min_x, &m_min_y, &m_max_x, &m_max_y);

    //m_neutro_filters = p.m_filters;
}

// Parseriza el fichero fname(que seran los morphTargets) y lo relaciona con el
// m_path_container[i] correspondiente(es decir, guarda las coordenadas el fichero en
// el m_storage del m_path_container[i]
void SvgGeneral::parse_svg_ficheros(const char* fname,int indice) {
	printf("SvgGeneral::parse_svg_ficheros\n");
	//objeto de la clase parser
	agg::svg::parser p(m_paths_container[indice]);

	//construccion
	p.parse(fname, false);

	//para la ventana...tamaño y titulo
	m_paths_container[indice].arrange_orientations();
	m_paths_container[indice].bounding_rect(&m_min_x, &m_min_y, &m_max_x, &m_max_y);

    //m_ficheros_filters = p.m_filters;
}

void SvgGeneral::resizePaisaje(renderer_solid ren, renderer_base_pixfmt rb, agg::rendering_buffer rbuf){
	//printf("SvgGeneral::resizePaisaje\n");
	double valueAux=0.0;
	double valueAux2=0.0;

	valueAux=rbuf.width()/initial_width();
	valueAux2=rbuf.height()/initial_height();

	agg::trans_affine mtx2;
	mtx2 *= agg::trans_affine_scaling(valueAux,1.0);
	mtx2 *= agg::trans_affine_scaling(1.0,valueAux2);

	// Ojo la linea siguiente Pablo la tenia comentada pero es que sino
	// ¿Cuando hace el render del fondo?
	m_path_fondo.render(ras, sl, ren, mtx2, rb.clip_box(), 1.0,-1);

}

agg::trans_affine SvgGeneral::transformacionesCara(){
	//printf("SvgGeneral::transformacionesCara\n");
	agg::trans_affine mtx;

	ras.gamma(agg::gamma_power(m_gamma));
	mtx *= agg::trans_affine_translation((m_min_x + m_max_x) * -0.5, (m_min_y + m_max_y) * -0.5);
	mtx *= agg::trans_affine_scaling(m_scale);
	mtx *= agg::trans_affine_rotation(agg::deg2rad(m_rotate));

	// Lo que tenia Pablo
	//mtx *= agg::trans_affine_translation((m_min_x + m_max_x) * 0.5 + m_x, (m_min_y + m_max_y) * 0.5 + m_y + 30);

	mtx *= agg::trans_affine_translation((m_min_x + m_max_x) * 0.5, (m_min_y + m_max_y) * 0.5);
	mtx *= agg::trans_affine_translation(m_x, m_y);
	mtx *= agg::trans_affine_translation(cameraX, cameraY);
	return mtx;
}

void SvgGeneral::calculoCoordenadas(renderer_solid ren, renderer_base_pixfmt rb){
	//printf("SvgGeneral::calculoCoordenadas\n");
	numeroDeVertices = m_paths_container[0].total_vertices();
	//start_timer();

	for(unsigned idx = 0; idx < numeroDeVertices; idx++) {
		//printf("SvgGeneral::numeroDeVertices %d\n", idx);
		// Puntos iniciales
		m_paths_container[0].vertex(idx,&x_0,&y_0);

		// Formula del sumatorio
		// En i=0 guarda los vertices del fichero neutro (en i=0 no se utilizan alpha)
		// En i>=1 hace el sumatorio teniendo en cuenta la formula
		for(int i=0;i<numeroDeFicheros;i++) 	{
			//printf("SvgGeneral::numeroDeFicheros %d\n", i);
			if(i==0) {
				x_sum=x_0;
				y_sum=y_0;
			}
			else {
				m_paths_container[i].vertex(idx,&x_i,&y_i);
				x_sum=((m_alphas_container[i])*(x_i-x_0))+x_sum;
				y_sum=((m_alphas_container[i])*(y_i-y_0))+y_sum;
			}
		}//fin de for segundo

		//coordenadas del path resultante
		x_f=x_sum;
		y_f=y_sum;

		//guardo las coordenadas en el path que se pintara en la ventana
		m_path_total.modify_vertex(idx, x_f, y_f);
	}

	for(int i=0;i<(static_cast<unsigned int>(m_paths_container[0].paths_number()));i++)
		m_path_total.render(ras, sl, ren, mtx, rb.clip_box(), 1.0,i);
}

void SvgGeneral::setCameraPosition(float x, float y, float z) {
	cameraX = x;
	cameraY = y;
	m_scale = z;
}

float SvgGeneral::absoluteValue(float v) {
	return v > 0 ? v : -v;
}

float SvgGeneral::sign(float x) {
	return x >= 0 ? 1.0f : -1.0f;
}

