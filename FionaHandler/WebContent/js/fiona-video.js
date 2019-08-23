function showYoutubeVideo(videoID, autoPlay){
	
	if(jQuery("#video_content").length > 0){
		closeVideoDialog();
	}
	
	var divVideo = jQuery("<div id='video_content' class='video closable'></div>");
	jQuery("body").append(divVideo);
	
	var htmlVideo = "<object width='640' height='360'>";
	htmlVideo += "<param name='movie' value='https://www.youtube.com/v/" + videoID + "?version=3'></param>";
	htmlVideo += "<param name='allowFullScreen' value='true'></param>";
	htmlVideo += "<param name='allowScriptAccess' value='always'></param>";	
	htmlVideo += "<embed src='https://www.youtube.com/v/" + videoID + "?";
	if(autoPlay != 0)
		htmlVideo += "autoplay=" + autoPlay + "&";
	htmlVideo += "version=3' type='application/x-shockwave-flash' allowfullscreen='true' allowScriptAccess='always' autoplay ='1' width='640' height='360'></embed>";
	htmlVideo += "</object>";
	
	jQuery("#video_content").append(htmlVideo);
	
	var dialogOptions = {
			maxHeight : 360,
			minHeight : 360,
			maxWidth : 680,
			minWidth : 680,
			position : 'bottom',
			resizable : false,
			show : {
				effect : 'drop',
				direction : 'up'
			},
			close : closeVideoDialog
		};
		jQuery("#video_content").dialog(dialogOptions);
		jQuery(".ui-widget-header")
		.css(
				"background",
				"url('"
						+ hostAddres
						+ "img/black_bar_thick.png') repeat-x scroll 50% 50% #CCCCCC");
}

function closeVideoDialog(){
	jQuery("#video_content").dialog("destroy");
	jQuery("#video_content").remove();
}