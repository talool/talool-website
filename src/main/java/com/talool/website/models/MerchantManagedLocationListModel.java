package com.talool.website.models;

import java.util.List;

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
public class MerchantManagedLocationListModel extends
		LoadableDetachableModel<List<MerchantManagedLocation>>
{

	private static final long serialVersionUID = -6377845043575682681L;
	private static final Logger LOG = LoggerFactory.getLogger(MerchantManagedLocationListModel.class);
	private String _merchantId = null;

	@Override
	protected List<MerchantManagedLocation> load()
	{
		List<MerchantManagedLocation> locations = null;

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

	public void setMerchantId(final String id)
	{
		_merchantId = id;
	}

}
