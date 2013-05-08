package com.talool.website.models;

import java.util.List;
import java.util.UUID;

import org.apache.wicket.model.LoadableDetachableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.talool.core.DealOffer;
import com.talool.core.service.ServiceException;
import com.talool.service.ServiceFactory;
import com.talool.website.util.SessionUtils;

/**
 * 
 * @author dmccuen
 * 
 */
public class AvailableDealOffersListModel extends LoadableDetachableModel<List<DealOffer>>
{
	private static final long serialVersionUID = -1840804920962326496L;
	private static final Logger LOG = LoggerFactory.getLogger(AvailableDealOffersListModel.class);

	@Override
	protected List<DealOffer> load()
	{
		List<DealOffer> dealOffers = null;

		final UUID merchantId = SessionUtils.getSession().getMerchantAccount().getMerchant().getId();

		try
		{
			dealOffers = ServiceFactory.get().getTaloolService()
					.getAllRelatedDealsOffersForMerchantId(merchantId);
		}
		catch (ServiceException e)
		{
			LOG.error("problem loading merchant identities for merchantAccountId " + merchantId, e);
		}

		return dealOffers;
	}
	
	public boolean isEmpty()
	{
		List<DealOffer> dealOffers = load();
		return CollectionUtils.isEmpty(dealOffers);
	}
}
