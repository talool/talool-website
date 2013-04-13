package com.talool.website.models;

import org.apache.wicket.model.LoadableDetachableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.Deal;
import com.talool.core.service.ServiceException;
import com.talool.service.ServiceFactory;

/**
 * 
 * @author dmccuen
 * 
 */
public class DealModel extends LoadableDetachableModel<Deal>
{

	private static final long serialVersionUID = -6956910878696402522L;

	private static final Logger LOG = LoggerFactory.getLogger(DealModel.class);

	private Long dealId;

	public DealModel(final Long customerId)
	{
		this.dealId = customerId;
	}

	@Override
	protected Deal load()
	{

		Deal deal = null;

		try
		{
			deal = ServiceFactory.get().getTaloolService().getDeal(dealId);
		}
		catch (ServiceException e)
		{
			LOG.error("problem loading deal", e);
		}

		return deal;
	}

}
