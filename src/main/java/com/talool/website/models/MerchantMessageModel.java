package com.talool.website.models;

import org.apache.wicket.model.LoadableDetachableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.MerchantAccount;
import com.talool.core.service.ServiceException;
import com.talool.service.ServiceFactory;
import com.talool.website.panel.message.MerchantMessage;

/**
 * 
 * @author dmccuen
 * 
 */
public class MerchantMessageModel extends LoadableDetachableModel<MerchantMessage>
{
	private static final long serialVersionUID = 5475505647334506135L;

	private static final Logger LOG = LoggerFactory.getLogger(MerchantMessageModel.class);

	private String merchantMessageId;

	public MerchantMessageModel(final String merchantAccountId)
	{
		this.merchantMessageId = merchantAccountId;
	}

	@Override
	protected MerchantMessage load()
	{
		// TODO call the service and lookup the message by its ID
		MerchantMessage message = new MerchantMessage("foo","TEST", 10);
		return message;
	}

}
