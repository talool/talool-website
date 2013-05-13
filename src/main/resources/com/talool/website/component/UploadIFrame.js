$(function() {
	window.callUploadComponent = function(url,name,callback) {
		Wicket.Ajax.post({ u: callback+'&url='+ encodeURIComponent(url)+"&name="+encodeURIComponent(name) });
	};
});