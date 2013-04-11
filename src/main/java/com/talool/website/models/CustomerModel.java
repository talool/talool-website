package com.talool.website.models;

import org.apache.wicket.model.LoadableDetachableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.Customer;
import com.talool.core.service.ServiceException;
import com.talool.service.ServiceFactory;

/**
 * 
 * @author dmccuen
 * 
 */
public class CustomerModel extends LoadableDetachableModel<Customer>
{

	private static final long serialVersionUID = -6956910878696402522L;

	private static final Logger LOG = LoggerFactory.getLogger(CustomerModel.class);

	private Long customerId;

	public CustomerModel(final Long customerId)
	{
		this.customerId = customerId;
	}

	@Override
	protected Customer load()
	{

		Customer customer = null;

		try
		{
			customer = ServiceFactory.get().getTaloolService().getCustomerById(customerId);
		}
		catch (ServiceException e)
		{
			LOG.error("problem loading customer", e);
		}

		return customer;
	}

}
