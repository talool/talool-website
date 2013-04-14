package com.talool.website.models;

import java.util.List;

import org.apache.wicket.model.LoadableDetachableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.DealAcquire;
import com.talool.core.service.ServiceException;
import com.talool.service.ServiceFactory;

public class DealAcquireListModel extends LoadableDetachableModel<List<DealAcquire>> {

	private static final long serialVersionUID = -871001031643638887L;
	private static final Logger LOG = LoggerFactory.getLogger(DealAcquireListModel.class);
	private long _customerId;

	@Override
	protected List<DealAcquire> load() {
		
		List<DealAcquire> deals = null;
		
		try 
		{
			deals = ServiceFactory.get().getTaloolService().getDealAcquiresByCustomerId(_customerId);
		} 
		catch (ServiceException se) 
		{
			LOG.error("problem loading DealAcquires", se);
		}
		
		return deals;
	}
	
	public void setCustomerId(long id)
	{
		_customerId = id;
	}

}
