package com.talool.website.pages.lists;

import java.util.Iterator;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.talool.core.SearchOptions;
import com.talool.core.service.ServiceException;
import com.talool.service.ServiceFactory;
import com.talool.stats.MerchantSummary;
import com.talool.stats.PaginatedResult;
import com.talool.website.service.PermissionService;
import com.talool.website.util.SessionUtils;

/**
 * 
 * @author dmccuen
 * 
 */
public class MerchantSummaryDataProvider implements IDataProvider<MerchantSummary>
{
	private static final long serialVersionUID = 5376503635036180549L;

	private boolean isAscending = false;
	private String sortParameter;

	private Long size = null;

	public MerchantSummaryDataProvider(final String sortParameter, final boolean isAscending)
	{
		this.isAscending = isAscending;
		this.sortParameter = sortParameter;
	}

	@Override
	public Iterator<? extends MerchantSummary> iterator(long first, long count)
	{
		Iterator<? extends MerchantSummary> iter = null;
		PaginatedResult<MerchantSummary> results = null;

		final SearchOptions searchOpts = new SearchOptions.Builder().
				sortProperty(sortParameter).ascending(isAscending).maxResults(Integer.valueOf(String.valueOf(count)))
				.firstResult(first).build();

		try
		{
			if (PermissionService.get().canViewAllCustomers(SessionUtils.getSession().getMerchantAccount().getEmail()))
			{
				results = ServiceFactory.get().getTaloolService().getMerchantSummary(searchOpts, true);
			}
			else
			{
				results = ServiceFactory.get().getTaloolService().getPublisherMerchantSummary(
						SessionUtils.getSession().getMerchantAccount().getMerchant().getId(), searchOpts, true);
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
			if (PermissionService.get().canViewAllCustomers(SessionUtils.getSession().getMerchantAccount().getEmail()))
			{
				size = ServiceFactory.get().getTaloolService().getMerchantSummaryCount();
			}
			else
			{
				size = ServiceFactory.get().getTaloolService()
						.getPublisherMerchantSummaryCount(SessionUtils.getSession().getMerchantAccount().getMerchant().getId());
			}

		}
		catch (ServiceException e)
		{
			e.printStackTrace();
		}
		return size;
	}

	@Override
	public IModel<MerchantSummary> model(MerchantSummary object)
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
