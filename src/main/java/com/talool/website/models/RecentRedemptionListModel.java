package com.talool.website.models;

import java.util.List;
import java.util.UUID;

import org.apache.wicket.model.LoadableDetachableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.service.AnalyticService.RecentRedemption;
import com.talool.core.service.ServiceException;
import com.talool.service.ServiceFactory;

public class RecentRedemptionListModel extends LoadableDetachableModel<List<RecentRedemption>>
{
	private static final long serialVersionUID = -871001031643638887L;
	private static final Logger LOG = LoggerFactory.getLogger(RecentRedemptionListModel.class);
	private UUID _merchantId;

	@Override
	protected List<RecentRedemption> load()
	{

		List<RecentRedemption> redemptions = null;

		try
		{
			redemptions = ServiceFactory.get().getAnalyticService().getRecentRedemptions(_merchantId);
		}
		catch (ServiceException se)
		{
			LOG.error("problem loading redemptions", se);
		}

		return redemptions;
	}

	public void setMerchantId(final UUID id)
	{
		_merchantId = id;
	}

}
