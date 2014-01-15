package com.talool.website.panel.message;

import java.util.UUID;

import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.Merchant;
import com.talool.core.MerchantAccount;
import com.talool.core.service.ServiceException;
import com.talool.service.ServiceFactory;
import com.talool.website.models.MerchantMessageModel;
import com.talool.website.panel.BaseDefinitionPanel;
import com.talool.website.panel.SubmitCallBack;
import com.talool.website.util.SessionUtils;

public class MerchantMessagePanel extends BaseDefinitionPanel
{
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(MerchantMessagePanel.class);
	private boolean isNew;

	public MerchantMessagePanel(final String id, final UUID merchantId, final SubmitCallBack callback)
	{
		super(id, callback);

		Merchant merchant = null;
		try
		{
			merchant = ServiceFactory.get().getTaloolService().getMerchantById(merchantId);
		}
		catch (ServiceException se)
		{
			LOG.error("problem loading merchant", se);
		}

		MerchantMessage message = new MerchantMessage("foo", "", 10);
		setDefaultModel(Model.of(message));
		isNew = true;

	}

	public MerchantMessagePanel(final String id, final SubmitCallBack callback,
			final String merchantMessageId)
	{
		super(id, callback);
		setDefaultModel(new MerchantMessageModel(merchantMessageId));
		isNew = false;
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		// TODO
	}

	@SuppressWarnings("unchecked")
	@Override
	public CompoundPropertyModel<MerchantMessage> getDefaultCompoundPropertyModel()
	{
		return new CompoundPropertyModel<MerchantMessage>((IModel<MerchantMessage>) getDefaultModel());
	}

	@Override
	public String getObjectIdentifier()
	{
		MerchantMessage message = (MerchantMessage) form.getDefaultModelObject();
		return message.getMessageId();
	}

	@Override
	public void save() throws ServiceException
	{
		SessionUtils.successMessage("Successfully saved merchant message");
	}

	@Override
	public String getSaveButtonLabel()
	{
		return "Save Message";
	}


}
