package com.talool.website.panel.dashboard;

import java.util.UUID;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import com.talool.core.service.AnalyticService.RecentRedemption;
import com.talool.website.models.RecentRedemptionListModel;

public class RecentRedemptionsPanel extends Panel {

	private static final long serialVersionUID = 1L;
	protected UUID merchantId;

	public RecentRedemptionsPanel(String id, UUID merchantId)
	{
		super(id);
		this.merchantId = merchantId;
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();
		
		RecentRedemptionListModel model = new RecentRedemptionListModel();
		model.setMerchantId(merchantId);
		final ListView<RecentRedemption> redemptions = new ListView<RecentRedemption>("rptr", model)
				{

					private static final long serialVersionUID = 1L;

					@Override
					protected void populateItem(ListItem<RecentRedemption> item)
					{
						RecentRedemption redemption = item.getModelObject();

						item.setModel(new CompoundPropertyModel<RecentRedemption>(redemption));

						item.add(new Label("title"));
						item.add(new Label("customerName"));
						item.add(new Label("code"));
						item.add(new Label("date"));

					}

				};
				add(redemptions);
	}
	
	
	
}
