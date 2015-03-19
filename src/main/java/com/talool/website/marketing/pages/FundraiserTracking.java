package com.talool.website.marketing.pages;

import com.talool.core.FactoryManager;
import com.talool.core.MerchantCodeGroup;
import com.talool.core.service.ServiceException;
import com.talool.core.service.TaloolService;
import com.talool.website.behaviors.CoBrandBehavior;
import com.talool.website.marketing.pages.mobile.MobileFundraiserTracking;
import com.talool.website.marketing.panel.TrackingPanel;
import com.talool.website.util.PublisherCobrand;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class FundraiserTracking extends BaseMarketingPage
{
	private static final long serialVersionUID = 8390824646913457971L;
	private static final String panelName = "trackme";
	private String code;
	
	protected transient static final TaloolService taloolService = FactoryManager.get()
			.getServiceFactory().getTaloolService();

	public FundraiserTracking(PageParameters parameters)
	{
		super(parameters);	

		if (parameters.getIndexedCount()==3){
			code = parameters.get(2).toString();
			try
			{
				MerchantCodeGroup mcg = taloolService.getMerchantCodeGroupForCode(code);
				// js behavior to change the body class and inject a co-brand
				add(new CoBrandBehavior(new PublisherCobrand(mcg.getPublisherId())));
			}
			catch (ServiceException e) {}

		}
		
	}
	
	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		add(new TrackingPanel(panelName, code));
		
	}
	
	@Override
	public void handleMobile() {
		// redirect to mobile web (doing it late, so subclasses can redirect in the constructor)
		throw new RestartResponseException(MobileFundraiserTracking.class, this.parameters);
	}

}
