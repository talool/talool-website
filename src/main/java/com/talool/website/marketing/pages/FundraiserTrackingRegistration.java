package com.talool.website.marketing.pages;

import com.talool.core.FactoryManager;
import com.talool.core.service.TaloolService;
import com.talool.website.behaviors.CoBrandBehavior;
import com.talool.website.marketing.pages.mobile.MobileFundraiserTrackingRegistration;
import com.talool.website.marketing.panel.TrackingRegistrationClosedPanel;
import com.talool.website.marketing.panel.TrackingRegistrationPanel;
import com.talool.website.util.PermissionUtils;
import com.talool.website.util.PublisherCobrand;
import org.apache.log4j.Logger;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class FundraiserTrackingRegistration extends BaseMarketingPage
{
	private static final long serialVersionUID = 8390824646913457971L;
	private static final Logger LOG = Logger.getLogger(FundraiserTrackingRegistration.class);
	private static final String panelName = "trackme";
	
	private PublisherCobrand cobrand;
	private String fundraiserName;
	
	protected transient static final TaloolService taloolService = FactoryManager.get()
			.getServiceFactory().getTaloolService();

	public FundraiserTrackingRegistration(PageParameters parameters)
	{
		super(parameters);	

		// This page requires a co-brand (even if it is ours)
		if (!parameters.isEmpty() && parameters.getIndexedCount()>=2)
		{
			String cobrandName = parameters.get(0).toString();
			cobrand = new PublisherCobrand(cobrandName);
			if (cobrand.hasCobrand())
			{
				// js behavior to change the body class and inject a co-brand
				add(new CoBrandBehavior(cobrand));

				if (parameters.getIndexedCount()==3)
				{
					fundraiserName = parameters.get(2).toString();
				}
			}
			else
			{
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

		if (PermissionUtils.isTrackingOpen(cobrand.getPublisher().getId()))
		{
			TrackingRegistrationPanel panel = new TrackingRegistrationPanel(panelName, cobrand);
			if (fundraiserName != null) panel.setFundraiserName(fundraiserName);
			add(panel);
		}
		else
		{
			add(new TrackingRegistrationClosedPanel(panelName));
		}

	}
	
	@Override
	public void handleMobile() {
		// redirect to mobile web (doing it late, so subclasses can redirect in the constructor)
		throw new RestartResponseException(MobileFundraiserTrackingRegistration.class, this.parameters);
	}
	
	private void handleInvalidParams()
	{
		// invalid url... redirect to homepage
		LOG.error("Params are jacked up, redirecting to the homepage.");
		throw new RestartResponseException(HomePage.class);
	}


}
