package com.talool.website.pages;

import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.datetime.DateConverter;
import org.apache.wicket.datetime.PatternDateConverter;
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

import com.talool.core.service.ServiceException;
import com.talool.stats.CustomerSummary;
import com.talool.website.component.ConfirmationAjaxLink;
import com.talool.website.component.CustomerSearchPanel;
import com.talool.website.pages.CustomerSearchDataProvider.CustomerSearchOpts;
import com.talool.website.pages.CustomerSearchDataProvider.CustomerSearchOpts.CustomerSearchType;
import com.talool.website.pages.lists.CustomersPage;
import com.talool.website.panel.AdminModalWindow;
import com.talool.website.panel.SubmitCallBack;
import com.talool.website.panel.customer.definition.CustomerPanel;
import com.talool.website.panel.customer.definition.CustomerPurchaseDealOfferPanel;
import com.talool.website.panel.customer.definition.CustomerResetPasswordPanel;
import com.talool.website.service.PermissionService;
import com.talool.website.util.SecuredPage;
import com.talool.website.util.SessionUtils;

/**
 * History page. Keep in mind a DealAcquire object only has history records when
 * updates occur. In other words, if a DealAcquire record is created, that
 * record represent the current state.
 * 
 * It isn't until a state change, gift, etc take place (an update) which forms a
 * DealAcquireHistory record. This saves persistence space by not forming
 * duplicate records until updates occur. Therefore special cases occur when
 * looking into the history: either a DealAcquire record exists (current state)
 * or a DealAcquire record + any DealAcquireHistory formed from updates.
 * 
 * 
 * @author clintz
 * 
 */
@SecuredPage
public class CustomerSearchPage extends BasePage
{
	private static final long serialVersionUID = 2102415289760762365L;
	private static final Logger LOG = Logger.getLogger(CustomerSearchPage.class);
	private static final int ITEMS_PER_PAGE = 50;
	private static final int MOS_RECENT_MAX_RESULTS = 50;
	private static final String CUST_CONTAINER_ID = "customerContainer";
	private static final String REPEATER_ID = "customerRptr";
	private static final String NAVIGATOR_ID = "navigator";
	private static final String VISIBLE_COUNT_ID = "visibleCount";
	
	private CustomerSearchPanel searchPanel;
	private long itemCount;
	private long visibleCount;

	private String sortParameter = "registrationDate";
	private boolean isAscending = false;

	public CustomerSearchPage()
	{
		super();
	}

	public CustomerSearchPage(PageParameters parameters)
	{
		super(parameters);
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		final WebMarkupContainer customerContainer = new WebMarkupContainer(CUST_CONTAINER_ID);
		add(customerContainer.setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true));

		CustomerSearchDataProvider provider = getProvider("");
		final DataView<CustomerSummary> customers = getDataView(provider);
		customerContainer.add(customers);
		
		searchPanel = new CustomerSearchPanel("searchForm"){

			private static final long serialVersionUID = 1L;

			@Override
			public void onSearch(AjaxRequestTarget target, String customerEmail) {
				WebMarkupContainer container = (WebMarkupContainer) getPage().get(CUST_CONTAINER_ID);

				// replace the data view
				CustomerSearchDataProvider provider = getProvider(customerEmail);
				final DataView<CustomerSummary> dataView = getDataView(provider);
				container.replace(dataView);
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
				
				// update the labels above the pagination
				itemCount = provider.getTrueSize();
				visibleCount = Math.min(ITEMS_PER_PAGE, itemCount);
				pagingNavigator.setVisible(itemCount > ITEMS_PER_PAGE);
				if (itemCount==0)
				{
					SessionUtils.errorMessage("There are no customers matching '" + searchPanel.getCustomerEmail() + "'");
				}
				target.add(feedback);
			}
			
		};
		customerContainer.add(searchPanel);
		
