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
		
		this.isMobile = function()
		{
			return navigator.userAgent.match(/(Android|iPhone|iPod|iPad)/);
		};
		
		this.cubismChart = function(id, horizons, step)
		{
			$(id)[0].innerHTML = "";
			
			var context = cubism.context().step(step).size(500);
			var graphite = context.graphite("http://graphite.talool.com");

			d3.select(id).call(function(div) {
	
				div.append("div")
		  		.attr("class", "axis")
		  		.call(context.axis().orient("top"));
				
				for (var h in horizons)
				{
					var horizon = horizons[h];
					
					var data = [];
					for (var i=0; i<horizon.metrics.length; i++)
					{
						var metric = horizon.metrics[i];
						data[i] = graphite.metric(metric.definition);
					}
					
					div.append("div")
				      .attr("class", "horizon")
				      .data(data)
				      .call(context
				    		  .horizon()
				    		  .height(50)
				    		  .title(horizon.title)
				        	);
				}
	
			  	div.append("div")
			      	.attr("class", "rule")
			      	.call(context.rule());
	
			});
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