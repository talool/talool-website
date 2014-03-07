package com.talool.website.panel.merchant;

import java.util.UUID;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.website.models.MetricListModel;
import com.talool.website.models.MetricListModel.CHART_RANGE;
import com.talool.website.models.MetricListModel.CHART_TYPE;
import com.talool.website.pages.BasePage;
import com.talool.website.panel.BaseTabPanel;
import com.talool.website.panel.SubmitCallBack;
import com.talool.website.panel.analytics.ChartPanel;
import com.talool.website.panel.dashboard.ActiveUsersPanel;
import com.talool.website.panel.dashboard.AvailableDealsPanel;
import com.talool.website.panel.dashboard.MerchantReachPanel;
import com.talool.website.panel.dashboard.RecentRedemptionsPanel;

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
		
		MetricListModel customerChartModel = new MetricListModel(_merchantId, CHART_RANGE.LAST_6_MONTHS, CHART_TYPE.CUSTOMERS);
		addOrReplace(new ChartPanel("customerChart", "Customers", customerChartModel));
		
		MetricListModel redemptionChartModel = new MetricListModel(_merchantId, CHART_RANGE.LAST_6_MONTHS, CHART_TYPE.REDEMPTIONS);
		addOrReplace(new ChartPanel("redemptionChart", "Redemptions", redemptionChartModel));
		
		MetricListModel giftChartModel = new MetricListModel(_merchantId, CHART_RANGE.LAST_6_MONTHS, CHART_TYPE.GIFTS);
		addOrReplace(new ChartPanel("giftChart", "Gifts", giftChartModel));
		
		MetricListModel dealChartModel = new MetricListModel(_merchantId, CHART_RANGE.LAST_6_MONTHS, CHART_TYPE.POPULAR_DEALS);
		addOrReplace(new ChartPanel("dealChart", "Popular Deals", dealChartModel));
		
		add(new AvailableDealsPanel("availableDeals", _merchantId));
		add(new RecentRedemptionsPanel("recentRedemptions", _merchantId));
		add(new ActiveUsersPanel("activeUsers", _merchantId));
		add(new MerchantReachPanel("merchantReaches", _merchantId));
		
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
