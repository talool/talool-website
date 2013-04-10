package com.talool.website.models;

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
public class MerchantModel extends LoadableDetachableModel<Merchant>
{
	private static final Logger LOG = LoggerFactory.getLogger(MerchantModel.class);

	private static final long serialVersionUID = -1571731014724589519L;

	private Long merchantId;

	private Merchant merchant;

	public MerchantModel(final Long merchantId)
	{
		this.merchantId = merchantId;
	}

	@Override
	protected Merchant load()
	{

		if (merchant != null)
		{
			return merchant;
		}

		try
		{
			merchant = ServiceFactory.get().getTaloolService().getMerchantById(merchantId);
		}
		catch (ServiceException e)
		{
			LOG.error("problem loading merchant", e);
		}

		return merchant;
	}

}
