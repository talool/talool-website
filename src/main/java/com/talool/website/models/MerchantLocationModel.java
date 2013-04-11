package com.talool.website.models;

import org.apache.wicket.model.LoadableDetachableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.MerchantLocation;
import com.talool.core.service.ServiceException;
import com.talool.service.ServiceFactory;

/**
 * 
 * @author dmccuen
 * 
 */
public class MerchantLocationModel extends LoadableDetachableModel<MerchantLocation>
{

	private static final long serialVersionUID = -458506447252990798L;

	private static final Logger LOG = LoggerFactory.getLogger(MerchantLocationModel.class);

	private Long merchantLocationId;

	public MerchantLocationModel(final Long id)
	{
		this.merchantLocationId = id;
	}

	@Override
	protected MerchantLocation load()
	{
		MerchantLocation merchantLocation = null;

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
