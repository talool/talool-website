package com.talool.website.panel.customer;

import java.util.UUID;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.core.Customer;
import com.talool.website.models.FriendListModel;

public class CustomerFriendsPanel extends Panel
{
	private static final long serialVersionUID = -6839312363625800389L;
	private UUID _customerId;

	public CustomerFriendsPanel(String id, PageParameters parameters)
	{
		super(id);
		_customerId = UUID.fromString(parameters.get("id").toString());

	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		FriendListModel model = new FriendListModel();
		model.setCustomerId(_customerId);
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
					item.add(new AttributeModifier("class", "gray0-bg"));
				}

				item.add(new Label("firstName"));
				item.add(new Label("lastName"));
				item.add(new Label("email"));
			}

		};

		add(customers);
	}

}