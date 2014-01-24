package com.talool.website.pages.lists;

import java.util.UUID;

import org.apache.log4j.Logger;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.core.request.handler.PageProvider;
import org.apache.wicket.core.request.handler.RenderPageRequestHandler.RedirectPolicy;
import org.apache.wicket.datetime.DateConverter;
import org.apache.wicket.datetime.PatternDateConverter;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.core.FactoryManager;
import com.talool.core.service.AnalyticService;
import com.talool.core.service.CustomerService;
import com.talool.core.service.ServiceException;
import com.talool.stats.CustomerSummary;
import com.talool.website.component.ConfirmationAjaxLink;
import com.talool.website.pages.BasePage;
import com.talool.website.pages.CustomerManagementPage;
import com.talool.website.pages.CustomerSearchPage;
import com.talool.website.panel.AdminModalWindow;
import com.talool.website.panel.SubmitCallBack;
import com.talool.website.panel.customer.definition.CustomerPanel;
import com.talool.website.panel.customer.definition.CustomerPurchaseDealOfferPanel;
import com.talool.website.panel.customer.definition.CustomerResetPasswordPanel;
import com.talool.website.service.PermissionService;
import com.talool.website.util.SecuredPage;
import com.talool.website.util.SessionUtils;

@SecuredPage
public class CustomersPage extends BasePage
{
	private static final Logger LOG = Logger.getLogger(CustomersPage.class);
	private static final String CUST_CONTAINER_ID = "customerContainer";

	private static final long serialVersionUID = 2102415289760762365L;

	protected transient static final CustomerService customerService = FactoryManager.get()
			.getServiceFactory().getCustomerService();

	protected transient static final AnalyticService analyticService = FactoryManager.get()
			.getServiceFactory().getAnalyticService();

	private String sortParameter = "redemptions";
	private boolean isAscending = false;
	private int itemsPerPage = 50;

	public CustomersPage()
	{
		super();
		if (!PermissionService.get().canViewAllCustomers(SessionUtils.getSession().getMerchantAccount().getEmail()))
		{
			throw new RestartResponseException(
					new PageProvider(
							CustomerSearchPage.class, null),
					RedirectPolicy.NEVER_REDIRECT);
		}
	}

	public CustomersPage(PageParameters parameters)
	{
		super(parameters);
		if (parameters.get("sortParam") != null)
		{
			sortParameter = parameters.get("sortParam").toString();
		}
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		final StringBuilder sb = new StringBuilder();

		final WebMarkupContainer customerContainer = new WebMarkupContainer(CUST_CONTAINER_ID);
		add(customerContainer.setOutputMarkupId(true));

		final DataView<CustomerSummary> customers = new DataView<CustomerSummary>("customerRptr",
				new CustomerSummaryDataProvider(sortParameter, isAscending))
		{

			private static final long serialVersionUID = 4104816505968727445L;

			@Override
			protected void populateItem(Item<CustomerSummary> item)
			{
				sb.setLength(0);
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
					sb.append("Are you sure you want to delete ").append(email).append(" ?");
					item.add(new ConfirmationAjaxLink<Void>("deleteCustomer", sb.toString())
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

		customerContainer.add(customers);
		customers.setItemsPerPage(itemsPerPage);

		final AjaxPagingNavigator pagingNavigator = new AjaxPagingNavigator("navigator", customers);

		// pagingNavigator.setVisible(customers.size() > 0);

		customerContainer.add(pagingNavigator.setOutputMarkupId(true));

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

	@Override
	public String getHeaderTitle()
	{
		return "Customers";
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

	@Override
	public boolean hasActionLink()
	{
		return false;
	}

}
