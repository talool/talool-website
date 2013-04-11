package com.talool.website.models;

import org.apache.wicket.model.LoadableDetachableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.MerchantManagedLocation;
import com.talool.core.service.ServiceException;
import com.talool.service.ServiceFactory;

/**
 * 
 * @author dmccuen
 * 
 */
public class MerchantManagedLocationModel extends LoadableDetachableModel<MerchantManagedLocation>
{

	private static final long serialVersionUID = -458506447252990798L;

	private static final Logger LOG = LoggerFactory.getLogger(MerchantManagedLocationModel.class);

	private Long merchantLocationId;

	public MerchantManagedLocationModel(final Long id)
	{
		this.merchantLocationId = id;
	}

	@Override
	protected MerchantManagedLocation load()
	{
		MerchantManagedLocation merchantLocation = null;

		try
		{
			merchantLocation = ServiceFactory.get().getTaloolService().getMerchantLocationById(merchantLocationId);
		}
		catch (ServiceException e)
		{
			LOG.error("problem loading merchant location", e);
		}

		return merchantLocation;
	}

}
