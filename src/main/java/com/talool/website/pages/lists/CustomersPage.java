package com.talool.website.pages.lists;

import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.datetime.DateConverter;
import org.apache.wicket.datetime.PatternDateConverter;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.core.Customer;
import com.talool.core.DealOfferPurchase;
import com.talool.core.FactoryManager;
import com.talool.core.gift.Gift;
import com.talool.core.gift.GiftStatus;
import com.talool.core.service.AnalyticService;
import com.talool.core.service.CustomerService;
import com.talool.core.service.ServiceException;
import com.talool.website.component.ConfirmationLink;
import com.talool.website.models.CustomerListModel;
import com.talool.website.pages.BasePage;
import com.talool.website.pages.CustomerManagementPage;
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

	private static final long serialVersionUID = 2102415289760762365L;
	
	protected transient static final CustomerService customerService = FactoryManager.get()
			.getServiceFactory().getCustomerService();
	
	protected transient static final AnalyticService analyticService = FactoryManager.get()
			.getServiceFactory().getAnalyticService();

	public CustomersPage()
	{
		super();
	}

	public CustomersPage(PageParameters parameters)
	{
		super(parameters);
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		final StringBuilder sb = new StringBuilder();

		final ListView<Customer> customers = new ListView<Customer>("customerRptr",
				new CustomerListModel())
		{

			private static final long serialVersionUID = 4104816505968727445L;

			@Override
			protected void populateItem(ListItem<Customer> item)
			{
				sb.setLength(0);
				Customer customer = item.getModelObject();
				final String email = customer.getEmail();
				final UUID customerId = customer.getId();

				item.setModel(new CompoundPropertyModel<Customer>(customer));

				if (item.getIndex() % 2 == 0)
				{
					item.add(new AttributeModifier("class", "odd-row-bg"));
				}

				item.add(new Label("firstName"));
				item.add(new Label("lastName"));

				DateConverter converter = new PatternDateConverter("MM/dd/yyyy", false);
				String createdOn = converter.convertToString(customer.getCreated(), getLocale());
				item.add(new Label("creationDate", createdOn));
				
				String offerTitle = new String();
				long redemptionCount = 0;
				long giftCount = 0;
				try 
				{
					List<DealOfferPurchase> offers = customerService.getDealOfferPurchasesByCustomerId(customerId);
					if (offers.size()==1)
					{
						offerTitle = offers.get(0).getDealOffer().getTitle();
					}
					else if (offers.size()==0)
					{
						offerTitle = "no offers";
					}
					else
					{
						offerTitle = "multiple offers";
					}
					List<Gift> gifts = customerService.getGifts(customerId, GiftStatus.values());
					giftCount = gifts.size();
					redemptionCount = analyticService.getTotalRedemptions(customerId);
				}
				catch (ServiceException e)
				{
					LOG.error(e.getLocalizedMessage(), e);
					SessionUtils.errorMessage("There was a problem getting stats for " + email);
				}
				item.add(new Label("books",offerTitle));
				item.add(new Label("redemptions",redemptionCount));
				item.add(new Label("gifts",giftCount));

				PageParameters customerParams = new PageParameters();
				customerParams.set("id", customer.getId());
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
					item.add(new ConfirmationLink<Void>("deleteCustomer", sb.toString())
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
				}
				else
				{
					WebMarkupContainer deleteCustomer = new WebMarkupContainer("deleteCustomer");
					item.add(deleteCustomer.setVisible(false));
				}

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

		};

		add(customers);
	}

	@Override
	public String getHeaderTitle()
	{
		return "Customers";
	}

	@Override
	public Panel getNewDefinitionPanel(String contentId, SubmitCallBack callback)
	{
		return new CustomerPanel(contentId, callback);
	}

	@Override
	public String getNewDefinitionPanelTitle()
	{
		return "Create New Customer";
	}

}
