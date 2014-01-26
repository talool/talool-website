$(function() {
	
	var data = { labels : ${labels}, datasets : ${data} };
	
	var options = {};
	
	//Get context with jQuery - using jQuery's .get() method.
	var ctx = $("#${componentMarkupId}").get(0).getContext("2d");
	//This will get the first returned node in the jQuery collection.
	new Chart(ctx).Line(data,options);
	
});