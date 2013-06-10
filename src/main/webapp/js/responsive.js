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
			
			// add a class for mobile web
			oo.isMobile = navigator.userAgent.match(/(Android|iPhone|iPod|iPad)/);
			if (oo.isMobile) 
			{
				// try to bust out to the app
				this.appRedirect();
				
				$('body').addClass("mw");
				var ww = ( $(window).width() < window.screen.width ) ? $(window).width() : window.screen.width; //get proper width
				var mw = 320; // min width of site
				var ratio =  ww / mw; //calculate ratio
				if ( ww < mw)
				{ //smaller than minimum size
				   $('#Viewport').attr('content', 'initial-scale=' + ratio + ', maximum-scale=' + ratio + ', minimum-scale=' + ratio + ', user-scalable=yes, width=' + ww);
				}
				else
				{ //regular size
				   $('#Viewport').attr('content', 'initial-scale=1.0, maximum-scale=2, minimum-scale=1.0, user-scalable=yes, width=' + ww);
				}
			}
		};
		
		this.appRedirect = function()
		{
			  document.location = 'taloolmydeals://';
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