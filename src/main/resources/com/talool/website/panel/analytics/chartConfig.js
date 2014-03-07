$(function() {
	
	var data = { labels : ${labels}, datasets : ${data} };
	
	var options = {};
	
	function initChart(panel,data,option)
	{
		var ctx = panel.getContext("2d");
		//This will get the first returned node in the jQuery collection.
		new Chart(ctx).Line(data,options);
	}
	
	var panel = $("#${componentMarkupId}").get(0);
	if (panel == null)
	{
		// race condition.  panel isn't on the page yet.
		setTimeout( function(){ 
			panel = $("#${componentMarkupId}").get(0);
			initChart(panel,data,options); 
			},200 );
	}
	else
	{
		initChart(panel,data,options);
	}
	
	
	
	
});