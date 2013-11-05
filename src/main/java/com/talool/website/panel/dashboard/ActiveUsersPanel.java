package com.talool.website.panel.dashboard;

import java.util.UUID;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import com.talool.core.service.AnalyticService.ActiveUser;
import com.talool.website.models.ActiveUserListModel;
public class ActiveUsersPanel extends Panel {

	private static final long serialVersionUID = 1L;
	protected UUID merchantId;

	public ActiveUsersPanel(String id, UUID merchantId)
	{
		super(id);
		this.merchantId = merchantId;
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();
		
		ActiveUserListModel model = new ActiveUserListModel();
		model.setMerchantId(merchantId);
		final ListView<ActiveUser> users = new ListView<ActiveUser>("rptr", model)
		{

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<ActiveUser> item)
			{
				ActiveUser user = item.getModelObject();

				item.setModel(new CompoundPropertyModel<ActiveUser>(user));

				item.add(new Label("customerName"));
				item.add(new Label("dealCount"));

			}

		};
		add(users);
	}
	
	
	
}
