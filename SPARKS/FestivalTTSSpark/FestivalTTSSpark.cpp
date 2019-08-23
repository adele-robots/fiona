/*
 * FestivalTTSSpark.cpp
 *
 *  Created on: 23/12/2013
 *      Author: jandro
 */

/// @file FestivalTTSSpark.cpp
/// @brief FestivalTTSSpark class implementation.

#include "stdAfx.h"
#include "FestivalTTSSpark.h"

#ifdef _WIN32
#else
extern "C" Component *createComponent(char *componentInstanceName,
		char *componentType, ComponentSystem *componentSystem) {
	if (!strcmp(componentType, "FestivalTTSSpark")) {
		return new FestivalTTSSpark(componentInstanceName, componentSystem);
	} else {
		return NULL;
	}
}
#endif

/* UTF-8 to ISO-8859-1/ISO-8859-15 mapper.
 * Return 0..255 for valid ISO-8859-15 code points, 256 otherwise.
*/
static inline unsigned int to_latin9(const unsigned int code)
{
    /* Code points 0 to U+00FF are the same in both. */
    if (code < 256U)
        return code;
    switch (code) {
    case 0x0152U: return 188U; /* U+0152 = 0xBC: OE ligature */
    case 0x0153U: return 189U; /* U+0153 = 0xBD: oe ligature */
    case 0x0160U: return 166U; /* U+0160 = 0xA6: S with caron */
    case 0x0161U: return 168U; /* U+0161 = 0xA8: s with caron */
    case 0x0178U: return 190U; /* U+0178 = 0xBE: Y with diaresis */
    case 0x017DU: return 180U; /* U+017D = 0xB4: Z with caron */
    case 0x017EU: return 184U; /* U+017E = 0xB8: z with caron */
    case 0x20ACU: return 164U; /* U+20AC = 0xA4: Euro */
    default:      return 256U;
    }
}

/* Convert an UTF-8 string to ISO-8859-15.
 * All invalid sequences are ignored.
 * Note: output == input is allowed,
 * but   input < output < input + length
 * is not.
 * Output has to have room for (length+1) chars, including the trailing NUL byte.
*/
size_t utf8_to_latin9(char *const output, const char *const input, const size_t length)
{
    unsigned char             *out = (unsigned char *)output;
    const unsigned char       *in  = (const unsigned char *)input;
    const unsigned char *const end = (const unsigned char *)input + length;
    unsigned int               c;

    while (in < end)
        if (*in < 128)
            *(out++) = *(in++); /* Valid codepoint */
        else
        if (*in < 192)
            in++;               /* 10000000 .. 10111111 are invalid */
        else
        if (*in < 224) {        /* 110xxxxx 10xxxxxx */
            if (in + 1 >= end)
                break;
            if ((in[1] & 192U) == 128U) {
                c = to_latin9( (((unsigned int)(in[0] & 0x1FU)) << 6U)
                             |  ((unsigned int)(in[1] & 0x3FU)) );
                if (c < 256)
                    *(out++) = c;
            }
            in += 2;

        } else
        if (*in < 240) {        /* 1110xxxx 10xxxxxx 10xxxxxx */
            if (in + 2 >= end)
                break;
            if ((in[1] & 192U) == 128U &&
                (in[2] & 192U) == 128U) {
                c = to_latin9( (((unsigned int)(in[0] & 0x0FU)) << 12U)
                             | (((unsigned int)(in[1] & 0x3FU)) << 6U)
                             |  ((unsigned int)(in[2] & 0x3FU)) );
                if (c < 256)
                    *(out++) = c;
            }
            in += 3;

        } else
        if (*in < 248) {        /* 11110xxx 10xxxxxx 10xxxxxx 10xxxxxx */
            if (in + 3 >= end)
                break;
            if ((in[1] & 192U) == 128U &&
                (in[2] & 192U) == 128U &&
                (in[3] & 192U) == 128U) {
                c = to_latin9( (((unsigned int)(in[0] & 0x07U)) << 18U)
                             | (((unsigned int)(in[1] & 0x3FU)) << 12U)
                             | (((unsigned int)(in[2] & 0x3FU)) << 6U)
                             |  ((unsigned int)(in[3] & 0x3FU)) );
                if (c < 256)
                    *(out++) = c;
            }
            in += 4;

        } else
        if (*in < 252) {        /* 111110xx 10xxxxxx 10xxxxxx 10xxxxxx 10xxxxxx */
            if (in + 4 >= end)
                break;
            if ((in[1] & 192U) == 128U &&
                (in[2] & 192U) == 128U &&
                (in[3] & 192U) == 128U &&
                (in[4] & 192U) == 128U) {
                c = to_latin9( (((unsigned int)(in[0] & 0x03U)) << 24U)
                             | (((unsigned int)(in[1] & 0x3FU)) << 18U)
                             | (((unsigned int)(in[2] & 0x3FU)) << 12U)
                             | (((unsigned int)(in[3] & 0x3FU)) << 6U)
                             |  ((unsigned int)(in[4] & 0x3FU)) );
                if (c < 256)
                    *(out++) = c;
            }
            in += 5;

        } else
        if (*in < 254) {        /* 1111110x 10xxxxxx 10xxxxxx 10xxxxxx 10xxxxxx 10xxxxxx */
            if (in + 5 >= end)
                break;
            if ((in[1] & 192U) == 128U &&
                (in[2] & 192U) == 128U &&
                (in[3] & 192U) == 128U &&
                (in[4] & 192U) == 128U &&
                (in[5] & 192U) == 128U) {
                c = to_latin9( (((unsigned int)(in[0] & 0x01U)) << 30U)
                             | (((unsigned int)(in[1] & 0x3FU)) << 24U)
                             | (((unsigned int)(in[2] & 0x3FU)) << 18U)
                             | (((unsigned int)(in[3] & 0x3FU)) << 12U)
                             | (((unsigned int)(in[4] & 0x3FU)) << 6U)
                             |  ((unsigned int)(in[5] & 0x3FU)) );
                if (c < 256)
                    *(out++) = c;
            }
            in += 6;

        } else
            in++;               /* 11111110 and 11111111 are invalid */

    /* Terminate the output string. */
    *out = '\0';

    return (size_t)(out - (unsigned char *)output);
}

