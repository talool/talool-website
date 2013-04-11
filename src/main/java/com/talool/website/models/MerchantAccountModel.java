package com.talool.website.models;

import org.apache.wicket.model.LoadableDetachableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.Customer;
import com.talool.core.MerchantAccount;
import com.talool.core.service.ServiceException;
import com.talool.service.ServiceFactory;

/**
 * 
 * @author dmccuen
 * 
 */
public class MerchantAccountModel extends LoadableDetachableModel<MerchantAccount>
{
	private static final long serialVersionUID = 5475505647334506135L;

	private static final Logger LOG = LoggerFactory.getLogger(MerchantAccountModel.class);

	private Long merchantAccountId;

	public MerchantAccountModel(final Long merchantAccountId)
	{
		this.merchantAccountId = merchantAccountId;
	}

	@Override
	protected MerchantAccount load()
	{

		MerchantAccount merchantAccount = null;

		try
		{
			merchantAccount = ServiceFactory.get().getTaloolService().getMerchantAccountById(merchantAccountId);
		}
		catch (ServiceException e)
		{
			LOG.error("problem loading merchant account", e);
		}

		return merchantAccount;
	}

}
