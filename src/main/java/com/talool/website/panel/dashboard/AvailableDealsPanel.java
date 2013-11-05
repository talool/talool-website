package com.talool.website.panel.dashboard;

import java.util.UUID;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import com.talool.core.service.AnalyticService.AvailableDeal;
import com.talool.website.models.AvailableDealListModel;

public class AvailableDealsPanel extends Panel {

	private static final long serialVersionUID = 1L;
	protected UUID merchantId;

	public AvailableDealsPanel(String id, UUID merchantId)
	{
		super(id);
		this.merchantId = merchantId;
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();
		
		AvailableDealListModel model = new AvailableDealListModel();
		model.setMerchantId(merchantId);
		final ListView<AvailableDeal> deals = new ListView<AvailableDeal>("rptr", model)
		{

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<AvailableDeal> item)
			{
				AvailableDeal deal = item.getModelObject();

				item.setModel(new CompoundPropertyModel<AvailableDeal>(deal));

				item.add(new Label("title"));
				item.add(new Label("redemptionCount"));

			}

		};
		add(deals);
	}
	
	
	
}