		// Set the labels above the pagination
		itemCount = provider.getTrueSize();
		visibleCount = Math.min(ITEMS_PER_PAGE, itemCount);
		customerContainer.add(new Label("customerCount",new PropertyModel<Long>(this, "itemCount")).setOutputMarkupId(true));
		customerContainer.add(new Label("visibleCount",new PropertyModel<Long>(this, VISIBLE_COUNT_ID)).setOutputMarkupId(true));
		// Set the pagination
		customerContainer.add(getPagination(customers));

	}
	
	private AjaxPagingNavigator getPagination(final DataView<CustomerSummary> customers)
	{
		AjaxPagingNavigator pagingNavigator = new AjaxPagingNavigator(NAVIGATOR_ID, customers){

			private static final long serialVersionUID = 1L;

			@Override
			protected void onAjaxEvent(AjaxRequestTarget target) {
				super.onAjaxEvent(target);
				
				// update the label showing the current number of items on the page
				long pageNum = customers.getCurrentPage();
				long itemCount = customers.getItemCount();
				long itemsRemaining = itemCount - pageNum*ITEMS_PER_PAGE;
				visibleCount = Math.min(ITEMS_PER_PAGE, itemsRemaining);
				WebMarkupContainer container = (WebMarkupContainer) getPage().get(CUST_CONTAINER_ID);
				Label visibleCount = (Label) container.get(VISIBLE_COUNT_ID);
				target.add(visibleCount);
			}
			
		};
		pagingNavigator.setOutputMarkupId(true);
		pagingNavigator.setVisible(itemCount > ITEMS_PER_PAGE);
		
		return pagingNavigator;
	}
	
	private CustomerSearchDataProvider getProvider(String customerEmail)
	{
		CustomerSearchDataProvider provider = null;
		if (StringUtils.isEmpty(customerEmail))
		{
			provider = new CustomerSearchDataProvider(new CustomerSearchOpts(
					CustomerSearchType.RecentRegistrations, sortParameter, isAscending).setCappedResultCount(MOS_RECENT_MAX_RESULTS));
			SessionUtils.infoMessage("Showing up to "+MOS_RECENT_MAX_RESULTS+" of the most recent registrations.  Search by email to find specific customers.");
		}
		else
		{
			provider = new CustomerSearchDataProvider(new CustomerSearchOpts(
					CustomerSearchType.PublisherCustomerSummaryByEmail, sortParameter, isAscending).setEmail(customerEmail));
		}
		
		return provider;
	}
	
	private DataView<CustomerSummary> getDataView(IDataProvider<CustomerSummary> provider)
	{
		DataView<CustomerSummary> customers = new DataView<CustomerSummary>(REPEATER_ID, provider)
		{

			private static final long serialVersionUID = -5170887821646081691L;

			@Override
			protected void populateItem(Item<CustomerSummary> item)
			{
				CustomerSummary customer = item.getModelObject();
				final String email = customer.getEmail();
				final UUID customerId = customer.getCustomerId();

				item.setModel(new CompoundPropertyModel<CustomerSummary>(customer));

				if (item.getIndex() % 2 == 0)
				{
					item.add(new AttributeModifier("class", "odd-row-bg"));
				}

				item.add(new Label("firstName"));
				item.add(new Label("lastName"));

				DateConverter converter = new PatternDateConverter("MM/dd/yyyy", false);
				String createdOn = converter.convertToString(customer.getRegistrationDate(), getLocale());
				item.add(new Label("registrationDate", createdOn));

				item.add(new Label("commaSeperatedDealOfferTitles"));
				item.add(new Label("redemptions"));

				PageParameters customerParams = new PageParameters();
				customerParams.set("id", customer.getCustomerId());
				customerParams.set("email", customer.getEmail());
				String url = (String) urlFor(CustomerManagementPage.class, customerParams);
				ExternalLink emailLink = new ExternalLink("emailLink", Model.of(url),
						new PropertyModel<String>(customer, "email"));
				item.add(emailLink);

				final AdminModalWindow definitionModal = getModal();
				final SubmitCallBack callback = getCallback(definitionModal);

				if (PermissionService.get().canDeleteCustomer(
						SessionUtils.getSession().getMerchantAccount().getEmail()))
				{

					item.add(new ConfirmationAjaxLink<Void>("deleteCustomer", "Are you sure you want to delete " + email + " ?")
					{

						private static final long serialVersionUID = -4592149231430681542L;

						@Override
						public void onClick(AjaxRequestTarget target)
						{
							getSession().getFeedbackMessages().clear();
							try
							{
								taloolService.deleteCustomer(customerId);
								setResponsePage(CustomersPage.class);
							}
							catch (ServiceException e)
							{
								LOG.error(e.getLocalizedMessage(), e);
								SessionUtils.errorMessage("There was a problem deleting " + email);
							}

						}
					});

					item.add(new AjaxLink<Void>("editLink")
					{

						private static final long serialVersionUID = -4592149231430681542L;

						@Override
						public void onClick(AjaxRequestTarget target)
						{
							getSession().getFeedbackMessages().clear();
							CustomerPanel panel = new CustomerPanel(definitionModal.getContentId(), callback,
									customerId);
							definitionModal.setContent(panel);
							definitionModal.setTitle("Edit Customer");
							definitionModal.show(target);
						}
					});

					item.add(new AjaxLink<Void>("pwLink")
					{

						private static final long serialVersionUID = 8581489018535203283L;

						@Override
						public void onClick(AjaxRequestTarget target)
						{
							getSession().getFeedbackMessages().clear();
							CustomerResetPasswordPanel panel = new CustomerResetPasswordPanel(definitionModal
									.getContentId(), callback, customerId);
							definitionModal.setContent(panel);
							definitionModal.setTitle("Reset Password");
							definitionModal.show(target);
						}
					});

					item.add(new AjaxLink<Void>("dealOfferLink")
					{

						private static final long serialVersionUID = 7381871678482983865L;

						@Override
						public void onClick(AjaxRequestTarget target)
						{
							getSession().getFeedbackMessages().clear();
							CustomerPurchaseDealOfferPanel panel = new CustomerPurchaseDealOfferPanel(
									definitionModal.getContentId(), callback, customerId);
							definitionModal.setContent(panel);
							definitionModal.setTitle("Purchase Deal Offer");
							definitionModal.show(target);
						}
					});
				}
				else
				{
					WebMarkupContainer deleteCustomer = new WebMarkupContainer("deleteCustomer");
					item.add(deleteCustomer.setVisible(false));

					WebMarkupContainer editLink = new WebMarkupContainer("editLink");
					item.add(editLink.setVisible(false));

					WebMarkupContainer pwLink = new WebMarkupContainer("pwLink");
					item.add(pwLink.setVisible(false));

					WebMarkupContainer dealOfferLink = new WebMarkupContainer("dealOfferLink");
					item.add(dealOfferLink.setVisible(false));
				}

			}

		};
		customers.setItemsPerPage(ITEMS_PER_PAGE);
		return customers;
	}

	public boolean hasActionLink()
	{
		return false;
	}

	@Override
	public String getHeaderTitle()
	{
		return "Customer Search";
	}

	@Override
	public Panel getNewDefinitionPanel(String contentId, SubmitCallBack callback)
	{
		return null;
	}

	@Override
	public String getNewDefinitionPanelTitle()
	{
		return null;
	}
}
