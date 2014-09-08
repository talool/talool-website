package com.talool.website.marketing.pages.seo;

import java.util.UUID;

import org.apache.log4j.Logger;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigation;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.core.DealOffer;
import com.talool.core.FactoryManager;
import com.talool.core.service.ServiceException;
import com.talool.core.service.TaloolService;
import com.talool.stats.DealSummary;
import com.talool.website.behaviors.CoBrandBehavior;
import com.talool.website.component.StaticImage;
import com.talool.website.marketing.pages.BaseMarketingPage;
import com.talool.website.marketing.pages.HomePage;
import com.talool.website.pages.lists.DealSummaryDataProvider;
import com.talool.website.util.PublisherCobrand;

public class DealOfferDealsSummaryPage extends BaseMarketingPage
{
	private static final long serialVersionUID = 8390824646913457971L;
	private static final Logger LOG = Logger.getLogger(DealOfferDealsSummaryPage.class);
	private static final String CONTAINER_ID = "dealList";
	private static final String REPEATER_ID = "dealRptr";
	private static final String NAVIGATOR_ID = "navigator";
	
	private String cobrandMerchantName;
	private String cobrandClassName;
	private PublisherCobrand cobrand;
	
	private UUID _dealOfferId;
	private DealOffer dealOffer;
	
	private String sortParameter = "merchantName";
	private boolean isAscending = true;
	private int itemsPerPage = 20;
	private long itemCount;
	
	protected transient static final TaloolService taloolService = FactoryManager.get()
			.getServiceFactory().getTaloolService();

	public DealOfferDealsSummaryPage(PageParameters parameters)
	{
		super(parameters);	

		// This page requires a co-brand (even if it is ours)
		if (!parameters.isEmpty())
		{
			cobrandMerchantName = parameters.get("merchant").toString();
			cobrandClassName = parameters.get("cobrand").toString();
			cobrand = new PublisherCobrand(cobrandClassName, cobrandMerchantName);
			
			String id = parameters.get("id").toString();
			_dealOfferId = UUID.fromString(id);
			
			try
			{
				dealOffer = taloolService.getDealOffer(_dealOfferId);
				cobrand.init();
				// js behavior to change the body class and inject a co-brand
				add(new CoBrandBehavior(cobrand.cobrandClassName));
			}
			catch(ServiceException se)
			{
				LOG.error("Failed to init the cobrand: ", se);
				handleInvalidParams();
			}
		}
		else
		{
			handleInvalidParams();
		}
	}
	
	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		
		add(new Label("offerTitle", dealOffer.getTitle()));
		add(new Label("offerSummary", dealOffer.getSummary()));
		
		final WebMarkupContainer container = new WebMarkupContainer(CONTAINER_ID);
		container.setOutputMarkupId(true);
		add(container);

		final DealSummaryDataProvider dataProvider = new DealSummaryDataProvider(_dealOfferId, sortParameter, isAscending);
		final DataView<DealSummary> deals = getDataView(dataProvider);
		container.add(deals);
		
		// Set the labels above the pagination
		itemCount = deals.getItemCount();
		Label totalCount = new Label("totalCount",new PropertyModel<Long>(this, "itemCount"));
		container.add(totalCount.setOutputMarkupId(true));
				
		final AjaxPagingNavigator pagingNavigator = new AjaxPagingNavigator(NAVIGATOR_ID, deals);
		container.add(pagingNavigator.setOutputMarkupId(true));
		pagingNavigator.setVisible(dataProvider.size() > itemsPerPage);
		pagingNavigator.getPagingNavigation().setViewSize(5);
		
	}
	
	private AjaxPagingNavigator getPagination(final DataView<DealSummary> deals)
	{
		AjaxPagingNavigator pagingNavigator = new AjaxPagingNavigator(NAVIGATOR_ID, deals);
		pagingNavigator.setOutputMarkupId(true);
		pagingNavigator.setVisible(itemCount > itemsPerPage);
		return pagingNavigator;
	}
	
	private DataView<DealSummary> getDataView(IDataProvider<DealSummary> dataProvider)
	{
		final DataView<DealSummary> deals = new DataView<DealSummary>(REPEATER_ID,dataProvider)
		{

			private static final long serialVersionUID = 2705519558987278333L;

			@Override
			protected void populateItem(Item<DealSummary> item)
			{
				final DealSummary deal = item.getModelObject();

				item.setModel(new CompoundPropertyModel<DealSummary>(deal));

				if (item.getIndex() % 2 == 0)
				{
					item.add(new AttributeModifier("class", "odd-row-bg"));
				}
				
				item.add(new StaticImage("imageUrl", false));
				
				item.add(new Label("merchantName"));
				item.add(new Label("merchantCity"));
				item.add(new Label("merchantState"));
				item.add(new Label("title"));
				item.add(new Label("summary"));
				item.add(new Label("details"));
				item.add(new Label("tags"));
				item.add(new Label("expires"));
				item.setVisible(deal.getIsActive());
			}

		};
		deals.setItemsPerPage(itemsPerPage);
		return deals;
	}
	
	@SuppressWarnings("unchecked")
	private void doAjaxSearchRefresh(final String sortParam, final AjaxRequestTarget target)
	{
		WebMarkupContainer container = (WebMarkupContainer) get(CONTAINER_ID);

		final DataView<DealSummary> dataView = ((DataView<DealSummary>) container.get(REPEATER_ID));
		final DealSummaryDataProvider provider = (DealSummaryDataProvider) dataView.getDataProvider();

		// toggle asc/desc
		if (sortParam.equals(sortParameter))
		{
			isAscending = isAscending == true ? false : true;
			provider.setAscending(isAscending);
		}

		this.sortParameter = sortParam;

		provider.setSortParameter(sortParam);

		final AjaxPagingNavigator pagingNavigator = (AjaxPagingNavigator) container.get(NAVIGATOR_ID);
		pagingNavigator.getPageable().setCurrentPage(0);

		target.add(container);
		target.add(pagingNavigator);

	}
	
	private void resetPage(final AjaxRequestTarget target)
	{
		// refresh the list after a book is edited
		final WebMarkupContainer container = (WebMarkupContainer) get(CONTAINER_ID);
		final DealSummaryDataProvider provider = new DealSummaryDataProvider(_dealOfferId, sortParameter, isAscending);
		final DataView<DealSummary> dataView = getDataView(provider);
		container.replace(dataView);
		itemCount = provider.size();
		target.add(container);
		
		// replace the pagination
		final AjaxPagingNavigator pagingNavigator = getPagination(dataView);
		container.replace(pagingNavigator);
		target.add(pagingNavigator);
		
		PagingNavigation nav = pagingNavigator.getPagingNavigation();
		if (nav != null)
		{
			nav.setViewSize(5);
		}

	}
	
	@Override
	public void handleMobile() {
		// redirect to mobile web (doing it late, so subclasses can redirect in the constructor)
		// throw new RestartResponseException(MobileFundraiserTrackingRegistration.class, this.parameters);
	}
	
	private void handleInvalidParams()
	{
		// invalid url... redirect to homepage
		LOG.error("Params are jacked up, redirecting to the homepage.");
		throw new RestartResponseException(HomePage.class);
	}


}
