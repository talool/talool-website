package com.talool.website.pages;

import java.util.Arrays;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.datetime.DateConverter;
import org.apache.wicket.datetime.PatternDateConverter;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.core.service.ServiceException;
import com.talool.stats.CustomerSummary;
import com.talool.website.component.ConfirmationAjaxLink;
import com.talool.website.pages.lists.CustomerSummaryDataProvider;
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
	private static final String CUST_CONTAINER_ID = "customerContainer";

	private String sortParameter = "redemptions";
	private boolean isAscending = false;

	private static final ChoiceRenderer<SearchType> searchChoiceRenderer = new ChoiceRenderer<SearchType>(
			"displayVal", "name");

	private enum SearchType
	{
		emailAddress("Email Address"), mostRecent("Most Recent");

		private String displayVal;

		private SearchType(String displayVal)
		{
			this.displayVal = displayVal;
		}

		public String getDisplayVal()
		{
			return displayVal;
		}
	};

	private SearchType selectedSearchType = SearchType.emailAddress;

	private String elementId;

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

		Form<Void> form = new Form<Void>("searchForm")
		{
			private static final long serialVersionUID = -477362909507132959L;

			@Override
			protected void onSubmit()
			{
				doSearch();
			}
		};

		form.add(new DropDownChoice<SearchType>("searchSelect",
				new PropertyModel<SearchType>(this, "selectedSearchType"), Arrays.asList(SearchType.values()),
				searchChoiceRenderer));

		add(form);
		form.add(new TextField<String>("elementId", new PropertyModel<String>(this, "elementId"), String.class).setRequired(true));

		final WebMarkupContainer customerContainer = new WebMarkupContainer(CUST_CONTAINER_ID);
		add(customerContainer.setOutputMarkupId(true).setVisible(false).setOutputMarkupPlaceholderTag(true));

		WebMarkupContainer customers = new WebMarkupContainer("customerRptr");
		WebMarkupContainer navigator = new WebMarkupContainer("navigator");

		customerContainer.add(navigator.setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true));

		customerContainer.add(customers);

		customerContainer.add(new AjaxLink<Void>("customerLink")
		{
			private static final long serialVersionUID = -4528179721619677443L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				doAjaxSearchRefresh("lastName", target);
			}
		});

		customerContainer.add(new AjaxLink<Void>("registrationLink")
		{
			private static final long serialVersionUID = -4528179721619677443L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				doAjaxSearchRefresh("registrationDate", target);
			}
		});

		customerContainer.add(new AjaxLink<Void>("redemptionLink")
		{
			private static final long serialVersionUID = -4528179721619677443L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				doAjaxSearchRefresh("redemptions", target);
			}
		});

		if (elementId != null)
		{
			doSearch();
		}

	}

	public boolean hasActionLink()
	{
		return false;
	}

	private void doSearch()
	{

		final DataView<CustomerSummary> customers = new DataView<CustomerSummary>("customerRptr",
				new CustomerSearchDataProvider(sortParameter, isAscending, elementId))
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

		WebMarkupContainer customerContainer = (WebMarkupContainer) CustomerSearchPage.this.get(CUST_CONTAINER_ID);

		customers.setItemsPerPage(ITEMS_PER_PAGE);
		customerContainer.replace(customers);

		final AjaxPagingNavigator pagingNavigator = new
				AjaxPagingNavigator("navigator", customers);

		customerContainer.replace(pagingNavigator);

		if (customers.getDataProvider().size() == 0)
		{
			customerContainer.setVisible(false);
			SessionUtils.errorMessage("There are no customers matching '" + elementId + "'");
		}
		else
		{
			customerContainer.setVisible(true);
		}

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

	@SuppressWarnings("unchecked")
	private void doAjaxSearchRefresh(final String sortParam, final AjaxRequestTarget target)
	{
		WebMarkupContainer container = (WebMarkupContainer) get(CUST_CONTAINER_ID);

		final DataView<CustomerSummary> dataView = ((DataView<CustomerSummary>) container.get("customerRptr"));
		final CustomerSummaryDataProvider provider = (CustomerSummaryDataProvider) dataView.getDataProvider();

		// toggle asc/desc
		if (sortParam.equals(sortParameter))
		{
			isAscending = isAscending == true ? false : true;
			provider.setAscending(isAscending);
		}

		this.sortParameter = sortParam;

		provider.setSortParameter(sortParam);

		final AjaxPagingNavigator pagingNavigator = (AjaxPagingNavigator) container.get("navigator");
		pagingNavigator.getPageable().setCurrentPage(0);

		target.add(container);
		target.add(pagingNavigator);

	}

}