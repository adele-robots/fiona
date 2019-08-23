/// @file HTMLRemoverSpark.cpp
/// @brief HTMLRemoverSpark class implementation.


#include "stdAfx.h"
#include "HTMLRemoverSpark.h"


#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "HTMLRemoverSpark")) {
			return new HTMLRemoverSpark(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif

/// Initializes HTMLRemoverSpark component.
void HTMLRemoverSpark::init(void){
}

/// Unitializes the HTMLRemoverSpark component.
void HTMLRemoverSpark::quit(void){
}

//**To change for your convenience**
//Example of provided interface implementation
//IUpdateable1 implementation
void HTMLRemoverSpark::processData(char *prompt){
	// Extraer del texto el HTML
	std::string cadena(prompt);
	stripHTMLTags(cadena);
	char *nuevoTexto = new char[cadena.length() + 1];
	strcpy(nuevoTexto, cadena.c_str());
	myFlow->processData(nuevoTexto);
	//LoggerInfo("[FIONA-logger]Nuevo texto a sintetizar:%s",nuevoTexto);
	//FIN Extracción HTML
}

string& HTMLRemoverSpark::stripHTMLTags(string& s) {
	static bool inTag = false;
	bool done = false;
	while (!done) {
		if (inTag) {
// The previous line started an HTML tag
// but didn't finish. Must search for '>'.
			size_t rightPos = s.find('>');
			if (rightPos != string::npos) {
				inTag = false;
				s.erase(0, rightPos + 1);
			} else {
				done = true;
				s.erase();
			}
		} else {
			// Look for start of tag:
			size_t leftPos = s.find('<');
			if (leftPos != string::npos) {
				// See if tag close is in this line:
				size_t rightPos = s.find('>');
				if (rightPos == string::npos) {
					inTag = done = true;
					s.erase(leftPos);
				} else
					s.erase(leftPos, rightPos - leftPos + 1);
			} else
				done = true;
		}
	}
	return s;
}


