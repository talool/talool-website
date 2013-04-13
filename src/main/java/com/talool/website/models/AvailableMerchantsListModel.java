package com.talool.website.models;

import java.util.List;

import org.apache.wicket.model.LoadableDetachableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.MerchantIdentity;
import com.talool.core.service.ServiceException;
import com.talool.service.ServiceFactory;
import com.talool.website.util.SessionUtils;

/**
 * 
 * @author clintz
 * 
 */
public class AvailableMerchantsListModel extends LoadableDetachableModel<List<MerchantIdentity>>
{
	private static final Logger LOG = LoggerFactory.getLogger(AvailableMerchantsListModel.class);
	private static final long serialVersionUID = 3057667212077774497L;

	@Override
	protected List<MerchantIdentity> load()
	{
		List<MerchantIdentity> mIds = null;

		final Long merchantId = SessionUtils.getSession().getMerchantAccount().getId();

		try
		{
			mIds = ServiceFactory.get().getTaloolService()
					.getAuthorizedMerchantIdentities(SessionUtils.getSession().getMerchantAccount().getId());
		}
		catch (ServiceException e)
		{
			LOG.error("problem loading merchant identities for merchantAccountId " + merchantId, e);
		}

		return mIds;
	}
}
