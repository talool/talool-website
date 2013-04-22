package com.talool.website.models;

import java.util.UUID;

import org.apache.wicket.model.LoadableDetachableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.DealAcquire;
import com.talool.core.service.ServiceException;
import com.talool.service.ServiceFactory;

/**
 * 
 * @author dmccuen
 * 
 */
public class DealAcquireModel extends LoadableDetachableModel<DealAcquire>
{

	private static final long serialVersionUID = 5716763870469873256L;

	private static final Logger LOG = LoggerFactory.getLogger(DealAcquireModel.class);

	private UUID dealAcquireId;

	public DealAcquireModel(final UUID dealAcquireId)
	{
		this.dealAcquireId = dealAcquireId;
	}

	@Override
	protected DealAcquire load()
	{

		DealAcquire dealAcquire = null;

		try
		{
			dealAcquire = ServiceFactory.get().getTaloolService().getDealAcquire(dealAcquireId);
		}
		catch (ServiceException e)
		{
			LOG.error("problem loading deal", e);
		}

		return dealAcquire;
	}

}
