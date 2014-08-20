window.oo = null;

$(function() {
	var Talool = function(cfg) {
		this.appStoreLink = "https://itunes.apple.com/us/app/talool/id669529943";
		this.playStoreLink = "https://play.google.com/store/apps/details?id=com.talool.android";
		this.deepLinkKillSwitch = "nl=1";
		this.init = function() {
			// add a class for mobile web
			var isIOS = navigator.userAgent.match(/(iPhone|iPad)/);
			var isAndroid = navigator.userAgent.match(/(Android)/);
			var that = this;
			if (isIOS) 
			{
				$('body').addClass("apple");
				
				if (cfg && cfg.deeplink)
				{
					
					var q = document.location.search;
					if (q.indexOf(this.deepLinkKillSwitch) == -1)
					{
						var fallback = document.location.href;
						fallback += (fallback.indexOf("?") == -1)?"?":"&";
						fallback += this.deepLinkKillSwitch;
						this.iOSDeepLink(cfg.deeplink, fallback);
					}
					
				}
			}
			else if (isAndroid) 
			{
				$('body').addClass("android");
				
				if (cfg && cfg.deeplink)
				{
					window.location = cfg.deeplink;
				}
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
			
			$(".og").parent(".ui-content").addClass("og");
			
			// check if this is a tablet
			var ww = ( $(window).width() < window.screen.width ) ? $(window).width() : window.screen.width; //get proper width
			if (ww > 600) 
			{
				$('body').addClass("tablet");
			}  
			
			$('.show-loading-msg').click(function() {
				  var $this = $( this ),
				  theme = $this.jqmData( "theme" ) || $.mobile.loader.prototype.options.theme,
				  msgText = $this.jqmData( "msgtext" ) || $.mobile.loader.prototype.options.text,
				  textVisible = $this.jqmData( "textvisible" ) || $.mobile.loader.prototype.options.textVisible,
				  textonly = !!$this.jqmData( "textonly" );
				  html = $this.jqmData( "html" ) || "";
				$.mobile.loading( 'show', {
				  text: msgText,
				  textVisible: textVisible,
				  theme: theme,
				  textonly: textonly,
				  html: html
				  });
				});
			
		};
		
		this.iOSDeepLink = function(link, fallback) {
			setTimeout(function() {
				window.location = fallback;
			}, 25);
			window.location = link;
		};
		this.stopSpinner = function() {
			// stop the spinner
			$.mobile.loading( "hide" );
		};
		
		this.finishRegistration = function(feedbackId) {
			// stop the spinner
			this.stopSpinner();
			// clear the form
			$('form')[0].reset();
			// set the focus on the feedback panel
			$('html, body').animate({
		        scrollTop: ($("#"+feedbackId).offset().top - 100)
		    }, 200);
		};

		this.init();
	};

	var cfg = (window.ooConfig && window.ooConfig.mobile)?window.ooConfig.mobile:null;
	window.oo = new Talool(cfg);
	
});