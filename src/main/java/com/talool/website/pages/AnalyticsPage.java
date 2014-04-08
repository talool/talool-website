package com.talool.website.pages;

import java.util.List;

import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.core.service.AnalyticService.ActivationCodeSummary;
import com.talool.website.models.ActivationCodeSummaryModel;
import com.talool.website.panel.SubmitCallBack;
import com.talool.website.panel.analytics.CubismHorizon;
import com.talool.website.panel.analytics.CubismHorizonFactory;
import com.talool.website.panel.analytics.CubismPanel;
import com.talool.website.panel.analytics.ScoreBoardFactory;
import com.talool.website.util.SecuredPage;

/**
 * 
 * @author dmccuen
 * 
 */
@SecuredPage
public class AnalyticsPage extends BasePage
{
	private static final long serialVersionUID = -6214364791355264043L;

	public AnalyticsPage(PageParameters parameters)
	{
		super(parameters);
	}

	@Override
	public String getHeaderTitle()
	{
		return "Analytics";
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		
		List<CubismHorizon> metrics = CubismHorizonFactory.getKeyMetrics();
		add(new CubismPanel("chart", "Recent Customer Activity", metrics));
		
		List<CubismHorizon> purchaseChart = CubismHorizonFactory.getPurchaseMetrics();
		add(new CubismPanel("purchaseChart", "Breakdown of Recent Purchases", purchaseChart));

		add(ScoreBoardFactory.createTotalCustomers("totalCustomers").setRenderBodyOnly(true));

		add(ScoreBoardFactory.createTotalRedemptions("totalRedemptions").setRenderBodyOnly(true));

		add(ScoreBoardFactory.createTotalEmailGifts("totalEmailGifts").setRenderBodyOnly(true));

		add(ScoreBoardFactory.createTotalFacebookGifts("totalFacebookGifts").setRenderBodyOnly(true));

		add(ScoreBoardFactory.createTotalFacebookCustomers("totalFacebookCustomers").setRenderBodyOnly(true));

		final ListView<ActivationCodeSummary> activationStats = new ListView<ActivationCodeSummary>("rptr",
				new ActivationCodeSummaryModel())
		{

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final ListItem<ActivationCodeSummary> item)
			{
				final ActivationCodeSummary summary = item.getModelObject();
				item.add(ScoreBoardFactory.createTotalBookActivations("metric", summary));
			}

		};

		add(activationStats);

	}

	@Override
	public Panel getNewDefinitionPanel(String contentId, SubmitCallBack callback)
	{
		// Returning null because these will be handled by the tab panels, if at all
		return null;
	}

	@Override
	public String getNewDefinitionPanelTitle()
	{
		// Returning null because these will be handled by the tab panels, if at all
		return null;
	}

	public boolean hasActionLink()
	{
		return false;
	}

}
