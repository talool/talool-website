package com.talool.website.panel.merchant.definition;

import java.util.UUID;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.Merchant;
import com.talool.core.MerchantAccount;
import com.talool.core.service.ServiceException;
import com.talool.service.ServiceFactory;
import com.talool.website.models.MerchantAccountModel;
import com.talool.website.panel.BaseDefinitionPanel;
import com.talool.website.panel.SubmitCallBack;
import com.talool.website.panel.customer.definition.SetPasswordPanel;
import com.talool.website.util.SessionUtils;

public class MerchantAccountPanel extends BaseDefinitionPanel
{
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(MerchantAccountPanel.class);
	private boolean isNew;

	public MerchantAccountPanel(final String id, final UUID merchantId, final SubmitCallBack callback)
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

		MerchantAccount account = domainFactory.newMerchantAccount(merchant);
		setDefaultModel(Model.of(account));
		isNew = true;

	}

	public MerchantAccountPanel(final String id, final SubmitCallBack callback,
			final Long merchantAccountId)
	{
		super(id, callback);
		setDefaultModel(new MerchantAccountModel(merchantAccountId));
		isNew = false;
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		form.add(new TextField<String>("roleTitle").setRequired(true));
		form.add(new TextField<String>("email").setRequired(true));
		
		if (isNew)  {
			SetPasswordPanel pwPanel = new SetPasswordPanel("passwordPanel");
			pwPanel.setDefaultModel(getDefaultModel());
			form.add(pwPanel);
		} else {
			form.add(new WebMarkupContainer("passwordPanel"));
		}
		
		form.add(new CheckBox("allowDealCreation"));
	}

	@SuppressWarnings("unchecked")
	@Override
	public CompoundPropertyModel<MerchantAccount> getDefaultCompoundPropertyModel()
	{
		return new CompoundPropertyModel<MerchantAccount>((IModel<MerchantAccount>) getDefaultModel());
	}

	@Override
	public String getObjectIdentifier()
	{
		MerchantAccount account = (MerchantAccount) form.getDefaultModelObject();
		return account.getEmail();
	}

	@Override
	public void save() throws ServiceException
	{
		MerchantAccount account = (MerchantAccount) form.getDefaultModelObject();
		taloolService.save(account);
		SessionUtils.successMessage("Successfully saved merchant account'", account.getEmail(), "'");
	}

	@Override
	public String getSaveButtonLabel()
	{
		return "Save Merchant Account";
	}


}
