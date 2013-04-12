package com.talool.website.panel.customer;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.Customer;
import com.talool.website.models.FriendListModel;
import com.talool.website.panel.AdminModalWindow;
import com.talool.website.panel.BasePanel;
import com.talool.website.panel.CustomerPanel;
import com.talool.website.panel.SubmitCallBack;
import com.talool.website.panel.merchant.MerchantLocationPanel;

public class CustomerFriendsPanel extends BasePanel
{

	private static final Logger LOG = LoggerFactory.getLogger(CustomerFriendsPanel.class);
	private Long _customerId;

	public CustomerFriendsPanel(String id, PageParameters parameters)
	{
		super(id);
		_customerId = parameters.get("id").toLongObject();
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		final WebMarkupContainer container = new WebMarkupContainer("container");
		add(container.setOutputMarkupId(true));
		
		final AdminModalWindow locationModal;
		container.add(locationModal = new AdminModalWindow("modal"));
		final SubmitCallBack callback = new SubmitCallBack()
		{

			@Override
			public void submitSuccess(AjaxRequestTarget target)
			{
				locationModal.close(target);
				target.add(container);
			}

			@Override
			public void submitFailure(AjaxRequestTarget target)
			{

			}
		};

		final CustomerPanel customerPanel = new CustomerPanel(
				locationModal.getContentId(), callback, _customerId);
		locationModal.setContent(customerPanel);
		container.add(new AjaxLink<Void>("customerLink")
		{

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				getSession().getFeedbackMessages().clear();
				locationModal.setTitle((new StringBuilder("Friend of")).append(_customerId).toString());
				locationModal.setContent(new MerchantLocationPanel(locationModal.getContentId(),
						_customerId, callback));
				locationModal.show(target);
			}
		});
		
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