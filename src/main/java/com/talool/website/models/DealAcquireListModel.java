package com.talool.website.models;

import java.util.List;
import java.util.UUID;

import org.apache.wicket.model.LoadableDetachableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.DealAcquire;
import com.talool.core.service.ServiceException;
import com.talool.service.ServiceFactory;

public class DealAcquireListModel extends LoadableDetachableModel<List<DealAcquire>>
{
	private static final long serialVersionUID = -871001031643638887L;
	private static final Logger LOG = LoggerFactory.getLogger(DealAcquireListModel.class);
	private UUID _customerId;
	private boolean getRecent = false;

	@Override
	protected List<DealAcquire> load()
	{

		List<DealAcquire> deals = null;

		try
		{
			if (getRecent)
			{
				deals = ServiceFactory.get().getAnalyticService().getRecentRedemptions();
			}
			else
			{
				deals = ServiceFactory.get().getCustomerService().getDealAcquiresByCustomerId(_customerId);
			}
			
		}
		catch (ServiceException se)
		{
			LOG.error("problem loading DealAcquires", se);
		}

		return deals;
	}

	public void setCustomerId(final UUID id)
	{
		_customerId = id;
	}
	
	public void setGetRecent(boolean  b)
	{
		getRecent = b;
	}

}
