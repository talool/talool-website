package com.talool.website.panel.dealoffer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigation;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.Deal;
import com.talool.core.MediaType;
import com.talool.core.MerchantMedia;
import com.talool.core.service.ServiceException;
import com.talool.stats.DealSummary;
import com.talool.website.component.DealDeleteLink;
import com.talool.website.component.DealMover;
import com.talool.website.models.DealModel;
import com.talool.website.pages.BasePage;
import com.talool.website.pages.MerchantManagementPage;
import com.talool.website.pages.lists.DealSummaryDataProvider;
import com.talool.website.panel.BaseTabPanel;
import com.talool.website.panel.SubmitCallBack;
import com.talool.website.panel.deal.wizard.DealWizard;
import com.talool.website.panel.image.EditableImage;
import com.talool.website.util.PermissionUtils;
import com.talool.website.util.SessionUtils;

public class DealOfferDealsPanel extends BaseTabPanel {

	private static final long serialVersionUID = -6358605032461151690L;
	
	private static final Logger LOG = LoggerFactory.getLogger(DealOfferDealsPanel.class);
	private static final String talool = "Talool";
	private static final String CONTAINER_ID = "dealList";
	private static final String REPEATER_ID = "dealRptr";
	private static final String NAVIGATOR_ID = "navigator";
	
	private String sortParameter = "merchantName";
	private boolean isAscending = true;
	private int itemsPerPage = 20;
	private long itemCount;
	private Map<UUID,DealSummary> selectedDeals = new HashMap<UUID,DealSummary>();
	
	private UUID _dealOfferId;
	private DealWizard wizard;
	private boolean bulkMoveEnabled;
	private DealMover mover;

	public DealOfferDealsPanel(String id, PageParameters parameters) {
		super(id);
		_dealOfferId = UUID.fromString(parameters.get("id").toString());
		
		bulkMoveEnabled = PermissionUtils.canMoveDeals(SessionUtils.getSession().getMerchantAccount());
	}
	
	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		final WebMarkupContainer container = new WebMarkupContainer(CONTAINER_ID);
		container.setOutputMarkupId(true);
		add(container);
		
		mover = new DealMover("mover", new PropertyModel<Map<UUID,DealSummary>>(this,"selectedDeals")){
			private static final long serialVersionUID = 1L;

			@Override
			public void onMove(AjaxRequestTarget target) {
				resetPage(target);
				target.add(((BasePage)this.getPage()).feedback);
				selectedDeals = new HashMap<UUID,DealSummary>();
				mover.reset(target);
				target.add(mover);
			}
			
		};
		container.add(mover.setOutputMarkupId(true).setVisible(bulkMoveEnabled));

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

		container.add(new AjaxLink<Void>("merchantSortLink")
		{
			private static final long serialVersionUID = -4528179721619677443L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				doAjaxSearchRefresh("merchantName", target);
			}
		});
		
		container.add(new AjaxLink<Void>("citySortLink")
		{
			private static final long serialVersionUID = -4528179721619677443L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				doAjaxSearchRefresh("merchantCity", target);
			}
		});
		
		container.add(new AjaxLink<Void>("stateSortLink")
		{
			private static final long serialVersionUID = -4528179721619677443L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				doAjaxSearchRefresh("merchantState", target);
			}
		});
		
		container.add(new AjaxLink<Void>("redemptionSortLink")
				{
					private static final long serialVersionUID = -4528179721619677443L;

					@Override
					public void onClick(AjaxRequestTarget target)
					{
						doAjaxSearchRefresh("redemptionCount", target);
					}
				});
		
		// Wizard
		wizard = new DealWizard("dealWiz", "Deal Wizard")
		{

			private static final long serialVersionUID = 1L;

			@Override
			protected void onFinish(AjaxRequestTarget target)
			{
				super.onFinish(target);
				// refresh the list after a deal is edited
				target.add(container);
			}
		};
		addOrReplace(wizard.setOutputMarkupId(true));
		
		// hide the action button
		final BasePage page = (BasePage) getPage();
		page.getActionLink().add(new AttributeModifier("class","hide"));
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
				final UUID dealId = deal.getDealId();

				item.setModel(new CompoundPropertyModel<DealSummary>(deal));

				if (item.getIndex() % 2 == 0)
				{
					item.add(new AttributeModifier("class", "odd-row-bg"));
				}
				
				boolean isSelected = false;
				AjaxCheckBox cb = new AjaxCheckBox("bulkCheckbox", new Model<Boolean>(isSelected)){

					private static final long serialVersionUID = 1L;

					@Override
					protected void onUpdate(AjaxRequestTarget target) {
						if (getModelObject())
						{
							selectedDeals.put(dealId, deal);
						}
						else
						{
							selectedDeals.remove(dealId);
						}
						mover.reset(target);
						target.add(mover);
					}
					
				};
				item.add(cb.setVisible(bulkMoveEnabled));
				
				PageParameters booksParams = new PageParameters();
				booksParams.set("id", deal.getMerchantId());
				booksParams.set("name", deal.getMerchantName());
				String url = (String) urlFor(MerchantManagementPage.class, booksParams);
				ExternalLink namelLink = new ExternalLink("merchantLink", Model.of(url),
						new PropertyModel<String>(deal, "merchantName"));
				item.add(namelLink);
				
				item.add(new Label("merchantCity"));
				item.add(new Label("merchantState"));
				item.add(new Label("title"));
				item.add(new Label("summary"));
				item.add(new Label("details"));
				item.add(new Label("expires"));
				
				item.add(new Label("acquireCount"));
				item.add(new Label("redemptionCount"));
				item.add(new Label("giftCount"));
				
				item.add(new EditableImage("editableImage",Model.of(deal.getImageUrl()), deal.getMerchantId(), MediaType.DEAL_IMAGE)
				{

					private static final long serialVersionUID = 1L;

					@Override
					public void onMediaUploadComplete(AjaxRequestTarget target, MerchantMedia media) 
					{
						try
						{
							Deal d = taloolService.getDeal(dealId);
							d.setImage(media);
							taloolService.merge(d);
							target.add(this);
						}
						catch (ServiceException se)
						{
							LOG.error("Failed to save new image with managedLocation",se);
						}
						
					}
					
				});
				

				item.add(new AjaxLink<Void>("editLink")
				{

					private static final long serialVersionUID = -3574012236379219691L;

					@Override
					public void onClick(AjaxRequestTarget target)
					{
						getSession().getFeedbackMessages().clear();
						wizard.setModelObject(new DealModel(dealId, true).getObject());
						wizard.open(target);
					}
				});
				
				final BasePage page = (BasePage)getPage();
				DealDeleteLink deleteLink = new DealDeleteLink("deleteLink", deal)
				{
					private static final long serialVersionUID = 268692101349122303L;

					@Override
					public void onDeleteComplete(boolean success,
							AjaxRequestTarget target) {
						if (success) resetPage(target);
						target.add(page.feedback);
					}
				};
				item.add(deleteLink);

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
	public String getActionLabel() {
		return "";
	}

	@Override
	public Panel getNewDefinitionPanel(String contentId, SubmitCallBack callback) {
		return null;
	}

}
