package com.talool.website.marketing.pages.mobile;

import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.core.Merchant;
import com.talool.core.service.ServiceException;
import com.talool.service.ServiceFactory;
import com.talool.website.marketing.panel.TrackingRegistrationClosedPanel;
import com.talool.website.marketing.panel.TrackingRegistrationPanel;
import com.talool.website.util.PermissionUtils;

public class MobileFundraiserTrackingPage extends MobilePage {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(MobileFundraiserTrackingPage.class);
	private static final String payback = "Payback Book";
	private static final String panelName = "trackme";

	public MobileFundraiserTrackingPage(PageParameters parameters)
	{
		super(parameters);
		
		// js behavior to change the body class and inject a co-brand
		//String cobrandName = parameters.get(0).toString();
		//add(new CoBrandBehavior(cobrandName));
	}
	
	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		
		try
		{
			// TODO don't hardcode Ted, make this more flexible
			List<Merchant> teds = ServiceFactory.get().getTaloolService().getMerchantByName(payback);
			Merchant ted = teds.get(0);
			UUID publisherId = ted.getId();
			long merchantAccountId = ted.getMerchantAccounts().iterator().next().getId();
			
			if (PermissionUtils.isTrackingOpen(publisherId))
			{
				add(new TrackingRegistrationPanel(panelName, ted, merchantAccountId));
			}
			else
			{
				add(new TrackingRegistrationClosedPanel(panelName));
			}
			
		}
		catch (ServiceException se)
		{
			LOG.error("Failed to find payback", se);
			add(new TrackingRegistrationClosedPanel(panelName));
		}
	}
}
