package com.talool.website.models;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

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

	private UUID merchantId;

	@Override
	protected List<Merchant> load()
	{
		List<Merchant> merchants = null;

		try
		{
			if (merchantId == null)
			{
				merchants = ServiceFactory.get().getTaloolService().getAllMerchants();
			}
			else
			{
				merchants = ServiceFactory.get().getTaloolService().getMerchantsCreatedByMerchant(merchantId);
			}
		}
		catch (ServiceException e)
		{
			LOG.error("problem loading merchants", e);
		}

		return merchants;
	}

	public void setMerchantId(UUID id)
	{
		merchantId = id;
	}

	class MerchantComparator implements Comparator<Merchant>
	{
		@Override
		public int compare(Merchant m1, Merchant m2)
		{
			return m1.getName().compareTo(m2.getName());
		}
	}

}
