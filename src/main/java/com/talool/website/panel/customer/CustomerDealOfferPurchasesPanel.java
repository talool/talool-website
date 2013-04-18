package com.talool.website.panel.customer;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.core.DealOfferPurchase;
import com.talool.website.models.DealOfferPurchaseListModel;

public class CustomerDealOfferPurchasesPanel extends Panel
{
	private static final long serialVersionUID = -1572792713158372783L;
	private String _customerId;

	public CustomerDealOfferPurchasesPanel(String id, PageParameters parameters)
	{
		super(id);
		_customerId = parameters.get("id").toString();
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		DealOfferPurchaseListModel model = new DealOfferPurchaseListModel();
		model.setCustomerId(_customerId);
		final ListView<DealOfferPurchase> dops = new ListView<DealOfferPurchase>("rptr", model)
		{

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<DealOfferPurchase> item)
			{
				DealOfferPurchase dop = item.getModelObject();

				item.setModel(new CompoundPropertyModel<DealOfferPurchase>(dop));

				if (item.getIndex() % 2 == 0)
				{
					item.add(new AttributeModifier("class", "gray0-bg"));
				}

				item.add(new Label("dealOffer.title"));
				item.add(new Label("dealOffer.price"));
			}

		};

		add(dops);
	}

}