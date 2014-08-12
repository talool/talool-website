package com.talool.website.panel.merchant;

import java.util.UUID;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.core.util.string.JavaScriptUtils;
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

import com.talool.core.Merchant;
import com.talool.core.service.ServiceException;
import com.talool.stats.MerchantSummary;
import com.talool.website.component.ConfirmationIndicatingAjaxLink;
import com.talool.website.models.MerchantModel;
import com.talool.website.pages.BasePage;
import com.talool.website.pages.FundraiserManagementPage;
import com.talool.website.pages.lists.MerchantSummaryDataProvider;
import com.talool.website.panel.BaseTabPanel;
import com.talool.website.panel.SubmitCallBack;
import com.talool.website.panel.merchant.wizard.MerchantWizard;
import com.talool.website.panel.merchant.wizard.MerchantWizard.MerchantWizardMode;
import com.talool.website.util.SessionUtils;

public class FundraisersPanel extends BaseTabPanel {

	private static final long serialVersionUID = 2170124491668826388L;
	private static final Logger LOG = LoggerFactory.getLogger(FundraisersPanel.class);
	private static final String CONTAINER_ID = "schoolList";
	private static final String REPEATER_ID = "schoolRptr";
	private static final String NAVIGATOR_ID = "navigator";
	
	private String sortParameter = "name";
	private boolean isAscending = true;
	private int itemsPerPage = 20;
	private long itemCount;
	
	private UUID _merchantId;
	private MerchantWizard wizard;

	public FundraisersPanel(String id) {
		super(id);
		_merchantId = SessionUtils.getSession().getMerchantAccount().getMerchant().getId();

	}

	@Override
	protected void onInitialize() {
		super.onInitialize();
		
		final BasePage page = (BasePage) getPage();
		
		final WebMarkupContainer container = new WebMarkupContainer(CONTAINER_ID);
		container.setOutputMarkupId(true);
		add(container);
		
		final MerchantSummaryDataProvider dataProvider = new MerchantSummaryDataProvider(sortParameter, isAscending);
		dataProvider.setFilterFundraiser(true);
		final DataView<MerchantSummary> deals = getDataView(dataProvider);
		container.add(deals);
		
		// Set the labels above the pagination
		itemCount = deals.getItemCount();
		Label totalCount = new Label("totalCount",new PropertyModel<Long>(this, "itemCount"));
		container.add(totalCount.setOutputMarkupId(true));
				
		final AjaxPagingNavigator pagingNavigator = new AjaxPagingNavigator(NAVIGATOR_ID, deals);
		container.add(pagingNavigator.setOutputMarkupId(true));
		pagingNavigator.setVisible(dataProvider.size() > itemsPerPage);
		pagingNavigator.getPagingNavigation().setViewSize(5);

		container.add(new AjaxLink<Void>("nameLink")
		{
			private static final long serialVersionUID = -4528179721619677443L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				doAjaxSearchRefresh("name", target);
			}
		});
		
