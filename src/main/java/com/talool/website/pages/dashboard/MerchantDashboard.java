package com.talool.website.pages.dashboard;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.website.pages.BaseDashboard;
import com.talool.website.panel.dashboard.ActiveUsersPanel;
import com.talool.website.panel.dashboard.AvailableDealsPanel;
import com.talool.website.panel.dashboard.MerchantReachPanel;
import com.talool.website.panel.dashboard.RecentRedemptionsPanel;
import com.talool.website.util.SecuredPage;

@SecuredPage
public class MerchantDashboard extends BaseDashboard {

	private static final long serialVersionUID = 1L;
	
	public MerchantDashboard()
	{
		super();
	}

	public MerchantDashboard(PageParameters parameters)
	{
		super(parameters);
	}
	
	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		
		add(new AvailableDealsPanel("availableDeals", merchantId));
		add(new RecentRedemptionsPanel("recentRedemptions", merchantId));
		add(new ActiveUsersPanel("activeUsers", merchantId));
		add(new MerchantReachPanel("merchantReaches", merchantId));
		
	}

	@Override
	public boolean hasActionLink() {
		return false;
	}

	@Override
	public String getHeaderTitle() {
		return "Dashboard: "+merchant.getName();
	}
	
	
	
}
