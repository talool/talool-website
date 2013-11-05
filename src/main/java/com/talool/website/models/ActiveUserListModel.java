package com.talool.website.models;

import java.util.List;
import java.util.UUID;

import org.apache.wicket.model.LoadableDetachableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.service.AnalyticService.ActiveUser;
import com.talool.core.service.ServiceException;
import com.talool.service.ServiceFactory;

public class ActiveUserListModel extends LoadableDetachableModel<List<ActiveUser>>
{
	private static final long serialVersionUID = -871001031643638887L;
	private static final Logger LOG = LoggerFactory.getLogger(ActiveUserListModel.class);
	private UUID _merchantId;

	@Override
	protected List<ActiveUser> load()
	{

		List<ActiveUser> users = null;

		try
		{
			users = ServiceFactory.get().getAnalyticService().getActiveUsers(_merchantId);
		}
		catch (ServiceException se)
		{
			LOG.error("problem loading ActiveUsers", se);
		}

		return users;
	}

	public void setMerchantId(final UUID id)
	{
		_merchantId = id;
	}

}
