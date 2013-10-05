package com.talool.website.models;

import java.util.List;

import org.apache.wicket.model.LoadableDetachableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.service.CustomerSummary;
import com.talool.core.service.ServiceException;
import com.talool.service.ServiceFactory;

/**
 * 
 * @author clintz
 * 
 */
public class CustomerSummaryModel extends LoadableDetachableModel<List<CustomerSummary>>
{

	private static final long serialVersionUID = -2068941114791569781L;
	private static final Logger LOG = LoggerFactory.getLogger(CustomerSummaryModel.class);

	@Override
	protected List<CustomerSummary> load()
	{
		List<CustomerSummary> customers = null;

		try
		{
			customers = ServiceFactory.get().getCustomerService().getCustomerSummary(null);
		}
		catch (ServiceException e)
		{
			LOG.error("problem loading customer summary", e);
		}

		return customers;
	}

}
