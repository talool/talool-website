package com.talool.website.panel.merchant.definition;

import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.Merchant;
import com.talool.core.MerchantAccount;
import com.talool.core.service.ServiceException;
import com.talool.service.ServiceFactory;
import com.talool.website.models.MerchantAccountModel;
import com.talool.website.panel.BaseDefinitionPanel;
import com.talool.website.panel.SubmitCallBack;


public class MerchantAccountPanel extends BaseDefinitionPanel
{

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(MerchantAccountPanel.class);

	public MerchantAccountPanel(final String id, final Long merchantId, final SubmitCallBack callback)
	{
		super(id, callback, true);
		
		Merchant merchant = null;
		try {
			merchant = ServiceFactory.get().getTaloolService().getMerchantById(merchantId);
		} catch (ServiceException se) {
			LOG.error("problem loading merchant", se);
		}

		MerchantAccount account = domainFactory.newMerchantAccount(merchant);
		setDefaultModel(Model.of(account));
		
	}

	public MerchantAccountPanel(final String id, final SubmitCallBack callback, final Long merchantAccountId)
	{
		super(id, callback, false);
		setDefaultModel(new MerchantAccountModel(merchantAccountId));
	}
	
	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		
		form.add(new TextField<String>("roleTitle").setRequired(true));
		form.add(new TextField<String>("email").setRequired(true));
		// validate the passwords match
		FormComponent<String> pw1 = new PasswordTextField("password").setRequired(true);
		FormComponent<String> pw2 = new PasswordTextField("confirm",new PropertyModel<String>(this,"confirm")).setRequired(true);
		EqualPasswordInputValidator pwv = new EqualPasswordInputValidator(pw1,pw2);
		form.add(pw1);
		form.add(pw2);
		form.add(pwv);
		form.add(new CheckBox("allowDealCreation"));
	}

	@SuppressWarnings("unchecked")
	@Override
	public CompoundPropertyModel<MerchantAccount> getDefaultCompoundPropertyModel() {
		return new CompoundPropertyModel<MerchantAccount>((IModel<MerchantAccount>) getDefaultModel());
	}

	@Override
	public String getObjectIdentifier() {
		MerchantAccount account = (MerchantAccount) form.getDefaultModelObject();
		return account.getEmail();
	}

	@Override
	public void save() throws ServiceException {
		MerchantAccount account = (MerchantAccount) form.getDefaultModelObject();
		taloolService.save(account);
	}
	
	@Override
	public String getSaveButtonLabel() {
		return "Save Merchant Account";
	}
	
	private String confirm;

	public String getConfirm() {
		return confirm;
	}

	public void setConfirm(String confirm) {
		this.confirm = confirm;
	}

}
