$(function() {
	setTimeout(function() {
		var step = ${chartStep};
		var size = ${chartSize};
		var horizons = ${data};
		var id = "#${componentMarkupId}";
		window.oo.cubismChart(id,horizons,step,size);
	}, 100 );
});