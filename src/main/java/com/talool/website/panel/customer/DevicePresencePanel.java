package com.talool.website.panel.customer;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import com.talool.core.DevicePresence;
import com.talool.website.models.DevicePresenceListModel;

public class DevicePresencePanel extends Panel {

	private static final long serialVersionUID = 2117461773541188816L;
	
	private DevicePresenceListModel listModel;

	public DevicePresencePanel(String id, DevicePresenceListModel model) {
		super(id, model);
		
		listModel = model;
	}
	
	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		final ListView<DevicePresence> devices = new ListView<DevicePresence>("deviceRptr", listModel)
		{

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<DevicePresence> item)
			{
				DevicePresence device = item.getModelObject();

				item.setModel(new CompoundPropertyModel<DevicePresence>(device));

				if (item.getIndex() % 2 == 0)
				{
					item.add(new AttributeModifier("class", "odd-row-bg"));
				}

				item.add(new Label("deviceType"));
				item.add(new Label("deviceOsVersion"));
				item.add(new Label("deviceToken"));
				item.add(new Label("userAgent"));
				item.add(new Label("taloolVersion"));
				item.add(new Label("city"));
				item.add(new Label("stateCode"));
				item.add(new Label("zip"));
				item.add(new Label("ip"));
				item.add(new Label("updated"));
			}

		};

		add(devices);
	}

}
