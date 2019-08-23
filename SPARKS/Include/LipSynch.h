/// @file LipSynch.h
/// @brief LipSynch class definition.

#ifndef __LIP_SYNCH_H
#define __LIP_SYNCH_H

#include <vector>
//#include <loqtts.h>
//PABLO: Scene
#include "IScene.h"
//PABLO: MTB MODIFIED
#include "IMorphTargetBlender.h"
//PABLO: Scene
#include "IScene.h"
#include "StopWatch.h"

#include <sapi.h>
#include <sphelper.h>
#include <string>

#include <iostream>
#include <iomanip>

using namespace std;

/// Phoneme event information storage.

class TtsPhonemeEventRecord {
public:
	static const int MAX_LABEL_LENGHT=80;
	char label[MAX_LABEL_LENGHT];
	unsigned int start;
	unsigned int duration;
};

/// Associates phoneme with viseme ids. The association is many to one.

class PhonemeToVisemeMapRecord {
public:
	static const int MAX_PHONEME_LABEL=80;
	static const int MAX_MORPH_TARGET_LABEL=80;
	char phonemeLabel[80];
	char visemeMorphTarget[80];
};


/// \brief Virtual character lip synchroniztion with the text-to-speech engine.
///
/// Class that launches prompts to the TTS engine, monitors TTS event callbacks and 
/// calculate mouth-related morph target values.

class LipSynch 
{
public:
	//PABLO: MTB MODIFIED
	LipSynch(psisban::Config *bc, IMorphTargetBlender *mt) : 
		bodyConfiguration(bc), 
		morphTargetBlender(mt)
		{}
//	LipSynch(psisban::Config *bc):
//	  bodyConfiguration(bc)
//	  {}
	//void init();
	bool init();
	void updatePhonemeWeights(
		float *weightPreviousPhoneme,
		float *weightActualPhoneme,
		float *weightNextPhoneme,
		int elapsedTime,
		int t0, int t1, int t2, int t3
	);
	//void updateVisemeMorphTargets(void);
	void updateVisemeMorphTargets();
	//void sayThis(char *prompt);		/* Asíncrono */
	void sayThis(WCHAR* string);
	//void stopSpeech(void);			/* Asíncrono */
	//bool isEndOfSpeech(void);
	//void resetMorphTargets(void);
	//void quit(void);
	void quit();
public:
	StopWatch stopWatch;
	char voice[256];
	//MorphTargetBlender *blender;
	//Scene *sc;
	unsigned int total_dur;
	std::vector<TtsPhonemeEventRecord *> ttsPhonemeEventRecordArray;
	std::vector<PhonemeToVisemeMapRecord *> ttsPhonemeToVisemeMap;
	float apexHeight;
	float slewRate;
	void printWeights();
	//char *mapPhonemeToViseme(char *phonemeLabel);
	//ttsHandleType hReader;	

	// SAPI call back function
	static void __stdcall sapiEvent(WPARAM wParam, LPARAM lParam);

	// The SAPI voice
	ISpVoice*		m_pVoice;

	//configuration
	//ContenedorDeFicheros* m_files_container;
	std::vector<int> visemeToMorphTargetIndex;

private:
	//void initTTS(void);

private:
	psisban::Config *bodyConfiguration; // viseme info
	//ttsHandleType hSession;
	char *restLabel;
	//PABLO: MTB MODIFIED
	IMorphTargetBlender *morphTargetBlender;
};

class KK {
	void pp(void);
};
#endif

