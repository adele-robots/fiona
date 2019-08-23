/// @file TTSPronunciationAdapterSpark.cpp
/// @brief TTSPronunciationAdapterSpark class implementation.


#include "stdAfx.h"
#include "TTSPronunciationAdapterSpark.h"


#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "TTSPronunciationAdapterSpark")) {
			return new TTSPronunciationAdapterSpark(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif

/// Initializes TTSPronunciationAdapterSpark component.
void TTSPronunciationAdapterSpark::init(void){
	charMap.clear();
	int replacesNumber = getComponentConfiguration()->getLength(const_cast<char *>("String_Replaces"));
	for (int replacesIndex = 0; replacesIndex < replacesNumber; replacesIndex++) {
		string from, to;

		char setting[80];

		_snprintf(setting, 80, "String_Replaces.[%d].From", replacesIndex);
		from = getComponentConfiguration()->getString(setting);

		_snprintf(setting, 80, "String_Replaces.[%d].To", replacesIndex);
		to = getComponentConfiguration()->getString(setting);

		charMap.insert(pair<string, string>(from, to));
	}
}

/// Unitializes the TTSPronunciationAdapterSpark component.
void TTSPronunciationAdapterSpark::quit(void){
}

void TTSPronunciationAdapterSpark::processData(char * prompt){
	string str(prompt);
	size_t pos;
	// Para cada pareja del mapa
	for(map<string, string>::iterator it = charMap.begin(); it != charMap.end(); it++) {
		pos = 0;
		// Mientras haya coincidencias en la frase
		while((pos = str.find(it->first, pos)) != string::npos){
			// Sustituir
			str.replace(pos, it->first.length(), it->second.c_str(), it->second.length());
			pos+=it->second.length();
		}
	}
	myFlow->processData(const_cast<char*>(str.c_str()));
}


