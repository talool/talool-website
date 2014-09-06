package com.talool.website.marketing.pages;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.core.FactoryManager;
import com.talool.core.service.TaloolService;
import com.talool.website.behaviors.CoBrandBehavior;
import com.talool.website.panel.merchant.FundraiserTrackingRollupPanel;

public class FundraiserTrackingRollup extends BaseMarketingPage
{
	private static final long serialVersionUID = 8390824646913457971L;
	private static final String panelName = "track_all";
	private PageParameters parameters;
	
	protected transient static final TaloolService taloolService = FactoryManager.get()
			.getServiceFactory().getTaloolService();

	public FundraiserTrackingRollup(PageParameters parameters)
	{
		super(parameters);	

		this.parameters = parameters;
		
		// js behavior to change the body class and inject a co-brand
		if (parameters.getIndexedCount() > 0)
		{
			String cobrandClassName = parameters.get(0).toString();
			add(new CoBrandBehavior(cobrandClassName));
		}
		
		
	}
	
	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		FundraiserTrackingRollupPanel panel = new FundraiserTrackingRollupPanel(panelName, parameters, false);
		panel.setItemsPerPage(25);
		add(panel);
		
	}

}
