package com.talool.website.pages;

import java.util.Iterator;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.talool.core.SearchOptions;
import com.talool.core.service.ServiceException;
import com.talool.service.ServiceFactory;
import com.talool.stats.CustomerSummary;
import com.talool.stats.PaginatedResult;
import com.talool.website.util.SessionUtils;

/**
 * 
 * @author clintz
 * 
 */
public class CustomerSearchDataProvider implements IDataProvider<CustomerSummary>
{
	private static final long serialVersionUID = 5376503635036180549L;

	private boolean isAscending = false;
	private String sortParameter;
	private String email;

	private Long size = null;

	public CustomerSearchDataProvider(final String sortParameter, final boolean isAscending, final String email)
	{
		this.isAscending = isAscending;
		this.sortParameter = sortParameter;
		this.email = email;
	}

	@Override
	public Iterator<? extends CustomerSummary> iterator(long first, long count)
	{
		Iterator<? extends CustomerSummary> iter = null;
		PaginatedResult<CustomerSummary> results = null;

		final SearchOptions searchOpts = new SearchOptions.Builder().
				sortProperty(sortParameter).ascending(isAscending).maxResults(Integer.valueOf(String.valueOf(count)))
				.firstResult(first).build();

		try
		{

			results = ServiceFactory.get().getCustomerService()
					.getPublisherCustomerSummaryByEmail(SessionUtils.getSession().getMerchantAccount().getMerchant().getId(),
							searchOpts, email, false);

			if (results != null)
			{
				iter = results.getResults().iterator();
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
		if (size != null)
		{
			return size;
		}

		try
		{

			size = ServiceFactory.get().getCustomerService()
					.getPublisherCustomerSummaryEmailCount(SessionUtils.getSession().getMerchantAccount().getMerchant().getId(), email);

		}
		catch (ServiceException e)
		{
			e.printStackTrace();
		}
		return size;
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
