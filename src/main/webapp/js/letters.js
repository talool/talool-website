$(function() {
	var Letters = function() {

		var letterStamps = [];
		
		this.getLetter = function(letter,cfg) {
			var key = getStampHashKey(letter,cfg);
			var stamp = letterStamps[key];
			if (!stamp) {
				stamp = draw(letter,cfg);
				letterStamps[key] = stamp;
			}
			return stamp;
		};
		
		var getStampHashKey = function(text,cfg) {
			return text + cfg.color + cfg.scale;
		};

		var draw = function(letter,cfg) {
			var canvas = window.oo.getTempCanvas(100,100);
			var ctx = canvas.getContext("2d");
			ctx.fillStyle = cfg.color;
			ctx.strokeStyle = cfg.color;
			ctx.scale(cfg.scale,cfg.scale); 
			ctx.lineJoin = "miter";
			ctx.miterLimit = 4.0;
			ctx.save();
			
			// draw the letter and adjust spacing based on scale
			var spacing = drawLetter(letter, ctx);
			spacing.x = spacing.x*cfg.scale;
			spacing.y = spacing.y*cfg.scale;
			
			ctx.restore();
			
			var stamp = {spacing:spacing,canvas:canvas};
			return stamp;
		};
		
		var drawLetter = function(letter,ctx) {
			var d = {};
			switch(letter) {
				case "t": d = draw_t(ctx);break;
				case "a": d = draw_a(ctx);break;
				case "l": d = draw_l(ctx);break;
				case "o": d = draw_o(ctx);break;
				case "s": d = draw_s(ctx);break;
				case " ": d = draw_space(ctx);break;
				default: break;
			}
			return d;
		};
		
		var draw_space = function(ctx) {
			return {x: 30,y:0};
		};
		
		var draw_t = function(ctx) {
			// letters/T
			ctx.beginPath();
			ctx.moveTo(9.9, 55.0);
			ctx.lineTo(9.9, 26.9);
			ctx.lineTo(21.0, 26.9);
			ctx.lineTo(21.0, 17.7);
			ctx.lineTo(9.9, 17.7);
			ctx.lineTo(9.9, 0.5);
			ctx.lineTo(9.9, 0.5);
			ctx.lineTo(0.5, 0.5);
			ctx.lineTo(0.5, 0.5);
			ctx.lineTo(0.5, 57.2);
			ctx.bezierCurveTo(0.5, 58.3, 0.5, 59.5, 0.5, 60.6);
			ctx.bezierCurveTo(0.5, 68.4, 1.3, 76.1, 15.4, 76.1);
			ctx.lineTo(15.4, 76.1);
			ctx.lineTo(20.9, 76.1);
			ctx.lineTo(20.9, 66.9);
			ctx.lineTo(15.3, 66.9);
			ctx.lineTo(15.3, 66.8);
			ctx.lineTo(15.3, 66.8);
			ctx.bezierCurveTo(10.2, 66.2, 9.8, 62.6, 9.9, 58.2);
			ctx.bezierCurveTo(9.9, 57.2, 9.9, 56.1, 9.9, 55.0);
			ctx.lineTo(9.9, 55.0);
			ctx.closePath();
			ctx.fill();
			ctx.stroke();
			var max_x = 20.9;
			return {x:(max_x+3),y:0};
		};
		
		var draw_a = function(ctx) {
			// letters/A
			ctx.beginPath();
			
			// letters/A/Path
			ctx.moveTo(24.1, 17.7);
			ctx.bezierCurveTo(17.2, 17.7, 9.8, 19.8, 4.1, 24.1);
			ctx.lineTo(9.8, 31.4);
			ctx.bezierCurveTo(13.2, 28.8, 17.9, 26.1, 25.0, 26.1);
			ctx.bezierCurveTo(34.0, 26.1, 38.3, 32.1, 38.3, 37.8);
			ctx.lineTo(38.3, 40.6);
			ctx.lineTo(16.8, 40.6);
			ctx.lineTo(16.8, 40.7);
			ctx.bezierCurveTo(7.8, 40.9, 0.5, 48.6, 0.5, 58.2);
			ctx.bezierCurveTo(0.5, 67.9, 8.3, 76.1, 17.5, 76.1);
			ctx.bezierCurveTo(17.5, 76.1, 20.0, 76.1, 23.4, 76.1);
			ctx.bezierCurveTo(23.4, 76.1, 23.4, 76.1, 23.5, 76.1);
			ctx.bezierCurveTo(23.5, 76.1, 23.5, 76.1, 23.5, 76.1);
			ctx.lineTo(23.7, 76.1);
			ctx.lineTo(23.7, 76.1);
			ctx.bezierCurveTo(28.3, 76.1, 33.4, 74.3, 37.4, 72.1);
			ctx.lineTo(37.4, 76.1);
			ctx.bezierCurveTo(42.9, 76.1, 47.4, 76.1, 47.4, 76.1);
			ctx.lineTo(47.4, 64.6);
			ctx.lineTo(47.4, 39.6);
			ctx.lineTo(47.4, 36.2);
			ctx.bezierCurveTo(47.4, 26.6, 40.6, 17.7, 24.1, 17.7);
			
			// letters/A/Path
			ctx.moveTo(10.9, 58.3);
			ctx.bezierCurveTo(10.9, 53.1, 15.0, 49.0, 20.2, 48.8);
			ctx.lineTo(20.2, 48.8);
			ctx.lineTo(38.2, 48.8);
			ctx.lineTo(38.2, 61.1);
			ctx.bezierCurveTo(34.8, 64.8, 29.3, 67.6, 24.0, 67.8);
			ctx.lineTo(20.2, 67.8);
			ctx.bezierCurveTo(15.0, 67.7, 10.9, 63.5, 10.9, 58.3);
			ctx.fill();
			ctx.stroke();
			var max_x = 50;
			return {x:(max_x+2),y:0};
		};
		
		var draw_l = function(ctx) {
			// letters/L
			ctx.beginPath();
			ctx.moveTo(9.9, 0.5);
			ctx.lineTo(0.5, 0.5);
			ctx.lineTo(0.5, 57.2);
			ctx.bezierCurveTo(0.5, 66.1, -0.7, 76.0, 15.4, 76.1);
			ctx.lineTo(15.3, 66.9);
			ctx.lineTo(15.3, 66.8);
			ctx.bezierCurveTo(9.1, 66.1, 9.9, 60.8, 9.9, 55.0);
			ctx.lineTo(9.9, 0.5);
			ctx.closePath();
			ctx.fill();
			ctx.stroke();
			var max_x = 15.3;
			return {x:(max_x+1),y:0};
		};
		
		var draw_o = function(ctx) {
			// letters/O
			ctx.beginPath();
			
			// letters/O/Path
			ctx.moveTo(29.7, 17.8);
			ctx.bezierCurveTo(13.6, 17.8, 0.5, 30.9, 0.5, 47.0);
			ctx.bezierCurveTo(0.5, 63.1, 13.6, 76.1, 29.7, 76.1);
			ctx.bezierCurveTo(45.8, 76.1, 58.8, 63.1, 58.8, 47.0);
			ctx.bezierCurveTo(58.8, 30.9, 45.8, 17.8, 29.7, 17.8);
			
			// letters/O/Path
			ctx.moveTo(29.7, 67.2);
			ctx.bezierCurveTo(18.5, 67.2, 9.5, 58.1, 9.5, 47.0);
			ctx.bezierCurveTo(9.5, 35.8, 18.5, 26.8, 29.7, 26.8);
			ctx.bezierCurveTo(40.8, 26.8, 49.9, 35.8, 49.9, 47.0);
			ctx.bezierCurveTo(49.9, 58.1, 40.8, 67.2, 29.7, 67.2);
			ctx.fill();
			ctx.stroke();
			var max_x = 60;
			return {x:(max_x+3),y:0};
		};
		
		var draw_s = function(ctx) {
			// letters/S
			ctx.beginPath();
			ctx.moveTo(7.8, 60.2);
			ctx.bezierCurveTo(11.5, 64.7, 15.5, 67.6, 21.9, 67.6);
			ctx.bezierCurveTo(27.7, 67.6, 34.1, 65.2, 34.1, 59.1);
			ctx.bezierCurveTo(34.1, 53.3, 28.2, 51.7, 22.3, 50.4);
			ctx.bezierCurveTo(11.8, 48.1, 2.9, 45.8, 2.9, 34.4);
			ctx.bezierCurveTo(2.9, 23.7, 13.2, 18.2, 23.5, 18.2);
			ctx.bezierCurveTo(31.2, 18.2, 38.4, 21.1, 42.2, 28.2);
			ctx.lineTo(34.5, 33.2);
			ctx.bezierCurveTo(32.2, 29.4, 28.3, 26.7, 22.9, 26.7);
			ctx.bezierCurveTo(17.8, 26.7, 12.0, 29.1, 12.0, 34.3);
			ctx.bezierCurveTo(12.0, 38.9, 18.6, 40.9, 25.9, 42.3);
			ctx.bezierCurveTo(35.7, 44.2, 43.9, 47.7, 43.9, 58.8);
			ctx.bezierCurveTo(43.9, 71.3, 32.9, 76.1, 21.9, 76.1);
			ctx.bezierCurveTo(12.6, 76.1, 5.9, 73.6, 0.5, 65.9);
			ctx.lineTo(7.8, 60.2);
			ctx.closePath();
			ctx.fill();
			var max_x = 76.1;
			return {x:(max_x+0),y:0};
		};
		
	};

	if (window.oo) {
		window.oo.letters = new Letters();
	} 
	
});
