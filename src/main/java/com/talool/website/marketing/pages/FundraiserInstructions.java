package com.talool.website.marketing.pages;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.core.FactoryManager;
import com.talool.core.Merchant;
import com.talool.core.MerchantCodeGroup;
import com.talool.core.service.ServiceException;
import com.talool.core.service.TaloolService;
import com.talool.website.behaviors.CoBrandBehavior;
import com.talool.website.panel.NiceFeedbackPanel;
import com.talool.website.util.PublisherCobrand;

public class FundraiserInstructions extends BaseMarketingPage
{
	private static final long serialVersionUID = 8390824646913457971L;
	private static final Logger LOG = Logger.getLogger(FundraiserInstructions.class);
	
	private PublisherCobrand cobrand;
	private String fundraiserName;
	private String publisherName;
	private String name;
	private String code;
	private String cobrandMerchantName;
	private String cobrandClassName;
	
	protected transient static final TaloolService taloolService = FactoryManager.get()
			.getServiceFactory().getTaloolService();

	public FundraiserInstructions(PageParameters parameters)
	{
		super(parameters);	

		// This page requires a co-brand (even if it is ours)
		if (!parameters.isEmpty())
		{
			cobrandMerchantName = parameters.get("merchant").toString();
			cobrandClassName = parameters.get("cobrand").toString();
			cobrand = new PublisherCobrand(cobrandClassName, cobrandMerchantName);
			code = parameters.get("code").toString();
			
			try
			{
				MerchantCodeGroup mcg = taloolService.getMerchantCodeGroupForCode(code);
				if (mcg == null)
				{
					handleInvalidParams();
				}
				else
				{
					name = mcg.getCodeGroupTitle();
					fundraiserName = mcg.getMerchant().getName();
					Merchant publisher = taloolService.getMerchantById(mcg.getPublisherId());
					publisherName = publisher.getName();
					
					cobrand.init();
					// js behavior to change the body class and inject a co-brand
					add(new CoBrandBehavior(cobrand.cobrandClassName));
				}
				
			}
			catch(ServiceException se)
			{
				LOG.error("Failed to init the cobrand: ", se);
				handleInvalidParams();
			}
		}
		else
		{
			handleInvalidParams();
		}
	}
	
	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		
		final NiceFeedbackPanel feedback = new NiceFeedbackPanel("feedback");
		add(feedback.setOutputMarkupId(true));
		
		add(new Label("n1",name));
		add(new Label("n2",name));
		add(new Label("fn1",fundraiserName));
		add(new Label("fn2",fundraiserName));
		add(new Label("fn3",fundraiserName));
		add(new Label("pn1",publisherName));
		add(new Label("pn2",publisherName));
		add(new Label("pn3",publisherName));
		add(new Label("pn4",publisherName));
		add(new Label("pn5",publisherName));
		add(new Label("pn6",publisherName));
		add(new Label("c1",code));
		
		StringBuilder sb = new StringBuilder();
		if (StringUtils.isEmpty(name.trim()))
		{
			sb.append("that ").append(fundraiserName).append(" gets");
		}
		else
		{
			sb.append(name).append(" and ").append(fundraiserName).append(" both get");
		}
		add(new Label("customNamePhrase",sb.toString()));
		
		PageParameters pp = new PageParameters();
		pp.set(0,cobrandMerchantName);
		pp.set(1,cobrandClassName);
		pp.set(2,code);
		add(new BookmarkablePageLink<String>("salesTracker",FundraiserTracking.class, pp));
		
	}
	
	@Override
	public void handleMobile() {
		// redirect to mobile web (doing it late, so subclasses can redirect in the constructor)
		// throw new RestartResponseException(MobileFundraiserTrackingRegistration.class, this.parameters);
	}
	
	private void handleInvalidParams()
	{
		// invalid url... redirect to homepage
		LOG.error("Params are jacked up, redirecting to the homepage.");
		throw new RestartResponseException(HomePage.class);
	}


}
