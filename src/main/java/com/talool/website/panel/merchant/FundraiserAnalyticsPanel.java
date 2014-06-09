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
import com.talool.website.util.PermissionUtils;
import com.talool.website.util.SessionUtils;

public class FundraiserAnalyticsPanel extends BaseTabPanel
{

	private static final long serialVersionUID = 522375793977325630L;

	private UUID _fundraiserId;
	private UUID _publisherId;

	public FundraiserAnalyticsPanel(String id, PageParameters parameters)
	{
		super(id);
		_fundraiserId = UUID.fromString(parameters.get("id").toString());
		_publisherId = UUID.fromString(parameters.get("pid").toString());
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		List<CubismHorizon> metrics = CubismHorizonFactory.getFundraiserPurchaseMetricsByFundraiserId(_fundraiserId);
		CubismPanel chart = new CubismPanel("chart", "Purchase Activity", metrics);
		add(chart.setVisible(PermissionUtils.canViewAnalytics(SessionUtils.getSession().getMerchantAccount())));

		// hide the action button
		final BasePage page = (BasePage) this.getPage();
		page.getActionLink().add(new AttributeModifier("class", "hide"));
	}

	@Override
	public String getActionLabel()
	{
		return null;
	}

	@Override
	public Panel getNewDefinitionPanel(String contentId, SubmitCallBack callback)
	{
		return null;
	}

}
