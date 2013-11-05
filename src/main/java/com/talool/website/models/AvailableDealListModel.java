package com.talool.website.models;

import java.util.List;
import java.util.UUID;

import org.apache.wicket.model.LoadableDetachableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.service.AnalyticService.AvailableDeal;
import com.talool.core.service.ServiceException;
import com.talool.service.ServiceFactory;

public class AvailableDealListModel extends LoadableDetachableModel<List<AvailableDeal>>
{
	private static final long serialVersionUID = -871001031643638887L;
	private static final Logger LOG = LoggerFactory.getLogger(AvailableDealListModel.class);
	private UUID _merchantId;

	@Override
	protected List<AvailableDeal> load()
	{

		List<AvailableDeal> deals = null;

		try
		{
			deals = ServiceFactory.get().getAnalyticService().getAvailableDeals(_merchantId);
		}
		catch (ServiceException se)
		{
			LOG.error("problem loading AvailableDeals", se);
		}

		return deals;
	}

	public void setMerchantId(final UUID id)
	{
		_merchantId = id;
	}

}
