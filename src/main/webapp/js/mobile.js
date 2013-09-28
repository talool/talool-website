window.oo = null;

$(function() {
	var Talool = function(cfg) {
		
		this.init = function() {
			// add a class for mobile web
			var isIOS = navigator.userAgent.match(/(iPhone|iPad)/);
			var isAndroid = navigator.userAgent.match(/(Android)/);
			
			if (cfg && cfg.deeplink)
			{
				document.location = cfg.deeplink;
			}
			
			if (isIOS) 
			{
				$('body').addClass("apple");
				
				if (cfg && cfg.redirectToApp)
					document.location = 'taloolmydeals://';
			}
			else if (isAndroid) 
			{
				$('body').addClass("android");
				
				if (cfg && cfg.redirectToApp)
					document.location = 'talool://';
			}
			
			$("#download").click(function(){
				if (isIOS)
				{
					document.location = "https://itunes.apple.com/us/app/talool/id669529943";
				}
				else if (isAndroid)
				{
					document.location = "https://play.google.com/store/apps/details?id=com.talool.mobile.android";
				}
				
			});
			
			$(".og").parent(".ui-content").addClass("og");
			
			// check if this is a table
			var ww = ( $(window).width() < window.screen.width ) ? $(window).width() : window.screen.width; //get proper width
			if (ww > 600) 
			{
				$('body').addClass("tablet");
			}
			
		};

		this.init();
	};

	var cfg = (window.ooConfig && window.ooConfig.mobile)?window.ooConfig.mobile:null;
	window.oo = new Talool(cfg);
	
});