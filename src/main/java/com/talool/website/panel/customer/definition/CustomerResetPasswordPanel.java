package com.talool.website.panel.customer.definition;

import java.util.UUID;

import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

import com.talool.core.Customer;
import com.talool.core.service.ServiceException;
import com.talool.website.models.CustomerModel;
import com.talool.website.panel.BaseDefinitionPanel;
import com.talool.website.panel.SubmitCallBack;
import com.talool.website.util.SessionUtils;

public class CustomerResetPasswordPanel extends BaseDefinitionPanel {

	private static final long serialVersionUID = -7185664671674236396L;

	public CustomerResetPasswordPanel(String id, SubmitCallBack callback, final UUID customerId) {
		super(id, callback);
		setDefaultModel(new CustomerModel(customerId));
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
		return new CompoundPropertyModel<Customer>((IModel<Customer>) getDefaultModel());
	}

	@Override
	public String getObjectIdentifier() {
		final Customer customer = (Customer) form.getDefaultModelObject();
		return customer.getEmail();
	}

	@Override
	public void save() throws ServiceException {
		final Customer customer = (Customer) form.getDefaultModelObject();
		taloolService.save(customer);
		SessionUtils.successMessage("Successfully changed the password for customer '", customer.getEmail(), "'");
	}

	@Override
	public String getSaveButtonLabel() {
		return "Change Password";
	}

}
