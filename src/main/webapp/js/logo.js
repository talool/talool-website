window.oo = null;

$(function() {
	var Talool = function() {
		this.init = function(color, scale, bAdmin) {
		
		  var canvas = document.getElementById("canvas");
		  var ctx = canvas.getContext("2d");
		  ctx.fillStyle = color;
		  ctx.scale(scale,scale);
		  if (bAdmin) this.drawToolsLogo(ctx);
		  else this.drawLogo(ctx);
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
		
		this.drawToolsLogo = function(ctx) {

			// boundingbox/tools
		      ctx.save();

		      // boundingbox/tools/mask
		      ctx.save();
		      ctx.beginPath();
		      ctx.moveTo(0.0, 0.0);
		      ctx.lineTo(474.5, 0.0);
		      ctx.lineTo(474.5, 75.7);
		      ctx.lineTo(0.0, 75.8);
		      ctx.lineTo(0.0, 0.0);
		      ctx.closePath();
		      ctx.clip();

		      // boundingbox/tools/s
		      ctx.beginPath();
		      ctx.moveTo(437.9, 59.8);
		      ctx.bezierCurveTo(441.5, 64.2, 445.5, 67.2, 452.0, 67.2);
		      ctx.bezierCurveTo(457.8, 67.2, 464.1, 64.7, 464.1, 58.7);
		      ctx.bezierCurveTo(464.1, 52.8, 458.3, 51.3, 452.4, 50.0);
		      ctx.bezierCurveTo(441.9, 47.6, 432.9, 45.4, 432.9, 34.0);
		      ctx.bezierCurveTo(432.9, 23.3, 443.3, 17.7, 453.5, 17.7);
		      ctx.bezierCurveTo(461.3, 17.7, 468.5, 20.7, 472.3, 27.7);
		      ctx.lineTo(464.6, 32.8);
		      ctx.bezierCurveTo(462.3, 28.9, 458.4, 26.2, 453.0, 26.2);
		      ctx.bezierCurveTo(447.9, 26.2, 442.1, 28.7, 442.1, 33.9);
		      ctx.bezierCurveTo(442.1, 38.5, 448.7, 40.5, 456.0, 41.9);
		      ctx.bezierCurveTo(465.8, 43.8, 474.0, 47.3, 474.0, 58.4);
		      ctx.bezierCurveTo(474.0, 70.8, 463.0, 75.7, 452.0, 75.7);
		      ctx.bezierCurveTo(442.7, 75.7, 436.0, 73.2, 430.6, 65.4);
		      ctx.lineTo(437.9, 59.8);
		      ctx.closePath();
		      ctx.fill();

		      // boundingbox/tools/l3
		      ctx.beginPath();
		      ctx.moveTo(421.8, 0.1);
		      ctx.lineTo(412.4, 0.1);
		      ctx.lineTo(412.4, 56.8);
		      ctx.bezierCurveTo(412.4, 65.7, 411.2, 75.6, 427.3, 75.7);
		      ctx.lineTo(427.2, 66.4);
		      ctx.lineTo(427.2, 66.4);
		      ctx.bezierCurveTo(421.0, 65.6, 421.8, 60.3, 421.8, 54.6);
		      ctx.lineTo(421.8, 0.1);
		      ctx.closePath();
		      ctx.fill();

		      // boundingbox/tools/o4
		      ctx.beginPath();

		      // boundingbox/tools/o4/Path
		      ctx.moveTo(378.4, 17.4);
		      ctx.bezierCurveTo(362.3, 17.4, 349.3, 30.4, 349.3, 46.5);
		      ctx.bezierCurveTo(349.3, 62.6, 362.3, 75.7, 378.4, 75.7);
		      ctx.bezierCurveTo(394.5, 75.7, 407.6, 62.6, 407.6, 46.5);
		      ctx.bezierCurveTo(407.6, 30.4, 394.5, 17.4, 378.4, 17.4);

		      // boundingbox/tools/o4/Path
		      ctx.moveTo(378.4, 66.7);
		      ctx.bezierCurveTo(367.3, 66.7, 358.2, 57.7, 358.2, 46.5);
		      ctx.bezierCurveTo(358.2, 35.4, 367.3, 26.3, 378.4, 26.3);
		      ctx.bezierCurveTo(389.6, 26.3, 398.6, 35.4, 398.6, 46.5);
		      ctx.bezierCurveTo(398.6, 57.7, 389.6, 66.7, 378.4, 66.7);
		      ctx.fill();

		      // boundingbox/tools/o3
		      ctx.beginPath();

		      // boundingbox/tools/o3/Path
		      ctx.moveTo(316.1, 17.4);
		      ctx.bezierCurveTo(300.0, 17.4, 286.9, 30.4, 286.9, 46.5);
		      ctx.bezierCurveTo(286.9, 62.6, 300.0, 75.7, 316.1, 75.7);
		      ctx.bezierCurveTo(332.2, 75.7, 345.2, 62.6, 345.2, 46.5);
		      ctx.bezierCurveTo(345.2, 30.4, 332.2, 17.4, 316.1, 17.4);

		      // boundingbox/tools/o3/Path
		      ctx.moveTo(316.1, 66.7);
		      ctx.bezierCurveTo(304.9, 66.7, 295.9, 57.7, 295.9, 46.5);
		      ctx.bezierCurveTo(295.9, 35.4, 304.9, 26.3, 316.1, 26.3);
		      ctx.bezierCurveTo(327.2, 26.3, 336.3, 35.4, 336.3, 46.5);
		      ctx.bezierCurveTo(336.3, 57.7, 327.2, 66.7, 316.1, 66.7);
		      ctx.fill();

		      // boundingbox/tools/t2
		      ctx.beginPath();
		      ctx.moveTo(275.4, 54.6);
		      ctx.lineTo(275.4, 26.4);
		      ctx.lineTo(286.5, 26.4);
		      ctx.lineTo(286.5, 17.3);
		      ctx.lineTo(275.4, 17.3);
		      ctx.lineTo(275.4, 0.1);
		      ctx.lineTo(275.4, 0.0);
		      ctx.lineTo(266.0, 0.0);
		      ctx.lineTo(266.0, 0.1);
		      ctx.lineTo(266.0, 56.8);
		      ctx.bezierCurveTo(266.0, 57.9, 266.0, 59.0, 266.0, 60.1);
		      ctx.bezierCurveTo(266.0, 68.0, 266.8, 75.6, 280.9, 75.7);
		      ctx.lineTo(280.9, 75.7);
		      ctx.lineTo(286.4, 75.7);
		      ctx.lineTo(286.4, 66.4);
		      ctx.lineTo(280.8, 66.4);
		      ctx.lineTo(280.8, 66.4);
		      ctx.lineTo(280.8, 66.4);
		      ctx.bezierCurveTo(275.7, 65.8, 275.3, 62.2, 275.4, 57.7);
		      ctx.bezierCurveTo(275.4, 56.7, 275.4, 55.7, 275.4, 54.6);
		      ctx.lineTo(275.4, 54.6);
		      ctx.closePath();
		      ctx.fill();

		      // boundingbox/tools/l2
		      ctx.beginPath();
		      ctx.moveTo(226.8, 0.1);
		      ctx.lineTo(217.4, 0.1);
		      ctx.lineTo(217.4, 56.8);
		      ctx.bezierCurveTo(217.4, 65.7, 216.2, 75.6, 232.3, 75.7);
		      ctx.lineTo(232.2, 66.4);
		      ctx.lineTo(232.2, 66.4);
		      ctx.bezierCurveTo(226.0, 65.6, 226.8, 60.3, 226.8, 54.6);
		      ctx.lineTo(226.8, 0.1);
		      ctx.closePath();
		      ctx.fill();

		      // boundingbox/tools/o2
		      ctx.beginPath();

		      // boundingbox/tools/o2/Path
		      ctx.moveTo(183.4, 17.4);
		      ctx.bezierCurveTo(167.3, 17.4, 154.3, 30.4, 154.3, 46.5);
		      ctx.bezierCurveTo(154.3, 62.6, 167.3, 75.7, 183.4, 75.7);
		      ctx.bezierCurveTo(199.5, 75.7, 212.6, 62.6, 212.6, 46.5);
		      ctx.bezierCurveTo(212.6, 30.4, 199.5, 17.4, 183.4, 17.4);

		      // boundingbox/tools/o2/Path
		      ctx.moveTo(183.4, 66.7);
		      ctx.bezierCurveTo(172.3, 66.7, 163.2, 57.7, 163.2, 46.5);
		      ctx.bezierCurveTo(163.2, 35.4, 172.3, 26.3, 183.4, 26.3);
		      ctx.bezierCurveTo(194.6, 26.3, 203.6, 35.4, 203.6, 46.5);
		      ctx.bezierCurveTo(203.6, 57.7, 194.6, 66.7, 183.4, 66.7);
		      ctx.fill();

		      // boundingbox/tools/o1
		      ctx.beginPath();

		      // boundingbox/tools/o1/Path
		      ctx.moveTo(121.1, 17.4);
		      ctx.bezierCurveTo(105.0, 17.4, 91.9, 30.4, 91.9, 46.5);
		      ctx.bezierCurveTo(91.9, 62.6, 105.0, 75.7, 121.1, 75.7);
		      ctx.bezierCurveTo(137.2, 75.7, 150.2, 62.6, 150.2, 46.5);
		      ctx.bezierCurveTo(150.2, 30.4, 137.2, 17.4, 121.1, 17.4);

		      // boundingbox/tools/o1/Path
		      ctx.moveTo(121.1, 66.7);
		      ctx.bezierCurveTo(109.9, 66.7, 100.9, 57.7, 100.9, 46.5);
		      ctx.bezierCurveTo(100.9, 35.4, 109.9, 26.3, 121.1, 26.3);
		      ctx.bezierCurveTo(132.2, 26.3, 141.3, 35.4, 141.3, 46.5);
		      ctx.bezierCurveTo(141.3, 57.7, 132.2, 66.7, 121.1, 66.7);
		      ctx.fill();

		      // boundingbox/tools/l1
		      ctx.beginPath();
		      ctx.moveTo(85.4, 0.1);
		      ctx.lineTo(76.0, 0.1);
		      ctx.lineTo(76.0, 56.8);
		      ctx.bezierCurveTo(76.0, 65.7, 74.8, 75.6, 90.9, 75.7);
		      ctx.lineTo(90.8, 66.4);
		      ctx.lineTo(90.8, 66.4);
		      ctx.bezierCurveTo(84.6, 65.6, 85.4, 60.3, 85.4, 54.6);
		      ctx.lineTo(85.4, 0.1);
		      ctx.closePath();
		      ctx.fill();

		      // boundingbox/tools/a
		      ctx.beginPath();

		      // boundingbox/tools/a/Path
		      ctx.moveTo(47.4, 17.2);
		      ctx.bezierCurveTo(40.6, 17.2, 33.1, 19.3, 27.4, 23.7);
		      ctx.lineTo(33.1, 30.9);
		      ctx.bezierCurveTo(36.5, 28.3, 41.2, 25.7, 48.3, 25.7);
		      ctx.bezierCurveTo(57.4, 25.7, 61.6, 31.6, 61.6, 37.4);
		      ctx.lineTo(61.6, 40.2);
		      ctx.lineTo(40.1, 40.2);
		      ctx.lineTo(40.1, 40.2);
		      ctx.bezierCurveTo(31.1, 40.4, 23.8, 48.2, 23.8, 57.7);
		      ctx.bezierCurveTo(23.8, 67.4, 31.6, 75.7, 40.8, 75.7);
		      ctx.bezierCurveTo(40.8, 75.7, 43.3, 75.7, 46.7, 75.7);
		      ctx.bezierCurveTo(46.7, 75.7, 46.8, 75.7, 46.8, 75.7);
		      ctx.bezierCurveTo(46.8, 75.7, 46.8, 75.7, 46.8, 75.7);
		      ctx.lineTo(47.0, 75.7);
		      ctx.lineTo(47.0, 75.7);
		      ctx.bezierCurveTo(51.6, 75.6, 56.7, 73.9, 60.7, 71.7);
		      ctx.lineTo(60.7, 75.7);
		      ctx.bezierCurveTo(66.2, 75.7, 70.7, 75.7, 70.7, 75.7);
		      ctx.lineTo(70.7, 64.2);
		      ctx.lineTo(70.7, 39.1);
		      ctx.lineTo(70.7, 35.7);
		      ctx.bezierCurveTo(70.7, 26.1, 63.9, 17.2, 47.4, 17.2);

		      // boundingbox/tools/a/Path
		      ctx.moveTo(34.2, 57.9);
		      ctx.bezierCurveTo(34.2, 52.7, 38.3, 48.5, 43.5, 48.4);
		      ctx.lineTo(43.5, 48.4);
		      ctx.lineTo(61.5, 48.4);
		      ctx.lineTo(61.5, 60.7);
		      ctx.bezierCurveTo(58.1, 64.3, 52.6, 67.2, 47.3, 67.3);
		      ctx.lineTo(43.5, 67.3);
		      ctx.bezierCurveTo(38.3, 67.2, 34.2, 63.1, 34.2, 57.9);
		      ctx.fill();

		      // boundingbox/tools/t1
		      ctx.beginPath();
		      ctx.moveTo(9.4, 54.6);
		      ctx.lineTo(9.4, 26.4);
		      ctx.lineTo(20.5, 26.4);
		      ctx.lineTo(20.5, 17.3);
		      ctx.lineTo(9.4, 17.3);
		      ctx.lineTo(9.4, 0.1);
		      ctx.lineTo(9.4, 0.0);
		      ctx.lineTo(0.0, 0.0);
		      ctx.lineTo(0.0, 0.1);
		      ctx.lineTo(0.0, 56.8);
		      ctx.bezierCurveTo(0.0, 57.9, 0.0, 59.0, 0.0, 60.1);
		      ctx.bezierCurveTo(-0.0, 68.0, 0.8, 75.6, 14.9, 75.7);
		      ctx.lineTo(14.9, 75.7);
		      ctx.lineTo(20.4, 75.7);
		      ctx.lineTo(20.4, 66.4);
		      ctx.lineTo(14.8, 66.4);
		      ctx.lineTo(14.8, 66.4);
		      ctx.lineTo(14.8, 66.4);
		      ctx.bezierCurveTo(9.7, 65.8, 9.3, 62.2, 9.4, 57.7);
		      ctx.bezierCurveTo(9.4, 56.7, 9.4, 55.7, 9.4, 54.6);
		      ctx.lineTo(9.4, 54.6);
		      ctx.closePath();
		      ctx.fill();
		      ctx.restore();
		      ctx.restore();
		    };
	};

	var oo = new Talool();
	if (window.ooConfig) {
		oo.init(ooConfig.logo.color,ooConfig.logo.scale, ooConfig.logo.bAdmin);
	} else {
		oo.init("#fff",.5, true);
	}
	window.oo = oo;
});