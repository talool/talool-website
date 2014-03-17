$(function() {
	
	$.fn.graphite.defaults.width = "500";
	$.fn.graphite.defaults.height = "300";
	$.fn.graphite.defaults.url = "http://graphite.talool.com/render/";
	
	$("#${componentMarkupId}").graphite({
	    from: "-4hours",
	    colorList: "red,green",
	    target: [
	        "stats_counts.talool.redemption",
	        "stats_counts.talool.favorite",
	    ],
	    title: "Doug Rocks"
	});
	
	
});