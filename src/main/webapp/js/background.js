$(function() {
	var Background = function(cfg) {

		var oo = window.oo;
		var bgCanvas = document.getElementById("bgCanvas");
		var fgCanvas = document.getElementById("fgCanvas");
		var bgCtx, fgCtx;
		var stampDStore = [];
		var actorStore = [];
		var animimateCycleCount = 0;
		var animimateLimit = 200000;
		var line_height = 90*cfg.scale;
		var bgStamped = false;

		this.init = function() {
			// bail if these objects are not set
			if (!oo.logo) {
				console.log("FAIL! We need to include and config logo.js!");
				return;
			}
			
			if (!cfg.scale) cfg.scale = 1;
			
			// resize the background canvas to match the window size
			bgCanvas.width = window.innerWidth;
			bgCanvas.height = window.innerHeight;
			fgCanvas.width = window.innerWidth;
			fgCanvas.height = window.innerHeight;
			
			// reset the core variables
			stamp_d = {x:0,y:0};
			actorStore = [];
			stampDStore = [];
			animimateCycleCount = 0;
			bgCtx = bgCanvas.getContext("2d");
			fgCtx = fgCanvas.getContext("2d");
			bgCtx.clearRect(0,0,bgCanvas.width,bgCanvas.height);
			fgCtx.clearRect(0,0,fgCanvas.width,fgCanvas.height);
			
			var stamp = oo.logo.getToolsStamp(cfg);
			stampLoop(stamp);
		};
		
		var stampLoop = function(stamp) {
			bgCtx.drawImage(stamp.canvas,stamp_d.x,stamp_d.y);
			stampDStore.push({x:stamp_d.x,y:stamp_d.y});
			stamp_d.x += stamp.spacing.x;
			
			// recursion
			var bContinue = false;
			if (stamp_d.x >= window.innerWidth) {
				stamp_d.y += line_height;
				stamp_d.x = 0;
			}
			if (stamp_d.y < window.innerHeight) bContinue = true;
			if (bContinue) stampLoop(stamp);
			else if (bgStamped === false){
				bgStamped = true;
				// start animation
				requestAnimFrame(function() {
					animate(fgCtx);
				});
			}
		};
		
		// TODO make it work for stamps, then drill down to letters
		var animate = function(ctx) {
			// pick a random stamp
			var j = Math.floor(Math.random() * stampDStore.length);
			var d2 = stampDStore[j];
			
			// create the actor
			var aCfg = {scale: cfg.scale, color: cfg.animationColor};
			var actor = new Actor(d2, aCfg);
			actorStore.push(actor);
			drawActors(ctx);
			
			// recursion
			if (animimateCycleCount++ >= animimateLimit) return;
			requestAnimFrame(function() {
				animate(fgCtx);
			});
		};
		
		var Actor = function(loc, cfg) {
			this.stamp = oo.logo.getToolsStamp(cfg);
			this.loc = loc;
			this.cfg = cfg;
			this.bDead = false;
			this.alpha = 1;
			this.fade = function() {
				// drop the color alpha
				this.alpha = this.alpha-.01;
				if (this.alpha <= 0) this.bDead=true;
			};
		};
		
		var drawActors = function(ctx) {
			ctx.clearRect(0,0,window.innerWidth,window.innerHeight);
			var deadIdx = -1;
			for (var i=0; i<actorStore.length; i++) {
				var actor = actorStore[i];
				ctx.globalAlpha = actor.alpha;
				ctx.drawImage(actor.stamp.canvas,actor.loc.x,actor.loc.y);
				actor.fade();
				if (actor.bDead) {
					deadIdx = i;
				}
			}
			if (deadIdx>0) {
				actorStore.splice(deadIdx,1);
			}
		};
		
		this.init();
		
	};

	if (window.ooConfig.background && window.oo) {
		window.oo.background = new Background(window.ooConfig.background);
		$(window).resize(function() {
			window.oo.background.init();
		});
	} 
	
});