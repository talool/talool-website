package com.talool.website.panel.dealoffer;

import java.util.List;
import java.util.UUID;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.website.models.DealOfferModel;
import com.talool.website.pages.BasePage;
import com.talool.website.panel.BaseTabPanel;
import com.talool.website.panel.SubmitCallBack;
import com.talool.website.panel.analytics.CubismHorizon;
import com.talool.website.panel.analytics.CubismHorizonFactory;
import com.talool.website.panel.analytics.CubismPanel;
import com.talool.website.panel.dashboard.ActiveUsersPanel;
import com.talool.website.panel.dashboard.RecentRedemptionsPanel;

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
		
		DealOfferModel model = new DealOfferModel(_dealOfferId);
		List<CubismHorizon> metrics = CubismHorizonFactory.getPurchaseMetrics(model.getObject());
		add(new CubismPanel("chart", "Purchase Activity", metrics));
		
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
