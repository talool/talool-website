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
			return navigator.userAgent.match(/(Android|iPhone|iPod)/);
		};
		
		this.mediaPicker = function()
		{
			$("ul.thumbnails.image_picker_selector").each(function(idx, elem){
				$(elem).remove();
			});
			
			$("select.merchantMediaSelector option").each(function(idx, elem){
				if (elem.value.indexOf("http")==0)
				{
					$(elem).attr("data-img-src",elem.value);
				}
			});
			
			$("select.merchantMediaSelector").imagepicker();
			
		};
		
		this.cubismChart = function(id, horizons, step, size)
		{
			$(id)[0].innerHTML = "";
			
			var context = cubism.context().step(step).size(size);
			var graphite = context.graphite("http://carb.talool.com");
			
			var talool_colors = ["#d94701","#fd8d3c","#fdbe85","#feedde","#83e0dc","#00bfb7","#008a83","#19534f"];
			
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
				    		  .colors(talool_colors)
				        	);
				}
	
			  	div.append("div")
			      	.attr("class", "rule")
			      	.call(context.rule());
	
			});
		};
		
		this.toggleSelected = function(elementsSelector, selectedSelector)
		{
			var cn = "selected";
			$(elementsSelector).removeClass(cn);
			$(selectedSelector).addClass(cn);
		};
		
		this.initSliders = function(cn)
		{
			$("."+cn).each(function(i){
				var s = $(this);
				s.removeClass(cn); // make sure we don't init this one again
				var amtFldSelector = "#"+s.data('amt-fld-id');
				var sliderSelector = "#"+s.data('slider-id');
				var callback = s.data('callback');
				$( sliderSelector ).slider({
				  range: true,
				  min: s.data('min'),
				  max: s.data('max'),
				  values: [ s.data('low'), s.data('high') ],
				  slide: function( event, ui ) {
				    $( amtFldSelector ).val( ui.values[ 0 ] + " - " + ui.values[ 1 ] );
				  },
				  change: function(event, ui) {
				  	var url = callback + "&min="+ui.values[ 0 ] + "&max=" + ui.values[ 1 ];
				  	Wicket.Ajax.get({ u: url });
				  }
				});
				$( amtFldSelector ).val( $( sliderSelector ).slider( "values", 0 ) +
				  " - " + $( sliderSelector ).slider( "values", 1 ) );
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