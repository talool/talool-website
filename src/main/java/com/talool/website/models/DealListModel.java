package com.talool.website.models;

import java.util.List;

import org.apache.wicket.model.LoadableDetachableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.Deal;
import com.talool.core.service.ServiceException;
import com.talool.service.ServiceFactory;

public class DealListModel extends LoadableDetachableModel<List<Deal>> {

	private static final long serialVersionUID = -6415448310988721401L;
	private static final Logger LOG = LoggerFactory.getLogger(DealListModel.class);
	private static enum LOAD_METHOD {MERHCANT, CUSTOMER, DEAL_OFFER}
	private long _merchantId;
	private long _customerId;
	private long _dealOfferId;
	private LOAD_METHOD _method;

	@Override
	protected List<Deal> load() {
		
		List<Deal> deals = null;
		
		try 
		{
			if (_method == LOAD_METHOD.MERHCANT) 
			{
				deals = ServiceFactory.get().getTaloolService().getDealsByMerchantId(_merchantId);
			}
			else if (_method == LOAD_METHOD.CUSTOMER)
			{
				deals = ServiceFactory.get().getTaloolService().getDealsByCustomerId(_customerId);
			}
			else if (_method == LOAD_METHOD.DEAL_OFFER)
			{
				deals = ServiceFactory.get().getTaloolService().getDealsByDealOfferId(_dealOfferId);
			}
		} 
		catch (ServiceException se) 
		{
			LOG.error("problem loading deals", se);
		}
		
		return deals;
	}
	
	public void setMerchantId(long id)
	{
		_merchantId = id;
		_method = LOAD_METHOD.MERHCANT;
	}
	public void setCustomerId(long id)
	{
		_customerId = id;
		_method = LOAD_METHOD.CUSTOMER;
	}
	public void setDealOfferId(long id)
	{
		_dealOfferId = id;
		_method = LOAD_METHOD.DEAL_OFFER;
	}

}
