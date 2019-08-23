/// @file dumpFormat.h
/// @brief FFMpeg helper function declaration.


#ifndef __DUMP_FORMAT_H
#define __DUMP_FORMAT_H


#pragma warning(disable:4244) // FFMpeg silly warning

extern "C" {
#include <libavcodec/avcodec.h>
#include <libavformat/avformat.h>
#include <libswscale/swscale.h>
#include <libavutil/opt.h>
}


void getFFMpegFormatString(
	char *formatString,
	int formatStringLenght,
	AVFormatContext *ic,
	int index,
	const char *url,
	int is_output
);


#endif
