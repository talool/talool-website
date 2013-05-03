$(function() {
	window.callDealUploadComponent = function(url,name) {
		Wicket.Ajax.post({ u: '${callbackUrl}&url='+ encodeURIComponent(url)+"&name="+encodeURIComponent(name) });
	};
});