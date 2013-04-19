package com.talool.website.models;

import java.util.List;
import java.util.UUID;

import org.apache.wicket.model.LoadableDetachableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.DealOffer;
import com.talool.core.service.ServiceException;
import com.talool.service.ServiceFactory;

public class DealOfferListModel extends LoadableDetachableModel<List<DealOffer>>
{

	private static final long serialVersionUID = -4489553425426553445L;
	private static final Logger LOG = LoggerFactory.getLogger(DealOfferListModel.class);
	private static enum LOAD_METHOD
	{
		MERHCANT, ALL
	}
	private UUID _merchantId;
	private LOAD_METHOD _method;

	@Override
	protected List<DealOffer> load()
	{

		List<DealOffer> books = null;

		try
		{
			if (_method == LOAD_METHOD.MERHCANT)
			{
				books = ServiceFactory.get().getTaloolService().getDealOffersByMerchantId(_merchantId);
			}
			else
			{
				books = ServiceFactory.get().getTaloolService().getDealOffers();
			}

		}
		catch (ServiceException e)
		{
			LOG.error("problem loading books (DealOffers)", e);
		}

		return books;
	}

	public void setMerchantId(final UUID id)
	{
		_merchantId = id;
		_method = LOAD_METHOD.MERHCANT;
	}

}
