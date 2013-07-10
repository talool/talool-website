package com.talool.website.models;

import java.util.List;

import org.apache.wicket.model.LoadableDetachableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.Merchant;
import com.talool.core.service.ServiceException;
import com.talool.service.ServiceFactory;

/**
 * 
 * @author clintz
 * 
 */
public class MerchantListModel extends LoadableDetachableModel<List<Merchant>>
{
	private static final Logger LOG = LoggerFactory.getLogger(MerchantListModel.class);

	private static final long serialVersionUID = -1571731014724589519L;

	@Override
	protected List<Merchant> load()
	{
		List<Merchant> merchants = null;

		try
		{
			merchants = ServiceFactory.get().getTaloolService().getAllMerchants();
		}
		catch (ServiceException e)
		{
			LOG.error("problem loading merchants", e);
		}

		return merchants;
	}

}
