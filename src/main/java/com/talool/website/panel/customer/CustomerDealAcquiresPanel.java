package com.talool.website.panel.customer;

import java.util.UUID;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.core.DealAcquire;
import com.talool.website.models.DealAcquireListModel;

public class CustomerDealAcquiresPanel extends Panel
{
	private static final long serialVersionUID = 4601133594874157184L;
	private UUID _customerId;

	public CustomerDealAcquiresPanel(String id, PageParameters parameters)
	{
		super(id);
		_customerId = UUID.fromString(parameters.get("id").toString());
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		DealAcquireListModel model = new DealAcquireListModel();
		model.setCustomerId(_customerId);
		final ListView<DealAcquire> dealAcquires = new ListView<DealAcquire>("rptr", model)
		{

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<DealAcquire> item)
			{
				DealAcquire dealAcquire = item.getModelObject();

				item.setModel(new CompoundPropertyModel<DealAcquire>(dealAcquire));

				if (item.getIndex() % 2 == 0)
				{
					item.add(new AttributeModifier("class", "odd-row-bg"));
				}

				item.add(new Label("deal.title"));
				item.add(new Label("deal.dealOffer.title"));
				item.add(new Label("deal.merchant.name"));
				item.add(new Label("redemptionDate"));
				item.add(new Label("acquireStatus"));

				
			}

		};

		add(dealAcquires);
	}

}