package com.adelerobots.web.fiopre.utilidades;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class AvatarUtils {

	public static boolean detectComponent(final String usermaild5, final String componente) throws JDOMException,
			IOException, FileNotFoundException {
		boolean found = false;

		// Creamos el builder basado en SAX
		SAXBuilder builder = new SAXBuilder();
		// Construimos el arbol DOM a partir del fichero xml
		File avatarCfgFile = new File("/datos/nfs/users/private/" + usermaild5
				+ "/", "avatar.xml");

		Document doc = builder.build(new FileInputStream(avatarCfgFile));
		Element root = doc.getRootElement();
		Element declarations = root.getChild("ComponentDeclarations");

		@SuppressWarnings("unchecked")
		List<Element> components = declarations.getChildren();
		for (int i = 0; i < components.size() && !found; i++) {
			Element component = components.get(i);
			String valorType = component.getAttributeValue("type");

			if (valorType != null
					&& componente.compareToIgnoreCase(valorType) == 0) {
				found = true;
			}
		}
		return found;
	}
	
	public static String escapeBoolean(boolean found) {
		return String.valueOf(found);
	}
	
}
