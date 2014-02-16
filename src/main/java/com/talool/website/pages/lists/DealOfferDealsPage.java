package com.talool.website.pages.lists;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.core.util.string.JavaScriptUtils;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.Deal;
import com.talool.core.Merchant;
import com.talool.core.MerchantAccount;
import com.talool.core.service.ServiceException;
import com.talool.stats.DealSummary;
import com.talool.website.component.ConfirmationIndicatingAjaxLink;
import com.talool.website.component.StaticImage;
import com.talool.website.models.DealListModel;
import com.talool.website.models.DealModel;
import com.talool.website.pages.BasePage;
import com.talool.website.panel.SubmitCallBack;
import com.talool.website.panel.deal.wizard.DealWizard;
import com.talool.website.util.SecuredPage;
import com.talool.website.util.SessionUtils;

@SecuredPage
public class DealOfferDealsPage extends BasePage
{
	private static final long serialVersionUID = 6008230892463177176L;
	private static final Logger LOG = LoggerFactory.getLogger(DealOfferDealsPage.class);
	private static final String talool = "Talool";
	private static final String CONTAINER_ID = "dealList";
	private static final String REPEATER_ID = "dealRptr";
	private static final String NAVIGATOR_ID = "navigator";
	
	private String sortParameter = "merchantName";
	private boolean isAscending = true;
	private int itemsPerPage = 50;
	
	private UUID _dealOfferId;
	private DealWizard wizard;

	public DealOfferDealsPage(PageParameters parameters)
	{
		super(parameters);
		_dealOfferId = UUID.fromString(parameters.get("id").toString());
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		DealListModel model = new DealListModel();
		model.setDealOfferId(_dealOfferId);

		final WebMarkupContainer container = new WebMarkupContainer(CONTAINER_ID);
		container.setOutputMarkupId(true);
		add(container);

		final DealSummaryDataProvider dataProvider = new DealSummaryDataProvider(_dealOfferId, sortParameter, isAscending);
		final DataView<DealSummary> deals = new DataView<DealSummary>(REPEATER_ID,dataProvider)
		{

			private static final long serialVersionUID = 2705519558987278333L;

			@Override
			protected void populateItem(Item<DealSummary> item)
			{
				DealSummary deal = item.getModelObject();
				final UUID dealId = deal.getDealId();

				item.setModel(new CompoundPropertyModel<DealSummary>(deal));

				if (item.getIndex() % 2 == 0)
				{
					item.add(new AttributeModifier("class", "odd-row-bg"));
				}
				item.add(new Label("merchantName"));
				item.add(new Label("title"));
				item.add(new Label("summary"));
				item.add(new Label("details"));
				
				if (StringUtils.isEmpty(deal.getImageUrl()))
				{
					item.add(new StaticImage("myimage", false, "/img/000.png"));
				}
				else
				{
					item.add(new StaticImage("myimage", false, deal.getImageUrl()));
				}
				

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
				
				StringBuilder confirm = new StringBuilder();
				confirm.append("Are you sure you want to remove \"").append(deal.getTitle()).append("\"?");
				ConfirmationIndicatingAjaxLink<Void> deleteLink = new ConfirmationIndicatingAjaxLink<Void>("deleteLink", JavaScriptUtils.escapeQuotes(confirm.toString()).toString())
				{
					private static final long serialVersionUID = 268692101349122303L;

					@Override
					public void onClick(AjaxRequestTarget target)
					{
						getSession().getFeedbackMessages().clear();
						try 
						{
							// Assign it to Talool and make it not active. 
							// This will hide it from the publisher/merchant without deleting it.
							// If it has ever had active deal acquires, we don't want to delete it because 
							// we want to preserve the history.
							// TODO detected if there are deal acquires, and delete if possible
							Deal deal = taloolService.getDeal(dealId);
							List<Merchant> merchants = taloolService.getMerchantByName(talool);
							Merchant _talool = merchants.get(0);
							deal.setActive(false);
							deal.setMerchant(_talool);
							taloolService.merge(deal);
							target.add(container);
							Session.get().success(deal.getTitle() + " has been sent back to Talool.  Contact us if you want it back.");
						} 
						catch (ServiceException se)
						{
							LOG.error("problem fetcing the talool merchant id", se);
							Session.get().error("There was a problem removing this deal.  Contact us if you want it removed manually.");
						}
						target.add(((BasePage)getPage()).feedback);
						
					}
				};
				item.add(deleteLink);

			}

		};
		deals.setItemsPerPage(itemsPerPage);
		container.add(deals);
		
		final AjaxPagingNavigator pagingNavigator = new AjaxPagingNavigator(NAVIGATOR_ID, deals);
		container.add(pagingNavigator.setOutputMarkupId(true));
		pagingNavigator.setVisible(dataProvider.size() > itemsPerPage);

		container.add(new AjaxLink<Void>("merchantSortLink")
		{
			private static final long serialVersionUID = -4528179721619677443L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				doAjaxSearchRefresh("merchantName", target);
			}
		});
		
		// override the action button
		final BasePage page = (BasePage) this.getPage();
		AjaxLink<Void> actionLink = new AjaxLink<Void>("actionLink")
		{

			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				getSession().getFeedbackMessages().clear();
				MerchantAccount ma = SessionUtils.getSession().getMerchantAccount();
				Deal deal = domainFactory.newDeal(null, ma, true);
				wizard.setModelObject(deal);
				wizard.open(target);
			}
		};
		page.setActionLink(actionLink);
		Label actionLabel = new Label("actionLabel", getNewDefinitionPanelTitle());
		actionLabel.setOutputMarkupId(true);
		actionLink.add(actionLabel);
		actionLink.setOutputMarkupId(true);

		// Wizard
		wizard = new DealWizard("wiz", "Deal Wizard")
		{

			private static final long serialVersionUID = 1L;

			@Override
			protected void onFinish(AjaxRequestTarget target)
			{
				super.onFinish(target);
				target.add(container);
			}
		};
		add(wizard);

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

	@Override
	public String getHeaderTitle()
	{
		MerchantAccount ma = SessionUtils.getSession().getMerchantAccount();
		StringBuilder sb = new StringBuilder(ma.getMerchant().getName());
		sb.append(" > ").append(getPageParameters().get("name")).append(" > Deals");
		return sb.toString();
	}

	@Override
	public Panel getNewDefinitionPanel(String contentId, SubmitCallBack callback)
	{
		return null;
	}

	@Override
	public String getNewDefinitionPanelTitle()
	{
		return "New Deal";
	}
	
	@Override
	public boolean hasActionLink() {
		// TODO return false for Publishers and true for Merchants
		return false;
	}
}
