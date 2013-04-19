window.oo = null;

$(function() {
	var Talool = function(cfg) {
		
		this.config = cfg;
		
		this.init = function() {
			if (this.config.logo) this.initLogo(this.config.logo);
		};
		
		this.initLogo = function(cfg) {
			var canvas = document.getElementById("canvas");
			var ctx = canvas.getContext("2d");
			ctx.fillStyle = cfg.color;
			ctx.strokeStyle = cfg.color;
			ctx.scale(cfg.scale,cfg.scale);
			if (cfg.bAdmin) this.drawToolsLogo(ctx);
			else this.drawLogo(ctx);
		};
		
		this.drawLogo = function(ctx) {
		
			// layer1/Group
		      ctx.save();

		      // layer1/Group/L2
		      ctx.save();
		      ctx.beginPath();
		      ctx.moveTo(227.3, 0.5);
		      ctx.lineTo(217.9, 0.5);
		      ctx.lineTo(217.9, 57.2);
		      ctx.bezierCurveTo(217.9, 66.1, 216.7, 76.0, 232.8, 76.1);
		      ctx.lineTo(232.7, 66.9);
		      ctx.lineTo(232.7, 66.8);
		      ctx.bezierCurveTo(226.5, 66.1, 227.3, 60.8, 227.3, 55.0);
		      ctx.lineTo(227.3, 0.5);
		      ctx.closePath();
		      //ctx.fillStyle = "rgba(255,255,255,.3)";
		      ctx.fill();
		      //ctx.strokeStyle = "#fff";
		      ctx.lineJoin = "miter";
		      ctx.miterLimit = 4.0;
		      ctx.stroke();

		      // layer1/Group/O2
		      ctx.beginPath();

		      // layer1/Group/O2/Path
		      ctx.moveTo(183.9, 17.8);
		      ctx.bezierCurveTo(167.8, 17.8, 154.8, 30.9, 154.8, 47.0);
		      ctx.bezierCurveTo(154.8, 63.1, 167.8, 76.1, 183.9, 76.1);
		      ctx.bezierCurveTo(200.0, 76.1, 213.1, 63.1, 213.1, 47.0);
		      ctx.bezierCurveTo(213.1, 30.9, 200.0, 17.8, 183.9, 17.8);

		      // layer1/Group/O2/Path
		      ctx.moveTo(183.9, 67.2);
		      ctx.bezierCurveTo(172.8, 67.2, 163.7, 58.1, 163.7, 47.0);
		      ctx.bezierCurveTo(163.7, 35.8, 172.8, 26.8, 183.9, 26.8);
		      ctx.bezierCurveTo(195.1, 26.8, 204.1, 35.8, 204.1, 47.0);
		      ctx.bezierCurveTo(204.1, 58.1, 195.1, 67.2, 183.9, 67.2);
		      ctx.fill();
		      ctx.stroke();

		      // layer1/Group/O1
		      ctx.beginPath();

		      // layer1/Group/O1/Path
		      ctx.moveTo(121.6, 17.8);
		      ctx.bezierCurveTo(105.5, 17.8, 92.4, 30.9, 92.4, 47.0);
		      ctx.bezierCurveTo(92.4, 63.1, 105.5, 76.1, 121.6, 76.1);
		      ctx.bezierCurveTo(137.7, 76.1, 150.7, 63.1, 150.7, 47.0);
		      ctx.bezierCurveTo(150.7, 30.9, 137.7, 17.8, 121.6, 17.8);

		      // layer1/Group/O1/Path
		      ctx.moveTo(121.6, 67.2);
		      ctx.bezierCurveTo(110.4, 67.2, 101.4, 58.1, 101.4, 47.0);
		      ctx.bezierCurveTo(101.4, 35.8, 110.4, 26.8, 121.6, 26.8);
		      ctx.bezierCurveTo(132.7, 26.8, 141.8, 35.8, 141.8, 47.0);
		      ctx.bezierCurveTo(141.8, 58.1, 132.7, 67.2, 121.6, 67.2);
		      ctx.fill();
		      ctx.stroke();

		      // layer1/Group/L1
		      ctx.beginPath();
		      ctx.moveTo(85.9, 0.5);
		      ctx.lineTo(76.5, 0.5);
		      ctx.lineTo(76.5, 57.2);
		      ctx.bezierCurveTo(76.5, 66.1, 75.3, 76.0, 91.4, 76.1);
		      ctx.lineTo(91.3, 66.9);
		      ctx.lineTo(91.3, 66.8);
		      ctx.bezierCurveTo(85.1, 66.1, 85.9, 60.8, 85.9, 55.0);
		      ctx.lineTo(85.9, 0.5);
		      ctx.closePath();
		      ctx.fill();
		      ctx.stroke();

		      // layer1/Group/A
		      ctx.beginPath();

		      // layer1/Group/A/Path
		      ctx.moveTo(47.9, 17.7);
		      ctx.bezierCurveTo(41.1, 17.7, 33.6, 19.8, 27.9, 24.1);
		      ctx.lineTo(33.6, 31.4);
		      ctx.bezierCurveTo(37.0, 28.8, 41.7, 26.1, 48.8, 26.1);
		      ctx.bezierCurveTo(57.9, 26.1, 62.1, 32.1, 62.1, 37.8);
		      ctx.lineTo(62.1, 40.6);
		      ctx.lineTo(40.6, 40.6);
		      ctx.lineTo(40.6, 40.7);
		      ctx.bezierCurveTo(31.6, 40.9, 24.3, 48.6, 24.3, 58.2);
		      ctx.bezierCurveTo(24.3, 67.9, 32.1, 76.1, 41.3, 76.1);
		      ctx.bezierCurveTo(41.3, 76.1, 43.8, 76.1, 47.2, 76.1);
		      ctx.bezierCurveTo(47.2, 76.1, 47.3, 76.1, 47.3, 76.1);
		      ctx.bezierCurveTo(47.3, 76.1, 47.3, 76.1, 47.3, 76.1);
		      ctx.lineTo(47.5, 76.1);
		      ctx.lineTo(47.5, 76.1);
		      ctx.bezierCurveTo(52.1, 76.1, 57.2, 74.3, 61.2, 72.1);
		      ctx.lineTo(61.2, 76.1);
		      ctx.bezierCurveTo(66.7, 76.1, 71.2, 76.1, 71.2, 76.1);
		      ctx.lineTo(71.2, 64.6);
		      ctx.lineTo(71.2, 39.6);
		      ctx.lineTo(71.2, 36.2);
		      ctx.bezierCurveTo(71.2, 26.6, 64.4, 17.7, 47.9, 17.7);

		      // layer1/Group/A/Path
		      ctx.moveTo(34.7, 58.3);
		      ctx.bezierCurveTo(34.7, 53.1, 38.8, 49.0, 44.0, 48.8);
		      ctx.lineTo(44.0, 48.8);
		      ctx.lineTo(62.0, 48.8);
		      ctx.lineTo(62.0, 61.1);
		      ctx.bezierCurveTo(58.6, 64.8, 53.1, 67.6, 47.8, 67.8);
		      ctx.lineTo(44.0, 67.8);
		      ctx.bezierCurveTo(38.8, 67.7, 34.7, 63.5, 34.7, 58.3);
		      ctx.fill();
		      ctx.stroke();

		      // layer1/Group/T
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
		    
		    this.init();
	};

	if (window.ooConfig) {
		window.oo = new Talool(window.ooConfig);
	} 
	
});