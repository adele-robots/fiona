package com.adelerobots.web.fiopre.customHeading;

import com.treelogic.fawna.presentacion.core.persistencia.DatosCabeceraContexto;
import com.treelogic.fawna.presentacion.core.persistencia.ICustomHeaddingAction;
import com.treelogic.fawna.presentacion.core.utilidades.IConfiguration;
import com.treelogic.fawna.presentacion.core.utilidades.ICustomHeader;
import com.treelogic.fawna.presentacion.core.utilidades.IHelperSimple;

public class LocaleHeading implements ICustomHeaddingAction{

    private static final String idiomaDefault = "en";
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public void completeHeader(ICustomHeader header, IHelperSimple helper, IConfiguration confFile) throws Exception {
        String idioma = idiomaDefault;
        
        if(header instanceof DatosCabeceraContexto)
        	((DatosCabeceraContexto) header).setIdioma(idioma);
   
    }

}