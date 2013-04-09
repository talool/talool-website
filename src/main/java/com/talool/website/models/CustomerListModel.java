package com.talool.website.models;

import java.util.List;

import org.apache.wicket.model.LoadableDetachableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.Customer;
import com.talool.core.service.ServiceException;
import com.talool.service.ServiceFactory;

public class CustomerListModel extends LoadableDetachableModel<List<Customer>> {

	private static final long serialVersionUID = -2068941114791569781L;
	private static final Logger LOG = LoggerFactory.getLogger(CustomerListModel.class);

	@Override
	protected List<Customer> load() {
		List<Customer> customers = null;
		
		try
		{
			customers = ServiceFactory.get().getTaloolService().getCustomers();
		}
		catch (ServiceException e)
		{
			LOG.error("problem loading customers", e);
		}
		
		return customers;
	}

}
