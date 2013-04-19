package com.talool.website.panel.merchant.definition;

import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

import com.talool.core.MerchantAccount;
import com.talool.core.service.ServiceException;
import com.talool.website.models.MerchantAccountModel;
import com.talool.website.panel.BaseDefinitionPanel;
import com.talool.website.panel.SubmitCallBack;
import com.talool.website.panel.customer.definition.SetPasswordPanel;
import com.talool.website.util.SessionUtils;

public class MerchantAccountResetPasswordPanel extends BaseDefinitionPanel {

	private static final long serialVersionUID = -5888013091682666960L;

	public MerchantAccountResetPasswordPanel(String id, SubmitCallBack callback, final Long merchantAccountId) {
		super(id, callback);
		setDefaultModel(new MerchantAccountModel(merchantAccountId));
	}
	
	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		SetPasswordPanel pwPanel = new SetPasswordPanel("passwordPanel");
		pwPanel.setDefaultModel(getDefaultModel());
		form.add(pwPanel);
	}

	@SuppressWarnings("unchecked")
	@Override
	public CompoundPropertyModel<?> getDefaultCompoundPropertyModel() {
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
		SessionUtils.successMessage("Successfully changed the merchant account password for '", account.getEmail(), "'");
	}

	@Override
	public String getSaveButtonLabel() {
		return "Change Password";
	}

}
