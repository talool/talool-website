package com.talool.website.models;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.apache.wicket.model.LoadableDetachableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.talool.core.Deal;
import com.talool.core.DealOffer;
import com.talool.core.Merchant;
import com.talool.core.MerchantAccount;
import com.talool.core.service.ServiceException;
import com.talool.core.service.TaloolService;
import com.talool.service.ServiceConfig;
import com.talool.service.ServiceFactory;
import com.talool.utils.KeyValue;
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
	private UUID _merchantId;
	private UUID _taloolPublisherId;
	private DealOffer _currentDealOffer;
	
	public AvailableDealOffersListModel(Deal deal)
	{
		super();
		_currentDealOffer = deal.getDealOffer();
		_merchantId = deal.getMerchant().getId();
		try
		{
			long id = ServiceConfig.get().getTaloolPublisherMerchantAccountId();
			MerchantAccount t = ServiceFactory.get().getTaloolService().getMerchantAccountById(id);
			_taloolPublisherId = t.getMerchant().getId();
		}
		catch (ServiceException se)
		{
			LOG.error("Failed to get the Talool Publisher");
		}
	}

	@Override
	protected List<DealOffer> load()
	{
		List<DealOffer> dealOffers = null;

		Merchant m = SessionUtils.getSession().getMerchantAccount().getMerchant();
		final UUID merchantId = m.getId();
		final boolean isPublisher = m.getProperties().getAsBool(KeyValue.publisher);
		
		try
		{
			// by default we use the deal offers associated with the logged in user
			TaloolService taloolService = ServiceFactory.get().getTaloolService();
			dealOffers = taloolService.getAllRelatedDealsOffersForMerchantId(merchantId);
			
			// if the logged in merchant isn't a publisher, we also add the offers from Talool
			if (!isPublisher && _taloolPublisherId != null && !_merchantId.equals(_taloolPublisherId))
			{
				List<DealOffer> moreDealOffers = taloolService.getDealOffersByMerchantId(_taloolPublisherId);
				if (!CollectionUtils.isEmpty(moreDealOffers))
				{
					dealOffers.addAll(moreDealOffers);
				}
			}
			
			// we also add the offers from the merchant associated with the deal
			if (_merchantId != null && !_merchantId.equals(merchantId))
			{
				List<DealOffer> moreDealOffers = taloolService.getDealOffersByMerchantId(_merchantId);
				if (!CollectionUtils.isEmpty(moreDealOffers))
				{
					dealOffers.addAll(moreDealOffers);
				}
			}
			
			// we also add the current offer on the deal
			// TODO see why CollectionUtils didn't help here
			if (_currentDealOffer != null)
			{
				Iterator<DealOffer> i = dealOffers.iterator();
				DealOffer d;
				Boolean hasCurrentDealOffer = false;
				while(i!=null && i.hasNext())
				{
					d = i.next();
					if (d.getId().equals(_currentDealOffer.getId())) 
					{
						hasCurrentDealOffer=true;
						break;
					}
				}
				if (!hasCurrentDealOffer)
				{
					dealOffers.add(_currentDealOffer);
				}
				
			}
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
	
	public void addMerchantId(UUID id) {
		_merchantId = id;
	}
	
	public void addDealOffer(DealOffer d) {
		_currentDealOffer = d;
	}
}
