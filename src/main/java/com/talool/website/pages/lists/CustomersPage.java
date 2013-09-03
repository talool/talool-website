package com.talool.website.pages.lists;

import java.util.UUID;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.datetime.DateConverter;
import org.apache.wicket.datetime.PatternDateConverter;
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
import com.talool.website.models.CustomerListModel;
import com.talool.website.pages.BasePage;
import com.talool.website.pages.CustomerManagementPage;
import com.talool.website.panel.AdminModalWindow;
import com.talool.website.panel.SubmitCallBack;
import com.talool.website.panel.customer.definition.CustomerPanel;
import com.talool.website.panel.customer.definition.CustomerPurchaseDealOfferPanel;
import com.talool.website.panel.customer.definition.CustomerResetPasswordPanel;
import com.talool.website.util.SecuredPage;

@SecuredPage
public class CustomersPage extends BasePage
{
	private static final long serialVersionUID = 2102415289760762365L;

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

		final ListView<Customer> customers = new ListView<Customer>("customerRptr",
				new CustomerListModel())
		{

			private static final long serialVersionUID = 4104816505968727445L;

			@Override
			protected void populateItem(ListItem<Customer> item)
			{
				Customer customer = item.getModelObject();
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

				PageParameters customerParams = new PageParameters();
				customerParams.set("id", customer.getId());
				customerParams.set("email", customer.getEmail());
				String url = (String) urlFor(CustomerManagementPage.class, customerParams);
				ExternalLink emailLink = new ExternalLink("emailLink", Model.of(url),
						new PropertyModel<String>(customer, "email"));
				item.add(emailLink);

				final AdminModalWindow definitionModal = getModal();
				final SubmitCallBack callback = getCallback(definitionModal);
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
						CustomerResetPasswordPanel panel = new CustomerResetPasswordPanel(definitionModal.getContentId(), callback,
								customerId);
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
						CustomerPurchaseDealOfferPanel panel = new CustomerPurchaseDealOfferPanel(definitionModal.getContentId(), callback,
								customerId);
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
