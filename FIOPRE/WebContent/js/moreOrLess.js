jQuery(function() {

	// The height of the content block when it's not expanded
	var adjustheight = 150;
	// The "more" link text
	var moreText = "+  More";
	// The "less" link text
	var lessText = "- Less";

	// Sets the .more-block div to the specified height and hides any content
	// that overflows
	if (jQuery(".more-less .more-block").height() > 150) {
		jQuery(".more-less .more-block").css('height', adjustheight).css(
				'overflow', 'hidden');

		// The section added to the bottom of the "more-less" div
		jQuery(".more-less").append(
				'<a href="#" class="adjust">' + moreText + '</a>');

		jQuery("a.adjust").text(moreText);

		jQuery(".adjust").toggle(
				function() {
					jQuery(this).parents("div:first").find(".more-block").css(
							'height', 'auto').css('overflow', 'visible');
					// Hide the [...] when expanded
					jQuery(this).parents("div:first").find("p.continued").css(
							'display', 'none');
					jQuery(this).text(lessText);
				},
				function() {
					jQuery(this).parents("div:first").find(".more-block").css(
							'height', adjustheight).css('overflow', 'hidden');
					jQuery(this).parents("div:first").find("p.continued").css(
							'display', 'block');
					jQuery(this).text(moreText);
				});

	}

});