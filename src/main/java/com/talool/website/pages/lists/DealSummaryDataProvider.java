package com.talool.website.pages.lists;

import java.util.Iterator;
import java.util.UUID;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.talool.core.SearchOptions;
import com.talool.core.service.ServiceException;
import com.talool.service.ServiceFactory;
import com.talool.stats.DealSummary;
import com.talool.stats.PaginatedResult;

/**
 * 
 * @author dmccuen
 * 
 */
public class DealSummaryDataProvider implements IDataProvider<DealSummary>
{
	private static final long serialVersionUID = 5376503635036180549L;

	private boolean isAscending = false;
	private String sortParameter;
	private UUID offerId;

	private Long size = null;

	public DealSummaryDataProvider(final UUID dealOfferId, final String sortParameter, final boolean isAscending)
	{
		this.isAscending = isAscending;
		this.sortParameter = sortParameter;
		this.offerId = dealOfferId;
	}

	@Override
	public Iterator<? extends DealSummary> iterator(long first, long count)
	{
		Iterator<? extends DealSummary> iter = null;
		PaginatedResult<DealSummary> results = null;

		final SearchOptions searchOpts = new SearchOptions.Builder().
				sortProperty(sortParameter).ascending(isAscending).maxResults(Integer.valueOf(String.valueOf(count)))
				.firstResult(first).build();

		try
		{
			results = ServiceFactory.get().getTaloolService().getDealSummary(offerId, searchOpts, true);

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
			size = ServiceFactory.get().getTaloolService().getDealSummaryCount(offerId);

		}
		catch (ServiceException e)
		{
			e.printStackTrace();
		}
		return size;
	}

	@Override
	public IModel<DealSummary> model(DealSummary object)
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
