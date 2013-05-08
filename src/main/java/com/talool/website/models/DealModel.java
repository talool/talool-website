package com.talool.website.models;

import java.util.UUID;

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

	private UUID dealId;
	private boolean initColletions = false;

	public DealModel(final UUID dealId, boolean initCollections)
	{
		this.dealId = dealId;
		this.initColletions = initCollections;
	}

	public DealModel(final UUID dealId)
	{
		this.dealId = dealId;
	}

	@Override
	protected Deal load()
	{

		Deal deal = null;

		try
		{
			deal = ServiceFactory.get().getTaloolService().getDeal(dealId);

			if (initColletions)
			{
				ServiceFactory.get().getTaloolService().initialize(deal.getTags());
				ServiceFactory.get().getTaloolService().initialize(deal.getMerchant().getTags());
				ServiceFactory.get().getTaloolService().initialize(deal.getDealOffer());
			}
		}
		catch (ServiceException e)
		{
			LOG.error("problem loading deal", e);
		}

		return deal;
	}

}
