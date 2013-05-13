window.oo = null;
$(function() {
	var Responsive = function(cfg) {

		this.init = function() {
			var respond = function() {
				if (window.innerWidth < cfg.mobileWidth) {
					$('body').addClass(cfg.mobileClass).removeClass(cfg.tabletClass);
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
		
		    
		this.init();
	};
	
	// if the config exists, add this obj to the Talool namespace
	if (window.ooConfig.responsive && window.oo) {
		window.oo.responsive = new Responsive(window.ooConfig.responsive);
	}
	
});