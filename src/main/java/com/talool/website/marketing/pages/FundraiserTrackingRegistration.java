package com.talool.website.marketing.pages;

import org.apache.log4j.Logger;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.core.FactoryManager;
import com.talool.core.service.ServiceException;
import com.talool.core.service.TaloolService;
import com.talool.website.behaviors.CoBrandBehavior;
import com.talool.website.marketing.pages.mobile.MobileFundraiserTrackingRegistration;
import com.talool.website.marketing.panel.TrackingRegistrationClosedPanel;
import com.talool.website.marketing.panel.TrackingRegistrationPanel;
import com.talool.website.util.PermissionUtils;
import com.talool.website.util.PublisherCobrand;

public class FundraiserTrackingRegistration extends BaseMarketingPage
{
	private static final long serialVersionUID = 8390824646913457971L;
	private static final Logger LOG = Logger.getLogger(FundraiserTrackingRegistration.class);
	private static final String panelName = "trackme";
	
	private PublisherCobrand cobrand;
	
	protected transient static final TaloolService taloolService = FactoryManager.get()
			.getServiceFactory().getTaloolService();

	public FundraiserTrackingRegistration(PageParameters parameters)
	{
		super(parameters);	

		// This page requires a co-brand (even if it is ours)
		if (!parameters.isEmpty() && parameters.getIndexedCount()>=2)
		{
			String cobrandMerchantName = parameters.get(0).toString();
			String cobrandClassName = parameters.get(1).toString();
			cobrand = new PublisherCobrand(cobrandClassName, cobrandMerchantName);
			try
			{
				cobrand.init();
				// js behavior to change the body class and inject a co-brand
				add(new CoBrandBehavior(cobrand.cobrandClassName));
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

		if (PermissionUtils.isTrackingOpen(cobrand.publisher.getId()))
		{
			add(new TrackingRegistrationPanel(panelName, cobrand));
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
