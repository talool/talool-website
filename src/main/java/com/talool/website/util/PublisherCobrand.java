package com.talool.website.util;

import java.io.Serializable;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import com.talool.core.Merchant;
import com.talool.core.service.ServiceException;
import com.talool.service.ServiceConfig;
import com.talool.service.ServiceFactory;

public class PublisherCobrand implements Serializable {
	
	private static final long serialVersionUID = -8512739345275628862L;
	
	public String cobrandClassName;
	public String cobrandParamName;
	public PublisherOption publisherOption;
	public Merchant publisher;
	public long merchantAccountId;
	
	/*
	 * This is brittle.  But we're still figuring out what to do with co-brands.
	 * We need a way to connect a short url param to a merchant for cobranding.
	 * This is done through service.properties, where a merchant id is defined
	 * for the keys in the enum below.
	 * 
	 * If a merchant id for a PublisherOption.merchantKey doesn't match what is in the DB, 
	 * a ServiceException is thrown in init().
	 * 
	 */
	private static enum PublisherOption {
		Talool_Publishing("cobrand.publisher.talool.merchant.id","sales"), 
		Payback_Mobile("cobrand.publisher.payback.merchant.id","payback");
		
		private String merchantKey;
		private String paramName;

        private PublisherOption(String mKey, String pname) {
                this.merchantKey = mKey;
                this.paramName = pname;
        }
	}
	
	public PublisherCobrand(String cobrandClassName,
			String publisherParamName) {
		super();
		this.cobrandClassName = cobrandClassName;
		this.cobrandParamName = publisherParamName;
		
		for (PublisherOption pub : PublisherOption.values())
		{
			if (publisherParamName.equals(pub.paramName))
			{
				publisherOption = pub;
				break;
			}
		}
	}
	
	public void init() throws ServiceException
	{
		
		if (publisherOption == null)
		{
			throw new ServiceException("No Publisher mapped from param name: "+ cobrandParamName);
		}
		
		String id = ServiceConfig.get().getString(publisherOption.merchantKey);
		if (StringUtils.isEmpty(id))
		{
			throw new ServiceException("No property defined for key: "+ publisherOption.merchantKey);
		}
		
		try
		{
			publisher = ServiceFactory.get().getTaloolService().getMerchantById(UUID.fromString(id));
			merchantAccountId = publisher.getMerchantAccounts().iterator().next().getId();
		}
		catch (IllegalArgumentException e)
		{
			throw new ServiceException("Invalid UUID defined for property with key: "+ publisherOption.merchantKey, e);
		}
		catch (NullPointerException e)
		{
			throw new ServiceException("No merchant found for id: "+ id, e);
		}

	}
}
