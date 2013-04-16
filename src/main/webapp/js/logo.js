window.oo = null;

$(function() {
	var Talool = function() {
		this.init = function(color, scale) {
		
		  var canvas = document.getElementById("canvas");
		  var ctx = canvas.getContext("2d");
		  ctx.fillStyle = color;
		  ctx.scale(scale,scale);
		  this.drawLogo(ctx);
		};
		
		this.drawLogo = function(ctx) {
		
			// layer1/Group
		      ctx.save();

		      // layer1/Group/Clipping Path
		      ctx.save();
		      ctx.beginPath();
		      ctx.moveTo(0.0, 0.0);
		      ctx.lineTo(234.0, 0.0);
		      ctx.lineTo(234.0, 90.0);
		      ctx.lineTo(0.0, 90.0);
		      ctx.lineTo(0.0, 0.0);
		      ctx.closePath();
		      ctx.clip();

		      // layer1/Group/Compound Path
		      ctx.beginPath();

		      // layer1/Group/Compound Path/Path
		      ctx.moveTo(123.2, 25.9);
		      ctx.bezierCurveTo(107.1, 25.9, 94.1, 38.9, 94.1, 55.0);
		      ctx.bezierCurveTo(94.1, 71.1, 107.1, 84.2, 123.2, 84.2);
		      ctx.bezierCurveTo(139.3, 84.2, 152.4, 71.1, 152.4, 55.0);
		      ctx.bezierCurveTo(152.4, 38.9, 139.3, 25.9, 123.2, 25.9);

		      // layer1/Group/Compound Path/Path
		      ctx.moveTo(123.2, 75.2);
		      ctx.bezierCurveTo(112.1, 75.2, 103.0, 66.2, 103.0, 55.0);
		      ctx.bezierCurveTo(103.0, 43.9, 112.1, 34.8, 123.2, 34.8);
		      ctx.bezierCurveTo(134.4, 34.8, 143.4, 43.9, 143.4, 55.0);
		      ctx.bezierCurveTo(143.4, 66.2, 134.4, 75.2, 123.2, 75.2);
		      ctx.fill();

		      // layer1/Group/Compound Path
		      ctx.beginPath();

		      // layer1/Group/Compound Path/Path
		      ctx.moveTo(185.6, 25.9);
		      ctx.bezierCurveTo(169.5, 25.9, 156.5, 38.9, 156.5, 55.0);
		      ctx.bezierCurveTo(156.5, 71.1, 169.5, 84.2, 185.6, 84.2);
		      ctx.bezierCurveTo(201.7, 84.2, 214.8, 71.1, 214.8, 55.0);
		      ctx.bezierCurveTo(214.8, 38.9, 201.7, 25.9, 185.6, 25.9);

		      // layer1/Group/Compound Path/Path
		      ctx.moveTo(185.6, 75.2);
		      ctx.bezierCurveTo(174.5, 75.2, 165.4, 66.2, 165.4, 55.0);
		      ctx.bezierCurveTo(165.4, 43.9, 174.5, 34.8, 185.6, 34.8);
		      ctx.bezierCurveTo(196.8, 34.8, 205.8, 43.9, 205.8, 55.0);
		      ctx.bezierCurveTo(205.8, 66.2, 196.8, 75.2, 185.6, 75.2);
		      ctx.fill();

		      // layer1/Group/Path
		      ctx.beginPath();
		      ctx.moveTo(11.6, 63.1);
		      ctx.lineTo(11.6, 34.9);
		      ctx.lineTo(22.6, 34.9);
		      ctx.lineTo(22.6, 25.8);
		      ctx.lineTo(11.6, 25.8);
		      ctx.lineTo(11.6, 8.6);
		      ctx.lineTo(11.6, 8.6);
		      ctx.lineTo(2.2, 8.6);
		      ctx.lineTo(2.2, 8.6);
		      ctx.lineTo(2.2, 65.3);
		      ctx.bezierCurveTo(2.2, 66.4, 2.2, 67.5, 2.2, 68.6);
		      ctx.bezierCurveTo(2.2, 76.5, 3.0, 84.1, 17.1, 84.2);
		      ctx.lineTo(17.1, 84.2);
		      ctx.lineTo(22.6, 84.2);
		      ctx.lineTo(22.6, 74.9);
		      ctx.lineTo(16.9, 74.9);
		      ctx.lineTo(16.9, 74.9);
		      ctx.lineTo(16.9, 74.9);
		      ctx.bezierCurveTo(11.9, 74.3, 11.5, 70.7, 11.5, 66.3);
		      ctx.bezierCurveTo(11.5, 65.2, 11.6, 64.2, 11.6, 63.1);
		      ctx.lineTo(11.6, 63.1);
		      ctx.closePath();
		      ctx.fill();

		      // layer1/Group/Path
		      ctx.beginPath();
		      ctx.moveTo(87.6, 8.6);
		      ctx.lineTo(78.2, 8.6);
		      ctx.lineTo(78.2, 65.3);
		      ctx.bezierCurveTo(78.2, 74.2, 77.0, 84.1, 93.1, 84.2);
		      ctx.lineTo(92.9, 74.9);
		      ctx.lineTo(92.9, 74.9);
		      ctx.bezierCurveTo(86.7, 74.1, 87.6, 68.8, 87.6, 63.1);
		      ctx.lineTo(87.6, 8.6);
		      ctx.closePath();
		      ctx.fill();

		      // layer1/Group/Path
		      ctx.beginPath();
		      ctx.moveTo(229.0, 8.6);
		      ctx.lineTo(219.6, 8.6);
		      ctx.lineTo(219.6, 65.3);
		      ctx.bezierCurveTo(219.6, 74.2, 218.4, 84.1, 234.5, 84.2);
		      ctx.lineTo(234.4, 74.9);
		      ctx.lineTo(234.4, 74.9);
		      ctx.bezierCurveTo(228.1, 74.1, 229.0, 68.8, 229.0, 63.1);
		      ctx.lineTo(229.0, 8.6);
		      ctx.closePath();
		      ctx.fill();

		      // layer1/Group/Compound Path
		      ctx.beginPath();

		      // layer1/Group/Compound Path/Path
		      ctx.moveTo(49.6, 25.7);
		      ctx.bezierCurveTo(42.7, 25.7, 35.3, 27.9, 29.6, 32.2);
		      ctx.lineTo(35.2, 39.5);
		      ctx.bezierCurveTo(38.7, 36.8, 43.3, 34.2, 50.5, 34.2);
		      ctx.bezierCurveTo(59.5, 34.2, 63.8, 40.2, 63.8, 45.9);
		      ctx.lineTo(63.8, 48.7);
		      ctx.lineTo(42.3, 48.7);
		      ctx.lineTo(42.3, 48.7);
		      ctx.bezierCurveTo(33.2, 48.9, 26.0, 56.7, 26.0, 66.3);
		      ctx.bezierCurveTo(26.0, 75.9, 33.8, 84.2, 43.0, 84.2);
		      ctx.bezierCurveTo(43.0, 84.2, 45.4, 84.2, 48.9, 84.2);
		      ctx.bezierCurveTo(48.9, 84.2, 48.9, 84.2, 48.9, 84.2);
		      ctx.bezierCurveTo(49.0, 84.2, 49.0, 84.2, 49.0, 84.2);
		      ctx.lineTo(49.2, 84.2);
		      ctx.lineTo(49.2, 84.2);
		      ctx.bezierCurveTo(53.8, 84.1, 58.9, 82.4, 62.9, 80.2);
		      ctx.lineTo(62.9, 84.2);
		      ctx.bezierCurveTo(68.4, 84.2, 72.8, 84.2, 72.8, 84.2);
		      ctx.lineTo(72.8, 72.7);
		      ctx.lineTo(72.8, 47.6);
		      ctx.lineTo(72.8, 44.3);
		      ctx.bezierCurveTo(72.8, 34.6, 66.0, 25.7, 49.6, 25.7);

		      // layer1/Group/Compound Path/Path
		      ctx.moveTo(36.3, 66.4);
		      ctx.bezierCurveTo(36.3, 61.2, 40.5, 57.0, 45.6, 56.9);
		      ctx.lineTo(45.6, 56.9);
		      ctx.lineTo(63.6, 56.9);
		      ctx.lineTo(63.6, 69.2);
		      ctx.bezierCurveTo(60.2, 72.9, 54.7, 75.7, 49.4, 75.8);
		      ctx.lineTo(45.7, 75.8);
		      ctx.bezierCurveTo(40.5, 75.8, 36.3, 71.6, 36.3, 66.4);
		      ctx.fill();
		      ctx.restore();
		      ctx.restore();
		};
	};

	var oo = new Talool();
	oo.init("#fff",.5);
	window.oo = oo;
});