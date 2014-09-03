package com.talool.website.panel.merchant;

import java.util.List;
import java.util.UUID;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.website.pages.BasePage;
import com.talool.website.panel.BaseTabPanel;
import com.talool.website.panel.SubmitCallBack;
import com.talool.website.panel.analytics.CubismHorizon;
import com.talool.website.panel.analytics.CubismHorizonFactory;
import com.talool.website.panel.analytics.CubismPanel;
import com.talool.website.panel.dashboard.ActiveUsersPanel;
import com.talool.website.panel.dashboard.AvailableDealsPanel;
import com.talool.website.panel.dashboard.MerchantReachPanel;
import com.talool.website.panel.dashboard.MerchantRecentRedemptionsPanel;

public class MerchantAnalyticsPanel extends BaseTabPanel {

	private static final long serialVersionUID = 522375793977325630L;

	private UUID _merchantId;
	
	public MerchantAnalyticsPanel(String id, PageParameters parameters) {
		super(id);
		_merchantId = UUID.fromString(parameters.get("id").toString());
	}
	
	@Override
	protected void onInitialize() {
		super.onInitialize();
		
		List<CubismHorizon> metrics = CubismHorizonFactory.getRedemptionMetricsByMerchantId(_merchantId);
		add(new CubismPanel("chart", "Redemption Activity", metrics));
		
		add(new AvailableDealsPanel("availableDeals", _merchantId));
		add(new MerchantRecentRedemptionsPanel("recentRedemptions", _merchantId));
		add(new ActiveUsersPanel("activeUsers", _merchantId));
		
		// hide the action button
		final BasePage page = (BasePage) this.getPage();
		page.getActionLink().add(new AttributeModifier("class","hide"));
	}

	@Override
	public String getActionLabel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Panel getNewDefinitionPanel(String contentId, SubmitCallBack callback) {
		// TODO Auto-generated method stub
		return null;
	}

}
