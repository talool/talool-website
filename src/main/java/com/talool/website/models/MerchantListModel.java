package com.talool.website.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.apache.wicket.model.LoadableDetachableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.Deal;
import com.talool.core.DealOffer;
import com.talool.core.Merchant;
import com.talool.core.SearchOptions;
import com.talool.core.SearchOptions.Builder;
import com.talool.core.service.ServiceException;
import com.talool.service.ServiceFactory;

/**
 * 
 * @author clintz
 * 
 */
public class MerchantListModel extends LoadableDetachableModel<List<Merchant>>
{
	private static final Logger LOG = LoggerFactory.getLogger(MerchantListModel.class);

	private static final long serialVersionUID = -1571731014724589519L;
	
	private static UUID merchantId;

	@Override
	protected List<Merchant> load()
	{
		List<Merchant> merchants = null;

		try
		{
			if (merchantId == null)
			{
				merchants = ServiceFactory.get().getTaloolService().getAllMerchants();
			}
			else
			{
				// TODO need to add a method to the service to do this work
				HashMap<UUID,Merchant> map = new HashMap<UUID, Merchant>();
				List<DealOffer> offers = ServiceFactory.get().getTaloolService().getDealOffersByMerchantId(merchantId);
				for (DealOffer offer : offers)
				{
					Builder sob = new SearchOptions.Builder();
					sob.sortProperty("name");
					sob.ascending(true);
					
					List<Deal> deals = ServiceFactory.get().getTaloolService().getDealsByDealOfferId(offer.getId(), null, null);
					for (Deal deal : deals)
					{
						map.put(deal.getMerchant().getId(), deal.getMerchant());
					}
				}
				merchants = new ArrayList<Merchant>();
				merchants.addAll(map.values());
				Collections.sort(merchants, new MerchantComparator());
			}
		}
		catch (ServiceException e)
		{
			LOG.error("problem loading merchants", e);
		}

		return merchants;
	}
	
	public void setMerchantId(UUID id)
	{
		merchantId = id;
	}
	
	class MerchantComparator implements Comparator<Merchant> 
	{
		@Override
	    public int compare(Merchant m1, Merchant m2) {
	        return m1.getName().compareTo(m2.getName());
	    }
	}

}
