jQuery(window).load(function(){

	// The window.load event guarantees that
	// all the images are loaded before the
	// auto-advance begins.

	var timeOut = null;

	jQuery('#slideshow .arrow').click(function(e,simulated){
		
		// The simulated parameter is set by the
		// trigger method.
		
		if(!simulated){
			
			// A real click occured. Cancel the
			// auto advance animation.
			// Comentamos esto porque no queremos que
			// tras usar los botones de verdad, se desactive
			// el autoadvance
			//clearTimeout(timeOut);
		}
	});

	// A self executing named function expression:
	
	(function autoAdvance(){
		
		// Simulating a click on the next arrow.
		jQuery('#slideshow .next').trigger('click',[true]);
		
		// Schedulling a time out in 5 seconds.
		timeOut = setTimeout(autoAdvance,5000);		
	})();

});