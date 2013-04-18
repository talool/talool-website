package com.talool.website.models;

import java.util.List;

import org.apache.wicket.model.LoadableDetachableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.MerchantAccount;
import com.talool.core.service.ServiceException;
import com.talool.service.ServiceFactory;

/**
 * 
 * @author dmccuen
 * 
 */
public class MerchantAccountListModel extends LoadableDetachableModel<List<MerchantAccount>>
{

	private static final long serialVersionUID = -1537678799039334997L;
	private static final Logger LOG = LoggerFactory.getLogger(MerchantAccountListModel.class);
	private String _merchantId = null;

	@Override
	protected List<MerchantAccount> load()
	{
		List<MerchantAccount> accounts = null;

		try
		{
			if (_merchantId != null)
			{
				accounts = ServiceFactory.get().getTaloolService().getAccountsForMerchant(_merchantId);
			}
		}
		catch (ServiceException e)
		{
			LOG.error("problem loading merchants", e);
		}

		return accounts;
	}

	public void setMerchantId(final String id)
	{
		_merchantId = id;
	}

}
