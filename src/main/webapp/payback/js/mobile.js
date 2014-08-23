window.payback = null;

$(function() {
	var Payback = function(cfg) {
		
		this.config = cfg;
		
		this.appStoreLink = "https://itunes.apple.com/us/app/talool/id669529943";
		this.playStoreLink = "https://play.google.com/store/apps/details?id=com.talool.android";
		
		this.init = function() {
			// add a class for mobile web
			var isIOS = navigator.userAgent.match(/(iPhone|iPad)/);
			var isAndroid = navigator.userAgent.match(/(Android)/);
			var that = this;
			if (isIOS) 
			{
				$('body').addClass("apple");

			}
			else if (isAndroid) 
			{
				$('body').addClass("android");
			}
			
			$("#download").click(function(){
				if (isIOS)
				{
					document.location = that.appStoreLink;
				}
				else if (isAndroid)
				{
					document.location = that.playStoreLink;
				}
				
			});
			
			
		};

		
		this.init();

	};

	window.payback = new Payback();
	
});