package com.talool.website.models;

import java.util.List;
import java.util.UUID;

import org.apache.wicket.model.LoadableDetachableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.DealOfferPurchase;
import com.talool.core.service.ServiceException;
import com.talool.service.ServiceFactory;

public class DealOfferPurchaseListModel extends LoadableDetachableModel<List<DealOfferPurchase>>
{
	private static final long serialVersionUID = -2313633510574402378L;
	private static final Logger LOG = LoggerFactory.getLogger(DealOfferPurchaseListModel.class);
	private UUID _customerId;

	@Override
	protected List<DealOfferPurchase> load()
	{

		List<DealOfferPurchase> books = null;

		try
		{
			books = ServiceFactory.get().getTaloolService()
					.getDealOfferPurchasesByCustomerId(_customerId);
		}
		catch (ServiceException e)
		{
			LOG.error("problem loading DealOfferPurchase list", e);
		}

		return books;
	}

	public void setCustomerId(final UUID id)
	{
		_customerId = id;
	}

}
