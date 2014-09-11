package com.talool.website.models;

import java.util.Collections;
import java.util.Comparator;
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
	private UUID _customerId = null;
	private UUID _merchantId = null;
	private UUID _dealOfferId = null;
	private Integer maxResults = 50;
	//private String _trackingCode;

	@Override
	protected List<DealOfferPurchase> load()
	{

		List<DealOfferPurchase> books = null;

		try
		{
			if (_customerId != null)
			{
				books = ServiceFactory.get().getCustomerService()
						.getDealOfferPurchasesByCustomerId(_customerId);
			}
			else if (_merchantId != null)
			{
				books = ServiceFactory.get().getTaloolService()
							.getDealOfferPurchasesByMerchantId(_merchantId);
			}
			else if (_dealOfferId != null)
			{
				books = ServiceFactory.get().getTaloolService()
						.getDealOfferPurchasesByDealOfferId(_dealOfferId);
			}
		}
		catch (ServiceException e)
		{
			LOG.error("problem loading DealOfferPurchase list", e);
		}

		Collections.sort(books, new PurchaseComparator());
		if (books.size() > maxResults)
		{
			books = books.subList(0, maxResults);
		}
		return books;
	}

	public void setCustomerId(final UUID id)
	{
		_customerId = id;
	}
	
	public class PurchaseComparator implements Comparator<DealOfferPurchase> {
	    @Override
	    public int compare(DealOfferPurchase object1, DealOfferPurchase object2) {
	        return object1.getCreated().compareTo(object2.getCreated());
	    }
	}

	public void setMerchantId(UUID merchantId) {
		_merchantId = merchantId;
	}

	public void setDealOfferId(UUID dealOfferId) {
		_dealOfferId = dealOfferId;
	}
	
	public void setMaxResults(int max)
	{
		maxResults = max;
	}

}
