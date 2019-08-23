#ifndef __FFMPEG_READER_H
#define __FFMPEG_READER_H


#include "Component.h"

#include "IFlow.h"

extern "C" {
#include <libavcodec/avcodec.h>
#include <libavformat/avformat.h>
#include <libswscale/swscale.h>
#include <libavcodec/opt.h>
#include <libavfilter/avfilter.h>
//#include <libavfilter/asrc_abuffer.h>
#include <libavfilter/vsrc_buffer.h>
#include <libavfilter/avfiltergraph.h>
#include <libavfilter/avcodec.h>
//#include <libavfilter/buffersink.h>
}



class VoiceModulatorSpark :
	public Component
{
public:
	IFlow<AudioWrap *> *myAudioFlow;
private:
	void initializeRequiredInterfaces() {
		requestRequiredInterface<IFlow<AudioWrap *> >(&myAudioFlow);
	}

public:
	VoiceModulatorSpark(
		char *instanceName,
		ComponentSystem *cs
	) : Component(instanceName, cs)
	{}
	virtual ~VoiceModulatorSpark(){};


	void init();
	void quit();

	//IFlow implementation
	void processData(AudioWrap *);

private:
	int init_filters(const char *);
	void fatal_libav_error(const char *, int);

private:
		AVFilter * filter;
		AVFilterContext * ctx;
		char * args;
		void * opaque;
		AVFilterGraph * filter_graph;
		char *filter_descr;
		AVFormatContext *avf;
		AVCodecContext *video_dec;
		AVFilterContext *video_in_filter;
		AVFilterContext *video_out_filter;
		int audio_stream_index;
		int64_t last_pts;
		AVFilterInOut *outputs;
		AVFilterInOut *inputs;
};


#endif
