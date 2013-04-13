package com.talool.website.models;

import java.util.List;

import org.apache.wicket.model.LoadableDetachableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.MerchantIdentity;
import com.talool.core.service.ServiceException;
import com.talool.service.ServiceFactory;

/**
 * 
 * @author clintz
 * 
 */
public class AvailableMerchantsListModel extends LoadableDetachableModel<List<MerchantIdentity>>
{
	private static final Logger LOG = LoggerFactory.getLogger(AvailableMerchantsListModel.class);
	private static final long serialVersionUID = 3057667212077774497L;
	private Long merchantAccountId;

	public AvailableMerchantsListModel(final Long merchantAccountId)
	{
		this.merchantAccountId = merchantAccountId;
	}

	@Override
	protected List<MerchantIdentity> load()
	{
		List<MerchantIdentity> mIds = null;

		try
		{
			mIds = ServiceFactory.get().getTaloolService()
					.getAuthorizedMerchantIdentities(merchantAccountId);
		}
		catch (ServiceException e)
		{
			LOG.error("problem loading merchant identities for merchantAccountId " + merchantAccountId, e);
		}

		return mIds;
	}
}
