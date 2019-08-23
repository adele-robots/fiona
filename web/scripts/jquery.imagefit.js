(function($){$.fn.imagefit=function(options){var fit={all:function(imgs){imgs.each(function(){fit.one(this);})},one:function(img){$(img)
.width('100%').each(function()
{$(this).height(Math.round($(this).attr('startheight')*($(this).width()/$(this).attr('startwidth'))));})}};this.each(function(){var container=this;var imgs=$('img',container).not($("table img"));imgs.each(function(){$(this).attr('startwidth',$(this).width())
.attr('startheight',$(this).height())
.css('max-width',$(this).attr('startwidth')+"px");fit.one(this);});$(window).bind('resize',function(){fit.all(imgs);});});return this;};})(jQuery);