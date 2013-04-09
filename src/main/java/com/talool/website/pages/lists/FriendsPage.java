package com.talool.website.pages.lists;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.Customer;
import com.talool.core.service.ServiceException;
import com.talool.service.ServiceFactory;
import com.talool.website.models.FriendListModel;
import com.talool.website.pages.BasePage;
import com.talool.website.panel.AdminMenuPanel;

public class FriendsPage extends BasePage {
	
	private static final long serialVersionUID = 3634980968241854373L;
	private static final Logger LOG = LoggerFactory.getLogger(FriendsPage.class);
	private Long _customerId;

	public FriendsPage()
	{
		super();
	}

	public FriendsPage(PageParameters parameters)
	{
		super(parameters);
		_customerId = parameters.get("id").toLongObject();
	}
	
	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		add(new AdminMenuPanel("adminMenuPanel").setRenderBodyOnly(true));
		
		StringBuffer pageTitle = new StringBuffer("Friends of ");
		FriendListModel model = new FriendListModel();
		
		try {
			Customer customer = ServiceFactory.get().getTaloolService().getCustomerById(_customerId);
			pageTitle.append(customer.getFirstName()).append(" ").append(customer.getLastName());
			model.setCustomerId(customer.getId());
		} catch (ServiceException se) {
			LOG.error("problem loading customer", se);
		}
		
		add(new Label("pageTitle",pageTitle.toString()));
		
		final ListView<Customer> customers = new ListView<Customer>("customerRptr", model)
		{

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<Customer> item)
			{
				Customer customer = item.getModelObject();

				item.setModel(new CompoundPropertyModel<Customer>(customer));

				if (item.getIndex() % 2 == 0)
				{
					item.add(new AttributeModifier("class", "odd"));
				}
				else
				{
					item.add(new AttributeModifier("class", "even"));
				}

				item.add(new Label("firstName"));
				item.add(new Label("lastName"));
				item.add(new Label("email"));
			}

		};

		add(customers);
	}

}