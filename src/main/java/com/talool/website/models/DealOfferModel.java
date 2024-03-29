package com.talool.website.models;

import java.util.UUID;

import org.apache.wicket.model.LoadableDetachableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.DealOffer;
import com.talool.core.service.ServiceException;
import com.talool.service.ServiceFactory;

/**
 * 
 * @author dmccuen
 * 
 */
public class DealOfferModel extends LoadableDetachableModel<DealOffer>
{
	private static final long serialVersionUID = -6956910878696402522L;

	private static final Logger LOG = LoggerFactory.getLogger(DealOfferModel.class);

	private UUID dealOfferId;

	public DealOfferModel(final UUID dealOfferId)
	{
		this.dealOfferId = dealOfferId;
	}

	@Override
	protected DealOffer load()
	{

		DealOffer dealOffer = null;

		try
		{
			dealOffer = ServiceFactory.get().getTaloolService().getDealOffer(dealOfferId);
		}
		catch (ServiceException e)
		{
			LOG.error("problem loading customer", e);
		}

		return dealOffer;
	}

}
