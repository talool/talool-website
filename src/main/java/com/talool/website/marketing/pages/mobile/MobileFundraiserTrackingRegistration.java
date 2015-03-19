package com.talool.website.marketing.pages.mobile;

import com.talool.website.marketing.panel.TrackingRegistrationClosedPanel;
import com.talool.website.marketing.panel.TrackingRegistrationPanel;
import com.talool.website.util.PermissionUtils;
import com.talool.website.util.PublisherCobrand;
import org.apache.log4j.Logger;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class MobileFundraiserTrackingRegistration extends MobilePage {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(MobileFundraiserTrackingRegistration.class);
	private static final String panelName = "trackme";
	private PublisherCobrand cobrand;
	private String fundraiserName;

	public MobileFundraiserTrackingRegistration(PageParameters parameters)
	{
		super(parameters);
		
		// This page requires a co-brand (even if it is ours)
		if (!parameters.isEmpty() && parameters.getIndexedCount()>=2)
		{
			String cobrandName = parameters.get(0).toString();
			cobrand = new PublisherCobrand(cobrandName);
			if (cobrand.hasCobrand())
			{
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
	
	private void handleInvalidParams()
	{
		// invalid url... redirect to homepage
		LOG.error("Params are jacked up, redirecting to the mobile homepage.");
		throw new RestartResponseException(MobileHomePage.class);
	}
}
