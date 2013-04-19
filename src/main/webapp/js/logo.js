window.oo = null;

/*
 * TODO drawImage can take height and width args, so we don't need to include 
 * the scale in the key... even transparency can be altered outside the stamp, 
 * with globalAlpha.
 */

$(function() {
	var Logo = function(cfg) {
		
		var oo = window.oo;
		var logoText = "talool";
		var toolsText = "talool tools";
		var stamps = [];
		
		this.init = function() {

			// create the default stamps
			if (!cfg.color) cfg.color = "#fff";
			if (!cfg.scale) cfg.scale = 1;
			createStamp(window.oo.getTempCanvas(500,100),logoText,cfg);
			createStamp(window.oo.getTempCanvas(800,100),toolsText,cfg);
			
			// if there is a target canvas, stamp out the logo
			if (cfg.target) {
				var canvas = $(cfg.target)[0];
				var ctx = canvas.getContext("2d");
				if (cfg.bAdmin) {
					ctx.drawImage(this.getToolsStamp(cfg).canvas,0,0);
				} else {
					ctx.drawImage(this.getLogoStamp(cfg).canvas,0,0);
				}
			}
		};
		
		this.getLogoStamp = function(cfg) {
			var key = getStampHashKey(logoText,cfg);
			var stamp = stamps[key];
			if (!stamp) {
				stamp = createStamp(window.oo.getTempCanvas(500,100),logoText,cfg);
			}
			return stamp;
		};
		this.getToolsStamp = function(cfg) {
			var key = getStampHashKey(toolsText,cfg);
			var stamp = stamps[key];
			if (!stamp) {
				stamp = createStamp(window.oo.getTempCanvas(500,100),toolsText,cfg);
			}
			return stamp;
		};
		
		var createStamp = function(canvas,text,cfg) {
			if (!oo.letters) {
				console.log("FAIL! We need to include and config letters.js!");
				return;
			}
			
			var ctx = canvas.getContext("2d");
			var letterStamp;
			var d = {x:0, y:0};
			var chars = text.split('');
			for (var i=0; i<chars.length; i++) {
				letterStamp = oo.letters.getLetter(chars[i],cfg);
				ctx.drawImage(letterStamp.canvas,d.x,d.y);
				d.x += letterStamp.spacing.x;
				d.y += letterStamp.spacing.y;
			}
			
			var stamp = {canvas: canvas, spacing: {x:d.x, y:d.y}};
			stamps[getStampHashKey(text,cfg)] = stamp;
			
			return stamp;
		};
		
		var getStampHashKey = function(text,cfg) {
			return text + cfg.color + cfg.scale;
		};
		    
		this.init();
	};
	
	// if the config exists, add this obj to the Talool namespace
	if (window.ooConfig.logo && window.oo) {
		window.oo.logo = new Logo(window.ooConfig.logo);
	}
	
});