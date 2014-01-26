package com.talool.website.pages;

import java.util.UUID;

import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.core.DealOffer;
import com.talool.website.models.DealOfferListModel;
import com.talool.website.models.MetricListModel;
import com.talool.website.models.MetricListModel.CHART_RANGE;
import com.talool.website.models.MetricListModel.CHART_TYPE;
import com.talool.website.panel.SubmitCallBack;
import com.talool.website.panel.analytics.ChartPanel;
import com.talool.website.panel.analytics.ScoreBoardFactory;
import com.talool.website.util.SecuredPage;
import com.talool.website.util.SessionUtils;

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
		return "My Analytics";
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		
		UUID merchantId = SessionUtils.getSession().getMerchantAccount().getMerchant().getId();
		
		MetricListModel customerChartModel = new MetricListModel(merchantId, CHART_RANGE.LAST_6_MONTHS, CHART_TYPE.CUSTOMERS);
		add(new ChartPanel("customerChart","Customers",customerChartModel));
		
		MetricListModel locationChartModel = new MetricListModel(merchantId, CHART_RANGE.LAST_6_MONTHS, CHART_TYPE.CUSTOMER_LOCATIONS);
		add(new ChartPanel("locationChart","Customer Locations",locationChartModel));
		
		MetricListModel bookChartModel = new MetricListModel(merchantId, CHART_RANGE.LAST_6_MONTHS, CHART_TYPE.BOOKS);
		add(new ChartPanel("bookChart","Books",bookChartModel));
		
		MetricListModel purchaseChartModel = new MetricListModel(merchantId, CHART_RANGE.LAST_6_MONTHS, CHART_TYPE.BOOK_PURCHASES);
		add(new ChartPanel("purchaseChart","Book Purchases",purchaseChartModel));
		
		MetricListModel activationChartModel = new MetricListModel(merchantId, CHART_RANGE.LAST_6_MONTHS, CHART_TYPE.BOOKS_ACTIVATIONS);
		add(new ChartPanel("activationChart","Book Activations",activationChartModel));
		
		MetricListModel redemptionChartModel = new MetricListModel(merchantId, CHART_RANGE.LAST_6_MONTHS, CHART_TYPE.REDEMPTIONS);
		add(new ChartPanel("redemptionChart","Redemptions",redemptionChartModel));
		
		MetricListModel giftChartModel = new MetricListModel(merchantId, CHART_RANGE.LAST_6_MONTHS, CHART_TYPE.GIFTS);
		add(new ChartPanel("giftChart","Gifts",giftChartModel));

		add(ScoreBoardFactory.createTotalCustomers("totalCustomers").setRenderBodyOnly(true));

		add(ScoreBoardFactory.createTotalRedemptions("totalRedemptions").setRenderBodyOnly(true));

		add(ScoreBoardFactory.createTotalEmailGifts("totalEmailGifts").setRenderBodyOnly(true));

		add(ScoreBoardFactory.createTotalFacebookGifts("totalFacebookGifts").setRenderBodyOnly(true));

		add(ScoreBoardFactory.createTotalFacebookCustomers("totalFacebookCustomers").setRenderBodyOnly(true));

		final ListView<DealOffer> activationStats = new ListView<DealOffer>("activationStatRptr",
				new DealOfferListModel())
		{

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<DealOffer> item)
			{
				DealOffer offer = item.getModelObject();
				item.add(ScoreBoardFactory.createTotalBookActivations("totalActivations", offer));
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
