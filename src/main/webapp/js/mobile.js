window.oo = null;

$(function() {
	var Talool = function(cfg) {
		
		this.init = function() {
			// add a class for mobile web
			var isIOS = navigator.userAgent.match(/(iPhone|iPad)/);
			var isAndroid = navigator.userAgent.match(/(Android)/);
			if (isIOS) 
			{
				$('body').addClass("apple");
				
				if (cfg && cfg.redirectToApp)
					document.location = 'taloolmydeals://';
			}
			if (isAndroid) 
			{
				$('body').addClass("android");
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
		};

		this.init();
	};

	var cfg = (window.ooConfig && window.ooConfig.mobile)?window.ooConfig.mobile:null;
	window.oo = new Talool(cfg);
	
});