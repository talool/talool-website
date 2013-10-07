package com.talool.website.pages.lists;

import java.util.Iterator;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

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
public class CustomerSummaryDataProvider implements IDataProvider<CustomerSummary>
{
	private static final long serialVersionUID = 5376503635036180549L;

	private boolean isAscending = false;
	private String sortParameter;

	public CustomerSummaryDataProvider(final String sortParameter, final boolean isAscending)
	{
		this.isAscending = isAscending;
		this.sortParameter = sortParameter;
	}

	@Override
	public Iterator<? extends CustomerSummary> iterator(long first, long count)
	{
		Iterator<? extends CustomerSummary> iter = null;

		final SearchOptions searchOpts = new SearchOptions.Builder().
				sortProperty(sortParameter).ascending(isAscending).maxResults(Integer.valueOf(String.valueOf(count)))
				.firstResult(first).build();

		try
		{
			PaginatedResult<CustomerSummary> result = ServiceFactory.get().getCustomerService().getCustomerSummary(searchOpts, false);
			if (result != null)
			{
				iter = result.getResults().iterator();
			}
		}
		catch (ServiceException e)
		{
			e.printStackTrace();
		}

		return iter;

	}

	@Override
	public long size()
	{
		long cnt = 0;
		try
		{
			cnt = ServiceFactory.get().getCustomerService().getCustomerSummaryCount();
		}
		catch (ServiceException e)
		{
			e.printStackTrace();
		}
		return cnt;
	}

	@Override
	public IModel<CustomerSummary> model(CustomerSummary object)
	{
		return Model.of(object);
	}

	public boolean isAscending()
	{
		return isAscending;
	}

	public void setAscending(boolean isAscending)
	{
		this.isAscending = isAscending;
	}

	public String getSortParameter()
	{
		return sortParameter;
	}

	public void setSortParameter(String sortParameter)
	{
		this.sortParameter = sortParameter;
	}

	@Override
	public void detach()
	{
		// TODO Auto-generated method stub
	}

}
