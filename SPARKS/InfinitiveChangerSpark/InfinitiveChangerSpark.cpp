/// @file InfinitiveChangerSpark.cpp
/// @brief InfinitiveChangerSpark class implementation.


#include "InfinitiveChangerSpark.h"




#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "InfinitiveChangerSpark")) {
			return new InfinitiveChangerSpark(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif

/// Initializes InfinitiveChangerSpark component.
void InfinitiveChangerSpark::init(void){
	freelingPath = getComponentConfiguration()->getString(const_cast<char*>("Freeling_Path"));
	ChangeInfinitive=getComponentConfiguration()->getBool(const_cast<char*>("Change_Infinitive"));
	ChangeSingular=getComponentConfiguration()->getBool(const_cast<char*>("Change_Singular"));
	ChangeUnknown=getComponentConfiguration()->getBool(const_cast<char*>("Change_Unknown"));

	std::wstring path=util::string2wstring(freelingPath);

	util::init_locale(L"default");
	
	tk = new tokenizer(path+L"tokenizer.dat");
  	sp= new splitter(path+L"splitter.dat");
  	
	maco_options opt(L"es");
	opt.UserMap=false;
	opt.QuantitiesDetection = true;  //deactivate ratio/currency/magnitudes detection
	opt.AffixAnalysis = true; opt.MultiwordsDetection = true; opt.NumbersDetection = true; 
	opt.PunctuationDetection = true; opt.DatesDetection = true; opt.QuantitiesDetection = false; 
	opt.DictionarySearch = true; opt.ProbabilityAssignment = false; opt.NERecognition = true;
	opt.UserMapFile=L"";
	opt.LocutionsFile=path+L"locucions.dat"; opt.AffixFile=path+L"afixos.dat";
	opt.ProbabilityFile=path+L"probabilitats.dat"; opt.DictionaryFile=path+L"dicc.src";
	opt.NPdataFile=path+L"np.dat"; opt.PunctuationFile=path+L"../common/punct.dat"; 
	morfo = new maco(opt);
	if(ChangeUnknown) {
		alts_ort = new alternatives(path+L"/alternatives-ort.dat");
		alts_phon = new alternatives(path+L"/alternatives-phon.dat");
	}
}

/// Unitializes the InfinitiveChangerSpark component.
void InfinitiveChangerSpark::quit(void){
	delete tk;
	delete sp;
	delete morfo;
	if(ChangeUnknown) {
		delete alts_ort;
		delete alts_phon;
	}
}


void InfinitiveChangerSpark::processData(char * prompt){
	using namespace std;
	using namespace freeling;

	list<word> lw;
	list<sentence> ls;

	std::string phrase(prompt);
	std::wstring wphrase=util::string2wstring(phrase);

	if(ChangeUnknown) {
		lw=tk->tokenize(wphrase);
		ls=sp->split(lw, true);

		morfo->analyze(ls);
		alts_ort->analyze(ls);
		alts_phon->analyze(ls);

		size_t last_pos = 0;
		for (list<freeling::sentence>::iterator s=ls.begin(); s!=ls.end(); s++) {
		  for (freeling::sentence::iterator w=s->begin(); w!=s->end(); w++) {
			if(w->alternatives_begin() != w->alternatives_end()) {
				wphrase = wphrase.replace(wphrase.find(w->get_form(), last_pos), w->get_form().size(), w->alternatives_begin()->first);
				last_pos = wphrase.find(w->alternatives_begin()->first, last_pos) + w->alternatives_begin()->first.size();
			}
			else
				last_pos = wphrase.find(w->get_form(), last_pos) + w->get_form().size();
		  }
		}
		last_pos = 0;
		lw.clear();
		ls.clear();
	}

	lw=tk->tokenize(wphrase);
	ls=sp->split(lw, true);
	
	morfo->analyze(ls);
	
	size_t last_pos = 0;
	for (list<freeling::sentence>::iterator s=ls.begin(); s!=ls.end(); s++) {
		for (freeling::sentence::iterator w=s->begin(); w!=s->end(); w++) {
			if (ChangeInfinitive && (w->get_tag()).compare(0,1,L"V") == 0) {
				wphrase = wphrase.replace(wphrase.find(w->get_form(), last_pos), w->get_form().size(), w->get_lemma());
				last_pos = wphrase.find(w->get_lemma(), last_pos) + w->get_lemma().size();
			}
			else if (ChangeSingular && (w->get_tag()).compare(0,1,L"N") == 0 && (w->get_tag()).compare(3,1,L"P") == 0) {
				wphrase = wphrase.replace(wphrase.find(w->get_form(), last_pos), w->get_form().size(), w->get_lemma());
				last_pos = wphrase.find(w->get_lemma(), last_pos) + w->get_lemma().size();
			}
			else
				last_pos = wphrase.find(w->get_form(), last_pos) + w->get_form().size();
      }
    }
    last_pos = 0;
    lw.clear();
    ls.clear();

	std::string output=util::wstring2string(wphrase);
	char *coutput = const_cast<char*>(output.c_str());


	myFlow->processData(coutput);
	}
