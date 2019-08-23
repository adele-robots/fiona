/// @file dumpFormat.cpp
/// @brief FFMpeg utility functions.


#include "stdAfx.h"	

#pragma warning(disable:4244) // FFMpeg silly warnings

extern "C" {
#include <libavcodec/avcodec.h>
#include <libavformat/avformat.h>
#include <libswscale/swscale.h>
#include <libavcodec/opt.h>
}


// c�digo adaptado de ffmpeg, libavformat/utils.c

void addContent(char *s, int tam, char *fmt, ...) {
	int len = (int)strlen(s);

	va_list pArgs;
	va_start(pArgs, fmt);
	vsprintf_s(s + len, tam - len, fmt, pArgs);
	va_end(pArgs);
}


static void print_fps(
	char *formatString,
	int formatStringLenght,
	double d, 
	const char *postfix
)
{
	uint64_t v= floor(d*100);
	if     (v% 100      ) addContent(formatString, formatStringLenght, ", %3.2f %s", d, postfix);
	else if(v%(100*1000)) addContent(formatString, formatStringLenght, ", %1.0f %s", d, postfix);
	else                  addContent(formatString, formatStringLenght, ", %1.0fk %s", d/1000, postfix);
}


static void addContent
(
	char *formatString, 
	int formatStringLenght, 
	AVFormatContext *ic, 
	int i, 
	int index, 
	int is_output
)
{
	char buf[256];
	int flags = (is_output ? ic->oformat->flags : ic->iformat->flags);
	AVStream *st = ic->streams[i];
	int g = av_gcd(st->time_base.num, st->time_base.den);
	AVMetadataTag *lang = av_metadata_get(st->metadata, "language", NULL, 0);
	avcodec_string(buf, sizeof(buf), st->codec, is_output);
	addContent(formatString, formatStringLenght, "    Stream #%d.%d", index, i);
	/* the pid is an important information, so we display it */
	/* XXX: add a generic system */
	if (flags & AVFMT_SHOW_IDS)
		addContent(formatString, formatStringLenght, "[0x%x]", st->id);
	if (lang)
		addContent(formatString, formatStringLenght, "(%s)", lang->value);
	av_log(NULL, AV_LOG_DEBUG, ", %d/%d", st->time_base.num/g, st->time_base.den/g);
	addContent(formatString, formatStringLenght, ": %s", buf);
	if (st->sample_aspect_ratio.num && // default
		av_cmp_q(st->sample_aspect_ratio, st->codec->sample_aspect_ratio)) {
			AVRational display_aspect_ratio;
			av_reduce(&display_aspect_ratio.num, &display_aspect_ratio.den,
				st->codec->width*st->sample_aspect_ratio.num,
				st->codec->height*st->sample_aspect_ratio.den,
				1024*1024);
			addContent(formatString, formatStringLenght, ", PAR %d:%d DAR %d:%d",
				st->sample_aspect_ratio.num, st->sample_aspect_ratio.den,
				display_aspect_ratio.num, display_aspect_ratio.den);
	}
	if(st->codec->codec_type == AVMEDIA_TYPE_VIDEO){
		if(st->r_frame_rate.den && st->r_frame_rate.num)
			print_fps(formatString, formatStringLenght, av_q2d(st->r_frame_rate), "tbr");
		if(st->time_base.den && st->time_base.num)
			print_fps(formatString, formatStringLenght, 1/av_q2d(st->time_base), "tbn");
		if(st->codec->time_base.den && st->codec->time_base.num)
			print_fps(formatString, formatStringLenght, 1/av_q2d(st->codec->time_base), "tbc");
	}
	addContent(formatString, formatStringLenght, "\n");
}



void getFFMpegFormatString(
	char *formatString,
	int formatStringLenght,
	AVFormatContext *ic,
	int index,
	const char *url,
	int is_output
)
{
	unsigned int i;
	uint8_t *printed = (uint8_t *)av_mallocz(ic->nb_streams);
	if (ic->nb_streams && !printed)
		return;

	formatString[0] = 0;
	addContent(formatString, formatStringLenght, "%s #%d, %s, %s '%s':\n",
		is_output ? "Output" : "Input",
		index,
		is_output ? ic->oformat->name : ic->iformat->name,
		is_output ? "to" : "from",
		url);
	if (!is_output) {
		addContent(formatString, formatStringLenght, "  Duration: ");
		if (ic->duration != AV_NOPTS_VALUE) {
			int hours, mins, secs, us;
			secs = ic->duration / AV_TIME_BASE;
			us = ic->duration % AV_TIME_BASE;
			mins = secs / 60;
			secs %= 60;
			hours = mins / 60;
			mins %= 60;
			addContent(formatString, formatStringLenght, "%02d:%02d:%02d.%02d", hours, mins, secs,
				(100 * us) / AV_TIME_BASE);
		} else {
			addContent(formatString, formatStringLenght, "N/A");
		}
		if (ic->start_time != AV_NOPTS_VALUE) {
			int secs, us;
			addContent(formatString, formatStringLenght, ", start: ");
			secs = ic->start_time / AV_TIME_BASE;
			us = ic->start_time % AV_TIME_BASE;
			addContent(formatString, formatStringLenght, "%d.%06d",
				secs, (int)av_rescale(us, 1000000, AV_TIME_BASE));
		}
		addContent(formatString, formatStringLenght, ", bitrate: ");
		if (ic->bit_rate) {
			addContent(formatString, formatStringLenght,"%d kb/s", ic->bit_rate / 1000);
		} else {
			addContent(formatString, formatStringLenght, "N/A");
		}
		addContent(formatString, formatStringLenght, "\n");
	}
	if(ic->nb_programs) {
		unsigned int j, k, total = 0;
		for(j=0; j<ic->nb_programs; j++) {
			AVMetadataTag *name = av_metadata_get(ic->programs[j]->metadata,
				"name", NULL, 0);
			addContent(formatString, formatStringLenght, "  Program %d %s\n", ic->programs[j]->id,
				name ? name->value : "");
			for(k=0; k<ic->programs[j]->nb_stream_indexes; k++) {
				addContent(formatString, formatStringLenght, ic, ic->programs[j]->stream_index[k], index, is_output);
				printed[ic->programs[j]->stream_index[k]] = 1;
			}
			total += ic->programs[j]->nb_stream_indexes;
		}
		if (total < ic->nb_streams)
			addContent(formatString, formatStringLenght, "  No Program\n");
	}
	for(i=0;i<ic->nb_streams;i++)
		if (!printed[i])
			addContent(formatString, formatStringLenght, ic, i, index, is_output);

	if (ic->metadata) {
		AVMetadataTag *tag=NULL;
		addContent(formatString, formatStringLenght, "  Metadata\n");
		while((tag=av_metadata_get(ic->metadata, "", tag, AV_METADATA_IGNORE_SUFFIX))) {
			addContent(formatString, formatStringLenght, "    %-16s: %s\n", tag->key, tag->value);
		}
	}
	av_free(printed);
}


