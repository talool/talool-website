package com.talool.website.models;

import java.util.List;
import java.util.UUID;

import org.apache.wicket.model.LoadableDetachableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.SearchOptions;
import com.talool.core.service.ServiceException;
import com.talool.domain.PropertyCriteria;
import com.talool.domain.PropertyCriteria.Filter;
import com.talool.service.ServiceFactory;
import com.talool.stats.MerchantSummary;
import com.talool.stats.PaginatedResult;
import com.talool.utils.KeyValue;

/**
 * 
 * @author dmccuen
 * 
 */
public class FundraiserListModel extends LoadableDetachableModel<List<MerchantSummary>>
{
	private static final Logger LOG = LoggerFactory.getLogger(FundraiserListModel.class);

	private static final long serialVersionUID = -1571731014724589519L;

	private UUID publisherId;

	@Override
	protected List<MerchantSummary> load()
	{
		List<MerchantSummary> merchants = null;

		try
		{
			
			PropertyCriteria criteria = new PropertyCriteria();
			criteria.setFilters(Filter.equal(KeyValue.fundraiser, true));
			
			final SearchOptions searchOpts = new SearchOptions.Builder().
					sortProperty("name").ascending(true).maxResults(100).build();
			
			PaginatedResult<MerchantSummary> results = ServiceFactory.get().getTaloolService().getPublisherMerchantSummary(
					publisherId, searchOpts, criteria, true);
			
			merchants = results.getResults();
			
		}
		catch (ServiceException e)
		{
			LOG.error("problem loading fundraisers", e);
		}

		return merchants;
	}

	public void setPublisherId(UUID id)
	{
		publisherId = id;
	}

}