char * removeInterrogationMarks(const char * prompt) {
	string str(prompt);
	size_t pos;
	// Mientras haya coincidencias en la frase
	while((pos = str.find("¿")) != string::npos){
		// Sustituir
		str.replace(pos, 1, " ", 1);
	}
	while((pos = str.find("!")) != string::npos){
		// Sustituir
		str.replace(pos, 1, ".", 1);
	}
	return const_cast<char *>(str.c_str());
}

/// Initializes FestivalTTSSpark component.
void FestivalTTSSpark::init(void) {
	//Set the queue size (in samples, 1 byte/sample)
	try {
		audioQueueSize = getComponentConfiguration()->getInt(const_cast<char *>("AudioQueueSizeBytes"));
	} catch(::Exception &e){
		LoggerWarn("[FIONA-logger]Default size: 44100");
		audioQueueSize = 44100;
	}
	audioQueue.init(audioQueueSize);

	valid_sample_rate =	getComponentConfiguration()->getInt(const_cast<char *>("AudioVideoConfig_Remote_AvatarStream_AudioSampleRate"));

	festival_initialized = false;

}

/// Unitializes the FestivalTTSSpark component.
void FestivalTTSSpark::quit(void) {
	wave.clear();
}

//IThreadProc implementation
void FestivalTTSSpark::process() {
	// Synthesize received message
	if (!colaMensajes.empty()) {
		if(!festival_initialized) {
		    festival_initialize(TRUE, FESTIVAL_HEAP_SIZE);
		    LoggerInfo("[FIONA-logger]FestivalTTSSpark::process festival iniciado");
		    EST_String voice;
		    std::string voz = getComponentConfiguration()->getString(const_cast<char*>("Voice"));
		    if(voz=="en-GB")
		    	voice = EST_String("(voice_en1_mbrola)");
		    else if(voz=="en-US")
		    	voice = EST_String("(voice_us2_mbrola)");
		    else if(voz=="de-DE")
		    	voice = EST_String("(voice_german_de4_os)");
		    else if(voz=="es-ES")
		    	voice = EST_String("(voice_JuntaDeAndalucia_es_pa_diphone)");
		    else if(voz=="fr-FR")
		    	voice = EST_String("(voice_fr1_mbrola)");
		    else if(voz=="it-IT")
		    	voice = EST_String("(voice_pc_diphone)");
		    else
		    	voice = EST_String("(voice_en1_mbrola)");
		    festival_eval_command(voice);
		    LoggerInfo("[FIONA-logger]FestivalTTSSpark::process voz establecida a %s", voz.c_str());
		    festival_initialized = true;
		}
		string texto = colaMensajes.front();
		LoggerInfo("[FIONA-logger]FestivalTTSSpark::process mensaje recibido: %s", texto.c_str());
		char text[texto.size()+1];
		utf8_to_latin9(text, removeInterrogationMarks(texto.c_str()), texto.size());

		if(!festival_text_to_wave(text,wave)) return;
		colaMensajes.pop();
		wave.resample(valid_sample_rate);
		int samples_left = wave.num_samples() * 2;
		while(samples_left > audioQueue.audioBufferSize) {
			queueAudioBuffer((char *) wave.values().p_memory + wave.num_samples() * 2 - samples_left, audioQueue.audioBufferSize);
			samples_left = samples_left - audioQueue.audioBufferSize;
		}
		if(samples_left > 0) {
			queueAudioBuffer((char *) wave.values().p_memory + wave.num_samples() * 2 - samples_left, samples_left);
		}
	}
}

void FestivalTTSSpark::processData(char *prompt) {
	colaMensajes.push(string(prompt));
}

int FestivalTTSSpark::getStoredAudioSize() {
	return audioQueue.storedAudioSize;
}

void FestivalTTSSpark::queueAudioBuffer(char *buffer, int size) {
	audioQueue.queueAudioBuffer(buffer, size);
}

void FestivalTTSSpark::dequeueAudioBuffer(char *buffer, int size) {
	audioQueue.dequeueAudioBuffer(buffer, size);
}

void FestivalTTSSpark::reset() {
	audioQueue.reset();
}

