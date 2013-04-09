package com.talool.website.models;

import java.util.List;

import org.apache.wicket.model.LoadableDetachableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.Customer;
import com.talool.core.service.ServiceException;
import com.talool.service.ServiceFactory;

public class FriendListModel extends LoadableDetachableModel<List<Customer>> {

	private static final long serialVersionUID = 8670053407495918990L;
	private static final Logger LOG = LoggerFactory.getLogger(FriendListModel.class);
	private Long _customerId = null;

	@Override
	protected List<Customer> load() {
		List<Customer> customers = null;
		
		try
		{
			if (_customerId != null) 
			{
				customers = ServiceFactory.get().getTaloolService().getFriends(_customerId.longValue());
			}
		}
		catch (ServiceException e)
		{
			LOG.error("problem loading friends", e);
		}
		
		return customers;
	}
	
	public void setCustomerId(long id) {
		_customerId = id;
	}

}
