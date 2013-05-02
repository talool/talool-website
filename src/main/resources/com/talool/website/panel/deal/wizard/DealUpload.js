$(function() {
	window.callDealUploadComponent = function(url) {
		Wicket.Ajax.post({ u: '${callbackUrl}&url='+ encodeURIComponent(url) });
	};
});