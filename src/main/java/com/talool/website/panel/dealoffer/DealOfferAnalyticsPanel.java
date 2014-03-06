package com.talool.website.panel.dealoffer;

import java.util.UUID;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.website.models.MetricListModel;
import com.talool.website.models.MetricListModel.CHART_RANGE;
import com.talool.website.models.MetricListModel.CHART_TYPE;
import com.talool.website.panel.BaseTabPanel;
import com.talool.website.panel.SubmitCallBack;
import com.talool.website.panel.analytics.ChartPanel;

public class DealOfferAnalyticsPanel extends BaseTabPanel {

	private static final long serialVersionUID = 522375793977325630L;

	private UUID _dealOfferId;
	
	public DealOfferAnalyticsPanel(String id, PageParameters parameters) {
		super(id);
		_dealOfferId = UUID.fromString(parameters.get("id").toString());
	}
	
	@Override
	protected void onInitialize() {
		super.onInitialize();
		
		MetricListModel redemptionChartModel = new MetricListModel(_dealOfferId, CHART_RANGE.LAST_6_MONTHS, CHART_TYPE.REDEMPTIONS);
		addOrReplace(new ChartPanel("redemptionChart", "Redemptions", redemptionChartModel));
		
		MetricListModel activationChartModel = new MetricListModel(_dealOfferId, CHART_RANGE.LAST_6_MONTHS, CHART_TYPE.BOOKS_ACTIVATIONS);
		addOrReplace(new ChartPanel("activationChart", "Book Activations", activationChartModel));
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