		// override the action button
		AjaxLink<Void> actionLink = new AjaxLink<Void>("actionLink")
		{

			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				getSession().getFeedbackMessages().clear();
				
				final Merchant merchant = domainFactory.newMerchant();

				merchant.getLocations().iterator().next().
						setCreatedByMerchantAccount(SessionUtils.getSession().getMerchantAccount());
				wizard.setModelObject(merchant);
				wizard.open(target);
			}
		};
		page.setActionLink(actionLink);
		Label actionLabel = new Label("actionLabel", getActionLabel());
		actionLabel.setOutputMarkupId(true);
		actionLink.add(actionLabel);
		actionLink.setOutputMarkupId(true);
		
		// Wizard
		wizard = new MerchantWizard("wiz", "Fundraising Wizard", MerchantWizardMode.FUNDRAISER)
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
		
	}
	
	private AjaxPagingNavigator getPagination(final DataView<MerchantSummary> schools)
	{
		AjaxPagingNavigator pagingNavigator = new AjaxPagingNavigator(NAVIGATOR_ID, schools);
		pagingNavigator.setOutputMarkupId(true);
		pagingNavigator.setVisible(itemCount > itemsPerPage);
		return pagingNavigator;
	}
	
	private DataView<MerchantSummary> getDataView(IDataProvider<MerchantSummary> dataProvider)
	{
		final BasePage page = (BasePage)getPage();
		final DataView<MerchantSummary> schools = new DataView<MerchantSummary>(REPEATER_ID,dataProvider)
		{

			private static final long serialVersionUID = 2705519558987278333L;

			@Override
			protected void populateItem(Item<MerchantSummary> item)
			{
				final MerchantSummary school = item.getModelObject();
				final UUID fundraiserId = school.getMerchantId();

				item.setModel(new CompoundPropertyModel<MerchantSummary>(school));

				if (item.getIndex() % 2 == 0)
				{
					item.add(new AttributeModifier("class", "odd-row-bg"));
				}
				
				PageParameters booksParams = new PageParameters();
				booksParams.set("id", fundraiserId);
				booksParams.set("pid", _merchantId);
				booksParams.set("name", school.getName());
				String url = (String) urlFor(FundraiserManagementPage.class, booksParams);
				ExternalLink nameLink = new ExternalLink("name", Model.of(url),
						new PropertyModel<String>(school, "name"));
				item.add(nameLink);

				item.add(new AjaxLink<Void>("editLink")
				{

					private static final long serialVersionUID = -3574012236379219691L;

					@Override
					public void onClick(AjaxRequestTarget target)
					{
						getSession().getFeedbackMessages().clear();
						wizard.setModelObject(new MerchantModel(fundraiserId, true).getObject());
						wizard.open(target);
					}
				});
				
				StringBuilder confirm = new StringBuilder();
				confirm.append("Are you sure you want to remove \"").append(school.getName()).append("\"?");
				ConfirmationIndicatingAjaxLink<Void> deleteLink = new ConfirmationIndicatingAjaxLink<Void>("deleteLink", JavaScriptUtils.escapeQuotes(confirm.toString()).toString())
				{
					private static final long serialVersionUID = 268692101349122303L;

					@Override
					public void onClick(AjaxRequestTarget target)
					{
						
						
						try 
						{
							getSession().getFeedbackMessages().clear();
							Merchant fundraiser = taloolService.getMerchantById(fundraiserId);
							fundraiser.setIsDiscoverable(false);
							taloolService.save(fundraiser);
							resetPage(target);
							
							Session.get().success(school.getName() + " has been hidden from your customers.  Contact us if you want it back.");
						} 
						catch (ServiceException se)
						{
							LOG.error("problem hiding merchant", se);
							Session.get().error("There was a problem removing this fundraiser.  Contact us if you want it removed manually.");
						}
						target.add(page.feedback);
						
					}
				};
				item.add(deleteLink);

			}

		};
		schools.setItemsPerPage(itemsPerPage);
		return schools;
	}
	
	@SuppressWarnings("unchecked")
	private void doAjaxSearchRefresh(final String sortParam, final AjaxRequestTarget target)
	{
		WebMarkupContainer container = (WebMarkupContainer) get(CONTAINER_ID);

		final DataView<MerchantSummary> dataView = ((DataView<MerchantSummary>) container.get(REPEATER_ID));
		final MerchantSummaryDataProvider provider = (MerchantSummaryDataProvider) dataView.getDataProvider();

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
		final MerchantSummaryDataProvider provider = new MerchantSummaryDataProvider(sortParameter, isAscending);
		final DataView<MerchantSummary> dataView = getDataView(provider);
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
		return "New Organization";
	}

	@Override
	public Panel getNewDefinitionPanel(String contentId, SubmitCallBack callback) {
		return null;
	}
	
}
