package com.talool.website.models;

import org.apache.wicket.model.LoadableDetachableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.SearchOptions;
import com.talool.core.service.ServiceException;
import com.talool.service.ServiceFactory;
import com.talool.stats.CustomerSummary;
import com.talool.stats.PaginatedResult;

/**
 * 
 * @author clintz
 * 
 */
public class CustomerSummaryModel extends LoadableDetachableModel<PaginatedResult<CustomerSummary>>
{

	private static final long serialVersionUID = -2068941114791569781L;
	private static final Logger LOG = LoggerFactory.getLogger(CustomerSummaryModel.class);

	private final SearchOptions searchOpts;

	public CustomerSummaryModel(final SearchOptions searchOpts)
	{
		this.searchOpts = searchOpts;
	}

	@Override
	protected PaginatedResult<CustomerSummary> load()
	{
		PaginatedResult<CustomerSummary> results = null;

		try
		{
			results = ServiceFactory.get().getCustomerService().getCustomerSummary(searchOpts, true);
		}
		catch (ServiceException e)
		{
			LOG.error("problem loading customer summary", e);
		}

		return results;
	}

}
