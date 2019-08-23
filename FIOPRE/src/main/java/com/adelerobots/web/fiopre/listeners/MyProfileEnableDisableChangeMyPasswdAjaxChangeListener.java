package com.adelerobots.web.fiopre.listeners;

import com.treelogic.fawna.presentacion.componentes.event.api.GestorDatosComponentes;
import com.treelogic.fawna.presentacion.componentes.event.api.GestorEstadoComponentes;
import com.treelogic.fawna.presentacion.componentes.event.interfaces.IProcesadorDeAjaxChangeListener;

/**
 * Listener para gestionar habilitar o no 
 * los campos de cambio de password desde el perfil del usuario
 * 
 * @author adele
 */
public class MyProfileEnableDisableChangeMyPasswdAjaxChangeListener 
	implements 	IProcesadorDeAjaxChangeListener
{

	private static final long serialVersionUID = -9011968808295207641L;
	

	private static final String FIELD_CHANGEMYPASSWD = "changeMyPasswd";
	private static final String FIELD_PASSWORD = "password"; 
	private static final String FIELD_PASSWORD_LBL = FIELD_PASSWORD+"_lbl";
	private static final String FIELD_PASSWORD_LBLREQ = FIELD_PASSWORD+"_lblreq";
	private static final String FIELD_NEWPASSWORD1 = "newPassword1";
	private static final String FIELD_NEWPASSWORD1_LBL = FIELD_NEWPASSWORD1+"_lbl";
	private static final String FIELD_NEWPASSWORD1_LBLREQ = FIELD_NEWPASSWORD1+"_lblreq";
	private static final String FIELD_NEWPASSWORD2 = "newPassword2";
	private static final String FIELD_NEWPASSWORD2_LBL = FIELD_NEWPASSWORD2+"_lbl";
	private static final String FIELD_NEWPASSWORD2_LBLREQ = FIELD_NEWPASSWORD2+"_lblreq";

	/*
	 * (non-Javadoc)
	 * @see com.treelogic.fawna.presentacion.componentes.event.interfaces.IProcesadorDeAjaxChangeListener#procesarAjaxChangeListener(com.treelogic.fawna.presentacion.componentes.event.api.GestorEstadoComponentes, com.treelogic.fawna.presentacion.componentes.event.api.GestorDatosComponentes)
	 */
	public void procesarAjaxChangeListener(
			final GestorEstadoComponentes gE,
			final GestorDatosComponentes gD)
	{
		final boolean checked = Boolean.TRUE.equals((Boolean) gD.getValue(FIELD_CHANGEMYPASSWD));
		gD.setValue(FIELD_CHANGEMYPASSWD, checked);
		if (!checked) {
			//Reset values of related components
			gD.setValue(FIELD_PASSWORD, null);
			gD.setValue(FIELD_NEWPASSWORD1, null);
			gD.setValue(FIELD_NEWPASSWORD2, null);
		}
		//Change status of related components
		gE.setPropiedad(FIELD_PASSWORD, "required", checked);
		gE.setPropiedad(FIELD_PASSWORD, "disabled", !checked);
		gE.setPropiedad(FIELD_PASSWORD_LBL, "rendered", !checked);
		gE.setPropiedad(FIELD_PASSWORD_LBLREQ, "rendered", checked);
		gE.setPropiedad(FIELD_NEWPASSWORD1, "required", checked);
		gE.setPropiedad(FIELD_NEWPASSWORD1, "disabled", !checked);
		gE.setPropiedad(FIELD_NEWPASSWORD1_LBL, "rendered", !checked);
		gE.setPropiedad(FIELD_NEWPASSWORD1_LBLREQ, "rendered", checked);
		gE.setPropiedad(FIELD_NEWPASSWORD2, "required", checked);
		gE.setPropiedad(FIELD_NEWPASSWORD2, "disabled", !checked);
		gE.setPropiedad(FIELD_NEWPASSWORD2_LBL, "rendered", !checked);
		gE.setPropiedad(FIELD_NEWPASSWORD2_LBLREQ, "rendered", checked);
		

		
	}

}
