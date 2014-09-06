package com.talool.website.pages.lists;

import java.util.Iterator;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.talool.core.Merchant;
import com.talool.core.SearchOptions;
import com.talool.core.service.ServiceException;
import com.talool.domain.PropertyCriteria;
import com.talool.domain.PropertyCriteria.Filter;
import com.talool.service.ServiceFactory;
import com.talool.stats.FundraiserSummary;
import com.talool.stats.PaginatedResult;
import com.talool.utils.KeyValue;
import com.talool.website.util.PermissionUtils;
import com.talool.website.util.SessionUtils;

/**
 * 
 * @author dmccuen
 * 
 */
public class FundraiserSummaryDataProvider implements IDataProvider<FundraiserSummary>
{
	private static final long serialVersionUID = 5376503635036180549L;

	private boolean isAscending = false;
	private String sortParameter;
	private UUID merchantId;
	private Long size = null;

	public FundraiserSummaryDataProvider(final String sortParameter, final boolean isAscending)
	{
		this.isAscending = isAscending;
		this.sortParameter = sortParameter;
	}

	@Override
	public Iterator<? extends FundraiserSummary> iterator(long first, long count)
	{
		
		Iterator<? extends FundraiserSummary> iter = null;
		PaginatedResult<FundraiserSummary> results = null;

		final SearchOptions searchOpts = new SearchOptions.Builder().
				sortProperty(sortParameter).ascending(isAscending).maxResults(Integer.valueOf(String.valueOf(count)))
				.firstResult(first).build();

		try
		{
			if (merchantId != null)
			{
				results = ServiceFactory.get().getTaloolService().getPublisherFundraiserSummaries(merchantId, searchOpts, true);
			}
			else
			{
				results = ServiceFactory.get().getTaloolService().getFundraiserSummaries(searchOpts, true);
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
			if (merchantId != null)
			{
				size = ServiceFactory.get().getTaloolService().getPublisherFundraiserSummaryCount(merchantId);
			}
			else
			{
				size = ServiceFactory.get().getTaloolService().getFundraiserSummaryCount();
			}

		}
		catch (ServiceException e)
		{
			e.printStackTrace();
		}
		return size;
	}

	@Override
	public IModel<FundraiserSummary> model(FundraiserSummary object)
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

	public void setMerchantId(UUID merchantId) {
		this.merchantId = merchantId;
	}

	@Override
	public void detach()
	{
		// TODO Auto-generated method stub
	}

}
