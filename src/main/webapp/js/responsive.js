window.oo = null;
$(function() {
	var Responsive = function(cfg) {

		this.init = function() {
			var respond = function() {
				if (window.innerWidth < cfg.mobileWidth) {
					$('body').addClass(cfg.mobileClass).removeClass(cfg.tabletClass);
					if (cfg.redirectToApp)
					{
						this.appRedirect();
					}
				} else if (window.innerWidth < cfg.tabletWidth) {
					$('body').addClass(cfg.tabletClass).removeClass(cfg.mobileClass);
				} else {
					$('body').removeClass(cfg.tabletClass).removeClass(cfg.mobileClass);
				}
				$("#debug").text(window.innerWidth);
			};
			$(window).resize(respond);
			respond();
		};
		
		this.appRedirect = function()
		{
			  document.location = 'yourAppScheme://';
			  setTimeout( function()
			  {
			      if( confirm( 'You do not seem to have Your App installed, do you want to go download it now?'))
			      {
			        document.location = 'http://itunes.apple.com/us/app/yourAppId';
			      }
			  }, 300);
		};
		    
		this.init();
	};
	
	// if the config exists, add this obj to the Talool namespace
	if (window.ooConfig.responsive && window.oo) {
		window.oo.responsive = new Responsive(window.ooConfig.responsive);
	}
	
});