package com.talool.website.models;

import java.util.UUID;

import org.apache.wicket.model.LoadableDetachableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.Merchant;
import com.talool.core.MerchantCodeGroup;
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

	private UUID merchantId;
	private String merchantCode;
	private boolean initColletions = false;

	public MerchantModel(final UUID merchantId, boolean initializeCollections)
	{
		this.merchantId = merchantId;
		this.initColletions = initializeCollections;
	}
	
	public MerchantModel(final String merchantCode, boolean initializeCollections)
	{
		this.merchantCode = merchantCode;
		this.initColletions = initializeCollections;
	}

	@Override
	protected Merchant load()
	{
		Merchant merchant = null;

		try
		{
			if (merchantId != null)
			{
				merchant = ServiceFactory.get().getTaloolService().getMerchantById(merchantId);
			}
			else
			{
				MerchantCodeGroup mcg = ServiceFactory.get().getTaloolService().getMerchantCodeGroupForCode(merchantCode);
				if (mcg!=null)
				{
					merchant = mcg.getMerchant();
				}
			}
			
			if (initColletions)
			{
				ServiceFactory.get().getTaloolService().initialize(merchant.getLocations());
				ServiceFactory.get().getTaloolService().initialize(merchant.getMerchantAccounts());
			}
		}
		catch (ServiceException e)
		{
			LOG.error("problem loading merchant", e);
		}
		catch (NullPointerException e)
		{
			//LOG.error("problem loading merchant", e);
		}

		return merchant;
	}

}
