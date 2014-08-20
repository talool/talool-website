window.oo = null;

$(function() {
	var Talool = function(cfg) {
		
		this.config = cfg;
		
		this.init = function() {
			// add a spinner like in jquery mobile
			$('.show-loading-msg').click(function() {
				  window.oo.spinner = $(this);
				  window.oo.spinner.spin();
			});
		};

		this.isMobile = function()
		{
			return navigator.userAgent.match(/(Android|iPhone|iPod|iPad)/);
		};

		this.stopSpinner = function() {
			// stop the spinner
			if (window.oo.spinner) {
				window.oo.spinner.spin(false);
				window.oo.spinner = null;
			}
		};
		
		this.finishRegistration = function(feedbackId) {
			// stop the spinner
			this.stopSpinner();
			// clear the form
			$('form')[0].reset();
			// set the focus on the feedback panel
			$('html, body').animate({
		        scrollTop: ($("#"+feedbackId).offset().top - 100)
		    }, 200);
		};

		this.init();

	};

	window.oo = new Talool();
	
});

window.requestAnimFrame = (function(callback) {
    return window.requestAnimationFrame || 
    		window.webkitRequestAnimationFrame || 
    		window.mozRequestAnimationFrame || 
    		window.oRequestAnimationFrame || 
    		window.msRequestAnimationFrame ||
    		function(callback) {
    			window.setTimeout(callback, 1000 / 60);
    		};
})();