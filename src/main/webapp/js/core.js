window.oo = null;

$(function() {
	var Talool = function(cfg) {
		
		this.config = cfg;
		
		this.init = function() {
			// global method setup
			$(".sidebar li:has(em)").addClass("selected");
		};
		
		this.getTempCanvas = function(width, height) {
			var pad = getScratchPad();
			var c = $("<canvas></canvas>")[0];
			c.width = width;
			c.height = height;
			pad.append(c);
			return c;
		};
		
		var getScratchPad = function() {
			var pad = $("#scratchPad");
			if (!pad[0]) {
				pad = $("<div id='scratchPad'></div>");
				pad.hide();
				$("body").append(pad);
			}
			return pad;
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