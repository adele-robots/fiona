/// @file VoiceModulatorSpark.cpp
/// @brief VoiceModulatorSpark class implementation.

#include "stdAfx.h"
#include "VoiceModulatorSpark.h"

#include "Logger.h"

#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "VoiceModulatorSpark")) {
			return new VoiceModulatorSpark(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif

void VoiceModulatorSpark::init() {
	avfilter_register_all();

	filter_descr = const_cast<char*>("aecho=0.8:0.9:1000|1800:0.3|0.25");
	audio_stream_index = -1;
	last_pts = AV_NOPTS_VALUE;

	int ret;
	if ((ret = init_filters(filter_descr)) < 0) {
	        quit();
	}

	filter = avfilter_get_by_name("aecho");
	filter->init(ctx, args, opaque);
}


void VoiceModulatorSpark::processData(AudioWrap * audio) {
	AudioWrap modulatedAudio(audio->audioBuffer, audio->bufferSizeInBytes);
	myAudioFlow->processData(&modulatedAudio);
}

void VoiceModulatorSpark::quit() {
	avfilter_inout_free(&outputs);
	avfilter_inout_free(&inputs);
}

int VoiceModulatorSpark::init_filters(const char *filters)
{
	char args[256];
	int r;
	AVFilter *vf_buffer = avfilter_get_by_name("buffer");
	AVFilter *vf_nullsink = avfilter_get_by_name("nullsink");
	outputs = avfilter_inout_alloc();
	inputs  = avfilter_inout_alloc();


	filter_graph = avfilter_graph_alloc();

	/* Buffer video source: the decoded frames from the codec will be
	 * inserted here. */
	snprintf(args, sizeof(args), "%d:%d:%d:%d:%d", video_dec->width,
	         video_dec->height, video_dec->pix_fmt,
	         video_dec->time_base.num, video_dec->time_base.den);

	r = avfilter_graph_create_filter(&video_in_filter, vf_buffer, "src", args,
	                                 NULL, filter_graph);
	if (r < 0)
	    fatal_libav_error("avfilter_graph_create_filter: buffer", r);

	/* Null video sink: to terminate the filter chain. */
	r = avfilter_graph_create_filter(&video_out_filter, vf_nullsink, "out",
	                                 NULL, NULL, filter_graph);
	if (r < 0)
	    fatal_libav_error("avfilter_graph_create_filter: nullsink", r);


	/* Endpoints for the filter graph. */
	outputs->name       = av_strdup("in");
	outputs->filter_ctx = video_in_filter;
	outputs->pad_idx    = 0;
	outputs->next       = NULL;
	inputs->name       = av_strdup("out");
	inputs->filter_ctx = video_out_filter;
	inputs->pad_idx    = 0;
	inputs->next       = NULL;

/*	These 2 blocks seems somewhat redundant
	the avfilter_graph_create_filter() could do that already somehow, maybe if
	we move the 2 AVFilterInOut into filter_graph*/


	r = avfilter_graph_parse(filter_graph, filters, &inputs, &outputs, NULL);
	if (r < 0)
	    fatal_libav_error("avfilter_graph_parse", r);

	r = avfilter_graph_config(filter_graph, NULL);
	if (r < 0)
	    fatal_libav_error("avfilter_graph_config", r);

	return r;
}

void VoiceModulatorSpark::fatal_libav_error(const char *tag, int r)
{
    char buf[1024];

    av_strerror(r, buf, sizeof(buf));
    fprintf(stderr, "%s: %s\n", tag, buf);
    exit(1);
}
