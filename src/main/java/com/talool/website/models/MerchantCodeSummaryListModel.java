package com.talool.website.models;

import java.util.List;
import java.util.UUID;

import org.apache.wicket.model.LoadableDetachableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.SearchOptions;
import com.talool.core.service.ServiceException;
import com.talool.service.ServiceFactory;
import com.talool.stats.MerchantCodeSummary;

public class MerchantCodeSummaryListModel extends LoadableDetachableModel<List<MerchantCodeSummary>>
{

	private static final long serialVersionUID = -6415448310988721401L;
	private static final Logger LOG = LoggerFactory.getLogger(MerchantCodeSummaryListModel.class);

	private UUID _merchantId;

	@Override
	protected List<MerchantCodeSummary> load()
	{

		List<MerchantCodeSummary> codes = null;

		try
		{
			SearchOptions searchOptions = new SearchOptions.Builder().maxResults(1000).page(0).sortProperty("name")
					.ascending(true).build();
			codes = ServiceFactory.get().getTaloolService().getMerchantCodeSummariesForFundraiser(_merchantId, searchOptions);
		}
		catch (ServiceException se)
		{
			LOG.error("problem loading MerchantCodeGroups", se);
		}

		return codes;
	}

	public void setMerchantId(UUID id)
	{
		_merchantId = id;
	}

}
