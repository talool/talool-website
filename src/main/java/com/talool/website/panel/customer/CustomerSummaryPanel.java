package com.talool.website.panel.customer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.core.Customer;
import com.talool.core.Sex;
import com.talool.website.models.CustomerModel;
import com.talool.website.models.DevicePresenceListModel;

public class CustomerSummaryPanel extends Panel {

	private static final long serialVersionUID = 1282313643836725998L;
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
	private UUID _customerId;
	
	public CustomerSummaryPanel(String id, PageParameters parameters)
	{
		super(id);
		_customerId = UUID.fromString(parameters.get("id").toString());
		CustomerModel model = new CustomerModel(_customerId);
		setDefaultModel(model);
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		
		CustomerModel model = (CustomerModel) getDefaultModel();
		Customer customer = model.getObject();
		
		add(new DevicePresencePanel("devices", new DevicePresenceListModel(_customerId)));
		
		add(new Label("name", customer.getFullName()));
		add(new Label("email", customer.getEmail()));
		
		Sex sex = customer.getSex();
		if (sex != null)
		{
			add(new Label("sex", sex.name()));
		}
		else
		{
			add(new Label("sex","unknown"));
		}
		
		
		add(new Label("regDate", dateFormat.format(customer.getCreated())));
		
		Date bday = customer.getBirthDate();
		if (bday != null)
		{
			add(new Label("bDate", dateFormat.format(bday)));
		}
		else
		{
			add(new Label("bDate","unknown"));
		}
	}
}
