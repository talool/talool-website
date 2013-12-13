package com.talool.website.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.wicket.model.LoadableDetachableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.website.panel.message.MerchantMessage;

/**
 * 
 * @author dmccuen
 * 
 */
public class MerchantMessageListModel extends LoadableDetachableModel<List<MerchantMessage>>
{

	private static final long serialVersionUID = -1537678799039334997L;
	private static final Logger LOG = LoggerFactory.getLogger(MerchantMessageListModel.class);
	private UUID _merchantId = null;

	@Override
	protected List<MerchantMessage> load()
	{
		List<MerchantMessage> messages = new ArrayList<MerchantMessage>();

		MerchantMessage message;
		for (int i=0; i<10; i++)
		{
			message = new MerchantMessage(_merchantId.toString(), "Test Message", 10);
			messages.add(message);
		}

		return messages;
	}

	public void setMerchantId(final UUID id)
	{
		_merchantId = id;
	}

}
