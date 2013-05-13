package com.talool.website.pages.dashboard;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.website.pages.BaseDashboard;
import com.talool.website.panel.dashboard.DealsModule;
import com.talool.website.panel.dashboard.IPhoneModule;
import com.talool.website.panel.dashboard.LocationsModule;
import com.talool.website.panel.dashboard.Module;
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

		add(new Module("detailsModule", merchantId));
		add(new Module("accountsModule", merchantId));
		add(new LocationsModule("locationsModule", merchantId));
		add(new IPhoneModule("offersModule", merchantId));
		add(new DealsModule("dealsModule", merchantId));
		
	}
	
}
