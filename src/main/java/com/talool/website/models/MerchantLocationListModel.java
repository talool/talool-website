package com.talool.website.models;

import java.util.List;
import java.util.UUID;

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
public class MerchantLocationListModel extends
		LoadableDetachableModel<List<MerchantLocation>>
{

	private static final long serialVersionUID = -6377845043575682681L;
	private static final Logger LOG = LoggerFactory.getLogger(MerchantLocationListModel.class);
	private UUID _merchantId = null;

	@Override
	protected List<MerchantLocation> load()
	{
		List<MerchantLocation> locations = null;

		try
		{
			if (_merchantId != null)
			{
				locations = ServiceFactory.get().getTaloolService().getLocationsForMerchant(_merchantId);
			}
		}
		catch (ServiceException e)
		{
			LOG.error("problem loading merchant locations", e);
		}

		return locations;
	}

	public void setMerchantId(final UUID id)
	{
		_merchantId = id;
	}

}
