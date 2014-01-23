package com.talool.website.pages;

import java.io.Serializable;
import java.util.Iterator;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.talool.core.SearchOptions;
import com.talool.core.service.ServiceException;
import com.talool.service.ServiceFactory;
import com.talool.stats.CustomerSummary;
import com.talool.stats.PaginatedResult;
import com.talool.website.pages.CustomerSearchDataProvider.CustomerSearchOpts.CustomerSearchType;
import com.talool.website.util.SessionUtils;

/**
 * 
 * @author clintz
 * 
 */
public class CustomerSearchDataProvider implements IDataProvider<CustomerSummary>
{
	private static final long serialVersionUID = 5376503635036180549L;
	private CustomerSearchOpts customerSearchOpts;
	private Long size = null;

	public static class CustomerSearchOpts implements Serializable
	{
		private static final long serialVersionUID = -4791731426403002313L;

		String email;
		String sortParameter;
		boolean isAscending;
		CustomerSearchType customerSearchType;

		public enum CustomerSearchType
		{
			PublisherCustomerSummaryByEmail("Email Address"), MostRecent("Most Recent");
			private String displayVal;

			private CustomerSearchType(String displayVal)
			{
				this.displayVal = displayVal;
			}

			public String getDisplayVal()
			{
				return displayVal;
			}
		}

		public CustomerSearchOpts(final CustomerSearchType searchType, final String sortParameter, final boolean isAscending)
		{
			this.sortParameter = sortParameter;
			this.isAscending = isAscending;
			this.customerSearchType = searchType;
		}

		public CustomerSearchOpts setEmail(String email)
		{
			this.email = email;
			return this;
		}

	}

	public CustomerSearchDataProvider(final CustomerSearchOpts customerSearchOpts)
	{
		this.customerSearchOpts = customerSearchOpts;
	}

	@Override
	public Iterator<? extends CustomerSummary> iterator(long first, long count)
	{
		Iterator<? extends CustomerSummary> iter = null;
		PaginatedResult<CustomerSummary> results = null;
		SearchOptions searchOpts = null;

		try
		{
			if (customerSearchOpts.customerSearchType == CustomerSearchType.PublisherCustomerSummaryByEmail)
			{
				searchOpts = new SearchOptions.Builder().
						sortProperty(customerSearchOpts.sortParameter).ascending(customerSearchOpts.isAscending)
						.maxResults(Integer.valueOf(String.valueOf(count)))
						.firstResult(first).build();

				results = ServiceFactory.get().getCustomerService()
						.getPublisherCustomerSummaryByEmail(SessionUtils.getSession().getMerchantAccount().getMerchant().getId(),
								searchOpts, customerSearchOpts.email, false);
			}
			else if (customerSearchOpts.customerSearchType == CustomerSearchType.MostRecent)
			{
				searchOpts = new SearchOptions.Builder().
						sortProperty(customerSearchOpts.sortParameter).ascending(customerSearchOpts.isAscending)
						.maxResults(Integer.valueOf(String.valueOf(count)))
						.firstResult(first).build();

				results = ServiceFactory.get().getCustomerService()
						.getPublisherCustomerSummary(SessionUtils.getSession().getMerchantAccount().getMerchant().getId(),
								searchOpts, false);
			}

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
			if (customerSearchOpts.customerSearchType == CustomerSearchType.PublisherCustomerSummaryByEmail)
			{
				size = ServiceFactory.get().getCustomerService()
						.getPublisherCustomerSummaryEmailCount(SessionUtils.getSession().getMerchantAccount().getMerchant().getId(), customerSearchOpts.email);
			}
			else if (customerSearchOpts.customerSearchType == CustomerSearchType.MostRecent)
			{
				size = ServiceFactory.get().getCustomerService()
						.getPublisherCustomerSummaryCount(SessionUtils.getSession().getMerchantAccount().getMerchant().getId());
			}

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
		return customerSearchOpts.isAscending;
	}

	public void setAscending(boolean isAscending)
	{
		customerSearchOpts.isAscending = isAscending;
	}

	public String getSortParameter()
	{
		return customerSearchOpts.sortParameter;
	}

	public void setSortParameter(String sortParameter)
	{
		customerSearchOpts.sortParameter = sortParameter;
	}

	@Override
	public void detach()
	{}

}
