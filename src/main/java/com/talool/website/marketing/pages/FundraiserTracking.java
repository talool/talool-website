package com.talool.website.marketing.pages;

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.core.FactoryManager;
import com.talool.core.service.TaloolService;
import com.talool.website.behaviors.CoBrandBehavior;
import com.talool.website.marketing.pages.mobile.MobileFundraiserTrackingPage;
import com.talool.website.marketing.panel.TrackingPanel;

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

		// js behavior to change the body class and inject a co-brand
		String cobrandName = parameters.get(0).toString();
		add(new CoBrandBehavior(cobrandName));
		
		if (parameters.getIndexedCount()==2){
			code = parameters.get(1).toString();
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
		throw new RestartResponseException(MobileFundraiserTrackingPage.class, this.parameters);
	}

}